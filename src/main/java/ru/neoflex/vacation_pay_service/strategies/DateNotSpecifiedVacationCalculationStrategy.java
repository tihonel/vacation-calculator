package ru.neoflex.vacation_pay_service.strategies;

import org.springframework.stereotype.Component;
import ru.neoflex.vacation_pay_service.models.Vacation;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component("dateNotSpecifiedStrategy")
public class DateNotSpecifiedVacationCalculationStrategy implements VacationCalculationStrategy {
    @Override
    public BigDecimal calculate(Vacation vacation) {
        double dailySalary = vacation.getAverageSalary().doubleValue() / 29.3 * vacation.getVacationDays();
        return new BigDecimal(dailySalary).setScale(2, RoundingMode.HALF_UP);
    }
}
