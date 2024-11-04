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

package io.domainlifecycles.events.gruelboxproxyjakartajmsidempotency;

import com.gruelbox.transactionoutbox.NoTransactionActiveException;
import io.domainlifecycles.domain.types.DomainEvent;
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
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = TestApplicationJmsGruelboxIdempotency.class)
@DirtiesContext
public class GruelboxEventHandlingJmsIdempotencyTests {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

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

    @Test
    public void testIntegrationCommit() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestCommit"+ UUID.randomUUID().toString());
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //publish same event again to simulate duplicates
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await()
            .pollDelay(5, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();

                var received = new ArrayList<>();
                received.addAll(aRepository.received);
                received.addAll(anApplicationService.received);
                received.addAll(queryClient.received);
                received.addAll(outboundService.received);
                received.addAll(aDomainService.received);
                softly.assertThat(received.stream().filter(e -> e.equals(evt)).count()).isEqualTo(5);
                softly.assertAll();
            });
    }

    @Test
    public void testIntegrationUnreceivedCommit() throws Exception{
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit"+ UUID.randomUUID().toString());
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
                softly.assertThat(queryClient.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
                softly.assertAll();
            });
    }

    @Test
    public void testIntegrationRollback() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestRollback"+ UUID.randomUUID().toString());
        DomainEvents.publish(evt);

        platformTransactionManager.rollback(status);
        //then
        await()
            .pollDelay(5, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.received).doesNotContain(evt);
                softly.assertThat(aRepository.received).doesNotContain(evt);
                softly.assertThat(anApplicationService.received).doesNotContain(evt);
                softly.assertThat(queryClient.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
                softly.assertAll();
            });

    }

    @Test
    public void testIntegrationNoTransaction() {
        //when
        var evt = new ADomainEvent("TestNoTrans"+ UUID.randomUUID().toString());
        //then
        assertThatThrownBy(() ->
            DomainEvents.publish(evt)
        ).isInstanceOf(NoTransactionActiveException.class);
    }

    @Test
    public void testIntegrationAggregateDomainEventCommit() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventCommit"+ UUID.randomUUID().toString());
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
                softly.assertThat(queryClient.received).doesNotContain(evt);
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
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventRollback"+ UUID.randomUUID().toString());
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
                softly.assertThat(queryClient.received).doesNotContain(evt);
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
        var evt = new AnAggregateDomainEvent("TestAggregateDomainWithException"+ UUID.randomUUID().toString());
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
                softly.assertThat(queryClient.received).doesNotContain(evt);
                softly.assertThat(outboundService.received).doesNotContain(evt);
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
        var evt = new ADomainEvent("TestCommit2"+ UUID.randomUUID().toString());
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
                softly.assertThat(queryClient.received).doesNotContain(evt);
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
        var evt = new CounterDomainEvent("Cnt"+ UUID.randomUUID().toString());
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        // publish same event again
        // simulates duplicate
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
