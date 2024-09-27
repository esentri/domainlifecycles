package io.domainlifecycles.events.mq.consume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractMqDomainEventConsumer<CONSUMER, MESSAGE> implements MqDomainEventConsumer {

    protected Logger log = LoggerFactory.getLogger(AbstractMqDomainEventConsumer.class);


    protected final ObjectMapper objectMapper;
    protected final ExecutionContextDetector executionContextDetector;
    protected final ExecutionContextProcessor executionContextProcessor;
    protected final ClassProvider classProvider;

    protected List<MqDomainEventHandler> handlers = new ArrayList<>();
    protected List<CONSUMER> consumers = new ArrayList<>();
    protected List<Future<Void>> processingFutures = new ArrayList<>();

    protected AtomicBoolean runFlag = new AtomicBoolean(true);

    protected boolean initialized = false;

    public AbstractMqDomainEventConsumer(ObjectMapper objectMapper,
                                         ExecutionContextDetector executionContextDetector,
                                         ExecutionContextProcessor executionContextProcessor,
                                         ClassProvider classProvider) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper is required!");
        this.executionContextDetector = Objects.requireNonNull(executionContextDetector, "An ExecutionContextDetector is required!");
        this.executionContextProcessor = Objects.requireNonNull(executionContextProcessor, "An ExecutionContextProcessor is required!");
        this.classProvider = Objects.requireNonNull(classProvider, "A ClassProvider is required!");
    }

    @Override
    public void initialize(){
        connect();
        initializeHandlers();
        initialized = true;
    }

    abstract protected void connect();

    protected void subscribe(MqDomainEventHandler mqDomainEventHandler) {
        log.info("Subscribing handler {}", mqDomainEventHandler.getHandlerId());
        handlers.add(mqDomainEventHandler);
        var topicName = mqDomainEventHandler.getDomainEventType()
            .getName();
        var consumerName = mqDomainEventHandler.getHandlerId();
        CONSUMER consumer = createConsumer(topicName, consumerName);
        consumers.add(consumer);
        processingFutures.add(CompletableFuture.supplyAsync(() -> process(consumer, mqDomainEventHandler)));
        log.info("Subscribed handler {}", mqDomainEventHandler.getHandlerId());
    }

    abstract protected CONSUMER createConsumer(String topicName, String consumerName);

    protected Void process(CONSUMER consumer, MqDomainEventHandler handler) {
        log.info("Consumer starting processing");
        while (runFlag.get()) {
                if(handler.isPaused()){
                    continue;
                }
                var message = consumeMessage(consumer);
                if (message == null) {
                    continue;
                }
                DomainEvent domainEvent = parseMessage(message, handler.getDomainEventType());
                if(domainEvent != null) {
                    try {
                        log.trace("Invoking handler", handler.getHandlerId());
                        handler.handle(domainEvent);
                        log.trace("Handled message {}",  handler.getHandlerId());
                    } catch (Throwable t) {
                        log.error("Handling message failed {}",  handler.getHandlerId(), t);
                    }
                }
                acknowledge(message);
        }
        log.info("Processing finished");
        closeConsumer(consumer);
        return null;
    }

    abstract protected MESSAGE consumeMessage(CONSUMER consumer);

    protected DomainEvent parseMessage(MESSAGE message, Class<? extends DomainEvent> domainEventType){
        var body = messageBody(message);
        try {
            return objectMapper.readValue(body, domainEventType);
        } catch (JsonProcessingException e) {
            var msg = String.format("DomainEvent '%s' deserialialization failed!", body);
            log.error(msg, e);
            throw DLCEventsException.fail(msg,  e);
        }
    }

    abstract protected void acknowledge(MESSAGE message);

    abstract protected String messageBody(MESSAGE message);

    protected void initializeHandlers(){
        Domain.getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dtm -> !dtm.isAbstract() && dtm.getDomainType().equals(DomainType.DOMAIN_EVENT))
            .map(dtm -> (DomainEventMirror) dtm)
            .flatMap(dem -> handlersForDomainEvent(dem).stream())
            .forEach(this::subscribe);
    }

    protected List<MqDomainEventHandler> handlersForDomainEvent(DomainEventMirror domainEventMirror){
        var handlers = new ArrayList<MqDomainEventHandler>();
        domainEventMirror.getListeningDomainServices().forEach(ds ->
            handlers.addAll(ds.getMethods().stream()
                .filter(me -> me.listensTo(domainEventMirror))
                .map(me -> newHandler(ds.getTypeName(), me.getName(), domainEventMirror.getTypeName()))
                .toList())
        );
        domainEventMirror.getListeningApplicationServices().forEach(ds ->
            handlers.addAll(ds.getMethods().stream()
                .filter(me -> me.listensTo(domainEventMirror))
                .map(me -> newHandler(ds.getTypeName(), me.getName(), domainEventMirror.getTypeName()))
                .toList())
        );
        domainEventMirror.getListeningOutboundServices().forEach(ds ->
            handlers.addAll(ds.getMethods().stream()
                .filter(me -> me.listensTo(domainEventMirror))
                .map(me -> newHandler(ds.getTypeName(), me.getName(), domainEventMirror.getTypeName()))
                .toList())
        );
        domainEventMirror.getListeningRepositories().forEach(ds ->
            handlers.addAll(ds.getMethods().stream()
                .filter(me -> me.listensTo(domainEventMirror))
                .map(me -> newHandler(ds.getTypeName(), me.getName(), domainEventMirror.getTypeName()))
                .toList())
        );
        domainEventMirror.getListeningQueryClients().forEach(ds ->
            handlers.addAll(ds.getMethods().stream()
                .filter(me -> me.listensTo(domainEventMirror))
                .map(me -> newHandler(ds.getTypeName(), me.getName(), domainEventMirror.getTypeName()))
                .toList())
        );
        domainEventMirror.getListeningAggregates().forEach(ds ->
            handlers.addAll(ds.getMethods().stream()
                .filter(me -> me.listensTo(domainEventMirror))
                .map(me -> newHandler(ds.getTypeName(), me.getName(), domainEventMirror.getTypeName()))
                .toList())
        );
        return handlers;
    }

    protected MqDomainEventHandler newHandler(String handlerClassName, String handlerMethodName, String domainEventName){
        return new MqDomainEventHandler(
            handlerClassName,
            handlerMethodName,
            (Class<? extends DomainEvent>) classProvider.getClassForName(domainEventName),
            executionContextDetector,
            executionContextProcessor
        );
    }

    @Override
    public void closeAll() {
        runFlag.set(false);

        processingFutures.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Getting data from future failed", e);
            }
        });
        consumers.forEach(c -> closeConsumer(c));
        closeConnection();
        log.info("Closed session and connection");
    }

    abstract protected void closeConnection();

    abstract protected void closeConsumer(CONSUMER consumer);

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void pauseHandler(String handlerClassName, String handlerMethodName, String domainEventTypeName){
        log.info("Pause handler {}.{}({})", handlerClassName, handlerMethodName, domainEventTypeName);
        var h = get(handlerClassName, handlerMethodName, domainEventTypeName);
        if(h != null){
            h.pause();
        }
    }

    @Override
    public void resumeHandler(String handlerClassName, String handlerMethodName, String domainEventTypeName){
        log.info("Resume handler {}.{}({})", handlerClassName, handlerMethodName, domainEventTypeName);
        var h = get(handlerClassName, handlerMethodName, domainEventTypeName);
        if(h != null){
            h.resume();
        }
    }

    @Override
    public boolean isHandlerPaused(String handlerClassName, String handlerMethodName, String domainEventTypeName){
        var h = get(handlerClassName, handlerMethodName, domainEventTypeName);
        if(h != null){
            return h.isPaused();
        }
        return false;
    }

    private MqDomainEventHandler get(String handlerClassName, String handlerMethodName, String domainEventTypeName){
        Objects.requireNonNull(handlerClassName, "A handlerClassName must be defined!");
        Objects.requireNonNull(handlerClassName, "A handlerMethodName must be defined!");
        Objects.requireNonNull(handlerClassName, "A domainEventTypeName must be defined!");
        var handler = this.handlers.stream()
            .filter(h ->
                h.getHandlerClassName().equals(handlerClassName)
                && h.getHandlerMethodName().equals(handlerMethodName)
                && h.getDomainEventType().getName().equals(domainEventTypeName)
            )
            .findFirst();
        if(handler.isPresent()){
            return handler.get();
        }
        else{
            log.error("No handler found: {}.{}({})", handlerClassName, handlerMethodName, domainEventTypeName);
            return null;
        }
    }
}
