package io.domainlifecycles.assertion;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsEmail {

    @Nested
    class TestIsValidEmail {

        @Test
        public void testIsValidEmailOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isValidEmail("a@b.de", "Failed"));
        }

        @Test
        public void testIsValidEmailFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isValidEmail("aaaa-at-be.de", "Failed"));
        }

        @Test
        public void testIsValidEmailOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isValidEmail(null, "Failed"));
        }
    }

    @Nested
    class TestOptionalIsValidEmail {

        @Test
        public void testOptionalIsValidEmailOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsValidEmail(Optional.of("c@d.com"), "Failed"));
        }

        @Test
        public void testOptionalIsValidEmailOkEmptyOptional(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsValidEmail(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsValidEmailFailEmptyString(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsValidEmail(Optional.of(""), "Failed"));
        }

        @Test
        public void testOptionalIsValidEmailFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsValidEmail(null, "Failed"));
        }
    }
}
