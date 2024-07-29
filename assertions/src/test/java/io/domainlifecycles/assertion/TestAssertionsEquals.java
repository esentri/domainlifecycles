package io.domainlifecycles.assertion;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsEquals {

    @Nested
    class TestEquals {

        @Test
        public void testObjectEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals("A", "B", "Failed"));
        }

        @Test
        public void testObjectEqualsNullFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals(null, "B", "Failed"));
        }

        @Test
        public void testObjectEqualsNullCompareFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals("A", null, "Failed"));
        }

        @Test
        public void testObjectEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.equals("A", "A", "Failed"));
        }

        @Test
        public void testObjectEqualsBothNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.equals(null, null, "Failed"));
        }

        @Test
        public void testIntEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals(5, 6, "Failed"));
        }

        @Test
        public void testIntEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.equals(3, 3, "Failed"));
        }

        @Test
        public void testLongEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals(5l, 6l, "Failed"));
        }

        @Test
        public void testLongEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.equals(3l, 3l, "Failed"));
        }

        @Test
        public void testByteEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals((byte)5, (byte)6, "Failed"));
        }

        @Test
        public void testByteEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.equals((byte)3, (byte)3, "Failed"));
        }

        @Test
        public void testShortEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals((short)5, (short)6, "Failed"));
        }

        @Test
        public void testShortEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.equals((short)3, (short)3, "Failed"));
        }

        @Test
        public void testDoubleEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals(5.0, 6.0, "Failed"));
        }

        @Test
        public void testDoubleEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.equals(3.0, 3.0, "Failed"));
        }

        @Test
        public void testFloatEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals((float)5.0, (float)6.0, "Failed"));
        }

        @Test
        public void testFloatEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.equals((float)3.0, (float)3.0, "Failed"));
        }

        @Test
        public void testCharEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals('a', 'b', "Failed"));
        }

        @Test
        public void testCharEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.equals('x', 'x', "Failed"));
        }

        @Test
        public void testBooleanEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.equals(true, false, "Failed"));
        }

        @Test
        public void testBooleanEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.equals(false, false, "Failed"));
        }
    }

    @Nested
    class TestNotEquals {

        @Test
        public void testObjectNotEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals("A", "A", "Failed"));
        }

        @Test
        public void testObjectNotEqualsBothNullFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals(null, null, "Failed"));
        }

        @Test
        public void testObjectNotEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals("A", "A1", "Failed"));
        }

        @Test
        public void testObjectNotEqualsNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals(null, "A1", "Failed"));
        }

        @Test
        public void testObjectNotEqualsNullParameterOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals("A", null, "Failed"));
        }

        @Test
        public void testIntNotEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals(5, 5, "Failed"));
        }

        @Test
        public void testIntNotEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals(4, 3, "Failed"));
        }

        @Test
        public void testLongNotNotEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals(8l, 8l, "Failed"));
        }

        @Test
        public void testLongNotEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals(2l, 3l, "Failed"));
        }

        @Test
        public void testByteNotEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals((byte)6, (byte)6, "Failed"));
        }

        @Test
        public void testByteNotEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals((byte)5, (byte)3, "Failed"));
        }

        @Test
        public void testShortNotEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals((short)6, (short)6, "Failed"));
        }

        @Test
        public void testShortNotEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals((short)6, (short)3, "Failed"));
        }

        @Test
        public void testDoubleNotEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals(6.0, 6.0, "Failed"));
        }

        @Test
        public void testDoubleNotEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals(5.0, 3.0, "Failed"));
        }

        @Test
        public void testFloatNotEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals((float)5.0, (float)5.0, "Failed"));
        }

        @Test
        public void testFloatNotEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals((float)3.0, (float)4.0, "Failed"));
        }

        @Test
        public void testCharNotEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals('a', 'a', "Failed"));
        }

        @Test
        public void testCharNotEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals('x', 'f', "Failed"));
        }

        @Test
        public void testBooleanNotEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.notEquals(true, true, "Failed"));
        }

        @Test
        public void testBooleanNotEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.notEquals(false, true, "Failed"));
        }
    }

    @Nested
    class TestOptionalEquals {

        @Test
        public void testOptionalObjectEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalEquals(Optional.of("A"), "B", "Failed"));
        }

        @Test
        public void testOptionalObjectEqualsEmptyFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalEquals(Optional.empty(), "B", "Failed"));
        }

        @Test
        public void testOptionalObjectEqualsNullFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalEquals(null, "B", "Failed"));
        }

        @Test
        public void testOptionalObjectEqualsEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalEquals(Optional.empty(), null, "Failed"));
        }

        @Test
        public void testOptionalObjectEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalEquals(Optional.of("A"), "A", "Failed"));
        }
    }

    @Nested
    class TestOptionalNotEquals {

        @Test
        public void testOptionalObjectNotEqualsFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalNotEquals(Optional.of("A"), "A", "Failed"));
        }

        @Test
        public void testOptionalObjectNotEqualsEmptyFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalNotEquals(Optional.empty(), null, "Failed"));
        }

        @Test
        public void testOptionalObjectNotEqualsOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalNotEquals(Optional.of("A"), "Y", "Failed"));
        }

        @Test
        public void testOptionalObjectNotEqualsEmptyOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalNotEquals(Optional.empty(), "Y", "Failed"));
        }

        @Test
        public void testOptionalObjectNotEqualsNullFail(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalNotEquals(null, "A", "Failed"));
        }
    }
}
