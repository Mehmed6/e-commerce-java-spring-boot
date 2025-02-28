package com.doganmehmet.app.mapper;

import com.doganmehmet.app.dto.jwtToken.JwtTokenDTO;
import com.doganmehmet.app.entity.JwtToken;
import org.mapstruct.Mapper;

@Mapper(implementationName = "JwtTokenMapperImpl", componentModel = "spring")
public interface IJwtTokenMapper {

    default JwtTokenDTO entityToDto(JwtToken jwtToken)
    {
        var dto = new JwtTokenDTO();
        dto.setJwt_token(jwtToken.getJwtToken());
        dto.setRefresh_token(jwtToken.getRefreshToken().getRefreshToken());
        dto.setUser(jwtToken.getUser().getUsername());

        return dto;
    }
}
