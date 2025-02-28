package com.doganmehmet.app.dto.account;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountDTO {
    private String username;
    private String accountNo;
    private BigDecimal balance;
    private String iban;
}
