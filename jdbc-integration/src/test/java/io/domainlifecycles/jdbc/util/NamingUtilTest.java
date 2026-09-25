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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.jdbc.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class NamingUtilTest {

    @Test
    void testSnakeToCamel() {
        var snake = "this_is_a_test";
        var camel = NamingUtil.snakeCaseToCamelCase(snake);
        Assertions.assertThat(camel).isEqualTo("thisIsATest");
    }

    @Test
    void testSnakeToCamelSimple() {
        var snake = "this";
        var camel = NamingUtil.snakeCaseToCamelCase(snake);
        Assertions.assertThat(camel).isEqualTo("this");
    }

    @Test
    void testSnakeToCamelTrailingUnderscoreDoesNotThrow() {
        var snake = "foo_";
        var camel = NamingUtil.snakeCaseToCamelCase(snake);
        Assertions.assertThat(camel).isEqualTo("foo");
    }

    @Test
    void testSnakeToCamelDoubleUnderscoreCollapsesToOneWordBoundary() {
        var snake = "foo__bar";
        var camel = NamingUtil.snakeCaseToCamelCase(snake);
        Assertions.assertThat(camel).isEqualTo("fooBar");
    }

    @Test
    void testCamelToSnake() {
        var camel = "thisIsATest";
        var snake = NamingUtil.camelCaseToSnakeCase(camel);
        Assertions.assertThat(snake).isEqualTo("this_is_a_test");
    }

    @Test
    void testCamelToSnakeSimple() {
        var camel = "this";
        var snake = NamingUtil.camelCaseToSnakeCase(camel);
        Assertions.assertThat(snake).isEqualTo("this");
    }

    @Test
    void testCamelToSnakeUpperStart() {
        var camel = "ThisIsATest";
        var snake = NamingUtil.camelCaseToSnakeCase(camel);
        Assertions.assertThat(snake).isEqualTo("this_is_a_test");
    }
}
