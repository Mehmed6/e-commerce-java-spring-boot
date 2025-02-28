package com.doganmehmet.app.dto.address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String registrationAddress;
    private String zipCode;
    private String city;
    private String country;
    private String addressType;
}
