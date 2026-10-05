package io.domainlifecycles.autoconfig.features.multiple.persistence_cache_annotation;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.configurations.DlcJacksonAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcNoTxInMemoryDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcServiceKindAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringOpenApiAutoConfiguration;
import io.domainlifecycles.autoconfig.configurations.DlcSpringWebAutoConfiguration;
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
        DlcJacksonAutoConfiguration.class,
        DlcSpringBusDomainEventsAutoConfiguration.class,
        DlcNoTxInMemoryDomainEventsAutoConfiguration.class,
        DlcServiceKindAutoConfiguration.class
    })
public class TestApplicationTransactionCacheByAnnotation {
}
