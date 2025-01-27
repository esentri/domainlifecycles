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

package io.domainlifecycles.events.jakarta.jms.publish;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.mq.publish.AbstractMqDomainEventPublisher;
import io.domainlifecycles.events.mq.publish.MqDomainEventPublisher;
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
 * JakartaJmsDomainEventPublisher is a class that extends AbstractMqDomainEventPublisher and implements the MqDomainEventPublisher interface.
 * It is responsible for publishing DomainEvent messages to a JMS Topic using Jakarta Messaging API.
 *
 * The class requires a ConnectionFactory and an ObjectMapper to be passed in the constructor.
 *
 * When instantiated, it creates a connection and session to interact with the JMS Topic.
 *
 * It provides methods to connect to the JMS provider, close the connection and session, create a Topic, and send a message to the Topic.
 *
 * @see AbstractMqDomainEventPublisher
 * @see MqDomainEventPublisher
 *
 * @author Mario Herb
 */
public class JakartaJmsDomainEventPublisher extends AbstractMqDomainEventPublisher<Topic> {

    private static final Logger log = LoggerFactory.getLogger(JakartaJmsDomainEventPublisher.class);
    private final ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;

    /**
     * Constructor for JakartaJmsDomainEventPublisher.
     *
     * @param connectionFactory The ConnectionFactory to be used for creating connections.
     * @param objectMapper The ObjectMapper instance for serialization/deserialization.
     */
    public JakartaJmsDomainEventPublisher(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        super(objectMapper);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "ConnectionFactory is required!");
        connect();
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
            log.info("Creating session");
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            log.info("Created session");
        } catch (JMSException e) {
            var msg = "JakartaJmsDomainEventPublisher initialization failed";
            log.error(msg, e);
            throw DLCEventsException.fail(msg, e);
        }
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    protected Topic createTopic(String topicName) {
        try {
            return session.createTopic(topicName);
        } catch (JMSException e) {
            throw DLCEventsException.fail("Creating topic '{}' failed!", topicName, e);
        }
    }

    /**
     * {@inheritDoc}
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
