package io.domainlifecycles.persistence.mapping.converter.def;

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
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@code Default*Converter} classes converting between the date/time related Java types.
 */
public class DateTimeDefaultConvertersTest {

    @Test
    public void testInstantToOffsetDateTimeUTC() {
        var converter = new DefaultInstantToOffsetDateTimeUTCConverter();
        var instant = Instant.parse("2024-05-17T10:30:00Z");

        var converted = converter.convert(instant);

        assertThat(converted).isEqualTo(OffsetDateTime.of(2024, 5, 17, 10, 30, 0, 0, ZoneOffset.UTC));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testOffsetDateTimeToInstant() {
        var converter = new DefaultOffsetDateTimeToInstantConverter();
        var offsetDateTime = OffsetDateTime.of(2024, 5, 17, 10, 30, 0, 0, ZoneOffset.ofHours(2));

        var converted = converter.convert(offsetDateTime);

        assertThat(converted).isEqualTo(Instant.parse("2024-05-17T08:30:00Z"));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testDateToOffsetDateTimeUTC() {
        var converter = new DefaultDateToOffsetDateTimeUTCConverter();
        var date = Date.from(Instant.parse("2024-05-17T10:30:00Z"));

        var converted = converter.convert(date);

        assertThat(converted).isEqualTo(OffsetDateTime.of(2024, 5, 17, 10, 30, 0, 0, ZoneOffset.UTC));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testOffsetDateTimeToDate() {
        var converter = new DefaultOffsetDateTimeToDateConverter();
        var offsetDateTime = OffsetDateTime.of(2024, 5, 17, 10, 30, 0, 0, ZoneOffset.UTC);

        var converted = converter.convert(offsetDateTime);

        assertThat(converted).isEqualTo(Date.from(Instant.parse("2024-05-17T10:30:00Z")));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testCalendarToOffsetDateTimeUsesTheCalendarsOwnTimeZone() {
        var converter = new DefaultCalendarToOffsetDateTimeConverter();
        var calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.set(2024, Calendar.MAY, 17, 10, 30, 0);

        var converted = converter.convert(calendar);

        assertThat(converted).isEqualTo(OffsetDateTime.of(2024, 5, 17, 10, 30, 0, 0, ZoneOffset.UTC));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testOffsetDateTimeToCalendarRoundTripsToTheSameInstant() {
        var toCalendar = new DefaultOffsetDateTimeToCalendarConverter();
        var toOffsetDateTime = new DefaultCalendarToOffsetDateTimeConverter();
        var offsetDateTime = OffsetDateTime.of(2024, 5, 17, 10, 30, 0, 0, ZoneOffset.ofHours(2));

        var calendar = toCalendar.convert(offsetDateTime);
        var roundTripped = toOffsetDateTime.convert(calendar);

        assertThat(roundTripped.toInstant()).isEqualTo(offsetDateTime.toInstant());
        assertThat(toCalendar.convert(null)).isNull();
    }

    @Test
    public void testLocalDateTimeToOffsetDateTimeUTC() {
        var converter = new DefaultLocalDateTimeToOffsetDateTimeUTCConverter();
        var localDateTime = LocalDateTime.of(2024, 5, 17, 10, 30);

        var converted = converter.convert(localDateTime);

        assertThat(converted).isEqualTo(OffsetDateTime.of(2024, 5, 17, 10, 30, 0, 0, ZoneOffset.UTC));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testOffsetDateTimeToLocalDateTimeNormalizesToUTCFirst() {
        // the offset is not simply stripped: a non-UTC OffsetDateTime is shifted to the UTC instant first.
        var converter = new DefaultOffsetDateTimeToLocalDateTimeConverter();
        var offsetDateTime = OffsetDateTime.of(2024, 5, 18, 1, 0, 0, 0, ZoneOffset.ofHours(3));

        var converted = converter.convert(offsetDateTime);

        assertThat(converted).isEqualTo(LocalDateTime.of(2024, 5, 17, 22, 0));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testLocalDateToOffsetDateTimeUTCUsesStartOfDay() {
        var converter = new DefaultLocalDateToOffsetDateTimeUTCConverter();
        var localDate = LocalDate.of(2024, 5, 17);

        var converted = converter.convert(localDate);

        assertThat(converted).isEqualTo(OffsetDateTime.of(2024, 5, 17, 0, 0, 0, 0, ZoneOffset.UTC));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testOffsetDateTimeToLocalDateNormalizesToUTCFirst() {
        // a date-crossing example: 2024-05-18T01:00+03:00 is 2024-05-17T22:00Z, one calendar day earlier.
        var converter = new DefaultOffsetDateTimeToLocalDateConverter();
        var offsetDateTime = OffsetDateTime.of(2024, 5, 18, 1, 0, 0, 0, ZoneOffset.ofHours(3));

        var converted = converter.convert(offsetDateTime);

        assertThat(converted).isEqualTo(LocalDate.of(2024, 5, 17));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testLocalDateTimeToLocalTime() {
        var converter = new DefaultLocalDateTimeToLocalTimeConverter();
        var localDateTime = LocalDateTime.of(2024, 5, 17, 10, 30, 15);

        assertThat(converter.convert(localDateTime)).isEqualTo(LocalTime.of(10, 30, 15));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testLocalTimeToLocalDateTimeUsesTheUnixEpochDate() {
        var converter = new DefaultLocalTimeToLocalDateTimeConverter();
        var localTime = LocalTime.of(10, 30, 15);

        var converted = converter.convert(localTime);

        assertThat(converted).isEqualTo(LocalDateTime.of(1970, 1, 1, 10, 30, 15));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testOffsetTimeToOffsetDateTimeUsesTheUnixEpochDate() {
        var converter = new DefaultOffsetTimeToOffsetDateTimeConverter();
        var offsetTime = OffsetTime.of(10, 30, 15, 0, ZoneOffset.ofHours(2));

        var converted = converter.convert(offsetTime);

        assertThat(converted).isEqualTo(
            OffsetDateTime.of(1970, 1, 1, 10, 30, 15, 0, ZoneOffset.ofHours(2)));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testOffsetDateTimeToOffsetTimeKeepsTheOffset() {
        var converter = new DefaultOffsetDateTimeToOffsetTimeConverter();
        var offsetDateTime = OffsetDateTime.of(2024, 5, 17, 10, 30, 15, 0, ZoneOffset.ofHours(2));

        var converted = converter.convert(offsetDateTime);

        assertThat(converted).isEqualTo(OffsetTime.of(10, 30, 15, 0, ZoneOffset.ofHours(2)));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testOffsetDateTimeToZonedDateTimeKeepsTheOffset() {
        var converter = new DefaultOffsetDateTimeToZonedDateTimeConverter();
        var offsetDateTime = OffsetDateTime.of(2024, 5, 17, 10, 30, 15, 0, ZoneOffset.ofHours(2));

        var converted = converter.convert(offsetDateTime);

        assertThat(converted).isEqualTo(ZonedDateTime.of(2024, 5, 17, 10, 30, 15, 0, ZoneOffset.ofHours(2)));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testZonedDateTimeToOffsetDateTime() {
        var converter = new DefaultZonedDateTimeToOffsetDateTimeConverter();
        var zonedDateTime = ZonedDateTime.of(2024, 5, 17, 10, 30, 15, 0, ZoneId.of("Europe/Berlin"));

        var converted = converter.convert(zonedDateTime);

        assertThat(converted.toInstant()).isEqualTo(zonedDateTime.toInstant());
        assertThat(converted.getOffset()).isEqualTo(zonedDateTime.getOffset());
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testIntegerToYear() {
        var converter = new DefaultIntegerToYearConverter();

        assertThat(converter.convert(2024)).isEqualTo(Year.of(2024));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testYearToInteger() {
        var converter = new DefaultYearToIntegerConverter();

        assertThat(converter.convert(Year.of(2024))).isEqualTo(2024);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testShortToYear() {
        var converter = new DefaultShortToYearConverter();

        assertThat(converter.convert((short) 2024)).isEqualTo(Year.of(2024));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testYearToShort() {
        var converter = new DefaultYearToShortConverter();

        assertThat(converter.convert(Year.of(2024))).isEqualTo((short) 2024);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testMonthDayToShort() {
        var converter = new DefaultMonthDayToShortConverter();

        assertThat(converter.convert(MonthDay.of(12, 25))).isEqualTo((short) 1225);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testShortToMonthDay() {
        var converter = new DefaultShortToMonthDayConverter();

        assertThat(converter.convert((short) 1225)).isEqualTo(MonthDay.of(12, 25));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testYearMonthToIntegerAndBackRoundTripForAPositiveYear() {
        var toInteger = new DefaultYearMonthToIntegerConverter();
        var toYearMonth = new DefaultIntegerToYearMonthConverter();
        var yearMonth = YearMonth.of(2024, 5);

        var encoded = toInteger.convert(yearMonth);
        var decoded = toYearMonth.convert(encoded);

        assertThat(encoded).isEqualTo(202405);
        assertThat(decoded).isEqualTo(yearMonth);
        assertThat(toInteger.convert(null)).isNull();
        assertThat(toYearMonth.convert(null)).isNull();
    }

    @Test
    public void testYearMonthToIntegerAndBackRoundTripForANegativeYear() {
        // the month is encoded with a sign matching the year's sign, and decoded back via Math.abs(...) -
        // this only round-trips correctly because of that paired encode/decode logic.
        var toInteger = new DefaultYearMonthToIntegerConverter();
        var toYearMonth = new DefaultIntegerToYearMonthConverter();
        var yearMonth = YearMonth.of(-5, 12);

        var encoded = toInteger.convert(yearMonth);
        var decoded = toYearMonth.convert(encoded);

        assertThat(encoded).isEqualTo(-512);
        assertThat(decoded).isEqualTo(yearMonth);
    }

    @Test
    public void testYearMonthToLongAndBackRoundTripForAPositiveYear() {
        var toLong = new DefaultYearMonthToLongConverter();
        var toYearMonth = new DefaultLongToYearMonthConverter();
        var yearMonth = YearMonth.of(2024, 5);

        var encoded = toLong.convert(yearMonth);
        var decoded = toYearMonth.convert(encoded);

        assertThat(encoded).isEqualTo(202405L);
        assertThat(decoded).isEqualTo(yearMonth);
        assertThat(toLong.convert(null)).isNull();
        assertThat(toYearMonth.convert(null)).isNull();
    }

    @Test
    public void testYearMonthToLongAndBackRoundTripForANegativeYear() {
        var toLong = new DefaultYearMonthToLongConverter();
        var toYearMonth = new DefaultLongToYearMonthConverter();
        var yearMonth = YearMonth.of(-5, 12);

        var encoded = toLong.convert(yearMonth);
        var decoded = toYearMonth.convert(encoded);

        assertThat(encoded).isEqualTo(-512L);
        assertThat(decoded).isEqualTo(yearMonth);
    }
}
