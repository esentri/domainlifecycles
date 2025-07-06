package io.domainlifecycles.autoconfig.features.single.events.jms;

import io.domainlifecycles.autoconfig.model.events.ADomainEvent;
import io.domainlifecycles.autoconfig.model.events.ADomainService;
import io.domainlifecycles.autoconfig.model.events.AQueryHandler;
import io.domainlifecycles.autoconfig.model.events.ARepository;
import io.domainlifecycles.autoconfig.model.events.AnApplicationService;
import io.domainlifecycles.autoconfig.model.events.AnOutboundService;
import io.domainlifecycles.events.api.DomainEvents;
import io.domainlifecycles.events.exception.DLCEventsException;
import jakarta.jms.Connection;
import jakarta.jms.DeliveryMode;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import java.util.UUID;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles({"test", "test-dlc-domain"})
public class SpringJmsEventAutoConfigTests {

    @Autowired
    private ADomainService aDomainService;

    @Autowired
    private ARepository aRepository;

    @Autowired
    private AnApplicationService anApplicationService;

    @Autowired
    private AQueryHandler queryHandler;

    @Autowired
    private AnOutboundService outboundService;

    @Autowired
    private ActiveMQConnectionFactory activeMQConnectionFactory;

    @Test
    @DirtiesContext
    public void testEvents() throws JMSException {
        MessageProducer producer = null;
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        MessageConsumer consumerB = null;
        MessageConsumer consumerC = null;
        try {
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            var topic = session.createTopic("a.b.c");
            producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            consumer = session.createSharedConsumer(topic, "d.e.f");
            consumerB = session.createSharedConsumer(topic, "g.e.h");
            consumerC = session.createSharedConsumer(topic, "g.e.h");

            var textMessage = session.createTextMessage("Test");
            producer.send(textMessage);

            var msg = (TextMessage)consumer.receive(100);
            String txt = "";
            if(msg != null){
                msg.acknowledge();
                txt = msg.getText();
            }
            msg = (TextMessage)consumerC.receive(1000);
            String txt3 = "";
            if(msg != null){
                msg.acknowledge();
                txt3 = msg.getText();
            }
            String txt2 = "";
            msg = (TextMessage)consumerB.receive(100);

            if(msg != null){
                msg.acknowledge();
                txt2 = msg.getText();
            }

            assertThat(txt).isEqualTo("Test");
            assertThat(txt2.equals("Test")^txt3.equals("Test")).isTrue();


        } catch (JMSException e) {
            var msg = "JakartaJmsDomainEventPublisher initialization failed";
            throw DLCEventsException.fail(msg,  e);
        }finally {
            producer.close();
            consumer.close();
            consumerB.close();
            consumerC.close();
            session.close();
            connection.close();
        }
    }

    @Test
    @DirtiesContext
    public void testIntegrationReceived() {
        //when
        var evt = new ADomainEvent("Test"+ UUID.randomUUID());
        DomainEvents.publish(evt);
        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                {
                    SoftAssertions softly = new SoftAssertions();
                    softly.assertThat(aDomainService.received).contains(evt);
                    softly.assertThat(aRepository.received).contains(evt);
                    softly.assertThat(anApplicationService.received).contains(evt);
                    softly.assertThat(queryHandler.received).contains(evt);
                    softly.assertThat(outboundService.received).contains(evt);
                    softly.assertAll();
                }
            );
    }
}
