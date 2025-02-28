package com.doganmehmet.app.mapper;

import com.doganmehmet.app.dto.cartitem.CartItemDTO;
import com.doganmehmet.app.dto.cartitem.CartItemDTOS;
import com.doganmehmet.app.entity.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationName = "CartItemMapperImpl", componentModel = "spring")
public interface ICartItemMapper {

    CartItemDTO toCartItemDTO(CartItem cartItem);

    List<CartItemDTO> toCartItemDTOList(List<CartItem> cartItems);

    default CartItemDTOS toCartItemDTOS(List<CartItem> cartItems)
    {
        var dtos = new CartItemDTOS();
        dtos.setCartItems(toCartItemDTOList(cartItems));
        return dtos;
    }
}
