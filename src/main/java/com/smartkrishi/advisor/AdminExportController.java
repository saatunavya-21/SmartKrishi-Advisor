
package com.smartkrishi.advisor;

import java.io.IOException;
import java.util.List;

import com.smartkrishi.advisor.entity.Farmer;
import com.smartkrishi.advisor.entity.CropDemand;

import com.smartkrishi.advisor.repository.FarmerRepository;
import com.smartkrishi.advisor.repository.CropDemandRepository;
import com.smartkrishi.advisor.repository.ProduceRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminExportController {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private CropDemandRepository cropDemandRepository;

    @Autowired
    private ProduceRepository produceRepository;


    // Export Farmer Records
    @GetMapping("/admin/export-farmers")
    public void exportFarmers(
            HttpSession session,
            HttpServletResponse response) throws IOException {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {
            response.sendRedirect("/admin/login");
            return;
        }

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=farmers.csv"
        );

        List<Farmer> farmers = farmerRepository.findAll();

        var writer = response.getWriter();

        writer.println(
            "ID,Name,Email,Phone,Village,District,State,Land Area,Soil Type"
        );

        for (Farmer farmer : farmers) {
            writer.println(
                csv(farmer.getId()) + "," +
                csv(farmer.getName()) + "," +
                csv(farmer.getEmail()) + "," +
                csv(farmer.getPhone()) + "," +
                csv(farmer.getVillage()) + "," +
                csv(farmer.getDistrict()) + "," +
                csv(farmer.getState()) + "," +
                csv(farmer.getLandArea()) + "," +
                csv(farmer.getSoilType())
            );
        }

        writer.flush();
    }


    // Export Buyer Demand Records
    @GetMapping("/admin/export-demands")
    public void exportDemands(
            HttpSession session,
            HttpServletResponse response) throws IOException {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {
            response.sendRedirect("/admin/login");
            return;
        }

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=buyer-demands.csv"
        );

        List<CropDemand> demands =
                cropDemandRepository.findAll();

        var writer = response.getWriter();

        writer.println(
            "ID,Buyer ID,Crop,Quantity,Unit,Location"
        );

        for (CropDemand demand : demands) {
            writer.println(
                csv(demand.getId()) + "," +
                csv(demand.getBuyerId()) + "," +
                csv(demand.getCropName()) + "," +
                csv(demand.getQuantity()) + "," +
                csv(demand.getUnit()) + "," +
                csv(demand.getLocation())
            );
        }

        writer.flush();
    }


    // Export Farmer Produce Records
    @GetMapping("/admin/export-produce")
    public void exportProduce(
            HttpSession session,
            HttpServletResponse response) throws IOException {

        if (!Boolean.TRUE.equals(
                session.getAttribute("adminLoggedIn"))) {
            response.sendRedirect("/admin/login");
            return;
        }

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=farmer-produce.csv"
        );

        var writer = response.getWriter();

        writer.println(
            "ID,Farmer ID,Crop,Quantity,Unit,Expected Price,Quality,Location"
        );

        var produceList = produceRepository.findAll();

        for (var produce : produceList) {
            writer.println(
                csv(produce.getId()) + "," +
                csv(produce.getFarmerId()) + "," +
                csv(produce.getCropName()) + "," +
                csv(produce.getQuantity()) + "," +
                csv(produce.getUnit()) + "," +
                csv(produce.getExpectedPrice()) + "," +
                csv(produce.getQuality()) + "," +
                csv(produce.getLocation())
            );
        }

        writer.flush();
    }


    // CSV Formatting Method
    private String csv(Object value) {

        String text = value == null ? "" : value.toString();

        return "\"" + text.replace("\"", "\"\"") + "\"";
    }

}
