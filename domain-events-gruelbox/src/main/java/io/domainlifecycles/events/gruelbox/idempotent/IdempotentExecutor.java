package io.domainlifecycles.events.gruelbox.idempotent;

import io.domainlifecycles.domain.types.AggregateDomainEvent;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.events.consume.execution.detector.AggregateExecutionContext;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;
import io.domainlifecycles.events.consume.execution.detector.ServiceExecutionContext;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.services.api.ServiceProvider;

public class IdempotentExecutor {

    private final ServiceProvider serviceProvider;
    private final HandlerExecutor handlerExecutor;

    public IdempotentExecutor(ServiceProvider serviceProvider, HandlerExecutor handlerExecutor) {
        this.serviceProvider = serviceProvider;
        this.handlerExecutor = handlerExecutor;
    }

    public void execute(IdempotentExecutionContext idempotentExecutionContext){
        ExecutionContext executionContext = null;
        var service = serviceProvider.getServiceInstance(idempotentExecutionContext.handlerClass().getName());
        if(Repository.class.isAssignableFrom(idempotentExecutionContext.handlerClass())){
            executionContext = new AggregateExecutionContext<>((Repository) service, idempotentExecutionContext.handlerMethod(), (AggregateDomainEvent)idempotentExecutionContext.domainEvent());
        }else{
            executionContext = new ServiceExecutionContext(service, idempotentExecutionContext.handlerMethod(), idempotentExecutionContext.domainEvent());
        }
        handlerExecutor.execute(executionContext);
    }

}
