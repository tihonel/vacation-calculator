package ru.neoflex.vacation_pay_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.vacation_pay_service.models.Vacation;
import ru.neoflex.vacation_pay_service.services.VacationService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.time.LocalDate;

@RestController
@RequestMapping("/vacation")
@Slf4j
public class VacationController {

    private final VacationService vacationService;

    @Autowired
    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping("/calculate")
    public ResponseEntity<?> calculateVacationPay(
            @RequestParam("averageSalary") BigDecimal averageSalary,
            @RequestParam("vacationDays") int vacationDays,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate) {
        try {
            Vacation vacation = new Vacation(averageSalary.setScale(2, RoundingMode.HALF_UP),
                    vacationDays, startDate);
            log.info("Vacation: " + vacation);
            BigDecimal vacationPay = vacationService.calculateVacationPay(vacation);
            log.info("Vacation pay: " + vacationPay);
            return ResponseEntity.ok(vacationPay);
        } catch (JsonProcessingException | ConnectException e) {
            log.error("Error calculating vacation pay", e);
            return ResponseEntity.internalServerError().body("Failed to calculate vacation pay");
        }
    }
}