package ru.neoflex.vacation_pay_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.neoflex.vacation_pay_service.models.Vacation;
import ru.neoflex.vacation_pay_service.strategies.VacationCalculationStrategy;
import ru.neoflex.vacation_pay_service.strategies.VacationCalculationStrategyFactory;
import java.math.BigDecimal;
import java.net.ConnectException;

@Service
public class VacationServiceImp implements VacationService{
    private final VacationCalculationStrategyFactory factory;

    @Autowired
    public VacationServiceImp(VacationCalculationStrategyFactory factory) {
        this.factory = factory;
    }

    @Override
    public BigDecimal calculateVacationPay(Vacation vacation) throws JsonProcessingException, ConnectException {
        VacationCalculationStrategy strategy = factory.getStrategy(vacation);
        return strategy.calculate(vacation);
    }
}
