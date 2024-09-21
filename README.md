# Калькулятор Отпускных

Калькулятор Отпускных — это приложение на Spring Boot, которое рассчитывает отпускные на основе средней зарплаты, количества дней отпуска и даты начала отпуска. Приложение также учитывает праздничные дни с использованием сервиса [AbstractAPI](https://www.abstractapi.com/). Используется бесплатный API key, который можно поменять в application.properties.



Выходные и праздничные дни учитываются при расчёте среднедневной зарплаты за прошедший год.

## Функции

- Расчет отпускных на основе средней зарплаты и количества дней отпуска.
- Учет праздничных дней с использованием сервиса AbstractAPI.
- Поддержка указания даты начала отпуска

## Vacation Pay Calculator
The Vacation Pay Calculator is a Spring Boot application that calculates vacation pay based on the average salary, number of vacation days, and the start date of the vacation. The application also takes into account public holidays using [AbstractAPI](https://www.abstractapi.com/). A free API key is used, which can be changed in application.properties.

Weekends and public holidays are taken into account when calculating the average daily salary for the past year.

## Features
Calculate vacation pay based on average salary and number of vacation days.

Account for public holidays using the AbstractAPI service.

Support for specifying the start date of the vacation.