package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.dto.user.UserDTO;
import com.doganmehmet.app.request.RegisterRequest;
import com.doganmehmet.app.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController(JSONBeanName.JSON_REGISTER_CONTROLLER)
@RequestMapping("/register/json")
public class RegisterController {

    private final RegisterService m_registerService;

    public RegisterController(RegisterService registerService)
    {
        m_registerService = registerService;
    }

    @PostMapping
    public UserDTO register(@Valid @RequestBody RegisterRequest registerRequest)
    {
        return m_registerService.register(registerRequest);
    }
}
