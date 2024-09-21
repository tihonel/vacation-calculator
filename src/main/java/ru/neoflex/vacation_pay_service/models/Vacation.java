package ru.neoflex.vacation_pay_service.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Vacation {
    @NonNull
    private BigDecimal averageSalary;
    @NonNull
    private int vacationDays;
    private LocalDate startDate;
}
