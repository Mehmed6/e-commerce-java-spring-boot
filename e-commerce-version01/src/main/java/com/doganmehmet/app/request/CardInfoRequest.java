package com.doganmehmet.app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardInfoRequest {

    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Holder name cannot be empty")
    private String cardHolderName;
    @NotBlank(message = "Card number cannot be empty")
    private String cardNumber;
    @NotBlank(message = "Expiry month cannot be empty")
    private String expiryMonth;
    @NotBlank(message = "Expiry Year cannot be empty")
    private String expiryYear;
    @NotBlank(message = "CVV cannot be empty")
    private String cvv;
}
