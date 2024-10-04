package io.domainlifecycles.assertion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestIsAfterOrEqual {

    @Nested
    class TestIsAfterOrEqualTo {

        @Test
        public void testIsAfterOrEqualToLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateOkNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfterOrEqualTo(LocalDate.now().minusDays(1), LocalDate.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeOkNull() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.isAfterOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeFail() {
            assertThatThrownBy(() -> DomainAssertions.isAfterOrEqualTo(LocalTime.now().minusHours(1), LocalTime.now(),
                "Failed")).isInstanceOf(DomainAssertionException.class);
        }
    }

    @Nested
    class TestOptionalIsAfterOrEqualTo {

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDate.now().plusDays(1)),
                    LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDate.now().minusDays(1)),
                    LocalDate.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalDate.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeOk() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalTime.now().plusHours(1)),
                    LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeOkEmpty() {
            assertThatNoException().isThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeFail() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalTime.now().minusHours(1)),
                    LocalTime.now(), "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeFailNull() {
            assertThatThrownBy(
                () -> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalTime.now(), "Failed")).isInstanceOf(
                IllegalArgumentException.class);
        }
    }
}
