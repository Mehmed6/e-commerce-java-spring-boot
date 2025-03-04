package com.doganmehmet.app.controller.thymeleaf;

import com.doganmehmet.app.bean.thymeleaf.ThymeleafBeanName;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.request.LoginRequest;
import com.doganmehmet.app.services.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller(ThymeleafBeanName.THYMELEAF_LOGIN_CONTROLLER)
@RequestMapping("/auth/login")
public class LoginController {

    private final LoginService m_loginService;

    public LoginController(LoginService loginService)
    {
        m_loginService = loginService;
    }

    @GetMapping
    public String showLoginForm(Model model, @ModelAttribute("message") String message)
    {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login/my-login";
    }

    @PostMapping
    public String login(@Valid LoginRequest loginRequest,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {

        if (bindingResult.hasErrors())
            return "login/my-login";

        try {
            var token = m_loginService.login(loginRequest);

            session.setAttribute("jwtToken", token.getJwtToken());
            session.setAttribute("refreshToken", token.getRefreshToken().getRefreshToken());
            session.setAttribute("username", loginRequest.getUsername());

            return "redirect:/dashboard";
        } catch (ApiException e) {
            model.addAttribute("errorMessage", e.getMyError().getErrorMessage());
            return "login/my-login";
        }
    }

}
