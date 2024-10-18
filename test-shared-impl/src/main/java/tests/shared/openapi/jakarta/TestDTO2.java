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

package tests.shared.openapi.jakarta;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tests.shared.openapi.TestId;
import tests.shared.openapi.TestIdInterface;

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
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class TestDTO2 {

    private @Size(max = 5) String stringSized;
    private Optional<@Size(max = 5) String> optionalStringSized;
    private @NotNull String stringNotNull;
    private @NotEmpty String stringNotEmpty;
    private @NotBlank String stringNotBlank;
    private @Pattern(regexp = "[0-9]") String stringPattern;
    private @Email String stringEmail;
    private Optional<@Email String> optionalStringEmail;
    private Optional<@Pattern(regexp = "[0-9]") String> optionalStringPattern;
    private @Min(5) BigDecimal bigDecimalMin;
    private @Min(5) BigInteger bigIntegerMin;
    private @Min(5) int anIntMin;
    private @Min(5) long aLongMin;
    private @Min(5) byte aByteMin;
    private @Min(5) short aShortMin;
    private Optional<@Min(5) BigDecimal> optionalBigDecimalMin;
    private Optional<@Min(5) BigInteger> optionalBigIntegerMin;
    private Optional<@Min(5) Integer> optionalIntegerMin;
    private Optional<@Min(5) Long> optionalLongMin;
    private Optional<@Min(5) Byte> optionalByteMin;
    private Optional<@Min(5) Short> optionalShortMin;
    private @Max(5) BigDecimal bigDecimalMax;
    private @Max(5) BigInteger bigIntegerMax;
    private @Max(5) int anIntMax;
    private @Max(5) long aLongMax;
    private @Max(5) byte aByteMax;
    private @Max(5) short aShortMax;
    private Optional<@Max(5) BigDecimal> optionalBigDecimalMax;
    private Optional<@Max(5) BigInteger> optionalBigIntegerMax;
    private Optional<@Max(5) Integer> optionalIntegerMax;
    private Optional<@Max(5) Long> optionalLongMax;
    private Optional<@Max(5) Byte> optionalByteMax;
    private Optional<@Max(5) Short> optionalShortMax;
    private @DecimalMin("5.0") BigDecimal bigDecimalDecimalMin;
    private @DecimalMin("5.0") BigInteger bigIntegerDecimalMin;
    private @DecimalMin("5.0") int anIntDecimalMin;
    private @DecimalMin("5.0") long aLongDecimalMin;
    private @DecimalMin("5.0") byte aByteDecimalMin;
    private @DecimalMin("5.0") short aShortDecimalMin;
    private @DecimalMin("5.0") float aFloatDecimalMin;
    private @DecimalMin("5.0") double aDoubleDecimalMin;
    private Optional<@DecimalMin("5.0") BigDecimal> optionalBigDecimalDecimalMin;
    private Optional<@DecimalMin("5.0") BigInteger> optionalBigIntegerDecimalMin;
    private Optional<@DecimalMin("5.0") Integer> optionalIntegerDecimalMin;
    private Optional<@DecimalMin("5.0") Long> optionalLongDecimalMin;
    private Optional<@DecimalMin("5.0") Byte> optionalByteDecimalMin;
    private Optional<@DecimalMin("5.0") Short> optionalShortDecimalMin;
    private Optional<@DecimalMin("5.0") Float> optionalFloatDecimalMin;
    private Optional<@DecimalMin("5.0") Double> optionalDoubleDecimalMin;
    private @DecimalMax(value = "5.0", inclusive = false) BigDecimal bigDecimalDecimalMaxExclusive;
    private @DecimalMax(value = "5.0", inclusive = false) BigInteger bigIntegerDecimalMaxExclusive;
    private @DecimalMax(value = "5.0", inclusive = false) int anIntDecimalMaxExclusive;
    private @DecimalMax(value = "5.0", inclusive = false) long aLongDecimalMaxExclusive;
    private @DecimalMax(value = "5.0", inclusive = false) byte aByteDecimalMaxExclusive;
    private @DecimalMax(value = "5.0", inclusive = false) short aShortDecimalMaxExclusive;
    private @DecimalMax(value = "5.0", inclusive = false) float aFloatDecimalMaxExclusive;
    private @DecimalMax(value = "5.0", inclusive = false) double aDoubleDecimalMaxExclusive;
    private Optional<@DecimalMax(value = "5.0", inclusive = false) BigDecimal> optionalBigDecimalDecimalMaxExclusive;
    private Optional<@DecimalMax(value = "5.0", inclusive = false) BigInteger> optionalBigIntegerDecimalMaxExclusive;
    private Optional<@DecimalMax(value = "5.0", inclusive = false) Integer> optionalIntegerDecimalMaxExclusive;
    private Optional<@DecimalMax(value = "5.0", inclusive = false) Long> optionalLongDecimalMaxExclusive;
    private Optional<@DecimalMax(value = "5.0", inclusive = false) Byte> optionalByteDecimalMaxExclusive;
    private Optional<@DecimalMax(value = "5.0", inclusive = false) Short> optionalShortDecimalMaxExclusive;
    private Optional<@DecimalMax(value = "5.0", inclusive = false) Float> optionalFloatDecimalMaxExclusive;
    private Optional<@DecimalMax(value = "5.0", inclusive = false) Double> optionalDoubleDecimalMaxExclusive;
    private @Negative BigDecimal bigDecimalNegative;
    private @Negative BigInteger bigIntegerNegative;
    private @Negative int anIntNegative;
    private @Negative long aLongNegative;
    private @Negative byte aByteNegative;
    private @Negative short aShortNegative;
    private @Negative float aFloatNegative;
    private @Negative double aDoubleNegative;
    private Optional<@Negative BigDecimal> optionalBigDecimalNegative;
    private Optional<@Negative BigInteger> optionalBigIntegerNegative;
    private Optional<@Negative Integer> optionalIntegerNegative;
    private Optional<@Negative Long> optionalLongNegative;
    private Optional<@Negative Byte> optionalByteNegative;
    private Optional<@Negative Short> optionalShortNegative;
    private Optional<@Negative Float> optionalFloatNegative;
    private Optional<@Negative Double> optionalDoubleNegative;
    private @Size(max = 2) List<String> stringListSized;
    private @Size(max = 2) String[] stringArraySized;
    private @NotEmpty List<String> stringListNotEmpty;
    private @NotEmpty String[] stringArrayNotEmpty;
    private @Digits(integer = 3, fraction = 2) BigDecimal bigDecimalDigits;
    private @Digits(integer = 3, fraction = 0) BigInteger bigIntegerDigits;
    private @Digits(integer = 3, fraction = 0) int anIntDigits;
    private @Digits(integer = 3, fraction = 0) long aLongDigits;
    private @Digits(integer = 1, fraction = 0) byte aByteDigits;
    private @Digits(integer = 3, fraction = 2) short aShortDigits;
    private @Digits(integer = 3, fraction = 2) float aFloatDigits;
    private @Digits(integer = 3, fraction = 2) double aDoubleDigits;
    private Optional<@Digits(integer = 3, fraction = 2) BigDecimal> optionalBigDecimalDigits;
    private Optional<@Digits(integer = 3, fraction = 0) BigInteger> optionalBigIntegerDigits;
    private Optional<@Digits(integer = 3, fraction = 0) Integer> optionalIntegerDigits;
    private Optional<@Digits(integer = 3, fraction = 0) Long> optionalLongDigits;
    private Optional<@Digits(integer = 1, fraction = 0) Byte> optionalByteDigits;
    private Optional<@Digits(integer = 3, fraction = 0) Short> optionalShortDigits;
    private Optional<@Digits(integer = 3, fraction = 2) Float> optionalFloatDigits;
    private Optional<@Digits(integer = 3, fraction = 2) Double> optionalDoubleDigits;
    private @Past Instant instantPast;
    private Optional<@Past Instant> optionalInstantPast;
    private @Future LocalDate localDateFuture;
    private Optional<@Future LocalDate> optionalLocalDateFuture;
    private @PastOrPresent LocalDateTime localDateTimePastOrPresent;
    private Optional<@PastOrPresent LocalDateTime> optionalLocalDateTimePastOrPresent;
    @NotNull
    private LocalTime localTimeNotNull;
    private Optional<@Past LocalTime> optionalLocalTimePast;
    private @FutureOrPresent ZonedDateTime zonedDateTimeFutureOrPresent;
    private Optional<@FutureOrPresent ZonedDateTime> optionalZonedDateTimeFutureOrPresent;
    @NotNull
    private OffsetDateTime offsetDateTimeNotNull;
    private Optional<@Past OffsetDateTime> optionalOffsetDateTimePast;
    private @Past OffsetTime offsetTimePast;
    private Optional<@Past OffsetTime> optionalOffsetTimePast;
    private @Past YearMonth yearMonthPast;
    private Optional<@Past YearMonth> optionalYearMonthPast;
    private @Past MonthDay monthDayPast;
    private Optional<@Past MonthDay> optionalMonthDayPast;
    private @Past Year yearPast;
    private Optional<@Past Year> optionalYearPast;

    private TestId testId;

    private Optional<TestId> optionalTestId;

    private TestIdInterface testIdInterface;


    public TestDTO2() {
    }

    @Builder
    public TestDTO2(String stringSized,
                    String optionalStringSized,
                    String stringNotNull,
                    String stringNotEmpty,
                    String stringNotBlank,
                    String stringPattern,
                    String stringEmail,
                    String optionalStringEmail,
                    String optionalStringPattern,
                    BigDecimal bigDecimalMin,
                    BigInteger bigIntegerMin,
                    int anIntMin,
                    long aLongMin,
                    byte aByteMin,
                    short aShortMin,
                    BigDecimal optionalBigDecimalMin,
                    BigInteger optionalBigIntegerMin,
                    Integer optionalIntegerMin,
                    Long optionalLongMin,
                    Byte optionalByteMin,
                    Short optionalShortMin,
                    BigDecimal bigDecimalMax,
                    BigInteger bigIntegerMax,
                    int anIntMax,
                    long aLongMax,
                    byte aByteMax,
                    short aShortMax,
                    BigDecimal optionalBigDecimalMax,
                    BigInteger optionalBigIntegerMax,
                    Integer optionalIntegerMax,
                    Long optionalLongMax,
                    Byte optionalByteMax,
                    Short optionalShortMax,
                    BigDecimal bigDecimalDecimalMin,
                    BigInteger bigIntegerDecimalMin,
                    int anIntDecimalMin,
                    long aLongDecimalMin,
                    byte aByteDecimalMin,
                    short aShortDecimalMin,
                    float aFloatDecimalMin,
                    double aDoubleDecimalMin,
                    BigDecimal optionalBigDecimalDecimalMin,
                    BigInteger optionalBigIntegerDecimalMin,
                    Integer optionalIntegerDecimalMin,
                    Long optionalLongDecimalMin,
                    Byte optionalByteDecimalMin,
                    Short optionalShortDecimalMin,
                    Float optionalFloatDecimalMin,
                    Double optionalDoubleDecimalMin,
                    BigDecimal bigDecimalDecimalMaxExclusive,
                    BigInteger bigIntegerDecimalMaxExclusive,
                    int anIntDecimalMaxExclusive,
                    long aLongDecimalMaxExclusive,
                    byte aByteDecimalMaxExclusive,
                    short aShortDecimalMaxExclusive,
                    float aFloatDecimalMaxExclusive,
                    double aDoubleDecimalMaxExclusive,
                    BigDecimal optionalBigDecimalDecimalMaxExclusive,
                    BigInteger optionalBigIntegerDecimalMaxExclusive,
                    Integer optionalIntegerDecimalMaxExclusive,
                    Long optionalLongDecimalMaxExclusive,
                    Byte optionalByteDecimalMaxExclusive,
                    Short optionalShortDecimalMaxExclusive,
                    Float optionalFloatDecimalMaxExclusive,
                    Double optionalDoubleDecimalMaxExclusive,
                    BigDecimal bigDecimalNegative,
                    BigInteger bigIntegerNegative,
                    int anIntNegative,
                    long aLongNegative,
                    byte aByteNegative,
                    short aShortNegative,
                    float aFloatNegative,
                    double aDoubleNegative,
                    BigDecimal optionalBigDecimalNegative,
                    BigInteger optionalBigIntegerNegative,
                    Integer optionalIntegerNegative,
                    Long optionalLongNegative,
                    Byte optionalByteNegative,
                    Short optionalShortNegative,
                    Float optionalFloatNegative,
                    Double optionalDoubleNegative,
                    List<String> stringListSized,
                    String[] stringArraySized,
                    List<String> stringListNotEmpty,
                    String[] stringArrayNotEmpty,
                    BigDecimal bigDecimalDigits,
                    BigInteger bigIntegerDigits,
                    int anIntDigits,
                    long aLongDigits,
                    byte aByteDigits,
                    short aShortDigits,
                    float aFloatDigits,
                    double aDoubleDigits,
                    BigDecimal optionalBigDecimalDigits,
                    BigInteger optionalBigIntegerDigits,
                    Integer optionalIntegerDigits,
                    Long optionalLongDigits,
                    Byte optionalByteDigits,
                    Short optionalShortDigits,
                    Float optionalFloatDigits,
                    Double optionalDoubleDigits,
                    Instant instantPast,
                    Instant optionalInstantPast,
                    LocalDate localDateFuture,
                    LocalDate optionalLocalDateFuture,
                    LocalDateTime localDateTimePastOrPresent,
                    LocalDateTime optionalLocalDateTimePastOrPresent,
                    LocalTime localTimeNotNull,
                    LocalTime optionalLocalTimePast,
                    ZonedDateTime zonedDateTimeFutureOrPresent,
                    ZonedDateTime optionalZonedDateTimeFutureOrPresent,
                    OffsetDateTime offsetDateTimeNotNull,
                    OffsetDateTime optionalOffsetDateTimePast,
                    OffsetTime offsetTimePast,
                    OffsetTime optionalOffsetTimePast,
                    YearMonth yearMonthPast,
                    YearMonth optionalYearMonthPast,
                    MonthDay monthDayPast,
                    MonthDay optionalMonthDayPast,
                    Year yearPast,
                    Year optionalYearPast,
                    TestId testId,
                    TestId optionalTestId,
                    TestIdInterface testIdInterface
    ) {
        this.stringSized = stringSized;
        this.optionalStringSized = Optional.ofNullable(optionalStringSized);
        this.stringNotNull = stringNotNull;
        this.stringNotEmpty = stringNotEmpty;
        this.stringNotBlank = stringNotBlank;
        this.stringPattern = stringPattern;
        this.stringEmail = stringEmail;
        this.optionalStringEmail = Optional.ofNullable(optionalStringEmail);
        this.optionalStringPattern = Optional.ofNullable(optionalStringPattern);
        this.bigDecimalMin = bigDecimalMin;
        this.bigIntegerMin = bigIntegerMin;
        this.anIntMin = anIntMin;
        this.aLongMin = aLongMin;
        this.aByteMin = aByteMin;
        this.aShortMin = aShortMin;
        this.optionalBigDecimalMin = Optional.ofNullable(optionalBigDecimalMin);
        this.optionalBigIntegerMin = Optional.ofNullable(optionalBigIntegerMin);
        this.optionalIntegerMin = Optional.ofNullable(optionalIntegerMin);
        this.optionalLongMin = Optional.ofNullable(optionalLongMin);
        this.optionalByteMin = Optional.ofNullable(optionalByteMin);
        this.optionalShortMin = Optional.ofNullable(optionalShortMin);
        this.bigDecimalMax = bigDecimalMax;
        this.bigIntegerMax = bigIntegerMax;
        this.anIntMax = anIntMax;
        this.aLongMax = aLongMax;
        this.aByteMax = aByteMax;
        this.aShortMax = aShortMax;
        this.optionalBigDecimalMax = Optional.ofNullable(optionalBigDecimalMax);
        this.optionalBigIntegerMax = Optional.ofNullable(optionalBigIntegerMax);
        this.optionalIntegerMax = Optional.ofNullable(optionalIntegerMax);
        this.optionalLongMax = Optional.ofNullable(optionalLongMax);
        this.optionalByteMax = Optional.ofNullable(optionalByteMax);
        this.optionalShortMax = Optional.ofNullable(optionalShortMax);
        this.bigDecimalDecimalMin = bigDecimalDecimalMin;
        this.bigIntegerDecimalMin = bigIntegerDecimalMin;
        this.anIntDecimalMin = anIntDecimalMin;
        this.aLongDecimalMin = aLongDecimalMin;
        this.aByteDecimalMin = aByteDecimalMin;
        this.aShortDecimalMin = aShortDecimalMin;
        this.aFloatDecimalMin = aFloatDecimalMin;
        this.aDoubleDecimalMin = aDoubleDecimalMin;
        this.optionalBigDecimalDecimalMin = Optional.ofNullable(optionalBigDecimalDecimalMin);
        this.optionalBigIntegerDecimalMin = Optional.ofNullable(optionalBigIntegerDecimalMin);
        this.optionalIntegerDecimalMin = Optional.ofNullable(optionalIntegerDecimalMin);
        this.optionalLongDecimalMin = Optional.ofNullable(optionalLongDecimalMin);
        this.optionalByteDecimalMin = Optional.ofNullable(optionalByteDecimalMin);
        this.optionalShortDecimalMin = Optional.ofNullable(optionalShortDecimalMin);
        this.optionalFloatDecimalMin = Optional.ofNullable(optionalFloatDecimalMin);
        this.optionalDoubleDecimalMin = Optional.ofNullable(optionalDoubleDecimalMin);
        this.bigDecimalDecimalMaxExclusive = bigDecimalDecimalMaxExclusive;
        this.bigIntegerDecimalMaxExclusive = bigIntegerDecimalMaxExclusive;
        this.anIntDecimalMaxExclusive = anIntDecimalMaxExclusive;
        this.aLongDecimalMaxExclusive = aLongDecimalMaxExclusive;
        this.aByteDecimalMaxExclusive = aByteDecimalMaxExclusive;
        this.aShortDecimalMaxExclusive = aShortDecimalMaxExclusive;
        this.aFloatDecimalMaxExclusive = aFloatDecimalMaxExclusive;
        this.aDoubleDecimalMaxExclusive = aDoubleDecimalMaxExclusive;
        this.optionalBigDecimalDecimalMaxExclusive = Optional.ofNullable(optionalBigDecimalDecimalMaxExclusive);
        this.optionalBigIntegerDecimalMaxExclusive = Optional.ofNullable(optionalBigIntegerDecimalMaxExclusive);
        this.optionalIntegerDecimalMaxExclusive = Optional.ofNullable(optionalIntegerDecimalMaxExclusive);
        this.optionalLongDecimalMaxExclusive = Optional.ofNullable(optionalLongDecimalMaxExclusive);
        this.optionalByteDecimalMaxExclusive = Optional.ofNullable(optionalByteDecimalMaxExclusive);
        this.optionalShortDecimalMaxExclusive = Optional.ofNullable(optionalShortDecimalMaxExclusive);
        this.optionalFloatDecimalMaxExclusive = Optional.ofNullable(optionalFloatDecimalMaxExclusive);
        this.optionalDoubleDecimalMaxExclusive = Optional.ofNullable(optionalDoubleDecimalMaxExclusive);
        this.bigDecimalNegative = bigDecimalNegative;
        this.bigIntegerNegative = bigIntegerNegative;
        this.anIntNegative = anIntNegative;
        this.aLongNegative = aLongNegative;
        this.aByteNegative = aByteNegative;
        this.aShortNegative = aShortNegative;
        this.aFloatNegative = aFloatNegative;
        this.aDoubleNegative = aDoubleNegative;
        this.optionalBigDecimalNegative = Optional.ofNullable(optionalBigDecimalNegative);
        this.optionalBigIntegerNegative = Optional.ofNullable(optionalBigIntegerNegative);
        this.optionalIntegerNegative = Optional.ofNullable(optionalIntegerNegative);
        this.optionalLongNegative = Optional.ofNullable(optionalLongNegative);
        this.optionalByteNegative = Optional.ofNullable(optionalByteNegative);
        this.optionalShortNegative = Optional.ofNullable(optionalShortNegative);
        this.optionalFloatNegative = Optional.ofNullable(optionalFloatNegative);
        this.optionalDoubleNegative = Optional.ofNullable(optionalDoubleNegative);
        this.stringListSized = stringListSized;
        this.stringArraySized = stringArraySized;
        this.stringListNotEmpty = stringListNotEmpty;
        this.stringArrayNotEmpty = stringArrayNotEmpty;
        this.bigDecimalDigits = bigDecimalDigits;
        this.bigIntegerDigits = bigIntegerDigits;
        this.anIntDigits = anIntDigits;
        this.aLongDigits = aLongDigits;
        this.aByteDigits = aByteDigits;
        this.aShortDigits = aShortDigits;
        this.aFloatDigits = aFloatDigits;
        this.aDoubleDigits = aDoubleDigits;
        this.optionalBigDecimalDigits = Optional.ofNullable(optionalBigDecimalDigits);
        this.optionalBigIntegerDigits = Optional.ofNullable(optionalBigIntegerDigits);
        this.optionalIntegerDigits = Optional.ofNullable(optionalIntegerDigits);
        this.optionalLongDigits = Optional.ofNullable(optionalLongDigits);
        this.optionalByteDigits = Optional.ofNullable(optionalByteDigits);
        this.optionalShortDigits = Optional.ofNullable(optionalShortDigits);
        this.optionalFloatDigits = Optional.ofNullable(optionalFloatDigits);
        this.optionalDoubleDigits = Optional.ofNullable(optionalDoubleDigits);
        this.instantPast = instantPast;
        this.optionalInstantPast = Optional.ofNullable(optionalInstantPast);
        this.localDateFuture = localDateFuture;
        this.optionalLocalDateFuture = Optional.ofNullable(optionalLocalDateFuture);
        this.localDateTimePastOrPresent = localDateTimePastOrPresent;
        this.optionalLocalDateTimePastOrPresent = Optional.ofNullable(optionalLocalDateTimePastOrPresent);
        this.localTimeNotNull = localTimeNotNull;
        this.optionalLocalTimePast = Optional.ofNullable(optionalLocalTimePast);
        this.zonedDateTimeFutureOrPresent = zonedDateTimeFutureOrPresent;
        this.optionalZonedDateTimeFutureOrPresent = Optional.ofNullable(optionalZonedDateTimeFutureOrPresent);
        this.offsetDateTimeNotNull = offsetDateTimeNotNull;
        this.optionalOffsetDateTimePast = Optional.ofNullable(optionalOffsetDateTimePast);
        this.offsetTimePast = offsetTimePast;
        this.optionalOffsetTimePast = Optional.ofNullable(optionalOffsetTimePast);
        this.yearMonthPast = yearMonthPast;
        this.optionalYearMonthPast = Optional.ofNullable(optionalYearMonthPast);
        this.monthDayPast = monthDayPast;
        this.optionalMonthDayPast = Optional.ofNullable(optionalMonthDayPast);
        this.yearPast = yearPast;
        this.optionalYearPast = Optional.ofNullable(optionalYearPast);
        this.testId = testId;
        this.optionalTestId = Optional.ofNullable(optionalTestId);
        this.testIdInterface = testIdInterface;
    }
}
