package com.smartkrishi.advisor;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CropRecommendationController {

    @GetMapping("/crop-recommendation")
    public String showCropForm(HttpSession session, Model model) {
        model.addAttribute("isLoggedIn", session.getAttribute("farmerId") != null);
        return "crop-recommendation";
    }

    @PostMapping("/crop-recommendation")
    public String recommendCrops(
            @RequestParam String soilType,
            @RequestParam String season,
            @RequestParam String waterAvailability,
            Model model,
            HttpSession session) {

        model.addAttribute("isLoggedIn", session.getAttribute("farmerId") != null);
        List<String> crops = new ArrayList<>();

        if ("Black Soil".equalsIgnoreCase(soilType)) {
            crops.add("Cotton");
            crops.add("Soybean");
            crops.add("Sorghum (Jowar)");
            if ("Kharif".equalsIgnoreCase(season)) {
                crops.add("Pigeon Pea (Arhar)");
            }
        }
        else if ("Red Soil".equalsIgnoreCase(soilType)) {
            crops.add("Groundnut");
            crops.add("Finger Millet (Ragi)");
            crops.add("Pigeon Pea");
            if ("High".equalsIgnoreCase(waterAvailability)) {
                crops.add("Maize");
            }
        }
        else if ("Alluvial Soil".equalsIgnoreCase(soilType)) {
            if ("Kharif".equalsIgnoreCase(season)) {
                crops.add("Rice (Paddy)");
                crops.add("Maize");
                crops.add("Sugarcane");
            } else if ("Rabi".equalsIgnoreCase(season)) {
                crops.add("Wheat");
                crops.add("Mustard");
                crops.add("Chickpea (Gram)");
            } else {
                crops.add("Green Gram (Moong)");
                crops.add("Watermelon / Vegetables");
            }
        }
        else if ("Sandy Soil".equalsIgnoreCase(soilType)) {
            crops.add("Pearl Millet (Bajra)");
            crops.add("Cluster Bean (Guar)");
            crops.add("Groundnut");
        }
        else if ("Clay Soil".equalsIgnoreCase(soilType)) {
            crops.add("Rice (Paddy)");
            crops.add("Sugarcane");
            if ("Rabi".equalsIgnoreCase(season)) {
                crops.add("Wheat");
            }
        }

        if (crops.isEmpty()) {
            crops.add("Millets (Bajra / Jowar)");
            crops.add("Pulses (Moong / Urad)");
        }

        model.addAttribute("soilType", soilType);
        model.addAttribute("season", season);
        model.addAttribute("waterAvailability", waterAvailability);
        model.addAttribute("crops", crops);

        return "crop-recommendation";
    }
}
