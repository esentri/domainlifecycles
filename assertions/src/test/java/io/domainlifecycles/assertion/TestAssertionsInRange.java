package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsInRange {

    @Nested
    class TestIsInRange {

        @Test
        public void testIsInRangePrimitiveDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange(1.0, 0.0,2.0,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveDoubleFailOver(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(2.01, 0.0,2.0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveDoubleFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(-0.01, 0.0,2.0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange(1.0f, 0.0f,2.0f,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveFloatFailOver(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(2.01f, 0.0f,2.0f, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveFloatFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(-0.01f, 0.0f,2.0f, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveIntOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange(1, 0,2,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveIntFailOver(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(3, 0,2, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveIntFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(-1, 0,2, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveByteOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange((byte)1, (byte)0,(byte)2,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveByteFailOver(){
            assertThatThrownBy(()-> DomainAssertions.isInRange((byte)3, (byte)0,(byte)2, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveByteFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.isInRange((byte)-1, (byte)0,(byte)2, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveShortOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange((short)1, (short)0,(short)2,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveShortFailOver(){
            assertThatThrownBy(()-> DomainAssertions.isInRange((short)3, (short)0,(short)2, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveShortFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.isInRange((short)-1, (short)0,(short)2, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange(1l, 0l ,2l ,"Failed"));
        }

        @Test
        public void testIsInRangePrimitiveLongFailOver(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(3l, 0l ,2l , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangePrimitiveLongFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(-1l, 0l ,2l , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangeLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange(Long.valueOf(1l), 0l ,2l ,"Failed"));
        }

        @Test
        public void testIsInRangeLongOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange(null, 0l ,2l ,"Failed"));
        }

        @Test
        public void testIsInRangeLongFailOver(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(Long.valueOf(3l), 0l ,2l , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangeLongFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(Long.valueOf(-1l), 0l ,2l , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangeBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange(BigDecimal.valueOf(1l), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsInRangeBigDecimalOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange(null, BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsInRangeBigDecimalFailOver(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(BigDecimal.valueOf(3l), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangeBigDecimalFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(BigDecimal.valueOf(-1l), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangeBigIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange(BigInteger.valueOf(1l), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsInRangeBigIntegerOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isInRange(null, BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsInRangeBigIntegerFailOver(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(BigInteger.valueOf(3l), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsInRangeBigIntegerFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.isInRange(BigInteger.valueOf(-1l), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsInRange {

        @Test
        public void testOptionalIsInRangeLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Long.valueOf(1l)), 0l ,2l ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeLongOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Long)null), 0l ,2l ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeLongFailOver(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Long.valueOf(3l)), 0l ,2l , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeLongFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Long.valueOf(-1l)), 0l ,2l , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeLongFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange((Optional<Long>)null, 0l ,2l , "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsInRangeBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(BigDecimal.valueOf(1l)), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigDecimalOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((BigDecimal)null), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigDecimalFailOver(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(BigDecimal.valueOf(3l)), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeBigDecimalFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(BigDecimal.valueOf(-1l)), BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeBigDecimalFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange((Optional<BigDecimal>)null, BigDecimal.valueOf(0l) ,BigDecimal.valueOf(2l) , "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsInRangeBigIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(BigInteger.valueOf(1l)), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigIntegerOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((BigInteger)null), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeBigIntegerFailOver(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(BigInteger.valueOf(3l)), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeBigIntegerFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(BigInteger.valueOf(-1l)), BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeBigIntegerFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange((Optional<BigInteger>)null, BigInteger.valueOf(0l) ,BigInteger.valueOf(2l) , "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsInRangeDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Double.valueOf(1l)), Double.valueOf(0l) ,Double.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeDoubleOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Double)null), Double.valueOf(0l) ,Double.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeDoubleFailOver(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Double.valueOf(3l)), Double.valueOf(0l) ,Double.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeDoubleFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Double.valueOf(-1l)), Double.valueOf(0l) ,Double.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeDoubleFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange((Optional<Double>)null, Double.valueOf(0l) ,Double.valueOf(2l) , "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsInRangeFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Float.valueOf(1l)), Float.valueOf(0l) ,Float.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeFloatOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Float)null), Float.valueOf(0l) ,Float.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeFloatFailOver(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Float.valueOf(3l)), Float.valueOf(0l) ,Float.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeFloatFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Float.valueOf(-1l)), Float.valueOf(0l) ,Float.valueOf(2l) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeFloatFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange((Optional<Float>)null, Float.valueOf(0l) ,Float.valueOf(2l) , "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsInRangeIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Integer.valueOf(1)), Integer.valueOf(0) ,Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeIntegerOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Integer)null), Integer.valueOf(0) ,Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeIntegerFailOver(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Integer.valueOf(3)), Integer.valueOf(0) ,Integer.valueOf(2) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeIntegerFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Integer.valueOf(-1)), Integer.valueOf(0) ,Integer.valueOf(2) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeIntegerFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange((Optional<Integer>)null, Integer.valueOf(0) ,Integer.valueOf(2) , "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsInRangeShortOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Short.valueOf((short)1)), Short.valueOf((short)0) ,Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeShortOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Short)null), Short.valueOf((short)0) ,Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeShortFailOver(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Short.valueOf((short)3)), Short.valueOf((short)0) ,Short.valueOf((short)2) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeShortFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Short.valueOf((short)-1)), Short.valueOf((short)0) ,Short.valueOf((short)2) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeShortFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange((Optional<Short>)null, Short.valueOf((short)0) ,Short.valueOf((short)2) , "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsInRangeByteOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Byte.valueOf((byte)1)), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeByteOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.ofNullable((Byte)null), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsInRangeByteFailOver(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Byte.valueOf((byte)3)), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeByteFailUnder(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange(Optional.of(Byte.valueOf((byte)-1)), Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) , "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsInRangeByteFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsInRange((Optional<Byte>)null, Byte.valueOf((byte)0) ,Byte.valueOf((byte)2) , "Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
