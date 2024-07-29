package io.domainlifecycles.assertion;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsRegex {

    @Nested
    class TestRegEx {

        @Test
        public void testRegExFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.regEx("a","[b]", "Failed"));
        }

        @Test
        public void testRegExOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.regEx("a", "[a]" ,"Failed"));
        }
    }

    @Nested
    class TestOptionalRegEx {

        @Test
        public void testOptionalRegExFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalRegEx(Optional.of("a"),"[b]", "Failed"));
        }

        @Test
        public void testOptionalRegExOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalRegEx(Optional.of("a"), "[a]" ,"Failed"));
        }

        @Test
        public void testOptionalRegExOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalRegEx(Optional.empty(), "[a]" ,"Failed"));
        }

        @Test
        public void testOptionalRegExFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalRegEx(null,"[b]", "Failed"));
        }
    }
}
