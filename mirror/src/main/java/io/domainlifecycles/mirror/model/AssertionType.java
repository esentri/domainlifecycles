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

package io.domainlifecycles.mirror.model;


/**
 * Assertions that can be used.
 *
 * @author Mario Herb
 */
public enum AssertionType {
    hasSize(AssertionTypeGroup.ITERABLE),
    hasSizeMax(AssertionTypeGroup.ITERABLE),
    hasSizeMin(AssertionTypeGroup.ITERABLE),
    hasMaxDigits(AssertionTypeGroup.NUMERIC_FRACTIONAL),
    hasMaxDigitsInteger(AssertionTypeGroup.NUMERIC),
    hasMaxDigitsFraction(AssertionTypeGroup.NUMERIC_FRACTIONAL),
    equals(AssertionTypeGroup.OBJECT),
    isFalse(AssertionTypeGroup.BOOLEAN),
    isTrue(AssertionTypeGroup.BOOLEAN),
    isOneOf(AssertionTypeGroup.OBJECT),
    hasLength(AssertionTypeGroup.STRING),
    hasLengthMax(AssertionTypeGroup.STRING),
    hasLengthMin(AssertionTypeGroup.STRING),
    isFuture(AssertionTypeGroup.TEMPORAL),
    isFutureOrPresent(AssertionTypeGroup.TEMPORAL),
    isPast(AssertionTypeGroup.TEMPORAL),
    isPastOrPresent(AssertionTypeGroup.TEMPORAL),
    isBefore(AssertionTypeGroup.TEMPORAL),
    isAfter(AssertionTypeGroup.TEMPORAL),
    isBeforeOrEqualTo(AssertionTypeGroup.TEMPORAL),
    isAfterOrEqualTo(AssertionTypeGroup.TEMPORAL),
    isNotEmpty(AssertionTypeGroup.STRING),
    isNotEmptyIterable(AssertionTypeGroup.ITERABLE),
    isNotBlank(AssertionTypeGroup.STRING),
    isValidEmail(AssertionTypeGroup.STRING),
    isNotNull(AssertionTypeGroup.OBJECT),
    isNull(AssertionTypeGroup.OBJECT),
    isInRange(AssertionTypeGroup.NUMERIC),
    notEquals(AssertionTypeGroup.OBJECT),
    regEx(AssertionTypeGroup.STRING),
    isPositiveOrZero(AssertionTypeGroup.NUMERIC),
    isNegativeOrZero(AssertionTypeGroup.NUMERIC),
    isPositive(AssertionTypeGroup.NUMERIC),
    isNegative(AssertionTypeGroup.NUMERIC),
    isGreaterThan(AssertionTypeGroup.NUMERIC),
    isGreaterOrEqual(AssertionTypeGroup.NUMERIC),
    isGreaterOrEqualNonDecimal(AssertionTypeGroup.NUMERIC),
    isLessThan(AssertionTypeGroup.NUMERIC),
    isLessOrEqual(AssertionTypeGroup.NUMERIC),
    isLessOrEqualNonDecimal(AssertionTypeGroup.NUMERIC);


    private final AssertionTypeGroup typeGroup;

    AssertionType(AssertionTypeGroup typeGroup) {
        this.typeGroup = typeGroup;
    }


}
