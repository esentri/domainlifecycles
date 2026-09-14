package io.domainlifecycles.persistence.mapping.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BoxTypeNameConverterTest {

    @ParameterizedTest
    @CsvSource({
        "long, java.lang.Long",
        "int, java.lang.Integer",
        "double, java.lang.Double",
        "byte, java.lang.Byte",
        "short, java.lang.Short",
        "float, java.lang.Float",
        "boolean, java.lang.Boolean",
        "char, java.lang.Character"
    })
    public void testConvertsEveryPrimitiveTypeNameToItsBoxedCounterpart(String primitiveName, String boxedName) {
        assertThat(BoxTypeNameConverter.convertToBoxedType(primitiveName)).isEqualTo(boxedName);
    }

    @Test
    public void testNonPrimitiveTypeNamesArePassedThroughUnchanged() {
        assertThat(BoxTypeNameConverter.convertToBoxedType("java.lang.String")).isEqualTo("java.lang.String");
        assertThat(BoxTypeNameConverter.convertToBoxedType("java.lang.Long")).isEqualTo("java.lang.Long");
        assertThat(BoxTypeNameConverter.convertToBoxedType("void")).isEqualTo("void");
    }
}
