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

package io.domainlifecycles.jooq.persistence.tests.manyToManyWithJoinEntity;

import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.domain.types.clone.EntityCloner;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyA;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyAId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyB;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyBId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoin;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoinId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToManyId;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManyToManyAggregateRootRepository_ITest extends BasePersistence_ITest {

    private ManyToManyAggregateRootRepository manyToManyAggregateRootRepository;

    @BeforeAll
    public void init(){
        manyToManyAggregateRootRepository = new ManyToManyAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }
    @Test
    public void testUpdateCompleteAddEntityDuplicatesDifferentRefs(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().add(TestEntityManyToManyJoin.builder()
            .setId(new TestEntityManyToManyJoinId(125L))
            .setTestEntityManyToManyB(
                TestEntityManyToManyB.builder()
                .setId(new TestEntityManyToManyBId(126L))
                .setName("NEW")
                .build())
            .setTestEntityManyToManyAId(insertedCopy.getTestEntityManyToManyAList().get(0).getId())
            .build()
        ) ;
        insertedCopy.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().add(TestEntityManyToManyJoin.builder()
            .setId(new TestEntityManyToManyJoinId(127L))
            .setTestEntityManyToManyB(
                TestEntityManyToManyB.builder()
                    .setId(new TestEntityManyToManyBId(126L))
                    .setName("NEW")
                    .build())
            .setTestEntityManyToManyAId(insertedCopy.getTestEntityManyToManyAList().get(1).getId())
            .build()
        ) ;
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(2).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteAddEntityDuplicatesDifferentRefsInconsitent(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().add(TestEntityManyToManyJoin.builder()
            .setId(new TestEntityManyToManyJoinId(125L))
            .setTestEntityManyToManyB(
                TestEntityManyToManyB.builder()
                    .setId(new TestEntityManyToManyBId(126L))
                    .setName("NEW1")
                    .build())
            .setTestEntityManyToManyAId(insertedCopy.getTestEntityManyToManyAList().get(0).getId())
            .build()
        ) ;
        insertedCopy.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().add(TestEntityManyToManyJoin.builder()
            .setId(new TestEntityManyToManyJoinId(127L))
            .setTestEntityManyToManyB(
                TestEntityManyToManyB.builder()
                    .setId(new TestEntityManyToManyBId(126L))
                    .setName("NEW")
                    .build())
            .setTestEntityManyToManyAId(insertedCopy.getTestEntityManyToManyAList().get(1).getId())
            .build()
        ) ;
         persistenceEventTestHelper.resetEventsCaught();
        //when
        assertThrows(DLCPersistenceException.class, () -> manyToManyAggregateRootRepository.update(insertedCopy)
        );

    }

    @Test
    public void testUpdateCompleteAddEntityDuplicatesSameRef(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityManyToManyB b = TestEntityManyToManyB.builder()
            .setId(new TestEntityManyToManyBId(126L))
            .setName("NEW")
            .build();
        insertedCopy.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().add(TestEntityManyToManyJoin.builder()
            .setId(new TestEntityManyToManyJoinId(125L))
            .setTestEntityManyToManyB(b)
            .setTestEntityManyToManyAId(insertedCopy.getTestEntityManyToManyAList().get(0).getId())
            .build()
        ) ;
        insertedCopy.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().add(TestEntityManyToManyJoin.builder()
            .setId(new TestEntityManyToManyJoinId(127L))
            .setTestEntityManyToManyB(b)
            .setTestEntityManyToManyAId(insertedCopy.getTestEntityManyToManyAList().get(1).getId())
            .build()
        ) ;
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(2).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteDeleteEntityDuplicates(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().remove(1);
        insertedCopy.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().remove(1);
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteEntityUpdateDuplicates(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(1).getTestEntityManyToManyB().setName("UPD");
        insertedCopy.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1).getTestEntityManyToManyB().setName("UPD");
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateCompleteEntityUpdateDuplicatesInconsistent(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(1).getTestEntityManyToManyB().setName("UPD");
         persistenceEventTestHelper.resetEventsCaught();
        //then
        assertThrows( DLCPersistenceException.class,
            () -> manyToManyAggregateRootRepository.update(insertedCopy)
        );

    }

    @Test
    public void testInsertOnlyRoot(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyOnlyRoot();
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository
            .findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));
    }

    @Test
    public void testInsertComplete(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository
            .findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(0).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(1).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityManyToManyAList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(0).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted.getTestEntityManyToManyAList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));

    }


    @Test
    public void testUpdateSimpleOnlyRoot(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyOnlyRoot();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .isEqualTo(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));
    }

    @Test
    public void testUpdateSimpleComplete(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));

    }

    @Test
    public void testUpdateSimpleCompleteOnlyEntity(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.getTestEntityManyToManyAList().get(0).setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntityManyToManyAList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));

    }

    @Test
    public void testUpdateSimpleInsertEntity(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyOnlyRoot();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityManyToManyA testEntityManyToManyA = TestEntityManyToManyA.builder()
            .setName("TestEntityAdded")
            .setId(new TestEntityManyToManyAId(3l))
            .setTestRootManyToMany(insertedCopy)
            .build();

        insertedCopy.setTestEntityManyToManyAList(Arrays.asList(testEntityManyToManyA));
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityManyToManyAList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));

    }

    @Test
    public void testUpdateCompleteInsertEntity(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityManyToManyA testEntityManyToManyA = TestEntityManyToManyA.builder()
            .setName("TestEntityAdded")
            .setId(new TestEntityManyToManyAId(15l))
            .setTestRootManyToMany(insertedCopy)
            .build();

        insertedCopy.getTestEntityManyToManyAList().add(testEntityManyToManyA);
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityManyToManyAList().get(2));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));

    }

    @Test
    public void testUpdateCompleteDeleteEntity(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntityManyToManyAList().remove(1);
         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(0).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));

    }

    @Test
    public void testUpdateCompleteInsertEntityAndDeleteEntityAndUpdateEntity(){
        //given
        TestRootManyToMany tr = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(tr);
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        TestEntityManyToManyA testEntityManyToManyA = TestEntityManyToManyA.builder()
            .setName("TestEntityAdded")
            .setId(new TestEntityManyToManyAId(15l))
            .setTestRootManyToMany(insertedCopy)
            .build();

        insertedCopy.getTestEntityManyToManyAList().add(testEntityManyToManyA);
        insertedCopy.getTestEntityManyToManyAList().get(1).setName("UPDATED");
        insertedCopy.getTestEntityManyToManyAList().remove(0);

         persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(new TestRootManyToManyId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(0).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated.getTestEntityManyToManyAList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated.getTestEntityManyToManyAList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));
    }

    @Test
    public void testUpdateCompleteDeleteEntityAndUpdateRoot(){
        //given
        TestRootManyToMany trs = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setName("UPDATED");
        insertedCopy.getTestEntityManyToManyAList().remove(0);
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(0).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));

    }

    @Test
    public void testDeleteComplete(){
        //given
        TestRootManyToMany trs = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootManyToMany> deleted = manyToManyAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(0).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(1).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityManyToManyAList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(0));
        if(!persistenceEventTestHelper.expectedEvents.stream().anyMatch(e -> e.domainObject.equals(deleted.get().getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(0).getTestEntityManyToManyB()))){
            persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(0).getTestEntityManyToManyB());
        }
        if(!persistenceEventTestHelper.expectedEvents.stream().anyMatch(e -> e.domainObject.equals(deleted.get().getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1).getTestEntityManyToManyB()))){
            persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1).getTestEntityManyToManyB());
        }
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get().getTestEntityManyToManyAList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));

    }

    @Test
    public void testDeleteOnlyRoot(){
        //given
        TestRootManyToMany trs = TestDataGenerator.buildManyToManyOnlyRoot();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootManyToMany> deleted = manyToManyAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));

    }

    @Test
    public void testCloner(){
        TestRootManyToMany trs = TestDataGenerator.buildManyToManyComplete();
        EntityCloner cl = new EntityCloner(persistenceConfiguration.domainObjectBuilderProvider);
        TestRootManyToMany cloned = (TestRootManyToMany) cl.clone(trs);
        Assertions.assertThat(trs)
            .usingRecursiveComparison()
            .ignoringOverriddenEqualsForTypes(Entity.class)
            .ignoringFieldsOfTypes(UUID.class)
            .ignoringFields("valueObjectContainingEntity")
            .ignoringCollectionOrder()
            .isEqualTo(cloned);
    }

    @Test
    public void testUpdateDeleteComplete(){
        //given
        TestRootManyToMany trs = TestDataGenerator.buildManyToManyComplete();
        TestRootManyToMany inserted = manyToManyAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        TestRootManyToMany insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        insertedCopy.getTestEntityManyToManyAList().clear();
        insertedCopy.setName("UPDATED");
        //when
        TestRootManyToMany updated = manyToManyAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootManyToMany> found = manyToManyAggregateRootRepository.findResultById(inserted.getId()).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(0).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1).getTestEntityManyToManyJoinList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(1));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(0).getTestEntityManyToManyB());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0).getTestEntityManyToManyJoinList().get(0));
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted.getTestEntityManyToManyAList().get(0));

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        assertThat(persistenceEventTestHelper.eventsCaught.stream().map(e-> new PersistenceEventTestHelper.DomainObjectEventType(e.getInstance().domainObject(),e.getEventType())).collect(Collectors.toList()))
            .containsExactlyInAnyOrderElementsOf(persistenceEventTestHelper.expectedEvents.stream().map(e -> new PersistenceEventTestHelper.DomainObjectEventType(e.domainObject, e.eventType)).collect(Collectors.toList()));

    }

}
