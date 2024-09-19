package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsLessOrEqual {

    @Nested
    class TestIsLessOrEqual {
        @Test
        public void testIsLessOrEqualBytePrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual((byte)2, (byte)2 ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualBytePrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual((byte)3, (byte)2 ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualShortPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual((short)2, (short)2 ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualShortPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual((short)3, (short)2 ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualIntPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(2, 2 ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualIntPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(3, 2 ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualLongPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(2l, 2l ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualLongPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(3l, 2l ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualDoublePrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(2.0, 2.0 ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualDoublePrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(3.0, 2.0 ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualFloatPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(2.0f, 2.0f ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualFloatPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(3.0f, 2.0f ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualByteOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Byte.valueOf((byte)2), Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualByteOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual((Byte)null, Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualByteOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Byte.valueOf((byte)2), (Byte)null ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualByteFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(Byte.valueOf((byte)3), Byte.valueOf((byte)2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualShortOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Short.valueOf((short)2), Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualShortOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual((Short)null, Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualShortOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Short.valueOf((short)2),  (Short)null,"Failed"));
        }

        @Test
        public void testIsLessOrEqualShortFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(Short.valueOf((short)3), Short.valueOf((short)2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Integer.valueOf(2), Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualIntegerOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual((Integer)null, Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualIntegerOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Integer.valueOf(2) , (Integer)null,"Failed"));
        }

        @Test
        public void testIsLessOrEqualIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(Integer.valueOf(3), Integer.valueOf(2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Long.valueOf(2l), Long.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualLongOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual((Long)null, Long.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualLongOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Long.valueOf(2l),  (Long)null,"Failed"));
        }

        @Test
        public void testIsLessOrEqualLongFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(Long.valueOf(3l), Long.valueOf(2l) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Double.valueOf(2.0), Double.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualDoubleOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual((Double)null, Double.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualDoubleOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Double.valueOf(2.0),  (Double)null,"Failed"));
        }

        @Test
        public void testIsLessOrEqualDoubleFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(Double.valueOf(3.0), Double.valueOf(2.0) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Float.valueOf(2.0f), Float.valueOf(2.0f) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualFloatOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual((Float)null, Float.valueOf(2.0f) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualFloatOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(Float.valueOf(2.0f) , (Float)null,"Failed"));
        }

        @Test
        public void testIsLessOrEqualFloatFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(Float.valueOf(3.0f), Float.valueOf(2.0f) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(BigDecimal.valueOf(2.0), BigDecimal.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualBigDecimalOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual((BigDecimal)null, BigDecimal.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualBigDecimalOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(BigDecimal.valueOf(2.0) , (BigDecimal)null,"Failed"));
        }

        @Test
        public void testIsLessOrEqualBigDecimalFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(BigDecimal.valueOf(3.0), BigDecimal.valueOf(2.0) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsLessOrEqualBigIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(BigInteger.valueOf(2l), BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualBigIntegerOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual((BigInteger)null, BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualBigIntegerOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isLessOrEqual(BigInteger.valueOf(2l), (BigInteger)null ,"Failed"));
        }

        @Test
        public void testIsLessOrEqualBigIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.isLessOrEqual(BigInteger.valueOf(3l), BigInteger.valueOf(2l) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsLessOrEqual {

        @Test
        public void testOptionalIsLessOrEqualBigIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(BigInteger.valueOf(2l)), BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualBigIntegerOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), BigInteger.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualBigIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(BigInteger.valueOf(3l)), BigInteger.valueOf(2l) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualBigIntegerFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(null, BigInteger.valueOf(2l) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualByteOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Byte.valueOf((byte)2)), Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualByteOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Byte.valueOf((byte)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualByteFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Byte.valueOf((byte)3)), Byte.valueOf((byte)2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualByteFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(null, Byte.valueOf((byte)2) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualShortOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Short.valueOf((short)2)), Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualShortOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Short.valueOf((short)2) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualShortFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Short.valueOf((short)3)), Short.valueOf((short)2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualShortFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(null, Short.valueOf((short)2) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Integer.valueOf(2)), Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualIntegerOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Integer.valueOf(2) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Integer.valueOf(3)), Integer.valueOf(2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualIntegerFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(null, Integer.valueOf(2) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Long.valueOf(2l)), Long.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualLongOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Long.valueOf(2l) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualLongFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Long.valueOf(3l)), Long.valueOf(2l) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualLongFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(null, Long.valueOf(2l) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Double.valueOf(2.0)), Double.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualDoubleOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Double.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualDoubleFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Double.valueOf(3.0)), Double.valueOf(2.0) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualDoubleFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(null, Double.valueOf(2.0) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Float.valueOf(2.0f)), Float.valueOf(2.0f) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualFloatOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), Float.valueOf(2.0f) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualFloatFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(Float.valueOf(3.0f)), Float.valueOf(2.0f) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualFloatFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(null, Float.valueOf(2.0f) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(BigDecimal.valueOf(2.0)), BigDecimal.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualBigDecimalOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.empty(), BigDecimal.valueOf(2.0) ,"Failed"));
        }

        @Test
        public void testOptionalIsLessOrEqualBigDecimalFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(Optional.of(BigDecimal.valueOf(3.0)), BigDecimal.valueOf(2.0) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsLessOrEqualBigDecimalFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsLessOrEqual(null, BigDecimal.valueOf(2.0) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
