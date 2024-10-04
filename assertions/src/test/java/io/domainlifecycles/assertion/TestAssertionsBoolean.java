package io.domainlifecycles.assertion;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsBoolean {

    @Nested
    class TestIsTrue {

        @Test
        public void testIsTrueFail() {
            assertThatThrownBy(() -> DomainAssertions.isTrue(false, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsTrueOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isTrue(true, "Failed"));
        }
    }

    @Nested
    class TestIsFalse {

        @Test
        public void testIsFalseFail() {
            assertThatThrownBy(() -> DomainAssertions.isFalse(true, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsFalseOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isFalse(false, "Failed"));
        }
    }
}
