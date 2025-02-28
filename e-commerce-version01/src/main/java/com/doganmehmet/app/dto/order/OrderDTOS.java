package com.doganmehmet.app.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDTOS {
    List<OrderDTO> lastOrders;
}
