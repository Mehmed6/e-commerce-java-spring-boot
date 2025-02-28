package com.doganmehmet.app.controller.json;

import com.doganmehmet.app.bean.json.JSONBeanName;
import com.doganmehmet.app.response.LoginResponse;
import com.doganmehmet.app.services.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(JSONBeanName.JSON_REFRESH_TOKEN_CONTROLLER)
@RequestMapping("/refreshToken/json")
public class RefreshTokenController {

    private final RefreshTokenService m_refreshTokenService;

    public RefreshTokenController(RefreshTokenService refreshTokenService)
    {
        m_refreshTokenService = refreshTokenService;
    }

    @PostMapping
    public LoginResponse refreshToken(@RequestBody String refreshToken)
    {
        return m_refreshTokenService.refreshToken(refreshToken);
    }
}
