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
}
