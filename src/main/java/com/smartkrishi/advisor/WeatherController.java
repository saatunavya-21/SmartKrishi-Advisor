

package com.smartkrishi.advisor;

import tools.jackson.databind.JsonNode;
import com.smartkrishi.advisor.entity.Farmer;
import com.smartkrishi.advisor.repository.FarmerRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class WeatherController {

    @Autowired
    private FarmerRepository farmerRepository;

    private final RestClient restClient = RestClient.create();

    @GetMapping("/weather")
    public String showWeather(HttpSession session, Model model) {

        Long farmerId = (Long) session.getAttribute("farmerId");

        if (farmerId == null) {
            return "redirect:/login";
        }

        try {
            Farmer farmer = farmerRepository.findById(farmerId)
                    .orElseThrow();

            String location = "Rangareddy, Telangana";

         // Hyderabad approximate coordinates
         double latitude = 17.3850;
         double longitude = 78.4867;    
            // Fetch current weather
            String weatherUrl = UriComponentsBuilder
                    .fromUriString("https://api.open-meteo.com/v1/forecast")
                    .queryParam("latitude", latitude)
                    .queryParam("longitude", longitude)
                    .queryParam("current",
                            "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m")
                    .queryParam("timezone", "auto")
                    .build()
                    .encode()
                    .toUriString();

            JsonNode weatherData = restClient.get()
                    .uri(weatherUrl)
                    .retrieve()
                    .body(JsonNode.class);

            JsonNode current = weatherData.get("current");

            model.addAttribute("location", location);
            model.addAttribute("temperature",
                    current.get("temperature_2m").asDouble());
            model.addAttribute("humidity",
                    current.get("relative_humidity_2m").asInt());
            model.addAttribute("feelsLike",
                    current.get("apparent_temperature").asDouble());
            model.addAttribute("rain",
                    current.get("precipitation").asDouble());
            model.addAttribute("wind",
                    current.get("wind_speed_10m").asDouble());
            model.addAttribute("weatherCode",
                    current.get("weather_code").asInt());
            model.addAttribute("weatherTime",
                    current.get("time").asText());

        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute("error",
                    "Unable to retrieve weather information: " + e.getMessage());
        }

        return "weather";
    }
}
