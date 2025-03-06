package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.cartitem.CartItemDTOS;
import com.doganmehmet.app.mapper.ICartItemMapper;
import com.doganmehmet.app.request.AddToCartRequest;
import com.doganmehmet.app.service.CartService;
import com.doganmehmet.app.service.SecurityControl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController(JSONBeanName.JSON_CART_CONTROLLER)
@RequestMapping("json/api/cart")
public class CartController {
    private final CartService m_cartService;
    private final ICartItemMapper m_cartItemMapper;
    private final SecurityControl m_securityControl;

    public CartController(CartService cartService, ICartItemMapper cartItemMapper, SecurityControl securityControl)
    {
        m_cartService = cartService;
        m_cartItemMapper = cartItemMapper;
        m_securityControl = securityControl;
    }

    @PostMapping("/add/cart")
    public CartItemDTOS addToCart(@Valid @RequestBody AddToCartRequest request)
    {
        m_securityControl.checkTokenUserMatch(request.getUsername());
        return m_cartItemMapper.toCartItemDTOS(m_cartService.
                addToCart(request.getUsername(), request.getProductId(), request.getQuantity()));
    }

    @GetMapping("/show/my-cart")
    public CartItemDTOS showCart(@RequestParam String username)
    {
        m_securityControl.checkTokenUserMatch(username);
        return m_cartItemMapper.toCartItemDTOS(m_cartService.getCartItems(username));
    }

    @DeleteMapping("/delete/from/cart")
    public CartItemDTOS deleteProductFromCart(@RequestParam String username,@RequestParam Long productId)
    {
        m_securityControl.checkTokenUserMatch(username);
        return m_cartItemMapper.toCartItemDTOS(m_cartService.deleteProductFromCart(username, productId));
    }

    @DeleteMapping("/clear/cart")
    public void clearCart(@RequestParam String username)
    {
        m_securityControl.checkTokenUserMatch(username);
        m_cartService.clearCart(username);
    }
}
