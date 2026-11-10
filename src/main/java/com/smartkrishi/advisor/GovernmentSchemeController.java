package com.smartkrishi.advisor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GovernmentSchemeController {

    public static class Scheme {
        private String name;
        private String ministry;
        private String description;
        private String eligibility;
        private String benefits;
        private String officialUrl;

        public Scheme(String name, String ministry, String description, String eligibility, String benefits, String officialUrl) {
            this.name = name;
            this.ministry = ministry;
            this.description = description;
            this.eligibility = eligibility;
            this.benefits = benefits;
            this.officialUrl = officialUrl;
        }

        public String getName() { return name; }
        public String getMinistry() { return ministry; }
        public String getDescription() { return description; }
        public String getEligibility() { return eligibility; }
        public String getBenefits() { return benefits; }
        public String getOfficialUrl() { return officialUrl; }
    }

    private static final List<Scheme> SCHEMES = List.of(
        new Scheme(
            "PM-KISAN (Pradhan Mantri Kisan Samman Nidhi)",
            "Ministry of Agriculture & Farmers Welfare",
            "Central Sector scheme providing income support to all landholding farmer families in the country to supplement financial needs for procuring agricultural inputs and domestic requirements.",
            "All landholder farmer families with cultivable land in their names (subject to institutional exclusions like high tax payers).",
            "Direct cash benefit of ₹6,000 per year transferred directly to bank accounts in three equal 4-monthly installments of ₹2,000.",
            "https://pmkisan.gov.in"
        ),
        new Scheme(
            "PMFBY (Pradhan Mantri Fasal Bima Yojana)",
            "Ministry of Agriculture & Farmers Welfare",
            "Comprehensive, low-premium crop insurance scheme safeguarding farmers against non-preventable natural perils, drought, flood, pests, and post-harvest cyclone damage.",
            "All farmers growing notified crops in notified areas (both loanee and non-loanee farmers are eligible).",
            "Uniform subsidized farmer premium: 2% for Kharif food/oilseeds, 1.5% for Rabi, and 5% for annual commercial/horticultural crops; 100% loss compensation.",
            "https://pmfby.gov.in"
        ),
        new Scheme(
            "Soil Health Card Scheme",
            "Department of Agriculture & Cooperation",
            "Promotes balanced nutrient application by assessing 12 chemical parameters of farm soil and providing crop-wise fertilizer dosage recommendations.",
            "All operational farmers across all states and union territories.",
            "Free laboratory soil test report card issued every 2 years; reduces excessive fertilizer expenditure by 15-20% while improving crop yields.",
            "https://soilhealth.dac.gov.in"
        ),
        new Scheme(
            "PMKSY - Per Drop More Crop (Micro Irrigation)",
            "Ministry of Jal Shakti & MoAFW",
            "Focuses on enhancing water use efficiency at the farm level through Precision Micro-Irrigation technologies (Drip and Sprinkler systems).",
            "Individual farmers, SHGs, cooperative societies, and farmer producer organizations.",
            "Financial subsidy up to 55% for small/marginal farmers and 45% for other farmers on modern drip and sprinkler equipment installations.",
            "https://pmksy.gov.in"
        ),
        new Scheme(
            "e-NAM (National Agriculture Market)",
            "Small Farmers Agribusiness Consortium (SFAC)",
            "Pan-India electronic trading portal networking existing APMC mandis to create a unified national market for agricultural commodities.",
            "Any farmer registered with their local APMC market yard with bank account and Aadhaar.",
            "Transparent online auction bidding, electronic payment directly into farmer accounts, eliminating cartelization and middleman deductions.",
            "https://enam.gov.in"
        ),
        new Scheme(
            "Kisan Credit Card (KCC) Scheme",
            "Reserve Bank of India & NABARD",
            "Ensures timely institutional credit at highly concessional interest rates for agricultural operational expenses and animal husbandry.",
            "All farmers, tenant farmers, oral lessees, sharecroppers, and SHG joint liability groups.",
            "Credit limit up to ₹3 Lakh at an effective interest rate of only 4% per annum (with timely 3% prompt repayment subvention).",
            "https://agricoop.nic.in"
        )
    );

    @GetMapping({"/government-schemes", "/schemes"})
    public String showSchemes(Model model) {
        model.addAttribute("schemes", SCHEMES);
        return "government-schemes";
    }
}

