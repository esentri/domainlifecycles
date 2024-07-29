package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsPositive {

    @Nested
    class TestIsPositive {

        @Test
        public void testIsPositiveShortOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Short.valueOf((short)2), "Failed"));
        }

        @Test
        public void testIsPositiveShortOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Short)null, "Failed"));
        }

        @Test
        public void testIsPositiveShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Short.valueOf((short)-1), "Failed"));
        }

        @Test
        public void testIsPositiveIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Integer.valueOf(2), "Failed"));
        }

        @Test
        public void testIsPositiveIntegerOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Integer)null, "Failed"));
        }

        @Test
        public void testIsPositiveIntegerFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Integer.valueOf(-1), "Failed"));
        }

        @Test
        public void testIsPositiveLongOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Long.valueOf(2), "Failed"));
        }

        @Test
        public void testIsPositiveLongOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Long)null, "Failed"));
        }

        @Test
        public void testIsPositiveLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Long.valueOf(-1l), "Failed"));
        }

        @Test
        public void testIsPositiveDoubleOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Double.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsPositiveDoubleOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Double)null, "Failed"));
        }

        @Test
        public void testIsPositiveDoubleFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Double.valueOf(-1.0), "Failed"));
        }

        @Test
        public void testIsPositiveFloatOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Float.valueOf(2.0f), "Failed"));
        }

        @Test
        public void testIsPositiveFloatOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Float)null, "Failed"));
        }

        @Test
        public void testIsPositiveFloatFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Float.valueOf(-1.0f), "Failed"));
        }

        @Test
        public void testIsPositiveBigDecimalOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(BigDecimal.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsPositiveBigDecimalOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((BigDecimal)null, "Failed"));
        }

        @Test
        public void testIsPositiveBigDecimalFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(BigDecimal.valueOf(-1.0), "Failed"));
        }

        @Test
        public void testIsPositiveBigIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(BigInteger.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsPositiveBigIntegerOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((BigInteger)null, "Failed"));
        }

        @Test
        public void testIsPositiveBigIntegerFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(BigInteger.valueOf(-1l), "Failed"));
        }

        @Test
        public void testIsPositiveBytePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((byte)2, "Failed"));
        }

        @Test
        public void testIsPositiveBytePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive((byte)-1, "Failed"));
        }

        @Test
        public void testIsPositiveShortPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((short)2, "Failed"));
        }

        @Test
        public void testIsPositiveShortPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive((short)-1, "Failed"));
        }

        @Test
        public void testIsPositiveIntPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(2, "Failed"));
        }

        @Test
        public void testIsPositiveIntPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(-1, "Failed"));
        }

        @Test
        public void testIsPositiveLongPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(2l, "Failed"));
        }

        @Test
        public void testIsPositiveLongPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(-1l, "Failed"));
        }

        @Test
        public void testIsPositiveDoublePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(2.0, "Failed"));
        }

        @Test
        public void testIsPositiveDoublePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(-1.0, "Failed"));
        }

        @Test
        public void testIsPositiveFloatPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(2.0f, "Failed"));
        }

        @Test
        public void testIsPositiveFloatPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(-1.0f, "Failed"));
        }

        @Test
        public void testIsPositiveByteOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive(Byte.valueOf((byte)2), "Failed"));
        }

        @Test
        public void testIsPositiveByteOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isPositive((Byte)null, "Failed"));
        }

        @Test
        public void testIsPositiveByteFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isPositive(Byte.valueOf((byte)-1), "Failed"));
        }
    }

    @Nested
    class TestOptionalIsPositive {

        @Test
        public void testOptionalIsPositiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositive(Optional.of(Byte.valueOf((byte)2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsPositive(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsPositive(Optional.of(Byte.valueOf((byte)-1)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPositive(null, "Failed"));
        }

        @Test
        public void testOptionalIsPositiveFailWrongType(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsPositive(Optional.of(""), "Failed"));
        }
    }
}
