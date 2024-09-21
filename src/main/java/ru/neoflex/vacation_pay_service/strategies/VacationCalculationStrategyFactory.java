package ru.neoflex.vacation_pay_service.strategies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.neoflex.vacation_pay_service.models.Vacation;

@Component
@Slf4j
public class VacationCalculationStrategyFactory {
    private final VacationCalculationStrategy dateSpecifiedStrategy;

    private final VacationCalculationStrategy dateNotSpecifiedStrategy;

    @Autowired
    public VacationCalculationStrategyFactory(@Qualifier("dateSpecifiedStrategy")
                                              VacationCalculationStrategy dateSpecifiedStrategy,
                                              @Qualifier("dateNotSpecifiedStrategy")
                                              VacationCalculationStrategy dateNotSpecifiedStrategy) {
        this.dateSpecifiedStrategy = dateSpecifiedStrategy;
        this.dateNotSpecifiedStrategy = dateNotSpecifiedStrategy;
    }

    public VacationCalculationStrategy getStrategy(Vacation vacation) {
        if (vacation.getStartDate() != null) {
            log.info("Using dateSpecifiedStrategy for: {}", vacation);
            return dateSpecifiedStrategy;
        } else {
            log.info("Using dateNotSpecifiedStrategy for: {}", vacation);
            return dateNotSpecifiedStrategy;
        }
    }
}
