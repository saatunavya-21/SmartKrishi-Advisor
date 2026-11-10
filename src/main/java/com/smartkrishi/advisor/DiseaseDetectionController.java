package com.smartkrishi.advisor;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiseaseDetectionController {

    @GetMapping("/disease-detection")
    public String showDiseaseForm(HttpSession session) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        return "disease-detection";
    }

    @PostMapping("/disease-detection")
    public String detectDisease(
            @RequestParam String crop,
            @RequestParam String symptoms,
            Model model,
            HttpSession session) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        String diagnosis;
        String actionPlan;

        if ("Yellow Leaves".equalsIgnoreCase(symptoms)) {
            diagnosis = "Nitrogen Deficiency, Iron Chlorosis, or Early Viral Infection (e.g., Yellow Mosaic / Leaf Curl Virus).";
            actionPlan = "Verify soil drainage. For nutrient deficiency, apply foliar spray of Urea (1-2%) or Ferrous Sulphate (0.5%). If accompanied by leaf curling, control sucking insect vectors (whiteflies/aphids) with Neem seed kernel extract (NSKE 5%).";
        } else if ("Brown Spots".equalsIgnoreCase(symptoms)) {
            diagnosis = "Fungal Foliar Leaf Spot (e.g., Helminthosporium / Cercospora Leaf Spot or Rice Blast).";
            actionPlan = "Remove and destroy heavily infected lower leaves to restrict fungal spore spread. Avoid overhead sprinkler irrigation. If infection progresses rapidly, apply Trichoderma viride bio-fungicide or consult local Krishi Kendra for registered fungicides.";
        } else if ("Wilting".equalsIgnoreCase(symptoms)) {
            diagnosis = "Vascular Wilt (Fusarium / Verticillium) or Bacterial Wilt, or Nematode Root-Knot Infestation.";
            actionPlan = "Inspect root zone for discoloration or rot. Drench root zone with copper oxychloride (0.3%) or Pseudomonas fluorescens culture. Ensure proper soil aeration and avoid excessive waterlogging.";
        } else if ("White Powder".equalsIgnoreCase(symptoms)) {
            diagnosis = "Powdery Mildew (Erysiphe / Leveillula fungal complex).";
            actionPlan = "Apply Wettable Sulphur (0.2%) spray or potassium bicarbonate solution during cooler morning hours. Thin out dense canopy to enhance air movement and sunlight penetration.";
        } else {
            diagnosis = "Unspecified plant distress condition.";
            actionPlan = "Collect plant sample in clean bag and present to nearest Krishi Vigyan Kendra (KVK) or Assistant Agricultural Officer (AAO).";
        }

        model.addAttribute("crop", crop);
        model.addAttribute("symptoms", symptoms);
        model.addAttribute("result", diagnosis);
        model.addAttribute("actionPlan", actionPlan);

        return "disease-detection";
    }
}
