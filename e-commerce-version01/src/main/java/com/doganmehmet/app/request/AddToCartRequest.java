package com.doganmehmet.app.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {

    @NotBlank(message = "Username cannot be empty or null!")
    String username;
    @PositiveOrZero(message = "ProductId can not be negative!")
    Long productId;
    @Positive(message = "Quantity must be positive!")
    int quantity;
}
