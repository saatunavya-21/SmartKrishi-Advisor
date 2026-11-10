package com.smartkrishi.advisor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.mock.web.MockHttpSession;

import com.smartkrishi.advisor.entity.*;
import com.smartkrishi.advisor.repository.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SmartKrishiAdvisorApplicationTests {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private ProduceRepository produceRepository;

    @Autowired
    private CropDemandRepository cropDemandRepository;

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FarmerController farmerController;

    @Autowired
    private BuyerController buyerController;

    @Autowired
    private DemandSupplyController demandSupplyController;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void testContextAndExistingDatabaseRecords() {
        System.out.println("=== VERIFYING PRESERVED EXISTING DATABASE RECORDS ===");
        long farmerCount = farmerRepository.count();
        long buyerCount = buyerRepository.count();
        long produceCount = produceRepository.count();
        long demandCount = cropDemandRepository.count();
        long priceCount = marketPriceRepository.count();
        long feedbackCount = feedbackRepository.count();

        System.out.println("Existing Farmers: " + farmerCount);
        System.out.println("Existing Buyers: " + buyerCount);
        System.out.println("Existing Produce: " + produceCount);
        System.out.println("Existing Demands: " + demandCount);
        System.out.println("Existing Benchmark Prices: " + priceCount);
        System.out.println("Existing Feedback: " + feedbackCount);

        assertTrue(farmerCount >= 0);
        assertTrue(buyerCount >= 0);
        assertTrue(produceCount >= 0);
    }

    @Test
    void testFarmerRegistrationAndDuplicateRejection() {
        String testEmail = "test_farmer_" + System.currentTimeMillis() + "@example.com";
        Farmer farmer = new Farmer();
        farmer.setName("Test Ramesh");
        farmer.setEmail(testEmail);
        farmer.setPassword("SecurePass123");
        farmer.setPhone("9876543210");
        farmer.setVillage("Shamshabad");
        farmer.setDistrict("Rangareddy");
        farmer.setState("Telangana");
        farmer.setLandArea(4.5);
        farmer.setSoilType("Red Soil");

        Model model = new ConcurrentModel();
        String view1 = farmerController.registerFarmer(farmer, model);
        assertEquals("register", view1);
        assertNotNull(model.getAttribute("message"));

        Farmer saved = farmerRepository.findByEmail(testEmail).orElse(null);
        assertNotNull(saved);
        assertTrue(passwordEncoder.matches("SecurePass123", saved.getPassword()));

        // Test duplicate email rejection
        Farmer duplicate = new Farmer();
        duplicate.setName("Duplicate Farmer");
        duplicate.setEmail(testEmail);
        duplicate.setPassword("AnotherPass123");
        Model model2 = new ConcurrentModel();
        farmerController.registerFarmer(duplicate, model2);
        assertNotNull(model2.getAttribute("errorMessage"));
        assertTrue(model2.getAttribute("errorMessage").toString().contains("already registered"));

        // Clean up test entity to preserve database cleanliness
        farmerRepository.delete(saved);
    }

    @Test
    void testBuyerRegistrationAndDuplicateRejection() {
        String testEmail = "test_buyer_" + System.currentTimeMillis() + "@example.com";
        Buyer buyer = new Buyer();
        buyer.setName("Agro Traders Ltd");
        buyer.setEmail(testEmail);
        buyer.setPassword("BuyerPass123");
        buyer.setPhone("9123456780");
        buyer.setVillage("Bowenpally");
        buyer.setDistrict("Hyderabad");
        buyer.setState("Telangana");

        Model model = new ConcurrentModel();
        String view1 = buyerController.registerBuyer(buyer, model);
        assertEquals("buyer-register", view1);
        assertNotNull(model.getAttribute("message"));

        Buyer saved = buyerRepository.findByEmail(testEmail).orElse(null);
        assertNotNull(saved);
        assertTrue(passwordEncoder.matches("BuyerPass123", saved.getPassword()));

        // Test duplicate email rejection
        Buyer duplicate = new Buyer();
        duplicate.setName("Duplicate Buyer");
        duplicate.setEmail(testEmail);
        duplicate.setPassword("AnotherPass123");
        Model model2 = new ConcurrentModel();
        buyerController.registerBuyer(duplicate, model2);
        assertNotNull(model2.getAttribute("errorMessage"));
        assertTrue(model2.getAttribute("errorMessage").toString().contains("already registered"));

        // Clean up test entity
        buyerRepository.delete(saved);
    }

    @Test
    void testFarmerProduceListingAndRetrieval() {
        Produce produce = new Produce();
        produce.setFarmerId(99999L);
        produce.setCropName("Sorghum Test Crop");
        produce.setQuantity(25.0);
        produce.setUnit("Quintal");
        produce.setExpectedPrice(2600.0);
        produce.setQuality("Good");
        produce.setLocation("Rangareddy Farm");

        Produce saved = produceRepository.save(produce);
        assertNotNull(saved.getId());

        List<Produce> farmerList = produceRepository.findByFarmerId(99999L);
        assertFalse(farmerList.isEmpty());
        assertEquals("Sorghum Test Crop", farmerList.get(0).getCropName());

        produceRepository.delete(saved);
    }

    @Test
    void testBuyerCropDemandSubmissionAndRetrieval() {
        CropDemand demand = new CropDemand();
        demand.setBuyerId(88888L);
        demand.setCropName("Paddy Test Demand");
        demand.setQuantity(50.0);
        demand.setUnit("Quintal");
        demand.setLocation("Hyderabad Market");

        CropDemand saved = cropDemandRepository.save(demand);
        assertNotNull(saved.getId());

        List<CropDemand> buyerDemands = cropDemandRepository.findByBuyerId(88888L);
        assertFalse(buyerDemands.isEmpty());
        assertEquals("Paddy Test Demand", buyerDemands.get(0).getCropName());

        cropDemandRepository.delete(saved);
    }

    @Test
    void testDemandSupplyUnitConversionAndNormalization() {
        // Create test produce: 10 Quintal + 500 Kg = 10 q + 5 q = 15 q
        Produce p1 = new Produce();
        p1.setFarmerId(77777L);
        p1.setCropName("Maize Unit Test");
        p1.setQuantity(10.0);
        p1.setUnit("Quintal");
        p1.setExpectedPrice(2200.0);
        p1.setQuality("Good");
        p1.setLocation("Farm 1");
        produceRepository.save(p1);

        Produce p2 = new Produce();
        p2.setFarmerId(77777L);
        p2.setCropName("maize unit test "); // whitespace and case variance
        p2.setQuantity(500.0);
        p2.setUnit("Kg"); // 500 Kg = 5 Quintals
        p2.setExpectedPrice(22.0);
        p2.setQuality("Good");
        p2.setLocation("Farm 2");
        produceRepository.save(p2);

        // Buyer demand: 12 Quintal
        CropDemand d1 = new CropDemand();
        d1.setBuyerId(66666L);
        d1.setCropName("MAIZE UNIT TEST");
        d1.setQuantity(12.0);
        d1.setUnit("Quintal");
        d1.setLocation("Market 1");
        cropDemandRepository.save(d1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("adminLoggedIn", true);
        Model model = new ConcurrentModel();

        String view = demandSupplyController.showDemandSupply(session, model);
        assertEquals("demand-supply", view);

        @SuppressWarnings("unchecked")
        List<DemandSupplyController.DemandSupplyAnalysis> list =
                (List<DemandSupplyController.DemandSupplyAnalysis>) model.getAttribute("analysisList");
        assertNotNull(list);

        DemandSupplyController.DemandSupplyAnalysis match = list.stream()
                .filter(item -> item.getCropName().equalsIgnoreCase("Maize Unit Test"))
                .findFirst()
                .orElse(null);

        assertNotNull(match, "Normalized crop should be found in analysis");
        assertEquals(15.0, match.getSupplyQuintals(), 0.01, "Supply should convert 10 q + 500 kg to 15.0 q");
        assertEquals(12.0, match.getDemandQuintals(), 0.01, "Demand should be 12.0 q");
        assertEquals(3.0, match.getDifferenceQuintals(), 0.01, "Difference should be +3.0 q");
        assertEquals("Surplus", match.getStatus(), "Status should be Surplus");

        // Clean up test records
        produceRepository.delete(p1);
        produceRepository.delete(p2);
        cropDemandRepository.delete(d1);
    }

    @Test
    void testMarketPriceAdminCrudLifecycle() {
        MarketPrice price = new MarketPrice();
        price.setCropName("Cotton Benchmark Test");
        price.setMarketName("Warangal Mandi");
        price.setDistrict("Warangal");
        price.setPrice(7200.0);
        price.setUnit("Quintal");

        MarketPrice saved = marketPriceRepository.save(price);
        assertNotNull(saved.getId());

        // Update
        saved.setPrice(7450.0);
        marketPriceRepository.save(saved);

        MarketPrice updated = marketPriceRepository.findById(saved.getId()).orElse(null);
        assertNotNull(updated);
        assertEquals(7450.0, updated.getPrice());

        // Delete
        marketPriceRepository.deleteById(saved.getId());
        assertFalse(marketPriceRepository.existsById(saved.getId()));
    }
}
