package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsMaxDigits {

    @Nested
    class TestHasMaxDigits {

        @Test
        public void testHasMaxDigitsFailInteger(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigits(BigDecimal.valueOf(12.23), 1,2,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsFailFraction(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigits(BigDecimal.valueOf(12.234), 1,2,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigits(BigDecimal.valueOf(12.234), 2, 3,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsNullOkBigDecimal(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigits((BigDecimal) null, 2, 3,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFailDoublePrimitive(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigits(12.1, 1,1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsOkDoublePrimitive(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigits(12.2, 2,1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFailDouble(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigits(Double.valueOf(12.1), 1,1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsOkDouble(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigits(Double.valueOf(12.2), 2,1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsNullOkDouble(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigits((Double) null, 2, 3,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFailFloatPrimitive(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigits(10.0f, 1,1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsOkFloatPrimitive(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigits(10.5f, 2,1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFailFloat(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigits(Float.valueOf(10.1f), 1,1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsOkFloat(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigits(Float.valueOf(10.1f), 2,1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsNullOkFloat(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigits((Float) null, 2, 3,  "Failed"));
        }
    }

    @Nested
    class TestHasMaxDigitsInteger {

        @Test
        public void testHasMaxDigitsIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(BigDecimal.valueOf(12.23), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(BigDecimal.valueOf(12.234), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerNullOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((BigDecimal) null, 2,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailBigInteger(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(BigInteger.valueOf(12), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkBigInteger(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(BigInteger.valueOf(12), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerNullOkBigInteger(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((BigInteger) null, 2,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailDoublePrimitive(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(12.1, 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkDoublePrimitive(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(12.2, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailDouble(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Double.valueOf(12.1), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkDouble(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Double.valueOf(12.2), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkDoubleNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((Double)null, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailFloatPrimitive(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(10.1f, 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkFloatPrimitive(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((float)10.2, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailFloat(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Float.valueOf(10.1f), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkFloat(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Float.valueOf(10.1f), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkFloatNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((Float)null, 2, "Failed"));
        }


        @Test
        public void testHasMaxDigitsIntegerFailIntPrimitive(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(12, 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkIntPrimitive(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(12, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailInteger(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Integer.valueOf(12), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkInteger(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Integer.valueOf(12), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkIntegerNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((Integer)null, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailLongPrimitive(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(12l, 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkLongPrimitive(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(12l, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailLong(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Long.valueOf(12), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkLong(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Long.valueOf(12), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkLongNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((Long)null, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailShortPrimitive(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((short)12, 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkShortPrimitive(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((short)12, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailShort(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Short.valueOf((short)12), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkShort(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Short.valueOf((short)12), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkShortNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((Short)null, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailBytePrimitive(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((byte)12, 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkBytePrimitive(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((byte)12, 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerFailByte(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Byte.valueOf((byte)12), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsIntegerOkByte(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger(Byte.valueOf((byte)12), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerOkByteNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsInteger((Byte)null, 2, "Failed"));
        }
    }

    @Nested
    class TestHasMaxDigitsFraction {

        @Test
        public void testHasMaxDigitsFractionFail(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsFraction(BigDecimal.valueOf(12.23), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsFractionOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsFraction(BigDecimal.valueOf(12.234), 3, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionNullOkBigDecimal(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsFraction((BigDecimal) null, 2,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionFailDoublePrimitive(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsFraction(12.13, 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsFractionOkDoublePrimitive(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsFraction(12.2, 1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionFailDouble(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsFraction(Double.valueOf(12.13), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsFractionOkDouble(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsFraction(Double.valueOf(10.2), 1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionNullOkDouble(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsFraction((Double) null, 2,  "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionFailFloatPrimitive(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsFraction(12.12f, 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsFractionOkFloatPrimitive(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsFraction(10.1f, 1, "Failed"));
        }


        @Test
        public void testHasMaxDigitsFractionFailFloat(){
            assertThatThrownBy(()-> DomainAssertions.hasMaxDigitsFraction(Float.valueOf(10.25f), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasMaxDigitsFractionOkFloat(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsFraction(Float.valueOf(10.1f), 1, "Failed"));
        }

        @Test
        public void testHasMaxDigitsFractionNullOkBigFloat(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.hasMaxDigitsFraction((Float) null, 2,  "Failed"));
        }
    }

    @Nested
    class TestOptionalHasMaxDigits {

        @Test
        public void testOptionalHasMaxDigitsBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(BigDecimal.valueOf(12.234)), 2, 3,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsBigDecimalFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(BigDecimal.valueOf(12.23)), 1,2,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Double.valueOf(12.23)), 2,2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsDoubleFailInt(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Double.valueOf(12.23)), 1,2,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsDoubleFailFrac(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Double.valueOf(12.23)), 2,1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Float.valueOf(12.23f)), 2,2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFloatFailInt(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Float.valueOf(12.23f)), 1,2,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsFloatFailFrac(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigits(Optional.of(Float.valueOf(12.23f)), 2,1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsEmptyOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigits(Optional.empty(), 2, 3,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigits(null, 1,2,  "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsFailWrongType(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigits(Optional.of("a"), 1,2,  "Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class TestOptionalHasMaxDigitsInteger {

        @Test
        public void testOptionalHasMaxDigitsIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(BigDecimal.valueOf(12.23)), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(BigDecimal.valueOf(12.234)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Double.valueOf(12.234)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Float.valueOf(10.1f)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Integer.valueOf(12)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Long.valueOf(12)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerBigIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(BigInteger.valueOf(12)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerByteOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Byte.valueOf((byte) 12)), 2, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerShortOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of(Short.valueOf((short) 12)), 2, "Failed"));
        }

        @Test
        public void testHasMaxDigitsIntegerEmptyOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.empty(), 2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerNullFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(null, 1,  "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsIntegerWrongTypeFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigitsInteger(Optional.of("sdad"), 1,  "Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class TestOptionalHasMaxDigitsFraction {

        @Test
        public void testOptionalHasMaxDigitsFractionBigDecimalFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(BigDecimal.valueOf(12.23)), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsFractionBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(BigDecimal.valueOf(12.234)), 3, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionDoubleFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(Double.valueOf(12.23)), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsFractionDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(Double.valueOf(12.234)), 3, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionFloatFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(Float.valueOf(12.23F)), 1,  "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsFractionFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of(Float.valueOf(12.234F)), 3, "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionEmptyOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.empty(), 2,  "Failed"));
        }

        @Test
        public void testOptionalHasMaxDigitsFractionNullFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigitsFraction(null, 1,  "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalHasMaxDigitsFractionWrongTypeFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalHasMaxDigitsFraction(Optional.of("sdfse"), 1,  "Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
