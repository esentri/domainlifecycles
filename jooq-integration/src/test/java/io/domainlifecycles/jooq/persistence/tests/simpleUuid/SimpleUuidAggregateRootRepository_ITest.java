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

package io.domainlifecycles.jooq.persistence.tests.simpleUuid;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleUuidAggregateRootRepository_ITest extends BasePersistence_ITest {

    private SimpleUuidAggregateRootRepository simpleUuidAggregateRootRepository;

    @BeforeAll
    public void init() {
        simpleUuidAggregateRootRepository = new SimpleUuidAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider);
    }


    @Test
    public void testInsertSimpleEntity() {
        //given
        TestRootSimpleUuid trs = TestDataGenerator.buildTestRootSimpleUUID();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootSimpleUuid inserted = simpleUuidAggregateRootRepository.insert(trs);
        //then
        Optional<TestRootSimpleUuid> found = simpleUuidAggregateRootRepository.findResultById(
            new TestRootSimpleUuidId(trs.getId().value())).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        Assertions.assertThat(found.get().getId().value()).isEqualTo(inserted.getId().value());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateSimpleEntity() {
        //given

        TestRootSimpleUuid trs = TestDataGenerator.buildTestRootSimpleUUID();
        TestRootSimpleUuid inserted = simpleUuidAggregateRootRepository.insert(trs);
        TestRootSimpleUuid insertedCopy = TestRootSimpleUuid.builder()
            .setId(new TestRootSimpleUuidId(inserted.getId().value()))
            .setName("TestRoot")
            .setConcurrencyVersion(inserted.concurrencyVersion())
            .build();
        insertedCopy.setName("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootSimpleUuid updated = simpleUuidAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootSimpleUuid> found = simpleUuidAggregateRootRepository.findResultById(
            new TestRootSimpleUuidId(trs.getId().value())).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        Assertions.assertThat(found.get().getId().value()).isEqualTo(inserted.getId().value());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(updated.getName()).isEqualTo(insertedCopy.getName());
    }

    @Test
    public void testDeleteSimpleEntity() {
        //given

        TestRootSimpleUuid trs = TestDataGenerator.buildTestRootSimpleUUID();
        TestRootSimpleUuid inserted = simpleUuidAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootSimpleUuid> deleted = simpleUuidAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootSimpleUuid> found = simpleUuidAggregateRootRepository.findResultById(
            new TestRootSimpleUuidId(trs.getId().value())).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        Assertions.assertThat(deleted.get().getId().value()).isEqualTo(inserted.getId().value());

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

}
