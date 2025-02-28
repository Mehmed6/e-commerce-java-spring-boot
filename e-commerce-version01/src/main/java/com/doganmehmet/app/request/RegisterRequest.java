package com.doganmehmet.app.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username can not be blank")
    private String username;
    @NotBlank(message = "Password can not be blank")
    private String password;
    @NotBlank(message = "Email can not be blank")
    private String email;

}
