package io.domainlifecycles.assertion;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsNegative {

    @Nested
    class TestIsNegative {

        @Test
        public void testIsNegativeBigIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(BigInteger.valueOf(-1L), "Failed"));
        }

        @Test
        public void testIsNegativeBigIntegerOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative((BigInteger)null, "Failed"));
        }

        @Test
        public void testIsNegativeBigIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(BigInteger.valueOf(0L), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeBytePrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative((byte)-1, "Failed"));
        }

        @Test
        public void testIsNegativeBytePrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative((byte)0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeShortPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative((short)-1, "Failed"));
        }

        @Test
        public void testIsNegativeShortPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative((short)0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeIntPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(-1, "Failed"));
        }

        @Test
        public void testIsNegativeIntPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeLongPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(-1L, "Failed"));
        }

        @Test
        public void testIsNegativeLongPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(0L, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeDoublePrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(-1.0, "Failed"));
        }

        @Test
        public void testIsNegativeDoublePrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(0.0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeFloatPrimitiveOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(-1.0f, "Failed"));
        }

        @Test
        public void testIsNegativeFloatPrimitiveFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(0.0f, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeByteOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(Byte.valueOf((byte)-1), "Failed"));
        }

        @Test
        public void testIsNegativeByteOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative((Byte)null, "Failed"));
        }

        @Test
        public void testIsNegativeByteFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(Byte.valueOf((byte)0), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeShortOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(Short.valueOf((short)-1), "Failed"));
        }

        @Test
        public void testIsNegativeShortOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative((Short)null, "Failed"));
        }

        @Test
        public void testIsNegativeShortFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(Short.valueOf((short)0), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeIntegerOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(Integer.valueOf(-1), "Failed"));
        }

        @Test
        public void testIsNegativeIntegerOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative((Integer)null, "Failed"));
        }

        @Test
        public void testIsNegativeIntegerFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(Integer.valueOf(0), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeLongOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(Long.valueOf(-1L), "Failed"));
        }

        @Test
        public void testIsNegativeLongOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative((Long)null, "Failed"));
        }

        @Test
        public void testIsNegativeLongFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(Long.valueOf(0L), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeDoubleOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(Double.valueOf(-1.0), "Failed"));
        }

        @Test
        public void testIsNegativeDoubleOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative((Double)null, "Failed"));
        }

        @Test
        public void testIsNegativeDoubleFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(Double.valueOf(0.0), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeFloatOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(Float.valueOf(-1.0f), "Failed"));
        }

        @Test
        public void testIsNegativeFloatOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative((Float)null, "Failed"));
        }

        @Test
        public void testIsNegativeFloatFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(Float.valueOf(0.0f), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsNegativeBigDecimalOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative(BigDecimal.valueOf(-1.0), "Failed"));
        }

        @Test
        public void testIsNegativeBigDecimalOkNull(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNegative((BigDecimal)null, "Failed"));
        }

        @Test
        public void testIsNegativeBigDecimalFail(){
            assertThatThrownBy(()-> DomainAssertions.isNegative(BigDecimal.valueOf(0.0), "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsNegative {

        @Test
        public void testOptionalIsNegativeOkByte(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.of(Byte.valueOf((byte)-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkShort(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.of(Short.valueOf((short)-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkInteger(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.of(Integer.valueOf(-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkLong(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.of(Long.valueOf(-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkBigDecimal(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.of(BigDecimal.valueOf(-1.0)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkBigInteger(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.of(BigInteger.valueOf(-1)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkDouble(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.of(Double.valueOf(-1.0)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkFloat(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.of(Float.valueOf(-1.0F)), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeOkEmpty(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsNegativeFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.of(Byte.valueOf((byte)0)), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsNegativeFailNull(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsNegative(null, "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsNegativeFailWrongType(){
            assertThatThrownBy(()-> DomainAssertions.optionalIsNegative(Optional.of(""), "Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
