package io.domainlifecycles.autoconfig.features.multiple.persistence_builder;

import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JooqMismatchingTransactionCacheProviderTest {

    @Configuration(proxyBeanMethods = false)
    static class JdbcProvider {

        @Bean
        TransactionCacheProvider<JdbcRecord> applicationTransactionCacheProvider() {
            return new ThreadBoundTransactionCacheProvider<>();
        }
    }

    @Test
    void testAProviderNotFittingTheJooqPersistenceStopsTheStart() {
        var application = new SpringApplicationBuilder(TestApplicationPersistenceAndBuilderAutoConfig.class, JdbcProvider.class)
            .web(WebApplicationType.NONE)
            .profiles("test", "test-dlc-domain", "test-dlc-persistence")
            .properties("dlc.features.persistence.transaction-cache.enabled=true");

        assertThatThrownBy(application::run)
            .rootCause()
            .hasMessageContaining("TransactionCacheProvider<UpdatableRecord<?>>")
            .hasMessageContaining("dlc.features.persistence.transaction-cache.enabled=false");
    }
}
