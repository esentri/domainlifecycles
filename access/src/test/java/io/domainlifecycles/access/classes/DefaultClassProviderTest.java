/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.access.classes;


import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.access.exception.DLCAccessException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
        DLCAccessException exception = assertThrows(DLCAccessException.class,
            () -> classProvider.getClassForName("someUnknownClass"));
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
