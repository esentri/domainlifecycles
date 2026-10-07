package io.domainlifecycles.jdbc.imp.matcher;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTableToEntityTypeMatcherTest {

    private static final String ENTITY_TYPE_NAME = "tests.shared.persistence.domain.simple.TestRootSimple";

    private final JdbcTableToEntityTypeMatcher matcher = new JdbcTableToEntityTypeMatcher();

    @BeforeAll
    static void initDomain() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
    }

    @Test
    void matchesTableIgnoringUnderscores() {
        var matched = matcher.findMatchingTable(Set.of("TEST_ROOT_SIMPLE", "OTHER_TABLE"), ENTITY_TYPE_NAME);

        assertThat(matched).contains("TEST_ROOT_SIMPLE");
    }

    @Test
    void matchesRegardlessOfCasing() {
        var matched = matcher.findMatchingTable(Set.of("test_root_simple"), ENTITY_TYPE_NAME);

        assertThat(matched).contains("test_root_simple");
    }

    @Test
    void returnsEmptyWhenNoTableMatches() {
        var matched = matcher.findMatchingTable(Set.of("SOME_UNRELATED_TABLE"), ENTITY_TYPE_NAME);

        assertThat(matched).isEmpty();
    }
}
