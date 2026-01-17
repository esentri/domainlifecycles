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

package io.domainlifecycles.events.activemq.publish;

import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.mq.publish.AbstractMqDomainEventPublisher;
import io.domainlifecycles.events.serialize.DomainEventSerializer;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.DeliveryMode;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import jakarta.jms.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * ActiveMqDomainEventPublisher is a class that extends AbstractMqDomainEventPublisher and implements
 * the functionality to publish domain events to ActiveMQ virtual topics. It establishes a connection to ActiveMQ
 * server, creates a session, and sends messages to specified topics.
 *
 * @author Mario Herb
 */
public class ActiveMqDomainEventPublisher extends AbstractMqDomainEventPublisher<Topic> {

    private static final Logger log = LoggerFactory.getLogger(ActiveMqDomainEventPublisher.class);
    private final ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private final String virtualTopicPrefix;

    /**
     * Constructor for ActiveMqDomainEventPublisher.
     *
     * @param connectionFactory The ConnectionFactory to be used for creating connections to ActiveMQ.
     * @param domainEventSerializer serialization and deserialization of events.
     * @param virtualTopicPrefix Prefix for virtual topics.
     */
    public ActiveMqDomainEventPublisher(
        ConnectionFactory connectionFactory,
        DomainEventSerializer domainEventSerializer,
        String virtualTopicPrefix
    ) {
        super(domainEventSerializer);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "ConnectionFactory is required!");
        connect();
        this.virtualTopicPrefix = Objects.requireNonNull(virtualTopicPrefix, "virtualTopicPrefix is required!");
    }

    /**
     * Establishes a connection to the ActiveMQ broker by creating a session and starting the connection.
     * Any exceptions that occur during the connection process are logged and a DLCEventsException is thrown.
     */
    @Override
    protected void connect() {
        try {
            log.info("Creating connection");
            connection = connectionFactory.createConnection();
            connection.setExceptionListener(e -> log.error(e.getMessage(), e));
            log.info("Starting connection");
            connection.start();
            log.info("Creating session");
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            log.info("Created session");
        } catch (JMSException e) {
            var msg = "ActiveMqDomainEventPublisher initialization failed";
            log.error(msg, e);
            throw DLCEventsException.fail(msg, e);
        }
    }

    /**
     * Closes the connection and session used for ActiveMQ, if they are not already closed.
     * Any exceptions that occur during the closing process are logged.
     */
    @Override
    public void closeAll() {
        try {
            log.info("Closing connection/session");
            if(connection!= null){
                connection.close();
            }
            if(session != null){
                session.close();
            }
            log.info("Closed connection/session");
        } catch (JMSException e) {
            log.error("Closing connection/session failed", e);
        }
    }

    /**
     * Creates a new Topic with the given topicName. The topicName is modified by replacing dots with hyphens and prepending the virtualTopicPrefix.
     *
     * @param topicName the name of the topic to be created
     * @return the newly created virtual Topic
     */
    @Override
    protected Topic createTopic(String topicName) {
        try {
            return session.createTopic(this.virtualTopicPrefix + topicName.replaceAll("\\.", "-"));
        } catch (JMSException e) {
            throw DLCEventsException.fail("Creating topic '{}' failed!", topicName, e);
        }
    }

    /**
     * Sends a message with the given body to the specified topic.
     *
     * @param body the message body to be sent
     * @param topic the topic to which the message will be sent
     */
    @Override
    protected void sendMessageToTopic(String body, Topic topic) {
        MessageProducer producer = null;
        try {
            var textMessage = session.createTextMessage(body);
            producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(textMessage);
            producer.close();
        } catch (JMSException e) {
            var msg = String.format("Publishing Message '%s' failed!", body);
            log.error(msg, e);
            throw DLCEventsException.fail(msg, e);
        } finally {
            if (producer != null) {
                try {
                    producer.close();
                } catch (JMSException e) {
                    log.error("Closing producer failed", e);
                }
            }
        }
    }



}
