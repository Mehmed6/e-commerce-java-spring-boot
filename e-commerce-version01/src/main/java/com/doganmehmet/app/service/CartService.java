package com.doganmehmet.app.service;


import com.doganmehmet.app.entity.CartItem;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.repository.IProductRepository;
import com.doganmehmet.app.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {
    private final Map<String, List<CartItem>> userCart = new HashMap<>();
    private final IProductRepository m_productRepository;
    private final IUserRepository m_userRepository;

    public CartService(IProductRepository productRepository, IUserRepository userRepository)
    {
        m_productRepository = productRepository;
        m_userRepository = userRepository;
    }

    public List<CartItem> addToCart(String username, Long productId, int quantity)
    {
        m_userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        var product = m_productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(MyError.PRODUCT_NOT_FOUND));

        if (product.getStock() < quantity)
            throw new ApiException(MyError.INSUFFICIENT_STOCK);

        try {
            var cartItems = userCart.computeIfAbsent(username, ci -> new ArrayList<>());

            cartItems.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst()
                    .ifPresentOrElse(
                            cartItem -> cartItem.setQuantity(cartItem.getQuantity() + quantity),
                            () -> cartItems.add(new CartItem(productId, product.getName(), quantity))
                    );

            return getCartItems(username);
        }
        catch (Exception ex) {
            throw new ApiException(MyError.GENERAL_ERROR, ex.getMessage());
        }

    }

    public List<CartItem> getCartItems(String username)
    {
        m_userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        return userCart.get(username) == null ? new ArrayList<>() : userCart.get(username);
    }

    public List<CartItem> deleteProductFromCart(String username, Long productId)
    {
        m_userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(MyError.USER_NOT_FOUND));

        var cartItems = getCartItems(username);

        if (cartItems.isEmpty())
            return new ArrayList<>();

        cartItems.removeIf(cartItem -> cartItem.getProductId().equals(productId));

        return cartItems;
    }

    public void clearCart(String username)
    {
        userCart.remove(username);
    }

}
