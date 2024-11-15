package io.domainlifecycles.assertion;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsLength {

    @Nested
    class TestHasLengthMax {

        @Test
        public void testHasLengthMaxOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasLengthMax("abc", 3, "Failed"));
        }

        @Test
        public void testHasLengthMaxFail() {
            assertThatThrownBy(() -> DomainAssertions.hasLengthMax("abc", 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasLengthMaxNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasLengthMax(null, 3, "Failed"));
        }
    }

    @Nested
    class TestHasLengthMin {

        @Test
        public void testHasLengthMinOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasLengthMin("abc", 3, "Failed"));
        }

        @Test
        public void testHasLengthMinFail() {
            assertThatThrownBy(() -> DomainAssertions.hasLengthMin("a", 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasLengthMinNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasLengthMin(null, 3, "Failed"));
        }
    }


    @Nested
    class TestOptionalHasLengthMax {

        @Test
        public void testOptionalHasLengthMaxOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalHasLengthMax(Optional.of("abc"), 3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthMaxFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalHasLengthMax(Optional.of("abc"), 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasLengthMaxEmptyOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalHasLengthMax(Optional.empty(), 3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthMaxNullFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalHasLengthMax(null, 2, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }

    @Nested
    class TestOptionalHasLengthMin {

        @Test
        public void testOptionalHasLengthMinOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalHasLengthMin(Optional.of("abc"), 3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthMinFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalHasLengthMin(Optional.of("a"), 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasLengthMinEmptyOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalHasLengthMin(Optional.empty(), 3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthMinNullFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalHasLengthMin(null, 2, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }

    @Nested
    class TestHasLength {

        @Test
        public void testHasLengthOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasLength("abc", 2, 3, "Failed"));
        }

        @Test
        public void testHasLengthShortFail() {
            assertThatThrownBy(() -> DomainAssertions.hasLength("a", 2, 3, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasLengthLongFail() {
            assertThatThrownBy(() -> DomainAssertions.hasLength("abdbddb", 2, 3, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasLengthNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasLength(null, 2, 3, "Failed"));
        }
    }

    @Nested
    class TestOptionalHasLength {

        @Test
        public void testOptionalHasLengthOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalHasLength(Optional.of("abc"), 2, 3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalHasLength(Optional.of("a"), 2, 3, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalHasLengthEmptyOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalHasLength(Optional.empty(), 2, 3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthNullFail() {
            assertThatThrownBy(() -> DomainAssertions.optionalHasLength(null, 2, 3, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
