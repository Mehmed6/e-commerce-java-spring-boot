package com.doganmehmet.app.dto.user;

import com.doganmehmet.app.enums.Roles;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class UserDTO {

    private String username;

    private String email;

    private Roles roles;

    private String created_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

}