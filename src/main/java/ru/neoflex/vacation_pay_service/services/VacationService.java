package ru.neoflex.vacation_pay_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.neoflex.vacation_pay_service.models.Vacation;
import java.math.BigDecimal;
import java.net.ConnectException;

public interface VacationService {
    public BigDecimal calculateVacationPay(Vacation vocation) throws JsonProcessingException, ConnectException;
}
