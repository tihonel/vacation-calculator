package ru.neoflex.vacation_pay_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.net.ConnectException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

class AbstractApiHolidaysApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AbstractApiHolidaysApiService holidaysApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHolidays_Success() throws JsonProcessingException, ConnectException {
        // given
        int year = 2023;
        String jsonResponse = "{\"response\":{\"holidays\":[{\"date\":{\"iso\":\"2023-01-01\"}},{\"date\":{\"iso\":\"2023-01-02\"}}]}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);
        when(objectMapper.readTree(jsonResponse)).thenReturn(new ObjectMapper().readTree(jsonResponse));

        // when
        Set<LocalDate> holidays = holidaysApiService.getHolidays(year);

        // then
        assertEquals(2, holidays.size());

        Set<LocalDate> expectedHolidays = Set.of(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2)
        );
        assertTrue(holidays.containsAll(expectedHolidays));
    }

    @Test
    void testGetHolidays_EmptyResponse() throws JsonProcessingException, ConnectException {
        // given
        int year = 2023;
        String jsonResponse = "{\"response\":{\"holidays\":[]}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);
        when(objectMapper.readTree(jsonResponse)).thenReturn(new ObjectMapper().readTree(jsonResponse));

        // when
        Set<LocalDate> holidays = holidaysApiService.getHolidays(year);

        // then
        assertTrue(holidays.isEmpty());
    }

    @Test
    void testGetHolidays_JsonProcessingException() throws JsonProcessingException, ConnectException {
        // given
        int year = 2023;
        String jsonResponse = "invalid json";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);
        when(objectMapper.readTree(jsonResponse)).thenThrow(new JsonProcessingException("Invalid JSON") {});

        // when & then
        try {
            holidaysApiService.getHolidays(year);
        } catch (JsonProcessingException e) {
            assertEquals("Invalid JSON", e.getMessage());
        }
    }

    @Test
    void testGetHolidays_HttpError() throws JsonProcessingException {
        // given
        int year = 2023;
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        // when & then
        try {
            holidaysApiService.getHolidays(year);
        } catch (ConnectException e) {
            // Ожидаем, что исключение будет выброшено, так как ответ от API не успешный
            assertEquals("Failed to get response after 3 attempts", e.getMessage());
        }
    }
}
