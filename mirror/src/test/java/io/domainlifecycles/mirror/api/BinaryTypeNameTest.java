package io.domainlifecycles.mirror.api;

import io.domainlifecycles.mirror.model.AssertedContainableTypeModel;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tests.mirror.ArrayFieldsEntity;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * The mirror reports the component type for array typed fields, the array characteristic being
 * exposed separately via {@link AssertedContainableTypeMirror#isArray()}. Everything that needs to
 * compare a mirrored type against {@link Class#getName()} - the auto record mapping in particular -
 * depends on {@link AssertedContainableTypeMirror#getBinaryTypeName()} reassembling the JVM name.
 */
public class BinaryTypeNameTest {

    @BeforeAll
    public static void init() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
    }

    private static AssertedContainableTypeMirror mirrorOfField(String fieldName) {
        return Domain.typeMirror(ArrayFieldsEntity.class.getName())
            .orElseThrow()
            .getAllFields()
            .stream()
            .filter(fm -> fm.getName().equals(fieldName))
            .findFirst()
            .orElseThrow()
            .getType();
    }

    private static Stream<Arguments> arrayFields() {
        return Stream.of(
            arguments("primitiveByteArray", byte.class, byte[].class),
            arguments("boxedByteArray", Byte.class, Byte[].class),
            arguments("primitiveIntArray", int.class, int[].class),
            arguments("primitiveLongArray", long.class, long[].class),
            arguments("primitiveShortArray", short.class, short[].class),
            arguments("primitiveCharArray", char.class, char[].class),
            arguments("primitiveBooleanArray", boolean.class, boolean[].class),
            arguments("primitiveFloatArray", float.class, float[].class),
            arguments("primitiveDoubleArray", double.class, double[].class),
            arguments("stringArray", String.class, String[].class)
        );
    }

    @ParameterizedTest
    @MethodSource("arrayFields")
    public void testArrayFieldReportsComponentTypeButBinaryArrayName(String fieldName,
                                                                     Class<?> expectedComponentType,
                                                                     Class<?> expectedArrayType) {
        var typeMirror = mirrorOfField(fieldName);

        assertThat(typeMirror.isArray()).isTrue();
        // documents the existing (lossy) contract of getTypeName()
        assertThat(typeMirror.getTypeName()).isEqualTo(expectedComponentType.getName());
        // the reassembled name must be usable wherever a Class#getName() is expected
        assertThat(typeMirror.getBinaryTypeName()).isEqualTo(expectedArrayType.getName());
    }

    @Test
    public void testNonArrayFieldsAreUnaffected() {
        assertThat(mirrorOfField("noArray").getBinaryTypeName()).isEqualTo(String.class.getName());
        assertThat(mirrorOfField("noArrayPrimitive").getBinaryTypeName()).isEqualTo(byte.class.getName());
        assertThat(mirrorOfField("noArray").getBinaryTypeName())
            .isEqualTo(mirrorOfField("noArray").getTypeName());
    }

    @Test
    public void testBinaryNamesAreLoadable() throws ClassNotFoundException {
        for (var fieldName : arrayFields().map(a -> (String) a.get()[0]).toList()) {
            assertThat(Class.forName(mirrorOfField(fieldName).getBinaryTypeName())).isNotNull();
        }
    }

    @Test
    public void testMultiDimensionalComponentNotationsBothSupported() {
        // AssertedContainableTypeMirrorBuilder and TypeMetaResolver disagree on how they spell the
        // component type of a multidimensional array - both notations must resolve identically.
        assertThat(model("[B", true).getBinaryTypeName()).isEqualTo(byte[][].class.getName());
        assertThat(model("byte[]", true).getBinaryTypeName()).isEqualTo(byte[][].class.getName());
    }

    private static AssertedContainableTypeMirror model(String typeName, boolean isArray) {
        return new AssertedContainableTypeModel(
            typeName,
            DomainType.NON_DOMAIN,
            List.of(),
            false,
            false,
            false,
            false,
            false,
            isArray,
            null,
            List.of(),
            null
        );
    }
}
