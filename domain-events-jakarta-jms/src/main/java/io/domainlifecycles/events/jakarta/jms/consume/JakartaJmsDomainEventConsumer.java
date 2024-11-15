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

package io.domainlifecycles.events.jakarta.jms.consume;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.events.mq.consume.AbstractMqDomainEventConsumer;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.events.exception.DLCEventsException;
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
 * A class responsible for consuming domain events using Jakarta Messaging (JMS) based message brokers.
 * Extends AbstractMqDomainEventConsumer and implements the necessary methods.
 *
 * @author Mario Herb
 */
public class JakartaJmsDomainEventConsumer extends AbstractMqDomainEventConsumer<MessageConsumer, TextMessage> {

    private static final Logger log = LoggerFactory.getLogger(JakartaJmsDomainEventConsumer.class);

    private final ConnectionFactory connectionFactory;
    private Connection connection;
    private Map<MessageConsumer, Session> sessions = new ConcurrentHashMap<>();
    private final long receiveTimeoutMs;

    /**
     * Constructs a JakartaJmsDomainEventConsumer with the provided parameters.
     *
     * @param connectionFactory The ConnectionFactory used for creating connections to the message broker
     * @param objectMapper The ObjectMapper instance to serialize/deserialize messages
     * @param executionContextDetector The ExecutionContextDetector for detecting execution contexts
     * @param executionContextProcessor The ExecutionContextProcessor for processing execution contexts
     * @param classProvider The ClassProvider for providing Class instances
     * @param receiveTimeoutMs The timeout value for receiving messages in milliseconds
     */
    public JakartaJmsDomainEventConsumer(ConnectionFactory connectionFactory,
                                         ObjectMapper objectMapper,
                                         ExecutionContextDetector executionContextDetector,
                                         ExecutionContextProcessor executionContextProcessor,
                                         ClassProvider classProvider,
                                         long receiveTimeoutMs) {
        super(objectMapper, executionContextDetector, executionContextProcessor, classProvider);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "ConnectionFactory is required!");
        this.receiveTimeoutMs = receiveTimeoutMs;
        initialize();
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    protected MessageConsumer createConsumer(String topicName, String consumerName) {
        try {
            log.info("Creating session");
            var session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            log.info("Created session");
            log.info("Creating queue: {}", consumerName);
            var topic = session.createTopic(topicName);
            log.info("Creating consumer for : {}", topicName);
            var consumer =  session.createSharedDurableConsumer(topic, consumerName);
            sessions.put(consumer, session);
            return consumer;
        } catch (JMSException e) {
            var msg = String.format("Subscription failed for topic '%s' consumerName '%s'", topicName, consumerName);
            log.error(msg, e);
            throw DLCEventsException.fail(msg,  e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TextMessage consumeMessage(MessageConsumer messageConsumer) {
        try {
            Message message = messageConsumer.receive(receiveTimeoutMs);
            return (TextMessage) message;
        } catch (JMSException e) {
            log.error("Consuming message failed", e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
