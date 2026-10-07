package io.domainlifecycles.jdbc.imp;

import io.domainlifecycles.jdbc.connection.SingleJdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcEntityIdentityProviderTest {

    private Connection connection;
    private JdbcEntityIdentityProvider provider;

    @BeforeAll
    static void initDomain() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared.persistence.domain"));
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_entity_identity_provider_test_" + UUID.randomUUID());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE SEQUENCE TEST_ROOT_SIMPLE_ID_SEQ START WITH 1000");
        }
        provider = new JdbcEntityIdentityProvider(new SingleJdbcConnectionProvider(connection), new H2JdbcDialect());
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void generatesIncrementingIdsFromSequenceForLongIdentity() {
        var first = provider.provideFor(TestRootSimple.class.getName());
        var second = provider.provideFor(TestRootSimple.class.getName());

        assertThat(first.value()).isEqualTo(1000L);
        assertThat(second.value()).isEqualTo(1001L);
    }

    @Test
    void generatesRandomUuidForUuidIdentityWithoutDatabaseAccess() {
        var id = provider.provideFor(TestRootSimpleUuid.class.getName());

        assertThat(id.value()).isInstanceOf(UUID.class);
    }
}
