package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAssertionsPositiveOrZero {

    @Nested
    class TestIsPositiveOrZero {

        @ParameterizedTest
        @ValueSource(ints = { 0, 1, Integer.MAX_VALUE })
        void assertValidIntValue(int value) {
            assertDoesNotThrow(() -> DomainAssertions.isPositiveOrZero(value, "message"));
        }

        @ParameterizedTest
        @ValueSource(ints = { -1, Integer.MIN_VALUE })
        void assertInvalidIntValue(int value) {
            assertThrows(DomainAssertionException.class, () -> DomainAssertions.isPositiveOrZero(value, "message"));
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
        public void testIsPositiveOrZeroBytePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero((byte)2, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroBytePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero((byte)-1, "Failed"));
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
        public void testIsPositiveOrZeroIntPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(2, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroIntPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(-1, "Failed"));
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
        public void testIsPositiveOrZeroDoublePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositiveOrZero(2.0, "Failed"));
        }

        @Test
        public void testIsPositiveOrZeroDoublePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositiveOrZero(-1.0, "Failed"));
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
    }

    @Nested
    class TestOptionalIsPositiveOrZero {

        @Test
        public void testOptionalIsPositiveOrZeroOkByte(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Byte.valueOf((byte)2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkShort(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Short.valueOf((short)2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkInteger(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Integer.valueOf(2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkLong(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Long.valueOf(2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkBigDecimal(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(BigDecimal.valueOf(2.0)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkBigInteger(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(BigInteger.valueOf(2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkDouble(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Double.valueOf(2.0)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOrZeroOkFloat(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositiveOrZero(Optional.of(Float.valueOf(2.0f)), "Failed"));
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
    }
}
