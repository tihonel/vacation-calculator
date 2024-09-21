package ru.neoflex.vacation_pay_service.helpers;

import ru.neoflex.vacation_pay_service.models.Vacation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class VacationHelper {
    public static Vacation getVacation(){
        Vacation vacation = new Vacation();
        vacation.setStartDate(LocalDate.of(2024,10,1));
        vacation.setAverageSalary(new BigDecimal(50000).setScale(2, RoundingMode.CEILING));
        vacation.setVacationDays(14);
        return vacation;
    }
}
