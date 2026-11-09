
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

        if (soilType.equals("Black Soil")) {
            crops.add("Cotton");
            crops.add("Soybean");
            crops.add("Sorghum");
        }
        else if (soilType.equals("Red Soil")) {
            crops.add("Groundnut");
            crops.add("Millets");
            crops.add("Pigeon Pea");
        }
        else if (soilType.equals("Alluvial Soil")) {
            crops.add("Maize");
            crops.add("Pulses");

            if (waterAvailability.equals("High")) {
                crops.add("Paddy");
            }
        }
        else if (soilType.equals("Sandy Soil")) {
            crops.add("Pearl Millet");
            crops.add("Groundnut");
            crops.add("Cluster Bean");
        }

        model.addAttribute("crops", crops);
        model.addAttribute("soilType", soilType);
        model.addAttribute("season", season);
        model.addAttribute("waterAvailability", waterAvailability);

        return "crop-recommendation";
    }
}
