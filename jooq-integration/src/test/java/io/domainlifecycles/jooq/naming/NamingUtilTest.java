package io.domainlifecycles.jooq.naming;

import io.domainlifecycles.jooq.util.NamingUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


public class NamingUtilTest {

    @Test
    public void testSnakeToCamel() {
        var snake = "this_is_a_test";
        var camel = NamingUtil.snakeCaseToCamelCase(snake);
        Assertions.assertThat(camel).isEqualTo("thisIsATest");
    }

    @Test
    public void testSnakeToCamelSimple() {
        var snake = "this";
        var camel = NamingUtil.snakeCaseToCamelCase(snake);
        Assertions.assertThat(camel).isEqualTo("this");
    }

    @Test
    public void testCamelToSnake() {
        var snake = "thisIsATest";
        var camel = NamingUtil.camelCaseToSnakeCase(snake);
        Assertions.assertThat(camel).isEqualTo("this_is_a_test");
    }

    @Test
    public void testCamelToSnakeSimple() {
        var snake = "this";
        var camel = NamingUtil.camelCaseToSnakeCase(snake);
        Assertions.assertThat(camel).isEqualTo("this");
    }

    @Test
    public void testCamelToSnakeUpperStart() {
        var snake = "ThisIsATest";
        var camel = NamingUtil.camelCaseToSnakeCase(snake);
        Assertions.assertThat(camel).isEqualTo("this_is_a_test");
    }

    @Test
    public void testSnakeToCamelTrailingUnderscoreDoesNotThrow() {
        var snake = "foo_";
        var camel = NamingUtil.snakeCaseToCamelCase(snake);
        Assertions.assertThat(camel).isEqualTo("foo");
    }

    @Test
    public void testSnakeToCamelDoubleUnderscoreCollapsesToOneWordBoundary() {
        var snake = "foo__bar";
        var camel = NamingUtil.snakeCaseToCamelCase(snake);
        Assertions.assertThat(camel).isEqualTo("fooBar");
    }

    @Test
    public void testCamelToSnakeDigitStaysAttachedToItsPrecedingSegment() {
        // matches io.domainlifecycles.jdbc.util.NamingUtil's behavior - both integrations must derive
        // the same sequence name from an Identity class name containing a digit (e.g. OrderIdBv3,
        // whose real sequence in the shared test migration schema is order_id_bv3_seq, glued together)
        var camel = "orderIdBv3";
        var snake = NamingUtil.camelCaseToSnakeCase(camel);
        Assertions.assertThat(snake).isEqualTo("order_id_bv3");
    }

    @Test
    public void testCamelToSnakeDigitAfterAWordBoundaryAlsoStaysAttached() {
        var camel = "testEntity2Id";
        var snake = NamingUtil.camelCaseToSnakeCase(camel);
        Assertions.assertThat(snake).isEqualTo("test_entity2_id");
    }

    @Test
    public void testIdentitySequenceNameOfTopLevelIdentity() {
        Assertions.assertThat(NamingUtil.identitySequenceName("com.example.OrderIdBv3")).isEqualTo("ORDER_ID_BV3_SEQ");
    }

    @Test
    public void testIdentitySequenceNameOfInnerIdentityIsPrefixedByEnclosingClass() {
        Assertions.assertThat(NamingUtil.identitySequenceName("com.example.Room$Id")).isEqualTo("ROOM_ID_SEQ");
        Assertions.assertThat(NamingUtil.identitySequenceName("com.example.Room$RoomNumber"))
            .isEqualTo("ROOM_ROOM_NUMBER_SEQ");
        Assertions.assertThat(NamingUtil.identitySequenceName("com.example.Hotel$Room$Id"))
            .isEqualTo("HOTEL_ROOM_ID_SEQ");
    }
}
