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

public class TestAssertionsPastOrPresent {

    @Nested
    class TestIsPastOrPresent {

        @Test
        public void testIsPastOrPresentLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(LocalDate.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((LocalDate) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(LocalDate.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(LocalTime.now().minusHours(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((LocalTime) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(LocalTime.now().plusHours(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(LocalDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(LocalDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(Instant.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentInstantOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((Instant) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(Instant.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(OffsetDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentOffsetDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(OffsetDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentOffsetTimeOk() {
            Assertions.assertDoesNotThrow(() -> DomainAssertions.isPastOrPresent(OffsetTime.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentOffsetTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentOffsetTimeFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isPastOrPresent(OffsetTime.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(Date.from(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastOrPresentDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((Date) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(Date.from(Instant.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastOrPresentCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastOrPresentCalendarOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((Calendar) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastOrPresentYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(Year.now().minusYears(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentYearOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((Year) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentYearFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(Year.now().plusYears(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(YearMonth.now().minusMonths(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentYearMonthOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((YearMonth) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentYearMonthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(YearMonth.now().plusMonths(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(ZonedDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentZonedDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(ZonedDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentMonthDayOk() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertDoesNotThrow(() -> DomainAssertions.isPastOrPresent(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)), "Failed"));
            }
        }

        @Test
        public void testIsPastOrPresentMonthDayOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((MonthDay) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentMonthDayFail() {
            if (!Month.from(LocalDate.now()).equals(Month.DECEMBER)) {
                Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isPastOrPresent(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)), "Failed"));
            }
        }
    }

    @Nested
    class TestOptionalIsPastOrPresent {

        @Test
        public void testOptionalIsPastOrPresentFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(LocalTime.now().plusHours(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(LocalTime.now().minusHours(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(Date.from(Instant.now().minusSeconds(1))), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(LocalDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(LocalDate.now().minusDays(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentCalendarOk(){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(Instant.now().minusSeconds(1)));

            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(calendar), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(Year.now().minusYears(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(ZonedDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(YearMonth.now().minusMonths(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentMonthDayOk(){
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS))), "Failed"));
            }
        }

        @Test
        public void testOptionalIsPastOrPresentOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(OffsetDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentOffsetTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(OffsetTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPastOrPresent(null, "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentFailWrongType(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPastOrPresent(Optional.of("String"), "Failed"));
        }
    }
}
