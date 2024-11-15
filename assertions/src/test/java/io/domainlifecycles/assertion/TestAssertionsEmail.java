package io.domainlifecycles.assertion;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsEmail {

    @Nested
    class TestIsValidEmail {

        @Test
        public void testIsValidEmailOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isValidEmail("a@b.de", "Failed"));
        }

        @Test
        public void testIsValidEmailFail() {
            assertThatThrownBy(() -> DomainAssertions.isValidEmail("aaaa-at-be.de", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsValidEmailOkNull() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isValidEmail(null, "Failed"));
        }
    }

    @Nested
    class TestOptionalIsValidEmail {

        @Test
        public void testOptionalIsValidEmailOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsValidEmail(Optional.of("c@d.com"), "Failed"));
        }

        @Test
        public void testOptionalIsValidEmailOkEmptyOptional() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsValidEmail(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsValidEmailFailEmptyString() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsValidEmail(Optional.of(""), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsValidEmailFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsValidEmail(null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
