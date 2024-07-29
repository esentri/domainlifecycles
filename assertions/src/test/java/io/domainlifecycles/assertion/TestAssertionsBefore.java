package io.domainlifecycles.assertion;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsBefore {

    @Nested
    class TestIsBefore {

        @Test
        public void testIsBeforeLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(LocalDate.now().minusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(LocalTime.now().minusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((LocalDateTime) null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(LocalDateTime.now().plusDays(1), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(Instant.now().minusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsBeforeInstantOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((LocalDateTime) null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(Instant.now().plusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsBeforeDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(Date.from(Instant.now().minusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsBeforeDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((Date) null, new Date(), "Failed"));
        }

        @Test
        public void testIsBeforeDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(Date.from(Instant.now().plusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsBeforeCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsBeforeCalendarOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((Date) null, new Date(), "Failed"));
        }

        @Test
        public void testIsBeforeCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsBeforeYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(Year.now().minusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeYearOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((Year) null, Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(Year.now().plusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsBeforeYearMonthOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((Year) null, Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsBeforeZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeZonedDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeMonthDayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(MonthDay.of(4,5), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testIsBeforeMonthDayOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeMonthDayFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(MonthDay.of(6,5), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testIsBeforeOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOffsetDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(OffsetTime.of(4,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testIsBeforeOffsetTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOffsetTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(OffsetTime.of(6,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }
    }

    @Nested
    class TestOptionalIsBefore {

        @Test
        public void testOptionalIsBeforeLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(LocalDate.now().minusDays(1)), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(LocalDate.now().plusDays(1)), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(LocalTime.now().minusHours(1)), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(LocalTime.now().plusHours(1)), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(LocalDateTime.now().minusDays(1)), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(LocalDateTime.now().plusDays(1)), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(Instant.now().minusSeconds(1)), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeInstantOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(Instant.now().plusSeconds(1)), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeInstantFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(Date.from(Instant.now().minusSeconds(1))), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeDateOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(Date.from(Instant.now().plusSeconds(1))), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeDateFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeCalendarOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeCalendarFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(Year.now().minusYears(1)), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(Year.now().plusYears(1)), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(YearMonth.now().minusMonths(1)), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearMonthOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(YearMonth.now().plusMonths(1)), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearMonthFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(ZonedDateTime.now().minusDays(1)), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeZonedDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(ZonedDateTime.now().plusDays(1)), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeZonedDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeMonthDayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(MonthDay.of(4,5)), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeMonthDayOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeMonthDayFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(MonthDay.of(6,5)), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeMonthDayFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(OffsetDateTime.now().minusDays(1)), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(OffsetDateTime.now().plusDays(1)), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(OffsetTime.of(4,5,0,0, ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(OffsetTime.of(6,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }
    }
}
