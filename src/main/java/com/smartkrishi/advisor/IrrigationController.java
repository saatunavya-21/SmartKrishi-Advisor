package com.smartkrishi.advisor;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IrrigationController {

    @GetMapping("/irrigation")
    public String showIrrigationForm(HttpSession session) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        return "irrigation";
    }

    @PostMapping("/irrigation")
    public String giveIrrigationAdvice(
            @RequestParam String crop,
            @RequestParam String soilType,
            @RequestParam String waterAvailability,
            Model model,
            HttpSession session) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        String advice;

        if (waterAvailability.equals("Low")) {
            advice = "Use water carefully. Consider drip irrigation and check soil moisture before watering.";
        } else if (soilType.equals("Sandy Soil")) {
            advice = "Sandy soil drains quickly. Check soil moisture regularly and use smaller, appropriately timed watering sessions.";
        } else if (soilType.equals("Clay Soil")) {
            advice = "Clay soil holds water longer. Avoid watering when the soil is already wet.";
        } else {
            advice = "Check soil moisture regularly and water according to the crop's growth stage and local weather.";
        }

        model.addAttribute("crop", crop);
        model.addAttribute("soilType", soilType);
        model.addAttribute("waterAvailability", waterAvailability);
        model.addAttribute("advice", advice);

        return "irrigation";
    }
}