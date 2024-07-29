package io.domainlifecycles.assertion;

import java.time.Instant;
import java.time.LocalDateTime;
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

public class TestAssertionsAfterOrEqual {

    @Nested
    class TestIsAfterOrEqual {

        @Test
        public void testIsAfterOrEqualToLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(LocalDateTime.now().plusDays(1), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((LocalDateTime) null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(Instant.now().plusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToInstantOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((LocalDateTime) null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(Instant.now().minusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(Date.from(Instant.now().plusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((Date) null, new Date(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(Date.from(Instant.now().minusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToCalendarOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((Date) null, new Date(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(Year.now().plusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((Year) null, Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(Year.now().minusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearMonthOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((Year) null, Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToZonedDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToMonthDayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(MonthDay.of(6,5), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToMonthDayOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToMonthDayFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(MonthDay.of(4,5), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(OffsetTime.of(6,5,0,0, ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(OffsetTime.of(4,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }
    }

    @Nested
    class TestOptionalIsAfterOrEqual {

        @Test
        public void testOptionalIsAfterOrEqualToOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetDateTime.now().plusDays(1)), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetDateTime.now().minusDays(1)), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetTime.of(6,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetTime.of(4,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDateTime.now().plusDays(1)), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDateTime.now().minusDays(1)), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Instant.now().plusSeconds(1)), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToInstantOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Instant.now().minusSeconds(1)), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToInstantFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Date.from(Instant.now().plusSeconds(1))), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToDateOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Date.from(Instant.now().minusSeconds(1))), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToDateFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToCalendarOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToCalendarFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Year.now().plusYears(1)), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Year.now().minusYears(1)), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(YearMonth.now().plusMonths(1)), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearMonthOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(YearMonth.now().minusMonths(1)), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearMonthFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(ZonedDateTime.now().plusDays(1)), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToZonedDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(ZonedDateTime.now().minusDays(1)), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToZonedDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToMonthDayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(MonthDay.of(6,5)), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToMonthDayOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToMonthDayFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(MonthDay.of(4,5)), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToMonthDayFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, MonthDay.of(5,5), "Failed"));
        }
    }
}
