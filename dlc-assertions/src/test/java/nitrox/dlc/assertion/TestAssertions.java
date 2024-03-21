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

package nitrox.dlc.assertion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class TestAssertions {

    @Test
    public void testObjectEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals("A", "B", "Failed"));
    }

    @Test
    public void testObjectEqualsNullFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals(null, "B", "Failed"));
    }

    @Test
    public void testObjectEqualsNullCompareFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals("A", null, "Failed"));
    }

    @Test
    public void testObjectEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.equals("A", "A", "Failed"));
    }

    @Test
    public void testObjectEqualsBothNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.equals(null, null, "Failed"));
    }

    @Test
    public void testOptionalObjectEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalEquals(Optional.of("A"), "B", "Failed"));
    }

    @Test
    public void testOptionalObjectEqualsEmptyFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalEquals(Optional.empty(), "B", "Failed"));
    }

    @Test
    public void testOptionalObjectEqualsNullFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalEquals(null, "B", "Failed"));
    }

    @Test
    public void testOptionalObjectEqualsEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalEquals(Optional.empty(), null, "Failed"));
    }

    @Test
    public void testOptionalObjectEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalEquals(Optional.of("A"), "A", "Failed"));
    }

    @Test
    public void testIntEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals(5, 6, "Failed"));
    }

    @Test
    public void testIntEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.equals(3, 3, "Failed"));
    }

    @Test
    public void testLongEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals(5l, 6l, "Failed"));
    }

    @Test
    public void testLongEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.equals(3l, 3l, "Failed"));
    }

    @Test
    public void testByteEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals((byte)5, (byte)6, "Failed"));
    }

    @Test
    public void testByteEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.equals((byte)3, (byte)3, "Failed"));
    }

    @Test
    public void testShortEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals((short)5, (short)6, "Failed"));
    }

    @Test
    public void testShortEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.equals((short)3, (short)3, "Failed"));
    }

    @Test
    public void testDoubleEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals(5.0, 6.0, "Failed"));
    }

    @Test
    public void testDoubleEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.equals(3.0, 3.0, "Failed"));
    }

    @Test
    public void testFloatEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals((float)5.0, (float)6.0, "Failed"));
    }

    @Test
    public void testFloatEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.equals((float)3.0, (float)3.0, "Failed"));
    }

    @Test
    public void testCharEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals('a', 'b', "Failed"));
    }

    @Test
    public void testCharEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.equals('x', 'x', "Failed"));
    }

    @Test
    public void testBooleanEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals(true, false, "Failed"));
    }

    @Test
    public void testBooleanEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.equals(false, false, "Failed"));
    }

    @Test
    public void testObjectNotEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals("A", "A", "Failed"));
    }

    @Test
    public void testObjectNotEqualsBothNullFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals(null, null, "Failed"));
    }

    @Test
    public void testObjectNotEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals("A", "A1", "Failed"));
    }

    @Test
    public void testObjectNotEqualsNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals(null, "A1", "Failed"));
    }

    @Test
    public void testObjectNotEqualsNullParameterOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals("A", null, "Failed"));
    }

    @Test
    public void testOptionalObjectNotEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalNotEquals(Optional.of("A"), "A", "Failed"));
    }

    @Test
    public void testOptionalObjectNotEqualsEmptyFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalNotEquals(Optional.empty(), null, "Failed"));
    }

    @Test
    public void testOptionalObjectNotEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalNotEquals(Optional.of("A"), "Y", "Failed"));
    }

    @Test
    public void testOptionalObjectNotEqualsEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalNotEquals(Optional.empty(), "Y", "Failed"));
    }

    @Test
    public void testOptionalObjectNotEqualsNullFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalNotEquals(null, "A", "Failed"));
    }

    @Test
    public void testIntNotEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals(5, 5, "Failed"));
    }

    @Test
    public void testIntNotEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals(4, 3, "Failed"));
    }

    @Test
    public void testLongNotNotEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals(8l, 8l, "Failed"));
    }

    @Test
    public void testLongNotEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals(2l, 3l, "Failed"));
    }

    @Test
    public void testByteNotEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals((byte)6, (byte)6, "Failed"));
    }

    @Test
    public void testByteNotEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals((byte)5, (byte)3, "Failed"));
    }

    @Test
    public void testShortNotEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals((short)6, (short)6, "Failed"));
    }

    @Test
    public void testShortNotEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals((short)6, (short)3, "Failed"));
    }

    @Test
    public void testDoubleNotEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals(6.0, 6.0, "Failed"));
    }

    @Test
    public void testDoubleNotEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals(5.0, 3.0, "Failed"));
    }

    @Test
    public void testFloatNotEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals((float)5.0, (float)5.0, "Failed"));
    }

    @Test
    public void testFloatNotEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals((float)3.0, (float)4.0, "Failed"));
    }

    @Test
    public void testCharNotEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals('a', 'a', "Failed"));
    }

    @Test
    public void testCharNotEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals('x', 'f', "Failed"));
    }

    @Test
    public void testBooleanNotEqualsFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals(true, true, "Failed"));
    }

    @Test
    public void testBooleanNotEqualsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals(false, true, "Failed"));
    }

    @Test
    public void testIsFalseFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFalse(true, "Failed"));
    }

    @Test
    public void testIsFalseOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFalse(false, "Failed"));
    }

    @Test
    public void testIsOneOfFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isOneOf("Z", List.of("A", "B", "C"), "Failed"));
    }

    @Test
    public void testIsOneOfOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isOneOf("A", List.of("A", "B", "C"), "Failed"));
    }

    @Test
    public void testIsOneOfNullCollectionFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.isOneOf("A", null, "Failed"));
    }

    @Test
    public void testIsOneOfNullObjectOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isOneOf(null, List.of("A", "B", "C"), "Failed"));
    }

    @Test
    public void testOptionalIsOneOfFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsOneOf(Optional.of("Z"), List.of("A", "B", "C"), "Failed"));
    }

    @Test
    public void testOptionalIsOneOfOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsOneOf(Optional.of("A"), List.of("A", "B", "C"), "Failed"));
    }

    @Test
    public void testOptionalIsOneOfNullCollectionFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsOneOf(Optional.of("A"), null, "Failed"));
    }

    @Test
    public void testIsOptionalOneOfNullObjectFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsOneOf(null, List.of("A", "B", "C"), "Failed"));
    }

    @Test
    public void testIsOptionalOneOfEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsOneOf(Optional.empty(), List.of("A", "B", "C"), "Failed"));
    }

    @Test
    public void testHasLengthMaxOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLengthMax("abc", 3, "Failed"));
    }

    @Test
    public void testHasLengthMaxFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasLengthMax("abc", 2, "Failed"));
    }

    @Test
    public void testHasLengthMaxNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLengthMax(null, 3, "Failed"));
    }

    @Test
    public void testOptionalHasLengthMaxOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLengthMax(Optional.of("abc"), 3, "Failed"));
    }

    @Test
    public void testOptionalHasLengthMaxFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasLengthMax(Optional.of("abc"), 2, "Failed"));
    }

    @Test
    public void testOptionalHasLengthMaxEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLengthMax(Optional.empty(), 3, "Failed"));
    }

    @Test
    public void testOptionalHasLengthMaxNullFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasLengthMax(null, 2, "Failed"));
    }

    @Test
    public void testHasLengthMinOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLengthMin("abc", 3, "Failed"));
    }

    @Test
    public void testHasLengthMinFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasLengthMin("a", 2, "Failed"));
    }

    @Test
    public void testHasLengthMinNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLengthMin(null, 3, "Failed"));
    }

    @Test
    public void testOptionalHasLengthMinOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLengthMin(Optional.of("abc"), 3, "Failed"));
    }

    @Test
    public void testOptionalHasLengthMinFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasLengthMin(Optional.of("a"), 2, "Failed"));
    }

    @Test
    public void testOptionalHasLengthMinEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLengthMin(Optional.empty(), 3, "Failed"));
    }

    @Test
    public void testOptionalHasLengthMinNullFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasLengthMin(null, 2, "Failed"));
    }

    @Test
    public void testHasLengthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLength("abc", 2, 3, "Failed"));
    }

    @Test
    public void testHasLengthShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasLength("a", 2, 3, "Failed"));
    }

    @Test
    public void testHasLengthLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasLength("abdbddb", 2, 3, "Failed"));
    }

    @Test
    public void testHasLengthNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLength(null, 2, 3, "Failed"));
    }

    @Test
    public void testOptionalHasLengthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLength(Optional.of("abc"), 2,3, "Failed"));
    }

    @Test
    public void testOptionalHasLengthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasLength(Optional.of("a"), 2,3, "Failed"));
    }

    @Test
    public void testOptionalHasLengthEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLength(Optional.empty(), 2,3, "Failed"));
    }

    @Test
    public void testOptionalHasLengthNullFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasLength(null, 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize(List.of(1,2,3), 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(List.of(1), 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(List.of("abdbddb",1,3,4,5,5), 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize((Collection) null, 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeMinOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin(List.of(1,2,3), 2, "Failed"));
    }

    @Test
    public void testHasSizeMinShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMin(List.of(1), 2,  "Failed"));
    }

    @Test
    public void testHasSizeMinNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin((Collection) null, 2,  "Failed"));
    }

    @Test
    public void testHasSizeMaxOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax(List.of(1,2,3), 3, "Failed"));
    }

    @Test
    public void testHasSizeMaxLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMax(List.of(1,45,6), 2,  "Failed"));
    }

    @Test
    public void testHasSizeMaxNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax((Collection) null, 2,  "Failed"));
    }

    @Test
    public void testHasSizeMapOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize(Map.of(1,2,3,4), 2, 3, "Failed"));
    }

    @Test
    public void testHasMapShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(Map.of(1,2), 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeMapLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(List.of("abdbddb",1,3,4,5,5), 1, 2, "Failed"));
    }

    @Test
    public void testHasSizeMapNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize((Map) null, 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeMapMinOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin(Map.of(1,2,3,4), 2, "Failed"));
    }

    @Test
    public void testHasSizeMapMinShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMin(Map.of(1,2), 2,  "Failed"));
    }

    @Test
    public void testHasSizeMapMinNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin((Map) null, 2,  "Failed"));
    }

    @Test
    public void testHasSizeMapMaxOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax(Map.of(1,2,3,2), 3, "Failed"));
    }

    @Test
    public void testHasSizeMapMaxLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMax(Map.of(1,45,6,6), 1,  "Failed"));
    }

    @Test
    public void testHasSizeMapMaxNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax((Map) null, 2,  "Failed"));
    }

    @Test
    public void testHasSizeArrayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize(new String[3], 2, 3, "Failed"));
    }

    @Test
    public void testHasArrayShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(new Long[1], 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeArrayLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(new Date[8], 1, 2, "Failed"));
    }

    @Test
    public void testHasSizeArrayNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize((Object[]) null, 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeArrayMinOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin(new Integer[2], 2, "Failed"));
    }

    @Test
    public void testHasSizeArrayMinShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMin(new Short[1], 2,  "Failed"));
    }

    @Test
    public void testHasSizeArrayMinNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin((String[]) null, 2,  "Failed"));
    }

    @Test
    public void testHasSizeArrayMaxOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax(new String[2], 3, "Failed"));
    }

    @Test
    public void testHasSizeArrayMaxLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMax(new String[2], 1,  "Failed"));
    }

    @Test
    public void testHasSizeArrayMaxNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax((String[]) null, 2,  "Failed"));
    }

    @Test
    public void testHasSizeOptionalArrayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSize(Optional.of(new String[3]), 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeOptionalArrayNullFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalArrayHasSize(null, 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeOptionalArrayEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSize(Optional.empty(), 2, 3, "Failed"));
    }

    @Test
    public void testHasSizeOptionalArrayMinOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSizeMin(Optional.of(new Integer[2]), 2, "Failed"));
    }

    @Test
    public void testHasSizeOptionalArrayMinNullFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalArrayHasSizeMin(null, 2,  "Failed"));
    }

    @Test
    public void testHasSizeOptionalArrayMinEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSizeMin(Optional.empty(), 2,  "Failed"));
    }

    @Test
    public void testHasSizeOptionalArrayMaxOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSizeMax(Optional.of(new String[2]), 3, "Failed"));
    }

    @Test
    public void testHasSizeArrayMaxNullFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalArrayHasSizeMax(null, 1,  "Failed"));
    }

    @Test
    public void testHasSizeArrayMaxEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSizeMax(Optional.empty(), 2,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsFailInteger(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(BigDecimal.valueOf(12.23), 1,2,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsFailFraction(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(BigDecimal.valueOf(12.234), 1,2,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits(BigDecimal.valueOf(12.234), 2, 3,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits((BigDecimal) null, 2, 3,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsFailInteger(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigits(Optional.of(BigDecimal.valueOf(12.23)), 1,2,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsFailFraction(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigits(Optional.of(BigDecimal.valueOf(12.234)), 1,2,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(BigDecimal.valueOf(12.234)), 2, 3,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigits(Optional.empty(), 2, 3,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigits(null, 1,2,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsFailWrongType(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigits(Optional.of("a"), 1,2,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(BigDecimal.valueOf(12.23), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(BigDecimal.valueOf(12.234), 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((BigDecimal) null, 2,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsFraction(BigDecimal.valueOf(12.23), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction(BigDecimal.valueOf(12.234), 3, "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction((BigDecimal) null, 2,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(BigDecimal.valueOf(12.23)), 1,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(BigDecimal.valueOf(12.234)), 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.empty(), 2,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsIntegerNullFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigitsInteger(null, 1,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsIntegerWrongTypeFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of("sdad"), 1,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsFractionFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(BigDecimal.valueOf(12.23)), 1,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsFractionOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(BigDecimal.valueOf(12.234)), 3, "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsFractionEmptyOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.empty(), 2,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsFractionNullFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigitsFraction(null, 1,  "Failed"));
    }

    @Test
    public void testOptionalHasMaxDigitsFractionWrongTypeFail(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of("sdfse"), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailBigInteger(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(BigInteger.valueOf(12), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkBigInteger(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(BigInteger.valueOf(12), 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerNullOkBigInteger(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((BigInteger) null, 2,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailDoublePrimitive(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(12.1, 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkDoublePrimitive(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(12.2, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsFailDoublePrimitive(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(12.1, 1,1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsOkDoublePrimitive(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits(12.2, 2,1, "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionFailDoublePrimitive(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsFraction(12.13, 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionOkDoublePrimitive(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction(12.2, 1, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailDouble(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Double.valueOf(12.1), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkDouble(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Double.valueOf(12.2), 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsFailDouble(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(Double.valueOf(12.1), 1,1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkDoubleNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Double)null, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsOkDouble(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits(Double.valueOf(12.2), 2,1, "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionFailDouble(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsFraction(Double.valueOf(12.13), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionOkDouble(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction(Double.valueOf(10.2), 1, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailFloatPrimitive(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(10.1f, 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkFloatPrimitive(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((float)10.2, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsFailFloatPrimitive(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(10.0f, 1,1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsOkFloatPrimitive(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits(10.5f, 2,1, "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionFailFloatPrimitive(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsFraction(12.12f, 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionOkFloatPrimitive(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction(10.1f, 1, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailFloat(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Float.valueOf(10.1f), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkFloat(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Float.valueOf(10.1f), 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsFailFloat(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(Float.valueOf(10.1f), 1,1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkFloatNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Float)null, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsOkFloat(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits(Float.valueOf(10.1f), 2,1, "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionFailFloat(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsFraction(Float.valueOf(10.25f), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsFractionOkFloat(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction(Float.valueOf(10.1f), 1, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailIntPrimitive(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(12, 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkIntPrimitive(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(12, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailInteger(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Integer.valueOf(12), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkInteger(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Integer.valueOf(12), 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkIntegerNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Integer)null, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailLongPrimitive(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(12l, 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkLongPrimitive(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(12l, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailLong(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Long.valueOf(12), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkLong(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Long.valueOf(12), 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkLongNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Long)null, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailShortPrimitive(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger((short)12, 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkShortPrimitive(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((short)12, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailShort(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Short.valueOf((short)12), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkShort(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Short.valueOf((short)12), 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkShortNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Short)null, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailBytePrimitive(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger((byte)12, 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkBytePrimitive(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((byte)12, 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerFailByte(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Byte.valueOf((byte)12), 1,  "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkByte(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Byte.valueOf((byte)12), 2, "Failed"));
    }

    @Test
    public void testHasMaxDigitsIntegerOkByteNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Byte)null, 2, "Failed"));
    }

    @Test
    public void testIsFutureLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(LocalDate.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(LocalDate.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureLocalDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((LocalDate) null, "Failed"));
    }

    @Test
    public void testIsFutureLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(LocalTime.now().minusHours(1), "Failed"));
    }

    @Test
    public void testIsFutureLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(LocalTime.now().plusHours(1), "Failed"));
    }

    @Test
    public void testIsFutureLocalTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((LocalTime) null, "Failed"));
    }

    @Test
    public void testOptionalIsFutureLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of(LocalTime.now().minusHours(1)), "Failed"));
    }

    @Test
    public void testOptionalIsFutureLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(LocalTime.now().plusHours(1)), "Failed"));
    }

    @Test
    public void testOptionalIsFutureLocalTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsFutureLocalTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFuture(null, "Failed"));
    }

    @Test
    public void testOptionalIsFutureLocalTimeFailWrongType(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of("String"), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(LocalDate.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentLocalDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((LocalDate) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(LocalDate.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(LocalTime.now().plusHours(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentLocalTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((LocalTime) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(LocalTime.now().minusHours(1), "Failed"));
    }

    @Test
    public void testOptionalIsFutureOrPresentLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalTime.now().minusHours(1)), "Failed"));
    }

    @Test
    public void testOptionalIsFutureOrPresentLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(LocalTime.now().plusHours(1)), "Failed"));
    }

    @Test
    public void testOptionalIsFutureOrPresentOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsFutureOrPresentFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(null, "Failed"));
    }

    @Test
    public void testOptionalIsFutureOrPresentFailWrongType(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of("String"), "Failed"));
    }

    @Test
    public void testIsPastLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(LocalDate.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsPastLocalDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((LocalDate) null, "Failed"));
    }

    @Test
    public void testIsPastLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(LocalDate.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsPastLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(LocalTime.now().minusHours(1), "Failed"));
    }

    @Test
    public void testIsPastLocalTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((LocalTime) null, "Failed"));
    }

    @Test
    public void testIsPastLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(LocalTime.now().plusHours(1), "Failed"));
    }

    @Test
    public void testOptionalIsPastFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsPast(Optional.of(LocalTime.now().plusHours(1)), "Failed"));
    }

    @Test
    public void testOptionalIsPastOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.of(LocalTime.now().minusHours(1)), "Failed"));
    }

    @Test
    public void testOptionalIsPastOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPast(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsPastFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPast(null, "Failed"));
    }

    @Test
    public void testOptionalIsPastFailWrongType(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPast(Optional.of("String"), "Failed"));
    }

    @Test
    public void testIsPastOrPresentLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(LocalDate.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentLocalDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((LocalDate) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(LocalDate.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(LocalTime.now().minusHours(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentLocalTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((LocalTime) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(LocalTime.now().plusHours(1), "Failed"));
    }

    @Test
    public void testOptionalIsPastOrPresentFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(LocalTime.now().plusHours(1)), "Failed"));
    }

    @Test
    public void testOptionalIsPastOrPresentOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.of(LocalTime.now().minusHours(1)), "Failed"));
    }

    @Test
    public void testOptionalIsPastOrPresentOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPastOrPresent(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsPastOrPresentFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPastOrPresent(null, "Failed"));
    }

    @Test
    public void testOptionalIsPastOrPresentFailWrongType(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPastOrPresent(Optional.of("String"), "Failed"));
    }

    @Test
    public void testIsBeforeLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(LocalDate.now().minusDays(1), LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsBeforeLocalDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((LocalDate) null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsBeforeLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsBeforeLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(LocalTime.now().minusHours(1), LocalTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeLocalTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((LocalDate) null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsBeforeLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(LocalDate.now().minusDays(1)), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalDateOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(LocalDate.now().plusDays(1)), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalDateFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(LocalTime.now().minusHours(1)), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(LocalTime.now().plusHours(1)), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, LocalTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsAfterLocalDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((LocalDate) null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsAfterLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(LocalDate.now().minusDays(1), LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsAfterLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterLocalTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((LocalDate) null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsAfterLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(LocalTime.now().minusHours(1), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(LocalDate.now().plusDays(1)), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalDateOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(LocalDate.now().minusDays(1)), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalDateFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(LocalTime.now().plusHours(1)), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(LocalTime.now().minusHours(1)), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, LocalTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(LocalDate.now().minusDays(1), LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToLocalDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(LocalTime.now().minusHours(1), LocalTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToLocalTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDate.now().minusDays(1)), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalDateOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDate.now().plusDays(1)), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalDateFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalTime.now().minusHours(1)), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalTime.now().plusHours(1)), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, LocalTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToLocalDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(LocalDate.now().minusDays(1), LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToLocalTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(LocalTime.now().minusHours(1), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDate.now().plusDays(1)), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalDateOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDate.now().minusDays(1)), LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalDateFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalDate.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalTime.now().plusHours(1)), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalTime.now().minusHours(1)), LocalTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalTime.now(), "Failed"));
    }

    @Test
    public void testIsFutureLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(LocalDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(LocalDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureLocalDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((LocalDateTime) null, "Failed"));
    }

    @Test
    public void testIsFutureInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(Instant.now().minusSeconds(1), "Failed"));
    }

    @Test
    public void testIsFutureInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(Instant.now().plusSeconds(1), "Failed"));
    }

    @Test
    public void testIsFutureInstantOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((Instant) null, "Failed"));
    }

    @Test
    public void testOptionalIsFutureFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of(Instant.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testOptionalIsFutureOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(Instant.now().plusSeconds(1)), "Failed"));
    }

    @Test
    public void testOptionalIsFutureOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsFutureFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFuture(null, "Failed"));
    }

    @Test
    public void testOptionalIsFutureFailWrongType(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of("String"), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(LocalDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentLocalDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((LocalDateTime) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(LocalDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(Instant.now().plusSeconds(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentInstantOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((Instant) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(Instant.now().minusSeconds(1), "Failed"));
    }

    @Test
    public void testOptionalIsFutureOrPresentFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Instant.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testOptionalIsFutureOrPresentOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFutureOrPresent(Optional.of(Instant.now().plusSeconds(1)), "Failed"));
    }


    @Test
    public void testIsPastLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(LocalDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsPastLocalDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((LocalDateTime) null, "Failed"));
    }

    @Test
    public void testIsPastLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(LocalDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsPastInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(Instant.now().minusSeconds(1), "Failed"));
    }

    @Test
    public void testIsPastInstantOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((Instant) null, "Failed"));
    }

    @Test
    public void testIsPastInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(Instant.now().plusSeconds(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(LocalDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentLocalDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((LocalDateTime) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(LocalDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(Instant.now().minusSeconds(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentInstantOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((Instant) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(Instant.now().plusSeconds(1), "Failed"));
    }


    @Test
    public void testIsBeforeLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeLocalDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((LocalDateTime) null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(LocalDateTime.now().plusDays(1), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(Instant.now().minusSeconds(1), Instant.now(), "Failed"));
    }

    @Test
    public void testIsBeforeInstantOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((LocalDateTime) null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(Instant.now().plusSeconds(1), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(LocalDateTime.now().minusDays(1)), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(LocalDateTime.now().plusDays(1)), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeLocalDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(Instant.now().minusSeconds(1)), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeInstantOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(Instant.now().plusSeconds(1)), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeInstantFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, Instant.now(), "Failed"));
    }

    @Test
    public void testIsAfterLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(LocalDateTime.now().plusDays(1), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterLocalDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((LocalDateTime) null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(Instant.now().plusSeconds(1), Instant.now(), "Failed"));
    }

    @Test
    public void testIsAfterInstantOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((LocalDateTime) null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(Instant.now().minusSeconds(1), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(LocalDateTime.now().plusDays(1)), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(LocalDateTime.now().minusDays(1)), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterLocalDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(Instant.now().plusSeconds(1)), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterInstantOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(Instant.now().minusSeconds(1)), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterInstantFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, Instant.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToLocalDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((LocalDateTime) null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(LocalDateTime.now().plusDays(1), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(Instant.now().minusSeconds(1), Instant.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToInstantOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((LocalDateTime) null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(Instant.now().plusSeconds(1), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDateTime.now().minusDays(1)), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(LocalDateTime.now().plusDays(1)), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToLocalDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Instant.now().minusSeconds(1)), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToInstantOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Instant.now().plusSeconds(1)), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToInstantFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, Instant.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(LocalDateTime.now().plusDays(1), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToLocalDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((LocalDateTime) null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(LocalDateTime.now().minusDays(1), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(Instant.now().plusSeconds(1), Instant.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToInstantOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((LocalDateTime) null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(Instant.now().minusSeconds(1), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDateTime.now().plusDays(1)), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDateTime.now().minusDays(1)), LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToLocalDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToInstantOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Instant.now().plusSeconds(1)), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToInstantOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToInstantFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Instant.now().minusSeconds(1)), Instant.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToInstantFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, Instant.now(), "Failed"));
    }

    @Test
    public void testIsFutureDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(Date.from(Instant.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsFutureDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(Date.from(Instant.now().plusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsFutureDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((Date) null, "Failed"));
    }

    @Test
    public void testIsFutureCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsFutureCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsFutureCalendarOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((Calendar) null, "Failed"));
    }

    @Test
    public void testOptionalCalendarIsFutureFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), "Failed"));
    }

    @Test
    public void testOptionalCalendarIsFutureOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(Date.from(Instant.now().plusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((Date) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(Date.from(Instant.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentCalendarOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((Calendar) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsPastDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(Date.from(Instant.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsPastDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((Date) null, "Failed"));
    }

    @Test
    public void testIsPastDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(Date.from(Instant.now().plusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsPastCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsPastCalendarOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((Calendar) null, "Failed"));
    }

    @Test
    public void testIsPastCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsPastOrPresentDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(Date.from(Instant.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsPastOrPresentDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((Date) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(Date.from(Instant.now().plusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsPastOrPresentCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsPastOrPresentCalendarOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((Calendar) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), "Failed"));
    }


    @Test
    public void testIsBeforeDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(Date.from(Instant.now().minusSeconds(1)), new Date(), "Failed"));
    }

    @Test
    public void testIsBeforeDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((Date) null, new Date(), "Failed"));
    }

    @Test
    public void testIsBeforeDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(Date.from(Instant.now().plusSeconds(1)), new Date(), "Failed"));
    }

    @Test
    public void testIsBeforeCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testIsBeforeCalendarOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((Date) null, new Date(), "Failed"));
    }

    @Test
    public void testIsBeforeCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(Date.from(Instant.now().minusSeconds(1))), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeDateOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(Date.from(Instant.now().plusSeconds(1))), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeDateFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeCalendarOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeCalendarFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testIsAfterDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(Date.from(Instant.now().plusSeconds(1)), new Date(), "Failed"));
    }

    @Test
    public void testIsAfterDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((Date) null, new Date(), "Failed"));
    }

    @Test
    public void testIsAfterDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(Date.from(Instant.now().minusSeconds(1)), new Date(), "Failed"));
    }

    @Test
    public void testIsAfterCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testIsAfterCalendarOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((Date) null, new Date(), "Failed"));
    }

    @Test
    public void testIsAfterCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(Date.from(Instant.now().plusSeconds(1))), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterDateOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(Date.from(Instant.now().minusSeconds(1))), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterDateFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterCalendarOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterCalendarFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(Date.from(Instant.now().minusSeconds(1)), new Date(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((Date) null, new Date(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(Date.from(Instant.now().plusSeconds(1)), new Date(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToCalendarOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((Date) null, new Date(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Date.from(Instant.now().minusSeconds(1))), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToDateOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Date.from(Instant.now().plusSeconds(1))), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToDateFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToCalendarOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToCalendarFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(Date.from(Instant.now().plusSeconds(1)), new Date(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToDateOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((Date) null, new Date(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(Date.from(Instant.now().minusSeconds(1)), new Date(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1)), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToCalendarOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((Date) null, new Date(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1)), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToDateOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Date.from(Instant.now().plusSeconds(1))), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToDateOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToDateFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Date.from(Instant.now().minusSeconds(1))), new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToDateFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, new Date(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToCalendarOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(GregorianCalendar.from(ZonedDateTime.now().plusSeconds(1))), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToCalendarOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToCalendarFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(GregorianCalendar.from(ZonedDateTime.now().minusSeconds(1))), new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToCalendarFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, new GregorianCalendar(), "Failed"));
    }

    @Test
    public void testIsFutureYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(Year.now().minusYears(1), "Failed"));
    }

    @Test
    public void testIsFutureYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(Year.now().plusYears(1), "Failed"));
    }

    @Test
    public void testIsFutureYearOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((Year) null, "Failed"));
    }

    @Test
    public void testIsFutureYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(YearMonth.now().minusMonths(1), "Failed"));
    }

    @Test
    public void testIsFutureYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(YearMonth.now().plusMonths(1), "Failed"));
    }

    @Test
    public void testIsFutureYearMonthOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((YearMonth) null, "Failed"));
    }

    @Test
    public void testOptionalYearMonthIsFutureFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsFuture(Optional.of(YearMonth.now().minusMonths(1)), "Failed"));
    }

    @Test
    public void testOptionalYearMonthIsFutureOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(YearMonth.now().plusMonths(1)), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(Year.now().plusYears(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentYearOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((Year) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(Year.now().minusYears(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(YearMonth.now().plusMonths(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentYearMonthOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((YearMonth) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(YearMonth.now().minusMonths(1), "Failed"));
    }

    @Test
    public void testIsPastYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(Year.now().minusYears(1), "Failed"));
    }

    @Test
    public void testIsPastYearOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((Year) null, "Failed"));
    }

    @Test
    public void testIsPastYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(Year.now().plusYears(1), "Failed"));
    }

    @Test
    public void testIsPastYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(YearMonth.now().minusMonths(1), "Failed"));
    }

    @Test
    public void testIsPastYearMonthOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((YearMonth) null, "Failed"));
    }

    @Test
    public void testIsPastYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(YearMonth.now().plusMonths(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(Year.now().minusYears(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentYearOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((Year) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(Year.now().plusYears(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(YearMonth.now().minusMonths(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentYearMonthOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((YearMonth) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(YearMonth.now().plusMonths(1), "Failed"));
    }


    @Test
    public void testIsBeforeYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(Year.now().minusYears(1), Year.now(), "Failed"));
    }

    @Test
    public void testIsBeforeYearOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((Year) null, Year.now(), "Failed"));
    }

    @Test
    public void testIsBeforeYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(Year.now().plusYears(1), Year.now(), "Failed"));
    }

    @Test
    public void testIsBeforeYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed"));
    }

    @Test
    public void testIsBeforeYearMonthOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((Year) null, Year.now(), "Failed"));
    }

    @Test
    public void testIsBeforeYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(Year.now().minusYears(1)), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeYearOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(Year.now().plusYears(1)), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeYearFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(YearMonth.now().minusMonths(1)), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeYearMonthOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(YearMonth.now().plusMonths(1)), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeYearMonthFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, YearMonth.now(), "Failed"));
    }

    @Test
    public void testIsAfterYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(Year.now().plusYears(1), Year.now(), "Failed"));
    }

    @Test
    public void testIsAfterYearOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((Year) null, Year.now(), "Failed"));
    }

    @Test
    public void testIsAfterYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(Year.now().minusYears(1), Year.now(), "Failed"));
    }

    @Test
    public void testIsAfterYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed"));
    }

    @Test
    public void testIsAfterYearMonthOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((Year) null, Year.now(), "Failed"));
    }

    @Test
    public void testIsAfterYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(Year.now().plusYears(1)), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterYearOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(Year.now().minusYears(1)), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterYearFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(YearMonth.now().plusMonths(1)), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterYearMonthOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(YearMonth.now().minusMonths(1)), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterYearMonthFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, YearMonth.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(Year.now().minusYears(1), Year.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToYearOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((Year) null, Year.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(Year.now().plusYears(1), Year.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToYearMonthOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((Year) null, Year.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Year.now().minusYears(1)), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToYearOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(Year.now().plusYears(1)), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToYearFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(YearMonth.now().minusMonths(1)), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToYearMonthOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(YearMonth.now().plusMonths(1)), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToYearMonthFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, YearMonth.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(Year.now().plusYears(1), Year.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToYearOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((Year) null, Year.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(Year.now().minusYears(1), Year.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(YearMonth.now().plusMonths(1), YearMonth.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToYearMonthOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((Year) null, Year.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(YearMonth.now().minusMonths(1), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToYearOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Year.now().plusYears(1)), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToYearOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToYearFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(Year.now().minusYears(1)), Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToYearFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, Year.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToYearMonthOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(YearMonth.now().plusMonths(1)), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToYearMonthOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToYearMonthFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(YearMonth.now().minusMonths(1)), YearMonth.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToYearMonthFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, YearMonth.now(), "Failed"));
    }

    @Test
    public void testIsFutureZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(ZonedDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(ZonedDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureZonedDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((ZonedDateTime) null, "Failed"));
    }

    @Test
    public void testIsFutureMonthDayFail(){
        if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)), "Failed"));
        }
    }

    @Test
    public void testIsFutureMonthDayOk() {
        if (!Month.from(LocalDate.now()).equals(Month.DECEMBER)) {
            Assertions.assertDoesNotThrow(() -> DomainAssertions.isFuture(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)), "Failed"));
        }
    }

    @Test
    public void testIsFutureMonthDayOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((MonthDay) null, "Failed"));
    }

    @Test
    public void testOptionalMonthDayIsFutureFail() {
        if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFuture(Optional.of(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS))), "Failed"));
        }
    }

    @Test
    public void testOptionalMonthDayIsFutureOk(){
        if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS))), "Failed"));
        }
    }

    @Test
    public void testIsFutureOrPresentZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(ZonedDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentZonedDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((ZonedDateTime) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(ZonedDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentMonthDayOk() {
        if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
            Assertions.assertDoesNotThrow(() -> DomainAssertions.isFutureOrPresent(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)), "Failed"));
        }
    }

    @Test
    public void testIsFutureOrPresentMonthDayOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((MonthDay) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentMonthDayFail() {
        if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isFutureOrPresent(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)), "Failed"));
        }
    }

    @Test
    public void testIsPastZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(ZonedDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsPastZonedDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((ZonedDateTime) null, "Failed"));
    }

    @Test
    public void testIsPastZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(ZonedDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsPastMonthDayOk() {
        if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
            Assertions.assertDoesNotThrow(() -> DomainAssertions.isPast(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)), "Failed"));
        }
    }

    @Test
    public void testIsPastMonthDayOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((MonthDay) null, "Failed"));
    }

    @Test
    public void testIsPastMonthDayFail() {
        if (!Month.from(LocalDate.now()).equals(Month.DECEMBER)) {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isPast(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)), "Failed"));
        }
    }

    @Test
    public void testIsPastOrPresentZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(ZonedDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentZonedDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((ZonedDateTime) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(ZonedDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentMonthDayOk() {
        if (!Month.from(LocalDate.now()).minus(1).equals(Month.JANUARY)) {
            Assertions.assertDoesNotThrow(() -> DomainAssertions.isPastOrPresent(MonthDay.from(LocalDate.now().minus(1, ChronoUnit.MONTHS)), "Failed"));
        }
    }

    @Test
    public void testIsPastOrPresentMonthDayOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((MonthDay) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentMonthDayFail() {
        if (!Month.from(LocalDate.now()).equals(Month.DECEMBER)) {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isPastOrPresent(MonthDay.from(LocalDate.now().plus(1, ChronoUnit.MONTHS)), "Failed"));
        }
    }


    @Test
    public void testIsBeforeZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeZonedDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeMonthDayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(MonthDay.of(4,5), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testIsBeforeMonthDayOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeMonthDayFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(MonthDay.of(6,5), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(ZonedDateTime.now().minusDays(1)), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeZonedDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(ZonedDateTime.now().plusDays(1)), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeZonedDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeMonthDayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(MonthDay.of(4,5)), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeMonthDayOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeMonthDayFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(MonthDay.of(6,5)), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeMonthDayFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testIsAfterZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterZonedDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterMonthDayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(MonthDay.of(6,5), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testIsAfterMonthDayOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterMonthDayFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(MonthDay.of(4,5), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsAfterZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(ZonedDateTime.now().plusDays(1)), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterZonedDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(ZonedDateTime.now().minusDays(1)), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterZonedDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterMonthDayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(MonthDay.of(6,5)), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsAfterMonthDayOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsAfterMonthDayFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(MonthDay.of(4,5)), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsAfterMonthDayFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToZonedDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToMonthDayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(MonthDay.of(4,5), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToMonthDayOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToMonthDayFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(MonthDay.of(6,5), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(ZonedDateTime.now().minusDays(1)), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToZonedDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(ZonedDateTime.now().plusDays(1)), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToZonedDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToMonthDayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(MonthDay.of(4,5)), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToMonthDayOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToMonthDayFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(MonthDay.of(6,5)), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToMonthDayFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(ZonedDateTime.now().plusDays(1), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToZonedDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(ZonedDateTime.now().minusDays(1), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToMonthDayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(MonthDay.of(6,5), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToMonthDayOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((ZonedDateTime) null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToMonthDayFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(MonthDay.of(4,5), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToZonedDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(ZonedDateTime.now().plusDays(1)), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToZonedDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToZonedDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(ZonedDateTime.now().minusDays(1)), ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToZonedDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, ZonedDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToMonthDayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(MonthDay.of(6,5)), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToMonthDayOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToMonthDayFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(MonthDay.of(4,5)), MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToMonthDayFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, MonthDay.of(5,5), "Failed"));
    }

    @Test
    public void testIsFutureOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(OffsetDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture(OffsetDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureOffsetDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((OffsetDateTime) null, "Failed"));
    }

    @Test
    public void testIsFutureOffsetTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFuture(OffsetTime.now().minusSeconds(1), "Failed"));
    }

    @Test
    public void testIsFutureOffsetTimeOk() {
        Assertions.assertDoesNotThrow(() -> DomainAssertions.isFuture(OffsetTime.now().plusSeconds(1), "Failed"));
    }

    @Test
    public void testIsFutureOffsetTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFuture((OffsetTime) null, "Failed"));
    }

    @Test
    public void testOptionalOffsetTimeIsFutureFail() {
        Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.optionalIsFuture(Optional.of(OffsetTime.now().minusSeconds(1)), "Failed"));
    }

    @Test
    public void testOptionalOffsetTimeIsFutureOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsFuture(Optional.of(OffsetTime.now().plusSeconds(1)), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent(OffsetDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentOffsetDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((OffsetDateTime) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFutureOrPresent(OffsetDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentOffsetTimeOk() {
        Assertions.assertDoesNotThrow(() -> DomainAssertions.isFutureOrPresent(OffsetTime.now().plusSeconds(1), "Failed"));
    }

    @Test
    public void testIsFutureOrPresentOffsetTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isFutureOrPresent((OffsetTime) null, "Failed"));
    }

    @Test
    public void testIsFutureOrPresentOffsetTimeFail() {
        Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isFutureOrPresent(OffsetTime.now().minusSeconds(1), "Failed"));
    }

    @Test
    public void testIsPastOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast(OffsetDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsPastOffsetDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((OffsetDateTime) null, "Failed"));
    }

    @Test
    public void testIsPastOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPast(OffsetDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsPastOffsetTimeOk() {
        Assertions.assertDoesNotThrow(() -> DomainAssertions.isPast(OffsetTime.now().minusSeconds(1), "Failed"));
    }

    @Test
    public void testIsPastOffsetTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPast((OffsetTime) null, "Failed"));
    }

    @Test
    public void testIsPastOffsetTimeFail() {
        Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isPast(OffsetTime.now().plusSeconds(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent(OffsetDateTime.now().minusDays(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentOffsetDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((OffsetDateTime) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPastOrPresent(OffsetDateTime.now().plusDays(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentOffsetTimeOk() {
        Assertions.assertDoesNotThrow(() -> DomainAssertions.isPastOrPresent(OffsetTime.now().minusSeconds(1), "Failed"));
    }

    @Test
    public void testIsPastOrPresentOffsetTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPastOrPresent((OffsetTime) null, "Failed"));
    }

    @Test
    public void testIsPastOrPresentOffsetTimeFail() {
        Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isPastOrPresent(OffsetTime.now().plusSeconds(1), "Failed"));
    }


    @Test
    public void testIsBeforeOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOffsetDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOffsetTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore(OffsetTime.of(4,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testIsBeforeOffsetTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBefore((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOffsetTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBefore(OffsetTime.of(6,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(OffsetDateTime.now().minusDays(1)), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOffsetDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(OffsetDateTime.now().plusDays(1)), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOffsetDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOffsetTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.of(OffsetTime.of(4,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOffsetTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBefore(Optional.empty(), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOffsetTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBefore(Optional.of(OffsetTime.of(6,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOffsetTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBefore(null, OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testIsAfterOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOffsetDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOffsetTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter(OffsetTime.of(6,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testIsAfterOffsetTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfter((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOffsetTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfter(OffsetTime.of(4,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(OffsetDateTime.now().plusDays(1)), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOffsetDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(OffsetDateTime.now().minusDays(1)), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOffsetDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOffsetTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.of(OffsetTime.of(6,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOffsetTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfter(Optional.empty(), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOffsetTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfter(Optional.of(OffsetTime.of(4,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOffsetTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfter(null, OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToOffsetDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToOffsetTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo(OffsetTime.of(4,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToOffsetTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isBeforeOrEqualTo((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsBeforeOrEqualToOffsetTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isBeforeOrEqualTo(OffsetTime.of(6,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetDateTime.now().minusDays(1)), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToOffsetDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetDateTime.now().plusDays(1)), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToOffsetDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToOffsetTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetTime.of(4,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToOffsetTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.empty(), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToOffsetTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(Optional.of(OffsetTime.of(6,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsBeforeOrEqualToOffsetTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsBeforeOrEqualTo(null, OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(OffsetDateTime.now().plusDays(1), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToOffsetDateTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToOffsetTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(OffsetTime.of(6,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToOffsetTimeOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((OffsetDateTime) null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testIsAfterOrEqualToOffsetTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(OffsetTime.of(4,5,0,0,ZoneOffset.UTC), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToOffsetDateTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetDateTime.now().plusDays(1)), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToOffsetDateTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToOffsetDateTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetDateTime.now().minusDays(1)), OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToOffsetDateTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, OffsetDateTime.now(), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToOffsetTimeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetTime.of(6,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToOffsetTimeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToOffsetTimeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(OffsetTime.of(4,5,0,0,ZoneOffset.UTC)), OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testOptionalIsAfterOrEqualToOffsetTimeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, OffsetTime.of(5,5,0,0,ZoneOffset.UTC), "Failed"));
    }

    @Test
    public void testIsNotEmptyStringOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotEmpty("String", "Failed"));
    }

    @Test
    public void testIsNotEmptyStringFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmpty("", "Failed"));
    }

    @Test
    public void testIsNotEmptyStringFailNull(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmpty(null, "Failed"));
    }

    @Test
    public void testOptionalIsNotEmptyStringOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNotEmpty(Optional.of("String"), "Failed"));
    }

    @Test
    public void testOptionalIsNotEmptyStringOkEmptyOptional(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNotEmpty(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsNotEmptyStringFailEmptyString(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsNotEmpty(Optional.of(""), "Failed"));
    }

    @Test
    public void testOptionalIsNotEmptyStringFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsNotEmpty(null, "Failed"));
    }

    @Test
    public void testIsValidEmailOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isValidEmail("a@b.de", "Failed"));
    }

    @Test
    public void testIsValidEmailFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isValidEmail("aaaa-at-be.de", "Failed"));
    }

    @Test
    public void testIsValidEmailOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isValidEmail(null, "Failed"));
    }

    @Test
    public void testOptionalIsValidEmailOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsValidEmail(Optional.of("c@d.com"), "Failed"));
    }

    @Test
    public void testOptionalIsValidEmailOkEmptyOptional(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsValidEmail(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsValidEmailFailEmptyString(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsValidEmail(Optional.of(""), "Failed"));
    }

    @Test
    public void testOptionalIsValidEmailFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsValidEmail(null, "Failed"));
    }

    @Test
    public void testIsNotBlankOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotBlank("a", "Failed"));
    }

    @Test
    public void testIsNotBlankFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotBlank(" ", "Failed"));
    }

    @Test
    public void testIsNotBlankFailNull(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotBlank(null, "Failed"));
    }

    @Test
    public void testOptionalIsNotBlankOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNotBlank(Optional.of("c"), "Failed"));
    }

    @Test
    public void testOptionalIsNotBlankOkEmptyOptional(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNotBlank(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsNotBlankFailEmptyString(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsNotBlank(Optional.of(""), "Failed"));
    }

    @Test
    public void testOptionalIsNotBlankFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsNotBlank(null, "Failed"));
    }

    @Test
    public void testIsNotEmptyIterableArrayOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotEmptyIterable(new String[1], "Failed"));
    }

    @Test
    public void testIsNotEmptyIterableArrayFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable(new String[0], "Failed"));
    }

    @Test
    public void testIsNotEmptyIterableArrayFailNull(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable((Object[])null, "Failed"));
    }

    @Test
    public void testIsNotEmptyIterableCollectionOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotEmptyIterable(List.of("a", "b"), "Failed"));
    }

    @Test
    public void testIsNotEmptyIterableCollectionFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable(List.of(), "Failed"));
    }

    @Test
    public void testIsNotEmptyIterableCollectionFailNull(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable((Collection)null, "Failed"));
    }

    @Test
    public void testIsNotEmptyIterableMapOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotEmptyIterable(Map.of("a", "b"), "Failed"));
    }

    @Test
    public void testIsNotEmptyIterableMapFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable(Map.of(), "Failed"));
    }

    @Test
    public void testIsNotEmptyIterableMapFailNull(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable((Map)null, "Failed"));
    }

    @Test
    public void testIsNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNull(null, "Failed"));
    }

    @Test
    public void testIsNullFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNull("", "Failed"));
    }

    @Test
    public void testIsNotNullOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotNull(1l, "Failed"));
    }

    @Test
    public void testIsNotNullFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotNull(null, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(1.0, 0.0,2.0,"Failed"));
    }

    @Test
    public void testIsInRangePrimitiveDoubleFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(2.01, 0.0,2.0, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveDoubleFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(-0.01, 0.0,2.0, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(1.0f, 0.0f,2.0f,"Failed"));
    }

    @Test
    public void testIsInRangePrimitiveFloatFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(2.01f, 0.0f,2.0f, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveFloatFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(-0.01f, 0.0f,2.0f, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveIntOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(1, 0,2,"Failed"));
    }

    @Test
    public void testIsInRangePrimitiveIntFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(3, 0,2, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveIntFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(-1, 0,2, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange((byte)1, (byte)0,(byte)2,"Failed"));
    }

    @Test
    public void testIsInRangePrimitiveByteFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange((byte)3, (byte)0,(byte)2, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveByteFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange((byte)-1, (byte)0,(byte)2, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange((short)1, (short)0,(short)2,"Failed"));
    }

    @Test
    public void testIsInRangePrimitiveShortFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange((short)3, (short)0,(short)2, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveShortFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange((short)-1, (short)0,(short)2, "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(1l, 0l ,2l ,"Failed"));
    }

    @Test
    public void testIsInRangePrimitiveLongFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(3l, 0l ,2l , "Failed"));
    }

    @Test
    public void testIsInRangePrimitiveLongFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(-1l, 0l ,2l , "Failed"));
    }

    @Test
    public void testIsInRangeLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(Long.valueOf(1l), 0l ,2l ,"Failed"));
    }

    @Test
    public void testIsInRangeLongOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(null, 0l ,2l ,"Failed"));
    }

    @Test
    public void testIsInRangeLongFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(Long.valueOf(3l), 0l ,2l , "Failed"));
    }

    @Test
    public void testIsInRangeLongFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(Long.valueOf(-1l), 0l ,2l , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Long.valueOf(1l)), 0l ,2l ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeLongOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Long)null), 0l ,2l ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeLongFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Long.valueOf(3l)), 0l ,2l , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeLongFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Long.valueOf(-1l)), 0l ,2l , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeLongFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Long>)null, 0l ,2l , "Failed"));
    }

    @Test
    public void testIsInRangeBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(BigDecimal.valueOf(1l), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsInRangeBigDecimalOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(null, BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsInRangeBigDecimalFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(BigDecimal.valueOf(3l), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed"));
    }

    @Test
    public void testIsInRangeBigDecimalFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(BigDecimal.valueOf(-1l), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(BigDecimal.valueOf(1l)), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeBigDecimalOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((BigDecimal)null), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeBigDecimalFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(BigDecimal.valueOf(3l)), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeBigDecimalFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(BigDecimal.valueOf(-1l)), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeBigDecimalFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<BigDecimal>)null, BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed"));
    }

    @Test
    public void testIsInRangeBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(BigInteger.valueOf(1l), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsInRangeBigIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(null, BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsInRangeBigIntegerFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(BigInteger.valueOf(3l), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed"));
    }

    @Test
    public void testIsInRangeBigIntegerFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(BigInteger.valueOf(-1l), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(BigInteger.valueOf(1l)), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeBigIntegerOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((BigInteger)null), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeBigIntegerFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(BigInteger.valueOf(3l)), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeBigIntegerFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(BigInteger.valueOf(-1l)), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeBigIntegerFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<BigInteger>)null, BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Double.valueOf(1l)), Double.valueOf(0l) ,Double.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeDoubleOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Double)null), Double.valueOf(0l) ,Double.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeDoubleFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Double.valueOf(3l)), Double.valueOf(0l) ,Double.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeDoubleFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Double.valueOf(-1l)), Double.valueOf(0l) ,Double.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeDoubleFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Double>)null, Double.valueOf(0l) ,Double.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Float.valueOf(1l)), Float.valueOf(0l) ,Float.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeFloatOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Float)null), Float.valueOf(0l) ,Float.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeFloatFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Float.valueOf(3l)), Float.valueOf(0l) ,Float.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeFloatFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Float.valueOf(-1l)), Float.valueOf(0l) ,Float.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeFloatFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Float>)null, Float.valueOf(0l) ,Float.valueOf(2l) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Integer.valueOf(1)), Integer.valueOf(0) ,Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeIntegerOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Integer)null), Integer.valueOf(0) ,Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeIntegerFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Integer.valueOf(3)), Integer.valueOf(0) ,Integer.valueOf(2) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeIntegerFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Integer.valueOf(-1)), Integer.valueOf(0) ,Integer.valueOf(2) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeIntegerFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Integer>)null, Integer.valueOf(0) ,Integer.valueOf(2) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Short.valueOf((short)1)), Short.valueOf((short)0) ,Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeShortOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Short)null), Short.valueOf((short)0) ,Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeShortFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Short.valueOf((short)3)), Short.valueOf((short)0) ,Short.valueOf((short)2) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeShortFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Short.valueOf((short)-1)), Short.valueOf((short)0) ,Short.valueOf((short)2) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeShortFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Short>)null, Short.valueOf((short)0) ,Short.valueOf((short)2) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Byte.valueOf((byte)1)), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeByteOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Byte)null), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsInRangeByteFailOver(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Byte.valueOf((byte)3)), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeByteFailUnder(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Byte.valueOf((byte)-1)), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) , "Failed"));
    }

    @Test
    public void testOptionalIsInRangeByteFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Byte>)null, Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) , "Failed"));
    }

    @Test
    public void testIsTrueFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isTrue(false, "Failed"));
    }

    @Test
    public void testIsTrueOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isTrue(true ,"Failed"));
    }

    @Test
    public void testRegExFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.regEx("a","[b]", "Failed"));
    }

    @Test
    public void testRegExOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.regEx("a", "[a]" ,"Failed"));
    }

    @Test
    public void testOptionalRegExFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalRegEx(Optional.of("a"),"[b]", "Failed"));
    }

    @Test
    public void testOptionalRegExOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalRegEx(Optional.of("a"), "[a]" ,"Failed"));
    }

    @Test
    public void testOptionalRegExOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalRegEx(Optional.empty(), "[a]" ,"Failed"));
    }

    @Test
    public void testOptionalRegExFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalRegEx(null,"[b]", "Failed"));
    }

    @Test
    public void testIsGreaterThanBytePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((byte)2, (byte)1 ,"Failed"));
    }

    @Test
    public void testIsGreaterThanBytePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan((byte)1, (byte)2 ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualBytePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((byte)2, (byte)2 ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualBytePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual((byte)1, (byte)2 ,"Failed"));
    }

    @Test
    public void testIsLessThanBytePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((byte)1, (byte)2 ,"Failed"));
    }

    @Test
    public void testIsLessThanBytePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan((byte)2, (byte)2 ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualBytePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((byte)2, (byte)2 ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualBytePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual((byte)3, (byte)2 ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroBytePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((byte)2, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroBytePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero((byte)-1, "Failed"));
    }

    @Test
    public void testIsPositiveBytePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((byte)2, "Failed"));
    }

    @Test
    public void testIsPositiveBytePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive((byte)-1, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroBytePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((byte)0, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroBytePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero((byte)1, "Failed"));
    }

    @Test
    public void testIsNegativeBytePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((byte)-1, "Failed"));
    }

    @Test
    public void testIsNegativeBytePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative((byte)0, "Failed"));
    }

    @Test
    public void testIsGreaterThanShortPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((short)2, (short)1 ,"Failed"));
    }

    @Test
    public void testIsGreaterThanShortPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan((short)1, (short)2 ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualShortPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((short)2, (short)2 ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualShortPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual((short)1, (short)2 ,"Failed"));
    }

    @Test
    public void testIsLessThanShortPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((short)1, (short)2 ,"Failed"));
    }

    @Test
    public void testIsLessThanShortPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan((short)2, (short)2 ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualShortPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((short)2, (short)2 ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualShortPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual((short)3, (short)2 ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroShortPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((short)2, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroShortPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero((short)-1, "Failed"));
    }

    @Test
    public void testIsPositiveShortPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((short)2, "Failed"));
    }

    @Test
    public void testIsPositiveShortPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive((short)-1, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroShortPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((short)0, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroShortPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero((short)1, "Failed"));
    }

    @Test
    public void testIsNegativeShortPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((short)-1, "Failed"));
    }

    @Test
    public void testIsNegativeShortPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative((short)0, "Failed"));
    }

    @Test
    public void testIsGreaterThanIntPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(2, 1 ,"Failed"));
    }

    @Test
    public void testIsGreaterThanIntPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(1, 2 ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualIntPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(2, 2 ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualIntPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(1, 2 ,"Failed"));
    }

    @Test
    public void testIsLessThanIntPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(1, 2 ,"Failed"));
    }

    @Test
    public void testIsLessThanIntPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(2, 2 ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualIntPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(2, 2 ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualIntPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(3, 2 ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroIntPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(2, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroIntPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(-1, "Failed"));
    }

    @Test
    public void testIsPositiveIntPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(2, "Failed"));
    }

    @Test
    public void testIsPositiveIntPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(-1, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroIntPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(0, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroIntPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(1, "Failed"));
    }

    @Test
    public void testIsNegativeIntPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(-1, "Failed"));
    }

    @Test
    public void testIsNegativeIntPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(0, "Failed"));
    }

    @Test
    public void testIsGreaterThanLongPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(2l, 1l ,"Failed"));
    }

    @Test
    public void testIsGreaterThanLongPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(1l, 2l ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualLongPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(2l, 2l ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualLongPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(1l, 2l ,"Failed"));
    }

    @Test
    public void testIsLessThanLongPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(1l, 2l ,"Failed"));
    }

    @Test
    public void testIsLessThanLongPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(2l, 2l ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualLongPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(2l, 2l ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualLongPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(3l, 2l ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroLongPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(2l, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroLongPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(-1l, "Failed"));
    }

    @Test
    public void testIsPositiveLongPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(2l, "Failed"));
    }

    @Test
    public void testIsPositiveLongPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(-1l, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroLongPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(0l, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroLongPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(1l, "Failed"));
    }

    @Test
    public void testIsNegativeLongPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(-1l, "Failed"));
    }

    @Test
    public void testIsNegativeLongPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(0l, "Failed"));
    }

    @Test
    public void testIsGreaterThanDoublePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(2.0, 1.0 ,"Failed"));
    }

    @Test
    public void testIsGreaterThanDoublePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(1.0, 2.0 ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualDoublePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(2.0, 2.0 ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualDoublePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(1.0, 2.0 ,"Failed"));
    }

    @Test
    public void testIsLessThanDoublePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(1.0, 2.0 ,"Failed"));
    }

    @Test
    public void testIsLessThanDoublePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(2.0, 2.0 ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualDoublePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(2.0, 2.0 ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualDoublePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(3.0, 2.0 ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroDoublePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(2.0, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroDoublePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(-1.0, "Failed"));
    }

    @Test
    public void testIsPositiveDoublePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(2.0, "Failed"));
    }

    @Test
    public void testIsPositiveDoublePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(-1.0, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroDoublePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(0.0, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroDoublePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(1.0, "Failed"));
    }

    @Test
    public void testIsNegativeDoublePrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(-1.0, "Failed"));
    }

    @Test
    public void testIsNegativeDoublePrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(0.0, "Failed"));
    }

    @Test
    public void testIsGreaterThanFloatPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(2.0f, 1.0f ,"Failed"));
    }

    @Test
    public void testIsGreaterThanFloatPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(1.0f, 2.0f ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualFloatPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(2.0f, 2.0f ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualFloatPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(1.0f, 2.0f ,"Failed"));
    }

    @Test
    public void testIsLessThanFloatPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(1.0f, 2.0f ,"Failed"));
    }

    @Test
    public void testIsLessThanFloatPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(2.0f, 2.0f ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualFloatPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(2.0f, 2.0f ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualFloatPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(3.0f, 2.0f ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroFloatPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(2.0f, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroFloatPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(-1.0f, "Failed"));
    }

    @Test
    public void testIsPositiveFloatPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(2.0f, "Failed"));
    }

    @Test
    public void testIsPositiveFloatPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(-1.0f, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroFloatPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(0.0f, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroFloatPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(1.0f, "Failed"));
    }

    @Test
    public void testIsNegativeFloatPrimitiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(-1.0f, "Failed"));
    }

    @Test
    public void testIsNegativeFloatPrimitiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(0.0f, "Failed"));
    }

    @Test
    public void testIsGreaterThanByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Byte.valueOf((byte)2), Byte.valueOf((byte)1) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanByteOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Byte)null, Byte.valueOf((byte)1) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Byte.valueOf((byte)1), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Byte.valueOf((byte)2), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualByteOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Byte)null, Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Byte.valueOf((byte)1), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testIsLessThanByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(Byte.valueOf((byte)1), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testIsLessThanByteOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((Byte)null, Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testIsLessThanByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(Byte.valueOf((byte)2), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(Byte.valueOf((byte)2), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualByteOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((Byte)null, Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(Byte.valueOf((byte)3), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(Byte.valueOf((byte)2), "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroByteOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((Byte)null, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(Byte.valueOf((byte)-1), "Failed"));
    }

    @Test
    public void testIsPositiveByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Byte.valueOf((byte)2), "Failed"));
    }

    @Test
    public void testIsPositiveByteOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Byte)null, "Failed"));
    }

    @Test
    public void testIsPositiveByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Byte.valueOf((byte)-1), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(Byte.valueOf((byte)0), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroByteOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((Byte)null, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(Byte.valueOf((byte)1), "Failed"));
    }

    @Test
    public void testIsNegativeByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Byte.valueOf((byte)-1), "Failed"));
    }

    @Test
    public void testIsNegativeByteOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Byte)null, "Failed"));
    }

    @Test
    public void testIsNegativeByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Byte.valueOf((byte)0), "Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Byte.valueOf((byte)2)), Byte.valueOf((byte)1) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanByteOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Byte.valueOf((byte)1) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Byte.valueOf((byte)1)), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanByteFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterThan(null, Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Byte.valueOf((byte)2)), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualByteOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Byte.valueOf((byte)1)), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualByteFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(null, Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.of(Byte.valueOf((byte)1)), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanByteOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.empty(), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessThan(Optional.of(Byte.valueOf((byte)2)), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanByteFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessThan(null, Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualByteOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Byte.valueOf((byte)2)), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualByteOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualByteFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Byte.valueOf((byte)3)), Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualByteFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessOrEqual(null, Byte.valueOf((byte)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsPositiveOrZeroOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Byte.valueOf((byte)2)), "Failed"));
    }

    @Test
    public void testOptionalIsPositiveOrZeroOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsPositiveOrZeroFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Byte.valueOf((byte)-1)), "Failed"));
    }

    @Test
    public void testOptionalIsPositiveOrZeroFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPositiveOrZero(null, "Failed"));
    }

    @Test
    public void testOptionalIsPositiveOrZeroFailWrongType(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(""), "Failed"));
    }

    @Test
    public void testOptionalIsPositiveOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositive(Optional.of(Byte.valueOf((byte)2)), "Failed"));
    }

    @Test
    public void testOptionalIsPositiveOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositive(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsPositiveFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsPositive(Optional.of(Byte.valueOf((byte)-1)), "Failed"));
    }

    @Test
    public void testOptionalIsPositiveFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPositive(null, "Failed"));
    }

    @Test
    public void testOptionalIsPositiveFailWrongType(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPositive(Optional.of(""), "Failed"));
    }

    @Test
    public void testOptionalIsNegativeOrZeroOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Byte.valueOf((byte)0)), "Failed"));
    }

    @Test
    public void testOptionalIsNegativeOrZeroOkEmtpy(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsNegativeOrZeroFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Byte.valueOf((byte)1)), "Failed"));
    }

    @Test
    public void testOptionalIsNegativeOrZeroFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsNegativeOrZero(null, "Failed"));
    }

    @Test
    public void testOptionalIsNegativeOrZeroFailWrongType(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(""), "Failed"));
    }

    @Test
    public void testOptionalIsNegativeOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegative(Optional.of(Byte.valueOf((byte)-1)), "Failed"));
    }

    @Test
    public void testOptionalIsNegativeOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegative(Optional.empty(), "Failed"));
    }

    @Test
    public void testOptionalIsNegativeFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsNegative(Optional.of(Byte.valueOf((byte)0)), "Failed"));
    }

    @Test
    public void testOptionalIsNegativeFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsNegative(null, "Failed"));
    }

    @Test
    public void testOptionalIsNegativeFailWrongType(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsNegative(Optional.of(""), "Failed"));
    }

    @Test
    public void testIsGreaterThanShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Short.valueOf((short)2), Short.valueOf((short)1) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanShortOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Short)null, Short.valueOf((short)1) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Short.valueOf((short)1), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Short.valueOf((short)2), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualShortOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Short)null, Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Short.valueOf((short)1), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsLessThanShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(Short.valueOf((short)1), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsLessThanShortOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((Short)null, Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsLessThanShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(Short.valueOf((short)2), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(Short.valueOf((short)2), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualShortOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((Short)null, Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(Short.valueOf((short)3), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(Short.valueOf((short)2), "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroShortOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((Short)null, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(Short.valueOf((short)-1), "Failed"));
    }

    @Test
    public void testIsPositiveShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Short.valueOf((short)2), "Failed"));
    }

    @Test
    public void testIsPositiveShortOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Short)null, "Failed"));
    }

    @Test
    public void testIsPositiveShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Short.valueOf((short)-1), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(Short.valueOf((short)0), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroShortOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((Short)null, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(Short.valueOf((short)1), "Failed"));
    }

    @Test
    public void testIsNegativeShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Short.valueOf((short)-1), "Failed"));
    }

    @Test
    public void testIsNegativeShortOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Short)null, "Failed"));
    }

    @Test
    public void testIsNegativeShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Short.valueOf((short)0), "Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Short.valueOf((short)2)), Short.valueOf((short)1) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanShortOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Short.valueOf((short)1) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Short.valueOf((short)1)), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanShortFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterThan(null, Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Short.valueOf((short)2)), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualShortOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Short.valueOf((short)1)), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualShortFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(null, Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.of(Short.valueOf((short)1)), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanShortOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.empty(), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessThan(Optional.of(Short.valueOf((short)2)), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanShortFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessThan(null, Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualShortOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Short.valueOf((short)2)), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualShortOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualShortFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Short.valueOf((short)3)), Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualShortFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessOrEqual(null, Short.valueOf((short)2) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Integer.valueOf(2), Integer.valueOf(1) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Integer)null, Integer.valueOf(1) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Integer.valueOf(1), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Integer.valueOf(2), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Integer)null, Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Integer.valueOf(1), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsLessThanIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(Integer.valueOf(1), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsLessThanIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((Integer)null, Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsLessThanIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(Integer.valueOf(2), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(Integer.valueOf(2), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((Integer)null, Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(Integer.valueOf(3), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(Integer.valueOf(2), "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((Integer)null, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(Integer.valueOf(-1), "Failed"));
    }

    @Test
    public void testIsPositiveIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Integer.valueOf(2), "Failed"));
    }

    @Test
    public void testIsPositiveIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Integer)null, "Failed"));
    }

    @Test
    public void testIsPositiveIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Integer.valueOf(-1), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(Integer.valueOf(0), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((Integer)null, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(Integer.valueOf(1), "Failed"));
    }

    @Test
    public void testIsNegativeIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Integer.valueOf(-1), "Failed"));
    }

    @Test
    public void testIsNegativeIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Integer)null, "Failed"));
    }

    @Test
    public void testIsNegativeIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Integer.valueOf(0), "Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Integer.valueOf(2)), Integer.valueOf(1) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanIntegerOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Integer.valueOf(1) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Integer.valueOf(1)), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanIntegerFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterThan(null, Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Integer.valueOf(2)), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualIntegerOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Integer.valueOf(1)), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualIntegerFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(null, Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.of(Integer.valueOf(1)), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanIntegerOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.empty(), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessThan(Optional.of(Integer.valueOf(2)), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanIntegerFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessThan(null, Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Integer.valueOf(2)), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualIntegerOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Integer.valueOf(3)), Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualIntegerFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessOrEqual(null, Integer.valueOf(2) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Long.valueOf(2l), Long.valueOf(1l) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanLongOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Long)null, Long.valueOf(1l) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Long.valueOf(1l), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Long.valueOf(2l), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualLongOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Long)null, Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Long.valueOf(1l), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessThanLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(Long.valueOf(1l), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessThanLongOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((Long)null, Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessThanLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(Long.valueOf(2l), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(Long.valueOf(2l), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualLongOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((Long)null, Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(Long.valueOf(3l), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(Long.valueOf(2l), "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroLongOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((Long)null, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(Long.valueOf(-1l), "Failed"));
    }

    @Test
    public void testIsPositiveLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Long.valueOf(2), "Failed"));
    }

    @Test
    public void testIsPositiveLongOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Long)null, "Failed"));
    }

    @Test
    public void testIsPositiveLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Long.valueOf(-1l), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(Long.valueOf(0l), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroLongOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((Long)null, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(Long.valueOf(1l), "Failed"));
    }

    @Test
    public void testIsNegativeLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Long.valueOf(-1l), "Failed"));
    }

    @Test
    public void testIsNegativeLongOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Long)null, "Failed"));
    }

    @Test
    public void testIsNegativeLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Long.valueOf(0l), "Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Long.valueOf(2l)), Long.valueOf(1l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanLongOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Long.valueOf(1l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Long.valueOf(1l)), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanLongFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterThan(null, Long.valueOf(2) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Long.valueOf(2l)), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualLongOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Long.valueOf(1l)), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualLongFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(null, Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.of(Long.valueOf(1l)), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanLongOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.empty(), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessThan(Optional.of(Long.valueOf(2l)), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanLongFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessThan(null, Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualLongOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Long.valueOf(2l)), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualLongOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualLongFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Long.valueOf(3l)), Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualLongFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessOrEqual(null, Long.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Double.valueOf(2.0), Double.valueOf(1.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanDoubleOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Double)null, Double.valueOf(1.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Double.valueOf(1.0), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Double.valueOf(2.0), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualDoubleOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Double)null, Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Double.valueOf(1.0), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessThanDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(Double.valueOf(1.0), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessThanDoubleOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((Double)null, Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessThanDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(Double.valueOf(2.0), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(Double.valueOf(2.0), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualDoubleOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((Double)null, Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(Double.valueOf(3.0), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(Double.valueOf(2.0), "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroDoubleOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((Double)null, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(Double.valueOf(-1.0), "Failed"));
    }

    @Test
    public void testIsPositiveDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Double.valueOf(2.0), "Failed"));
    }

    @Test
    public void testIsPositiveDoubleOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Double)null, "Failed"));
    }

    @Test
    public void testIsPositiveDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Double.valueOf(-1.0), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(Double.valueOf(0.0), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroDoubleOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((Double)null, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(Double.valueOf(1.0), "Failed"));
    }

    @Test
    public void testIsNegativeDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Double.valueOf(-1.0), "Failed"));
    }

    @Test
    public void testIsNegativeDoubleOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Double)null, "Failed"));
    }

    @Test
    public void testIsNegativeDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Double.valueOf(0.0), "Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Double.valueOf(2.0)), Double.valueOf(1.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanDoubleOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Double.valueOf(1.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Double.valueOf(1.0)), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanDoubleFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterThan(null, Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Double.valueOf(2.0)), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualDoubleOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Double.valueOf(1.0)), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualDoubleFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(null, Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.of(Double.valueOf(1.0)), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanDoubleOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.empty(), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessThan(Optional.of(Double.valueOf(2.0)), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanDoubleFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessThan(null, Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualDoubleOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Double.valueOf(2.0)), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualDoubleOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualDoubleFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Double.valueOf(3.0)), Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualDoubleFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessOrEqual(null, Double.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Float.valueOf(2.0f), Float.valueOf(1.0f) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanFloatOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Float)null, Float.valueOf(1.0f) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Float.valueOf(1.0f), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Float.valueOf(2.0f), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualFloatOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Float)null, Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Float.valueOf(1.0f), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsLessThanFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(Float.valueOf(1.0f), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsLessThanFloatOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((Float)null, Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsLessThanFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(Float.valueOf(2.0f), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(Float.valueOf(2.0f), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualFloatOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((Float)null, Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(Float.valueOf(3.0f), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(Float.valueOf(2.0f), "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroFloatOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((Float)null, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(Float.valueOf(-1.0f), "Failed"));
    }

    @Test
    public void testIsPositiveFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Float.valueOf(2.0f), "Failed"));
    }

    @Test
    public void testIsPositiveFloatOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Float)null, "Failed"));
    }

    @Test
    public void testIsPositiveFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Float.valueOf(-1.0f), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(Float.valueOf(0.0f), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroFloatOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((Float)null, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(Float.valueOf(1.0f), "Failed"));
    }

    @Test
    public void testIsNegativeFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Float.valueOf(-1.0f), "Failed"));
    }

    @Test
    public void testIsNegativeFloatOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Float)null, "Failed"));
    }

    @Test
    public void testIsNegativeFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Float.valueOf(0.0f), "Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Float.valueOf(2.0f)), Float.valueOf(1.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanFloatOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Float.valueOf(1.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Float.valueOf(1.0f)), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanFloatFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterThan(null, Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Float.valueOf(2.0f)), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualFloatOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Float.valueOf(1.0f)), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualFloatFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(null, Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.of(Float.valueOf(1.0f)), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanFloatOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.empty(), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessThan(Optional.of(Float.valueOf(2.0f)), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanFloatFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessThan(null, Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualFloatOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Float.valueOf(2.0f)), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualFloatOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualFloatFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Float.valueOf(3.0f)), Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualFloatFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessOrEqual(null, Float.valueOf(2.0f) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(BigDecimal.valueOf(2.0), BigDecimal.valueOf(1.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanBigDecimalOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((BigDecimal)null, BigDecimal.valueOf(1.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(BigDecimal.valueOf(2.0), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualBigDecimalOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((BigDecimal)null, BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessThanBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessThanBigDecimalOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((BigDecimal)null, BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessThanBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(BigDecimal.valueOf(2.0), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(BigDecimal.valueOf(2.0), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualBigDecimalOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((BigDecimal)null, BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(BigDecimal.valueOf(3.0), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(BigDecimal.valueOf(2.0), "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroBigDecimalOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((BigDecimal)null, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(BigDecimal.valueOf(-1.0), "Failed"));
    }

    @Test
    public void testIsPositiveBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(BigDecimal.valueOf(2.0), "Failed"));
    }

    @Test
    public void testIsPositiveBigDecimalOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((BigDecimal)null, "Failed"));
    }

    @Test
    public void testIsPositiveBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(BigDecimal.valueOf(-1.0), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(BigDecimal.valueOf(0.0), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroBigDecimalOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((BigDecimal)null, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(BigDecimal.valueOf(1.0), "Failed"));
    }

    @Test
    public void testIsNegativeBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(BigDecimal.valueOf(-1.0), "Failed"));
    }

    @Test
    public void testIsNegativeBigDecimalOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((BigDecimal)null, "Failed"));
    }

    @Test
    public void testIsNegativeBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(BigDecimal.valueOf(0.0), "Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(BigDecimal.valueOf(2.0)), BigDecimal.valueOf(1.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanBigDecimalOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), BigDecimal.valueOf(1.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterThan(Optional.of(BigDecimal.valueOf(1.0)), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanBigDecimalFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterThan(null, BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(BigDecimal.valueOf(2.0)), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualBigDecimalOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(BigDecimal.valueOf(1.0)), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualBigDecimalFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(null, BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.of(BigDecimal.valueOf(1.0)), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanBigDecimalOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.empty(), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessThan(Optional.of(BigDecimal.valueOf(2.0)), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanBigDecimalFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessThan(null, BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualBigDecimalOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(BigDecimal.valueOf(2.0)), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualBigDecimalOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualBigDecimalFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(BigDecimal.valueOf(3.0)), BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualBigDecimalFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessOrEqual(null, BigDecimal.valueOf(2.0) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(BigInteger.valueOf(2l), BigInteger.valueOf(1l) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanBigIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((BigInteger)null, BigInteger.valueOf(1l) ,"Failed"));
    }

    @Test
    public void testIsGreaterThanBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(BigInteger.valueOf(1l), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(BigInteger.valueOf(2l), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualBigIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((BigInteger)null, BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsGreaterOrEqualBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(BigInteger.valueOf(1l), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessThanBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(BigInteger.valueOf(1l), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessThanBigIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((BigInteger)null, BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessThanBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(BigInteger.valueOf(2l), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(BigInteger.valueOf(2l), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualBigIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((BigInteger)null, BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsLessOrEqualBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(BigInteger.valueOf(3l), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testIsPositiveOrZeroBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(BigInteger.valueOf(2l), "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroBigIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((BigInteger)null, "Failed"));
    }

    @Test
    public void testIsPositiveOrZeroBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(BigInteger.valueOf(-1l), "Failed"));
    }

    @Test
    public void testIsPositiveBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(BigInteger.valueOf(2l), "Failed"));
    }

    @Test
    public void testIsPositiveBigIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((BigInteger)null, "Failed"));
    }

    @Test
    public void testIsPositiveBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(BigInteger.valueOf(-1l), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(BigInteger.valueOf(0l), "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroBigIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((BigInteger)null, "Failed"));
    }

    @Test
    public void testIsNegativeOrZeroBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(BigInteger.valueOf(1l), "Failed"));
    }

    @Test
    public void testIsNegativeBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(BigInteger.valueOf(-1l), "Failed"));
    }

    @Test
    public void testIsNegativeBigIntegerOkNull(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((BigInteger)null, "Failed"));
    }

    @Test
    public void testIsNegativeBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(BigInteger.valueOf(0l), "Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(BigInteger.valueOf(2l)), BigInteger.valueOf(1l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanBigIntegerOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), BigInteger.valueOf(1l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterThan(Optional.of(BigInteger.valueOf(1l)), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterThanBigIntegerFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterThan(null, BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(BigInteger.valueOf(2l)), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualBigIntegerOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(BigInteger.valueOf(1l)), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsGreaterOrEqualBigIntegerFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsGreaterOrEqual(null, BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.of(BigInteger.valueOf(1l)), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanBigIntegerOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessThan(Optional.empty(), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessThan(Optional.of(BigInteger.valueOf(2l)), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessThanBigIntegerFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessThan(null, BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualBigIntegerOk(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(BigInteger.valueOf(2l)), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualBigIntegerOkEmpty(){
        Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualBigIntegerFail(){
        Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(BigInteger.valueOf(3l)), BigInteger.valueOf(2l) ,"Failed"));
    }

    @Test
    public void testOptionalIsLessOrEqualBigIntegerFailNull(){
        Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsLessOrEqual(null, BigInteger.valueOf(2l) ,"Failed"));
    }
}
