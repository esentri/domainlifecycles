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

package nitrox.dlc.events.nontransactional;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import nitrox.dlc.events.ADomainEvent;
import nitrox.dlc.events.ADomainService;
import nitrox.dlc.events.AQueryClient;
import nitrox.dlc.events.ARepository;
import nitrox.dlc.events.AnAggregate;
import nitrox.dlc.events.AnAggregateDomainEvent;
import nitrox.dlc.events.AnApplicationService;
import nitrox.dlc.events.AnOutboundService;
import nitrox.dlc.events.UnreceivedDomainEvent;
import nitrox.dlc.events.api.DomainEvents;
import nitrox.dlc.events.api.DomainEventsConfiguration;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.reflect.ReflectiveDomainMirrorFactory;
import nitrox.dlc.services.Services;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;


public class DirectNonTransactionalEventHandlingTests {

    private static ADomainService domainService;
    private static ARepository repository;
    private static AnApplicationService applicationService;
    private static AQueryClient queryClient;
    private static AnOutboundService outboundService;

    @BeforeAll
    public static void init(){
        Logger rootLogger =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.DEBUG);
        Domain.initialize(new ReflectiveDomainMirrorFactory("nitrox.dlc.events"));

        domainService = new ADomainService();
        repository = new ARepository();
        applicationService = new AnApplicationService();
        queryClient = new AQueryClient();
        outboundService = new AnOutboundService();

        var services = new Services();
        services.registerDomainServiceInstance(domainService);
        services.registerRepositoryInstance(repository);
        services.registerApplicationServiceInstance(applicationService);
        services.registerQueryClientInstance(queryClient);
        services.registerOutboundServiceInstance(outboundService);

        var configBuilder = new DomainEventsConfiguration.DomainEventsConfigurationBuilder();
        configBuilder.withServiceProvider(services);

        configBuilder.make();
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
        assertThat(queryClient.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
    }

    @Test
    public void testIntegrationUnreceivedCommit() {
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit");
        DomainEvents.publish(evt);
        //then
        assertThat(domainService.received).doesNotContain(evt);
        assertThat(repository.received).doesNotContain(evt);
        assertThat(applicationService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
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
        assertThat(queryClient.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        var root = repository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).contains(evt);
    }

    //TODO test fail non transactional
}
