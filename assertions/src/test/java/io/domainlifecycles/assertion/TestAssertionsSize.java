package io.domainlifecycles.assertion;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestAssertionsSize {

    @Nested
    class TestHasSize {

        @Test
        public void testHasSizeOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize(List.of(1,2,3), 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(List.of(1), 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(List.of("abdbddb",1,3,4,5,5), 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize((Collection) null, 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeMapOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize(Map.of(1,2,3,4), 2, 3, "Failed"));
        }

        @Test
        public void testHasMapShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(Map.of(1,2), 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeMapLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(Map.of("abdbddb",1,3,4,5,5), 1, 2, "Failed"));
        }

        @Test
        public void testHasSizeMapNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize((Map) null, 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeArrayOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize(new String[3], 2, 3, "Failed"));
        }

        @Test
        public void testHasArrayShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(new Long[1], 2, 3, "Failed"));
        }

        @Test
        public void testHasSizeArrayLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSize(new Date[8], 1, 2, "Failed"));
        }

        @Test
        public void testHasSizeArrayNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSize((Object[]) null, 2, 3, "Failed"));
        }
    }

    @Nested
    class TestHasSizeMin {

        @Test
        public void testHasSizeMinOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin(List.of(1,2,3), 2, "Failed"));
        }

        @Test
        public void testHasSizeMinShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMin(List.of(1), 2,  "Failed"));
        }

        @Test
        public void testHasSizeMinNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin((Collection) null, 2,  "Failed"));
        }

        @Test
        public void testHasSizeMapMinOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin(Map.of(1,2,3,4), 2, "Failed"));
        }

        @Test
        public void testHasSizeMapMinShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMin(Map.of(1,2), 2,  "Failed"));
        }

        @Test
        public void testHasSizeMapMinNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin((Map) null, 2,  "Failed"));
        }

        @Test
        public void testHasSizeArrayMinOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin(new Integer[2], 2, "Failed"));
        }

        @Test
        public void testHasSizeArrayMinShortFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMin(new Short[1], 2,  "Failed"));
        }

        @Test
        public void testHasSizeArrayMinNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMin((String[]) null, 2,  "Failed"));
        }
    }

    @Nested
    class TestHasSizeMax {

        @Test
        public void testHasSizeMaxOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax(List.of(1,2,3), 3, "Failed"));
        }

        @Test
        public void testHasSizeMaxLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMax(List.of(1,45,6), 2,  "Failed"));
        }

        @Test
        public void testHasSizeMaxNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax((Collection) null, 2,  "Failed"));
        }

        @Test
        public void testHasSizeMapMaxOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax(Map.of(1,2,3,2), 3, "Failed"));
        }

        @Test
        public void testHasSizeMapMaxLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMax(Map.of(1,45,6,6), 1,  "Failed"));
        }

        @Test
        public void testHasSizeMapMaxNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax((Map) null, 2,  "Failed"));
        }

        @Test
        public void testHasSizeArrayMaxOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax(new String[2], 3, "Failed"));
        }

        @Test
        public void testHasSizeArrayMaxLongFail(){
            Assertions.assertThrows(DomainAssertionException.class, ()-> DomainAssertions.hasSizeMax(new String[2], 1,  "Failed"));
        }

        @Test
        public void testHasSizeArrayMaxNullOk(){
            Assertions.assertDoesNotThrow(()-> DomainAssertions.hasSizeMax((String[]) null, 2,  "Failed"));
        }
    }
}
