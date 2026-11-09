package com.smartkrishi.advisor;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SoilAnalysisController {

    @GetMapping("/soil-analysis")
    public String showSoilForm(HttpSession session) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        return "soil-analysis";
    }

    @PostMapping("/soil-analysis")
    public String analyzeSoil(
            @RequestParam String soilType,
            @RequestParam double ph,
            @RequestParam double nitrogen,
            @RequestParam double phosphorus,
            @RequestParam double potassium,
            HttpSession session,
            Model model) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        String condition;

        if (ph < 5.5) {
            condition = "Acidic Soil";
        } else if (ph <= 7.5) {
            condition = "Near-Neutral Soil";
        } else {
            condition = "Alkaline Soil";
        }

        model.addAttribute("soilType", soilType);
        model.addAttribute("ph", ph);
        model.addAttribute("nitrogen", nitrogen);
        model.addAttribute("phosphorus", phosphorus);
        model.addAttribute("potassium", potassium);
        model.addAttribute("condition", condition);

        return "soil-analysis";
    }
}