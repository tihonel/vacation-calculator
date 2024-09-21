package ru.neoflex.vacation_pay_service.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.neoflex.vacation_pay_service.helpers.VacationHelper;
import ru.neoflex.vacation_pay_service.models.Vacation;
import ru.neoflex.vacation_pay_service.services.VacationService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.time.format.DateTimeFormatter;

@WebMvcTest(VacationController.class)
@Slf4j
public class VacationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VacationService vacationService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCalculateVacationPay_withCorrectParameters_shouldBeStatusOkEqualsResponseBody() throws Exception {
        // given
        Vacation vacation = VacationHelper.getVacation();
        log.info("vacation: {}", vacation);

        BigDecimal expected = new BigDecimal("34.16").setScale(2, RoundingMode.HALF_UP);

        Mockito.when(vacationService.calculateVacationPay(vacation)).thenReturn(expected);

        // when
        ResultActions actual = mockMvc.perform(get("/vacation/calculate")
                .param("averageSalary", vacation.getAverageSalary().toString())
                .param("vacationDays", String.valueOf(vacation.getVacationDays()))
                .param("startDate", vacation.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));

        // then
        actual.andExpect(status().isOk())
                .andExpect(content().string(expected.toString()));

        verify(vacationService, times(1)).calculateVacationPay(vacation);
    }

    @Test
    void testCalculateVacationPay_withNullStartDate_shouldBeStatusOkEqualsResponseBody() throws Exception {
        // given
        Vacation vacation = VacationHelper.getVacation();
        vacation.setStartDate(null);
        log.info("vacation: {}", vacation);

        BigDecimal expected = new BigDecimal("34.16").setScale(2, RoundingMode.HALF_UP);

        Mockito.when(vacationService.calculateVacationPay(vacation)).thenReturn(expected);

        // when
        ResultActions actual = mockMvc.perform(get("/vacation/calculate")
                .param("averageSalary", vacation.getAverageSalary().toString())
                .param("vacationDays", String.valueOf(vacation.getVacationDays())));

        // then
        actual.andExpect(status().isOk())
                .andExpect(content().string(expected.toString()));

        verify(vacationService, times(1)).calculateVacationPay(vacation);
    }

    @Test
    void testCalculateVacationPay_withJsonProcessingException_shouldBeStatusInternalServerError() throws Exception {
        // given
        Vacation vacation = VacationHelper.getVacation();
        log.info("vacation: {}", vacation);

        Mockito.when(vacationService.calculateVacationPay(vacation)).thenThrow(new JsonProcessingException("Test Exception") {});

        // when
        ResultActions actual = mockMvc.perform(get("/vacation/calculate")
                .param("averageSalary", vacation.getAverageSalary().toString())
                .param("vacationDays", String.valueOf(vacation.getVacationDays()))
                .param("startDate", vacation.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));

        // then
        actual.andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to calculate vacation pay"));

        verify(vacationService, times(1)).calculateVacationPay(vacation);
    }

    @Test
    void testCalculateVacationPay_withConnectException_shouldBeStatusInternalServerError() throws Exception {
        // given
        Vacation vacation = VacationHelper.getVacation();
        log.info("vacation: {}", vacation);

        Mockito.when(vacationService.calculateVacationPay(vacation)).thenThrow(new ConnectException("Test Exception"));

        // when
        ResultActions actual = mockMvc.perform(get("/vacation/calculate")
                .param("averageSalary", vacation.getAverageSalary().toString())
                .param("vacationDays", String.valueOf(vacation.getVacationDays()))
                .param("startDate", vacation.getStartDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));

        // then
        actual.andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to calculate vacation pay"));

        verify(vacationService, times(1)).calculateVacationPay(vacation);
    }
}