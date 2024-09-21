package ru.neoflex.vacation_pay_service.strategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.neoflex.vacation_pay_service.models.Vacation;
import java.math.BigDecimal;
import java.net.ConnectException;

public interface VacationCalculationStrategy {
    public BigDecimal calculate(Vacation vocation) throws JsonProcessingException, ConnectException;
}
