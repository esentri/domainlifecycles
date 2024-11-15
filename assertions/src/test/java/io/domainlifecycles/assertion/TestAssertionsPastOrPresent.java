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

public class TestAssertionsPastOrPresent {

    @Nested
    class TestIsPastOrPresent {

        @Test
        public void testIsPastOrPresentLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(LocalDate.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalDateOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((LocalDate) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPastOrPresent(LocalDate.now().plusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(LocalTime.now().minusHours(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((LocalTime) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPastOrPresent(LocalTime.now().plusHours(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(LocalDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalDateTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentLocalDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPastOrPresent(LocalDateTime.now().plusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(Instant.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentInstantOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((Instant) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentInstantFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPastOrPresent(Instant.now().plusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(OffsetDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentOffsetDateTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentOffsetDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPastOrPresent(OffsetDateTime.now().plusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(OffsetTime.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentOffsetTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentOffsetTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPastOrPresent(OffsetTime.now().plusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(Date.from(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastOrPresentDateOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((Date) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPastOrPresent(Date.from(Instant.now().plusSeconds(1)), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentCalendarOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)),
                    "Failed"));
        }

        @Test
        public void testIsPastOrPresentCalendarOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((Calendar) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentCalendarFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPastOrPresent(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(Year.now().minusYears(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentYearOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((Year) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentYearFail() {
            assertThatThrownBy(() -> DomainAssertions.isPastOrPresent(Year.now().plusYears(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(YearMonth.now().minusMonths(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentYearMonthOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((YearMonth) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentYearMonthFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPastOrPresent(YearMonth.now().plusMonths(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPastOrPresent(ZonedDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOrPresentZonedDateTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentZonedDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPastOrPresent(ZonedDateTime.now().plusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOrPresentMonthDayOk() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                assertThatNoException().isThrownBy(
                    () -> DomainAssertions.isPastOrPresent(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)),
                        "Failed"));
            }
        }

        @Test
        public void testIsPastOrPresentMonthDayOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPastOrPresent((MonthDay) null, "Failed"));
        }

        @Test
        public void testIsPastOrPresentMonthDayFail() {
            if (!Month.from(LocalDate.now()).equals(Month.DECEMBER)) {
                assertThatThrownBy(
                    () -> DomainAssertions.isPastOrPresent(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)),
                        "Failed")).isInstanceOf(DomainAssertionException.class);
            }
        }
    }

    @Nested
    class TestOptionalIsPastOrPresent {

        @Test
        public void testOptionalIsPastOrPresentFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsPastOrPresent(Optional.of(LocalTime.now().plusHours(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsPastOrPresentLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(LocalTime.now().minusHours(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(Date.from(Instant.now().minusSeconds(1))),
                    "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(LocalDateTime.now().minusSeconds(1)),
                    "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(LocalDate.now().minusDays(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentCalendarOk() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(Instant.now().minusSeconds(1)));

            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(calendar), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(Year.now().minusYears(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(ZonedDateTime.now().minusSeconds(1)),
                    "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(YearMonth.now().minusMonths(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentMonthDayOk() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsPastOrPresent(
                    Optional.of(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS))), "Failed"));
            }
        }

        @Test
        public void testOptionalIsPastOrPresentOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(OffsetDateTime.now().minusSeconds(1)),
                    "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of(OffsetTime.now().minusSeconds(1)),
                    "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsPastOrPresentFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsPastOrPresent(null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsPastOrPresentFailWrongType() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsPastOrPresent(Optional.of("String"), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
