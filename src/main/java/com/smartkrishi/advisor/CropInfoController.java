package com.smartkrishi.advisor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CropInfoController {

    public static class CropDetail {
        private String name;
        private String category;
        private String description;
        private String soilType;
        private String climate;
        private String season;
        private String waterRequirement;
        private String harvesting;
        private String duration;

        public CropDetail(String name, String category, String description, String soilType, String climate, String season, String waterRequirement, String harvesting, String duration) {
            this.name = name;
            this.category = category;
            this.description = description;
            this.soilType = soilType;
            this.climate = climate;
            this.season = season;
            this.waterRequirement = waterRequirement;
            this.harvesting = harvesting;
            this.duration = duration;
        }

        public String getName() { return name; }
        public String getCategory() { return category; }
        public String getDescription() { return description; }
        public String getSoilType() { return soilType; }
        public String getClimate() { return climate; }
        public String getSeason() { return season; }
        public String getWaterRequirement() { return waterRequirement; }
        public String getHarvesting() { return harvesting; }
        public String getDuration() { return duration; }
    }

    private static final List<CropDetail> CROPS = List.of(
        new CropDetail("Rice (Paddy)", "Cereals", "Primary staple cereal of India, grown predominantly in submerged clay or alluvial soils with high moisture retention.", "Clay Soil, Alluvial Soil", "Warm & Humid (22°C - 32°C)", "Kharif (June - Nov)", "High (1200 - 1800 mm)", "Harvest when panicles turn golden yellow and grain moisture drops to 20-22%.", "120 - 150 days"),
        new CropDetail("Wheat", "Cereals", "Major rabi cereal staple cultivated widely in northern and central fertile plains.", "Well-drained Alluvial, Clay Loam", "Cool & Dry (10°C - 25°C)", "Rabi (Oct - April)", "Moderate (450 - 650 mm)", "Harvest when grains become hard and straw turns dry golden brown.", "110 - 140 days"),
        new CropDetail("Cotton", "Cash Crops", "Important commercial fibre crop; best suited to deep moisture-retentive black cotton soils.", "Black Soil (Regur)", "Hot & Sunny (21°C - 30°C)", "Kharif (May - Nov)", "Moderate (600 - 800 mm)", "Pick clean mature open bolls in dry sunny mornings.", "150 - 180 days"),
        new CropDetail("Sugarcane", "Cash Crops", "Perennial heavy cash crop used for sugar, ethanol, and jaggery production.", "Deep Alluvial, Loamy Soil", "Hot & Humid (24°C - 35°C)", "Annual / Perennial", "High (1500 - 2500 mm)", "Cut at ground level when brix saccharometer indicates 18-20% sugar maturity.", "300 - 365 days"),
        new CropDetail("Maize (Corn)", "Cereals", "Versatile cereal used for food, poultry feed, and industrial starch; requires good drainage.", "Alluvial Loam, Red Loam", "Moderate (18°C - 27°C)", "Kharif / Rabi / Zaid", "Moderate (500 - 750 mm)", "Harvest when husk leaves dry up and black moisture layer forms at grain base.", "90 - 110 days"),
        new CropDetail("Chickpea (Gram)", "Pulses", "Major protein-rich pulse crop that enriches soil through atmospheric nitrogen fixation.", "Sandy Loam, Light Clay", "Cool & Dry (15°C - 25°C)", "Rabi (Oct - March)", "Low (300 - 450 mm)", "Harvest when plants dry up and pods rattle when shaken.", "90 - 120 days"),
        new CropDetail("Soybean", "Oilseeds", "Dual-purpose wonder crop rich in protein (40%) and edible oil (20%).", "Well-drained Black Soil, Loam", "Warm & Sunny (20°C - 30°C)", "Kharif (June - Oct)", "Moderate (550 - 700 mm)", "Harvest when 90% leaves turn yellow and drop off with moisture at 14%.", "95 - 110 days"),
        new CropDetail("Groundnut (Peanut)", "Oilseeds", "Vital oilseed and cash crop requiring loose friable sandy loam for optimal peg penetration.", "Sandy Soil, Red Sandy Loam", "Warm (22°C - 28°C)", "Kharif / Summer", "Moderate (500 - 600 mm)", "Pull out plants when inner shell wall develops dark brown tanning markings.", "105 - 125 days"),
        new CropDetail("Tomato", "Vegetables", "High-value commercial vegetable crop cultivated across round-the-year cycles.", "Well-drained Sandy Loam, Red Soil", "Mild (18°C - 27°C)", "Kharif, Rabi, Summer", "Moderate (400 - 600 mm)", "Pick at breaker or pink stage for long transport; red ripe for local mandis.", "75 - 90 days after transplant"),
        new CropDetail("Mustard", "Oilseeds", "Crucial rabi edible oilseed crop thriving in northern agricultural belts with cool winters.", "Alluvial Loam, Sandy Loam", "Cool & Dry (15°C - 22°C)", "Rabi (Oct - March)", "Low to Moderate (350 - 450 mm)", "Harvest in early morning when pods turn yellow to minimize shattering loss.", "105 - 130 days")
    );

    @GetMapping({"/crops", "/crop-info"})
    public String showCropInfo(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            Model model) {

        List<CropDetail> result = CROPS;

        if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("All")) {
            result = result.stream()
                    .filter(c -> c.getCategory().equalsIgnoreCase(category.trim()))
                    .collect(Collectors.toList());
        }

        if (search != null && !search.trim().isEmpty()) {
            String q = search.trim().toLowerCase();
            result = result.stream()
                    .filter(c -> c.getName().toLowerCase().contains(q) ||
                                 c.getSoilType().toLowerCase().contains(q) ||
                                 c.getSeason().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        model.addAttribute("crops", result);
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedCategory", category != null ? category : "All");

        return "crop-info";
    }
}

