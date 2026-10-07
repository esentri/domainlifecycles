package io.domainlifecycles.persistence.mapping.converter;

import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConverterRegistryTest {

    private static class StringToIntegerConverter extends TypeConverter<String, Integer> {
        StringToIntegerConverter() {
            super(String.class, Integer.class);
        }

        @Override
        public Integer convert(String from) {
            return Integer.valueOf(from);
        }
    }

    private static class IntegerToStringConverter extends TypeConverter<Integer, String> {
        IntegerToStringConverter() {
            super(Integer.class, String.class);
        }

        @Override
        public String convert(Integer from) {
            return String.valueOf(from);
        }
    }

    @Test
    public void testRegisterAndGetConverter() {
        var registry = new ConverterRegistry();
        var converter = new StringToIntegerConverter();
        registry.registerConverter(converter);

        var found = registry.getTypeConverter(String.class.getName(), Integer.class.getName());

        assertThat(found).isSameAs(converter);
    }

    @Test
    public void testDirectionMattersForLookup() {
        var registry = new ConverterRegistry();
        registry.registerConverter(new StringToIntegerConverter());
        registry.registerConverter(new IntegerToStringConverter());

        var stringToInt = registry.getTypeConverter(String.class.getName(), Integer.class.getName());
        var intToString = registry.getTypeConverter(Integer.class.getName(), String.class.getName());

        assertThat(stringToInt).isInstanceOf(StringToIntegerConverter.class);
        assertThat(intToString).isInstanceOf(IntegerToStringConverter.class);
    }

    @Test
    public void testUnknownConversionThrows() {
        var registry = new ConverterRegistry();

        assertThatThrownBy(() -> registry.getTypeConverter(String.class.getName(), Integer.class.getName()))
            .isInstanceOf(DLCPersistenceException.class)
            .hasMessageContaining(String.class.getName())
            .hasMessageContaining(Integer.class.getName());
    }

    @Test
    public void testRegisteringSamePairTwiceOverwritesThePreviousConverter() {
        var registry = new ConverterRegistry();
        var first = new StringToIntegerConverter();
        var second = new StringToIntegerConverter();
        registry.registerConverter(first);
        registry.registerConverter(second);

        var found = registry.getTypeConverter(String.class.getName(), Integer.class.getName());

        assertThat(found).isSameAs(second).isNotSameAs(first);
    }
}
