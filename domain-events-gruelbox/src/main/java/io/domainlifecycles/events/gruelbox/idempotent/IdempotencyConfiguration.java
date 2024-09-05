package io.domainlifecycles.events.gruelbox.idempotent;

import io.domainlifecycles.events.receive.execution.detector.AggregateExecutionContext;
import io.domainlifecycles.events.receive.execution.detector.ExecutionContext;
import io.domainlifecycles.events.receive.execution.detector.ServiceExecutionContext;
import io.domainlifecycles.mirror.api.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class IdempotencyConfiguration {
    private static final Logger log = LoggerFactory.getLogger(IdempotencyConfiguration.class);

    private List<IdempotencyConfigurationEntry> configurationEntries = new ArrayList<>();

    public void addConfigurationEntry(IdempotencyConfigurationEntry entry){
        if(!isDomainEventHandlerMethodWithExpectedName(entry)){
            var msg = String.format("Domain event handler '%s' has no handler method called '%s'", entry.handlerClass().getName(), entry.methodName());
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        configurationEntries.add(entry);
    }

    private boolean isDomainEventHandlerMethodWithExpectedName(IdempotencyConfigurationEntry entry){
        var mirror = Domain.getInitializedDomain().allTypeMirrors().get(entry.handlerClass().getName());
        if(mirror != null){
            try {
                var methodMirror = mirror.methodByName(entry.methodName());
                var domainEventMirror = methodMirror.getListenedEvent().orElseThrow();
                return domainEventMirror.getTypeName().equals(entry.domainEventClass().getName());
            }catch(Throwable t){
                return false;
            }
        }
        return false;
    }

    public Optional<IdempotencyConfigurationEntry> idempotencyProtectionConfiguration(ExecutionContext executionContext){
        return configurationEntries.stream().filter(cfg -> match(executionContext, cfg)).findFirst();

    }

    private boolean match(ExecutionContext executionContext, IdempotencyConfigurationEntry entry){
        if(executionContext instanceof ServiceExecutionContext serviceExecutionContext){
            return serviceExecutionContext.handler().getClass().getName().equals(entry.handlerClass().getName())
                && serviceExecutionContext.handlerMethodName().equals(entry.methodName())
                && serviceExecutionContext.domainEvent().getClass().getName().equals(entry.domainEventClass().getName());
        }else if(executionContext instanceof AggregateExecutionContext<?,?> aggregateExecutionContext){
            var aggregateMirror = Domain.repositoryMirrorFor(aggregateExecutionContext.aggregateRepository()).getManagedAggregate().orElseThrow();
            return aggregateMirror.getTypeName().equals(entry.handlerClass().getName())
                && aggregateExecutionContext.aggregateHandlerMethodName().equals(entry.methodName())
                && aggregateExecutionContext.domainEvent().getClass().getName().equals(entry.domainEventClass().getName());
        }
        return false;
    }
}
