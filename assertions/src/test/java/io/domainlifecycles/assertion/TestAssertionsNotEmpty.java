package io.domainlifecycles.assertion;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsNotEmpty {

    @Nested
    class TestIsNotEmpty {

        @Test
        public void testIsNotEmptyStringOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotEmpty("String", "Failed"));
        }

        @Test
        public void testIsNotEmptyStringFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmpty("", "Failed"));
        }

        @Test
        public void testIsNotEmptyStringFailNull(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmpty(null, "Failed"));
        }
    }

    @Nested
    class TestIsNotEmptyIterable {

        @Test
        public void testIsNotEmptyIterableArrayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotEmptyIterable(new String[1], "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableArrayFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable(new String[0], "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableArrayFailNull(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable((Object[])null, "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableCollectionOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotEmptyIterable(List.of("a", "b"), "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableCollectionFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable(List.of(), "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableCollectionFailNull(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable((Collection)null, "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableMapOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isNotEmptyIterable(Map.of("a", "b"), "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableMapFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable(Map.of(), "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableMapFailNull(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isNotEmptyIterable((Map)null, "Failed"));
        }
    }

    @Nested
    class TestOptionalIsNotEmpty {

        @Test
        public void testOptionalIsNotEmptyStringOkEmptyOptional(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNotEmpty(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsNotEmptyStringFailEmptyString(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsNotEmpty(Optional.of(""), "Failed"));
        }

        @Test
        public void testOptionalIsNotEmptyStringFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsNotEmpty(null, "Failed"));
        }

        @Test
        public void testOptionalIsNotEmptyStringOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsNotEmpty(Optional.of("String"), "Failed"));
        }
    }
}
