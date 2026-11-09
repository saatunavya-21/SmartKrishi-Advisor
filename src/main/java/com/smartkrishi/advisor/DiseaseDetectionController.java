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

        String result;

        if (symptoms.equals("Yellow Leaves")) {
            result = "Yellow leaves can be associated with nutrient deficiency, water stress or disease. Further inspection is needed.";
        } else if (symptoms.equals("Brown Spots")) {
            result = "Brown spots may be associated with fungal or bacterial leaf problems. Inspect the affected leaves carefully.";
        } else if (symptoms.equals("Wilting")) {
            result = "Wilting may be caused by water stress, root problems or disease. Check soil moisture and plant roots.";
        } else if (symptoms.equals("White Powder")) {
            result = "White powder-like growth may indicate powdery mildew. Seek local agricultural guidance for confirmation.";
        } else {
            result = "Select a symptom for basic plant health information.";
        }

        model.addAttribute("crop", crop);
        model.addAttribute("symptoms", symptoms);
        model.addAttribute("result", result);

        return "disease-detection";
    }
}