package ru.neoflex.vacation_pay_service.strategies;

import ru.neoflex.vacation_pay_service.models.Vocation;
import java.math.BigDecimal;

public interface VacationCalculationStrategy {
    public BigDecimal calculate(Vocation vocation);
}
