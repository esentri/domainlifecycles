package io.domainlifecycles.boot3.autoconfig.features.multiple.persistence_builder_jdbc;

import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = {TestApplicationJdbcPersistenceAndBuilderAutoConfig.class, JdbcCustomTransactionCacheProviderTest.CustomProvider.class},
    properties = "dlc.features.persistence.transaction-cache.enabled=true")
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
class JdbcCustomTransactionCacheProviderTest {

    @TestConfiguration
    static class CustomProvider {

        // any name: the type decides
        @Bean
        TransactionCacheProvider<JdbcRecord> applicationTransactionCacheProvider() {
            return new ThreadBoundTransactionCacheProvider<>();
        }
    }

    @Autowired
    private JdbcDomainPersistenceProvider jdbcDomainPersistenceProvider;

    @Autowired
    private TransactionCacheProvider<JdbcRecord> applicationTransactionCacheProvider;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testABeanOfTheProviderTypeReplacesTheProvider() {
        assertThat(applicationContext.containsBean("dlcJdbcTransactionCacheProvider")).isFalse();
        assertThat(jdbcDomainPersistenceProvider.transactionCacheProvider).isSameAs(applicationTransactionCacheProvider);
    }
}
