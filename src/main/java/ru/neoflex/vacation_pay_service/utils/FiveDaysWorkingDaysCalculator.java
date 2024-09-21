package ru.neoflex.vacation_pay_service.utils;

import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class FiveDaysWorkingDaysCalculator implements WorkingDaysCalculator {
    @Override
    public long getQuantityOfWorkingDays(Set<LocalDate> holidays, LocalDate lastWorkingPeriodDate) {
        Predicate<LocalDate> isHoliday = holidays::contains;
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        LocalDate startWorkingPeriodDate = lastWorkingPeriodDate.minusYears(1);

        long workingPeriod = ChronoUnit.DAYS.between(startWorkingPeriodDate, lastWorkingPeriodDate);

        return Stream.iterate(startWorkingPeriodDate, date -> date.plusDays(1))
                .limit(workingPeriod)
                .filter(isHoliday.or(isWeekend).negate()).count();

    }
}
