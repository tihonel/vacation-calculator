package ru.neoflex.vacation_pay_service.strategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.neoflex.vacation_pay_service.models.Vacation;
import ru.neoflex.vacation_pay_service.services.HolidaysApiService;
import ru.neoflex.vacation_pay_service.utils.WorkingDaysCalculator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.time.LocalDate;
import java.util.Set;

@Component("dateSpecifiedStrategy")
public class DateSpecifiedVacationCalculationStrategy implements VacationCalculationStrategy {
    private final HolidaysApiService holidaysApiService;
    private final WorkingDaysCalculator workingDaysCalculator;

    @Autowired
    public DateSpecifiedVacationCalculationStrategy(HolidaysApiService holidaysApiService, WorkingDaysCalculator workingDaysCalculator) {
        this.holidaysApiService = holidaysApiService;
        this.workingDaysCalculator = workingDaysCalculator;
    }

    @Override
    public BigDecimal calculate(Vacation vocation) throws JsonProcessingException, ConnectException {
        Set<LocalDate> holidays = holidaysApiService.getHolidays(vocation.getStartDate().getYear());
        holidays.addAll(holidaysApiService.getHolidays(vocation.getStartDate().getYear() - 1));

        long workingDays = workingDaysCalculator.getQuantityOfWorkingDays(holidays, vocation.getStartDate().minusDays(1));

        double dailySalary = vocation.getAverageSalary().doubleValue() * 12 / workingDays;
        return new BigDecimal(dailySalary * vocation.getVacationDays()).setScale(2, RoundingMode.HALF_UP);
    }
}
