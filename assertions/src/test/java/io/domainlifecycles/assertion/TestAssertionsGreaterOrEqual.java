package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsGreaterOrEqual {

    @Nested
    class TestIsGreaterOrEqual {

        @Test
        public void testIsGreaterOrEqualBytePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((byte)2, (byte)2 ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBytePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual((byte)1, (byte)2 ,"Failed"));
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
        public void testIsGreaterOrEqualIntPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(2, 2 ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualIntPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(1, 2 ,"Failed"));
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
        public void testIsGreaterOrEqualDoublePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(2.0, 2.0 ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualDoublePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(1.0, 2.0 ,"Failed"));
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
        public void testIsGreaterOrEqualByteOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Byte.valueOf((byte)2), Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualByteOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Byte) null, Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualByteOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Byte.valueOf((byte)2), (Byte) null ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualByteFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Byte.valueOf((byte)1), Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualShortOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Short.valueOf((short)2), Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualShortOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Short)null, Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualShortOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Short.valueOf((short)2), (Short)null ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Short.valueOf((short)1), Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Integer.valueOf(2), Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualIntegerOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Integer)null, Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualIntegerOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Integer.valueOf(2), (Integer)null ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualIntegerFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Integer.valueOf(1), Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualLongOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Long.valueOf(2l), Long.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualLongOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Long)null, Long.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualLongOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Long.valueOf(2l), (Long)null ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Long.valueOf(1l), Long.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualDoubleOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Double.valueOf(2.0), Double.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualDoubleOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Double)null, Double.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualDoubleOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Double.valueOf(2.0), (Double)null,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualDoubleFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Double.valueOf(1.0), Double.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualFloatOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Float.valueOf(2.0f), Float.valueOf(2.0f) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualFloatOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((Float)null, Float.valueOf(2.0f) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualFloatOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(Float.valueOf(2.0f) ,(Float)null,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualFloatFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(Float.valueOf(1.0f), Float.valueOf(2.0f) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigDecimalOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(BigDecimal.valueOf(2.0), BigDecimal.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigDecimalOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((BigDecimal)null, BigDecimal.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigDecimalOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(BigDecimal.valueOf(2.0), (BigDecimal)null,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigDecimalFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(BigInteger.valueOf(2l), BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigIntegerOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual((BigInteger)null, BigInteger.valueOf(2l),"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigIntegerOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterOrEqual(BigInteger.valueOf(2l), (BigInteger)null,"Failed"));
        }

        @Test
        public void testIsGreaterOrEqualBigIntegerFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterOrEqual(BigInteger.valueOf(1l), BigInteger.valueOf(2l) ,"Failed"));
        }
    }

    @Nested
    class TestOptionalIsGreaterOrEqual {

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
    }
}
