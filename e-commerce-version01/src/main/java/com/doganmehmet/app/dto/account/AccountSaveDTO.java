package com.doganmehmet.app.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountSaveDTO {
    @NotBlank(message = "Username cannot be empty or null!")
    private String username;
    @NotBlank(message = "Password cannot be empty or null!")
    private String password;
    @NotBlank(message = "AccountNo cannot be empty or null!")
    private String accountNo;
    @PositiveOrZero(message = "Balance must greater than or equal to 0")
    private BigDecimal balance;
    @NotBlank(message = "Iban cannot be empty or null!")
    private String iban;

}
