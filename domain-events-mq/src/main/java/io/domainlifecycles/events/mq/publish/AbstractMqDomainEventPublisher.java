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

public abstract class AbstractMqDomainEventPublisher<TOPIC> implements MqDomainEventPublisher{

    private static final Logger log = LoggerFactory.getLogger(AbstractMqDomainEventPublisher.class);

    private final ObjectMapper objectMapper;
    private final Map<String, TOPIC> topics = new HashMap<>();

    public AbstractMqDomainEventPublisher(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "ObjectMapper required!");
    }

    abstract protected void connect();

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

    @Override
    abstract public void closeAll();

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

    abstract protected TOPIC createTopic(String topicName);
}
