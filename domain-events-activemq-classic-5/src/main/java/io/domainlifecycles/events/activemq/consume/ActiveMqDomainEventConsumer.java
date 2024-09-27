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

public class ActiveMqDomainEventConsumer extends AbstractMqDomainEventConsumer<MessageConsumer, TextMessage> {

    private static final Logger log = LoggerFactory.getLogger(ActiveMqDomainEventConsumer.class);

    private final ConnectionFactory connectionFactory;

    private Connection connection;
    private Map<MessageConsumer, Session> sessions = new ConcurrentHashMap<>();

    private final String virtualTopicConsumerPrefix;
    private final String virtualTopicPrefix;

    private final long receiveTimeoutMs;

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

    @Override
    protected void acknowledge(TextMessage textMessage) {
        try {
            textMessage.acknowledge();
        } catch (JMSException e) {
            log.error("Acknowledging message failed", e);
        }
    }

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
