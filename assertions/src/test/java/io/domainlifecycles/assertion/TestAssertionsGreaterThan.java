package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsGreaterThan {

    @Nested
    class TestIsGreaterThan {

        @Test
        public void testIsGreaterThanBigIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(BigInteger.valueOf(2l), BigInteger.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigIntegerOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((BigInteger)null, BigInteger.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigIntegerOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(BigInteger.valueOf(1l), (BigInteger)null,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigIntegerFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(BigInteger.valueOf(1l), BigInteger.valueOf(2l) ,"Failed"));
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
        public void testIsGreaterThanShortPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((short)2, (short)1 ,"Failed"));
        }

        @Test
        public void testIsGreaterThanShortPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan((short)1, (short)2 ,"Failed"));
        }

        @Test
        public void testIsGreaterThanIntPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(2, 1 ,"Failed"));
        }

        @Test
        public void testIsGreaterThanIntPrimitiveFail() {
            Assertions.assertThrows(DomainAssertionException.class, () -> DomainAssertions.isGreaterThan(1, 2, "Failed"));
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
        public void testIsGreaterThanDoublePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(2.0, 1.0 ,"Failed"));
        }

        @Test
        public void testIsGreaterThanDoublePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(1.0, 2.0 ,"Failed"));
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
        public void testIsGreaterThanByteOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Byte.valueOf((byte)2), Byte.valueOf((byte)1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanByteOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Byte)null, Byte.valueOf((byte)1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanByteOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Byte.valueOf((byte)1), (Byte)null ,"Failed"));
        }

        @Test
        public void testIsGreaterThanByteFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Byte.valueOf((byte)1), Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanShortOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Short.valueOf((short)2), Short.valueOf((short)1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanShortOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Short)null, Short.valueOf((short)1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanShortOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Short.valueOf((short)1), (Short)null,"Failed"));
        }

        @Test
        public void testIsGreaterThanShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Short.valueOf((short)1), Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Integer.valueOf(2), Integer.valueOf(1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanIntegerOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Integer)null, Integer.valueOf(1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanIntegerOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Integer.valueOf(1),  (Integer)null,"Failed"));
        }

        @Test
        public void testIsGreaterThanIntegerFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Integer.valueOf(1), Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanLongOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Long.valueOf(2l), Long.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanLongOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Long)null, Long.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanLongOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Long.valueOf(1l) , (Long)null,"Failed"));
        }

        @Test
        public void testIsGreaterThanLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Long.valueOf(1l), Long.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanDoubleOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Double.valueOf(2.0), Double.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanDoubleOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Double)null, Double.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanDoubleOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Double.valueOf(1.0), (Double)null ,"Failed"));
        }

        @Test
        public void testIsGreaterThanDoubleFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Double.valueOf(1.0), Double.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanFloatOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Float.valueOf(2.0f), Float.valueOf(1.0f) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanFloatOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((Float)null, Float.valueOf(1.0f) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanFloatOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(Float.valueOf(1.0f), (Float)null,"Failed"));
        }

        @Test
        public void testIsGreaterThanFloatFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(Float.valueOf(1.0f), Float.valueOf(2.0f) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigDecimalOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(BigDecimal.valueOf(2.0), BigDecimal.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigDecimalOkFirstArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan((BigDecimal)null, BigDecimal.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigDecimalOkSecondArgNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isGreaterThan(BigDecimal.valueOf(1.0), (BigDecimal)null ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigDecimalFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isGreaterThan(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0) ,"Failed"));
        }
    }

    @Nested
    class TestOptionalIsGreaterThan {

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
    }
}
