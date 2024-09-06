package io.domainlifecycles.access.classes;


import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.access.exception.DLCAccessException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.assertj.core.api.Assertions;

class DefaultClassProviderTest {

    @ParameterizedTest
    @MethodSource("provideClassArguments")
    <T> void testGetClassesSuccess(String className, Class<T> clazz) {
        ClassProvider classProvider = new DefaultClassProvider();
        Class<?> classForName = classProvider.getClassForName(className);

        Assertions.assertThat(classForName).isEqualTo(clazz);
    }

    @Test
    void testGetClassFail() {
        ClassProvider classProvider = new DefaultClassProvider();
    }

    private static Stream<Arguments> provideClassArguments() {
        return Stream.of(
            Arguments.of("boolean", boolean.class),
            Arguments.of("byte", byte.class),
            Arguments.of("short", short.class),
            Arguments.of("int", int.class),
            Arguments.of("long", long.class),
            Arguments.of("float", float.class),
            Arguments.of("double", double.class),
            Arguments.of("char", char.class),
            Arguments.of("java.lang.String", String.class),
            Arguments.of("io.domainlifecycles.access.exception.DLCAccessException", DLCAccessException.class),
            Arguments.of("io.domainlifecycles.access.DlcAccess", DlcAccess.class)
            );
    }
}
