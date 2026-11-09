
package com.smartkrishi.advisor;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import com.smartkrishi.advisor.entity.MarketPrice;
import com.smartkrishi.advisor.repository.MarketPriceRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MarketPriceReportController {

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @GetMapping("/market-price-report")
    public String showReport(
            HttpSession session, Model model) {

        if (session.getAttribute("farmerId") == null) {
            return "redirect:/login";
        }

        List<MarketPrice> prices =
                marketPriceRepository.findAll();

        Map<String, double[]> report =
                new LinkedHashMap<>();

        for (MarketPrice price : prices) {

            String key = price.getCropName()
                    + " - " + price.getUnit();

            double[] values = report.get(key);

            if (values == null) {
                // count, total, minimum, maximum
                values = new double[] {
                    0, 0, price.getPrice(), price.getPrice()
                };
                report.put(key, values);
            }

            values[0]++;
            values[1] += price.getPrice();
            values[2] = Math.min(values[2], price.getPrice());
            values[3] = Math.max(values[3], price.getPrice());
        }

        model.addAttribute("report", report);

        return "market-price-report";
    }
}
