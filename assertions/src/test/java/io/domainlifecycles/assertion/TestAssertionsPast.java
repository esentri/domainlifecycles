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

public class TestAssertionsPast {

    @Nested
    class TestIsPast {

        @Test
        public void testIsPastLocalDateOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast(LocalDate.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastLocalDateOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((LocalDate) null, "Failed"));
        }

        @Test
        public void testIsPastLocalDateFailFuture() {
            assertThatThrownBy(() -> DomainAssertions.isPast(LocalDate.now().plusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastLocalDateFailPresent() {
            assertThatThrownBy(() -> DomainAssertions.isPast(LocalDate.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastLocalTimeOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast(LocalTime.now().minusHours(1), "Failed"));
        }

        @Test
        public void testIsPastLocalTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((LocalTime) null, "Failed"));
        }

        @Test
        public void testIsPastLocalTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isPast(LocalTime.now().plusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPast(LocalDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastLocalDateTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastLocalDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPast(LocalDateTime.now().plusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastInstantOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast(Instant.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastInstantOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((Instant) null, "Failed"));
        }

        @Test
        public void testIsPastInstantFail() {
            assertThatThrownBy(() -> DomainAssertions.isPast(Instant.now().plusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPast(ZonedDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastZonedDateTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastZonedDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isPast(ZonedDateTime.now().plusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastMonthDayOk() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.DECEMBER)) {
                assertThatNoException().isThrownBy(
                    () -> DomainAssertions.isPast(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)),
                        "Failed"));
            }
        }

        @Test
        public void testIsPastMonthDayOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((MonthDay) null, "Failed"));
        }

        @Test
        public void testIsPastMonthDayFailFuture() {
            if (!Month.from(LocalDate.now()).equals(Month.DECEMBER)) {
                assertThatThrownBy(
                    () -> DomainAssertions.isPast(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)),
                        "Failed")).isInstanceOf(DomainAssertionException.class);
            }
        }

        @Test
        public void testIsPastMonthDayFailPresent() {
            assertThatThrownBy(() -> DomainAssertions.isPast(MonthDay.from(LocalDate.now()), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPast(OffsetDateTime.now().minusDays(1), "Failed"));
        }

        @Test
        public void testIsPastOffsetDateTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsPastOffsetDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isPast(OffsetDateTime.now().plusDays(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPast(OffsetTime.now().minusSeconds(1), "Failed"));
        }

        @Test
        public void testIsPastOffsetTimeOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsPastOffsetTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isPast(OffsetTime.now().plusSeconds(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPast(Date.from(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastDateOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((Date) null, "Failed"));
        }

        @Test
        public void testIsPastDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isPast(Date.from(Instant.now().plusSeconds(1)), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastCalendarOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isPast(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testIsPastCalendarOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((Calendar) null, "Failed"));
        }

        @Test
        public void testIsPastCalendarFail() {
            assertThatThrownBy(() -> DomainAssertions.isPast(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPastYearOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast(Year.now().minusYears(1), "Failed"));
        }

        @Test
        public void testIsPastYearOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((Year) null, "Failed"));
        }

        @Test
        public void testIsPastYearFailFuture() {
            assertThatThrownBy(() -> DomainAssertions.isPast(Year.now().plusYears(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastYearFailPresent() {
            assertThatThrownBy(() -> DomainAssertions.isPast(Year.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastYearMonthOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast(YearMonth.now().minusMonths(1), "Failed"));
        }

        @Test
        public void testIsPastYearMonthOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPast((YearMonth) null, "Failed"));
        }

        @Test
        public void testIsPastYearMonthFailFuture() {
            assertThatThrownBy(() -> DomainAssertions.isPast(YearMonth.now().plusMonths(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsPastYearMonthFailPresent() {
            assertThatThrownBy(() -> DomainAssertions.isPast(YearMonth.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsPast {

        @Test
        public void testOptionalIsPastLocalTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsPast(Optional.of(LocalTime.now().plusSeconds(1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsPastLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPast(Optional.of(LocalTime.now().minusSeconds(1)), "Failed"));
        }


        @Test
        public void testOptionalIsPastLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPast(Optional.of(LocalDate.now().minusDays(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPast(Optional.of(LocalDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPast(Optional.of(Date.from(Instant.now().minusSeconds(1))), "Failed"));
        }

        @Test
        public void testOptionalIsPastCalendarOk() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(Instant.now().minusSeconds(1)));

            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsPast(Optional.of(calendar), "Failed"));
        }

        @Test
        public void testOptionalIsPastInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPast(Optional.of(Instant.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPast(Optional.of(Year.now().minusYears(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPast(Optional.of(ZonedDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPast(Optional.of(YearMonth.now().minusMonths(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastMonthDayOk() {
            if (!Month.from(LocalDate.now()).minus(1).equals(Month.DECEMBER)) {
                assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsPast(
                    Optional.of(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS))), "Failed"));
            }
        }

        @Test
        public void testOptionalIsPastOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPast(Optional.of(OffsetDateTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsPast(Optional.of(OffsetTime.now().minusSeconds(1)), "Failed"));
        }

        @Test
        public void testOptionalIsPastOkEmpty() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsPast(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsPastFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsPast(null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsPastFailWrongType() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsPast(Optional.of("String"), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
