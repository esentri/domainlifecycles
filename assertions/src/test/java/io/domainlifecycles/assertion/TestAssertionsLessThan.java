package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsLessThan {

    @Nested
    class TestIsLessThan {

        @Test
        public void testIsLessThanBigIntegerOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(BigInteger.valueOf(1l), BigInteger.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsLessThanBigIntegerOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan((BigInteger) null, BigInteger.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsLessThanBigIntegerOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(BigInteger.valueOf(2l), (BigInteger) null, "Failed"));
        }

        @Test
        public void testIsLessThanBigIntegerFail() {
            assertThatThrownBy(() -> DomainAssertions.isLessThan(BigInteger.valueOf(2l), BigInteger.valueOf(2l),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanBytePrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isLessThan((byte) 1, (byte) 2, "Failed"));
        }

        @Test
        public void testIsLessThanBytePrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isLessThan((byte) 2, (byte) 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanShortPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isLessThan((short) 1, (short) 2, "Failed"));
        }

        @Test
        public void testIsLessThanShortPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isLessThan((short) 2, (short) 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanIntPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isLessThan(1, 2, "Failed"));
        }

        @Test
        public void testIsLessThanIntPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isLessThan(2, 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanLongPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isLessThan(1l, 2l, "Failed"));
        }

        @Test
        public void testIsLessThanLongPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isLessThan(2l, 2l, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanDoublePrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isLessThan(1.0, 2.0, "Failed"));
        }

        @Test
        public void testIsLessThanDoublePrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isLessThan(2.0, 2.0, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanFloatPrimitiveOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isLessThan(1.0f, 2.0f, "Failed"));
        }

        @Test
        public void testIsLessThanFloatPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isLessThan(2.0f, 2.0f, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanByteOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Byte.valueOf((byte) 1), Byte.valueOf((byte) 2), "Failed"));
        }

        @Test
        public void testIsLessThanByteOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan((Byte) null, Byte.valueOf((byte) 2), "Failed"));
        }

        @Test
        public void testIsLessThanByteOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Byte.valueOf((byte) 2), (Byte) null, "Failed"));
        }

        @Test
        public void testIsLessThanByteFail() {
            assertThatThrownBy(() -> DomainAssertions.isLessThan(Byte.valueOf((byte) 2), Byte.valueOf((byte) 2),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanShortOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Short.valueOf((short) 1), Short.valueOf((short) 2), "Failed"));
        }

        @Test
        public void testIsLessThanShortOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan((Short) null, Short.valueOf((short) 2), "Failed"));
        }

        @Test
        public void testIsLessThanShortOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Short.valueOf((short) 2), (Short) null, "Failed"));
        }

        @Test
        public void testIsLessThanShortFail() {
            assertThatThrownBy(() -> DomainAssertions.isLessThan(Short.valueOf((short) 2), Short.valueOf((short) 2),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanIntegerOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Integer.valueOf(1), Integer.valueOf(2), "Failed"));
        }

        @Test
        public void testIsLessThanIntegerOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan((Integer) null, Integer.valueOf(2), "Failed"));
        }

        @Test
        public void testIsLessThanIntegerOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Integer.valueOf(2), (Integer) null, "Failed"));
        }

        @Test
        public void testIsLessThanIntegerFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isLessThan(Integer.valueOf(2), Integer.valueOf(2), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanLongOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Long.valueOf(1l), Long.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsLessThanLongOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan((Long) null, Long.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsLessThanLongOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Long.valueOf(2l), (Long) null, "Failed"));
        }

        @Test
        public void testIsLessThanLongFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isLessThan(Long.valueOf(2l), Long.valueOf(2l), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanDoubleOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Double.valueOf(1.0), Double.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsLessThanDoubleOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan((Double) null, Double.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsLessThanDoubleOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Double.valueOf(2.0), (Double) null, "Failed"));
        }

        @Test
        public void testIsLessThanDoubleFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isLessThan(Double.valueOf(2.0), Double.valueOf(2.0), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanFloatOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Float.valueOf(1.0f), Float.valueOf(2.0f), "Failed"));
        }

        @Test
        public void testIsLessThanFloatOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan((Float) null, Float.valueOf(2.0f), "Failed"));
        }

        @Test
        public void testIsLessThanFloatOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(Float.valueOf(2.0f), (Float) null, "Failed"));
        }

        @Test
        public void testIsLessThanFloatFail() {
            assertThatThrownBy(
                () -> DomainAssertions.isLessThan(Float.valueOf(2.0f), Float.valueOf(2.0f), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsLessThanBigDecimalOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsLessThanBigDecimalOkFirstArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan((BigDecimal) null, BigDecimal.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsLessThanBigDecimalOkSecondArgNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isLessThan(BigDecimal.valueOf(2.0), (BigDecimal) null, "Failed"));
        }

        @Test
        public void testIsLessThanBigDecimalFail() {
            assertThatThrownBy(() -> DomainAssertions.isLessThan(BigDecimal.valueOf(2.0), BigDecimal.valueOf(2.0),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsLessThan {

        @Test
        public void testOptionalIsLessThanByteOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Byte.valueOf((byte) 1)), Byte.valueOf((byte) 2),
                    "Failed"));
        }

        @Test
        public void testOptionalIsLessThanByteOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.empty(), Byte.valueOf((byte) 2), "Failed"));
        }

        @Test
        public void testOptionalIsLessThanByteFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Byte.valueOf((byte) 2)), Byte.valueOf((byte) 2),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessThanByteFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(null, Byte.valueOf((byte) 2), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessThanShortOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Short.valueOf((short) 1)),
                    Short.valueOf((short) 2), "Failed"));
        }

        @Test
        public void testOptionalIsLessThanShortOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.empty(), Short.valueOf((short) 2), "Failed"));
        }

        @Test
        public void testOptionalIsLessThanShortFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsLessThan(Optional.of(Short.valueOf((short) 2)),
                Short.valueOf((short) 2), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessThanShortFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(null, Short.valueOf((short) 2), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessThanIntegerOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Integer.valueOf(1)), Integer.valueOf(2),
                    "Failed"));
        }

        @Test
        public void testOptionalIsLessThanIntegerOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.empty(), Integer.valueOf(2), "Failed"));
        }

        @Test
        public void testOptionalIsLessThanIntegerFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Integer.valueOf(2)), Integer.valueOf(2),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessThanIntegerFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(null, Integer.valueOf(2), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessThanLongOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Long.valueOf(1l)), Long.valueOf(2l), "Failed"));
        }

        @Test
        public void testOptionalIsLessThanLongOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.empty(), Long.valueOf(2l), "Failed"));
        }

        @Test
        public void testOptionalIsLessThanLongFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Long.valueOf(2l)), Long.valueOf(2l),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessThanLongFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(null, Long.valueOf(2l), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessThanDoubleOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Double.valueOf(1.0)), Double.valueOf(2.0),
                    "Failed"));
        }

        @Test
        public void testOptionalIsLessThanDoubleOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.empty(), Double.valueOf(2.0), "Failed"));
        }

        @Test
        public void testOptionalIsLessThanDoubleFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Double.valueOf(2.0)), Double.valueOf(2.0),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessThanDoubleFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(null, Double.valueOf(2.0), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessThanFloatOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Float.valueOf(1.0f)), Float.valueOf(2.0f),
                    "Failed"));
        }

        @Test
        public void testOptionalIsLessThanFloatOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.empty(), Float.valueOf(2.0f), "Failed"));
        }

        @Test
        public void testOptionalIsLessThanFloatFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(Float.valueOf(2.0f)), Float.valueOf(2.0f),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessThanFloatFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(null, Float.valueOf(2.0f), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessThanBigDecimalOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(BigDecimal.valueOf(1.0)), BigDecimal.valueOf(2.0),
                    "Failed"));
        }

        @Test
        public void testOptionalIsLessThanBigDecimalOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.empty(), BigDecimal.valueOf(2.0), "Failed"));
        }

        @Test
        public void testOptionalIsLessThanBigDecimalFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(BigDecimal.valueOf(2.0)), BigDecimal.valueOf(2.0),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessThanBigDecimalFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(null, BigDecimal.valueOf(2.0), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessThanBigIntegerOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(BigInteger.valueOf(1l)), BigInteger.valueOf(2l),
                    "Failed"));
        }

        @Test
        public void testOptionalIsLessThanBigIntegerOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.empty(), BigInteger.valueOf(2l), "Failed"));
        }

        @Test
        public void testOptionalIsLessThanBigIntegerFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(Optional.of(BigInteger.valueOf(2l)), BigInteger.valueOf(2l),
                    "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessThanBigIntegerFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsLessThan(null, BigInteger.valueOf(2l), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
