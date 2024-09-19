package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsPositiveOrZero {

    @Nested
    class TestIsPositiveOrZero {

        @ParameterizedTest
        @ValueSource(ints = { 0, 1, Integer.MAX_VALUE })
        void assertValidIntValue(int value) {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isPositiveOrZero(value, "message"));
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, Integer.MIN_VALUE })
        void assertInvalidIntValue(int value) {
            assertThatThrownBy(() -> DomainAssertions.isPositiveOrZero(value, "message")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroShortOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(Short.valueOf((short)2), "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroShortOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero((Short)null, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroShortFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(Short.valueOf((short)-1), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(Integer.valueOf(2), "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroIntegerOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero((Integer)null, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(Integer.valueOf(-1), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(Long.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroLongOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero((Long)null, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroLongFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(Long.valueOf(-1l), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(Double.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroDoubleOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero((Double)null, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroDoubleFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(Double.valueOf(-1.0), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(Float.valueOf(2.0f), "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroFloatOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero((Float)null, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroFloatFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(Float.valueOf(-1.0f), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(BigDecimal.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroBigDecimalOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero((BigDecimal)null, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroBigDecimalFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(BigDecimal.valueOf(-1.0), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroBigIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(BigInteger.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroBigIntegerOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero((BigInteger)null, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroBigIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(BigInteger.valueOf(-1l), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroBytePrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero((byte)2, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroBytePrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero((byte)-1, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroShortPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero((short)2, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroShortPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero((short)-1, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroIntPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(2, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroIntPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(-1, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroLongPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(2l, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroLongPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(-1l, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroDoublePrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(2.0, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroDoublePrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(-1.0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroFloatPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(2.0f, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroFloatPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(-1.0f, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveOrZeroByteOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero(Byte.valueOf((byte)2), "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroByteOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositiveOrZero((Byte)null, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroByteFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositiveOrZero(Byte.valueOf((byte)-1), "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsPositiveOrZero {

        @Test
        public void testOptionalIsPositiveOrZeroOkByte(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Byte.valueOf((byte)2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkShort(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Short.valueOf((short)2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkInteger(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Integer.valueOf(2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkLong(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Long.valueOf(2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkBigDecimal(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(BigDecimal.valueOf(2.0)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkBigInteger(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(BigInteger.valueOf(2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkDouble(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Double.valueOf(2.0)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkFloat(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Float.valueOf(2.0f)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Byte.valueOf((byte)2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Byte.valueOf((byte)-1)), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsPositiveOrZeroFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(null, "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsPositiveOrZeroFailWrongType(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(""), "Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
