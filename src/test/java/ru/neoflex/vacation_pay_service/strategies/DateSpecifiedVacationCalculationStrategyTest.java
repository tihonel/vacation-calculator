package ru.neoflex.vacation_pay_service.strategies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.neoflex.vacation_pay_service.models.Vacation;
import ru.neoflex.vacation_pay_service.services.HolidaysApiService;
import ru.neoflex.vacation_pay_service.utils.WorkingDaysCalculator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DateSpecifiedVacationCalculationStrategyTest {
    @Mock
    private HolidaysApiService holidaysApiService;

    @Mock
    private WorkingDaysCalculator workingDaysCalculator;

    @InjectMocks
    private DateSpecifiedVacationCalculationStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculate() throws JsonProcessingException, ConnectException {
        // given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        int vacationDays = 10;
        BigDecimal averageSalary = new BigDecimal("50000");
        Vacation vacation = new Vacation(averageSalary, vacationDays, startDate);

        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(LocalDate.of(2022, 12, 31));
        holidays.add(LocalDate.of(2023, 1, 1));

        when(holidaysApiService.getHolidays(2023)).thenReturn(holidays);
        when(holidaysApiService.getHolidays(2022)).thenReturn(new HashSet<>());
        when(workingDaysCalculator.getQuantityOfWorkingDays(holidays, startDate.minusDays(1))).thenReturn(247L);

        // when
        BigDecimal result = strategy.calculate(vacation);


        // then
        BigDecimal expected = new BigDecimal((double) 50000 * 12/ 247 * vacationDays).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void testCalculateWithNoHolidays() throws JsonProcessingException, ConnectException {
        // given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        int vacationDays = 10;
        BigDecimal averageSalary = new BigDecimal("50000");
        Vacation vacation = new Vacation(averageSalary, vacationDays, startDate);

        Set<LocalDate> holidays = new HashSet<>();

        when(holidaysApiService.getHolidays(2023)).thenReturn(holidays);
        when(holidaysApiService.getHolidays(2022)).thenReturn(new HashSet<>());
        when(workingDaysCalculator.getQuantityOfWorkingDays(holidays, startDate.minusDays(1))).thenReturn(247L);

        // when
        BigDecimal result = strategy.calculate(vacation);

        // then
        BigDecimal expected = new BigDecimal((double) 50000 * 12 / 247 * vacationDays).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, result);
    }

    @Test
    void testCalculateWithJsonProcessingException() throws JsonProcessingException, ConnectException {
        // given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        int vacationDays = 10;
        BigDecimal averageSalary = new BigDecimal("50000").setScale(2, RoundingMode.HALF_UP);;
        Vacation vacation = new Vacation(averageSalary, vacationDays, startDate);

        when(holidaysApiService.getHolidays(2023)).thenThrow(new JsonProcessingException("Invalid JSON") {});

        // when & then
        JsonProcessingException exception = assertThrows(JsonProcessingException.class, () -> {
            strategy.calculate(vacation);
        });
        assertEquals("Invalid JSON", exception.getMessage());
    }

    @Test
    void testCalculateWithConnectException() throws JsonProcessingException, ConnectException {
        // given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        int vacationDays = 10;
        BigDecimal averageSalary = new BigDecimal("50000").setScale(2, RoundingMode.HALF_UP);;
        Vacation vacation = new Vacation(averageSalary, vacationDays, startDate);

        when(holidaysApiService.getHolidays(2023)).thenThrow(new ConnectException("Connection failed") {});

        // when & then
        ConnectException exception = assertThrows(ConnectException.class, () -> {
            strategy.calculate(vacation);
        });
        assertEquals("Connection failed", exception.getMessage());
    }
}
