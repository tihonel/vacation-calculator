package ru.neoflex.vacation_pay_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.neoflex.vacation_pay_service.models.Vacation;
import ru.neoflex.vacation_pay_service.strategies.VacationCalculationStrategy;
import ru.neoflex.vacation_pay_service.strategies.VacationCalculationStrategyFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class VacationServiceImpTest {
    @Mock
    private VacationCalculationStrategyFactory factory;

    @Mock
    private VacationCalculationStrategy strategy;

    @InjectMocks
    private VacationServiceImp vacationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateVacationPay() throws Exception {
        // given
        Vacation vacation = new Vacation();
        vacation.setAverageSalary(new BigDecimal("1000.88").setScale(2, RoundingMode.HALF_UP));
        vacation.setVacationDays(1);

        BigDecimal expected = new BigDecimal("34.16").setScale(2, RoundingMode.HALF_UP);

        when(factory.getStrategy(vacation)).thenReturn(strategy);
        when(strategy.calculate(vacation)).thenReturn(expected);

        // when
        BigDecimal actual = vacationService.calculateVacationPay(vacation);

        // then
        assertEquals(expected, actual);
        verify(factory, times(1)).getStrategy(vacation);
        verify(strategy, times(1)).calculate(vacation);
    }
}
