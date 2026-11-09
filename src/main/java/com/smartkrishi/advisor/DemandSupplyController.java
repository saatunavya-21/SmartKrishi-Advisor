
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.entity.Produce;
import com.smartkrishi.advisor.entity.CropDemand;

import com.smartkrishi.advisor.repository.ProduceRepository;
import com.smartkrishi.advisor.repository.CropDemandRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DemandSupplyController {

    @Autowired
    private ProduceRepository produceRepository;

    @Autowired
    private CropDemandRepository cropDemandRepository;

    @GetMapping("/demand-supply")
    public String showDemandSupply(
            HttpSession session, Model model) {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        List<Produce> produceList =
                produceRepository.findAll();

        List<CropDemand> demandList =
                cropDemandRepository.findAll();

        Map<String, Double> supply =
                new LinkedHashMap<>();

        Map<String, Double> demand =
                new LinkedHashMap<>();

        for (Produce p : produceList) {
            String crop = p.getCropName();

            supply.put(
                crop,
                supply.getOrDefault(crop, 0.0)
                    + p.getQuantity()
            );
        }

        for (CropDemand d : demandList) {
            String crop = d.getCropName();

            demand.put(
                crop,
                demand.getOrDefault(crop, 0.0)
                    + d.getQuantity()
            );
        }

        Map<String, Double> allCrops =
                new LinkedHashMap<>();

        for (String crop : supply.keySet()) {
            allCrops.put(crop, 0.0);
        }

        for (String crop : demand.keySet()) {
            allCrops.put(crop, 0.0);
        }

        model.addAttribute("supply", supply);
        model.addAttribute("demand", demand);
        model.addAttribute("allCrops", allCrops);

        return "demand-supply";
    }
}
