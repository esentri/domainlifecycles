package io.domainlifecycles.access.classes;


import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.access.exception.DLCAccessException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultClassProviderTest {

    @ParameterizedTest
    @MethodSource("provideClassArguments")
    <T> void testGetClassesSuccess(String className, Class<T> clazz) {
        ClassProvider classProvider = new DefaultClassProvider();
        Class<?> classForName = classProvider.getClassForName(className);

        assertThat(classForName).isEqualTo(clazz);
    }

    @Test
    void testGetClassFail() {
        ClassProvider classProvider = new DefaultClassProvider();
        DLCAccessException exception = assertThrows(DLCAccessException.class, () -> classProvider.getClassForName("someUnknownClass"));
        assertThat(exception).hasMessageContaining("could not be found");
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
