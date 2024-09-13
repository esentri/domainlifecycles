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

package io.domainlifecycles.events.activemq.nontransactionalpublish;

import io.domainlifecycles.events.activemq.api.ActiveMqDomainEventsConfiguration;
import io.domainlifecycles.events.activemq.domain.ADomainEvent;
import io.domainlifecycles.events.activemq.domain.ADomainService;
import io.domainlifecycles.events.activemq.domain.AQueryClient;
import io.domainlifecycles.events.activemq.domain.ARepository;
import io.domainlifecycles.events.activemq.domain.AnAggregate;
import io.domainlifecycles.events.activemq.domain.AnAggregateDomainEvent;
import io.domainlifecycles.events.activemq.domain.AnApplicationService;
import io.domainlifecycles.events.activemq.domain.AnOutboundService;
import io.domainlifecycles.events.activemq.domain.CounterDomainEvent;
import io.domainlifecycles.events.activemq.domain.TransactionalCounterService;
import io.domainlifecycles.events.activemq.domain.UnreceivedDomainEvent;
import io.domainlifecycles.events.api.DomainEvents;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.DeliveryMode;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@SpringBootTest(classes = TestApplicationSpringActiveMqClassicIntegration.class)
@Slf4j
public class SpringActiveMqClassicIntegrationTests {

    @Autowired
    private ConnectionFactory connectionFactory;

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
    private ActiveMqDomainEventsConfiguration activeMqDomainEventsConfiguration;

    @Test
    public void testEvents() throws JMSException {
        //given
        MessageProducer producer = null;
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        MessageConsumer consumerB = null;
        MessageConsumer consumerC = null;
        MessageConsumer consumer2 = null;
        try {
            log.info("Creating ActiveMq connection");
            connection = connectionFactory.createConnection();
            log.info("Starting ActiveMq connection");
            connection.start();
            log.info("Creating ActiveMq session");
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            log.info("Created ActiveMq session");
        //when
            //1 Topic with several consumer
            var topic = session.createTopic("VirtualTopic.abc");

            producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            //this consumer should get the message
            Queue def = session.createQueue("Consumer.def.VirtualTopic.abc");
            consumer = session.createConsumer(def);

            //exactly one of these consumers should get the message aswell
            Queue geh = session.createQueue("Consumer.geh.VirtualTopic.abc");
            consumerB = session.createConsumer(geh);
            consumerC = session.createConsumer(geh);

            Queue def2 = session.createQueue("Consumer.def2.VirtualTopic.abc");
            consumer2 = session.createConsumer(def2);
            //consumer is closed
            consumer2.close();

            //message is sent here
            var textMessage = session.createTextMessage("Test");
            producer.send(textMessage);

            //consumer is "recreated" after message was sent, consumer should get the message
            consumer2 = session.createConsumer(def2);

            var msg = (TextMessage)consumer.receive(100);
            String txt = "";
            if(msg != null){
                msg.acknowledge();
                txt = msg.getText();
            }
            msg = (TextMessage)consumerC.receive(100);
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

            //it should get the message aswell (durable message)
            msg = (TextMessage)consumer2.receive(100);
            String txtDef2 = "";
            if(msg != null){
                msg.acknowledge();
                txtDef2 = msg.getText();
                log.info("Def2 RECEIVED!");
            }

            //then
            assertThat(txt).isEqualTo("Test");
            assertThat(txt2.equals("Test")^txt3.equals("Test")).isTrue();
            assertThat(txtDef2).isEqualTo("Test");

        } catch (JMSException e) {
            log.error("Something failed", e);
        }finally {
            if(producer!= null){
                producer.close();
            }
            if(consumer!= null){
                consumer.close();
            }
            if(consumerB!= null){
                consumerB.close();
            }
            if(consumerC!= null){
                consumerC.close();
            }
            if(consumer2!= null){
                consumer2.close();
            }
            if(session!= null){
                session.close();
            }
            if(connection!= null){
                connection.close();
            }
        }
    }

    @Test
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
    public void testIntegrationReceivedPauseResume() {
        //when
        activeMqDomainEventsConfiguration.getMqDomainEventConsumer().pauseHandler(
            ADomainService.class.getName(),
            "onDomainEvent",
            ADomainEvent.class.getName()
        );
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                assertThat(activeMqDomainEventsConfiguration.getMqDomainEventConsumer().isHandlerPaused(
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

        activeMqDomainEventsConfiguration.getMqDomainEventConsumer().resumeHandler(
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
