package com.doganmehmet.app.dto.orderItem;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDTO {
    private String productName;
    private Integer quantity;
    private BigDecimal total_price;
}
