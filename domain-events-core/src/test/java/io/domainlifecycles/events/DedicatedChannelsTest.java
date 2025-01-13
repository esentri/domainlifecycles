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

package io.domainlifecycles.events;

import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.DomainEvents;
import io.domainlifecycles.events.domain.AQueryHandler;
import io.domainlifecycles.events.inmemory.InMemoryChannelFactory;
import io.domainlifecycles.events.domain.ADomainEvent;
import io.domainlifecycles.events.domain.ADomainService;
import io.domainlifecycles.events.domain.ARepository;
import io.domainlifecycles.events.domain.AnAggregate;
import io.domainlifecycles.events.domain.AnAggregateDomainEvent;
import io.domainlifecycles.events.domain.AnApplicationService;
import io.domainlifecycles.events.domain.AnOutboundService;
import io.domainlifecycles.events.domain.UnreceivedDomainEvent;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DedicatedChannelsTest {

    private static ADomainService domainService;
    private static ARepository repository;
    private static AnApplicationService applicationService;
    private static AQueryHandler queryHandler;
    private static AnOutboundService outboundService;

    @BeforeAll
    public static void init(){
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.events.domain"));

        domainService = new ADomainService();
        repository = new ARepository();
        applicationService = new AnApplicationService();
        queryHandler = new AQueryHandler();
        outboundService = new AnOutboundService();

        var services = new Services();
        services.registerServiceKindInstance(domainService);
        services.registerServiceKindInstance(repository);
        services.registerServiceKindInstance(applicationService);
        services.registerServiceKindInstance(queryHandler);
        services.registerServiceKindInstance(outboundService);

        var firstChannel = new InMemoryChannelFactory(services).processingChannel("first");
        var secondChannel = new InMemoryChannelFactory(services).processingChannel("second");
        var router = new DomainEventTypeBasedRouter(List.of(firstChannel, secondChannel));
        router.defineExplicitRoute(ADomainEvent.class, "first");
        router.defineExplicitRoute(AnAggregateDomainEvent.class, "second");
        new ChannelRoutingConfiguration(router);
    }

    @Test
    public void testIntegrationDomainEvent(){

        //when
        var evt = new ADomainEvent("Test");
        DomainEvents.publish(evt);
        //then
        assertThat(domainService.received).contains(evt);
        assertThat(repository.received).contains(evt);
        assertThat(applicationService.received).contains(evt);
        assertThat(queryHandler.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
    }

    @Test
    public void testIntegrationDomainEventFail(){
        //when
        var evt = new ADomainEvent("TestDomainServiceRollback");
        DomainEvents.publish(evt);
        //then
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(repository.received).contains(evt);
        assertThat(applicationService.received).contains(evt);
        assertThat(queryHandler.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
    }

    @Test
    public void testIntegrationUnreceivedCommit() {
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit");
        //then
        assertThatThrownBy(() -> DomainEvents.publish(evt)).hasMessageContaining("No default channel defined and no routing configured for DomainEvent");
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(repository.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(queryHandler.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationAggregateDomainEvent(){
        //when
        var evt = new AnAggregateDomainEvent("Test");
        DomainEvents.publish(evt);
        //then
        assertThat(repository.received).doesNotContain(evt);
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(queryHandler.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        var root = repository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).contains(evt);
    }
}
