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

package io.domainlifecycles.events.spring.before;

import io.domainlifecycles.events.ADomainEvent;
import io.domainlifecycles.events.ADomainService;
import io.domainlifecycles.events.AQueryClient;
import io.domainlifecycles.events.ARepository;
import io.domainlifecycles.events.AnAggregate;
import io.domainlifecycles.events.AnAggregateDomainEvent;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnOutboundService;
import io.domainlifecycles.events.UnreceivedDomainEvent;
import io.domainlifecycles.events.api.DomainEvents;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestApplicationBeforeCommit.class)
@DirtiesContext
public class DirectSpringTransactionalEventHandlingBeforeCommitTests {

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

    @Test
    public void testIntegrationCommit() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestCommit");
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);
        //then
        assertThat(aDomainService.received).contains(evt);
        assertThat(aRepository.received).contains(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
    }

    @Test
    public void testIntegrationUnreceivedCommit() throws Exception {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit");
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);
        //then
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationRollback() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestRollback");
        DomainEvents.publish(evt);

        platformTransactionManager.rollback(status);
        //then
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);

    }

    @Test
    public void testIntegrationNoTransaction() {
        //when
        var evt = new ADomainEvent("TestNoTrans");
        //then
        assertThatThrownBy(() ->
            DomainEvents.publish(evt)
        ).hasMessageContaining("No transaction active, but active transaction is required! Event dispatching failed for");
    }

    @Test
    public void testIntegrationAggregateDomainEventCommit() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventCommit");
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then

        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).contains(evt);
    }

    @Test
    public void testIntegrationAggregateDomainEventRollback() throws Exception {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventRollback");
        DomainEvents.publish(evt);
        platformTransactionManager.rollback(status);
        //then

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

        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationDomainServiceExceptionRollback() throws Exception {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new ADomainEvent("TestDomainServiceRollback");
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then

        assertThat(aRepository.received).contains(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }

    @Test
    public void testGhostEvent(){
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
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
        assertThat(aDomainService.received).contains(evt);
        assertThat(aRepository.received).contains(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
    }
}
