package com.doganmehmet.app.controller.thymeleaf;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {

        var usernameObj = session.getAttribute("username");
        var jwtTokenObj = session.getAttribute("jwtToken");
        var refreshTokenObj = session.getAttribute("refreshToken");

        if (usernameObj == null || jwtTokenObj == null || refreshTokenObj == null) {
            model.addAttribute("error", "Giriş yapmadınız veya oturum süresi doldu!");
            return "redirect:/my-login";
        }

        String username = usernameObj.toString();
        String jwtToken = jwtTokenObj.toString();
        String refreshToken = refreshTokenObj.toString();

        model.addAttribute("username", username);
        model.addAttribute("jwtToken", jwtToken);
        model.addAttribute("refreshToken", refreshToken);

        return "dashboard";
    }

}
