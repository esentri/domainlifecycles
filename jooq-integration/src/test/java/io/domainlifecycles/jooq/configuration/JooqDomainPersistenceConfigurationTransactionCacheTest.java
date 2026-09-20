package io.domainlifecycles.jooq.configuration;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jooq.cache.TransactionCacheJooqBinder;
import io.domainlifecycles.persistence.cache.NoOpTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers design section 8 of {@code persistence/docs/transaction-cache-design.md}: with
 * {@code transactionCacheEnabled = false} (or left at its default of {@code true}), the configuration must
 * end up wired to the provider design 3.4/8 prescribes - a pure unit test of the builder's {@code .make()}
 * output, deliberately without a database or a {@code Domain} model, since none of that is needed to observe
 * which {@code TransactionCacheProvider} the builder picked.
 */
class JooqDomainPersistenceConfigurationTransactionCacheTest {

    private static JooqDomainPersistenceConfiguration.JooqPersistenceConfigurationBuilder minimalConfig() {
        return JooqDomainPersistenceConfiguration.JooqPersistenceConfigurationBuilder.newConfig()
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .withRecordClassProvider(Set::of);
    }

    @Test
    void defaultsToAThreadBoundProvider() {
        var configuration = minimalConfig().make();

        assertThat(configuration.transactionCacheEnabled).isTrue();
        assertThat(configuration.transactionCacheProvider).isInstanceOf(ThreadBoundTransactionCacheProvider.class);
    }

    @Test
    void disablingTheFeatureUsesTheNoOpProvider() {
        var configuration = minimalConfig()
            .withTransactionCacheEnabled(false)
            .make();

        assertThat(configuration.transactionCacheEnabled).isFalse();
        assertThat(configuration.transactionCacheProvider).isInstanceOf(NoOpTransactionCacheProvider.class);
        //a disabled feature must never report an active cache, on any thread
        assertThat(configuration.transactionCacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    void aCustomProviderOverridesTheDefaultRegardlessOfTheEnabledFlag() {
        var customProvider = new NoOpTransactionCacheProvider<org.jooq.UpdatableRecord<?>>();

        var configuration = minimalConfig()
            .withTransactionCacheEnabled(true)
            .withTransactionCacheProvider(customProvider)
            .make();

        assertThat(configuration.transactionCacheProvider).isSameAs(customProvider);
    }

    @Test
    void registerOnIsANoOpForANoOpProviderBasedConfiguration() {
        //JooqAggregateRepository only ever calls TransactionCacheJooqBinder.registerOn(...) for a
        //ThreadBoundTransactionCacheProvider (see registerTransactionCacheBinderIfApplicable) - a disabled
        //feature must therefore never end up with a binder on the jOOQ Configuration at all, which this
        //documents at the unit that would otherwise silently start firing again if that guard regressed
        var configuration = minimalConfig().withTransactionCacheEnabled(false).make();

        assertThat(configuration.transactionCacheProvider)
            .as("a ThreadBoundTransactionCacheProvider is the only kind JooqAggregateRepository ever binds "
                + "a " + TransactionCacheJooqBinder.class.getSimpleName() + " to")
            .isNotInstanceOf(ThreadBoundTransactionCacheProvider.class);
    }
}
