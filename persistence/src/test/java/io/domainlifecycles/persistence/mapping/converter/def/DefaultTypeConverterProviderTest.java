package io.domainlifecycles.persistence.mapping.converter.def;

import io.domainlifecycles.persistence.mapping.converter.TypeConverter;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultTypeConverterProviderTest {

    @Test
    public void testProvidesAllDefaultConvertersFromTheDefConverterPackage() {
        var provider = new DefaultTypeConverterProvider();

        var converters = provider.provideConverters();

        assertThat(converters).isNotEmpty();
        assertThat(converters).allMatch(c -> c.getClass().getPackageName()
            .equals(DefaultTypeConverterProvider.DEFAULT_CONVERTERS_PACKAGE));
        // a representative sample of converters, one per data type family, must be present
        assertThat(converters).extracting(c -> c.getClass().getSimpleName())
            .contains(
                DefaultLongToIntConverter.class.getSimpleName(),
                DefaultBigIntegerToLongConverter.class.getSimpleName(),
                DefaultUuidToStringConverter.class.getSimpleName(),
                DefaultStringToUuidConverter.class.getSimpleName(),
                DefaultOffsetDateTimeToInstantConverter.class.getSimpleName(),
                DefaultYearMonthToIntegerConverter.class.getSimpleName()
            );
    }

    @Test
    public void testEveryConverterDeclaresAUniqueFromToTypePair() {
        // the ConverterRegistry keys converters by (fromClass, toClass): if two default converters declared
        // the same pair, one would silently shadow the other once registered - this guards against that.
        var provider = new DefaultTypeConverterProvider();
        var converters = provider.provideConverters();

        var distinctPairs = converters.stream()
            .map(c -> c.fromClass.getName() + "->" + c.toClass.getName())
            .collect(Collectors.toSet());

        assertThat(distinctPairs).hasSameSizeAs(converters);
    }

    @Test
    public void testEachCallReturnsFreshConverterInstances() {
        var provider = new DefaultTypeConverterProvider();

        var first = provider.provideConverters();
        var second = provider.provideConverters();

        assertThat(first).hasSameSizeAs(second);
        for (TypeConverter<?, ?> converterFromFirstCall : first) {
            assertThat(second).noneMatch(c -> c == converterFromFirstCall);
        }
    }
}
