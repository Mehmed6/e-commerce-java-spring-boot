package com.doganmehmet.app.controller.thymeleaf;


import com.doganmehmet.app.bean.thymeleaf.ThymeleafBeanName;
import com.doganmehmet.app.entity.CardInfo;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.repositories.IAddressRepository;
import com.doganmehmet.app.repositories.ICardInfoRepository;
import com.doganmehmet.app.repositories.IUserRepository;
import com.doganmehmet.app.request.CardInfoRequest;
import com.doganmehmet.app.services.CartService;
import com.doganmehmet.app.services.OrderService;
import com.doganmehmet.app.services.PaymentService;
import com.doganmehmet.app.services.thymeleaf.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller(ThymeleafBeanName.THYMELEAF_SHOPPING_CONTROLLER)
@RequestMapping("public/")
public class ShoppingController {
    private final CartService m_cartService;
    private final ProductService m_productService;
    private final OrderService m_orderService;
    private final IUserRepository m_userRepository;
    private final ICardInfoRepository m_cardInfoRepository;
    private final PaymentService m_paymentService;
    private final IAddressRepository m_addressRepository;


    public ShoppingController(CartService cartService, ProductService productService, OrderService orderService, IUserRepository userRepository, ICardInfoRepository cardInfoRepository, PaymentService paymentService, IAddressRepository addressRepository)
    {
        m_cartService = cartService;
        m_productService = productService;
        m_orderService = orderService;
        m_userRepository = userRepository;
        m_cardInfoRepository = cardInfoRepository;
        m_paymentService = paymentService;
        m_addressRepository = addressRepository;
    }

    private CardInfo checkCardIfExist(CardInfoRequest request)
    {
        if (m_cardInfoRepository.existsByCardNumber(request.getCardNumber()))
            return m_cardInfoRepository.findByCardNumber(request.getCardNumber());

        var card = new CardInfo();
        card.setCardNumber(request.getCardNumber());
        card.setCardHolderName(request.getCardHolderName());
        card.setExpiryMonth(request.getExpiryMonth());
        card.setExpiryYear(request.getExpiryYear());
        card.setCvv(request.getCvv());

        var user = m_userRepository.findByUsername(request.getUsername())
                .orElseThrow( () -> new ApiException(MyError.USER_NOT_FOUND));

        card.setUser(user);
        return m_cardInfoRepository.save(card);
    }

    @GetMapping("/cart/show/my-cart")
    public String showCart()
    {
        return "shopping/my-cart";
    }

    @GetMapping("/cart/add/my-cart")
    public String showAddForm(Model model)
    {
        model.addAttribute("products", m_productService.findAll());
        return "/shopping/addCart";
    }

    @PostMapping("/cart/add/my-cart")
    public String addMyCart(@RequestParam("username") String username,
                            @RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            HttpSession session,
                            Model model)
    {
        try {
            m_cartService.addToCart(username, productId, quantity);
        }
        catch (ApiException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "shopping/my-cart";
        }

        var cartItems = m_cartService.getCartItems(username);
        session.setAttribute("username", username);
        session.setAttribute("cartItems", cartItems);
        model.addAttribute("cartItems", cartItems);
        return "shopping/my-cart";
    }

    @PostMapping("/cart/delete/from/cart")
    public String deleteFromCart(@RequestParam("productId") Long productId, HttpSession session, Model model)
    {
        var username = (String) session.getAttribute("username");

        try {
            m_cartService.deleteProductFromCart(username, productId);
        }
        catch (ApiException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }
        model.addAttribute("cartItems", m_cartService.getCartItems(username));
        return "shopping/my-cart";
    }

    @GetMapping("/order/show/my-orders")
    public String showOrder(Model model)
    {
        model.addAttribute("orders", m_orderService.findAll());
        return "shopping/my-orders";
    }

    @GetMapping("/order/create/order")
    public String createOrder()
    {
        return "shopping/createOrder";
    }

    @PostMapping("order/create/order")
    public String createOrder(@RequestParam String username, @RequestParam String password, HttpSession session, Model model)
    {
        session.setAttribute("username", username);
        session.setAttribute("password", password);

        try {
            m_orderService.creatOrder(username, password);
        }
        catch (ApiException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "shopping/createOrder";
        }

        return "redirect:/public/order/show/my-orders";
    }

    @PostMapping("/order/cancel")
    public String deleteOrder(@RequestParam("orderId") Long orderId)
    {
        m_orderService.deleteOrderById(orderId);
        return "redirect:/public/order/show/my-orders";
    }

    @GetMapping("/pay")
    public String pay(@RequestParam("orderId") Long orderId, HttpSession session, Model model)
    {
        session.setAttribute("orderId", orderId);
        model.addAttribute("addresses", m_addressRepository.findAll());
        return "payment/paymentPage";
    }

    @PostMapping("pay")
    public String pay(@RequestParam Long addressId, @ModelAttribute CardInfoRequest request, HttpSession session, Model model , HttpServletRequest servletRequest)
    {
        var message = "";
        try {
            var orderId = (Long) session.getAttribute("orderId");
            var username = m_orderService.findOrderById(orderId).getUser().getUsername();
            request.setUsername(username);
            var card = checkCardIfExist(request);
            message = m_paymentService.makePayment(addressId, card.getCardInfoId(), orderId , servletRequest);
        }
        catch (ApiException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "redirect:/public/order/show/my-orders";
        }

        model.addAttribute("message", message);
        return "payment/paymentPage";
    }

}
