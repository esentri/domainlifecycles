package io.domainlifecycles.assertion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestIsAfterOrEqual {

    @Nested
    class TestIsAfterOrEqualTo {

        @Test
        public void testIsAfterOrEqualToLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(LocalDate.now().plusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(LocalDate.now().minusDays(1), LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo(LocalTime.now().plusHours(1), LocalTime.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeOkNull(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.isAfterOrEqualTo((LocalDate) null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testIsAfterOrEqualToLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.isAfterOrEqualTo(LocalTime.now().minusHours(1), LocalTime.now(), "Failed"));
        }
    }

    @Nested
    class TestOptionalIsAfterOrEqualTo {

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDate.now().plusDays(1)), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalDate.now().minusDays(1)), LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalDateFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalDate.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalTime.now().plusHours(1)), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeOkEmpty(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.empty(), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(Optional.of(LocalTime.now().minusHours(1)), LocalTime.now(), "Failed"));
        }

        @Test
        public void testOptionalIsAfterOrEqualToLocalTimeFailNull(){
            Assertions.assertThrows(IllegalArgumentException.class, ()-> DomainAssertions.optionalIsAfterOrEqualTo(null, LocalTime.now(), "Failed"));
        }
    }
}
