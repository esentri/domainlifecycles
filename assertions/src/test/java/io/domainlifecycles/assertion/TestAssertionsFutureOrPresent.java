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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsFutureOrPresent {

    @Nested
    class TestIsFutureOrPresent {

        @Test
        public void testIsFutureOrPresentLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(LocalDate.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalDateOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFutureOrPresent((LocalDate) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFutureOrPresent(LocalDate.now().minusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(LocalTime.now().plusHours(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFutureOrPresent((LocalTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFutureOrPresent(LocalTime.now().minusHours(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(OffsetDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentOffsetDateTimeOkNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent((OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentOffsetDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFutureOrPresent(OffsetDateTime.now().minusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(OffsetTime.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentOffsetTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFutureOrPresent((OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentOffsetTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFutureOrPresent(OffsetTime.now().minusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(LocalDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalDateTimeOkNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent((LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentLocalDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFutureOrPresent(LocalDateTime.now().minusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(Instant.now().plusSeconds(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentInstantOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFutureOrPresent((Instant) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentInstantFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFutureOrPresent(Instant.now().minusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(Date.from(Instant.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentDateOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFutureOrPresent((Date) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentDateFail() {
            assertThatThrownBy(() -> DomainAssertions.isFutureOrPresent(Date.from(Instant.now().minusSeconds(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentCalendarOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)),
                    "Failed"));
        }

        @Test
        public void testIsFutureOrPresentCalendarOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFutureOrPresent((Calendar) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentCalendarFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFutureOrPresent(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(Year.now().plusYears(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentYearOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFutureOrPresent((Year) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentYearFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFutureOrPresent(Year.now().minusYears(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(YearMonth.now().plusMonths(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentYearMonthOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFutureOrPresent((YearMonth) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentYearMonthFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFutureOrPresent(YearMonth.now().minusMonths(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent(ZonedDateTime.now().plusDays(1), "Failed"));
        }

        @Test
        public void testIsFutureOrPresentZonedDateTimeOkNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isFutureOrPresent((ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentZonedDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isFutureOrPresent(ZonedDateTime.now().minusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFutureOrPresentMonthDayOk() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                assertThatNoException().isThrownBy(
                    () -> DomainAssertions.isFutureOrPresent(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)),
                        "Failed"));
            }
        }

        @Test
        public void testIsFutureOrPresentMonthDayOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFutureOrPresent((MonthDay) null, "Failed"));
        }

        @Test
        public void testIsFutureOrPresentMonthDayFail() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                assertThatThrownBy(
                    () -> DomainAssertions.isFutureOrPresent(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)),
                        "Failed")).isInstanceOf(DomainAssertionException.class);
            }
        }
    }

    @Nested
    class TestOptionalIsFutureOrPresent {

        @Test
        public void testOptionalIsFutureOrPresentLocalTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalTime.now().minusHours(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalTime.now().plusHours(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFutureOrPresent(null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentFailWrongType() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of("String"), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalDateTime.now().plusSeconds(1)),
                    "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentLocalDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalDateTime.now().minusSeconds(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalDate.now().plusDays(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentLocalDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalDate.now().minusDays(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Date.from(Instant.now().plusSeconds(1))),
                    "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Date.from(Instant.now().minusSeconds(1))),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentCalendarOk() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(Instant.now().plusSeconds(1)));

            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(calendar), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentCalendarFail() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(Instant.now().minusSeconds(1)));

            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(calendar), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Year.now().plusYears(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentYearFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Year.now().minusYears(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(ZonedDateTime.now().plusSeconds(1)),
                    "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentZonedDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(ZonedDateTime.now().minusSeconds(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(YearMonth.now().plusMonths(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentYearMonthFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(YearMonth.now().minusMonths(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentMonthDayOk() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsFutureOrPresent(
                    Optional.of(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS))), "Failed"));
            }
        }

        @Test
        public void testOptionalIsFutureOrPresentMonthDayFail() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
                assertThatThrownBy(() -> DomainAssertions.optionalIsFutureOrPresent(
                    Optional.of(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS))), "Failed")).isInstanceOf(
                    DomainAssertionException.class);
            }
        }

        @Test
        public void testOptionalIsFutureOrPresentOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(OffsetDateTime.now().plusSeconds(1)),
                    "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentOffsetDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(OffsetDateTime.now().minusSeconds(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(OffsetTime.now().plusSeconds(1)),
                    "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentOffsetTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(OffsetTime.now().minusSeconds(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Instant.now().plusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsFutureOrPresentInstantFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Instant.now().minusSeconds(1)),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsFutureOrPresentOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Instant.now().plusSeconds(1)), "Failed"));
        }
    }
}
