package io.domainlifecycles.jdbc.configuration;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.connection.SingleJdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.cache.NoOpTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers design section 8 of {@code persistence/docs/transaction-cache-design.md}: with
 * {@code transactionCacheEnabled = false} (or left at its default of {@code true}), the configuration must
 * end up wired the way design sections 3.4/8 prescribe - in particular, {@link #connectionProvider} must be
 * left completely untouched when the feature is disabled, so behavior is then identical to a build without
 * the feature at all.
 */
class JdbcDomainPersistenceConfigurationTransactionCacheTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_transaction_cache_config_test_" + UUID.randomUUID());
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    private JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder minimalConfig() throws SQLException {
        return JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .withSchemaMetadata(JdbcSchemaMetadata.read(connection))
            .withConnectionProvider(new SingleJdbcConnectionProvider(connection))
            .withDialect(new H2JdbcDialect());
    }

    @Test
    void withoutAProviderTheCacheIsOffAndTheConnectionProviderIsLeftAsItIs() throws SQLException {
        var originalConnectionProvider = new SingleJdbcConnectionProvider(connection);

        var configuration = minimalConfig().withConnectionProvider(originalConnectionProvider).make();

        assertThat(configuration.transactionCacheEnabled).isFalse();
        assertThat(configuration.transactionCacheProvider).isInstanceOf(NoOpTransactionCacheProvider.class);
        assertThat(configuration.transactionCacheProvider.currentTransactionCache()).isEmpty();
        assertThat(configuration.connectionProvider).isSameAs(originalConnectionProvider);
    }

    @Test
    void aProviderSetIsIgnoredAsLongAsTheFeatureIsNotEnabled() throws SQLException {
        var configuration = minimalConfig()
            .withTransactionCacheProvider(new ThreadBoundTransactionCacheProvider<>())
            .make();

        assertThat(configuration.transactionCacheProvider).isInstanceOf(NoOpTransactionCacheProvider.class);
    }

    @Test
    void aGivenProviderIsUsedOnceEnabledAndTheConnectionProviderIsLeftAsItIs() throws SQLException {
        var originalConnectionProvider = new SingleJdbcConnectionProvider(connection);
        var givenProvider = new ThreadBoundTransactionCacheProvider<JdbcRecord>();

        var configuration = minimalConfig()
            .withConnectionProvider(originalConnectionProvider)
            .withTransactionCacheEnabled(true)
            .withTransactionCacheProvider(givenProvider)
            .make();

        assertThat(configuration.transactionCacheProvider).isSameAs(givenProvider);
        assertThat(configuration.connectionProvider).isSameAs(originalConnectionProvider);
    }

    @Test
    void disablingTheFeatureIgnoresAGivenProvider() throws SQLException {
        var configuration = minimalConfig()
            .withTransactionCacheProvider(new ThreadBoundTransactionCacheProvider<>())
            .withTransactionCacheEnabled(false)
            .make();

        assertThat(configuration.transactionCacheEnabled).isFalse();
        assertThat(configuration.transactionCacheProvider).isInstanceOf(NoOpTransactionCacheProvider.class);
    }
}
