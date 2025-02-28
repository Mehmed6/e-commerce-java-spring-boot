package com.doganmehmet.app.dto.cardinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardInfoDTO {

    private String cardHolderName;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvc;
}
