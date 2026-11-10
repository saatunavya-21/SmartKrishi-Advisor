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
        String recommendation;

        if (ph < 5.5) {
            condition = "Strongly Acidic Soil (pH < 5.5)";
            recommendation = "Apply agricultural lime (calcium carbonate) or dolomite to raise soil pH. Enhance organic matter through farmyard manure.";
        } else if (ph <= 6.5) {
            condition = "Moderately Acidic to Near-Neutral Soil (pH 5.5 - 6.5)";
            recommendation = "Good nutrient availability for most cereal and pulse crops. Maintain organic carbon with compost.";
        } else if (ph <= 7.5) {
            condition = "Optimal Neutral Soil (pH 6.5 - 7.5)";
            recommendation = "Ideal balanced soil condition. Maximum nutrient absorption efficiency for phosphorus, nitrogen, and micronutrients.";
        } else if (ph <= 8.5) {
            condition = "Moderately Alkaline / Calcareous Soil (pH 7.5 - 8.5)";
            recommendation = "Apply agricultural gypsum and organic green manure. Ensure zinc and iron micronutrient foliar application.";
        } else {
            condition = "Strongly Alkaline / Sodic Soil (pH > 8.5)";
            recommendation = "Requires soil reclamation using gypsum, pyrite, and deep tillage with leaching. Grow salt-tolerant varieties.";
        }

        model.addAttribute("soilType", soilType);
        model.addAttribute("ph", ph);
        model.addAttribute("nitrogen", nitrogen);
        model.addAttribute("phosphorus", phosphorus);
        model.addAttribute("potassium", potassium);
        model.addAttribute("condition", condition);
        model.addAttribute("recommendation", recommendation);

        return "soil-analysis";
    }
}
