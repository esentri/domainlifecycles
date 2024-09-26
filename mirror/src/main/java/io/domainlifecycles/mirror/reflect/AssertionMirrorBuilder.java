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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.model.AssertionModel;
import io.domainlifecycles.mirror.model.AssertionType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

/**
 * Builder to create {@link AssertionMirror}. Uses Java reflection.
 *
 * For this attempt we just read mirror metadata from the bean validation annotations directly by reflection.
 * One might think of using the built-in bean validation metadata facilities, but to use them a bean validation provider must be
 * provided within the classpath. It is not possible to provide javax and jakarta implementations concurrently due to
 * version conflicts which might happen within the resolved classpath. So as long as we want to provide
 * support for both Bean Validation 2.0 and 3.0, we might stick to this way of implementing bean validation supported assertion mirrors.
 *
 * @author Mario Herb
 */
public class AssertionMirrorBuilder {
    private final Type annotatedType;
    private final Annotation annotation;
    private final boolean contained;
    private final boolean isCollection;
    private final String VALID_PACKAGE_NAME_JAVAX = "javax.validation.constraints";
    private final String VALID_PACKAGE_NAME_JAKARTA = "jakarta.validation.constraints";

    public AssertionMirrorBuilder(Type annotatedType, Annotation annotation, boolean contained, boolean isCollection){
        this.annotatedType = Objects.requireNonNull(annotatedType);
        this.annotation = Objects.requireNonNull(annotation);
        this.contained = contained;
        this.isCollection = isCollection;
    }

    /**
     * Creates a new {@link AssertionMirror}.
     *
     * @return new instance of AssertionMirror
     */
    public Optional<AssertionMirror> build(){
        if(isNullAnnotation()){
            return buildAssertionMirrorForNullAnnotation();
        }else if(isNotNullAnnotation()){
            return buildAssertionMirrorForNotNullAnnotation();
        }else if(isNotNullAnnotation()){
            return buildAssertionMirrorForNotNullAnnotation();
        }else if(isAssertTrueAnnotation()){
            return buildAssertionMirrorForAssertTrueAnnotation();
        }else if(isAssertFalseAnnotation()){
            return buildAssertionMirrorForAssertFalseAnnotation();
        }else if(isMinAnnotation()){
            return buildAssertionMirrorForMinAnnotation();
        }else if(isMaxAnnotation()){
            return buildAssertionMirrorForMaxAnnotation();
        }else if(isDecimalMinAnnotation()){
            return buildAssertionMirrorForDecimalMinAnnotation();
        }else if(isDecimalMaxAnnotation()){
            return buildAssertionMirrorForDecimalMaxAnnotation();
        }else if(isNegativeAnnotation()){
            return buildAssertionMirrorForNegativeAnnotation();
        }else if(isNegativeOrZeroAnnotation()){
            return buildAssertionMirrorForNegativeOrZeroAnnotation();
        }else if(isPositiveAnnotation()){
            return buildAssertionMirrorForPositiveAnnotation();
        }else if(isPositiveOrZeroAnnotation()){
            return buildAssertionMirrorForPositiveOrZeroAnnotation();
        }else if(isSizeAnnotation()){
           return buildAssertionMirrorForSizeAnnotation();
        }else if(isDigitsAnnotation()){
            return buildAssertionMirrorForDigitsAnnotation();
        }else if(isPastAnnotation()){
            return buildAssertionMirrorForPastAnnotation();
        }else if(isPastOrPresentAnnotation()){
            return buildAssertionMirrorForPastOrPresentAnnotation();
        }else if(isFutureAnnotation()){
            return buildAssertionMirrorForFutureAnnotation();
        }else if(isFutureOrPresentAnnotation()){
            return buildAssertionMirrorForFutureOrPresentAnnotation();
        }else if(isPatternAnnotation()){
            return buildAssertionMirrorForPatternAnnotation();
        }else if(isNotEmptyAnnotation()){
            return buildAssertionMirrorForNotEmptyAnnotation();
        }else if(isNotBlankAnnotation()){
            return buildAssertionMirrorForNotBlankAnnotation();
        }else if(isEmailAnnotation()){
            return buildAssertionMirrorForEmailAnnotation();
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForNullAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var nullAnnotation = (jakarta.validation.constraints.Null) annotation;
            return Optional.of(new AssertionModel(AssertionType.isNull, null, null, nullAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var nullAnnotation = (javax.validation.constraints.Null) annotation;
            return Optional.of(new AssertionModel(AssertionType.isNull, null, null, nullAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForNotNullAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var notNullAnnotation = (jakarta.validation.constraints.NotNull) annotation;
            return Optional.of(new AssertionModel(AssertionType.isNotNull, null, null, notNullAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var notNullAnnotation = (javax.validation.constraints.NotNull) annotation;
            return Optional.of(new AssertionModel(AssertionType.isNotNull, null, null, notNullAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForAssertTrueAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var assertTrueAnnotation = (jakarta.validation.constraints.AssertTrue) annotation;
            return Optional.of(new AssertionModel(AssertionType.isTrue, null, null, assertTrueAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var assertTrueAnnotation = (javax.validation.constraints.AssertTrue) annotation;
            return Optional.of(new AssertionModel(AssertionType.isTrue, null, null, assertTrueAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForAssertFalseAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var assertFalseAnnotation = (jakarta.validation.constraints.AssertFalse) annotation;
            return Optional.of(new AssertionModel(AssertionType.isFalse, null, null, assertFalseAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var assertFalseAnnotation = (javax.validation.constraints.AssertFalse) annotation;
            return Optional.of(new AssertionModel(AssertionType.isFalse, null, null, assertFalseAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForMinAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var minAnnotation = (jakarta.validation.constraints.Min) annotation;
            return Optional.of(new AssertionModel(AssertionType.isGreaterOrEqualNonDecimal, String.valueOf(minAnnotation.value()), null, minAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var minAnnotation = (javax.validation.constraints.Min) annotation;
            return Optional.of(new AssertionModel(AssertionType.isGreaterOrEqualNonDecimal, String.valueOf(minAnnotation.value()), null, minAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForMaxAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var maxAnnotation = (jakarta.validation.constraints.Max) annotation;
            return Optional.of(new AssertionModel(AssertionType.isLessOrEqualNonDecimal, null, String.valueOf(maxAnnotation.value()), maxAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var maxAnnotation = (javax.validation.constraints.Max) annotation;
            return Optional.of(new AssertionModel(AssertionType.isLessOrEqualNonDecimal, null, String.valueOf(maxAnnotation.value()), maxAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForDecimalMinAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var minDecimalAnnotation = (jakarta.validation.constraints.DecimalMin) annotation;
            if(minDecimalAnnotation.inclusive()){
                return Optional.of(new AssertionModel(AssertionType.isGreaterOrEqual, minDecimalAnnotation.value(), null, minDecimalAnnotation.message()));
            }else{
                return Optional.of(new AssertionModel(AssertionType.isGreaterThan, minDecimalAnnotation.value(), null, minDecimalAnnotation.message()));
            }

        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var minDecimalAnnotation = (javax.validation.constraints.DecimalMin) annotation;
            if(minDecimalAnnotation.inclusive()){
                return Optional.of(new AssertionModel(AssertionType.isGreaterOrEqual, minDecimalAnnotation.value(), null, minDecimalAnnotation.message()));
            }else{
                return Optional.of(new AssertionModel(AssertionType.isGreaterThan, minDecimalAnnotation.value(), null, minDecimalAnnotation.message()));
            }
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForDecimalMaxAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var maxDecimalAnnotation = (jakarta.validation.constraints.DecimalMax) annotation;
            if(maxDecimalAnnotation.inclusive()){
                return Optional.of(new AssertionModel(AssertionType.isLessOrEqual, null, maxDecimalAnnotation.value(), maxDecimalAnnotation.message()));
            }else{
                return Optional.of(new AssertionModel(AssertionType.isLessThan, null, maxDecimalAnnotation.value(), maxDecimalAnnotation.message()));
            }

        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var maxDecimalAnnotation = (javax.validation.constraints.DecimalMax) annotation;
            if(maxDecimalAnnotation.inclusive()){
                return Optional.of(new AssertionModel(AssertionType.isLessOrEqual, null, maxDecimalAnnotation.value(), maxDecimalAnnotation.message()));
            }else{
                return Optional.of(new AssertionModel(AssertionType.isLessThan, null, maxDecimalAnnotation.value(), maxDecimalAnnotation.message()));
            }
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForNegativeAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var negativeAnnotation = (jakarta.validation.constraints.Negative) annotation;
            return Optional.of(new AssertionModel(AssertionType.isNegative, null, null, negativeAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var negativeAnnotation = (javax.validation.constraints.Negative) annotation;
            return Optional.of(new AssertionModel(AssertionType.isNegative, null, null, negativeAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForNegativeOrZeroAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var negativeOrZeroAnnotation = (jakarta.validation.constraints.NegativeOrZero) annotation;
            return Optional.of(new AssertionModel(AssertionType.isNegativeOrZero, null, null, negativeOrZeroAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var negativeOrZeroAnnotation = (javax.validation.constraints.NegativeOrZero) annotation;
            return Optional.of(new AssertionModel(AssertionType.isNegativeOrZero, null, null, negativeOrZeroAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForPositiveAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var positiveAnnotation = (jakarta.validation.constraints.Positive) annotation;
            return Optional.of(new AssertionModel(AssertionType.isPositive, null, null, positiveAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var positiveAnnotation = (javax.validation.constraints.Positive) annotation;
            return Optional.of(new AssertionModel(AssertionType.isPositive, null, null, positiveAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForPositiveOrZeroAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var positiveOrZeroAnnotation = (jakarta.validation.constraints.PositiveOrZero) annotation;
            return Optional.of(new AssertionModel(AssertionType.isPositiveOrZero, null, null, positiveOrZeroAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var positiveOrZeroAnnotation = (javax.validation.constraints.PositiveOrZero) annotation;
            return Optional.of(new AssertionModel(AssertionType.isPositiveOrZero, null, null, positiveOrZeroAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForSizeAnnotation(){
        var assertionType = AssertionType.hasSize;
        if(annotatedType instanceof Class<?> && CharSequence.class.isAssignableFrom((Class<?>)annotatedType)
            && (!this.isCollection || this.contained)){
            assertionType = AssertionType.hasLength;
        }
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var sizeAnnotation = (jakarta.validation.constraints.Size) annotation;
            return Optional.of(new AssertionModel(assertionType, String.valueOf(sizeAnnotation.min()), String.valueOf(sizeAnnotation.max()), sizeAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var sizeAnnotation = (javax.validation.constraints.Size) annotation;
            return Optional.of(new AssertionModel(assertionType, String.valueOf(sizeAnnotation.min()), String.valueOf(sizeAnnotation.max()), sizeAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForDigitsAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var digitsAnnotation = (jakarta.validation.constraints.Digits) annotation;
            return Optional.of(new AssertionModel(AssertionType.hasMaxDigits, String.valueOf(digitsAnnotation.integer()), String.valueOf(digitsAnnotation.fraction()), digitsAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var digitsAnnotation = (javax.validation.constraints.Digits) annotation;
            return Optional.of(new AssertionModel(AssertionType.hasMaxDigits, String.valueOf(digitsAnnotation.integer()), String.valueOf(digitsAnnotation.fraction()), digitsAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForPastAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var pastAnnotation = (jakarta.validation.constraints.Past) annotation;
            return Optional.of(new AssertionModel(AssertionType.isPast, null, null, pastAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var pastAnnotation = (javax.validation.constraints.Past) annotation;
            return Optional.of(new AssertionModel(AssertionType.isPast, null, null, pastAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForPastOrPresentAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var pastOrPresentAnnotation = (jakarta.validation.constraints.PastOrPresent) annotation;
            return Optional.of(new AssertionModel(AssertionType.isPastOrPresent, null, null, pastOrPresentAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var pastOrPresentAnnotation = (javax.validation.constraints.PastOrPresent) annotation;
            return Optional.of(new AssertionModel(AssertionType.isPastOrPresent, null, null, pastOrPresentAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForFutureAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var futureAnnotation = (jakarta.validation.constraints.Future) annotation;
            return Optional.of(new AssertionModel(AssertionType.isFuture, null, null, futureAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var futureAnnotation = (javax.validation.constraints.Future) annotation;
            return Optional.of(new AssertionModel(AssertionType.isFuture, null, null, futureAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForFutureOrPresentAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var futureOrPresentAnnotation = (jakarta.validation.constraints.FutureOrPresent) annotation;
            return Optional.of(new AssertionModel(AssertionType.isFutureOrPresent, null, null, futureOrPresentAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var futureOrPresentAnnotation = (javax.validation.constraints.FutureOrPresent) annotation;
            return Optional.of(new AssertionModel(AssertionType.isFutureOrPresent, null, null, futureOrPresentAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForPatternAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var patternAnnotation = (jakarta.validation.constraints.Pattern) annotation;
            return Optional.of(new AssertionModel(AssertionType.regEx, patternAnnotation.regexp(), null, patternAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var patternAnnotation = (javax.validation.constraints.Pattern) annotation;
            return Optional.of(new AssertionModel(AssertionType.regEx, patternAnnotation.regexp(), null, patternAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForNotEmptyAnnotation(){
        var assertionType = AssertionType.isNotEmptyIterable;
        if(annotatedType instanceof Class<?> && CharSequence.class.isAssignableFrom((Class<?>)annotatedType)){
            assertionType = AssertionType.isNotEmpty;
        }
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var notEmptyAnnotation = (jakarta.validation.constraints.NotEmpty) annotation;
            return Optional.of(new AssertionModel(assertionType, null, null, notEmptyAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var notEmptyAnnotation = (javax.validation.constraints.NotEmpty) annotation;
            return Optional.of(new AssertionModel(assertionType, null, null, notEmptyAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForNotBlankAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var notBlankAnnotation = (jakarta.validation.constraints.NotBlank) annotation;
            return Optional.of(new AssertionModel(AssertionType.isNotBlank, null, null, notBlankAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var notBlankAnnotation = (javax.validation.constraints.NotBlank) annotation;
            return Optional.of(new AssertionModel(AssertionType.isNotBlank, null, null, notBlankAnnotation.message()));
        }
        return Optional.empty();
    }

    private Optional<AssertionMirror> buildAssertionMirrorForEmailAnnotation(){
        if(annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAKARTA)){
            var emailAnnotation = (jakarta.validation.constraints.Email) annotation;
            return Optional.of(new AssertionModel(AssertionType.isValidEmail, null, null, emailAnnotation.message()));
        } else if (annotation.annotationType().getName().startsWith(VALID_PACKAGE_NAME_JAVAX)){
            var emailAnnotation = (javax.validation.constraints.Email) annotation;
            return Optional.of(new AssertionModel(AssertionType.isValidEmail, null, null, emailAnnotation.message()));
        }
        return Optional.empty();
    }

    private boolean isNullAnnotation(){
        return "Null".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isNotNullAnnotation(){
        return "NotNull".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isAssertTrueAnnotation(){
        return "AssertTrue".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isAssertFalseAnnotation(){
        return "AssertFalse".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isMinAnnotation(){
        return "Min".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isMaxAnnotation(){
        return "Max".equals(annotationIdentifier(annotation.annotationType()));

    }

    private boolean isDecimalMinAnnotation(){
        return "DecimalMin".equals(annotationIdentifier(annotation.annotationType()));

    }

    private boolean isDecimalMaxAnnotation() {
        return "DecimalMax".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isNegativeAnnotation(){
        return "Negative".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isNegativeOrZeroAnnotation(){
        return "NegativeOrZero".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isPositiveAnnotation(){
        return "Positive".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isPositiveOrZeroAnnotation(){
        return "PositiveOrZero".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isSizeAnnotation(){
        return "Size".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isDigitsAnnotation(){
        return "Digits".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isPastAnnotation(){
        return "Past".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isPastOrPresentAnnotation(){
        return "PastOrPresent".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isFutureAnnotation(){
        return "Future".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isFutureOrPresentAnnotation(){
        return "FutureOrPresent".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isPatternAnnotation(){
        return "Pattern".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isNotEmptyAnnotation(){
        return "NotEmpty".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isNotBlankAnnotation(){
        return "NotBlank".equals(annotationIdentifier(annotation.annotationType()));
    }

    private boolean isEmailAnnotation(){
        return "Email".equals(annotationIdentifier(annotation.annotationType()));
    }

    private String annotationIdentifier(Class<? extends Annotation> annotationType){
        if(VALID_PACKAGE_NAME_JAVAX.equals(annotationType.getPackageName()) || VALID_PACKAGE_NAME_JAKARTA.equals(annotationType.getPackageName())){
            return annotationType.getSimpleName();
        }
        return null;
    }


}
