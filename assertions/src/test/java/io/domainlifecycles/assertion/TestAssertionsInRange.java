package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsInRange {

    @Nested
    class TestIsInRange {

        @Test
        public void testIsInRangePrimitiveDoubleOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(1.0, 0.0,2.0,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveDoubleFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(2.01, 0.0,2.0, "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveDoubleFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(-0.01, 0.0,2.0, "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveFloatOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(1.0f, 0.0f,2.0f,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveFloatFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(2.01f, 0.0f,2.0f, "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveFloatFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(-0.01f, 0.0f,2.0f, "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveIntOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(1, 0,2,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveIntFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(3, 0,2, "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveIntFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(-1, 0,2, "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveByteOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange((byte)1, (byte)0,(byte)2,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveByteFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange((byte)3, (byte)0,(byte)2, "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveByteFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange((byte)-1, (byte)0,(byte)2, "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveShortOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange((short)1, (short)0,(short)2,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveShortFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange((short)3, (short)0,(short)2, "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveShortFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange((short)-1, (short)0,(short)2, "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveLongOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(1l, 0l ,2l ,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveLongFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(3l, 0l ,2l , "Failed"));
        }

        @Test
        public void testIsInRangePrimitiveLongFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(-1l, 0l ,2l , "Failed"));
        }

        @Test
        public void testIsInRangeLongOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(Long.valueOf(1l), 0l ,2l ,"Failed"));
        }

        @Test
        public void testIsInRangeLongOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(null, 0l ,2l ,"Failed"));
        }

        @Test
        public void testIsInRangeLongFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(Long.valueOf(3l), 0l ,2l , "Failed"));
        }

        @Test
        public void testIsInRangeLongFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(Long.valueOf(-1l), 0l ,2l , "Failed"));
        }

        @Test
        public void testIsInRangeBigDecimalOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(BigDecimal.valueOf(1l), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsInRangeBigDecimalOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(null, BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsInRangeBigDecimalFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(BigDecimal.valueOf(3l), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed"));
        }

        @Test
        public void testIsInRangeBigDecimalFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(BigDecimal.valueOf(-1l), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed"));
        }

        @Test
        public void testIsInRangeBigIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(BigInteger.valueOf(1l), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsInRangeBigIntegerOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isInRange(null, BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsInRangeBigIntegerFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(BigInteger.valueOf(3l), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed"));
        }

        @Test
        public void testIsInRangeBigIntegerFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isInRange(BigInteger.valueOf(-1l), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed"));
        }
    }

    @Nested
    class TestOptionalIsInRange {

        @Test
        public void testOptionalIsInRangeLongOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Long.valueOf(1l)), 0l ,2l ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeLongOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Long)null), 0l ,2l ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeLongFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Long.valueOf(3l)), 0l ,2l , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeLongFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Long.valueOf(-1l)), 0l ,2l , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeLongFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Long>)null, 0l ,2l , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigDecimalOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(BigDecimal.valueOf(1l)), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigDecimalOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((BigDecimal)null), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigDecimalFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(BigDecimal.valueOf(3l)), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigDecimalFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(BigDecimal.valueOf(-1l)), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigDecimalFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<BigDecimal>)null, BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(BigInteger.valueOf(1l)), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigIntegerOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((BigInteger)null), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigIntegerFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(BigInteger.valueOf(3l)), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigIntegerFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(BigInteger.valueOf(-1l)), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigIntegerFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<BigInteger>)null, BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeDoubleOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Double.valueOf(1l)), Double.valueOf(0l) ,Double.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeDoubleOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Double)null), Double.valueOf(0l) ,Double.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeDoubleFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Double.valueOf(3l)), Double.valueOf(0l) ,Double.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeDoubleFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Double.valueOf(-1l)), Double.valueOf(0l) ,Double.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeDoubleFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Double>)null, Double.valueOf(0l) ,Double.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeFloatOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Float.valueOf(1l)), Float.valueOf(0l) ,Float.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeFloatOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Float)null), Float.valueOf(0l) ,Float.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeFloatFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Float.valueOf(3l)), Float.valueOf(0l) ,Float.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeFloatFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Float.valueOf(-1l)), Float.valueOf(0l) ,Float.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeFloatFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Float>)null, Float.valueOf(0l) ,Float.valueOf(2l) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Integer.valueOf(1)), Integer.valueOf(0) ,Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeIntegerOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Integer)null), Integer.valueOf(0) ,Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeIntegerFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Integer.valueOf(3)), Integer.valueOf(0) ,Integer.valueOf(2) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeIntegerFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Integer.valueOf(-1)), Integer.valueOf(0) ,Integer.valueOf(2) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeIntegerFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Integer>)null, Integer.valueOf(0) ,Integer.valueOf(2) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeShortOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Short.valueOf((short)1)), Short.valueOf((short)0) ,Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeShortOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Short)null), Short.valueOf((short)0) ,Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeShortFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Short.valueOf((short)3)), Short.valueOf((short)0) ,Short.valueOf((short)2) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeShortFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Short.valueOf((short)-1)), Short.valueOf((short)0) ,Short.valueOf((short)2) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeShortFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Short>)null, Short.valueOf((short)0) ,Short.valueOf((short)2) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeByteOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.of(Byte.valueOf((byte)1)), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeByteOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Byte)null), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeByteFailOver(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Byte.valueOf((byte)3)), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeByteFailUnder(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsInRange(Optional.of(Byte.valueOf((byte)-1)), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) , "Failed"));
        }

        @Test
        public void testOptionalIsInRangeByteFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsInRange((Optional<Byte>)null, Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) , "Failed"));
        }
    }
}
