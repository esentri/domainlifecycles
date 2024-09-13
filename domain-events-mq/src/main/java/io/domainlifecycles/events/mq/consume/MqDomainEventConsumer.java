package io.domainlifecycles.events.mq.consume;

public interface MqDomainEventConsumer {
    void closeAll();

    void pauseHandler(String handlerClassName, String handlerMethodName, String domainEventTypeName);

    void resumeHandler(String handlerClassName, String handlerMethodName, String domainEventTypeName);

    public boolean isHandlerPaused(String handlerClassName, String handlerMethodName, String domainEventTypeName);

    void initialize();
}
