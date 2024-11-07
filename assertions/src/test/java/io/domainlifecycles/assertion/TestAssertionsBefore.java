/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.assertion;

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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsBefore {

    @Nested
    class TestIsBefore {

        @Test
        public void testIsBeforeLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(LocalDate.now().minusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(LocalDate.now(), (LocalDate) null, "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBefore(LocalDate.now().plusDays(1), LocalDate.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(LocalTime.now().minusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore((LocalTime) null, LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(LocalTime.now(), (LocalTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeLocalTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBefore(LocalTime.now().plusHours(1), LocalTime.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore((LocalDateTime) null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(LocalDateTime.now(), (LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeLocalDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isBefore(LocalDateTime.now().plusDays(1), LocalDateTime.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(Instant.now().minusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsBeforeInstantOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore((Instant) null, Instant.now(), "Failed"));
        }

        @Test
        public void testIsBeforeInstantOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(Instant.now(), (Instant) null, "Failed"));
        }


        @Test
        public void testIsBeforeInstantFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBefore(Instant.now().plusSeconds(1), Instant.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(Date.from(Instant.now().minusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsBeforeDateOkFirstArgNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isBefore((Date) null, new Date(), "Failed"));
        }

        @Test
        public void testIsBeforeDateOkSecondArgNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isBefore(new Date(), (Date) null, "Failed"));
        }

        @Test
        public void testIsBeforeDateFail() {
            assertThatThrownBy(() -> DomainAssertions.isBefore(Date.from(Instant.now().plusSeconds(1)), new Date(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeCalendarOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)),
                    new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsBeforeCalendarOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore((Calendar) null, Calendar.getInstance(), "Failed"));
        }

        @Test
        public void testIsBeforeCalendarOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(Calendar.getInstance(), (Calendar) null, "Failed"));
        }

        @Test
        public void testIsBeforeCalendarFailOnAfter() {
            assertThatThrownBy(
                () -> DomainAssertions.isBefore(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)),
                    new GregorianCalendar(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(Year.now().minusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeYearOkFirstArgNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isBefore((Year) null, Year.now(), "Failed"));
        }

        @Test
        public void testIsBeforeYearOkSecondArgNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isBefore(Year.now(), (Year) null, "Failed"));
        }

        @Test
        public void testIsBeforeYearFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBefore(Year.now().plusYears(1), Year.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsBeforeYearMonthOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore((YearMonth) null, YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsBeforeYearMonthOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(YearMonth.now(), (YearMonth) null, "Failed"));
        }

        @Test
        public void testIsBeforeYearMonthFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBefore(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeZonedDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeZonedDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(ZonedDateTime.now(), (ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeZonedDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isBefore(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeMonthDayOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(MonthDay.of(4, 5), MonthDay.of(5, 5), "Failed"));
        }

        @Test
        public void testIsBeforeMonthDayOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore((MonthDay) null, MonthDay.now(), "Failed"));
        }

        @Test
        public void testIsBeforeMonthDayOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(MonthDay.now(), (MonthDay) null, "Failed"));
        }

        @Test
        public void testIsBeforeMonthDayFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isBefore(MonthDay.of(6, 5), MonthDay.of(5, 5), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOffsetDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOffsetDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(OffsetDateTime.now(), (OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOffsetDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isBefore(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsBeforeOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(OffsetTime.of(4, 5, 0, 0, ZoneOffset.UTC),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testIsBeforeOffsetTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore((OffsetTime) null, OffsetTime.now(), "Failed"));
        }

        @Test
        public void testIsBeforeOffsetTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isBefore(OffsetTime.now(), (OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsBeforeOffsetTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isBefore(OffsetTime.of(6, 5, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsBefore {

        @Test
        public void testOptionalIsBeforeLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(LocalDate.now().minusDays(1)), LocalDate.now(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(LocalDate.now().plusDays(1)), LocalDate.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeLocalDateFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(null, LocalDate.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(LocalTime.now().minusHours(1)), LocalTime.now(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(LocalTime.now().plusHours(1)), LocalTime.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeLocalTimeFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(null, LocalTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(LocalDateTime.now().minusDays(1)),
                    LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeLocalDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(Optional.of(LocalDateTime.now().plusDays(1)),
                LocalDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeLocalDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(null, LocalDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(Instant.now().minusSeconds(1)), Instant.now(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsBeforeInstantOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeInstantFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(Instant.now().plusSeconds(1)), Instant.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeInstantFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(null, Instant.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(Date.from(Instant.now().minusSeconds(1))),
                    new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeDateOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(Date.from(Instant.now().plusSeconds(1))),
                    new Date(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeDateFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(null, new Date(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeCalendarOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsBefore(
                Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(),
                "Failed"));
        }

        @Test
        public void testOptionalIsBeforeCalendarOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeCalendarFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(
                Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeCalendarFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(null, new GregorianCalendar(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(Year.now().minusYears(1)), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(Optional.of(Year.now().plusYears(1)), Year.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeYearFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(null, Year.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(YearMonth.now().minusMonths(1)), YearMonth.now(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearMonthOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeYearMonthFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(YearMonth.now().plusMonths(1)), YearMonth.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeYearMonthFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(null, YearMonth.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(ZonedDateTime.now().minusDays(1)),
                    ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeZonedDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeZonedDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(Optional.of(ZonedDateTime.now().plusDays(1)),
                ZonedDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeZonedDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(null, ZonedDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeMonthDayOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(MonthDay.of(4, 5)), MonthDay.of(5, 5), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeMonthDayOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), MonthDay.of(5, 5), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeMonthDayFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(MonthDay.of(6, 5)), MonthDay.of(5, 5),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeMonthDayFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(null, MonthDay.of(5, 5), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(OffsetDateTime.now().minusDays(1)),
                    OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(Optional.of(OffsetDateTime.now().plusDays(1)),
                OffsetDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOffsetDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(null, OffsetDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsBeforeOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(OffsetTime.of(4, 5, 0, 0, ZoneOffset.UTC)),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.empty(), OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC),
                    "Failed"));
        }

        @Test
        public void testOptionalIsBeforeOffsetTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsBefore(Optional.of(OffsetTime.of(6, 5, 0, 0, ZoneOffset.UTC)),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsBeforeOffsetTimeFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsBefore(null, OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC),
                "Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
