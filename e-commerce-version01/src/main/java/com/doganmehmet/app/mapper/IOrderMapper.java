package com.doganmehmet.app.mapper;

import com.doganmehmet.app.dto.order.OrderDTO;
import com.doganmehmet.app.dto.order.OrderDTOS;
import com.doganmehmet.app.dto.orderItem.OrderItemDTO;
import com.doganmehmet.app.entity.Order;
import com.doganmehmet.app.entity.OrderItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(implementationName = "OrderMapperImpl", componentModel = "spring")
public interface IOrderMapper {

    @Mapping(target = "username", source = "user.username")
    OrderDTO toOrderDTO(Order order);

    List<OrderDTO> toOrderDTOList(List<Order> orders);

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "total_price", source = "price")
    OrderItemDTO toOrderItemDTO(OrderItems orderItems);

    default OrderDTOS toOrderDTOS(List<OrderDTO> orderDTOS)
    {
        var dtos = new OrderDTOS();
        dtos.setLastOrders(orderDTOS);
        return dtos;
    }
}
