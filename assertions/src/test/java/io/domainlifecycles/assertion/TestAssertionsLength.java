package io.domainlifecycles.assertion;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsLength {

    @Nested
    class TestHasLengthMax {

        @Test
        public void testHasLengthMaxOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLengthMax("abc", 3, "Failed"));
        }

        @Test
        public void testHasLengthMaxFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasLengthMax("abc", 2, "Failed"));
        }

        @Test
        public void testHasLengthMaxNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLengthMax(null, 3, "Failed"));
        }
    }

    @Nested
    class TestHasLengthMin {

        @Test
        public void testHasLengthMinOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLengthMin("abc", 3, "Failed"));
        }

        @Test
        public void testHasLengthMinFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasLengthMin("a", 2, "Failed"));
        }

        @Test
        public void testHasLengthMinNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLengthMin(null, 3, "Failed"));
        }
    }


    @Nested
    class TestOptionalHasLengthMax {

        @Test
        public void testOptionalHasLengthMaxOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLengthMax(Optional.of("abc"), 3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthMaxFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasLengthMax(Optional.of("abc"), 2, "Failed"));
        }

        @Test
        public void testOptionalHasLengthMaxEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLengthMax(Optional.empty(), 3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthMaxNullFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasLengthMax(null, 2, "Failed"));
        }
    }

    @Nested
    class TestOptionalHasLengthMin {

        @Test
        public void testOptionalHasLengthMinOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLengthMin(Optional.of("abc"), 3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthMinFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasLengthMin(Optional.of("a"), 2, "Failed"));
        }

        @Test
        public void testOptionalHasLengthMinEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLengthMin(Optional.empty(), 3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthMinNullFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasLengthMin(null, 2, "Failed"));
        }
    }

    @Nested
    class TestHasLength {

        @Test
        public void testHasLengthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLength("abc", 2, 3, "Failed"));
        }

        @Test
        public void testHasLengthShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasLength("a", 2, 3, "Failed"));
        }

        @Test
        public void testHasLengthLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasLength("abdbddb", 2, 3, "Failed"));
        }

        @Test
        public void testHasLengthNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasLength(null, 2, 3, "Failed"));
        }
    }

    @Nested
    class TestOptionalHasLength {

        @Test
        public void testOptionalHasLengthOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLength(Optional.of("abc"), 2,3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalHasLength(Optional.of("a"), 2,3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalHasLength(Optional.empty(), 2,3, "Failed"));
        }

        @Test
        public void testOptionalHasLengthNullFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalHasLength(null, 2, 3, "Failed"));
        }
    }
}
