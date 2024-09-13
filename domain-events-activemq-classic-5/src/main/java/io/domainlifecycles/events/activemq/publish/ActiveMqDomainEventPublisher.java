package io.domainlifecycles.events.activemq.publish;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.mq.publish.AbstractMqDomainEventPublisher;
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

public class ActiveMqDomainEventPublisher extends AbstractMqDomainEventPublisher<Topic> {

    private static final Logger log = LoggerFactory.getLogger(ActiveMqDomainEventPublisher.class);
    private final ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private final String virtualTopicPrefix;

    public ActiveMqDomainEventPublisher(
        ConnectionFactory connectionFactory,
        ObjectMapper objectMapper,
        String virtualTopicPrefix
    ) {
        super(objectMapper);
        this.connectionFactory = Objects.requireNonNull(connectionFactory, "ConnectionFactory is required!");
        connect();
        this.virtualTopicPrefix = Objects.requireNonNull(virtualTopicPrefix, "virtualTopicPrefix is required!");
    }

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

    @Override
    protected Topic createTopic(String topicName) {
        try {
            return session.createTopic(this.virtualTopicPrefix + topicName.replaceAll("\\.", "-"));
        } catch (JMSException e) {
            throw DLCEventsException.fail("Creating topic '{}' failed!", topicName, e);
        }
    }

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
