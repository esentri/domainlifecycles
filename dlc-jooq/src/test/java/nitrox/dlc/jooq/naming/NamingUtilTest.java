/*
 *
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

package nitrox.dlc.jooq.naming;

import nitrox.dlc.jooq.util.NamingUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


public class NamingUtilTest {

    @Test
    public void testSnakeToCamel() {
        var snake = "this_is_a_test";
        var camel = NamingUtil.snakeCaseToCamelCase(snake);
        Assertions.assertThat(camel).isEqualTo("thisIsATest");
    }

    @Test
    public void testSnakeToCamelSimple() {
        var snake = "this";
        var camel = NamingUtil.snakeCaseToCamelCase(snake);
        Assertions.assertThat(camel).isEqualTo("this");
    }

    @Test
    public void testCamelToSnake() {
        var snake = "thisIsATest";
        var camel = NamingUtil.camelCaseToSnakeCase(snake);
        Assertions.assertThat(camel).isEqualTo("this_is_a_test");
    }

    @Test
    public void testCamelToSnakeSimple() {
        var snake = "this";
        var camel = NamingUtil.camelCaseToSnakeCase(snake);
        Assertions.assertThat(camel).isEqualTo("this");
    }

    @Test
    public void testCamelToSnakeUpperStart() {
        var snake = "ThisIsATest";
        var camel = NamingUtil.camelCaseToSnakeCase(snake);
        Assertions.assertThat(camel).isEqualTo("this_is_a_test");
    }
}
