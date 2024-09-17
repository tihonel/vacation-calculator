package ru.neoflex.vacation_pay_service.services;

import ru.neoflex.vacation_pay_service.models.Vocation;
import java.math.BigDecimal;

public interface VacationService {
    public BigDecimal calculateVacationPay(Vocation vocation);
}
