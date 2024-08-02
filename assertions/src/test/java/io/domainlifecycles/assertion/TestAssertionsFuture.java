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

public class TestAssertionsFuture {

    @Nested
    class TestIsFuture {

        @Test
        public void testIsFutureLocalDateFailPast(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(LocalDate.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureLocalDateFailPresent(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsFutureLocalDateOkFuture(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(LocalDate.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureLocalDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((LocalDate) null, "Failed"));
        }

        @Test
        public void testIsFutureLocalTimeFailPast(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(LocalTime.now().minusHours(1), "Failed"));
        }

        @Test
        public void testIsFutureLocalTimeFailPresent(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsFutureLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(LocalTime.now().plusHours(1), "Failed"));
        }

        @Test
        public void testIsFutureLocalTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((LocalTime) null, "Failed"));
        }

        @Test
        public void testIsFutureLocalDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(LocalDateTime.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureLocalDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(LocalDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureLocalDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureInstantFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(Instant.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureInstantOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(Instant.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureInstantOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((Instant) null, "Failed"));
        }

        @Test
        public void testIsFutureDateFailPast(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(Date.from(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureDateFailPresent(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(Date.from(Instant.now()), "Failed"));
        }

        @Test
        public void testIsFutureDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(Date.from(Instant.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((Date) null, "Failed"));
        }

        @Test
        public void testIsFutureCalendarFailPast(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureCalendarFailPresent(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(GregorianCalendar.from(ZonedDateTime.now()), "Failed"));
        }

        @Test
        public void testIsFutureCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureCalendarOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((Calendar) null, "Failed"));
        }

        @Test
        public void testIsFutureYearFailPast(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(Year.now().minusYears(1), "Failed"));
        }

        @Test
        public void testIsFutureYearFailPresent(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(Year.now(), "Failed"));
        }

        @Test
        public void testIsFutureYearOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(Year.now().plusYears(1), "Failed"));
        }

        @Test
        public void testIsFutureYearOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((Year) null, "Failed"));
        }

        @Test
        public void testIsFutureYearMonthFailPast(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(YearMonth.now().minusMonths(1), "Failed"));
        }

        @Test
        public void testIsFutureYearMonthFailPresent(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsFutureYearMonthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(YearMonth.now().plusMonths(1), "Failed"));
        }

        @Test
        public void testIsFutureYearMonthOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((YearMonth) null, "Failed"));
        }

        @Test
        public void testIsFutureZonedDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(ZonedDateTime.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureZonedDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(ZonedDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureZonedDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureMonthDayFailPast(){
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)), "Failed"));
            }
        }

        @Test
        public void testIsFutureMonthDayFailPresent(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(MonthDay.from(LocalDate.now()), "Failed"));
        }

        @Test
        public void testIsFutureMonthDayOk() {
            if (!Month.from(LocalDate.now()).equals(Month.DECEMBER)) {
                Assertions.assertDoesNotThrow(() -> DomainAssertions.isFuture(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)), "Failed"));
            }
        }

        @Test
        public void testIsFutureMonthDayOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((MonthDay) null, "Failed"));
        }

        @Test
        public void testIsFutureOffsetDateTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(OffsetDateTime.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureOffsetDateTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(OffsetDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOffsetDateTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOffsetTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(OffsetTime.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureOffsetTimeOk() {
            Assertions.assertDoesNotThrow(() -> DomainAssertions.isFuture(OffsetTime.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureOffsetTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((OffsetTime) null, "Failed"));
        }
    }

    @Nested
    class TestOptionalIsFuture {

        @Test
        public void testOptionalIsFutureLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of(LocalTime.now().minusHours(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(LocalTime.now().plusHours(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureLocalTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsFutureLocalTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFuture(null, "Failed"));
        }

        @Test
        public void testOptionalIsFutureLocalTimeFailWrongType(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of("String"), "Failed"));
        }

        @Test
        public void testOptionalIsFutureFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(Instant.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsFutureFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFuture(null, "Failed"));
        }

        @Test
        public void testOptionalIsFutureFailWrongType(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of("String"), "Failed"));
        }

        @Test
        public void testOptionalLocalDateTimeIsFutureFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFuture(Optional.of(LocalDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalLocalDateTimeIsFutureOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(LocalDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalLocalDateIsFutureFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFuture(Optional.of(LocalDate.now().minusDays(1)), "Failed"));
        }

        @Test
        public void testOptionalLocalDateIsFutureOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(LocalDate.now().plusDays(1)), "Failed"));
        }

        @Test
        public void testOptionalDateIsFutureFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFuture(Optional.of(Date.from(Instant.now().minusSeconds(1))), "Failed"));
        }

        @Test
        public void testOptionalDateIsFutureOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(Date.from(Instant.now().plusSeconds(1))), "Failed"));
        }

        @Test
        public void testOptionalIsFutureCalendarFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), "Failed"));
        }

        @Test
        public void testOptionalIsFutureCalendarOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), "Failed"));
        }

        @Test
        public void testOptionalYearIsFutureFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFuture(Optional.of(Year.now().minusYears(1)), "Failed"));
        }

        @Test
        public void testOptionalYearIsFutureOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(Year.now().plusYears(1)), "Failed"));
        }

        @Test
        public void testOptionalZonedDateTimeIsFutureFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFuture(Optional.of(ZonedDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalZonedDateTimeIsFutureOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(ZonedDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalYearMonthIsFutureFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of(YearMonth.now().minusMonths(1)), "Failed"));
        }

        @Test
        public void testOptionalYearMonthIsFutureOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(YearMonth.now().plusMonths(1)), "Failed"));
        }

        @Test
        public void testOptionalMonthDayIsFutureFail() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFuture(Optional.of(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS))), "Failed"));
            }
        }

        @Test
        public void testOptionalMonthDayIsFutureOk(){
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS))), "Failed"));
            }
        }

        @Test
        public void testOptionalOffsetDateTimeIsFutureFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFuture(Optional.of(OffsetDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalOffsetDateTimeIsFutureOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(OffsetDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalOffsetTimeIsFutureFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFuture(Optional.of(OffsetTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalOffsetTimeIsFutureOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(OffsetTime.now().plusSeconds(1)), "Failed"));
        }
    }
}
