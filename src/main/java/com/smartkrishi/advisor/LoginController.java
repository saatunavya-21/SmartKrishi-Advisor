

package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Farmer;
import com.smartkrishi.advisor.repository.FarmerRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class LoginController {

    @Autowired
    private FarmerRepository farmerRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginFarmer(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Farmer farmer = farmerRepository.findByEmail(email)
                .orElse(null);

        if (farmer != null &&
                passwordEncoder.matches(password, farmer.getPassword())) {

            session.setAttribute("farmerId", farmer.getId());
            session.setAttribute("farmerName", farmer.getName());

            return "redirect:/dashboard";
        }

        model.addAttribute("message",
                "Invalid email or password!");

        return "login";
    }

    @GetMapping("/logout")
    public String logoutFarmer(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
