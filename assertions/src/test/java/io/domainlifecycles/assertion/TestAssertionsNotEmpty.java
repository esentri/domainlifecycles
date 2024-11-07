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

package io.domainlifecycles.assertion;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsNotEmpty {

    @Nested
    class TestIsNotEmpty {

        @Test
        public void testIsNotEmptyStringOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNotEmpty("String", "Failed"));
        }

        @Test
        public void testIsNotEmptyStringFail() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmpty("", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyStringFailNull() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmpty(null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }
    }

    @Nested
    class TestIsNotEmptyIterable {

        @Test
        public void testIsNotEmptyIterableArrayOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNotEmptyIterable(new String[1], "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableArrayFail() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable(new String[0], "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyIterableArrayFailNull() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable((Object[]) null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyIterableCollectionOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNotEmptyIterable(List.of("a", "b"), "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableCollectionFail() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable(List.of(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyIterableCollectionFailNull() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable((Collection) null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyIterableMapOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNotEmptyIterable(Map.of("a", "b"), "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableMapFail() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable(Map.of(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyIterableMapFailNull() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable((Map) null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsNotEmpty {

        @Test
        public void testOptionalIsNotEmptyStringOkEmptyOptional() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsNotEmpty(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsNotEmptyStringFailEmptyString() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsNotEmpty(Optional.of(""), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsNotEmptyStringFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsNotEmpty(null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsNotEmptyStringOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNotEmpty(Optional.of("String"), "Failed"));
        }
    }
}
