package ru.neoflex.vacation_pay_service.strategies;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import ru.neoflex.vacation_pay_service.models.Vacation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;

public class DateNotSpecifiedVacationCalculationStrategyTest {
    private final VacationCalculationStrategy strategy = new DateNotSpecifiedVacationCalculationStrategy();

    @Test
    public void testCalculateVacationPayWithPositiveDays() throws JsonProcessingException, ConnectException {
        // given
        double averageSalary = 120000;
        int vacationDays = 14;
        Vacation vacation = new Vacation();
        vacation.setAverageSalary(new BigDecimal(averageSalary).setScale(2, RoundingMode.HALF_UP));
        vacation.setVacationDays(vacationDays);

        // when
        BigDecimal actual = strategy.calculate(vacation);

        // then
        BigDecimal expected = new BigDecimal(averageSalary / 29.3 * vacationDays).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateVacationPayWithMinimumPositiveDays() throws JsonProcessingException, ConnectException {
        // given
        double averageSalary = 1000.88;
        int vacationDays = 1;
        Vacation vacation = new Vacation();
        vacation.setAverageSalary(new BigDecimal(averageSalary).setScale(2, RoundingMode.HALF_UP));
        vacation.setVacationDays(vacationDays);

        // when
        BigDecimal actual = strategy.calculate(vacation);

        // then
        BigDecimal expected = new BigDecimal(averageSalary / 29.3 * vacationDays).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateVacationPayWithLargeNumberOfDays() throws JsonProcessingException, ConnectException {
        // given
        double averageSalary = 140000;
        int vacationDays = 28;
        Vacation vocation = new Vacation();
        vocation.setAverageSalary(new BigDecimal(averageSalary).setScale(2, RoundingMode.HALF_UP));
        vocation.setVacationDays(vacationDays);

        // when
        BigDecimal actual = strategy.calculate(vocation);

        // then
        BigDecimal expected = new BigDecimal(averageSalary / 29.3 * vacationDays).setScale(2, RoundingMode.HALF_UP);;
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateVacationPayWithSmallSalary() throws JsonProcessingException, ConnectException {
        // given
        double averageSalary = 50;
        int vacationDays = 28;
        Vacation vacation = new Vacation();
        vacation.setAverageSalary(new BigDecimal(averageSalary).setScale(2, RoundingMode.HALF_UP));
        vacation.setVacationDays(vacationDays);

        // when
        BigDecimal actual = strategy.calculate(vacation);

        // then
        BigDecimal expected = new BigDecimal(averageSalary / 29.3 * vacationDays).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, actual);
    }
}
