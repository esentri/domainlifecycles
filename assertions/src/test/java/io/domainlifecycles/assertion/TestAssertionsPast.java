package io.domainlifecycles.assertion;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsPast {

    @Nested
    class TestIsPast {

        @Test
        public void testIsPastLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(LocalDate.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastLocalDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((LocalDate) null, "Failed"));
        }

        @Test
        public void testIsPastLocalDateFailFuture(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(LocalDate.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsPastLocalDateFailPresent(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsPastLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(LocalTime.now().minusHours(1), "Failed"));
        }

        @Test
        public void testIsPastLocalTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((LocalTime) null, "Failed"));
        }

        @Test
        public void testIsPastLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(LocalTime.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(LocalDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastLocalDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(LocalDateTime.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(Instant.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastInstantOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((Instant) null, "Failed"));
        }

        @Test
        public void testIsPastInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(Instant.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(ZonedDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastZonedDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(ZonedDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsPastMonthDayOk() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertDoesNotThrow(() -> DomainAssertions.isPast(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)), "Failed"));
            }
        }

        @Test
        public void testIsPastMonthDayOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((MonthDay) null, "Failed"));
        }

        @Test
        public void testIsPastMonthDayFailFuture() {
            if (!Month.from(LocalDate.now()).equals(Month.DECEMBER)) {
                Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isPast(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)), "Failed"));
            }
        }

        @Test
        public void testIsPastMonthDayFailPresent() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isPast(MonthDay.from(LocalDate.now()), "Failed"));
        }

        @Test
        public void testIsPastOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(OffsetDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOffsetDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(OffsetDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOffsetTimeOk() {
            Assertions.assertDoesNotThrow(() -> DomainAssertions.isPast(OffsetTime.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastOffsetTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsPastOffsetTimeFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isPast(OffsetTime.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(Date.from(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((Date) null, "Failed"));
        }

        @Test
        public void testIsPastDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(Date.from(Instant.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastCalendarOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((Calendar) null, "Failed"));
        }

        @Test
        public void testIsPastCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(Year.now().minusYears(1), "Failed"));
        }

        @Test
        public void testIsPastYearOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((Year) null, "Failed"));
        }

        @Test
        public void testIsPastYearFailFuture(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(Year.now().plusYears(1), "Failed"));
        }

        @Test
        public void testIsPastYearFailPresent(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(Year.now(), "Failed"));
        }

        @Test
        public void testIsPastYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(YearMonth.now().minusMonths(1), "Failed"));
        }

        @Test
        public void testIsPastYearMonthOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((YearMonth) null, "Failed"));
        }

        @Test
        public void testIsPastYearMonthFailFuture(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(YearMonth.now().plusMonths(1), "Failed"));
        }

        @Test
        public void testIsPastYearMonthFailPresent(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(YearMonth.now(), "Failed"));
        }
    }

    @Nested
    class TestOptionalIsPast {

        @Test
        public void testOptionalIsPastLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsPast(Optional.of(LocalTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(LocalTime.now().minusSeconds(1)), "Failed"));
        }


        @Test
        public void testOptionalIsPastLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(LocalDate.now().minusDays(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(LocalDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(Date.from(Instant.now().minusSeconds(1))), "Failed"));
        }

        @Test
        public void testOptionalIsPastCalendarOk(){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(Instant.now().minusSeconds(1)));

            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(calendar), "Failed"));
        }

        @Test
        public void testOptionalIsPastInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(Year.now().minusYears(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(ZonedDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(YearMonth.now().minusMonths(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastMonthDayOk(){
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertDoesNotThrow(() -> DomainAssertions.optionalIsPast(Optional.of(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS))), "Failed"));
            }
        }

        @Test
        public void testOptionalIsPastOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(OffsetDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(OffsetTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsPastFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPast(null, "Failed"));
        }

        @Test
        public void testOptionalIsPastFailWrongType(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPast(Optional.of("String"), "Failed"));
        }
    }
}
