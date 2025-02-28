package com.doganmehmet.app.controller.thymeleaf;

import com.doganmehmet.app.bean.thymeleaf.ThymeleafBeanName;
import com.doganmehmet.app.response.LoginResponse;
import com.doganmehmet.app.services.RefreshTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(ThymeleafBeanName.THYMELEAF_REFRESH_TOKEN_CONTROLLER)
public class RefreshTokenController {

    private final RefreshTokenService m_refreshTokenService;

    public RefreshTokenController(RefreshTokenService refreshTokenService)
    {
        m_refreshTokenService = refreshTokenService;
    }

    @PostMapping("/refreshToken")
    public LoginResponse refreshToken(@RequestBody String refreshToken)
    {
        return m_refreshTokenService.refreshToken(refreshToken);
    }
}
