/*
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

package io.domainlifecycles.events.activemq.consume;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.mq.consume.AbstractMqDomainEventConsumer;
import io.domainlifecycles.events.consume.execution.detector.ExecutionContextDetector;
import io.domainlifecycles.events.consume.execution.processor.ExecutionContextProcessor;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ActiveMqDomainEventConsumer class extends AbstractMqDomainEventConsumer and
 * implements methods for consuming domain events from ActiveMQ Classic 5 message broker.
 * It provides functionality to connect to ActiveMQ, create consumers for specified topics,
 * consume messages, acknowledge messages, extract message body, and close connections.
 *
 * @author Mario Herb
 */
public class ActiveMqDomainEventConsumer extends AbstractMqDomainEventConsumer<MessageConsumer, TextMessage> {

    private static final Logger log = LoggerFactory.getLogger(ActiveMqDomainEventConsumer.class);

    private final ConnectionFactory connectionFactory;

    private Connection connection;
    private Map<MessageConsumer, Session> sessions = new ConcurrentHashMap<>();

    private final String virtualTopicConsumerPrefix;
    private final String virtualTopicPrefix;

    private final long receiveTimeoutMs;

    /**
     * Constructor for creating an ActiveMqDomainEventConsumer.
     *
     * @param connectionFactory The ConnectionFactory to establish the connection.
     * @param objectMapper The ObjectMapper for serialization and deserialization.
     * @param executionContextDetector The ExecutionContextDetector for detecting execution contexts.
     * @param executionContextProcessor The ExecutionContextProcessor for processing execution contexts.
     * @param classProvider The ClassProvider for providing Class instances.
     * @param virtualTopicConsumerPrefix The prefix for virtual topic consumer.
     * @param virtualTopicPrefix The prefix for virtual topic.
     * @param receiveTimeoutMs The timeout in milliseconds for receiving messages.
     */
    public ActiveMqDomainEventConsumer(ConnectionFactory connectionFactory,
                                       ObjectMapper objectMapper,
                                       ExecutionContextDetector executionContextDetector,
                                       ExecutionContextProcessor executionContextProcessor,
                                       ClassProvider classProvider,
                                       String virtualTopicConsumerPrefix,
                                       String virtualTopicPrefix,
                                       long receiveTimeoutMs) {
        super(objectMapper, executionContextDetector, executionContextProcessor, classProvider);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "ConnectionFactory is required!");
        this.virtualTopicConsumerPrefix = Objects.requireNonNull(virtualTopicConsumerPrefix, "virtualTopicConsumerPrefix is required!");
        this.virtualTopicPrefix = Objects.requireNonNull(virtualTopicPrefix, "virtualTopicPrefix is required!");
        this.receiveTimeoutMs = receiveTimeoutMs;
        initialize();
    }

    /**
     * Establishes a connection with the Active MQ broker using the provided ConnectionFactory.
     * Throws a DLCEventsException with a descriptive error message if connection establishment fails.
     */
    @Override
    protected void connect() {
        try {
            log.info("Creating connection");
            connection = connectionFactory.createConnection();
            connection.setExceptionListener(e -> log.error(e.getMessage(), e));
            log.info("Starting connection");
            connection.start();
        } catch (JMSException e) {
            var msg = "JakartaJmsDomainEventConsumer connecting failed";
            log.error(msg, e);
            throw DLCEventsException.fail(msg, e);
        }
    }

    /**
     * Creates a message consumer for a specific (virtual) topic and consumer name.
     *
     * @param topicName The name of the topic to consume messages from
     * @param consumerName The name of the consumer for identification
     * @return the created MessageConsumer for the specified topic and consumer name
     */
    @Override
    protected MessageConsumer createConsumer(String topicName, String consumerName) {
        try {

            log.info("Creating session");
            var session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            log.info("Created session");
            log.info("Creating queue: {}", consumerName);
            var queueName = virtualTopicConsumerPrefix
                + consumerName
                + "."
                + virtualTopicPrefix+topicName.replaceAll("\\.", "-");
            var queue = session.createQueue(queueName);
            log.info("Creating consumer for topic : {}", topicName);
            log.info("Creating consumer for virtual queue name: {}", queueName);
            var consumer = session.createConsumer(queue);
            sessions.put(consumer, session);
            return consumer;
        } catch (JMSException e) {
            var msg = String.format("Subscription failed for topic '%s' consumerName '%s'", topicName, consumerName);
            log.error(msg, e);
            throw DLCEventsException.fail(msg,  e);
        }
    }

    /**
     * Consume a TextMessage from a given MessageConsumer with a timeout.
     *
     * @param messageConsumer The MessageConsumer to consume messages from.
     * @return The consumed TextMessage, or null if consuming message fails.
     */
    @Override
    protected TextMessage consumeMessage(MessageConsumer messageConsumer) {
        try {
            log.trace("Start consuming ");
            Message message = messageConsumer.receive(receiveTimeoutMs);
            return (TextMessage) message;
        } catch (JMSException e) {
            log.error("Consuming message failed", e);
        }
        return null;
    }

    /**
     * Acknowledges the TextMessage received from the message queue.
     *
     * @param textMessage The TextMessage to be acknowledged.
     */
    @Override
    protected void acknowledge(TextMessage textMessage) {
        try {
            textMessage.acknowledge();
        } catch (JMSException e) {
            log.error("Acknowledging message failed", e);
        }
    }

    /**
     * Retrieves the message body from a TextMessage.
     *
     * @param textMessage The TextMessage from which to retrieve the message body.
     * @return The message body as a String.
     */
    @Override
    protected String messageBody(TextMessage textMessage) {
        try {
            return textMessage.getText();
        } catch (JMSException e) {
            var msg = "Reading message body failed!";
            log.error(msg, e);
            throw DLCEventsException.fail(msg,  e);
        }
    }

    /**
     * Closes the connection to the Active MQ broker by closing the underlying JMS connection.
     * Logs informational messages before and after attempting to close the connection.
     * If an exception occurs during the closing process, it is logged as an error.
     */
    @Override
    protected void closeConnection() {
        try {
            log.info("Closing connection");
            connection.close();
        } catch (JMSException e) {
            log.error("Closing connection failed", e);
        }
        log.info("Closed connection");
    }

    /**
     * Closes the given MessageConsumer and its associated session.
     *
     * @param messageConsumer The MessageConsumer to be closed.
     */
    @Override
    protected void closeConsumer(MessageConsumer messageConsumer) {
        try {
            var session = sessions.get(messageConsumer);
            messageConsumer.close();
            session.close();
        } catch (JMSException e) {
            log.error("Closing consumer failed", e);
        }
    }

}
