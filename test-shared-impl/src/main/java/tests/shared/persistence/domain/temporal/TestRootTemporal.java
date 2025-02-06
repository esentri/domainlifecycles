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

package tests.shared.persistence.domain.temporal;

import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

@Getter
public class TestRootTemporal extends AggregateRootBase<TestRootTemporalId> {

    private TestRootTemporalId id;
    private LocalDate localDate;
    private LocalTime localTime;
    private LocalDateTime localDateTime;
    private Year myYear;
    private YearMonth yearMonth;
    private MonthDay monthDay;
    private ZonedDateTime zonedDateTime;
    private Calendar myCalendar;
    private Date myDate;
    private Instant myInstant;
    private OffsetDateTime offsetDateTime;
    private OffsetTime offsetTime;

    @Builder(setterPrefix = "set")
    public TestRootTemporal(TestRootTemporalId id,
                            long concurrencyVersion,
                            LocalDate localDate,
                            LocalTime localTime,
                            LocalDateTime localDateTime,
                            Year myYear,
                            YearMonth yearMonth,
                            MonthDay monthDay,
                            ZonedDateTime zonedDateTime,
                            Calendar myCalendar,
                            Date myDate,
                            Instant myInstant,
                            OffsetDateTime offsetDateTime,
                            OffsetTime offsetTime
    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine Root ID muss angegeben sein!");
        this.localDate = localDate;
        this.localDateTime = localDateTime;
        this.localTime = localTime;
        this.monthDay = monthDay;
        this.myDate = myDate;
        this.myCalendar = myCalendar;
        this.myInstant = myInstant;
        this.myYear = myYear;
        this.offsetDateTime = offsetDateTime;
        this.offsetTime = offsetTime;
        this.yearMonth = yearMonth;
        this.zonedDateTime = zonedDateTime;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public void setMyYear(Year myYear) {
        this.myYear = myYear;
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    public void setMonthDay(MonthDay monthDay) {
        this.monthDay = monthDay;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    public void setMyCalendar(Calendar myCalendar) {
        this.myCalendar = myCalendar;
    }

    public void setMyDate(Date myDate) {
        this.myDate = myDate;
    }

    public void setMyInstant(Instant myInstant) {
        this.myInstant = myInstant;
    }

    public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
        this.offsetDateTime = offsetDateTime;
    }

    public void setOffsetTime(OffsetTime offsetTime) {
        this.offsetTime = offsetTime;
    }

}
