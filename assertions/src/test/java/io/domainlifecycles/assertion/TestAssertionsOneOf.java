package io.domainlifecycles.assertion;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsOneOf {

    @Nested
    class TestIsOneOf {

        @Test
        public void testIsOneOfFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isOneOf("Z", List.of("A", "B", "C"), "Failed"));
        }

        @Test
        public void testIsOneOfOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isOneOf("A", List.of("A", "B", "C"), "Failed"));
        }

        @Test
        public void testIsOneOfNullCollectionFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.isOneOf("A", null, "Failed"));
        }

        @Test
        public void testIsOneOfNullObjectOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isOneOf(null, List.of("A", "B", "C"), "Failed"));
        }
    }

    @Nested
    class TestOptionalIsOneOf {

        @Test
        public void testOptionalIsOneOfFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsOneOf(Optional.of("Z"), List.of("A", "B", "C"), "Failed"));
        }

        @Test
        public void testOptionalIsOneOfOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsOneOf(Optional.of("A"), List.of("A", "B", "C"), "Failed"));
        }

        @Test
        public void testOptionalIsOneOfNullCollectionFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsOneOf(Optional.of("A"), null, "Failed"));
        }

        @Test
        public void testIsOptionalOneOfNullObjectFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsOneOf(null, List.of("A", "B", "C"), "Failed"));
        }
        @Test
        public void testIsOptionalOneOfEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsOneOf(Optional.empty(), List.of("A", "B", "C"), "Failed"));
        }
    }
}
