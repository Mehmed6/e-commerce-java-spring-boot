package com.doganmehmet.app.request;

import com.doganmehmet.app.enums.AddressTYPE;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {

    @NotBlank(message = "Username cannot be empty!")
    private String username;
    @NotBlank(message = "FirstName cannot be empty!")
    private String firstName;
    @NotBlank(message = "LastName cannot be empty!")
    private String lastName;
    @NotBlank(message = "Email cannot be empty!")
    private String email;
    @NotBlank(message = "Username cannot be empty!")
    private String phone;
    @NotBlank(message = "Registration address cannot be empty!")
    private String registrationAddress;
    @NotBlank(message = "Zip code cannot be empty!")
    private String zipCode;
    @NotBlank(message = "City cannot be empty!")
    private String city;
    @NotBlank(message = "Country cannot be empty!")
    private String country;

    @NotNull(message = "Address type cannot be empty!")
    @Enumerated(EnumType.STRING)
    private AddressTYPE addressType;
}
