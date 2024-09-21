package ru.neoflex.vacation_pay_service.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FiveDaysWorkingDaysCalculatorTest {
    private FiveDaysWorkingDaysCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new FiveDaysWorkingDaysCalculator();
    }

    @Test
    void testGetQuantityOfWorkingDays_NoHolidays() {
        // given
        Set<LocalDate> holidays = new HashSet<>();
        LocalDate lastWorkingPeriodDate = LocalDate.of(2023, 10, 1);

        // when
        long workingDays = calculator.getQuantityOfWorkingDays(holidays, lastWorkingPeriodDate);

        // then
        assertEquals(52 * 5, workingDays);
    }

    @Test
    void testGetQuantityOfWorkingDays_WithHolidays() {
        // given
        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(LocalDate.of(2023, 1, 1));
        holidays.add(LocalDate.of(2023, 12, 25));
        LocalDate lastWorkingPeriodDate = LocalDate.of(2023, 12, 31);

        // when
        long workingDays = calculator.getQuantityOfWorkingDays(holidays, lastWorkingPeriodDate);

        // then
        assertEquals(52 * 5 - countWeekends(holidays), workingDays);
    }

    @Test
    void testGetQuantityOfWorkingDays_WithWeekendHolidays() {
        // given
        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(LocalDate.of(2023, 1, 1));
        holidays.add(LocalDate.of(2023, 12, 25));
        LocalDate lastWorkingPeriodDate = LocalDate.of(2023, 12, 31);

        // when
        long workingDays = calculator.getQuantityOfWorkingDays(holidays, lastWorkingPeriodDate);

        // then
        assertEquals(52 * 5 - countWeekends(holidays), workingDays);
    }

    @Test
    void testGetQuantityOfWorkingDays_WithWeekend() {
        // given
        Set<LocalDate> holidays = new HashSet<>();
        LocalDate lastWorkingPeriodDate = LocalDate.of(2023, 10, 1);

        // when
        long workingDays = calculator.getQuantityOfWorkingDays(holidays, lastWorkingPeriodDate);

        // then
        assertEquals(52 * 5 - countWeekends(holidays), workingDays);
    }

    @Test
    void testGetQuantityOfWorkingDays_WithWeekendAndHolidays() {
        // given
        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(LocalDate.of(2023, 1, 1));
        holidays.add(LocalDate.of(2023, 12, 25));
        LocalDate lastWorkingPeriodDate = LocalDate.of(2023, 12, 31);

        // when
        long workingDays = calculator.getQuantityOfWorkingDays(holidays, lastWorkingPeriodDate);

        // then
        assertEquals(52 * 5 - countWeekends(holidays), workingDays);
    }

    private static long countWeekends(Set<LocalDate> holidays) {
        return holidays.stream()
                .filter(FiveDaysWorkingDaysCalculatorTest::isNotWeekend)
                .count();
    }

    private static boolean isNotWeekend(LocalDate date) {
        return !(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
    }
}
