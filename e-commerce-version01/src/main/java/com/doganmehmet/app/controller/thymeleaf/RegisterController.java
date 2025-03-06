package com.doganmehmet.app.controller.thymeleaf;

import com.doganmehmet.app.bean.thymeleaf.ThymeleafBeanName;
import com.doganmehmet.app.exception.ApiException;
import com.doganmehmet.app.request.RegisterRequest;
import com.doganmehmet.app.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;


@Controller(ThymeleafBeanName.THYMELEAF_REGISTER_CONTROLLER)
public class RegisterController {

    private final RegisterService m_registerService;

    public RegisterController(RegisterService registerService)
    {
        m_registerService = registerService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model)
    {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register/my-register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterRequest request,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model)
    {
        if (bindingResult.hasErrors())
            return "register/my-register";

        try {
            m_registerService.register(request);
        } catch (ApiException ex) {
            model.addAttribute("errorMessage", ex.getMyError().getErrorMessage());
            return "register/my-register";
        }

        redirectAttributes.addFlashAttribute("message", "Registration successful! You can log in now.");

        return "redirect:/auth/login";
    }
}
