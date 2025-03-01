package com.doganmehmet.app.controller.thymeleaf;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model, RedirectAttributes redirectAttributes, HttpSession session)
    {

        var usernameObj = session.getAttribute("username");
        var jwtTokenObj = session.getAttribute("jwtToken");
        var refreshTokenObj = session.getAttribute("refreshToken");

        if (usernameObj == null || jwtTokenObj == null || refreshTokenObj == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You have not logged in or your session has expired!");
            return "redirect:/auth/login";
        }

        String username = usernameObj.toString();
        String jwtToken = jwtTokenObj.toString();
        String refreshToken = refreshTokenObj.toString();

        model.addAttribute("username", username);
        model.addAttribute("jwtToken", jwtToken);
        model.addAttribute("refreshToken", refreshToken);

        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout()
    {
        return "login/my-login";
    }

}
