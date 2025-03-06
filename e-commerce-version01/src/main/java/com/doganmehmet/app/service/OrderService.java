package com.doganmehmet.app.service;

import com.doganmehmet.app.dto.order.OrderDTO;
import com.doganmehmet.app.dto.order.OrderDTOS;
import com.doganmehmet.app.entity.Order;
import com.doganmehmet.app.entity.OrderItems;
import com.doganmehmet.app.enums.Status;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.mapper.IOrderMapper;
import com.doganmehmet.app.repository.IOrderRepository;
import com.doganmehmet.app.repository.IProductRepository;
import com.doganmehmet.app.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final IOrderRepository m_orderRepository;
    private final SecurityControl m_securityControl;
    private final IProductRepository m_productRepository;
    private final IUserRepository m_userRepository;
    private final CartService m_cartService;
    private final IOrderMapper m_orderMapper;

    public OrderService(IOrderRepository orderRepository,
                        SecurityControl securityControl,
                        IProductRepository productRepository,
                        IUserRepository userRepository,
                        CartService cartService,
                        IOrderMapper orderMapper)
    {
        m_orderRepository = orderRepository;
        m_securityControl = securityControl;
        m_productRepository = productRepository;
        m_userRepository = userRepository;
        m_cartService = cartService;
        m_orderMapper = orderMapper;
    }

    @Transactional
    public OrderDTO creatOrder(String username, String password)
    {
        var user = m_securityControl.isUser(username, password);
        var cartItems = m_cartService.getCartItems(username);

        if (cartItems.isEmpty())
            throw new ApiException(MyError.CART_EMPTY);

        List<OrderItems> orderItemsList = new ArrayList<>();
        var totalPrice = BigDecimal.ZERO;

        var order = new Order();
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setTotalPrice(totalPrice);

        for (var item : cartItems) {
            var product = m_productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ApiException(MyError.PRODUCT_NOT_FOUND));

            var orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            totalPrice = totalPrice.add(orderItem.getPrice());
            orderItemsList.add(orderItem);
        }

        order.setOrderItems(orderItemsList);
        order.setTotalPrice(totalPrice);

        order = m_orderRepository.save(order);
        m_cartService.clearCart(username);

        return m_orderMapper.toOrderDTO(order);
    }


    @Transactional
    public OrderDTO completeOrder(Long orderId)
    {
        var order = m_orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(MyError.ORDER_NOT_FOUND));

        if (order.getStatus().equals(Status.COMPLETED))
            throw new ApiException(MyError.ORDER_ALREADY_COMPLETED);

        for (var item : order.getOrderItems()) {
            var product = item.getProduct();
            var quantity = item.getQuantity();

            if (product.getStock() < quantity)
                throw new ApiException(MyError.INSUFFICIENT_STOCK);

            product.setStock(product.getStock() - quantity);
            m_productRepository.save(product);
        }

        var user = order.getUser();

        if (user.getAccount() == null)
            throw new ApiException(MyError.ACCOUNT_NOT_FOUND);

        if (order.getTotalPrice().compareTo(user.getAccount().getBalance()) > 0) {
            order.setStatus(Status.CANCELED);
            m_orderRepository.save(order);
            throw new ApiException(MyError.INSUFFICIENT_BALANCE);
        }

        order.setStatus(Status.COMPLETED);
        user.getAccount().setBalance(user.getAccount().getBalance().subtract(order.getTotalPrice()));
        m_userRepository.save(user);

        return m_orderMapper.toOrderDTO(m_orderRepository.save(order));
    }

    public Order findOrderById(Long orderId)
    {
        return m_orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(MyError.ORDER_NOT_FOUND));
    }
    public List<OrderDTO> findOrderByUsername(String username)
    {
        return m_orderMapper.toOrderDTOList(m_orderRepository
                .findByUser(m_userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND))));
    }

    public List<Order> findAll()
    {
        return m_orderRepository.findAll();
    }

    public OrderDTOS deleteOrderById(String username, Long orderId)
    {
        var order = m_orderRepository.findById(orderId);

        if (order.isEmpty())
            throw new ApiException(MyError.ORDER_NOT_FOUND);

        if (order.get().getStatus().equals(Status.COMPLETED))
            throw new ApiException(MyError.ORDER_ALREADY_COMPLETED);

        m_orderRepository.deleteById(orderId);
        return m_orderMapper.toOrderDTOS(findOrderByUsername(username));
    }

    public OrderDTOS deleteOrderByStatus(String username, Status status)
    {
        var orders = m_orderRepository.findAllByStatus(status);
        m_orderRepository.deleteAll(orders);

        return m_orderMapper.toOrderDTOS(findOrderByUsername(username));
    }

    public void deleteOrderById(Long orderId)
    {
        m_orderRepository.deleteById(orderId);
    }
    public void deleteAll()
    {
        m_orderRepository.deleteAll();
    }
}
