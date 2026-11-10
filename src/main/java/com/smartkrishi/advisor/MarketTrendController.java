package com.smartkrishi.advisor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class MarketTrendController {

    public static class TrendRow {
        private String crop;
        private String mandi;
        private String district;
        private double lastMonthPrice;
        private double currentPrice;
        private String trendDirection; // Up, Down, Stable
        private double diffPercent;
        private String advice;

        public TrendRow(String crop, String mandi, String district, double lastMonthPrice, double currentPrice, String trendDirection, double diffPercent, String advice) {
            this.crop = crop;
            this.mandi = mandi;
            this.district = district;
            this.lastMonthPrice = lastMonthPrice;
            this.currentPrice = currentPrice;
            this.trendDirection = trendDirection;
            this.diffPercent = diffPercent;
            this.advice = advice;
        }

        public String getCrop() { return crop; }
        public String getMandi() { return mandi; }
        public String getDistrict() { return district; }
        public double getLastMonthPrice() { return lastMonthPrice; }
        public double getCurrentPrice() { return currentPrice; }
        public String getTrendDirection() { return trendDirection; }
        public double getDiffPercent() { return diffPercent; }
        public String getAdvice() { return advice; }
    }

    private static final List<TrendRow> TRENDS = List.of(
        new TrendRow("Wheat", "Central Mandi", "Karnal", 2380.0, 2450.0, "Up", +2.9, "Strong demand from food corporations; good time to sell gradually."),
        new TrendRow("Paddy (Basmati)", "City Grain Market", "Amritsar", 3650.0, 3850.0, "Up", +5.5, "Export orders surging; hold produce if storage conditions permit."),
        new TrendRow("Cotton (Medium)", "APMC Yard", "Nagpur", 7350.0, 7150.0, "Down", -2.7, "Arrival volume high; look for forward contract with ginning mills."),
        new TrendRow("Soybean", "Agri Mandi", "Indore", 4600.0, 4620.0, "Stable", +0.4, "Price stable; crushers active at current price levels."),
        new TrendRow("Tomato", "Vegetable Market", "Nashik", 1600.0, 2200.0, "Up", +37.5, "High volatility due to weather disruption; capitalize on spot rates."),
        new TrendRow("Onion", "APMC Market", "Lasalgaon", 1850.0, 1950.0, "Up", +5.4, "Moderate firming; buffer release monitoring advised."),
        new TrendRow("Maize", "District Yard", "Warangal", 2150.0, 2180.0, "Stable", +1.4, "Poultry and starch demand balancing farm arrivals."),
        new TrendRow("Mustard", "Kisan Mandi", "Jaipur", 5200.0, 5450.0, "Up", +4.8, "Edible oil duty protection supporting strong farmgate realizations.")
    );

    @GetMapping("/market-trends")
    public String showMarketTrends(
            @RequestParam(required = false) String district,
            Model model) {

        List<TrendRow> filtered = TRENDS;
        if (district != null && !district.trim().isEmpty() && !district.equalsIgnoreCase("All")) {
            filtered = TRENDS.stream()
                    .filter(t -> t.getDistrict().equalsIgnoreCase(district.trim()))
                    .toList();
        }

        model.addAttribute("trends", filtered);
        model.addAttribute("selectedDistrict", district != null ? district : "All");

        return "market-trends";
    }
}

