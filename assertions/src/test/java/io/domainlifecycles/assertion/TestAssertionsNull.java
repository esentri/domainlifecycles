package io.domainlifecycles.assertion;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsNull {

    @Nested
    class TestIsNull {

        @Test
        public void testIsNullOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNull(null, "Failed"));
        }

        @Test
        public void testIsNullFail(){
            assertThatThrownBy(()-> DomainAssertions.isNull("", "Failed")).isInstanceOf(DomainAssertionException.class);
        }

    }

    @Nested
    class TestIsNotNull {

        @Test
        public void testIsNotNullOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.isNotNull(1l, "Failed"));
        }

        @Test
        public void testIsNotNullFail(){
            assertThatThrownBy(()-> DomainAssertions.isNotNull(null, "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }
}
