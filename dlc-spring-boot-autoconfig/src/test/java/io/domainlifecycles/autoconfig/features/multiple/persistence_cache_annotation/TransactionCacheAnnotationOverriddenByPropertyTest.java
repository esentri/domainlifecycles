package io.domainlifecycles.autoconfig.features.multiple.persistence_cache_annotation;

import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.cache.NoOpTransactionCacheProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = TestApplicationTransactionCacheByAnnotation.class,
    properties = "dlc.features.persistence.transaction-cache.enabled=false")
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
class TransactionCacheAnnotationOverriddenByPropertyTest {

    @Autowired
    private JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Test
    void testAPropertySetInTheConfigurationTakesPrecedenceOverTheAnnotation() {
        assertThat(jooqDomainPersistenceProvider.transactionCacheProvider)
            .isInstanceOf(NoOpTransactionCacheProvider.class);
    }
}
