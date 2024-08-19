package io.domainlifecycles.events.api;

import io.domainlifecycles.events.publish.direct.DirectPassThroughDomainEventPublisher;
import io.domainlifecycles.events.receive.execution.GeneralReceivingDomainEventHandler;
import io.domainlifecycles.events.receive.execution.detector.MirrorBasedExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.handler.ReflectiveHandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.AsyncExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;

public class NonTransactionDefaultDomainEventsConfiguration {

    public static DomainEventsConfiguration configuration(ServiceProvider serviceProvider){
        var builder = new DomainEventsConfigurationBuilder();
        var handlerExecutor = new ReflectiveHandlerExecutor();
        var executionContextDetector = new MirrorBasedExecutionContextDetector(serviceProvider);
        var executionContextProcessor = new AsyncExecutionContextProcessor(handlerExecutor);
        var receivingDomainEventHandler = new GeneralReceivingDomainEventHandler(executionContextDetector, executionContextProcessor);
        return builder
            .withServiceProvider(serviceProvider)
            .withExecutionContextDetector(executionContextDetector)
            .withHandlerExecutor(handlerExecutor)
            .withExecutionContextProcessor(executionContextProcessor)
            .withReceivingDomainEventHandler(receivingDomainEventHandler)
            .withDomainEventPublisher(new DirectPassThroughDomainEventPublisher(receivingDomainEventHandler))
            .build();
    }
}
