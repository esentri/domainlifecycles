package io.domainlifecycles.assertion;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsSize {

    @Nested
    class TestHasSize {

        @Test
        public void testHasSizeOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSize(List.of(1, 2, 3), 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeShortFail() {
            assertThatThrownBy(() -> DomainAssertions.hasSize(List.of(1), 2, 3, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeLongFail() {
            assertThatThrownBy(
                () -> DomainAssertions.hasSize(List.of("abdbddb", 1, 3, 4, 5, 5), 2, 3, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSize((Collection) null, 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeMapOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSize(Map.of(1, 2, 3, 4), 2, 3, "Failed"));
        }

        @Test
        public void testHasMapShortFail() {
            assertThatThrownBy(() -> DomainAssertions.hasSize(Map.of(1, 2), 2, 3, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeMapLongFail() {
            assertThatThrownBy(
                () -> DomainAssertions.hasSize(Map.of("abdbddb", 1, 3, 4, 5, 5), 1, 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeMapNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSize((Map) null, 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeArrayOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSize(new String[3], 2, 3, "Failed"));
        }

        @Test
        public void testHasArrayShortFail() {
            assertThatThrownBy(() -> DomainAssertions.hasSize(new Long[1], 2, 3, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeArrayLongFail() {
            assertThatThrownBy(() -> DomainAssertions.hasSize(new Date[8], 1, 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeArrayNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSize((Object[]) null, 2, 3, "Failed"));
        }
    }

    @Nested
    class TestHasSizeMin {

        @Test
        public void testHasSizeMinOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMin(List.of(1, 2, 3), 2, "Failed"));
        }

        @Test
        public void testHasSizeMinShortFail() {
            assertThatThrownBy(() -> DomainAssertions.hasSizeMin(List.of(1), 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeMinNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMin((Collection) null, 2, "Failed"));
        }

        @Test
        public void testHasSizeMapMinOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMin(Map.of(1, 2, 3, 4), 2, "Failed"));
        }

        @Test
        public void testHasSizeMapMinShortFail() {
            assertThatThrownBy(() -> DomainAssertions.hasSizeMin(Map.of(1, 2), 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeMapMinNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMin((Map) null, 2, "Failed"));
        }

        @Test
        public void testHasSizeArrayMinOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMin(new Integer[2], 2, "Failed"));
        }

        @Test
        public void testHasSizeArrayMinShortFail() {
            assertThatThrownBy(() -> DomainAssertions.hasSizeMin(new Short[1], 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeArrayMinNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMin((String[]) null, 2, "Failed"));
        }
    }

    @Nested
    class TestHasSizeMax {

        @Test
        public void testHasSizeMaxOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMax(List.of(1, 2, 3), 3, "Failed"));
        }

        @Test
        public void testHasSizeMaxLongFail() {
            assertThatThrownBy(() -> DomainAssertions.hasSizeMax(List.of(1, 45, 6), 2, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeMaxNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMax((Collection) null, 2, "Failed"));
        }

        @Test
        public void testHasSizeMapMaxOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMax(Map.of(1, 2, 3, 2), 3, "Failed"));
        }

        @Test
        public void testHasSizeMapMaxLongFail() {
            assertThatThrownBy(() -> DomainAssertions.hasSizeMax(Map.of(1, 45, 6, 6), 1, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeMapMaxNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMax((Map) null, 2, "Failed"));
        }

        @Test
        public void testHasSizeArrayMaxOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMax(new String[2], 3, "Failed"));
        }

        @Test
        public void testHasSizeArrayMaxLongFail() {
            assertThatThrownBy(() -> DomainAssertions.hasSizeMax(new String[2], 1, "Failed")).isInstanceOf(
                DomainAssertionException.class);
        }

        @Test
        public void testHasSizeArrayMaxNullOk() {
            assertThatNoException().isThrownBy(() -> DomainAssertions.hasSizeMax((String[]) null, 2, "Failed"));
        }
    }
}
