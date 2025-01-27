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

package io.domainlifecycles.events.gruelboxproxyjakartajms;

import com.gruelbox.transactionoutbox.NoTransactionActiveException;
import io.domainlifecycles.events.ADomainEvent;
import io.domainlifecycles.events.ADomainService;
import io.domainlifecycles.events.AQueryHandler;
import io.domainlifecycles.events.ARepository;
import io.domainlifecycles.events.AnAggregate;
import io.domainlifecycles.events.AnAggregateDomainEvent;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnOutboundService;
import io.domainlifecycles.events.CounterDomainEvent;
import io.domainlifecycles.events.TransactionalCounterService;
import io.domainlifecycles.events.UnreceivedDomainEvent;
import io.domainlifecycles.events.api.DomainEvents;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = TestApplicationJmsGruelbox.class)
@DirtiesContext
public class GruelboxEventHandlingJmsTests {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

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
    private TransactionalCounterService transactionalCounterService;

    @Test
    public void testIntegrationCommit() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestCommit");
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);
        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.received).contains(evt);
                softly.assertThat(aRepository.received).contains(evt);
                softly.assertThat(anApplicationService.received).contains(evt);
                softly.assertThat(queryHandler.received).contains(evt);
                softly.assertThat(outboundService.received).contains(evt);
                softly.assertAll();
            });
    }

    @Test
    public void testIntegrationUnreceivedCommit() throws Exception{
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit");
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);
        //then
        await()
            .pollDelay(3, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(aRepository.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).doesNotContain(evt);
                softly.assertThat(queryHandler.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
                softly.assertAll();
            });
    }

    @Test
    public void testIntegrationRollback() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestRollback");
        DomainEvents.publish(evt);

        platformTransactionManager.rollback(status);
        //then
        await()
            .pollDelay(3, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(aRepository.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).doesNotContain(evt);
                softly.assertThat(queryHandler.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
                softly.assertAll();
            });

    }

    @Test
    public void testIntegrationNoTransaction() {
        //when
        var evt = new ADomainEvent("TestNoTrans");
        //then
        assertThatThrownBy(() ->
            DomainEvents.publish(evt)
        ).isInstanceOf(NoTransactionActiveException.class);
    }

    @Test
    public void testIntegrationAggregateDomainEventCommit() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventCommit");
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await()
            .pollDelay(3, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aRepository.received).doesNotContain(evt);
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).doesNotContain(evt);
                softly.assertThat(queryHandler.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
                var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
                softly.assertThat(root.received).contains(evt);
                softly.assertAll();
            });
    }

    @Test
    public void testIntegrationAggregateDomainEventRollback() throws Exception{
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventRollback");
        DomainEvents.publish(evt);
        platformTransactionManager.rollback(status);
        //then
        await()
            .pollDelay(3, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aRepository.received).doesNotContain(evt);
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).doesNotContain(evt);
                softly.assertThat(queryHandler.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
                var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
                softly.assertThat(root.received).doesNotContain(evt);
                softly.assertAll();
            })
        ;
    }

    @Test
    public void testIntegrationAggregateDomainEventRollbackExceptionOnHandler() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainWithException");
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await()
            .pollDelay(3, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aRepository.received).doesNotContain(evt);
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).doesNotContain(evt);
                softly.assertThat(queryHandler.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
                var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
                softly.assertThat(root.received).doesNotContain(evt);
                softly.assertAll();
            });
    }

    @Test
    public void testIntegrationDomainServiceExceptionRollback() throws Exception{
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new ADomainEvent("TestDomainServiceRollback");
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await()
            .pollDelay(3, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aRepository.received).contains(evt);
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).contains(evt);
                softly.assertThat(queryHandler.received).contains(evt);
                softly.assertThat(outboundService.received).contains(evt);
                var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
                softly.assertThat(root.received).doesNotContain(evt);
                softly.assertAll();
            });
    }

    @Test
    public void testExceptionBeforeCommit(){
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void beforeCommit(boolean readOnly) {
                throw new IllegalStateException("Error");
            }
        });

        //when
        var evt = new ADomainEvent("TestCommit2");
        DomainEvents.publish(evt);


        try {
            platformTransactionManager.commit(status);
        }catch (Throwable t){
        }

        //then
        await()
            .pollDelay(3, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(aRepository.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).doesNotContain(evt);
                softly.assertThat(queryHandler.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
                softly.assertAll();
            });
    }

    @Test
    public void testTransactionalBehaviourWithCounterService() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var cnt = transactionalCounterService.getCurrentCounterValue();
        //2 listener methods receive this event, they both run increase sql statements
        //one of those handlers throws an illegal state exception, rolling back one of the transactions
        //so finally the counter should only be increased once
        // but the event in the outbox is processed successfully,
        // the handlers listening on the event are transactionally independent of each other
        var evt = new CounterDomainEvent("Cnt");
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(transactionalCounterService.getCurrentCounterValue()).isEqualTo(cnt+1)
            );
    }
}
