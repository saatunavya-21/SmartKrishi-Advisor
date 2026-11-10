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

        StringBuilder adviceBuilder = new StringBuilder();

        if ("Rice".equalsIgnoreCase(crop)) {
            adviceBuilder.append("Paddy/Rice: Practice Alternate Wetting and Drying (AWD) rather than continuous deep flooding to save up to 30% water while maintaining yields. Maintain 2-5 cm shallow water during flowering stage. ");
        } else if ("Cotton".equalsIgnoreCase(crop)) {
            adviceBuilder.append("Cotton: Highly sensitive to waterlogging. Critical irrigation stages are squaring, flowering, and boll development. Drip irrigation or alternate furrow irrigation is recommended. ");
        } else if ("Maize".equalsIgnoreCase(crop)) {
            adviceBuilder.append("Maize: Critical water requirement occurs during tasseling and silking. Moisture stress during this period can reduce yield drastically. Avoid water stagnation. ");
        } else if ("Groundnut".equalsIgnoreCase(crop)) {
            adviceBuilder.append("Groundnut: Key watering stages are flowering, peg penetration, and pod development. Use sprinkler or light furrow irrigation. ");
        } else {
            adviceBuilder.append("General Advisory: Irrigate during morning or evening hours to reduce evaporation losses. ");
        }

        if ("Low".equalsIgnoreCase(waterAvailability)) {
            adviceBuilder.append("With limited water availability, employ micro-irrigation (drip/sprinkler), mulching with crop residue, and moisture conservation practices.");
        } else if ("Sandy Soil".equalsIgnoreCase(soilType)) {
            adviceBuilder.append("Sandy soils have low water holding capacity; provide frequent, light irrigations to prevent deep percolation loss.");
        } else if ("Clay Soil".equalsIgnoreCase(soilType)) {
            adviceBuilder.append("Clay soils retain moisture for longer durations; ensure adequate drainage channels to avoid root rot and waterlogging.");
        } else {
            adviceBuilder.append("Monitor soil moisture at 15-30 cm root zone before initiating the next irrigation cycle.");
        }

        model.addAttribute("crop", crop);
        model.addAttribute("soilType", soilType);
        model.addAttribute("waterAvailability", waterAvailability);
        model.addAttribute("advice", adviceBuilder.toString());

        return "irrigation";
    }
}
