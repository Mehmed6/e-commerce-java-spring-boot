package com.doganmehmet.app.service;


import com.doganmehmet.app.entity.Order;
import com.doganmehmet.app.entity.User;
import com.doganmehmet.app.enums.Status;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.repository.*;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final IAddressRepository m_addressRepository;
    private final IOrderRepository m_orderRepository;
    private final ICardInfoRepository m_cardInfoRepository;
    private final IProductRepository m_productRepository;
    private final IAccountRepository m_accountRepository;
    private final SecurityControl m_securityControl;

    @Value("${iyzico.apiKey}")
    private String m_apiKey;

    @Value("${iyzico.secretKey}")
    private String m_apiSecret;

    @Value("${iyzico.baseUrl}")
    private String m_baseUrl;

    public PaymentService(IAddressRepository addressRepository, IOrderRepository orderRepository, ICardInfoRepository cardInfoRepository, IProductRepository productRepository, IAccountRepository accountRepository, SecurityControl securityControl)
    {
        m_addressRepository = addressRepository;
        m_orderRepository = orderRepository;
        m_cardInfoRepository = cardInfoRepository;
        m_productRepository = productRepository;
        m_accountRepository = accountRepository;
        m_securityControl = securityControl;
    }

    private Address setAddressForUser(Long addressId)
    {
        var shippingAddress = new Address();
        var address = m_addressRepository.findByAddressId(addressId);

        shippingAddress.setContactName(address.getFirstName() + " " + address.getLastName());
        shippingAddress.setCity(address.getCity());
        shippingAddress.setCountry(address.getCountry());
        shippingAddress.setAddress(address.getRegistrationAddress());
        shippingAddress.setZipCode(address.getZipCode());
        return shippingAddress;
    }

    private PaymentCard setCardInfoForUser(Long cardId)
    {
        var userCard = m_cardInfoRepository.findById(cardId)
                .orElseThrow(() -> new ApiException(MyError.CARD_NOT_FOUND));

        var paymentCard = new PaymentCard();

        paymentCard.setCardHolderName(userCard.getCardHolderName());
        paymentCard.setCardNumber(userCard.getCardNumber());
        paymentCard.setExpireMonth(userCard.getExpiryMonth());
        paymentCard.setExpireYear(userCard.getExpiryYear());
        paymentCard.setCvc(userCard.getCvv());

        return paymentCard;
    }

    private Buyer setBuyerForUser(Long addressId, HttpServletRequest request)
    {
        var addressInfo = m_addressRepository.findById(addressId)
                .orElseThrow(() -> new ApiException(MyError.ADDRESS_NOT_FOUND));

        var buyer = new Buyer();
        buyer.setName(addressInfo.getFirstName());
        buyer.setSurname(addressInfo.getLastName());
        buyer.setEmail(addressInfo.getEmail());
        buyer.setCity(addressInfo.getCity());
        buyer.setCountry(addressInfo.getCountry());
        buyer.setGsmNumber(addressInfo.getPhone());
        buyer.setRegistrationAddress(addressInfo.getRegistrationAddress());
        buyer.setIp(request.getRemoteAddr());
        buyer.setZipCode(addressInfo.getZipCode());
        buyer.setIdentityNumber("123456789");
        return buyer;

    }

    public List<BasketItem> setItemsForUser(Long orderId)
    {
        var basketItems = new ArrayList<BasketItem>();
        var totalPrice = BigDecimal.ZERO;

        var order = m_orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(MyError.ORDER_NOT_FOUND));

        if (order.getStatus().equals(Status.COMPLETED))
            throw new ApiException(MyError.ORDER_ALREADY_COMPLETED);

        if (order.getStatus().equals(Status.CANCELED))
            throw new ApiException(MyError.ORDER_CANCELED);

        for (var orderItem : order.getOrderItems()) {
            var item = new BasketItem();
            var product = orderItem.getProduct();
            var quantity = orderItem.getQuantity();

            if (product.getStock() < quantity)
                throw new ApiException(MyError.INSUFFICIENT_STOCK);

            product.setStock(product.getStock() - quantity);
            m_productRepository.save(product);

            var price = product.getPrice().multiply(new BigDecimal(quantity));
            item.setId("BI" + product.getProductId() + ": " + product.getName());
            item.setName(product.getName());
            item.setCategory1(product.getCategory().getName());
            item.setCategory2(product.getCategory().getName());
            item.setPrice(price);
            item.setItemType(BasketItemType.PHYSICAL.name());
            basketItems.add(item);
            totalPrice = totalPrice.add(price);
        }

        return basketItems;
    }

    private BigDecimal checkBalance(User user, Order order)
    {
        var account = user.getAccount();

        if (account == null)
            throw new ApiException(MyError.ACCOUNT_NOT_FOUND);

        var balance = account.getBalance();
        var totalPrice = order.getTotalPrice();

        if (totalPrice.compareTo(balance) > 0)
            throw new ApiException(MyError.INSUFFICIENT_BALANCE);

        account.setBalance(account.getBalance().subtract(totalPrice));
        m_accountRepository.save(account);

        return totalPrice;
    }

    @Transactional
    public String makePayment(Long addressId, Long cardId, Long orderId, HttpServletRequest request)
    {
        var order = m_orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(MyError.ORDER_NOT_FOUND));

        var user = order.getUser();

        var options = new Options();
        options.setApiKey(m_apiKey);
        options.setSecretKey(m_apiSecret);
        options.setBaseUrl(m_baseUrl);

        var paymentRequest = new CreatePaymentRequest();
        var paidPrice = checkBalance(user, order);
        var basketItems = setItemsForUser(orderId);
        var buyer = setBuyerForUser(addressId, request);

        buyer.setId("B" + user.getUserId() + "-" + user.getUsername());
        paymentRequest.setLocale("tr");
        paymentRequest.setConversationId("987654321");

        paymentRequest.setBillingAddress(setAddressForUser(addressId));
        paymentRequest.setShippingAddress(setAddressForUser(addressId));
        paymentRequest.setPaymentCard(setCardInfoForUser(cardId));
        paymentRequest.setBuyer(buyer);
        paymentRequest.setBasketItems(basketItems);

        paymentRequest.setPrice(order.getTotalPrice());
        paymentRequest.setPaidPrice(paidPrice);

        var payment = Payment.create(paymentRequest, options);

        if ("success".equals(payment.getStatus())) {
            order.setStatus(Status.COMPLETED);
            m_orderRepository.save(order);
            return "Success, payment ID: " + payment.getPaymentId();
        } else
            throw new ApiException(MyError.GENERAL_ERROR, payment.getErrorMessage());

    }
}
