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

package io.domainlifecycles.events.springgruelboxintegration;

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
import io.domainlifecycles.events.PassThroughDomainEvent;
import io.domainlifecycles.events.UnreceivedDomainEvent;
import io.domainlifecycles.events.api.DomainEvents;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = TestApplicationGruelboxIntegration.class)
@Slf4j
public class GruelboxIntegrationTest {

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

    private boolean match(TransactionOutboxEntry entry, DomainEvent domainEvent){
        return domainEvent.equals(entry.getInvocation().getArgs()[0]);
    }

    @Test
    public void testIntegrationCommit() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestCommit");
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);

        //then
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(aDomainService.received).contains(evt);
        assertions.assertThat(aRepository.received).contains(evt);
        assertions.assertThat(anApplicationService.received).contains(evt);
        assertions.assertThat(queryClient.received).contains(evt);
        assertions.assertThat(outboundService.received).contains(evt);

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(1)
            );
        assertions.assertAll();
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
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(1)
            );
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(aDomainService.received).doesNotContain(evt);
        assertions.assertThat(aRepository.received).doesNotContain(evt);
        assertions.assertThat(anApplicationService.received).doesNotContain(evt);
        assertions.assertThat(queryClient.received).doesNotContain(evt);
        assertions.assertThat(outboundService.received).doesNotContain(evt);
        assertions.assertAll();
    }

    @Test
    public void testIntegrationRollback() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestRollback");
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
    public void testIntegrationAggregateDomainEventCommit() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventCommit");
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
    public void testIntegrationAggregateDomainEventRollback() throws Exception{
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventRollback");
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
    public void testIntegrationAggregateDomainEventRollbackExceptionOnHandler() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainWithException");
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
    public void testIntegrationDomainServiceExceptionRollback() throws Exception{
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new ADomainEvent("TestDomainServiceRollback");
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


}
