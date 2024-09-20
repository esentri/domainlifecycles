package io.domainlifecycles.assertion;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestAssertionsArraySize {

    @Nested
    class TestOptionalArrayHasSize {

        @Test
        public void testHasSizeOptionalArrayOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalArrayHasSize(Optional.of(new String[3]), 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayNullFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalArrayHasSize(null, 2, 3, "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testHasSizeOptionalArrayEmptyOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalArrayHasSize(Optional.empty(), 2, 3, "Failed"));
        }
    }

    @Nested
    class TestOptionalArrayHasSizeMin {

        @Test
        public void testHasSizeOptionalArrayMinOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalArrayHasSizeMin(Optional.of(new Integer[2]), 2, "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayMinNullFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalArrayHasSizeMin(null, 2,  "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testHasSizeOptionalArrayMinEmptyOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalArrayHasSizeMin(Optional.empty(), 2,  "Failed"));
        }
    }

    @Nested
    class TestOptionalArrayHasSizeMax {

        @Test
        public void testHasSizeOptionalArrayMaxOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalArrayHasSizeMax(Optional.of(new String[2]), 3, "Failed"));
        }

        @Test
        public void testHasSizeOptionalArrayMaxFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalArrayHasSizeMax(Optional.of(new String[2]), 1, "Failed")).isInstanceOf(DomainAssertionException.class);
        }

        @Test
        public void testHasSizeArrayMaxNullFail(){
            assertThatThrownBy(()-> DomainAssertions.optionalArrayHasSizeMax(null, 1,  "Failed")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        public void testHasSizeArrayMaxEmptyOk(){
            assertThatNoException().isThrownBy(()-> DomainAssertions.optionalArrayHasSizeMax(Optional.empty(), 2,  "Failed"));
        }
    }
}
