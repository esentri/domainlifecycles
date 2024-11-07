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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestIsAfterOrEqual {

    @Nested
    class TestIsAfterOrEqualTo {

        @Test
        public void testIsAfterOrEqualToLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateOkNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfterOrEqualTo(LocalDate.now().minusDays(1), LocalDate.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeOkNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfterOrEqualTo(LocalTime.now().minusHours(1), LocalTime.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsAfterOrEqualTo {

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDate.now().plusDays(1)),
                    LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDate.now().minusDays(1)),
                    LocalDate.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalDate.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalTime.now().plusHours(1)),
                    LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalTime.now().minusHours(1)),
                    LocalTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
