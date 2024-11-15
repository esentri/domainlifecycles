package io.domainlifecycles.assertion;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class TestAssertionsAfterOrEqual {

    @Nested
    class TestIsAfterOrEqual {

        @Test
        public void testIsAfterOrEqualToLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalDateTime.now().plusDays(1), LocalDateTime.now(),
                    "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((LocalDateTime) null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalDateTime.now(), (LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalDateTime.now().minusDays(1), LocalDateTime.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalDate.now(), (LocalDate) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfterOrEqualTo(LocalDate.now().minusDays(1), LocalDate.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalTime.now().plusSeconds(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((LocalTime) null, LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalTime.now(), (LocalTime) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfterOrEqualTo(LocalTime.now().minusSeconds(1), LocalTime.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(Instant.now().plusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToInstantOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((Instant) null, Instant.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToInstantOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(Instant.now(), (Instant) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToInstantFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfterOrEqualTo(Instant.now().minusSeconds(1), Instant.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(Date.from(Instant.now().plusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToDateOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((Date) null, new Date(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToDateOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(new Date(), (Date) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(Date.from(Instant.now().minusSeconds(1)), new Date(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToCalendarOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)),
                    new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToCalendarOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((Calendar) null, Calendar.getInstance(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToCalendarOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(Calendar.getInstance(), (Calendar) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToCalendarFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)),
                    new GregorianCalendar(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(Year.now().plusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((Year) null, Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(Year.now(), (Year) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(Year.now().minusYears(1), Year.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearMonthOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((YearMonth) null, YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearMonthOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(YearMonth.now(), (YearMonth) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToYearMonthFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfterOrEqualTo(YearMonth.now().minusMonths(1), YearMonth.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(),
                    "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToZonedDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToZonedDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(ZonedDateTime.now(), (ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToZonedDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToMonthDayOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(MonthDay.of(6, 5), MonthDay.of(5, 5), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToMonthDayOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((MonthDay) null, MonthDay.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToMonthDayOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(MonthDay.now(), (MonthDay) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToMonthDayFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(MonthDay.of(4, 5), MonthDay.of(5, 5), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(),
                    "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(OffsetDateTime.now(), (OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(OffsetTime.of(6, 5, 0, 0, ZoneOffset.UTC),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((OffsetTime) null, OffsetTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(OffsetTime.now(), (OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToOffsetTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfterOrEqualTo(OffsetTime.of(4, 5, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsAfterOrEqual {

        @Test
        public void testOptionalIsAfterOrEqualToOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetDateTime.now().plusDays(1)),
                    OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetDateTime.now().minusDays(1)),
                    OffsetDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, OffsetDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetTime.of(6, 5, 0, 0, ZoneOffset.UTC)),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetTimeOkEmpty() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(),
                OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetTime.of(4, 5, 0, 0, ZoneOffset.UTC)),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToOffsetTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC),
                    "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDateTime.now().plusDays(1)),
                    LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDateTime.now().minusDays(1)),
                    LocalDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Instant.now().plusSeconds(1)),
                    Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToInstantOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToInstantFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Instant.now().minusSeconds(1)),
                    Instant.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToInstantFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, Instant.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Date.from(Instant.now().plusSeconds(1))),
                    new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToDateOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Date.from(Instant.now().minusSeconds(1))),
                    new Date(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToDateFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, new Date(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToCalendarOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsAfterOrEqualTo(
                Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(),
                "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToCalendarOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToCalendarFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfterOrEqualTo(
                Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToCalendarFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, new GregorianCalendar(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Year.now().plusYears(1)), Year.now(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Year.now().minusYears(1)), Year.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, Year.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(YearMonth.now().plusMonths(1)),
                    YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearMonthOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearMonthFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(YearMonth.now().minusMonths(1)),
                    YearMonth.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToYearMonthFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, YearMonth.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(ZonedDateTime.now().plusDays(1)),
                    ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToZonedDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToZonedDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(ZonedDateTime.now().minusDays(1)),
                    ZonedDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToZonedDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, ZonedDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToMonthDayOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(MonthDay.of(6, 5)), MonthDay.of(5, 5),
                    "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToMonthDayOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), MonthDay.of(5, 5), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToMonthDayFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(MonthDay.of(4, 5)), MonthDay.of(5, 5),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToMonthDayFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, MonthDay.of(5, 5), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
