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

public class TestAssertionsFutureOrPresent {

    @Nested
    class TestIsFutureOrPresent {

        @Test
        public void testIsFutureOrPresentLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(LocalDate.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((LocalDate) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(LocalDate.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(LocalTime.now().plusHours(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((LocalTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(LocalTime.now().minusHours(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(OffsetDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentOffsetDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(OffsetDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentOffsetTimeOk() {
            Assertions.assertDoesNotThrow(() -> DomainAssertions.isFutureOrPresent(OffsetTime.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentOffsetTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentOffsetTimeFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isFutureOrPresent(OffsetTime.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(LocalDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(LocalDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(Instant.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentInstantOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((Instant) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(Instant.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(Date.from(Instant.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((Date) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(Date.from(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentCalendarOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((Calendar) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(Year.now().plusYears(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentYearOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((Year) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(Year.now().minusYears(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(YearMonth.now().plusMonths(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentYearMonthOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((YearMonth) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(YearMonth.now().minusMonths(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(ZonedDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentZonedDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(ZonedDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentMonthDayOk() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertDoesNotThrow(() -> DomainAssertions.isFutureOrPresent(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)), "Failed"));
            }
        }

        @Test
        public void testIsFutureOrPresentMonthDayOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((MonthDay) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentMonthDayFail() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isFutureOrPresent(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)), "Failed"));
            }
        }
    }

    @Nested
    class TestOptionalIsFutureOrPresent {

        @Test
        public void testOptionalIsFutureOrPresentLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalTime.now().minusHours(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalTime.now().plusHours(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(null, "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentFailWrongType(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of("String"), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalDate.now().plusDays(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalDate.now().minusDays(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Date.from(Instant.now().plusSeconds(1))), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Date.from(Instant.now().minusSeconds(1))), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentCalendarOk(){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(Instant.now().plusSeconds(1)));

            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(calendar), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentCalendarFail(){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(Instant.now().minusSeconds(1)));

            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(calendar), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Year.now().plusYears(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Year.now().minusYears(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(ZonedDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(ZonedDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(YearMonth.now().plusMonths(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(YearMonth.now().minusMonths(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentMonthDayOk(){
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS))), "Failed"));
            }
        }

        @Test
        public void testOptionalIsFutureOrPresentMonthDayFail(){
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS))), "Failed"));
            }
        }

        @Test
        public void testOptionalIsFutureOrPresentOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(OffsetDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(OffsetDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(OffsetTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentOffsetTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(OffsetTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Instant.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Instant.now().plusSeconds(1)), "Failed"));
        }
    }
}
