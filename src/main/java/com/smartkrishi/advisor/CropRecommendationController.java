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
    public String showCropForm(HttpSession session) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        return "crop-recommendation";
    }

    @PostMapping("/crop-recommendation")
    public String recommendCrops(
            @RequestParam String soilType,
            @RequestParam String season,
            @RequestParam String waterAvailability,
            Model model,
            HttpSession session) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

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
            if ("Rabi".equalsIgnoreCase(season)) {
                crops.add("Mustard");
            }
        }
        else if ("Alluvial Soil".equalsIgnoreCase(soilType)) {
            crops.add("Maize");
            crops.add("Pulses / Green Gram");

            if ("High".equalsIgnoreCase(waterAvailability)) {
                crops.add("Paddy (Rice)");
                crops.add("Sugarcane");
            } else {
                crops.add("Wheat");
            }
        }
        else if ("Sandy Soil".equalsIgnoreCase(soilType)) {
            crops.add("Pearl Millet (Bajra)");
            crops.add("Groundnut");
            crops.add("Cluster Bean (Guar)");
        }
        else if ("Clay Soil".equalsIgnoreCase(soilType)) {
            crops.add("Paddy (Rice)");
            crops.add("Wheat");
            crops.add("Chickpea (Gram)");
        }

        model.addAttribute("crops", crops);
        model.addAttribute("soilType", soilType);
        model.addAttribute("season", season);
        model.addAttribute("waterAvailability", waterAvailability);

        return "crop-recommendation";
    }
}
