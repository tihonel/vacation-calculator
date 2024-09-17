package ru.neoflex.vacation_pay_service.strategies;

import org.springframework.stereotype.Component;
import ru.neoflex.vacation_pay_service.models.Vocation;
import java.math.BigDecimal;

@Component("dateSpecifiedStrategy")
public class DateSpecifiedVacationCalculationStrategy implements VacationCalculationStrategy {
    @Override
    public BigDecimal calculate(Vocation vocation) {
        return null;
    }
}
