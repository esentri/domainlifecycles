package io.domainlifecycles.assertion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsBoolean {

    @Nested
    class TestIsTrue {

        @Test
        public void testIsTrueFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isTrue(false, "Failed"));
        }

        @Test
        public void testIsTrueOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isTrue(true ,"Failed"));
        }
    }

    @Nested
    class TestIsFalse {

        @Test
        public void testIsFalseFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isFalse(true, "Failed"));
        }

        @Test
        public void testIsFalseOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isFalse(false, "Failed"));
        }
    }
}
