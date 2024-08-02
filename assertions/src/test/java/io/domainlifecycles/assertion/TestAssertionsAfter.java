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

public class TestAssertionsAfter {

    @Nested
    class TestIsAfter {

        @Test
        public void testIsAfterLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalDateOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalDateOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(LocalDate.now(), (LocalDate) null, "Failed"));
        }

        @Test
        public void testIsAfterLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(LocalDate.now().minusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalTimeOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((LocalTime) null, LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalTimeOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(LocalTime.now(), (LocalTime) null, "Failed"));
        }

        @Test
        public void testIsAfterLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(LocalTime.now().minusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(LocalDateTime.now().plusDays(1), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalDateTimeOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((LocalDateTime) null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalDateTimeOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(LocalDateTime.now(), (LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsAfterLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(Instant.now().plusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsAfterInstantOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((Instant) null, Instant.now(), "Failed"));
        }

        @Test
        public void testIsAfterInstantOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(Instant.now(), (Instant) null, "Failed"));
        }

        @Test
        public void testIsAfterInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(Instant.now().minusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsAfterYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(Year.now().plusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterYearOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((Year) null, Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterYearOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(Year.now(), (Year) null, "Failed"));
        }

        @Test
        public void testIsAfterYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(Year.now().minusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsAfterYearMonthOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((YearMonth) null, YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsAfterYearMonthOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(YearMonth.now(), (YearMonth) null, "Failed"));
        }

        @Test
        public void testIsAfterYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsAfterZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterZonedDateTimeOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((ZonedDateTime) null, ZonedDateTime.now(),"Failed"));
        }

        @Test
        public void testIsAfterZonedDateTimeOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(ZonedDateTime.now(), (ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsAfterZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterMonthDayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(MonthDay.of(6,5), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testIsAfterMonthDayOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((MonthDay) null, MonthDay.now(), "Failed"));
        }

        @Test
        public void testIsAfterMonthDayOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(MonthDay.now(), (MonthDay) null, "Failed"));
        }

        @Test
        public void testIsAfterMonthDayFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(MonthDay.of(4,5), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testIsAfterOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOffsetDateTimeOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOffsetDateTimeOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(OffsetDateTime.now(), (OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsAfterOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(OffsetTime.of(6,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testIsAfterOffsetTimeOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((OffsetTime) null, OffsetTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOffsetTimeOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(OffsetTime.now(), (OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsAfterOffsetTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(OffsetTime.of(4,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testIsAfterDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(Date.from(Instant.now().plusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsAfterDateOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((Date) null, new Date(), "Failed"));
        }

        @Test
        public void testIsAfterDateOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(new Date(), (Date) null, "Failed"));
        }

        @Test
        public void testIsAfterDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(Date.from(Instant.now().minusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsAfterCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsAfterCalendarOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((Calendar) null, Calendar.getInstance(), "Failed"));
        }

        @Test
        public void testIsAfterCalendarOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(Calendar.getInstance(), (Calendar) null, "Failed"));
        }

        @Test
        public void testIsAfterCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), new GregorianCalendar(), "Failed"));
        }
    }

    @Nested
    class TestOptionalIsAfter {

        @Test
        public void testOptionalIsAfterLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(LocalDate.now().plusDays(1)), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(LocalDate.now().minusDays(1)), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(LocalTime.now().plusHours(1)), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(LocalTime.now().minusHours(1)), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(LocalDateTime.now().plusDays(1)), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(LocalDateTime.now().minusDays(1)), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(Instant.now().plusSeconds(1)), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterInstantOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(Instant.now().minusSeconds(1)), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterInstantFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(ZonedDateTime.now().plusDays(1)), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterZonedDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(ZonedDateTime.now().minusDays(1)), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterZonedDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterMonthDayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(MonthDay.of(6,5)), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsAfterMonthDayOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsAfterMonthDayFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(MonthDay.of(4,5)), MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsAfterMonthDayFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, MonthDay.of(5,5), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(OffsetDateTime.now().plusDays(1)), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetDateTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(OffsetDateTime.now().minusDays(1)), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetDateTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(OffsetTime.of(6,5,0,0, ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(OffsetTime.of(4,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(Date.from(Instant.now().plusSeconds(1))), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterDateOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(Date.from(Instant.now().minusSeconds(1))), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterDateFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterCalendarOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterCalendarFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(Year.now().plusYears(1)), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(Year.now().minusYears(1)), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(YearMonth.now().plusMonths(1)), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearMonthOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(YearMonth.now().minusMonths(1)), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearMonthFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, YearMonth.now(), "Failed"));
        }
    }
}
