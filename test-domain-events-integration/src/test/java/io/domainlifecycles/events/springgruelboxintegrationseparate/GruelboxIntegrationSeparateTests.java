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

package io.domainlifecycles.events.springgruelboxintegrationseparate;

import com.gruelbox.transactionoutbox.TransactionOutboxEntry;
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
import io.domainlifecycles.events.MyTransactionOutboxListener;
import io.domainlifecycles.events.TransactionalCounterService;
import io.domainlifecycles.events.UnreceivedDomainEvent;
import io.domainlifecycles.events.api.DomainEvents;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = TestApplicationGruelboxIntegrationSeparate.class)
@Slf4j
public class GruelboxIntegrationSeparateTests {

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
    private MyTransactionOutboxListener outboxListener;

    @Autowired
    private TransactionalCounterService transactionalCounterService;

    private boolean match(TransactionOutboxEntry entry, DomainEvent domainEvent){
        return domainEvent.equals(entry.getInvocation().getArgs()[0]);
    }

    @Test
    @DirtiesContext
    public void testIntegrationCommit() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestCommit"+ UUID.randomUUID());
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(1)
            );

        assertThat(aDomainService.received).contains(evt);
        assertThat(aRepository.received).contains(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
    }

    @Test
    @DirtiesContext
    public void testIntegrationUnreceivedCommit() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit"+ UUID.randomUUID());
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);
        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(1)
            );


        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
    }

    @Test
    @DirtiesContext
    public void testIntegrationRollback() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestRollback"+ UUID.randomUUID());
        DomainEvents.publish(evt);

        platformTransactionManager.rollback(status);
        await()
            .atMost(10, SECONDS)
            .pollDelay(9, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(0)
            );
        //then
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);

    }

    @Test
    @DirtiesContext
    public void testIntegrationAggregateDomainEventCommit() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventCommit"+ UUID.randomUUID());
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(1)
            );

        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).contains(evt);
    }

    @Test
    @DirtiesContext
    public void testIntegrationAggregateDomainEventRollback() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventRollback"+ UUID.randomUUID());
        DomainEvents.publish(evt);
        platformTransactionManager.rollback(status);
        //then
        await()
            .atMost(10, SECONDS)
            .pollDelay(9, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(0)
            );

        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }

    @Test
    @DirtiesContext
    public void testIntegrationAggregateDomainEventRollbackExceptionOnHandler() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainWithException"+ UUID.randomUUID());
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(1)
            );

        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }

    @Test
    @DirtiesContext
    public void testIntegrationDomainServiceExceptionRollback() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new ADomainEvent("TestDomainServiceRollback"+ UUID.randomUUID());
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(1)
            );

        assertThat(aRepository.received).contains(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }

    @Test
    @DirtiesContext
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
            .untilAsserted(() ->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(1)
            );

        assertThat(transactionalCounterService.getCurrentCounterValue()).isEqualTo(cnt + 1);
    }

}
