package io.domainlifecycles.assertion;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAssertionsEquals {

    @Nested
    class TestEquals {

        @Test
        void testObjectEqualsFailCustomException() {

            // org.junit.jupiter.api.assertThrows -> preferred lib?
            final var exception = assertThrows(
                CustomAssertionException.class,
                () -> DomainAssertionsEquals.equals(
                    "A",
                    "B",
                    CustomAssertionExceptions.defaultCustomAssertionException()
                ));

            assertThat(exception.exceptionCode())
                .isEqualTo(CustomAssertionExceptionCode.CUSTOM_ASSERTION_EXCEPTION_CODE_1);

            // this method is changed in a new assertj version, reversing the parameters.
            // we should decide, which api we want to use. org.junit.jupiter or org.assertj
            // org.assertj.core.api.catchThrowableOfType
            final var exception2 = catchThrowableOfType(
                () -> DomainAssertionsEquals.equals("A", "B", CustomAssertionExceptions.defaultCustomAssertionException()),
                CustomAssertionException.class
            );
            assertThat(exception2.exceptionCode())
                .isEqualTo(CustomAssertionExceptionCode.CUSTOM_ASSERTION_EXCEPTION_CODE_1);
        }

        @Test
        void testObjectEqualsFail() {
            // org.assertj.core.api.assertThatThrownBy -> replace with org.junit.jupiter.api.assertThrows
            assertThatThrownBy(() -> DomainAssertionsEquals.equals("A", "B", "Failed"))
                .isInstanceOf(DomainAssertionException.class);
        }

        @Test
        void testObjectEqualsNullFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.equals(null, "B", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testObjectEqualsNullCompareFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.equals("A", null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testObjectEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.equals("A", "A", "Failed"));
        }

        @Test
        void testObjectEqualsBothNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.equals(null, null, "Failed"));
        }

        @Test
        void testIntEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.equals(5, 6, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testIntEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.equals(3, 3, "Failed"));
        }

        @Test
        void testLongEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.equals(5l, 6l, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testLongEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.equals(3l, 3l, "Failed"));
        }

        @Test
        void testByteEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.equals((byte) 5, (byte) 6, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testByteEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.equals((byte) 3, (byte) 3, "Failed"));
        }

        @Test
        void testShortEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.equals((short) 5, (short) 6, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testShortEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.equals((short) 3, (short) 3, "Failed"));
        }

        @Test
        void testDoubleEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.equals(5.0, 6.0, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testDoubleEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.equals(3.0, 3.0, "Failed"));
        }

        @Test
        void testFloatEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.equals((float) 5.0, (float) 6.0, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testFloatEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.equals((float) 3.0, (float) 3.0, "Failed"));
        }

        @Test
        void testCharEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.equals('a', 'b', "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testCharEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.equals('x', 'x', "Failed"));
        }

        @Test
        void testBooleanEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.equals(true, false, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testBooleanEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.equals(false, false, "Failed"));
        }
    }

    @Nested
    class TestNotEquals {

        @Test
        void testObjectNotEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.notEquals("A", "A", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testObjectNotEqualsBothNullFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.notEquals(null, null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testObjectNotEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals("A", "A1", "Failed"));
        }

        @Test
        void testObjectNotEqualsNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals(null, "A1", "Failed"));
        }

        @Test
        void testObjectNotEqualsNullParameterOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals("A", null, "Failed"));
        }

        @Test
        void testIntNotEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.notEquals(5, 5, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testIntNotEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals(4, 3, "Failed"));
        }

        @Test
        void testLongNotNotEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.notEquals(8l, 8l, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testLongNotEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals(2l, 3l, "Failed"));
        }

        @Test
        void testByteNotEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.notEquals((byte) 6, (byte) 6, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testByteNotEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals((byte) 5, (byte) 3, "Failed"));
        }

        @Test
        void testShortNotEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.notEquals((short) 6, (short) 6, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testShortNotEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals((short) 6, (short) 3, "Failed"));
        }

        @Test
        void testDoubleNotEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.notEquals(6.0, 6.0, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testDoubleNotEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals(5.0, 3.0, "Failed"));
        }

        @Test
        void testFloatNotEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.notEquals((float) 5.0, (float) 5.0, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testFloatNotEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals((float) 3.0, (float) 4.0, "Failed"));
        }

        @Test
        void testCharNotEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.notEquals('a', 'a', "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testCharNotEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals('x', 'f', "Failed"));
        }

        @Test
        void testBooleanNotEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.notEquals(true, true, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testBooleanNotEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.notEquals(false, true, "Failed"));
        }
    }

    @Nested
    class TestOptionalEquals {

        @Test
        void testOptionalObjectEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.optionalEquals(Optional.of("A"), "B", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testOptionalObjectEqualsEmptyFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.optionalEquals(Optional.empty(), "B", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testOptionalObjectEqualsFirstArgNullFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.optionalEquals(null, "B", "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        void testOptionalObjectEqualsEmptyOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.optionalEquals(Optional.empty(), null, "Failed"));
        }

        @Test
        void testOptionalObjectEqualsOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertionsEquals.optionalEquals(Optional.of("A"), "A", "Failed"));
        }

        @Test
        void testOptionalObjectEqualsSecondArgNullOk() {
            assertThatThrownBy(() -> DomainAssertionsEquals.optionalEquals(Optional.of("A"), null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalNotEquals {

        @Test
        void testOptionalObjectNotEqualsFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.optionalNotEquals(Optional.of("A"), "A", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        void testOptionalObjectNotEqualsEmptyFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.optionalNotEquals(Optional.empty(), null, "Failed"));
        }

        @Test
        void testOptionalObjectNotEqualsOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertionsEquals.optionalNotEquals(Optional.of("A"), "Y", "Failed"));
        }

        @Test
        void testOptionalObjectNotEqualsEmptyOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertionsEquals.optionalNotEquals(Optional.empty(), "Y", "Failed"));
        }

        @Test
        void testOptionalObjectNotEqualsNullFail() {
            assertThatThrownBy(() -> DomainAssertionsEquals.optionalNotEquals(null, "A", "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
