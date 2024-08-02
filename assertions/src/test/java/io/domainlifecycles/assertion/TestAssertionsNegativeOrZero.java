package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsNegativeOrZero {

    @Nested
    class TestIsNegativeOrZero {

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
        public void testIsNegativeOrZeroBytePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero((byte)0, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroBytePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero((byte)1, "Failed"));
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
        public void testIsNegativeOrZeroIntPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(0, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroIntPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(1, "Failed"));
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
        public void testIsNegativeOrZeroDoublePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegativeOrZero(0.0, "Failed"));
        }

        @Test
        public void testIsNegativeOrZeroDoublePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegativeOrZero(1.0, "Failed"));
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
    }

    @Nested
    class TestOptionalIsNegativeOrZero {

        @Test
        public void testOptionalIsZeroOkByte(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Byte.valueOf((byte)0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkShort(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Short.valueOf((short)0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkInteger(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Integer.valueOf(0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkLong(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Long.valueOf(0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkBigDecimal(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(BigDecimal.valueOf(0.0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkBigInteger(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(BigInteger.valueOf(0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkDouble(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Double.valueOf(0.0)), "Failed"));
        }

        @Test
        public void testOptionalIsZeroOkFloat(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Float.valueOf((float)0.0)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkByte(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Byte.valueOf((byte)-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkShort(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Short.valueOf((short)-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkInteger(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Integer.valueOf(-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkLong(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Long.valueOf(-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkBigDecimal(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(BigDecimal.valueOf(-1.0)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkBigInteger(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(BigInteger.valueOf(-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkDouble(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Double.valueOf(-1.0)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkFloat(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegativeOrZero(Optional.of(Float.valueOf((float)-1.0)), "Failed"));
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
    }
}
