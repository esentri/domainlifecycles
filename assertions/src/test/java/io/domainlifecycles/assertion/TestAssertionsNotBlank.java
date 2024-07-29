package io.domainlifecycles.assertion;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsNotBlank {

    @Nested
    class TestIsNotBlank {

        @Test
        public void testIsNotBlankOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotBlank("a", "Failed"));
        }

        @Test
        public void testIsNotBlankFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotBlank(" ", "Failed"));
        }

        @Test
        public void testIsNotBlankFailNull(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotBlank(null, "Failed"));
        }
    }

    @Nested
    class TestOptionalIsNotBlank {

        @Test
        public void testOptionalIsNotBlankOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNotBlank(Optional.of("c"), "Failed"));
        }

        @Test
        public void testOptionalIsNotBlankOkEmptyOptional(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNotBlank(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsNotBlankFailEmptyString(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsNotBlank(Optional.of(""), "Failed"));
        }

        @Test
        public void testOptionalIsNotBlankFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsNotBlank(null, "Failed"));
        }
    }
}
