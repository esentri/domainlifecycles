/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

/**
 * Abstract class that represents a message queue domain event consumer.
 *
 * @param <CONSUMER> the type of the consumer
 * @param <MESSAGE> the type of the message
 *
 * @author Mario Herb
 */
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

    /**
     * Constructs an AbstractMqDomainEventConsumer with the provided dependencies.
     *
     * @param objectMapper The ObjectMapper for serialization and deserialization.
     * @param executionContextDetector The ExecutionContextDetector for detecting execution contexts.
     * @param executionContextProcessor The ExecutionContextProcessor for processing execution contexts.
     * @param classProvider The ClassProvider for providing Class instances.
     */
    public AbstractMqDomainEventConsumer(ObjectMapper objectMapper,
                                         ExecutionContextDetector executionContextDetector,
                                         ExecutionContextProcessor executionContextProcessor,
                                         ClassProvider classProvider) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper is required!");
        this.executionContextDetector = Objects.requireNonNull(executionContextDetector, "An ExecutionContextDetector is required!");
        this.executionContextProcessor = Objects.requireNonNull(executionContextProcessor, "An ExecutionContextProcessor is required!");
        this.classProvider = Objects.requireNonNull(classProvider, "A ClassProvider is required!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(){
        connect();
        initializeHandlers();
        initialized = true;
    }

    /**
     * Establishes a connection to a specific message broker resource or service.
     * Subclasses must implement this method for connecting to the corresponding resource.
     */
    abstract protected void connect();

    /**
     * Subscribes a given MqDomainEventHandler to the message broker for handling domain events.
     * This method adds the handler to the internal list of handlers, creates a consumer for the handler,
     * and initiates asynchronous processing for the handler.
     *
     * @param mqDomainEventHandler The MqDomainEventHandler to subscribe for handling domain events
     */
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

    /**
     * Creates a consumer for handling messages from a specified topic.
     *
     * @param topicName The name of the topic from which to consume messages
     * @param consumerName The name of the consumer
     * @return The created consumer instance
     */
    abstract protected CONSUMER createConsumer(String topicName, String consumerName);

    /**
     * Processes messages consumed by the consumer using the provided domain event handler.
     *
     * @param consumer The consumer instance to consume messages
     * @param handler The domain event handler to handle the messages
     * @return Void
     */
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

    /**
     * Consumes a message using the provided consumer.
     *
     * @param consumer The consumer instance to consume messages
     * @return The consumed message
     */
    abstract protected MESSAGE consumeMessage(CONSUMER consumer);

    /**
     * Parses the message to a DomainEvent object based on the provided domain event type.
     *
     * @param message The message to parse
     * @param domainEventType The type of the domain event to parse the message to
     * @return The parsed DomainEvent object
     */
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

    /**
     * Acknowledges the processing of a message. Subclasses must implement this method to acknowledge the receipt or processing of the provided message.
     *
     * @param message The message to be acknowledged
     */
    abstract protected void acknowledge(MESSAGE message);

    /**
     * Returns the message body from the provided message.
     *
     * @param message The message from which to extract the body
     * @return The message body as a String
     */
    abstract protected String messageBody(MESSAGE message);

    /**
     * Initializes event handlers for handling domain events.
     * This method retrieves all domain type mirrors from the initialized domain,
     * filters out abstract domain event mirrors, then retrieves handlers for each domain event mirror
     * and subscribes them to the event consumer.
     */
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

    /**
     * Retrieves a list of MqDomainEventHandler instances that are capable of handling the given DomainEventMirror.
     *
     * @param domainEventMirror The DomainEventMirror for which to find handlers
     * @return A list of MqDomainEventHandler instances that can handle the specified DomainEventMirror
     */
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

    /**
     * Creates a new MqDomainEventHandler for handling domain events.
     *
     * @param handlerClassName The class name of the handler
     * @param handlerMethodName The method name of the handler
     * @param domainEventName The name of the domain event
     * @return A new MqDomainEventHandler instance
     */
    protected MqDomainEventHandler newHandler(String handlerClassName, String handlerMethodName, String domainEventName){
        return new MqDomainEventHandler(
            handlerClassName,
            handlerMethodName,
            (Class<? extends DomainEvent>) classProvider.getClassForName(domainEventName),
            executionContextDetector,
            executionContextProcessor
        );
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Closes the connection related to the message broker resource or service.
     * Subclasses must implement this method to perform the necessary steps for closing the connection.
     */
    abstract protected void closeConnection();

    /**
     * Closes the provided consumer. Subclasses must implement this method for closing the consumer instance.
     *
     * @param consumer The consumer instance to be closed
     */
    abstract protected void closeConsumer(CONSUMER consumer);

    /**
     * @return true if the object is initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Pause the specified handler for the given domain event type.
     * The handler will be paused by calling the pause() method on the handler if found.
     *
     * @param handlerClassName The class name of the handler to be paused
     * @param handlerMethodName The method name of the handler to be paused
     * @param domainEventTypeName The type of the domain event for which the handler will be paused
     */
    @Override
    public void pauseHandler(String handlerClassName, String handlerMethodName, String domainEventTypeName){
        log.info("Pause handler {}.{}({})", handlerClassName, handlerMethodName, domainEventTypeName);
        var h = get(handlerClassName, handlerMethodName, domainEventTypeName);
        if(h != null){
            h.pause();
        }
    }

    /**
     * Resumes a specified handler for the given domain event type.
     *
     * @param handlerClassName The class name of the handler to be resumed
     * @param handlerMethodName The method name of the handler to be resumed
     * @param domainEventTypeName The type of the domain event for which the handler will be resumed
     */
    @Override
    public void resumeHandler(String handlerClassName, String handlerMethodName, String domainEventTypeName){
        log.info("Resume handler {}.{}({})", handlerClassName, handlerMethodName, domainEventTypeName);
        var h = get(handlerClassName, handlerMethodName, domainEventTypeName);
        if(h != null){
            h.resume();
        }
    }

    /**
     * Checks if a specified handler for a given domain event type is currently paused.
     *
     * @param handlerClassName The class name of the handler to check if paused
     * @param handlerMethodName The method name of the handler to check if paused
     * @param domainEventTypeName The type of the domain event for which to check handler's pause status
     * @return true if the handler is paused, false otherwise
     */
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
