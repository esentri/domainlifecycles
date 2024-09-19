package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsGreaterThan {

    @Nested
    class TestIsGreaterThan {

        @Test
        public void testIsGreaterThanBigIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(BigInteger.valueOf(2l), BigInteger.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigIntegerOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan((BigInteger)null, BigInteger.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigIntegerOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(BigInteger.valueOf(1l), (BigInteger)null,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(BigInteger.valueOf(1l), BigInteger.valueOf(2l) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanBytePrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan((byte)2, (byte)1 ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBytePrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan((byte)1, (byte)2 ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanShortPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan((short)2, (short)1 ,"Failed"));
        }

        @Test
        public void testIsGreaterThanShortPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan((short)1, (short)2 ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanIntPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(2, 1 ,"Failed"));
        }

        @Test
        public void testIsGreaterThanIntPrimitiveFail() {
            assertThatThrownBy(() -> DomainAssertions.isGreaterThan(1, 2, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanLongPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(2l, 1l ,"Failed"));
        }

        @Test
        public void testIsGreaterThanLongPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(1l, 2l ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanDoublePrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(2.0, 1.0 ,"Failed"));
        }

        @Test
        public void testIsGreaterThanDoublePrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(1.0, 2.0 ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanFloatPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(2.0f, 1.0f ,"Failed"));
        }

        @Test
        public void testIsGreaterThanFloatPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(1.0f, 2.0f ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanByteOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Byte.valueOf((byte)2), Byte.valueOf((byte)1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanByteOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan((Byte)null, Byte.valueOf((byte)1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanByteOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Byte.valueOf((byte)1), (Byte)null ,"Failed"));
        }

        @Test
        public void testIsGreaterThanByteFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(Byte.valueOf((byte)1), Byte.valueOf((byte)2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanShortOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Short.valueOf((short)2), Short.valueOf((short)1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanShortOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan((Short)null, Short.valueOf((short)1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanShortOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Short.valueOf((short)1), (Short)null,"Failed"));
        }

        @Test
        public void testIsGreaterThanShortFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(Short.valueOf((short)1), Short.valueOf((short)2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Integer.valueOf(2), Integer.valueOf(1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanIntegerOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan((Integer)null, Integer.valueOf(1) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanIntegerOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Integer.valueOf(1),  (Integer)null,"Failed"));
        }

        @Test
        public void testIsGreaterThanIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(Integer.valueOf(1), Integer.valueOf(2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Long.valueOf(2l), Long.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanLongOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan((Long)null, Long.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanLongOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Long.valueOf(1l) , (Long)null,"Failed"));
        }

        @Test
        public void testIsGreaterThanLongFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(Long.valueOf(1l), Long.valueOf(2l) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Double.valueOf(2.0), Double.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanDoubleOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan((Double)null, Double.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanDoubleOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Double.valueOf(1.0), (Double)null ,"Failed"));
        }

        @Test
        public void testIsGreaterThanDoubleFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(Double.valueOf(1.0), Double.valueOf(2.0) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Float.valueOf(2.0f), Float.valueOf(1.0f) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanFloatOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan((Float)null, Float.valueOf(1.0f) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanFloatOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(Float.valueOf(1.0f), (Float)null,"Failed"));
        }

        @Test
        public void testIsGreaterThanFloatFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(Float.valueOf(1.0f), Float.valueOf(2.0f) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsGreaterThanBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(BigDecimal.valueOf(2.0), BigDecimal.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigDecimalOkFirstArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan((BigDecimal)null, BigDecimal.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigDecimalOkSecondArgNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isGreaterThan(BigDecimal.valueOf(1.0), (BigDecimal)null ,"Failed"));
        }

        @Test
        public void testIsGreaterThanBigDecimalFail(){
            assertThatThrownBy(()-> DomainAssertions.isGreaterThan(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsGreaterThan {

        @Test
        public void testOptionalIsGreaterThanBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(BigDecimal.valueOf(2.0)), BigDecimal.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanBigDecimalOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), BigDecimal.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanBigDecimalFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(BigDecimal.valueOf(1.0)), BigDecimal.valueOf(2.0) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterThanBigDecimalFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(null, BigDecimal.valueOf(2.0) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterThanBigIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(BigInteger.valueOf(2l)), BigInteger.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanBigIntegerOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), BigInteger.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanBigIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(BigInteger.valueOf(1l)), BigInteger.valueOf(2l) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterThanBigIntegerFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(null, BigInteger.valueOf(2l) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterThanByteOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Byte.valueOf((byte)2)), Byte.valueOf((byte)1) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanByteOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Byte.valueOf((byte)1) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanByteFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Byte.valueOf((byte)1)), Byte.valueOf((byte)2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterThanByteFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(null, Byte.valueOf((byte)2) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterThanShortOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Short.valueOf((short)2)), Short.valueOf((short)1) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanShortOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Short.valueOf((short)1) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanShortFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Short.valueOf((short)1)), Short.valueOf((short)2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterThanShortFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(null, Short.valueOf((short)2) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterThanIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Integer.valueOf(2)), Integer.valueOf(1) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanIntegerOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Integer.valueOf(1) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Integer.valueOf(1)), Integer.valueOf(2) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterThanIntegerFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(null, Integer.valueOf(2) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterThanLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Long.valueOf(2l)), Long.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanLongOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Long.valueOf(1l) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanLongFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Long.valueOf(1l)), Long.valueOf(2l) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterThanLongFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(null, Long.valueOf(2) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterThanDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Double.valueOf(2.0)), Double.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanDoubleOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Double.valueOf(1.0) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanDoubleFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Double.valueOf(1.0)), Double.valueOf(2.0) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterThanDoubleFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(null, Double.valueOf(2.0) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsGreaterThanFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Float.valueOf(2.0f)), Float.valueOf(1.0f) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanFloatOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.empty(), Float.valueOf(1.0f) ,"Failed"));
        }

        @Test
        public void testOptionalIsGreaterThanFloatFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(Optional.of(Float.valueOf(1.0f)), Float.valueOf(2.0f) ,"Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsGreaterThanFloatFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsGreaterThan(null, Float.valueOf(2.0f) ,"Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
