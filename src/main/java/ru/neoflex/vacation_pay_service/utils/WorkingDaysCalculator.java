package ru.neoflex.vacation_pay_service.utils;

import java.time.LocalDate;
import java.util.Set;

public interface WorkingDaysCalculator {
    public long getQuantityOfWorkingDays(Set<LocalDate> holiday, LocalDate lastWorkingDay);
}
