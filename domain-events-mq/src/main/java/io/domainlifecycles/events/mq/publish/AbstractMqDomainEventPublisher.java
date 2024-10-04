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

package io.domainlifecycles.events.mq.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.exception.DLCEventsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An abstract class for publishing domain events to a message queue.
 *
 * @param <TOPIC> the type of topic used in the message queue
 *
 * @author Mario Herb
 */
public abstract class AbstractMqDomainEventPublisher<TOPIC> implements MqDomainEventPublisher{

    private static final Logger log = LoggerFactory.getLogger(AbstractMqDomainEventPublisher.class);

    private final ObjectMapper objectMapper;
    private final Map<String, TOPIC> topics = new HashMap<>();

    /**
     * Constructs an AbstractMqDomainEventPublisher with the specified ObjectMapper.
     *
     * @param objectMapper the ObjectMapper instance for serialization/deserialization (must not be null)
     */
    public AbstractMqDomainEventPublisher(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper required!");
    }

    /**
     * Establishes a connection to the message broker.
     * Subclasses should implement this method to create a connection,
     * start it, and create a session for message handling. Any exceptions that occur during the connection process
     * should be handled by logging them and throwing a DLCEventsException.
     */
    abstract protected void connect();

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(DomainEvent domainEvent) {
        var topic = getTopicForEvent(domainEvent.getClass().getName());
        String body;
        try {
            body = objectMapper.writeValueAsString(domainEvent);
        }catch (JsonProcessingException ex){
            var msg = String.format("Serialization of DomainEvent '%s' failed!", domainEvent);
            log.error(msg, ex);
            throw DLCEventsException.fail(msg,  ex);
        }
        sendMessageToTopic(body, topic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    abstract public void closeAll();

    /**
     * Sends a message with the specified body to the given topic.
     *
     * @param body the message body to send
     * @param topic the topic to send the message to
     */
    abstract protected void sendMessageToTopic(String body, TOPIC topic) ;

    private TOPIC getTopicForEvent(String domainEventClassName){
        var topicName = domainEventClassName;
        var topic = topics.get(topicName);
        if(topic == null){
            topic = createTopic(topicName);
            topics.put(topicName, topic);
        }
        return topic;
    }

    /**
     * Creates a new topic with the specified name.
     *
     * @param topicName the name of the topic to create
     * @return the created TOPIC instance
     */
    abstract protected TOPIC createTopic(String topicName);
}
