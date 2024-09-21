package ru.neoflex.vacation_pay_service.strategies;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.neoflex.vacation_pay_service.helpers.VacationHelper;
import ru.neoflex.vacation_pay_service.models.Vacation;

public class VacationCalculationStrategyFactoryTest {
    @MockBean(name = "dateSpecifiedStrategy")
    private VacationCalculationStrategy dateSpecifiedStrategy;

    @MockBean(name = "dateNotSpecifiedStrategy")
    private VacationCalculationStrategy dateNotSpecifiedStrategy;

    @InjectMocks
    private VacationCalculationStrategyFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStrategy_DateSpecified() {
        // given
        Vacation vacation = VacationHelper.getVacation();

        // when
        VacationCalculationStrategy strategy = factory.getStrategy(vacation);

        // then
        assertSame(dateSpecifiedStrategy, strategy);
    }

    @Test
    void testGetStrategy_DateNotSpecified() {
        // given
        Vacation vacation = VacationHelper.getVacation();
        vacation.setStartDate(null);

        // when
        VacationCalculationStrategy strategy = factory.getStrategy(vacation);

        // then
        assertSame(dateNotSpecifiedStrategy, strategy);
    }

    @Configuration
    public static class MockConfiguration
    {
        @Bean
        @Qualifier("dateSpecifiedStrategy")
        public VacationCalculationStrategy dateSpecifiedStrategy() {
            return Mockito.mock(VacationCalculationStrategy.class);
        }
        @Bean
        @Qualifier("dateNotSpecifiedStrategy")
        public VacationCalculationStrategy dateNotSpecifiedStrategy() {
            return Mockito.mock(VacationCalculationStrategy.class);
        }
    }
}
