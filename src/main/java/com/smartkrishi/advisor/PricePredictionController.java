package com.smartkrishi.advisor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class PricePredictionController {

    public static class PredictionResult {
        private String crop;
        private String district;
        private String period;
        private double currentBenchmark;
        private double predictedMin;
        private double predictedExpected;
        private double predictedMax;
        private String trend; // Bullish, Stable, Moderately Bearish
        private double percentChange;
        private String explanation;
        private List<MonthlyPoint> history;

        public PredictionResult(String crop, String district, String period, double currentBenchmark, double predictedMin, double predictedExpected, double predictedMax, String trend, double percentChange, String explanation, List<MonthlyPoint> history) {
            this.crop = crop;
            this.district = district;
            this.period = period;
            this.currentBenchmark = currentBenchmark;
            this.predictedMin = predictedMin;
            this.predictedExpected = predictedExpected;
            this.predictedMax = predictedMax;
            this.trend = trend;
            this.percentChange = percentChange;
            this.explanation = explanation;
            this.history = history;
        }

        public String getCrop() { return crop; }
        public String getDistrict() { return district; }
        public String getPeriod() { return period; }
        public double getCurrentBenchmark() { return currentBenchmark; }
        public double getPredictedMin() { return predictedMin; }
        public double getPredictedExpected() { return predictedExpected; }
        public double getPredictedMax() { return predictedMax; }
        public String getTrend() { return trend; }
        public double getPercentChange() { return percentChange; }
        public String getExplanation() { return explanation; }
        public List<MonthlyPoint> getHistory() { return history; }
    }

    public static class MonthlyPoint {
        private String month;
        private double price;

        public MonthlyPoint(String month, double price) {
            this.month = month;
            this.price = price;
        }

        public String getMonth() { return month; }
        public double getPrice() { return price; }
    }

    @GetMapping("/price-prediction")
    public String showPredictionPage(
            @RequestParam(required = false) String crop,
            @RequestParam(required = false) String district,
            @RequestParam(required = false, defaultValue = "3 Months") String period,
            Model model) {

        List<String> crops = List.of("Wheat", "Rice", "Cotton", "Maize", "Soybean", "Tomato", "Potato", "Onion", "Mustard");
        List<String> districts = List.of("Warangal", "Nagpur", "Indore", "Ahmedabad", "Nashik", "Ludhiana", "Patna", "Karnal", "Jaipur");
        List<String> periods = List.of("3 Months", "6 Months", "1 Year");

        model.addAttribute("cropList", crops);
        model.addAttribute("districtList", districts);
        model.addAttribute("periodList", periods);
        model.addAttribute("selectedCrop", crop);
        model.addAttribute("selectedDistrict", district);
        model.addAttribute("selectedPeriod", period);

        if (crop != null && !crop.trim().isEmpty() && district != null && !district.trim().isEmpty()) {
            PredictionResult prediction = calculatePrediction(crop.trim(), district.trim(), period);
            model.addAttribute("prediction", prediction);
        }

        return "price-prediction";
    }

    private PredictionResult calculatePrediction(String crop, String district, String period) {
        double baseline;
        String trend;
        double change;
        String explanation;
        List<MonthlyPoint> points = new ArrayList<>();

        switch (crop.toLowerCase()) {
            case "wheat":
                baseline = 2450.0;
                change = 6.2;
                trend = "Upward (Bullish)";
                explanation = "Post-harvest buffer stocks and government MSP procurement targets indicate steady upward price resilience. Higher mill demand anticipated over the upcoming quarter.";
                points.add(new MonthlyPoint("3 Months Ago", 2320.0));
                points.add(new MonthlyPoint("2 Months Ago", 2390.0));
                points.add(new MonthlyPoint("Last Month", 2420.0));
                points.add(new MonthlyPoint("Current Mandi", 2450.0));
                break;
            case "rice":
                baseline = 2800.0;
                change = 4.8;
                trend = "Moderate Upward";
                explanation = "Non-basmati export momentum and robust domestic consumption maintain firm pricing with lower downside risks.";
                points.add(new MonthlyPoint("3 Months Ago", 2650.0));
                points.add(new MonthlyPoint("2 Months Ago", 2720.0));
                points.add(new MonthlyPoint("Last Month", 2780.0));
                points.add(new MonthlyPoint("Current Mandi", 2800.0));
                break;
            case "cotton":
                baseline = 7200.0;
                change = -3.5;
                trend = "Slight Correction (Bearish)";
                explanation = "Global textile demand cooling and increased market arrivals in southern mandis likely to create mild short-term price softening.";
                points.add(new MonthlyPoint("3 Months Ago", 7550.0));
                points.add(new MonthlyPoint("2 Months Ago", 7400.0));
                points.add(new MonthlyPoint("Last Month", 7300.0));
                points.add(new MonthlyPoint("Current Mandi", 7200.0));
                break;
            case "tomato":
                baseline = 2100.0;
                change = 18.5;
                trend = "High Volatility (Bullish Spike)";
                explanation = "Perishable crop sensitivity to seasonal heat and unseasonal rains in key growing belts causing transient supply contractions.";
                points.add(new MonthlyPoint("3 Months Ago", 1400.0));
                points.add(new MonthlyPoint("2 Months Ago", 1750.0));
                points.add(new MonthlyPoint("Last Month", 1950.0));
                points.add(new MonthlyPoint("Current Mandi", 2100.0));
                break;
            default:
                baseline = 3200.0;
                change = 5.0;
                trend = "Stable Growth";
                explanation = "Normal seasonal mandi balance with steady consumer intake and stable feed mill purchasing.";
                points.add(new MonthlyPoint("3 Months Ago", 3050.0));
                points.add(new MonthlyPoint("2 Months Ago", 3120.0));
                points.add(new MonthlyPoint("Last Month", 3180.0));
                points.add(new MonthlyPoint("Current Mandi", 3200.0));
        }

        double expected = baseline * (1 + (change / 100.0));
        double min = expected * 0.94;
        double max = expected * 1.06;

        return new PredictionResult(crop, district, period, baseline, Math.round(min), Math.round(expected), Math.round(max), trend, change, explanation, points);
    }
}

