package io.domainlifecycles.assertion;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsEquals {

    @Nested
    class TestEquals {

        @Test
        public void testObjectEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.equals("A", "B", "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testObjectEqualsNullFail(){
            assertThatThrownBy(()-> DomainAssertions.equals(null, "B", "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testObjectEqualsNullCompareFail(){
            assertThatThrownBy(()-> DomainAssertions.equals("A", null, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testObjectEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.equals("A", "A", "Failed"));
        }

        @Test
        public void testObjectEqualsBothNullOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.equals(null, null, "Failed"));
        }

        @Test
        public void testIntEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.equals(5, 6, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIntEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.equals(3, 3, "Failed"));
        }

        @Test
        public void testLongEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.equals(5l, 6l, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testLongEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.equals(3l, 3l, "Failed"));
        }

        @Test
        public void testByteEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.equals((byte)5, (byte)6, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testByteEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.equals((byte)3, (byte)3, "Failed"));
        }

        @Test
        public void testShortEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.equals((short)5, (short)6, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testShortEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.equals((short)3, (short)3, "Failed"));
        }

        @Test
        public void testDoubleEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.equals(5.0, 6.0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testDoubleEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.equals(3.0, 3.0, "Failed"));
        }

        @Test
        public void testFloatEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.equals((float)5.0, (float)6.0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testFloatEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.equals((float)3.0, (float)3.0, "Failed"));
        }

        @Test
        public void testCharEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.equals('a', 'b', "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testCharEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.equals('x', 'x', "Failed"));
        }

        @Test
        public void testBooleanEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.equals(true, false, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testBooleanEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.equals(false, false, "Failed"));
        }
    }

    @Nested
    class TestNotEquals {

        @Test
        public void testObjectNotEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.notEquals("A", "A", "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testObjectNotEqualsBothNullFail(){
            assertThatThrownBy(()-> DomainAssertions.notEquals(null, null, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testObjectNotEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals("A", "A1", "Failed"));
        }

        @Test
        public void testObjectNotEqualsNullOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals(null, "A1", "Failed"));
        }

        @Test
        public void testObjectNotEqualsNullParameterOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals("A", null, "Failed"));
        }

        @Test
        public void testIntNotEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.notEquals(5, 5, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIntNotEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals(4, 3, "Failed"));
        }

        @Test
        public void testLongNotNotEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.notEquals(8l, 8l, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testLongNotEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals(2l, 3l, "Failed"));
        }

        @Test
        public void testByteNotEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.notEquals((byte)6, (byte)6, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testByteNotEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals((byte)5, (byte)3, "Failed"));
        }

        @Test
        public void testShortNotEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.notEquals((short)6, (short)6, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testShortNotEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals((short)6, (short)3, "Failed"));
        }

        @Test
        public void testDoubleNotEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.notEquals(6.0, 6.0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testDoubleNotEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals(5.0, 3.0, "Failed"));
        }

        @Test
        public void testFloatNotEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.notEquals((float)5.0, (float)5.0, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testFloatNotEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals((float)3.0, (float)4.0, "Failed"));
        }

        @Test
        public void testCharNotEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.notEquals('a', 'a', "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testCharNotEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals('x', 'f', "Failed"));
        }

        @Test
        public void testBooleanNotEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.notEquals(true, true, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testBooleanNotEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.notEquals(false, true, "Failed"));
        }
    }

    @Nested
    class TestOptionalEquals {

        @Test
        public void testOptionalObjectEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalEquals(Optional.of("A"), "B", "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalObjectEqualsEmptyFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalEquals(Optional.empty(), "B", "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalObjectEqualsFirstArgNullFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalEquals(null, "B", "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testOptionalObjectEqualsEmptyOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalEquals(Optional.empty(), null, "Failed"));
        }

        @Test
        public void testOptionalObjectEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalEquals(Optional.of("A"), "A", "Failed"));
        }

        @Test
        public void testOptionalObjectEqualsSecondArgNullOk(){
            assertThatThrownBy(()-> DomainAssertions.optionalEquals(Optional.of("A"), null, "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalNotEquals {

        @Test
        public void testOptionalObjectNotEqualsFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalNotEquals(Optional.of("A"), "A", "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalObjectNotEqualsEmptyFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalNotEquals(Optional.empty(), null, "Failed"));
        }

        @Test
        public void testOptionalObjectNotEqualsOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalNotEquals(Optional.of("A"), "Y", "Failed"));
        }

        @Test
        public void testOptionalObjectNotEqualsEmptyOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalNotEquals(Optional.empty(), "Y", "Failed"));
        }

        @Test
        public void testOptionalObjectNotEqualsNullFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalNotEquals(null, "A", "Failed")).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
