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

package io.domainlifecycles.events.jta;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.atomikos.icatch.jta.UserTransactionManager;
import io.domainlifecycles.events.ADomainEvent;
import io.domainlifecycles.events.ADomainService;
import io.domainlifecycles.events.AQueryHandler;
import io.domainlifecycles.events.ARepository;
import io.domainlifecycles.events.AnAggregate;
import io.domainlifecycles.events.AnAggregateDomainEvent;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnOutboundService;
import io.domainlifecycles.events.UnreceivedDomainEvent;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.DomainEvents;
import io.domainlifecycles.events.jta.api.JtaInMemoryChannelFactory;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class DirectJtaTransactionalEventHandlingAfterCommitTests {

    private static ADomainService domainService;
    private static ARepository repository;
    private static AnApplicationService applicationService;
    private static AQueryHandler queryHandler;
    private static AnOutboundService outboundService;
    private static UserTransactionManager userTransactionManager;


    @BeforeAll
    public static void init() {
        Logger rootLogger =
            (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.DEBUG);

        userTransactionManager = new UserTransactionManager();

        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.events"));

        domainService = new ADomainService();
        repository = new ARepository();
        applicationService = new AnApplicationService();
        queryHandler = new AQueryHandler();
        outboundService = new AnOutboundService();

        var services = new Services();
        services.registerServiceKindInstance(domainService);
        services.registerServiceKindInstance(repository);
        services.registerServiceKindInstance(applicationService);
        services.registerServiceKindInstance(outboundService);
        services.registerServiceKindInstance(queryHandler);

        var channel = new JtaInMemoryChannelFactory(userTransactionManager, services,
            5,
            true).processingChannel("c1");
        var router = new DomainEventTypeBasedRouter(List.of(channel));
        router.defineDefaultChannel("c1");
        new ChannelRoutingConfiguration(router);
    }

    @Test
    public void testIntegrationCommit() throws Exception {
        userTransactionManager.begin();
        //when
        var evt = new ADomainEvent("TestCommit");
        DomainEvents.publish(evt);

        userTransactionManager.commit();
        //then
        assertThat(domainService.received).contains(evt);
        assertThat(repository.received).contains(evt);
        assertThat(applicationService.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        assertThat(queryHandler.received).contains(evt);
    }

    @Test
    public void testIntegrationUnreceivedCommit() throws Exception {
        userTransactionManager.begin();
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit");
        DomainEvents.publish(evt);

        userTransactionManager.commit();
        //then
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(repository.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryHandler.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationRollback() throws Exception {
        userTransactionManager.begin();
        //when
        var evt = new ADomainEvent("TestRollback");
        DomainEvents.publish(evt);
        userTransactionManager.rollback();
        //then
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(repository.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryHandler.received).doesNotContain(evt);

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
    public void testIntegrationAggregateDomainEventCommit() throws Exception {
        //when
        userTransactionManager.begin();
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventCommit");
        DomainEvents.publish(evt);
        userTransactionManager.commit();
        //then
        assertThat(repository.received).doesNotContain(evt);
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryHandler.received).doesNotContain(evt);
        var root = repository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).contains(evt);
    }

    @Test
    public void testIntegrationAggregateDomainEventRollback() throws Exception {
        //when
        userTransactionManager.begin();
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventRollback");
        DomainEvents.publish(evt);
        userTransactionManager.rollback();
        //then

        assertThat(repository.received).doesNotContain(evt);
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryHandler.received).doesNotContain(evt);
        var root = repository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationAggregateDomainEventRollbackExceptionOnHandler() throws Exception {
        //when
        userTransactionManager.begin();
        var evt = new AnAggregateDomainEvent("TestAggregateDomainWithException");
        DomainEvents.publish(evt);
        userTransactionManager.commit();
        //then

        assertThat(repository.received).doesNotContain(evt);
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryHandler.received).doesNotContain(evt);
        var root = repository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationDomainServiceExceptionRollback() throws Exception {
        //when
        userTransactionManager.begin();
        var evt = new ADomainEvent("TestDomainServiceRollback");
        DomainEvents.publish(evt);
        userTransactionManager.commit();
        //then

        assertThat(repository.received).contains(evt);
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(applicationService.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        assertThat(queryHandler.received).contains(evt);
        var root = repository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }


}
