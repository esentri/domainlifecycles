package io.domainlifecycles.assertion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsNull {

    @Nested
    class TestIsNull {

        @Test
        public void testIsNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNull(null, "Failed"));
        }

        @Test
        public void testIsNullFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNull("", "Failed"));
        }

    }

    @Nested
    class TestIsNotNull {

        @Test
        public void testIsNotNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotNull(1l, "Failed"));
        }

        @Test
        public void testIsNotNullFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotNull(null, "Failed"));
        }
    }
}
