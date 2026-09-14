package io.domainlifecycles.persistence.mapping.converter.def;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for the {@code Default*Converter} classes converting between the numeric/boolean Java types
 * ({@link BigDecimal}, {@link BigInteger}, {@code byte}/{@code short}/{@code int}/{@code long}/{@code float}/
 * {@code double}, {@link Boolean}).
 */
public class NumericDefaultConvertersTest {

    @Test
    public void testBigDecimalToDouble() {
        var converter = new DefaultBigDecimalToDoubleConverter();

        assertThat(converter.convert(new BigDecimal("3.5"))).isEqualTo(3.5d);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testBigDecimalToFloat() {
        var converter = new DefaultBigDecimalToFloatConverter();

        assertThat(converter.convert(new BigDecimal("3.5"))).isEqualTo(3.5f);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testDoubleToBigDecimal() {
        var converter = new DefaultDoubleToBigDecimalConverter();

        assertThat(converter.convert(3.5d)).isEqualByComparingTo(new BigDecimal("3.5"));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testFloatToBigDecimal() {
        var converter = new DefaultFloatToBigDecimalConverter();

        assertThat(converter.convert(3.5f)).isEqualByComparingTo(new BigDecimal("3.5"));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testDoubleToFloat() {
        var converter = new DefaultDoubleToFloatConverter();

        assertThat(converter.convert(3.5d)).isEqualTo(3.5f);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testFloatToDouble() {
        var converter = new DefaultFloatToDoubleConverter();

        assertThat(converter.convert(3.5f)).isEqualTo(3.5d);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testBigIntegerToByteWithinRange() {
        var converter = new DefaultBigIntegerToByteConverter();

        assertThat(converter.convert(BigInteger.valueOf(127))).isEqualTo((byte) 127);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testBigIntegerToByteOutOfRangeThrows() {
        var converter = new DefaultBigIntegerToByteConverter();

        assertThatThrownBy(() -> converter.convert(BigInteger.valueOf(128)))
            .isInstanceOf(ArithmeticException.class);
    }

    @Test
    public void testBigIntegerToShortWithinRange() {
        var converter = new DefaultBigIntegerToShortConverter();

        assertThat(converter.convert(BigInteger.valueOf(Short.MAX_VALUE))).isEqualTo(Short.MAX_VALUE);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testBigIntegerToShortOutOfRangeThrows() {
        var converter = new DefaultBigIntegerToShortConverter();

        assertThatThrownBy(() -> converter.convert(BigInteger.valueOf(Short.MAX_VALUE + 1)))
            .isInstanceOf(ArithmeticException.class);
    }

    @Test
    public void testBigIntegerToIntegerWithinRange() {
        var converter = new DefaultBigIntegerToIntegerConverter();

        assertThat(converter.convert(BigInteger.valueOf(Integer.MAX_VALUE))).isEqualTo(Integer.MAX_VALUE);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testBigIntegerToIntegerOutOfRangeThrows() {
        var converter = new DefaultBigIntegerToIntegerConverter();
        var tooBig = BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE);

        assertThatThrownBy(() -> converter.convert(tooBig)).isInstanceOf(ArithmeticException.class);
    }

    @Test
    public void testBigIntegerToLongWithinRange() {
        var converter = new DefaultBigIntegerToLongConverter();

        assertThat(converter.convert(BigInteger.valueOf(Long.MAX_VALUE))).isEqualTo(Long.MAX_VALUE);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testBigIntegerToLongOutOfRangeThrows() {
        var converter = new DefaultBigIntegerToLongConverter();
        var tooBig = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);

        assertThatThrownBy(() -> converter.convert(tooBig)).isInstanceOf(ArithmeticException.class);
    }

    @Test
    public void testByteToBigInteger() {
        var converter = new DefaultByteToBigIntegerConverter();

        assertThat(converter.convert((byte) 42)).isEqualTo(BigInteger.valueOf(42));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testShortToBigInteger() {
        var converter = new DefaultShortToBigIntegerConverter();

        assertThat(converter.convert((short) 42)).isEqualTo(BigInteger.valueOf(42));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testIntegerToBigInteger() {
        var converter = new DefaultIntegerToBigIntegerConverter();

        assertThat(converter.convert(42)).isEqualTo(BigInteger.valueOf(42));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testLongToBigInteger() {
        var converter = new DefaultLongToBigIntegerConverter();

        assertThat(converter.convert(42L)).isEqualTo(BigInteger.valueOf(42));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testByteToLong() {
        var converter = new DefaultByteToLongConverter();

        assertThat(converter.convert((byte) 42)).isEqualTo(42L);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testIntToLong() {
        var converter = new DefaultIntToLongConverter();

        assertThat(converter.convert(42)).isEqualTo(42L);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testLongToIntWithinRange() {
        var converter = new DefaultLongToIntConverter();

        assertThat(converter.convert(42L)).isEqualTo(42);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testLongToIntOutOfRangeSilentlyTruncatesWithoutThrowing() {
        // unlike the BigInteger converters (*ValueExact), this uses Long.intValue(), a plain narrowing
        // conversion: out-of-range values wrap around instead of raising an ArithmeticException.
        var converter = new DefaultLongToIntConverter();

        assertThat(converter.convert(Integer.MAX_VALUE + 1L)).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    public void testLongToByteWithinRange() {
        var converter = new DefaultLongToByteConverter();

        assertThat(converter.convert(42L)).isEqualTo((byte) 42);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testLongToByteOutOfRangeSilentlyTruncatesWithoutThrowing() {
        var converter = new DefaultLongToByteConverter();

        assertThat(converter.convert(200L)).isEqualTo((byte) -56);
    }

    @Test
    public void testBooleanToByte() {
        var converter = new DefaultBooleanToByteConverter();

        assertThat(converter.convert(Boolean.TRUE)).isEqualTo((byte) 1);
        assertThat(converter.convert(Boolean.FALSE)).isEqualTo((byte) 0);
    }

    @Test
    public void testBooleanToByteTreatsNullAsFalse() {
        // note the asymmetry with ByteToBoolean below: this converter never returns null.
        var converter = new DefaultBooleanToByteConverter();

        assertThat(converter.convert(null)).isEqualTo((byte) 0);
    }

    @Test
    public void testByteToBoolean() {
        var converter = new DefaultByteToBooleanConverter();

        assertThat(converter.convert((byte) 1)).isTrue();
        assertThat(converter.convert((byte) 0)).isFalse();
        assertThat(converter.convert((byte) 2)).isFalse();
    }

    @Test
    public void testByteToBooleanThrowsOnNullUnlikeItsInverseConverter() {
        // ByteToBoolean does not null-check its argument before unboxing it (from.byteValue()), so a null
        // input throws NullPointerException rather than returning a default value.
        var converter = new DefaultByteToBooleanConverter();

        assertThatThrownBy(() -> converter.convert(null)).isInstanceOf(NullPointerException.class);
    }
}
