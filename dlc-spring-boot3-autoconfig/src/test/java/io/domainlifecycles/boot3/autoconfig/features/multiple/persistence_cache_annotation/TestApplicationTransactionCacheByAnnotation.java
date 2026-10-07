package io.domainlifecycles.boot3.autoconfig.features.multiple.persistence_cache_annotation;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcJackson2AutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcNoTxInMemoryDomainEventsAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.boot3.autoconfig.configurations.DlcSpringWebAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Switches the Transaction Cache on - and limits it to a single aggregate per transaction - by annotation only.
 */
@SpringBootApplication
@EnableDlc(
    transactionCacheEnabled = true,
    transactionCacheMaxSize = 1,
    exclude = {
        DlcSpringWebAutoConfiguration.class,
        DlcSpringOpenApiAutoConfiguration.class,
        DlcJackson2AutoConfiguration.class,
        DlcSpringBusDomainEventsAutoConfiguration.class,
        DlcNoTxInMemoryDomainEventsAutoConfiguration.class,
        DlcServiceKindAutoConfiguration.class
    })
public class TestApplicationTransactionCacheByAnnotation {
}
