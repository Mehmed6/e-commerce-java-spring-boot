package com.doganmehmet.app.dto.jwtToken;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtTokenDTO {

    private String jwt_token;
    private String refresh_token;
    private String user;

}
