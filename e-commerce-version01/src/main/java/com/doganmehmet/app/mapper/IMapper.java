package com.doganmehmet.app.mapper;

import com.doganmehmet.app.dto.user.UserDTO;
import com.doganmehmet.app.entity.User;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Primary;

@Mapper(implementationName = "MapperImpl", componentModel = "spring")
@Primary
public interface IMapper {

    UserDTO toUserDTO(User user);
}
