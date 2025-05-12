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

package io.domainlifecycles.mirror.model;


/**
 * Assertions that can be used.
 *
 * @author Mario Herb
 */
public enum AssertionType {
    hasSize(AssertionTypeGroup.ITERABLE), /* Checks if the iterable has a specific size. */
    hasSizeMax(AssertionTypeGroup.ITERABLE), /* Checks if the iterable has a size less than or equal to a specified maximum. */
    hasSizeMin(AssertionTypeGroup.ITERABLE), /* Checks if the iterable has a size greater than or equal to a specified minimum. */
    hasMaxDigits(AssertionTypeGroup.NUMERIC_FRACTIONAL), /* Checks if the numeric value has a maximum number of digits (integer + fractional). */
    hasMaxDigitsInteger(AssertionTypeGroup.NUMERIC), /* Checks if the numeric value has a maximum number of integer digits. */
    hasMaxDigitsFraction(AssertionTypeGroup.NUMERIC_FRACTIONAL), /* Checks if the numeric value has a maximum number of fractional digits. */
    equals(AssertionTypeGroup.OBJECT), /* Checks if the object is equal to another object. */
    isFalse(AssertionTypeGroup.BOOLEAN), /* Checks if the value is false. */
    isTrue(AssertionTypeGroup.BOOLEAN), /* Checks if the value is true. */
    isOneOf(AssertionTypeGroup.OBJECT), /* Checks if the object is one of a specified set of values. */
    hasLength(AssertionTypeGroup.STRING), /* Checks if the string has a specific length. */
    hasLengthMax(AssertionTypeGroup.STRING), /* Checks if the string's length is less than or equal to a specified maximum. */
    hasLengthMin(AssertionTypeGroup.STRING), /* Checks if the string's length is greater than or equal to a specified minimum. */
    isFuture(AssertionTypeGroup.TEMPORAL), /* Checks if the temporal value is in the future. */
    isFutureOrPresent(AssertionTypeGroup.TEMPORAL), /* Checks if the temporal value is in the future or present. */
    isPast(AssertionTypeGroup.TEMPORAL), /* Checks if the temporal value is in the past. */
    isPastOrPresent(AssertionTypeGroup.TEMPORAL), /* Checks if the temporal value is in the past or present. */
    isBefore(AssertionTypeGroup.TEMPORAL), /* Checks if the temporal value is before a specified date/time. */
    isAfter(AssertionTypeGroup.TEMPORAL), /* Checks if the temporal value is after a specified date/time. */
    isBeforeOrEqualTo(AssertionTypeGroup.TEMPORAL), /* Checks if the temporal value is before or equal to a specified date/time. */
    isAfterOrEqualTo(AssertionTypeGroup.TEMPORAL), /* Checks if the temporal value is after or equal to a specified date/time. */
    isNotEmpty(AssertionTypeGroup.STRING), /* Checks if the string is not empty. */
    isNotEmptyIterable(AssertionTypeGroup.ITERABLE), /* Checks if the iterable is not empty. */
    isNotBlank(AssertionTypeGroup.STRING), /* Checks if the string is not blank. */
    isValidEmail(AssertionTypeGroup.STRING), /* Checks if the string is a valid email address. */
    isNotNull(AssertionTypeGroup.OBJECT), /* Checks if the object is not null. */
    isNull(AssertionTypeGroup.OBJECT), /* Checks if the object is null. */
    isInRange(AssertionTypeGroup.NUMERIC), /* Checks if the numeric value is within a specified range. */
    notEquals(AssertionTypeGroup.OBJECT), /* Checks if the object is not equal to another object. */
    regEx(AssertionTypeGroup.STRING), /* Checks if the string matches a specified regular expression. */
    isPositiveOrZero(AssertionTypeGroup.NUMERIC), /* Checks if the numeric value is positive or zero. */
    isNegativeOrZero(AssertionTypeGroup.NUMERIC), /* Checks if the numeric value is negative or zero. */
    isPositive(AssertionTypeGroup.NUMERIC), /* Checks if the numeric value is positive. */
    isNegative(AssertionTypeGroup.NUMERIC), /* Checks if the numeric value is negative. */
    isGreaterThan(AssertionTypeGroup.NUMERIC), /* Checks if the numeric value is greater than a specified value. */
    isGreaterOrEqual(AssertionTypeGroup.NUMERIC), /* Checks if the numeric value is greater than or equal to a specified value. */
    isGreaterOrEqualNonDecimal(AssertionTypeGroup.NUMERIC), /* Checks if the non-decimal numeric value is greater than or equal to a specified value. */
    isLessThan(AssertionTypeGroup.NUMERIC), /* Checks if the numeric value is less than a specified value. */
    isLessOrEqual(AssertionTypeGroup.NUMERIC), /* Checks if the numeric value is less than or equal to a specified value. */
    isLessOrEqualNonDecimal(AssertionTypeGroup.NUMERIC); /* Checks if the non-decimal numeric value is less than or equal to a specified value. */


    private final AssertionTypeGroup typeGroup;

    AssertionType(AssertionTypeGroup typeGroup) {
        this.typeGroup = typeGroup;
    }


}
