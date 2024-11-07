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

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsOneOf {

    @Nested
    class TestIsOneOf {

        @Test
        public void testIsOneOfFail() {
            assertThatThrownBy(() -> DomainAssertions.isOneOf("Z", List.of("A", "B", "C"), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsOneOfOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isOneOf("A", List.of("A", "B", "C"), "Failed"));
        }

        @Test
        public void testIsOneOfNullCollectionFail() {
            assertThatThrownBy(() -> DomainAssertions.isOneOf("A", null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testIsOneOfNullObjectOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isOneOf(null, List.of("A", "B", "C"), "Failed"));
        }
    }

    @Nested
    class TestOptionalIsOneOf {

        @Test
        public void testOptionalIsOneOfFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsOneOf(Optional.of("Z"), List.of("A", "B", "C"),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsOneOfOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsOneOf(Optional.of("A"), List.of("A", "B", "C"), "Failed"));
        }

        @Test
        public void testOptionalIsOneOfNullCollectionFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsOneOf(Optional.of("A"), null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testIsOptionalOneOfNullObjectFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsOneOf(null, List.of("A", "B", "C"), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testIsOptionalOneOfEmptyOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsOneOf(Optional.empty(), List.of("A", "B", "C"), "Failed"));
        }
    }
}
