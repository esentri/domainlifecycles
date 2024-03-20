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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import nitrox.dlc.domain.types.ValueObject;
import tests.shared.openapi.TestId;
import tests.shared.openapi.TestIdInterface;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Getter
public class TestVo2 implements ValueObject {

    private final @Size(max=5) String stringSized;
    private final Optional<@Size(max=5) String> optionalStringSized;
    private final @NotNull String stringNotNull;
    private final @NotEmpty String stringNotEmpty;
    private final @NotBlank String stringNotBlank;
    private final @Pattern(regexp = "[0-9]") String stringPattern;
    private final @Email String stringEmail;
    private final Optional<@Email String> optionalStringEmail;
    private final Optional<@Pattern(regexp = "[0-9]") String> optionalStringPattern;
    private final @Min(5) BigDecimal bigDecimalMin;
    private final @Min(5) int anIntMin;
    private final Optional<@Min(5) BigDecimal> optionalBigDecimalMin;
    private final @DecimalMin("5.0") BigDecimal bigDecimalDecimalMin;
    private final @DecimalMin("5.0") int anIntDecimalMin;
    private final Optional<@DecimalMin("5.0") BigDecimal> optionalBigDecimalDecimalMin;
    private final @DecimalMax(value = "5.0", inclusive = false) BigDecimal bigDecimalDecimalMaxExclusive;
    private final @DecimalMax(value = "5.0", inclusive = false) int anIntDecimalMaxExclusive;
    private final Optional<@DecimalMax(value = "5.0", inclusive = false) BigDecimal> optionalBigDecimalDecimalMaxExclusive;

    private final @Size(max = 2)  List<TestId> listTestIdSized;
    private final @Digits(integer = 3, fraction = 2) double aDoubleDigits;
    private final TestId testId;
    private final Optional<TestId> optionalTestId;
    private final TestIdInterface testIdInterface;


    @Builder
    public TestVo2(String stringSized,
                   String optionalStringSized,
                   String stringNotNull,
                   String stringNotEmpty,
                   String stringNotBlank,
                   String stringPattern,
                   String stringEmail,
                   String optionalStringEmail,
                   String optionalStringPattern,
                   BigDecimal bigDecimalMin,
                   int anIntMin,
                   BigDecimal optionalBigDecimalMin,
                   BigDecimal bigDecimalDecimalMin,
                   int anIntDecimalMin,
                   BigDecimal optionalBigDecimalDecimalMin,
                   BigDecimal bigDecimalDecimalMaxExclusive,
                   int anIntDecimalMaxExclusive,
                   BigDecimal optionalBigDecimalDecimalMaxExclusive,
                   List<TestId> listTestIdSized,
                   double aDoubleDigits,
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
        this.anIntMin = anIntMin;
        this.optionalBigDecimalMin = Optional.ofNullable(optionalBigDecimalMin);
        this.bigDecimalDecimalMin = bigDecimalDecimalMin;
        this.anIntDecimalMin = anIntDecimalMin;
        this.optionalBigDecimalDecimalMin = Optional.ofNullable(optionalBigDecimalDecimalMin);
        this.bigDecimalDecimalMaxExclusive = bigDecimalDecimalMaxExclusive;
        this.anIntDecimalMaxExclusive = anIntDecimalMaxExclusive;
        this.optionalBigDecimalDecimalMaxExclusive = Optional.ofNullable(optionalBigDecimalDecimalMaxExclusive);
        this.listTestIdSized = listTestIdSized;
        this.aDoubleDigits = aDoubleDigits;
        this.testId = testId;
        this.optionalTestId = Optional.ofNullable(optionalTestId);
        this.testIdInterface = testIdInterface;
    }
}
