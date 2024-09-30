package io.domainlifecycles.assertion;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsNotEmpty {

    @Nested
    class TestIsNotEmpty {

        @Test
        public void testIsNotEmptyStringOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNotEmpty("String", "Failed"));
        }

        @Test
        public void testIsNotEmptyStringFail() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmpty("", "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyStringFailNull() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmpty(null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }
    }

    @Nested
    class TestIsNotEmptyIterable {

        @Test
        public void testIsNotEmptyIterableArrayOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNotEmptyIterable(new String[1], "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableArrayFail() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable(new String[0], "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyIterableArrayFailNull() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable((Object[]) null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyIterableCollectionOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNotEmptyIterable(List.of("a", "b"), "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableCollectionFail() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable(List.of(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyIterableCollectionFailNull() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable((Collection) null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyIterableMapOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.isNotEmptyIterable(Map.of("a", "b"), "Failed"));
        }

        @Test
        public void testIsNotEmptyIterableMapFail() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable(Map.of(), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testIsNotEmptyIterableMapFailNull() {
            assertThatThrownBy(() -> DomainAssertions.isNotEmptyIterable((Map) null, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsNotEmpty {

        @Test
        public void testOptionalIsNotEmptyStringOkEmptyOptional() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.optionalIsNotEmpty(Optional.empty(), "Failed"));
        }

        @Test
        public void testOptionalIsNotEmptyStringFailEmptyString() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsNotEmpty(Optional.of(""), "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsNotEmptyStringFailNull() {
            assertThatThrownBy(() -> DomainAssertions.optionalIsNotEmpty(null, "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsNotEmptyStringOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsNotEmpty(Optional.of("String"), "Failed"));
        }
    }
}
