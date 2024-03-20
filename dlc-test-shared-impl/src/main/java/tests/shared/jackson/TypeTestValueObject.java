/*
 *
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

package tests.shared.jackson;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nitrox.dlc.domain.types.base.ValueObjectBase;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
//only for testing purposes, setter within ValueObjects make absolutely no sense!!!!
public class TypeTestValueObject extends ValueObjectBase {

    public boolean aBoolean;
    public char aChar;
    public String string;
    public UUID uuid;
    public Optional<String> optionalString;
    public BigDecimal bigDecimal;
    public BigInteger bigInteger;
    public int anInt;
    public long aLong;
    public byte aByte;
    public short aShort;
    public Optional<BigDecimal> optionalBigDecimal;
    public Optional<BigInteger> optionalBigInteger;
    public Optional<Integer> optionalInteger;
    public Optional<Long> optionalLong;
    public Optional<Byte> optionalByte;
    public Optional<Short> optionalShort;
    public float aFloat;
    public double aDouble;
    public Optional<Float> optionalFloat;
    public Optional<Double> optionalDouble;

    public Instant instant;
    public Optional<Instant> optionalInstant;
    public LocalDate localDate;
    public Optional<LocalDate> optionalLocalDate;
    public LocalDateTime localDateTime;
    public Optional<LocalDateTime> optionalLocalDateTime;
    public LocalTime localTime;
    public Optional<LocalTime> optionalLocalTime;
    public ZonedDateTime zonedDateTime;
    public Optional<ZonedDateTime> optionalZonedDateTime;
    public OffsetDateTime offsetDateTime;
    public Optional<OffsetDateTime> optionalOffsetDateTime;
    public OffsetTime offsetTime;
    public Optional<OffsetTime> optionalOffsetTime;
    public Date date;
    public Calendar calendar;
    public YearMonth yearMonth;
    public Optional<YearMonth> optionalYearMonth;
    public MonthDay monthDay;
    public Optional<MonthDay> optionalMonthDay;
    public Year year;
    public Optional<Year> optionalYear;


    public TypeTestValueObject() {
    }

    @Builder(setterPrefix = "set")
    public TypeTestValueObject(boolean aBoolean,
                               char aChar,
                               String string,
                               UUID uuid,
                               String optionalString,
                               BigDecimal bigDecimal,
                               BigInteger bigInteger,
                               int anInt,
                               long aLong,
                               byte aByte,
                               short aShort,
                               BigDecimal optionalBigDecimal,
                               BigInteger optionalBigInteger,
                               Integer optionalInteger,
                               Long optionalLong,
                               Byte optionalByte,
                               Short optionalShort,
                               float aFloat,
                               double aDouble,
                               Float optionalFloat,
                               Double optionalDouble,
                               Instant instant,
                               Instant optionalInstant,
                               LocalDate localDate,
                               LocalDate optionalLocalDate,
                               LocalDateTime localDateTime,
                               LocalDateTime optionalLocalDateTime,
                               LocalTime localTime,
                               LocalTime optionalLocalTime,
                               ZonedDateTime zonedDateTime,
                               ZonedDateTime optionalZonedDateTime,
                               OffsetDateTime offsetDateTime,
                               OffsetDateTime optionalOffsetDateTime,
                               OffsetTime offsetTime,
                               OffsetTime optionalOffsetTime,
                               Date date,
                               Calendar calendar,
                               YearMonth yearMonth,
                               YearMonth optionalYearMonth,
                               MonthDay monthDay,
                               MonthDay optionalMonthDay,
                               Year year,
                               Year optionalYear) {
        this.aBoolean = aBoolean;
        this.aChar = aChar;
        this.string = string;
        this.uuid = uuid;
        this.optionalString = Optional.ofNullable(optionalString);
        this.bigDecimal = bigDecimal;
        this.bigInteger = bigInteger;
        this.anInt = anInt;
        this.aLong = aLong;
        this.aByte = aByte;
        this.aShort = aShort;
        this.optionalBigDecimal = Optional.ofNullable(optionalBigDecimal);
        this.optionalBigInteger = Optional.ofNullable(optionalBigInteger);
        this.optionalInteger = Optional.ofNullable(optionalInteger);
        this.optionalLong = Optional.ofNullable(optionalLong);
        this.optionalByte = Optional.ofNullable(optionalByte);
        this.optionalShort = Optional.ofNullable(optionalShort);
        this.aFloat = aFloat;
        this.aDouble = aDouble;
        this.optionalFloat = Optional.ofNullable(optionalFloat);
        this.optionalDouble = Optional.ofNullable(optionalDouble);
        this.instant = instant;
        this.optionalInstant = Optional.ofNullable(optionalInstant);
        this.localDate = localDate;
        this.optionalLocalDate = Optional.ofNullable(optionalLocalDate);
        this.localDateTime = localDateTime;
        this.optionalLocalDateTime = Optional.ofNullable(optionalLocalDateTime);
        this.localTime = localTime;
        this.optionalLocalTime = Optional.ofNullable(optionalLocalTime);
        this.zonedDateTime = zonedDateTime;
        this.optionalZonedDateTime = Optional.ofNullable(optionalZonedDateTime);
        this.offsetDateTime = offsetDateTime;
        this.optionalOffsetDateTime = Optional.ofNullable(optionalOffsetDateTime);
        this.offsetTime = offsetTime;
        this.optionalOffsetTime = Optional.ofNullable(optionalOffsetTime);
        this.date = date;
        this.calendar = calendar;
        this.yearMonth = yearMonth;
        this.optionalYearMonth = Optional.ofNullable(optionalYearMonth);
        this.monthDay = monthDay;
        this.optionalMonthDay = Optional.ofNullable(optionalMonthDay);
        this.year = year;
        this.optionalYear = Optional.ofNullable(optionalYear);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeTestValueObject)) return false;
        if (!super.equals(o)) return false;
        TypeTestValueObject that = (TypeTestValueObject) o;
        return aBoolean == that.aBoolean
            && aChar == that.aChar
            && anInt == that.anInt
            && aLong == that.aLong
            && aByte == that.aByte
            && aShort == that.aShort
            && Float.compare(that.aFloat, aFloat) == 0
            && Double.compare(that.aDouble, aDouble) == 0
            && string.equals(that.string)
            && uuid.equals(that.uuid)
            && optionalString.equals(that.optionalString)
            && bigDecimal.equals(that.bigDecimal)
            && bigInteger.equals(that.bigInteger)
            && optionalBigDecimal.equals(that.optionalBigDecimal)
            && optionalBigInteger.equals(that.optionalBigInteger)
            && optionalInteger.equals(that.optionalInteger)
            && optionalLong.equals(that.optionalLong)
            && optionalByte.equals(that.optionalByte)
            && optionalShort.equals(that.optionalShort)
            && optionalFloat.equals(that.optionalFloat)
            && optionalDouble.equals(that.optionalDouble)
            && instant.equals(that.instant)
            && optionalInstant.equals(that.optionalInstant)
            && localDate.isEqual(that.localDate)
            && optionalLocalDate.get().isEqual(that.optionalLocalDate.get())
            && localDateTime.isEqual(that.localDateTime)
            && optionalLocalDateTime.get().isEqual(that.optionalLocalDateTime.get())
            && localTime.equals(that.localTime)
            && optionalLocalTime.equals(that.optionalLocalTime)
            && zonedDateTime.isEqual(that.zonedDateTime)
            && optionalZonedDateTime.get().isEqual(that.optionalZonedDateTime.get())
            && offsetDateTime.isEqual(that.offsetDateTime)
            && optionalOffsetDateTime.get().isEqual(that.optionalOffsetDateTime.get())
            && offsetTime.isEqual(that.offsetTime)
            && optionalOffsetTime.get().isEqual(that.optionalOffsetTime.get())
            && date.equals(that.date)
            && calendar.equals(that.calendar)
            && yearMonth.equals(that.yearMonth)
            && optionalYearMonth.equals(that.optionalYearMonth)
            && monthDay.equals(that.monthDay)
            && optionalMonthDay.equals(that.optionalMonthDay)
            && year.equals(that.year)
            && optionalYear.equals(that.optionalYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), aBoolean, aChar, string, uuid, optionalString, bigDecimal, bigInteger, anInt, aLong, aByte, aShort, optionalBigDecimal, optionalBigInteger, optionalInteger, optionalLong, optionalByte, optionalShort, aFloat, aDouble, optionalFloat, optionalDouble, instant, optionalInstant, localDate, optionalLocalDate, localDateTime, optionalLocalDateTime, localTime, optionalLocalTime, zonedDateTime, optionalZonedDateTime, offsetDateTime, optionalOffsetDateTime, offsetTime, optionalOffsetTime, date, calendar, yearMonth, optionalYearMonth, monthDay, optionalMonthDay, year, optionalYear);
    }
}
