package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.CropDemand;
import com.smartkrishi.advisor.entity.Produce;
import com.smartkrishi.advisor.repository.CropDemandRepository;
import com.smartkrishi.advisor.repository.ProduceRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class DemandSupplyController {

    @Autowired
    private ProduceRepository produceRepository;

    @Autowired
    private CropDemandRepository cropDemandRepository;

    public static class DemandSupplyAnalysis {
        private String cropName;
        private double supplyQuintals;
        private double demandQuintals;
        private double differenceQuintals;
        private String status;

        public DemandSupplyAnalysis(String cropName, double supplyQuintals, double demandQuintals) {
            this.cropName = cropName;
            this.supplyQuintals = Math.round(supplyQuintals * 100.0) / 100.0;
            this.demandQuintals = Math.round(demandQuintals * 100.0) / 100.0;
            this.differenceQuintals = Math.round((supplyQuintals - demandQuintals) * 100.0) / 100.0;
            if (this.differenceQuintals > 0.001) {
                this.status = "Surplus";
            } else if (this.differenceQuintals < -0.001) {
                this.status = "Shortage";
            } else {
                this.status = "Balanced";
            }
        }

        public String getCropName() {
            return cropName;
        }

        public double getSupplyQuintals() {
            return supplyQuintals;
        }

        public double getDemandQuintals() {
            return demandQuintals;
        }

        public double getDifferenceQuintals() {
            return differenceQuintals;
        }

        public String getStatus() {
            return status;
        }
    }

    @GetMapping("/demand-supply")
    public String showDemandSupply(HttpSession session, Model model) {

        if (!Boolean.TRUE.equals(session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        List<Produce> produceList = produceRepository.findAll();
        List<CropDemand> demandList = cropDemandRepository.findAll();

        Map<String, Double> supplyMap = new LinkedHashMap<>();
        Map<String, Double> demandMap = new LinkedHashMap<>();
        Map<String, String> displayNames = new LinkedHashMap<>();

        for (Produce p : produceList) {
            if (p.getCropName() == null || p.getCropName().isBlank()) continue;
            String normalized = p.getCropName().trim().toLowerCase();
            displayNames.putIfAbsent(normalized, formatTitleCase(p.getCropName().trim()));

            double qtyInQuintals = convertToQuintals(p.getQuantity(), p.getUnit());
            supplyMap.put(normalized, supplyMap.getOrDefault(normalized, 0.0) + qtyInQuintals);
        }

        for (CropDemand d : demandList) {
            if (d.getCropName() == null || d.getCropName().isBlank()) continue;
            String normalized = d.getCropName().trim().toLowerCase();
            displayNames.putIfAbsent(normalized, formatTitleCase(d.getCropName().trim()));

            double qtyInQuintals = convertToQuintals(d.getQuantity(), d.getUnit());
            demandMap.put(normalized, demandMap.getOrDefault(normalized, 0.0) + qtyInQuintals);
        }

        Set<String> allCropKeys = new TreeSet<>();
        allCropKeys.addAll(supplyMap.keySet());
        allCropKeys.addAll(demandMap.keySet());

        List<DemandSupplyAnalysis> analysisList = new ArrayList<>();
        Map<String, Double> legacySupply = new LinkedHashMap<>();
        Map<String, Double> legacyDemand = new LinkedHashMap<>();
        Map<String, Double> legacyAllCrops = new LinkedHashMap<>();

        for (String key : allCropKeys) {
            String displayName = displayNames.getOrDefault(key, formatTitleCase(key));
            double sup = supplyMap.getOrDefault(key, 0.0);
            double dem = demandMap.getOrDefault(key, 0.0);

            analysisList.add(new DemandSupplyAnalysis(displayName, sup, dem));

            legacySupply.put(displayName, Math.round(sup * 100.0) / 100.0);
            legacyDemand.put(displayName, Math.round(dem * 100.0) / 100.0);
            legacyAllCrops.put(displayName, 0.0);
        }

        model.addAttribute("analysisList", analysisList);
        // Preserving legacy attributes in case any existing references use them
        model.addAttribute("supply", legacySupply);
        model.addAttribute("demand", legacyDemand);
        model.addAttribute("allCrops", legacyAllCrops);

        return "demand-supply";
    }

    private double convertToQuintals(double quantity, String unit) {
        if (unit == null) return quantity;
        String u = unit.trim().toLowerCase();
        if (u.contains("kg") || u.contains("kilo")) {
            return quantity / 100.0;
        }
        return quantity;
    }

    private String formatTitleCase(String text) {
        if (text == null || text.isBlank()) return "";
        String[] words = text.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1).toLowerCase())
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }
}
