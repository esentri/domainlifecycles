package io.domainlifecycles.boot3.autoconfig.features.multiple.persistence_builder_jdbc;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers {@code dlc.features.persistence.jdbc.enabled=false}: reuses
 * {@link TestApplicationJdbcPersistenceAndBuilderAutoConfig}, which already excludes
 * {@code DlcJooqPersistenceAutoConfiguration} so JDBC is the only persistence backend on offer - disabling
 * JDBC too, by property this time instead of an exclude, must leave no persistence backend active at all.
 * Mirrors {@code dlc-spring-boot-autoconfig}'s test of the same name.
 */
@SpringBootTest(
    classes = TestApplicationJdbcPersistenceAndBuilderAutoConfig.class,
    properties = "dlc.features.persistence.jdbc.enabled=false")
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
class JdbcPersistenceDisabledByPropertyTest {

    @Autowired(required = false)
    private JdbcConnectionProvider jdbcConnectionProvider;

    @Autowired(required = false)
    private DomainPersistenceProvider<?> domainPersistenceProvider;

    @Test
    void testNoPersistenceBackendIsActive() {
        assertThat(jdbcConnectionProvider).isNull();
        assertThat(domainPersistenceProvider).isNull();
    }
}
