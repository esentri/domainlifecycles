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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsNegativeOrZero {

    @Nested
    class TestIsNegativeOrZero {

        @Test
        public void testIsNegativeOrZeroShortOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isNegativeOrZero(Short.valueOf((short) 0), "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroShortOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero((Short) null, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroShortFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isNegativeOrZero(Short.valueOf((short) 1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroIntegerOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero(Integer.valueOf(0), "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroIntegerOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero((Integer) null, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroIntegerFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(Integer.valueOf(1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroLongOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero(Long.valueOf(0l), "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroLongOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero((Long) null, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroLongFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(Long.valueOf(1l), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroDoubleOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero(Double.valueOf(0.0), "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroDoubleOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero((Double) null, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroDoubleFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(Double.valueOf(1.0), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroFloatOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero(Float.valueOf(0.0f), "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroFloatOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero((Float) null, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroFloatFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(Float.valueOf(1.0f), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroBigDecimalOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isNegativeOrZero(BigDecimal.valueOf(0.0), "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroBigDecimalOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero((BigDecimal) null, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroBigDecimalFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(BigDecimal.valueOf(1.0), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroBigIntegerOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isNegativeOrZero(BigInteger.valueOf(0l), "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroBigIntegerOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero((BigInteger) null, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroBigIntegerFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(BigInteger.valueOf(1l), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroBytePrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero((byte) 0, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroBytePrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero((byte) 1, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroShortPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero((short) 0, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroShortPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero((short) 1, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroIntPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero(0, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroIntPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(1, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroLongPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero(0l, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroLongPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(1l, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroDoublePrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero(0.0, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroDoublePrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(1.0, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroFloatPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero(0.0f, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroFloatPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(1.0f, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeOrZeroByteOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isNegativeOrZero(Byte.valueOf((byte) 0), "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroByteOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNegativeOrZero((Byte) null, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroByteFail() {
            assertThatThrownBy(() -> DomainAssertions.isNegativeOrZero(Byte.valueOf((byte) 1), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsNegativeOrZero {

        @Test
        public void testOptionalIsZeroOkByte() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Byte.valueOf((byte) 0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkShort() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Short.valueOf((short) 0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkInteger() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Integer.valueOf(0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkLong() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Long.valueOf(0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkBigDecimal() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(BigDecimal.valueOf(0.0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkBigInteger() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(BigInteger.valueOf(0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkDouble() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Double.valueOf(0.0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkFloat() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Float.valueOf((float) 0.0)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkByte() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Byte.valueOf((byte) -1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkShort() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Short.valueOf((short) -1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkInteger() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Integer.valueOf(-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkLong() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Long.valueOf(-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkBigDecimal() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(BigDecimal.valueOf(-1.0)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkBigInteger() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(BigInteger.valueOf(-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkDouble() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Double.valueOf(-1.0)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkFloat() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Float.valueOf((float) -1.0)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOrZeroOkEmtpy() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNegativeOrZero(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOrZeroFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Byte.valueOf((byte) 1)),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsNegativeOrZeroFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsNegativeOrZero(null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsNegativeOrZeroFailWrongType() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsNegativeOrZero(Optional.of(""), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
