package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsNegative {

    @Nested
    class TestIsNegative {

        @Test
        public void testIsNegativeBigIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(BigInteger.valueOf(-1l), "Failed"));
        }

        @Test
        public void testIsNegativeBigIntegerOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((BigInteger)null, "Failed"));
        }

        @Test
        public void testIsNegativeBigIntegerFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(BigInteger.valueOf(0l), "Failed"));
        }

        @Test
        public void testIsNegativeBytePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((byte)-1, "Failed"));
        }

        @Test
        public void testIsNegativeBytePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative((byte)0, "Failed"));
        }

        @Test
        public void testIsNegativeShortPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((short)-1, "Failed"));
        }

        @Test
        public void testIsNegativeShortPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative((short)0, "Failed"));
        }

        @Test
        public void testIsNegativeIntPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(-1, "Failed"));
        }

        @Test
        public void testIsNegativeIntPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(0, "Failed"));
        }

        @Test
        public void testIsNegativeLongPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(-1l, "Failed"));
        }

        @Test
        public void testIsNegativeLongPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(0l, "Failed"));
        }

        @Test
        public void testIsNegativeDoublePrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(-1.0, "Failed"));
        }

        @Test
        public void testIsNegativeDoublePrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(0.0, "Failed"));
        }

        @Test
        public void testIsNegativeFloatPrimitiveOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(-1.0f, "Failed"));
        }

        @Test
        public void testIsNegativeFloatPrimitiveFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(0.0f, "Failed"));
        }

        @Test
        public void testIsNegativeByteOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Byte.valueOf((byte)-1), "Failed"));
        }

        @Test
        public void testIsNegativeByteOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Byte)null, "Failed"));
        }

        @Test
        public void testIsNegativeByteFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Byte.valueOf((byte)0), "Failed"));
        }

        @Test
        public void testIsNegativeShortOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Short.valueOf((short)-1), "Failed"));
        }

        @Test
        public void testIsNegativeShortOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Short)null, "Failed"));
        }

        @Test
        public void testIsNegativeShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Short.valueOf((short)0), "Failed"));
        }

        @Test
        public void testIsNegativeIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Integer.valueOf(-1), "Failed"));
        }

        @Test
        public void testIsNegativeIntegerOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Integer)null, "Failed"));
        }

        @Test
        public void testIsNegativeIntegerFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Integer.valueOf(0), "Failed"));
        }

        @Test
        public void testIsNegativeLongOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Long.valueOf(-1l), "Failed"));
        }

        @Test
        public void testIsNegativeLongOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Long)null, "Failed"));
        }

        @Test
        public void testIsNegativeLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Long.valueOf(0l), "Failed"));
        }

        @Test
        public void testIsNegativeDoubleOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Double.valueOf(-1.0), "Failed"));
        }

        @Test
        public void testIsNegativeDoubleOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Double)null, "Failed"));
        }

        @Test
        public void testIsNegativeDoubleFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Double.valueOf(0.0), "Failed"));
        }

        @Test
        public void testIsNegativeFloatOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(Float.valueOf(-1.0f), "Failed"));
        }

        @Test
        public void testIsNegativeFloatOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((Float)null, "Failed"));
        }

        @Test
        public void testIsNegativeFloatFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(Float.valueOf(0.0f), "Failed"));
        }

        @Test
        public void testIsNegativeBigDecimalOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative(BigDecimal.valueOf(-1.0), "Failed"));
        }

        @Test
        public void testIsNegativeBigDecimalOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNegative((BigDecimal)null, "Failed"));
        }

        @Test
        public void testIsNegativeBigDecimalFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNegative(BigDecimal.valueOf(0.0), "Failed"));
        }
    }

    @Nested
    class TestOptionalIsNegative {

        @Test
        public void testOptionalIsNegativeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegative(Optional.of(Byte.valueOf((byte)-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNegative(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsNegative(Optional.of(Byte.valueOf((byte)0)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsNegative(null, "Failed"));
        }

        @Test
        public void testOptionalIsNegativeFailWrongType(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsNegative(Optional.of(""), "Failed"));
        }
    }
}
