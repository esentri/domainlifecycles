package io.domainlifecycles.assertion;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsNotBlank {

    @Nested
    class TestIsNotBlank {

        @Test
        public void testIsNotBlankOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNotBlank("a", "Failed"));
        }

        @Test
        public void testIsNotBlankFail() {
            assertThatThrownBy(() -> DomainAssertions.isNotBlank(" ", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotBlankFailNull() {
            assertThatThrownBy(() -> DomainAssertions.isNotBlank(null, "Failed"));
        }
    }

    @Nested
    class TestOptionalIsNotBlank {

        @Test
        public void testOptionalIsNotBlankOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsNotBlank(Optional.of("c"), "Failed"));
        }

        @Test
        public void testOptionalIsNotBlankOkEmptyOptional() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsNotBlank(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsNotBlankFailEmptyString() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsNotBlank(Optional.of(""), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsNotBlankFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsNotBlank(null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
