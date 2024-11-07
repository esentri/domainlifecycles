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

public class TestAssertionsRegex {

    @Nested
    class TestRegEx {

        @Test
        public void testRegExFail() {
            assertThatThrownBy(() -> DomainAssertions.regEx("a", "[b]", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testRegExOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.regEx("a", "[a]", "Failed"));
        }
    }

    @Nested
    class TestOptionalRegEx {

        @Test
        public void testOptionalRegExFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalRegEx(Optional.of("a"), "[b]", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalRegExOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalRegEx(Optional.of("a"), "[a]", "Failed"));
        }

        @Test
        public void testOptionalRegExOkEmpty() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalRegEx(Optional.empty(), "[a]", "Failed"));
        }

        @Test
        public void testOptionalRegExFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalRegEx(null, "[b]", "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
