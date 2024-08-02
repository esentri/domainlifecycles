package io.domainlifecycles.assertion;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsArraySize {

    @Nested
    class TestOptionalArrayHasSize {

        @Test
        public void testHasSizeOptionalArrayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSize(Optional.of(new String[3]), 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayNullFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalArrayHasSize(null, 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSize(Optional.empty(), 2, 3, "Failed"));
        }
    }

    @Nested
    class TestOptionalArrayHasSizeMin {

        @Test
        public void testHasSizeOptionalArrayMinOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSizeMin(Optional.of(new Integer[2]), 2, "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayMinNullFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalArrayHasSizeMin(null, 2,  "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayMinEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSizeMin(Optional.empty(), 2,  "Failed"));
        }
    }

    @Nested
    class TestOptionalArrayHasSizeMax {

        @Test
        public void testHasSizeOptionalArrayMaxOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSizeMax(Optional.of(new String[2]), 3, "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayMaxFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalArrayHasSizeMax(Optional.of(new String[2]), 1, "Failed"));
        }

        @Test
        public void testHasSizeArrayMaxNullFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalArrayHasSizeMax(null, 1,  "Failed"));
        }

        @Test
        public void testHasSizeArrayMaxEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalArrayHasSizeMax(Optional.empty(), 2,  "Failed"));
        }
    }
}
