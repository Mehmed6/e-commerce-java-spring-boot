package com.doganmehmet.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentRequest {

    @NotBlank(message = "Address ID cannot be empty")
    private Long addressId;
    @NotBlank(message = "Card ID cannot be empty")
    private Long cardId;
    @NotBlank(message = "Order ID cannot be empty")
    private Long orderId;
}
