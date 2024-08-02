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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsBeforeOrEqual {

    @Nested
    class TestIsBeforeOrEqualTo {

        @Test
        public void testIsBeforeOrEqualToLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(LocalDate.now().minusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(LocalDate.now(), (LocalDate) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(LocalTime.now().minusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalTimeOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((LocalTime) null, LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalTimeOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(LocalTime.now(), (LocalTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateTimeOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((LocalDateTime) null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateTimeOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(LocalDateTime.now(), (LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(LocalDateTime.now().plusDays(1), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(Instant.now().minusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToInstantOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((Instant) null, Instant.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToInstantOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(Instant.now(), (Instant) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(Instant.now().plusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(Date.from(Instant.now().minusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToDateOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((Date) null, new Date(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToDateOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(new Date(), (Date) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(Date.from(Instant.now().plusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToCalendarOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((Calendar) null, Calendar.getInstance(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToCalendarOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(Calendar.getInstance(), (Calendar) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(Year.now().minusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((Year) null, Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(Year.now(), (Year) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(Year.now().plusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearMonthOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((YearMonth) null, YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearMonthOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(YearMonth.now(), (YearMonth) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToZonedDateTimeOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToZonedDateTimeOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(ZonedDateTime.now(), (ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToMonthDayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(MonthDay.of(4,5), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToMonthDayOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((MonthDay) null, MonthDay.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToMonthDayOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(MonthDay.now(), (MonthDay) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToMonthDayFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(MonthDay.of(6,5), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetDateTimeOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetDateTimeOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(OffsetDateTime.now(), (OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(OffsetTime.of(4,5,0,0, ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetTimeOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((OffsetTime) null, OffsetTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetTimeOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(OffsetTime.now(), (OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(OffsetTime.of(6,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }
    }

    @Nested
    class TestOptionalIsBeforeOrEqualTo {

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDate.now().minusDays(1)), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDate.now().plusDays(1)), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalTime.now().minusHours(1)), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalTime.now().plusHours(1)), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetDateTime.now().minusDays(1)), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetDateTime.now().plusDays(1)), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetTime.of(4,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetTime.of(6,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDateTime.now().minusDays(1)), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDateTime.now().plusDays(1)), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Instant.now().minusSeconds(1)), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToInstantOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Instant.now().plusSeconds(1)), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToInstantFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Date.from(Instant.now().minusSeconds(1))), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToDateOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Date.from(Instant.now().plusSeconds(1))), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToDateFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToCalendarOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToCalendarFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Year.now().minusYears(1)), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Year.now().plusYears(1)), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(YearMonth.now().minusMonths(1)), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearMonthOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(YearMonth.now().plusMonths(1)), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearMonthFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(ZonedDateTime.now().minusDays(1)), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToZonedDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(ZonedDateTime.now().plusDays(1)), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToZonedDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToMonthDayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(MonthDay.of(4,5)), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToMonthDayOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToMonthDayFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(MonthDay.of(6,5)), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToMonthDayFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, MonthDay.of(5,5), "Failed"));
        }
    }
}
