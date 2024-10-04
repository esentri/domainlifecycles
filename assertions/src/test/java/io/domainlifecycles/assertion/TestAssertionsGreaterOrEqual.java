package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsGreaterOrEqual {

    @Nested
    class TestIsGreaterOrEqual {

        @Test
        public void testIsGreaterOrEqualBytePrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isGreaterOrEqual((byte) 2, (byte) 2, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBytePrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual((byte) 1, (byte) 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualShortPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isGreaterOrEqual((short) 2, (short) 2, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualShortPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual((short) 1, (short) 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualIntPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isGreaterOrEqual(2, 2, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualIntPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual(1, 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualLongPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isGreaterOrEqual(2l, 2l, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualLongPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual(1l, 2l, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualDoublePrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isGreaterOrEqual(2.0, 2.0, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualDoublePrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual(1.0, 2.0, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualFloatPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isGreaterOrEqual(2.0f, 2.0f, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualFloatPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual(1.0f, 2.0f, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualByteOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Byte.valueOf((byte) 2), Byte.valueOf((byte) 2), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualByteOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual((Byte) null, Byte.valueOf((byte) 2), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualByteOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Byte.valueOf((byte) 2), (Byte) null, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualByteFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual(Byte.valueOf((byte) 1), Byte.valueOf((byte) 2),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualShortOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Short.valueOf((short) 2), Short.valueOf((short) 2), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualShortOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual((Short) null, Short.valueOf((short) 2), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualShortOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Short.valueOf((short) 2), (Short) null, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualShortFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Short.valueOf((short) 1), Short.valueOf((short) 2),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualIntegerOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Integer.valueOf(2), Integer.valueOf(2), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualIntegerOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual((Integer) null, Integer.valueOf(2), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualIntegerOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Integer.valueOf(2), (Integer) null, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualIntegerFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Integer.valueOf(1), Integer.valueOf(2), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualLongOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Long.valueOf(2l), Long.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualLongOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual((Long) null, Long.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualLongOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Long.valueOf(2l), (Long) null, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualLongFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Long.valueOf(1l), Long.valueOf(2l), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualDoubleOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Double.valueOf(2.0), Double.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualDoubleOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual((Double) null, Double.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualDoubleOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Double.valueOf(2.0), (Double) null, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualDoubleFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual(Double.valueOf(1.0), Double.valueOf(2.0),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualFloatOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Float.valueOf(2.0f), Float.valueOf(2.0f), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualFloatOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual((Float) null, Float.valueOf(2.0f), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualFloatOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(Float.valueOf(2.0f), (Float) null, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualFloatFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual(Float.valueOf(1.0f), Float.valueOf(2.0f),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualBigDecimalOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(BigDecimal.valueOf(2.0), BigDecimal.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigDecimalOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual((BigDecimal) null, BigDecimal.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigDecimalOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(BigDecimal.valueOf(2.0), (BigDecimal) null, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigDecimalFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterOrEqualBigIntegerOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(BigInteger.valueOf(2l), BigInteger.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigIntegerOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual((BigInteger) null, BigInteger.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigIntegerOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isGreaterOrEqual(BigInteger.valueOf(2l), (BigInteger) null, "Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigIntegerFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterOrEqual(BigInteger.valueOf(1l), BigInteger.valueOf(2l),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsGreaterOrEqual {

        @Test
        public void testOptionalIsGreaterOrEqualBigIntegerOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(BigInteger.valueOf(2l)),
                    BigInteger.valueOf(2l), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualBigIntegerOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), BigInteger.valueOf(2l), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualBigIntegerFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(BigInteger.valueOf(1l)),
                BigInteger.valueOf(2l), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualBigIntegerFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(null, BigInteger.valueOf(2l), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualByteOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Byte.valueOf((byte) 2)),
                    Byte.valueOf((byte) 2), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualByteOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Byte.valueOf((byte) 2), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualByteFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Byte.valueOf((byte) 1)),
                Byte.valueOf((byte) 2), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualByteFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(null, Byte.valueOf((byte) 2), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualShortOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Short.valueOf((short) 2)),
                    Short.valueOf((short) 2), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualShortOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Short.valueOf((short) 2), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualShortFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Short.valueOf((short) 1)),
                Short.valueOf((short) 2), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualShortFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(null, Short.valueOf((short) 2), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualIntegerOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Integer.valueOf(2)), Integer.valueOf(2),
                    "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualIntegerOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Integer.valueOf(2), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualIntegerFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Integer.valueOf(1)), Integer.valueOf(2),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualIntegerFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(null, Integer.valueOf(2), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualLongOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Long.valueOf(2l)), Long.valueOf(2l),
                    "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualLongOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Long.valueOf(2l), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualLongFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Long.valueOf(1l)), Long.valueOf(2l),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualLongFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(null, Long.valueOf(2l), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualDoubleOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Double.valueOf(2.0)), Double.valueOf(2.0),
                    "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualDoubleOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Double.valueOf(2.0), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualDoubleFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Double.valueOf(1.0)), Double.valueOf(2.0),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualDoubleFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(null, Double.valueOf(2.0), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualFloatOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Float.valueOf(2.0f)), Float.valueOf(2.0f),
                    "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualFloatOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), Float.valueOf(2.0f), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualFloatFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(Float.valueOf(1.0f)), Float.valueOf(2.0f),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualFloatFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(null, Float.valueOf(2.0f), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualBigDecimalOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(BigDecimal.valueOf(2.0)),
                    BigDecimal.valueOf(2.0), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualBigDecimalOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(Optional.empty(), BigDecimal.valueOf(2.0), "Failed"));
        }

        @Test
        public void testOptionalIsGreaterOrEqualBigDecimalFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsGreaterOrEqual(Optional.of(BigDecimal.valueOf(1.0)),
                BigDecimal.valueOf(2.0), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterOrEqualBigDecimalFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsGreaterOrEqual(null, BigDecimal.valueOf(2.0), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
