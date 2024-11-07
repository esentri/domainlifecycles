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

public class TestAssertionsAfter {

    @Nested
    class TestIsAfter {

        @Test
        public void testIsAfterLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalDateOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalDateOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(LocalDate.now(), (LocalDate) null, "Failed"));
        }

        @Test
        public void testIsAfterLocalDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfter(LocalDate.now().minusDays(1), LocalDate.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsAfterLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter((LocalTime) null, LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(LocalTime.now(), (LocalTime) null, "Failed"));
        }

        @Test
        public void testIsAfterLocalTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfter(LocalTime.now().minusHours(1), LocalTime.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsAfterLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(LocalDateTime.now().plusDays(1), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter((LocalDateTime) null, LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterLocalDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(LocalDateTime.now(), (LocalDateTime) null, "Failed"));
        }

        @Test
        public void testIsAfterLocalDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfter(LocalDateTime.now().minusDays(1), LocalDateTime.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(Instant.now().plusSeconds(1), Instant.now(), "Failed"));
        }

        @Test
        public void testIsAfterInstantOkFirstArgNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isAfter((Instant) null, Instant.now(), "Failed"));
        }

        @Test
        public void testIsAfterInstantOkSecondArgNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isAfter(Instant.now(), (Instant) null, "Failed"));
        }

        @Test
        public void testIsAfterInstantFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfter(Instant.now().minusSeconds(1), Instant.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsAfterYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(Year.now().plusYears(1), Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterYearOkFirstArgNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isAfter((Year) null, Year.now(), "Failed"));
        }

        @Test
        public void testIsAfterYearOkSecondArgNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isAfter(Year.now(), (Year) null, "Failed"));
        }

        @Test
        public void testIsAfterYearFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfter(Year.now().minusYears(1), Year.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsAfterYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsAfterYearMonthOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter((YearMonth) null, YearMonth.now(), "Failed"));
        }

        @Test
        public void testIsAfterYearMonthOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(YearMonth.now(), (YearMonth) null, "Failed"));
        }

        @Test
        public void testIsAfterYearMonthFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfter(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsAfterZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterZonedDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterZonedDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(ZonedDateTime.now(), (ZonedDateTime) null, "Failed"));
        }

        @Test
        public void testIsAfterZonedDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfter(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterMonthDayOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(MonthDay.of(6, 5), MonthDay.of(5, 5), "Failed"));
        }

        @Test
        public void testIsAfterMonthDayOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter((MonthDay) null, MonthDay.now(), "Failed"));
        }

        @Test
        public void testIsAfterMonthDayOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(MonthDay.now(), (MonthDay) null, "Failed"));
        }

        @Test
        public void testIsAfterMonthDayFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfter(MonthDay.of(4, 5), MonthDay.of(5, 5), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOffsetDateTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOffsetDateTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(OffsetDateTime.now(), (OffsetDateTime) null, "Failed"));
        }

        @Test
        public void testIsAfterOffsetDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfter(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOffsetTimeOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isAfter(OffsetTime.of(6, 5, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testIsAfterOffsetTimeOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter((OffsetTime) null, OffsetTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOffsetTimeOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(OffsetTime.now(), (OffsetTime) null, "Failed"));
        }

        @Test
        public void testIsAfterOffsetTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfter(OffsetTime.of(4, 5, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(Date.from(Instant.now().plusSeconds(1)), new Date(), "Failed"));
        }

        @Test
        public void testIsAfterDateOkFirstArgNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isAfter((Date) null, new Date(), "Failed"));
        }

        @Test
        public void testIsAfterDateOkSecondArgNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isAfter(new Date(), (Date) null, "Failed"));
        }

        @Test
        public void testIsAfterDateFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfter(Date.from(Instant.now().minusSeconds(1)), new Date(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterCalendarOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)),
                    new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testIsAfterCalendarOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter((Calendar) null, Calendar.getInstance(), "Failed"));
        }

        @Test
        public void testIsAfterCalendarOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfter(Calendar.getInstance(), (Calendar) null, "Failed"));
        }

        @Test
        public void testIsAfterCalendarFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isAfter(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)),
                    new GregorianCalendar(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsAfter {

        @Test
        public void testOptionalIsAfterLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(LocalDate.now().plusDays(1)), LocalDate.now(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(LocalDate.now().minusDays(1)), LocalDate.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterLocalDateFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(null, LocalDate.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(LocalTime.now().plusHours(1)), LocalTime.now(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(LocalTime.now().minusHours(1)), LocalTime.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterLocalTimeFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(null, LocalTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterLocalDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(LocalDateTime.now().plusDays(1)),
                    LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), LocalDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterLocalDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(Optional.of(LocalDateTime.now().minusDays(1)),
                LocalDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterLocalDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfter(null, LocalDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterInstantOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(Instant.now().plusSeconds(1)), Instant.now(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsAfterInstantOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), Instant.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterInstantFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(Instant.now().minusSeconds(1)), Instant.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterInstantFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(null, Instant.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterZonedDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(ZonedDateTime.now().plusDays(1)),
                    ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterZonedDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), ZonedDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterZonedDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(Optional.of(ZonedDateTime.now().minusDays(1)),
                ZonedDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterZonedDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfter(null, ZonedDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterMonthDayOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(MonthDay.of(6, 5)), MonthDay.of(5, 5), "Failed"));
        }

        @Test
        public void testOptionalIsAfterMonthDayOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), MonthDay.of(5, 5), "Failed"));
        }

        @Test
        public void testOptionalIsAfterMonthDayFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(Optional.of(MonthDay.of(4, 5)), MonthDay.of(5, 5),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterMonthDayFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(null, MonthDay.of(5, 5), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOffsetDateTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(OffsetDateTime.now().plusDays(1)),
                    OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetDateTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), OffsetDateTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetDateTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(Optional.of(OffsetDateTime.now().minusDays(1)),
                OffsetDateTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOffsetDateTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfter(null, OffsetDateTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOffsetTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(OffsetTime.of(6, 5, 0, 0, ZoneOffset.UTC)),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC),
                    "Failed"));
        }

        @Test
        public void testOptionalIsAfterOffsetTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(OffsetTime.of(4, 5, 0, 0, ZoneOffset.UTC)),
                    OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOffsetTimeFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(null, OffsetTime.of(5, 5, 0, 0, ZoneOffset.UTC),
                "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(Date.from(Instant.now().plusSeconds(1))), new Date(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsAfterDateOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), new Date(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(Date.from(Instant.now().minusSeconds(1))),
                    new Date(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterDateFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(null, new Date(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterCalendarOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsAfter(
                Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(),
                "Failed"));
        }

        @Test
        public void testOptionalIsAfterCalendarOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), new GregorianCalendar(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterCalendarFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(
                Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterCalendarFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfter(null, new GregorianCalendar(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterYearOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(Year.now().plusYears(1)), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), Year.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(Optional.of(Year.now().minusYears(1)), Year.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterYearFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(null, Year.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterYearMonthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(YearMonth.now().plusMonths(1)), YearMonth.now(),
                    "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearMonthOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.empty(), YearMonth.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterYearMonthFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfter(Optional.of(YearMonth.now().minusMonths(1)), YearMonth.now(),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterYearMonthFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsAfter(null, YearMonth.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
