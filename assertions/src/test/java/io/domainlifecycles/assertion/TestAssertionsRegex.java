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
