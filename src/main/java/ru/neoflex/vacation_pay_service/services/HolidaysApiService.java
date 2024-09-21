package ru.neoflex.vacation_pay_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.ConnectException;
import java.time.LocalDate;
import java.util.Set;

public interface HolidaysApiService {
    public Set<LocalDate> getHolidays(int year) throws JsonProcessingException, ConnectException;
}
