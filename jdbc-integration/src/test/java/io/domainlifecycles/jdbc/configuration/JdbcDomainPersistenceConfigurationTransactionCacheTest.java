package io.domainlifecycles.jdbc.configuration;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.cache.TransactionCacheAwareConnectionProvider;
import io.domainlifecycles.jdbc.connection.SingleJdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
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
    void defaultsToAThreadBoundProviderAndWrapsTheConnectionProvider() throws SQLException {
        var configuration = minimalConfig().make();

        assertThat(configuration.transactionCacheEnabled).isTrue();
        assertThat(configuration.transactionCacheProvider).isInstanceOf(ThreadBoundTransactionCacheProvider.class);
        assertThat(configuration.connectionProvider).isInstanceOf(TransactionCacheAwareConnectionProvider.class);
    }

    @Test
    void disablingTheFeatureUsesTheNoOpProviderAndLeavesTheConnectionProviderUntouched() throws SQLException {
        var originalConnectionProvider = new SingleJdbcConnectionProvider(connection);

        var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .withSchemaMetadata(JdbcSchemaMetadata.read(connection))
            .withConnectionProvider(originalConnectionProvider)
            .withDialect(new H2JdbcDialect())
            .withTransactionCacheEnabled(false)
            .make();

        assertThat(configuration.transactionCacheEnabled).isFalse();
        assertThat(configuration.transactionCacheProvider).isInstanceOf(NoOpTransactionCacheProvider.class);
        assertThat(configuration.transactionCacheProvider.currentTransactionCache()).isEmpty();
        assertThat(configuration.connectionProvider)
            .as("a disabled feature must not wrap the connection provider at all")
            .isSameAs(originalConnectionProvider);
    }

    @Test
    void aCustomProviderOverridesTheDefaultAndDoesNotWrapTheConnectionProviderEither() throws SQLException {
        var originalConnectionProvider = new SingleJdbcConnectionProvider(connection);
        var customProvider = new NoOpTransactionCacheProvider<io.domainlifecycles.jdbc.records.JdbcRecord>();

        var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .withSchemaMetadata(JdbcSchemaMetadata.read(connection))
            .withConnectionProvider(originalConnectionProvider)
            .withDialect(new H2JdbcDialect())
            .withTransactionCacheProvider(customProvider)
            .make();

        assertThat(configuration.transactionCacheProvider).isSameAs(customProvider);
        assertThat(configuration.connectionProvider)
            .as("only the default ThreadBoundTransactionCacheProvider is auto-wired to the connection's "
                + "transaction boundary - a custom provider must be wired up by the caller")
            .isSameAs(originalConnectionProvider);
    }
}
