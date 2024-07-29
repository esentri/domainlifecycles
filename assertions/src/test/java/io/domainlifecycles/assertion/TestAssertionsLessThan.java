package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsLessThan {

    @Nested
    class TestIsLessThan {

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
        public void testIsLessThanBytePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan((byte)1, (byte)2 ,"Failed"));
        }

        @Test
        public void testIsLessThanBytePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan((byte)2, (byte)2 ,"Failed"));
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
        public void testIsLessThanIntPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(1, 2 ,"Failed"));
        }

        @Test
        public void testIsLessThanIntPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(2, 2 ,"Failed"));
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
        public void testIsLessThanDoublePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isLessThan(1.0, 2.0 ,"Failed"));
        }

        @Test
        public void testIsLessThanDoublePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isLessThan(2.0, 2.0 ,"Failed"));
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
    }

    @Nested
    class TestOptionalIsLessThan {

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
    }
}
