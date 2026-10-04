package io.domainlifecycles.boot3.autoconfig.features.multiple.persistence_builder;

import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import org.jooq.UpdatableRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TestApplicationPersistenceAndBuilderAutoConfig.class, JooqCustomTransactionCacheProviderTest.CustomProvider.class})
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
class JooqCustomTransactionCacheProviderTest {

    @TestConfiguration
    static class CustomProvider {

        // any name: the type decides
        @Bean
        TransactionCacheProvider<UpdatableRecord<?>> applicationTransactionCacheProvider() {
            return new ThreadBoundTransactionCacheProvider<>();
        }
    }

    @Autowired
    private JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Autowired
    private TransactionCacheProvider<UpdatableRecord<?>> applicationTransactionCacheProvider;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testABeanOfTheProviderTypeReplacesTheProvider() {
        assertThat(applicationContext.containsBean("dlcTransactionCacheProvider")).isFalse();
        assertThat(jooqDomainPersistenceProvider.transactionCacheProvider).isSameAs(applicationTransactionCacheProvider);
    }
}
