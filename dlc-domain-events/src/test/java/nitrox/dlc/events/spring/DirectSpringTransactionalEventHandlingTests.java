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

package nitrox.dlc.events.spring;

import nitrox.dlc.events.ADomainEvent;
import nitrox.dlc.events.ADomainService;
import nitrox.dlc.events.AReadModelProvider;
import nitrox.dlc.events.ARepository;
import nitrox.dlc.events.AnAggregate;
import nitrox.dlc.events.AnAggregateDomainEvent;
import nitrox.dlc.events.AnApplicationService;
import nitrox.dlc.events.AnOutboundService;
import nitrox.dlc.events.PassThroughDomainEvent;
import nitrox.dlc.events.UnreceivedDomainEvent;
import nitrox.dlc.events.api.DomainEvents;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest()
public class DirectSpringTransactionalEventHandlingTests {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private ADomainService aDomainService;

    @Autowired
    private ARepository aRepository;

    @Autowired
    private AnApplicationService anApplicationService;

    @Autowired
    private AReadModelProvider readModelProvider;

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
        assertThat(readModelProvider.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
    }

    @Test
    public void testIntegrationUnreceivedCommit() throws Exception{
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit");
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);
        //then
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(readModelProvider.received).doesNotContain(evt);
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
        assertThat(readModelProvider.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);

    }

    @Test
    public void testIntegrationNoTransaction() {

        //when
        var evt = new ADomainEvent("TestNoTrans");
        DomainEvents.publish(evt);

        //then
        assertThat(aDomainService.received).contains(evt);
        assertThat(aRepository.received).contains(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(readModelProvider.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
    }

    @Test
    public void testIntegrationRollbackButConfiguredPassThrough() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new PassThroughDomainEvent("TestRollbackPassThrough");
        DomainEvents.publish(evt);

        platformTransactionManager.rollback(status);
        //then
        assertThat(aDomainService.received).contains(evt);
        assertThat(aRepository.received).contains(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(readModelProvider.received).contains(evt);
        assertThat(outboundService.received).contains(evt);


    }

    @Test
    public void testIntegrationCommitConfiguredPassThrough() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new PassThroughDomainEvent("TestRollbackPassThrough");
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);
        //then
        assertThat(aDomainService.received).contains(evt);
        assertThat(aRepository.received).contains(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(readModelProvider.received).contains(evt);
        assertThat(outboundService.received).contains(evt);

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
        assertThat(readModelProvider.received).doesNotContain(evt);
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

        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(readModelProvider.received).doesNotContain(evt);
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
        assertThat(readModelProvider.received).doesNotContain(evt);
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

        assertThat(aRepository.received).contains(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(readModelProvider.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }


}
