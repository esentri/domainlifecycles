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

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsBeforeOrEqual {

    @Nested
    class TestIsBeforeOrEqualTo {

        @Test
        public void testIsBeforeOrEqualToLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(LocalDate.now().minusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(LocalDate.now(), (LocalDate) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateFail() {
            assertThatThrownBy(() -> DomainAssertions.isBeforeOrEqualTo(LocalDate.now().plusDays(1), LocalDate.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(LocalTime.now().minusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((LocalTime) null, LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(LocalTime.now(), (LocalTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isBeforeOrEqualTo(LocalTime.now().plusHours(1), LocalTime.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(LocalDateTime.now().minusDays(1), LocalDateTime.now(),
                    "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((LocalDateTime) null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(LocalDateTime.now(), (LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToLocalDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(LocalDateTime.now().plusDays(1), LocalDateTime.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(Instant.now().minusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToInstantOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((Instant) null, Instant.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToInstantOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(Instant.now(), (Instant) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToInstantFail() {
            assertThatThrownBy(() -> DomainAssertions.isBeforeOrEqualTo(Instant.now().plusSeconds(1), Instant.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(Date.from(Instant.now().minusSeconds(1)), new Date(),
                    "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToDateOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((Date) null, new Date(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToDateOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(new Date(), (Date) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(Date.from(Instant.now().plusSeconds(1)), new Date(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToCalendarOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)),
                    new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToCalendarOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((Calendar) null, Calendar.getInstance(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToCalendarOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(Calendar.getInstance(), (Calendar) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToCalendarFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)),
                    new GregorianCalendar(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(Year.now().minusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((Year) null, Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(Year.now(), (Year) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(Year.now().plusYears(1), Year.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearMonthOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((YearMonth) null, YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearMonthOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(YearMonth.now(), (YearMonth) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToYearMonthFail() {
            assertThatThrownBy(() -> DomainAssertions.isBeforeOrEqualTo(YearMonth.now().plusMonths(1), YearMonth.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(),
                    "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToZonedDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToZonedDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(ZonedDateTime.now(), (ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToZonedDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToMonthDayOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(MonthDay.of(4, 5), MonthDay.of(5, 5), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToMonthDayOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((MonthDay) null, MonthDay.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToMonthDayOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(MonthDay.now(), (MonthDay) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToMonthDayFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(MonthDay.of(6, 5), MonthDay.of(5, 5), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(),
                    "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(OffsetDateTime.now(), (OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOrEqualToOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(OffsetTime.of(4, 5, 0, 0, ZoneOffset.UTC),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo((OffsetTime) null, OffsetTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBeforeOrEqualTo(OffsetTime.now(), (OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOrEqualToOffsetTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isBeforeOrEqualTo(OffsetTime.of(6, 5, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsBeforeOrEqualTo {

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDate.now().minusDays(1)),
                    LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDate.now().plusDays(1)),
                    LocalDate.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, LocalDate.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalTime.now().minusHours(1)),
                    LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalTime.now().plusHours(1)),
                    LocalTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, LocalTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetDateTime.now().minusDays(1)),
                    OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetDateTime.now().plusDays(1)),
                    OffsetDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, OffsetDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetTime.of(4, 5, 0, 0, ZoneOffset.UTC)),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetTimeOkEmpty() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(),
                OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetTime.of(6, 5, 0, 0, ZoneOffset.UTC)),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToOffsetTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC),
                    "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDateTime.now().minusDays(1)),
                    LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDateTime.now().plusDays(1)),
                    LocalDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToLocalDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, LocalDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Instant.now().minusSeconds(1)),
                    Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToInstantOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToInstantFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Instant.now().plusSeconds(1)),
                    Instant.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToInstantFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, Instant.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Date.from(Instant.now().minusSeconds(1))),
                    new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToDateOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Date.from(Instant.now().plusSeconds(1))),
                    new Date(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToDateFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, new Date(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToCalendarOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsBeforeOrEqualTo(
                Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(),
                "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToCalendarOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToCalendarFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBeforeOrEqualTo(
                Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToCalendarFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, new GregorianCalendar(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Year.now().minusYears(1)), Year.now(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Year.now().plusYears(1)), Year.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, Year.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(YearMonth.now().minusMonths(1)),
                    YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearMonthOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearMonthFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(YearMonth.now().plusMonths(1)),
                    YearMonth.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToYearMonthFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, YearMonth.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(ZonedDateTime.now().minusDays(1)),
                    ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToZonedDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToZonedDateTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(ZonedDateTime.now().plusDays(1)),
                    ZonedDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToZonedDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, ZonedDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToMonthDayOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(MonthDay.of(4, 5)), MonthDay.of(5, 5),
                    "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToMonthDayOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), MonthDay.of(5, 5), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOrEqualToMonthDayFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(MonthDay.of(6, 5)), MonthDay.of(5, 5),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOrEqualToMonthDayFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBeforeOrEqualTo(null, MonthDay.of(5, 5), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
