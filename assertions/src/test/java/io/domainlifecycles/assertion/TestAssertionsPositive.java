package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsPositive {

    @Nested
    class TestIsPositive {

        @Test
        public void testIsPositiveShortOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(Short.valueOf((short)2), "Failed"));
        }

        @Test
        public void testIsPositiveShortOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive((Short)null, "Failed"));
        }

        @Test
        public void testIsPositiveShortFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(Short.valueOf((short)-1), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(Integer.valueOf(2), "Failed"));
        }

        @Test
        public void testIsPositiveIntegerOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive((Integer)null, "Failed"));
        }

        @Test
        public void testIsPositiveIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(Integer.valueOf(-1), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(Long.valueOf(2), "Failed"));
        }

        @Test
        public void testIsPositiveLongOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive((Long)null, "Failed"));
        }

        @Test
        public void testIsPositiveLongFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(Long.valueOf(-1l), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(Double.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsPositiveDoubleOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive((Double)null, "Failed"));
        }

        @Test
        public void testIsPositiveDoubleFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(Double.valueOf(-1.0), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(Float.valueOf(2.0f), "Failed"));
        }

        @Test
        public void testIsPositiveFloatOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive((Float)null, "Failed"));
        }

        @Test
        public void testIsPositiveFloatFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(Float.valueOf(-1.0f), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(BigDecimal.valueOf(2.0), "Failed"));
        }

        @Test
        public void testIsPositiveBigDecimalOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive((BigDecimal)null, "Failed"));
        }

        @Test
        public void testIsPositiveBigDecimalFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(BigDecimal.valueOf(-1.0), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveBigIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(BigInteger.valueOf(2l), "Failed"));
        }

        @Test
        public void testIsPositiveBigIntegerOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive((BigInteger)null, "Failed"));
        }

        @Test
        public void testIsPositiveBigIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(BigInteger.valueOf(-1l), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveBytePrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive((byte)2, "Failed"));
        }

        @Test
        public void testIsPositiveBytePrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive((byte)-1, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveShortPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive((short)2, "Failed"));
        }

        @Test
        public void testIsPositiveShortPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive((short)-1, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveIntPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(2, "Failed"));
        }

        @Test
        public void testIsPositiveIntPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(-1, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveLongPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(2l, "Failed"));
        }

        @Test
        public void testIsPositiveLongPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(-1l, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveDoublePrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(2.0, "Failed"));
        }

        @Test
        public void testIsPositiveDoublePrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(-1.0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveFloatPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(2.0f, "Failed"));
        }

        @Test
        public void testIsPositiveFloatPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(-1.0f, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsPositiveByteOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive(Byte.valueOf((byte)2), "Failed"));
        }

        @Test
        public void testIsPositiveByteOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isPositive((Byte)null, "Failed"));
        }

        @Test
        public void testIsPositiveByteFail(){
            assertThatThrownBy(()-> DomainAssertions.isPositive(Byte.valueOf((byte)-1), "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsPositive {

        @Test
        public void testOptionalIsPositiveOkByte(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.of(Byte.valueOf((byte)2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOkShort(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.of(Short.valueOf((short)2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOkInteger(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.of(Integer.valueOf(2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOkLong(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.of(Long.valueOf(2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOkBigDecimal(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.of(BigDecimal.valueOf(2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOkBigInteger(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.of(BigInteger.valueOf(2)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOkDouble(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.of(Double.valueOf(2.0)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOkFloat(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.of(Float.valueOf(2.0f)), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsPositiveFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.of(Byte.valueOf((byte)-1)), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsPositiveFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsPositive(null, "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsPositiveFailWrongType(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsPositive(Optional.of(""), "Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
