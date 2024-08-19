package io.domainlifecycles.events.api;

import io.domainlifecycles.events.publish.DomainEventPublisher;
import io.domainlifecycles.events.receive.execution.ReceivingDomainEventHandler;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.receive.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.receive.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.services.api.ServiceProvider;

public interface DomainEventsConfiguration {

    DomainEventPublisher getDomainEventPublisher();
    ServiceProvider getServiceProvider();
    ReceivingDomainEventHandler getReceivingDomainEventHandler();
    ExecutionContextDetector getExecutionContextDetector();
    HandlerExecutor getHandlerExecutor();
    ExecutionContextProcessor getExecutionContextProcessor();

    DomainEventsConfigurationBuilder toBuilder();
}
