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

package io.domainlifecycles.events.jakartajms;

import io.domainlifecycles.events.ADomainEvent;
import io.domainlifecycles.events.ADomainService;
import io.domainlifecycles.events.AQueryClient;
import io.domainlifecycles.events.ARepository;
import io.domainlifecycles.events.AnAggregate;
import io.domainlifecycles.events.AnAggregateDomainEvent;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnOutboundService;
import io.domainlifecycles.events.CounterDomainEvent;
import io.domainlifecycles.events.TransactionalCounterService;
import io.domainlifecycles.events.UnreceivedDomainEvent;
import io.domainlifecycles.events.api.DomainEvents;
import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.exception.DLCEventsException;
import io.domainlifecycles.events.mq.api.AbstractMqConsumingConfiguration;
import jakarta.jms.Connection;
import jakarta.jms.DeliveryMode;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@SpringBootTest(classes = TestApplicationSpringJmsIntegration.class)
@Slf4j
public class SpringJmsIntegrationTests {

    @Autowired
    private ADomainService aDomainService;

    @Autowired
    private ARepository aRepository;

    @Autowired
    private AnApplicationService anApplicationService;

    @Autowired
    private AQueryClient queryClient;

    @Autowired
    private AnOutboundService outboundService;

    @Autowired
    private TransactionalCounterService transactionalCounterService;

    @Autowired
    private ActiveMQConnectionFactory activeMQConnectionFactory;

    @Autowired
    private ProcessingChannel channel;

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
            log.info("Creating ActiveMq connection");
            connection = activeMQConnectionFactory.createConnection();
            log.info("Starting ActiveMq connection");
            connection.start();
            log.info("Creating ActiveMq session");
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            log.info("Created ActiveMq session");

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
                log.info("C RECEIVED!");
            }
            String txt2 = "";
            msg = (TextMessage)consumerB.receive(100);

            if(msg != null){
                msg.acknowledge();
                txt2 = msg.getText();
                log.info("B RECEIVED!");
            }

            assertThat(txt).isEqualTo("Test");
            assertThat(txt2.equals("Test")^txt3.equals("Test")).isTrue();


        } catch (JMSException e) {
            var msg = "JakartaJmsDomainEventPublisher initialization failed";
            log.error(msg, e);
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
                    softly.assertThat(queryClient.received).contains(evt);
                    softly.assertThat(outboundService.received).contains(evt);
                    softly.assertAll();
                }
            );
    }

    @Test
    @DirtiesContext
    public void testIntegrationUnreceived() {

        //when
        var evt = new UnreceivedDomainEvent("TestUnReceived"+ UUID.randomUUID());
        DomainEvents.publish(evt);
        //then
        await()
            .pollDelay(5, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()->
                {
                    SoftAssertions softly = new SoftAssertions();
                    softly.assertThat(aDomainService.received).doesNotContain(evt);
                    softly.assertThat(aRepository.received).doesNotContain(evt);
                    softly.assertThat(anApplicationService.received).doesNotContain(evt);
                    softly.assertThat(queryClient.received).doesNotContain(evt);
                    softly.assertThat(outboundService.received).doesNotContain(evt);
                    softly.assertAll();
                }
            );
    }

    @Test
    @DirtiesContext
    public void testIntegrationAggregateDomainEvent() {
        //when

        var evt = new AnAggregateDomainEvent("TestAggregateDomainEvent"+ UUID.randomUUID());
        DomainEvents.publish(evt);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
            {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aRepository.received).doesNotContain(evt);
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).doesNotContain(evt);
                softly.assertThat(queryClient.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
                var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
                softly.assertThat(root.received).contains(evt);
                softly.assertAll();
            }
        );
    }

    @Test
    @DirtiesContext
    public void testIntegrationAggregateDomainEventExceptionOnHandler() {
        //when

        var evt = new AnAggregateDomainEvent("TestAggregateDomainWithException"+ UUID.randomUUID());
        DomainEvents.publish(evt);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
            {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aRepository.received).doesNotContain(evt);
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).doesNotContain(evt);
                softly.assertThat(queryClient.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
                var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
                softly.assertThat(root.received).doesNotContain(evt);
                softly.assertAll();
            }
        );
    }

    @Test
    @DirtiesContext
    public void testIntegrationDomainServiceExceptionRollback() {
        //when

        var evt = new ADomainEvent("TestDomainServiceRollback"+ UUID.randomUUID());
        DomainEvents.publish(evt);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aRepository.received).contains(evt);
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).contains(evt);
                softly.assertThat(queryClient.received).contains(evt);
                softly.assertThat(outboundService.received).contains(evt);
                var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
                softly.assertThat(root.received).doesNotContain(evt);
                softly.assertAll();
            }
        );
    }

    @Test
    @DirtiesContext
    public void testTransactionalBehaviourWithCounterService() {

        var cnt = transactionalCounterService.getCurrentCounterValue();
        //2 listener methods receive this event, they both run increase sql statements
        //one of those handlers throws an illegal state exception, rolling back one of the transactions
        //so finally the counter should only be increased once
        // but the event in the outbox is processed successfully,
        // the handlers listening on the event are transactionally independent of each other
        var evt = new CounterDomainEvent("Cnt");
        DomainEvents.publish(evt);

        //then

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(transactionalCounterService.getCurrentCounterValue()).isEqualTo(cnt+1)
            );
    }

    @Test
    @DirtiesContext
    public void testIntegrationReceivedPauseResume() {
        //when
        var config = ((AbstractMqConsumingConfiguration)channel.getConsumingConfiguration());
        config.getMqDomainEventConsumer().pauseHandler(
            ADomainService.class.getName(),
            "onDomainEvent",
            ADomainEvent.class.getName()
        );
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
        assertThat(config.getMqDomainEventConsumer().isHandlerPaused(
            ADomainService.class.getName(),
            "onDomainEvent",
            ADomainEvent.class.getName()
        )).isTrue();});

        var evt = new ADomainEvent("Test"+ UUID.randomUUID());
        DomainEvents.publish(evt);
        //then
        await()
            .pollDelay(2, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()->
                {
                    SoftAssertions softly = new SoftAssertions();
                    softly.assertThat(aDomainService.received).doesNotContain(evt);
                    softly.assertThat(aRepository.received).contains(evt);
                    softly.assertThat(anApplicationService.received).contains(evt);
                    softly.assertThat(queryClient.received).contains(evt);
                    softly.assertThat(outboundService.received).contains(evt);
                    softly.assertAll();
                }
            );

        config.getMqDomainEventConsumer().resumeHandler(
            ADomainService.class.getName(),
            "onDomainEvent",
            ADomainEvent.class.getName()
        );

        await()
            .pollDelay(2, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> assertThat(aDomainService.received).contains(evt));
    }

}
