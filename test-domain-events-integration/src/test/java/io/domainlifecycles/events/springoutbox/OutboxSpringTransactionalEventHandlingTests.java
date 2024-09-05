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

package io.domainlifecycles.events.springoutbox;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import io.domainlifecycles.events.spring.outbox.api.ProcessingResult;
import io.domainlifecycles.events.spring.outbox.api.SpringOutboxConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@DirtiesContext
public class OutboxSpringTransactionalEventHandlingTests {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private ADomainService aDomainService;

    @Autowired
    private ARepository aRepository;

    @Autowired
    private AnApplicationService anApplicationService;

    @Autowired
    private AnOutboundService outboundService;

    @Autowired
    private AQueryClient queryClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringOutboxConfiguration config;

    private static final String SELECT = "SELECT processing_result FROM outbox WHERE processing_result IS NOT NULL AND domain_event LIKE '%'||?||'%';";

    private static final String SELECT_EVENT = "SELECT domain_event, processing_result FROM outbox WHERE domain_event LIKE '%'||?||'%';";

    @BeforeEach
    public void init(){
        config.getOutboxPoller().setPollingDelayMilliseconds(1000);
        config.getOutboxPoller().setPollingPeriodMilliseconds(1000);
    }

    @AfterEach
    public void after(){
        config.getOutboxPoller().setPollingDelayMilliseconds(5000);
        config.getOutboxPoller().setPollingPeriodMilliseconds(5000);
    }

    private String processingResultForEvent(String eventMessage){
        var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var status = platformTransactionManager.getTransaction(def);
        String processingResult = jdbcTemplate.query(SELECT, (r, rn)-> r.getString(1), eventMessage).stream().findFirst().orElse(null);
        platformTransactionManager.commit(status);
        return processingResult;
    }

    @Test
    public void testIntegrationCommit() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestCommit"+UUID.randomUUID());
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);
        //then

        await().atMost(10, SECONDS).until(() -> processingResultForEvent(evt.message()) != null);

        var processingResult = processingResultForEvent(evt.message());
        assertThat(processingResult).isEqualTo(ProcessingResult.OK.name());

        assertThat(aDomainService.received).contains(evt);
        assertThat(aRepository.received).contains(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
    }

    @Test
    public void testIntegrationRollback() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new ADomainEvent("TestRollback"+UUID.randomUUID());
        DomainEvents.publish(evt);

        platformTransactionManager.rollback(status);

        var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var statusTest = platformTransactionManager.getTransaction(def);
        String event = jdbcTemplate.query(SELECT_EVENT, (r, rn)-> r.getString(1), evt.message()).stream().findFirst().orElse(null);
        assertThat(event).isNull();
        platformTransactionManager.commit(statusTest);

        //then
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);

    }

    @Test
    public void testIntegrationUnreceivedCommit() throws Exception{
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new UnreceivedDomainEvent("TestUnReceivedCommit");
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);

        //then
        await().atMost(10, SECONDS).until(() -> processingResultForEvent(evt.message()) != null);

        var processingResult = processingResultForEvent(evt.message());
        assertThat(processingResult).isEqualTo(ProcessingResult.OK.name());

        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationNoTransaction() {

        //when
        var evt = new ADomainEvent("TestNoTrans"+UUID.randomUUID());
        DomainEvents.publish(evt);
        //then
        await().atMost(10, SECONDS).until(() -> processingResultForEvent(evt.message()) != null);
        var processingResult = processingResultForEvent(evt.message());
        assertThat(processingResult).isEqualTo(ProcessingResult.OK.name());

        assertThat(aDomainService.received).contains(evt);
        assertThat(aRepository.received).contains(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
    }

    @Test
    public void testIntegrationAggregateDomainEventCommit() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainEventCommit"+UUID.randomUUID());
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await().atMost(10, SECONDS).until(() -> processingResultForEvent(evt.message()) != null);
        var processingResult = processingResultForEvent(evt.message());
        assertThat(processingResult).isEqualTo(ProcessingResult.OK.name());

        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).contains(evt);
    }


    @Test
    public void testIntegrationAggregateDomainEventRollbackExceptionOnHandler() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestAggregateDomainWithException"+UUID.randomUUID());
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await().atMost(10, SECONDS).until(() -> processingResultForEvent(evt.message()) != null);
        var processingResult = processingResultForEvent(evt.message());
        assertThat(processingResult).isEqualTo(ProcessingResult.FAILED.name());

        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationDomainServiceExceptionRollback() throws Exception{
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new ADomainEvent("TestDomainServiceRollback"+UUID.randomUUID());
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await().atMost(10, SECONDS).until(() -> processingResultForEvent(evt.message()) != null);
        var processingResult = processingResultForEvent(evt.message());
        assertThat(processingResult).isEqualTo(ProcessingResult.FAILED_PARTIALLY.name());

        assertThat(aRepository.received).contains(evt);
        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(anApplicationService.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
        assertThat(queryClient.received).contains(evt);
        var root = aRepository.findById(new AnAggregate.AggregateId(1L)).orElseThrow();
        assertThat(root.received).doesNotContain(evt);
    }

    @Test
    public void testIntegrationExternalExceptionBeforeCommit() {

        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void beforeCommit(boolean readOnly) {
                throw new IllegalStateException("Error");
            }
        });
        //when
        var evt = new ADomainEvent("TestCommit"+UUID.randomUUID());
        DomainEvents.publish(evt);
        try {
            platformTransactionManager.commit(status);
        }catch (Throwable t){
        }
        //then
        String event = jdbcTemplate.query(SELECT_EVENT, (r, rn)-> r.getString(1), evt.message()).stream().findFirst().orElse(null);
        assertThat(event).isNull();

        assertThat(aDomainService.received).doesNotContain(evt);
        assertThat(aRepository.received).doesNotContain(evt);
        assertThat(anApplicationService.received).doesNotContain(evt);
        assertThat(outboundService.received).doesNotContain(evt);
        assertThat(queryClient.received).doesNotContain(evt);
    }

}
