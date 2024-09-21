package ru.neoflex.vacation_pay_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.net.ConnectException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class AbstractApiHolidaysApiService implements HolidaysApiService {
    @Value("${abstractapi.key}")
    private String urlKey;
    private static final int MAX_RETRIES = 3;

    private static final String url = "https://calendarific.com/api/v2/holidays?&"
            + "api_key=%s"
            + "&country=RU&"
            + "year=%d"
            + "&type=national";

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public AbstractApiHolidaysApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Set<LocalDate> getHolidays(int year) throws JsonProcessingException, ConnectException {
        String readyUrl = String.format(url, urlKey, year);
        String response = executeWithRetries(readyUrl);

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode holidaysNode = rootNode.path("response").path("holidays");

        Set<LocalDate> dates = new HashSet<>();
        for (JsonNode holidayNode : holidaysNode) {
            String dateStr = holidayNode.path("date").path("iso").asText();
            LocalDate date = LocalDate.parse(dateStr);
            dates.add(date);
        }

        return dates;
    }

    private String executeWithRetries(String url) throws ConnectException {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            }
        }
        throw new ConnectException("Failed to get response after " + MAX_RETRIES + " attempts");
    }
}
