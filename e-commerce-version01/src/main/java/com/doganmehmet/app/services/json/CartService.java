package com.doganmehmet.app.services.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.entity.CartItem;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.exception.MyError;
import com.doganmehmet.app.repositories.IProductRepository;
import com.doganmehmet.app.repositories.IUserRepository;
import com.doganmehmet.app.services.SecurityControl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(JSONBeanName.JSON_CART_SERVICE)
public class CartService {
    private final Map<String, List<CartItem>> userCart = new HashMap<>();
    private final IProductRepository m_productRepository;
    private final IUserRepository m_userRepository;
    private final SecurityControl m_securityControl;

    public CartService(IProductRepository productRepository, IUserRepository userRepository, SecurityControl securityControl)
    {
        m_productRepository = productRepository;
        m_userRepository = userRepository;
        m_securityControl = securityControl;
    }

    public List<CartItem> addToCart(String username, Long productId, int quantity)
    {
        var user = m_userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new ApiException(MyError.USER_NOT_FOUND);

        m_securityControl.checkTokenUserMatch(username);

        var product = m_productRepository.findById(productId);

        if (product.isEmpty())
            throw new ApiException(MyError.PRODUCT_NOT_FOUND);

        if (product.get().getStock() < quantity)
            throw new ApiException(MyError.INSUFFICIENT_STOCK);

        try {
//            var cartItems = userCart.getOrDefault(username, new ArrayList<>());
//            var item = cartItems.stream().filter(ci -> ci.getProductId().equals(productId)).findFirst();
//            if (item.isPresent())
//                item.get().setQuantity(item.get().getQuantity() + quantity);
//            else
//                cartItems.add(new CartItem(productId, product.get().getName(), quantity));
//
//            userCart.put(username, cartItems);
//            return "Success";
            var cartItems = userCart.computeIfAbsent(username, ci -> new ArrayList<>());

            cartItems.stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst()
                    .ifPresentOrElse(
                            cartItem -> cartItem.setQuantity(cartItem.getQuantity() + quantity),
                            () -> cartItems.add(new CartItem(productId, product.get().getName(), quantity))
                    );

            return getCartItems(username);
        }
        catch (Exception ignored) {
            throw new ApiException(MyError.GENERAL_ERROR);
        }

    }

    public List<CartItem> getCartItems(String username)
    {
        var user = m_userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new ApiException(MyError.USER_NOT_FOUND);

        m_securityControl.checkTokenUserMatch(username);

        return userCart.get(username) == null ? new ArrayList<>() : userCart.get(username);
    }

    public List<CartItem> deleteProductFromCart(String username, Long productId)
    {
        m_securityControl.checkTokenUserMatch(username);

        var cartItems = getCartItems(username);

        if (cartItems.isEmpty())
            return new ArrayList<>();

        cartItems.removeIf(cartItem -> cartItem.getProductId().equals(productId));

        return cartItems;
    }

    public void clearCart(String username)
    {
        m_securityControl.checkTokenUserMatch(username);

        userCart.remove(username);
    }
}
