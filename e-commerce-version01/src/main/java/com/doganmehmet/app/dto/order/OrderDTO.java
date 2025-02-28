package com.doganmehmet.app.dto.order;

import com.doganmehmet.app.dto.orderItem.OrderItemDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private String username;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> orderItems;
}
