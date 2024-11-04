package io.domainlifecycles.events.mq.consume;

import io.domainlifecycles.events.consume.execution.detector.ExecutionContext;
import io.domainlifecycles.events.consume.execution.handler.HandlerExecutor;
import io.domainlifecycles.events.gruelbox.idempotent.IdempotencyConfigurationEntry;

public interface TransactionalIdempotencyAwareHandlerExecutorProxy extends HandlerExecutor {

    boolean schedule(ExecutionContext executionContext, IdempotencyConfigurationEntry config);
}
