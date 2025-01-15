package io.domainlifecycles.assertion;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsFuture {

    @Nested
    class TestIsFuture {

        @Test
        public void testIsFutureLocalDateFailPast() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(LocalDate.now().minusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureLocalDateFailPresent() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(LocalDate.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureLocalDateOkFuture() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture(LocalDate.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureLocalDateOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((LocalDate) null, "Failed"));
        }

        @Test
        public void testIsFutureLocalTimeFailPast() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(LocalTime.now().minusHours(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureLocalTimeFailPresent() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(LocalTime.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureLocalTimeOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture(LocalTime.now().plusHours(1), "Failed"));
        }

        @Test
        public void testIsFutureLocalTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((LocalTime) null, "Failed"));
        }

        @Test
        public void testIsFutureLocalDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFuture(LocalDateTime.now().minusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFuture(LocalDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureLocalDateTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureInstantFail() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(Instant.now().minusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureInstantOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture(Instant.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureInstantOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((Instant) null, "Failed"));
        }

        @Test
        public void testIsFutureDateFailPast() {
            assertThatThrownBy(
                () -> DomainAssertions.isFuture(Date.from(Instant.now().minusSeconds(1)), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureDateFailPresent() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(Date.from(Instant.now()), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFuture(Date.from(Instant.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureDateOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((Date) null, "Failed"));
        }

        @Test
        public void testIsFutureCalendarFailPast() {
            assertThatThrownBy(
                () -> DomainAssertions.isFuture(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsFutureCalendarFailPresent() {
            assertThatThrownBy(
                () -> DomainAssertions.isFuture(GregorianCalendar.from(ZonedDateTime.now()), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureCalendarOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFuture(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureCalendarOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((Calendar) null, "Failed"));
        }

        @Test
        public void testIsFutureYearFailPast() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(Year.now().minusYears(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureYearFailPresent() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(Year.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureYearOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture(Year.now().plusYears(1), "Failed"));
        }

        @Test
        public void testIsFutureYearOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((Year) null, "Failed"));
        }

        @Test
        public void testIsFutureYearMonthFailPast() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(YearMonth.now().minusMonths(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureYearMonthFailPresent() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(YearMonth.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFuture(YearMonth.now().plusMonths(1), "Failed"));
        }

        @Test
        public void testIsFutureYearMonthOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((YearMonth) null, "Failed"));
        }

        @Test
        public void testIsFutureZonedDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFuture(ZonedDateTime.now().minusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFuture(ZonedDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureZonedDateTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureMonthDayFailPast() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.DECEMBER)) {
                assertThatThrownBy(
                    () -> DomainAssertions.isFuture(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)),
                        "Failed")).isInstanceOf(DomainAssertionException.class);
            }
        }

        @Test
        public void testIsFutureMonthDayFailPresent() {
            assertThatThrownBy(() -> DomainAssertions.isFuture(MonthDay.from(LocalDate.now()), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureMonthDayOk() {
            if (!Month.from(LocalDate.now()).equals(Month.DECEMBER)) {
                assertThatNoException().isThrownBy(
                    () -> DomainAssertions.isFuture(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)),
                        "Failed"));
            }
        }

        @Test
        public void testIsFutureMonthDayOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((MonthDay) null, "Failed"));
        }

        @Test
        public void testIsFutureOffsetDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFuture(OffsetDateTime.now().minusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFuture(OffsetDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOffsetDateTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOffsetTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFuture(OffsetTime.now().minusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFuture(OffsetTime.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureOffsetTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFuture((OffsetTime) null, "Failed"));
        }
    }

    @Nested
    class TestOptionalIsFuture {

        @Test
        public void testOptionalIsFutureLocalTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.of(LocalTime.now().minusHours(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(LocalTime.now().plusHours(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureLocalTimeOkEmpty() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsFutureLocalTimeFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsFutureLocalTimeFailWrongType() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.of("String"), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsFutureFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.of(Instant.now().minusSeconds(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(Instant.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOkEmpty() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsFutureFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsFutureFailWrongType() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.of("String"), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalLocalDateTimeIsFutureFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.of(LocalDateTime.now().minusSeconds(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalLocalDateTimeIsFutureOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(LocalDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalLocalDateIsFutureFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.of(LocalDate.now().minusDays(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalLocalDateIsFutureOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(LocalDate.now().plusDays(1)), "Failed"));
        }

        @Test
        public void testOptionalDateIsFutureFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(Date.from(Instant.now().minusSeconds(1))),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalDateIsFutureOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(Date.from(Instant.now().plusSeconds(1))),
                    "Failed"));
        }

        @Test
        public void testOptionalIsFutureCalendarFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(
                Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureCalendarOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsFuture(
                Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), "Failed"));
        }

        @Test
        public void testOptionalYearIsFutureFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(Year.now().minusYears(1)), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalYearIsFutureOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(Year.now().plusYears(1)), "Failed"));
        }

        @Test
        public void testOptionalZonedDateTimeIsFutureFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.of(ZonedDateTime.now().minusSeconds(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalZonedDateTimeIsFutureOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(ZonedDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalYearMonthIsFutureFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.of(YearMonth.now().minusMonths(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalYearMonthIsFutureOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(YearMonth.now().plusMonths(1)), "Failed"));
        }

        @Test
        public void testOptionalMonthDayIsFutureFail() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.DECEMBER)) {
                assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(
                    Optional.of(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS))), "Failed")).isInstanceOf(
                    DomainAssertionException.class);
            }
        }

        @Test
        public void testOptionalMonthDayIsFutureOk() {
            if (!Month.from(LocalDate.now()).plus(1).equals(Month.JANUARY)) {
                assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsFuture(
                    Optional.of(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS))), "Failed"));
            }
        }

        @Test
        public void testOptionalOffsetDateTimeIsFutureFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(OffsetDateTime.now().minusSeconds(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalOffsetDateTimeIsFutureOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(OffsetDateTime.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalOffsetTimeIsFutureFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFuture(Optional.of(OffsetTime.now().minusSeconds(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalOffsetTimeIsFutureOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFuture(Optional.of(OffsetTime.now().plusSeconds(1)), "Failed"));
        }
    }
}
