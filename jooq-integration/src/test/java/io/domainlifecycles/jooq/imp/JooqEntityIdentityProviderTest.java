package io.domainlifecycles.jooq.imp;

import io.domainlifecycles.jooq.nestedidentity.NestedIdentityRoot;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JooqEntityIdentityProviderTest {

    private Connection connection;
    private JooqEntityIdentityProvider provider;

    @BeforeAll
    static void initDomain() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.jooq.nestedidentity"));
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jooq_entity_identity_provider_test_" + UUID.randomUUID());
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE SEQUENCE NESTED_IDENTITY_ROOT_NESTED_IDENTITY_ROOT_ID_SEQ START WITH 2000");
        }
        provider = new JooqEntityIdentityProvider(DSL.using(connection, SQLDialect.H2));
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void resolvesSequenceOfNestedIdentityPrefixedByEnclosingClass() {
        var id = provider.provideFor(NestedIdentityRoot.class.getName());

        assertThat(id).isInstanceOf(NestedIdentityRoot.NestedIdentityRootId.class);
        assertThat(id.value()).isEqualTo(2000L);
    }
}
