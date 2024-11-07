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

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsArraySize {

    @Nested
    class TestOptionalArrayHasSize {

        @Test
        public void testHasSizeOptionalArrayOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalArrayHasSize(Optional.of(new String[3]), 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayNullFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalArrayHasSize(null, 2, 3, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testHasSizeOptionalArrayEmptyOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalArrayHasSize(Optional.empty(), 2, 3, "Failed"));
        }
    }

    @Nested
    class TestOptionalArrayHasSizeMin {

        @Test
        public void testHasSizeOptionalArrayMinOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalArrayHasSizeMin(Optional.of(new Integer[2]), 2, "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayMinNullFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalArrayHasSizeMin(null, 2, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testHasSizeOptionalArrayMinEmptyOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalArrayHasSizeMin(Optional.empty(), 2, "Failed"));
        }
    }

    @Nested
    class TestOptionalArrayHasSizeMax {

        @Test
        public void testHasSizeOptionalArrayMaxOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalArrayHasSizeMax(Optional.of(new String[2]), 3, "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayMaxFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalArrayHasSizeMax(Optional.of(new String[2]), 1, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeArrayMaxNullFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalArrayHasSizeMax(null, 1, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testHasSizeArrayMaxEmptyOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalArrayHasSizeMax(Optional.empty(), 2, "Failed"));
        }
    }
}
