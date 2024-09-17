package ru.neoflex.vacation_pay_service.strategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class VacationCalculationStrategyFactory {
    @Autowired
    @Qualifier("dateSpecifiedStrategy")
    private VacationCalculationStrategy dateSpecifiedStrategy;

    @Autowired
    @Qualifier("dateNotSpecifiedStrategy")
    private VacationCalculationStrategy dateNotSpecifiedStrategy;

    public VacationCalculationStrategy getStrategy(Vacation requestDTO) {
        if (requestDTO.getStartDate() != null) {
            return dateSpecifiedStrategy;
        } else {
            return dateNotSpecifiedStrategy;
        }
    }
}
}
