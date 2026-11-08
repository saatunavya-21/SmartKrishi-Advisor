
package com.smartkrishi.advisor;

import com.smartkrishi.advisor.repository.FarmerRepository;
import com.smartkrishi.advisor.repository.BuyerRepository;
import com.smartkrishi.advisor.repository.ProduceRepository;
import com.smartkrishi.advisor.repository.CropDemandRepository;
import com.smartkrishi.advisor.repository.FeedbackRepository;
import com.smartkrishi.advisor.repository.MarketPriceRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminReportsController {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private ProduceRepository produceRepository;

    @Autowired
    private CropDemandRepository cropDemandRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @GetMapping("/admin/reports")
    public String showReports(
            HttpSession session, Model model) {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {
            return "redirect:/admin/login";
        }

        model.addAttribute("totalFarmers",
                farmerRepository.count());

        model.addAttribute("totalBuyers",
                buyerRepository.count());

        model.addAttribute("totalProduce",
                produceRepository.count());

        model.addAttribute("totalDemand",
                cropDemandRepository.count());

        model.addAttribute("totalFeedback",
                feedbackRepository.count());

        model.addAttribute("totalMarketPrices",
                marketPriceRepository.count());

        return "admin-reports";
    }
}
