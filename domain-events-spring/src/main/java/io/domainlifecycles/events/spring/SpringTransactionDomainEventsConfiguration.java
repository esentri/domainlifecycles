package io.domainlifecycles.events.spring;

import io.domainlifecycles.events.api.DomainEventsConfiguration;
import io.domainlifecycles.events.api.NonTransactionDefaultDomainEventsConfiguration;
import io.domainlifecycles.events.spring.publish.DirectSpringTransactionalDomainEventPublisher;
import io.domainlifecycles.events.spring.receive.execution.handler.SpringTransactionalHandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * SpringTransactionDomainEventsConfiguration class provides a static method
 * to configure and create a DomainEventsConfiguration instance with Spring transaction support.
 *
 * The configuration method takes a {@link PlatformTransactionManager}, {@link ServiceProvider},
 * and a boolean flag indicating whether to publish events after the transaction is committed.
 *
 * @author Mario Herb
 */
public class SpringTransactionDomainEventsConfiguration {

    /**
     * Default DomainEventsConfiguration for Spring transaction handling.
     * @param transactionManager
     * @param serviceProvider
     * @param publishAfterCommit
     * @return
     */
    public static DomainEventsConfiguration configuration(
        PlatformTransactionManager transactionManager,
        ServiceProvider serviceProvider,
        boolean publishAfterCommit
    ){
        var baseConfig = NonTransactionDefaultDomainEventsConfiguration.configuration(serviceProvider);
        var builder = baseConfig.toBuilder();
        var publisher = new DirectSpringTransactionalDomainEventPublisher(baseConfig.getReceivingDomainEventHandler(), publishAfterCommit);
        return builder
            .withDomainEventPublisher(publisher)
            .withHandlerExecutor(new SpringTransactionalHandlerExecutor(transactionManager))
            .build();
    }
}
