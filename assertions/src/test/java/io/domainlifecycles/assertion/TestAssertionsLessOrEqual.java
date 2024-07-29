package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsLessOrEqual {

    @Nested
    class TestIsLessOrEqual {
        @Test
        public void testIsLessOrEqualBytePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual((byte)2, (byte)2 ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualBytePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual((byte)3, (byte)2 ,"Failed"));
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
        public void testIsLessOrEqualIntPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(2, 2 ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualIntPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(3, 2 ,"Failed"));
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
        public void testIsLessOrEqualDoublePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessOrEqual(2.0, 2.0 ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualDoublePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessOrEqual(3.0, 2.0 ,"Failed"));
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
    }

    @Nested
    class TestOptionalIsLessOrEqual {

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
    }
}
