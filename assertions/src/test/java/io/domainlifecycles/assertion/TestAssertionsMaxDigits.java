package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsMaxDigits {

    @Nested
    class TestHasMaxDigits {

        @Test
        public void testHasMaxDigitsFailInteger(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(BigDecimal.valueOf(12.23), 1,2,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFailFraction(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(BigDecimal.valueOf(12.234), 1,2,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits(BigDecimal.valueOf(12.234), 2, 3,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits((BigDecimal) null, 2, 3,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFailDoublePrimitive(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(12.1, 1,1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsOkDoublePrimitive(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits(12.2, 2,1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFailDouble(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(Double.valueOf(12.1), 1,1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsOkDouble(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits(Double.valueOf(12.2), 2,1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFailFloatPrimitive(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(10.0f, 1,1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsOkFloatPrimitive(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits(10.5f, 2,1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFailFloat(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigits(Float.valueOf(10.1f), 1,1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsOkFloat(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigits(Float.valueOf(10.1f), 2,1, "Failed"));
        }


    }

    @Nested
    class TestHasMaxDigitsInteger {

        @Test
        public void testHasMaxDigitsIntegerFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(BigDecimal.valueOf(12.23), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(BigDecimal.valueOf(12.234), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((BigDecimal) null, 2,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailBigInteger(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(BigInteger.valueOf(12), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkBigInteger(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(BigInteger.valueOf(12), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerNullOkBigInteger(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((BigInteger) null, 2,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailDoublePrimitive(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(12.1, 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkDoublePrimitive(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(12.2, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailDouble(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Double.valueOf(12.1), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkDouble(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Double.valueOf(12.2), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkDoubleNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Double)null, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailFloatPrimitive(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(10.1f, 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkFloatPrimitive(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((float)10.2, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailFloat(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Float.valueOf(10.1f), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkFloat(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Float.valueOf(10.1f), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkFloatNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Float)null, 2, "Failed"));
        }


        @Test
        public void testHasMaxDigitsIntegerFailIntPrimitive(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(12, 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkIntPrimitive(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(12, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailInteger(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Integer.valueOf(12), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkInteger(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Integer.valueOf(12), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkIntegerNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Integer)null, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailLongPrimitive(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(12l, 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkLongPrimitive(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(12l, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailLong(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Long.valueOf(12), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkLong(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Long.valueOf(12), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkLongNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Long)null, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailShortPrimitive(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger((short)12, 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkShortPrimitive(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((short)12, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailShort(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Short.valueOf((short)12), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkShort(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Short.valueOf((short)12), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkShortNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Short)null, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailBytePrimitive(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger((byte)12, 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkBytePrimitive(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((byte)12, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailByte(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsInteger(Byte.valueOf((byte)12), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkByte(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger(Byte.valueOf((byte)12), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkByteNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsInteger((Byte)null, 2, "Failed"));
        }
    }

    @Nested
    class TestHasMaxDigitsFraction {

        @Test
        public void testHasMaxDigitsFractionFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsFraction(BigDecimal.valueOf(12.23), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction(BigDecimal.valueOf(12.234), 3, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction((BigDecimal) null, 2,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionFailDoublePrimitive(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsFraction(12.13, 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionOkDoublePrimitive(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction(12.2, 1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionFailDouble(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsFraction(Double.valueOf(12.13), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionOkDouble(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction(Double.valueOf(10.2), 1, "Failed"));
        }


        @Test
        public void testHasMaxDigitsFractionFailFloatPrimitive(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsFraction(12.12f, 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionOkFloatPrimitive(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction(10.1f, 1, "Failed"));
        }


        @Test
        public void testHasMaxDigitsFractionFailFloat(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasMaxDigitsFraction(Float.valueOf(10.25f), 1,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionOkFloat(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasMaxDigitsFraction(Float.valueOf(10.1f), 1, "Failed"));
        }
    }

    @Nested
    class TestOptionalHasMaxDigits {

        @Test
        public void testOptionalHasMaxDigitsBigDecimalOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(BigDecimal.valueOf(12.234)), 2, 3,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsBigDecimalFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigits(Optional.of(BigDecimal.valueOf(12.23)), 1,2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsDoubleOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Double.valueOf(12.23)), 2,2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsDoubleFailInt(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Double.valueOf(12.23)), 1,2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsDoubleFailFrac(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Double.valueOf(12.23)), 2,1,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFloatOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Float.valueOf(12.23f)), 2,2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFloatFailInt(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Float.valueOf(12.23f)), 1,2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFloatFailFrac(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Float.valueOf(12.23f)), 2,1,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigits(Optional.empty(), 2, 3,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigits(null, 1,2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFailWrongType(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigits(Optional.of("a"), 1,2,  "Failed"));
        }
    }

    @Nested
    class TestOptionalHasMaxDigitsInteger {

        @Test
        public void testOptionalHasMaxDigitsIntegerFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(BigDecimal.valueOf(12.23)), 1,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerBigDecimalOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(BigDecimal.valueOf(12.234)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerDoubleOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Double.valueOf(12.234)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerFloatOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Float.valueOf(10.1f)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Integer.valueOf(12)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerLongOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Long.valueOf(12)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerBigIntegerOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(BigInteger.valueOf(12)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerByteOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Byte.valueOf((byte) 12)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerShortOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Short.valueOf((short) 12)), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.empty(), 2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerNullFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigitsInteger(null, 1,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerWrongTypeFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of("sdad"), 1,  "Failed"));
        }
    }

    @Nested
    class TestOptionalHasMaxDigitsFraction {

        @Test
        public void testOptionalHasMaxDigitsFractionBigDecimalFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(BigDecimal.valueOf(12.23)), 1,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionBigDecimalOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(BigDecimal.valueOf(12.234)), 3, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionDoubleFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(Double.valueOf(12.23)), 1,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionDoubleOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(Double.valueOf(12.234)), 3, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionFloatFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(Float.valueOf(12.23F)), 1,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionFloatOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(Float.valueOf(12.234F)), 3, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.empty(), 2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionNullFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigitsFraction(null, 1,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionWrongTypeFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of("sdfse"), 1,  "Failed"));
        }
    }
}
