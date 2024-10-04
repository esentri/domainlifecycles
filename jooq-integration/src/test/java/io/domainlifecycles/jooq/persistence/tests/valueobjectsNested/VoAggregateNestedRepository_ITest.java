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

package io.domainlifecycles.jooq.persistence.tests.valueobjectsNested;


import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.valueobjectsNested.MyEnum;
import tests.shared.persistence.domain.valueobjectsNested.NestedEnumSingleValued;
import tests.shared.persistence.domain.valueobjectsNested.NestedId;
import tests.shared.persistence.domain.valueobjectsNested.NestedSimpleVo;
import tests.shared.persistence.domain.valueobjectsNested.VoAggregateNested;
import tests.shared.persistence.domain.valueobjectsNested.VoAggregateNestedId;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoAggregateNestedRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoAggregateNestedRepository_ITest.class);

    private VoAggregateNestedRepository voAggregateNestedRepository;

    @BeforeAll
    public void init() {
        voAggregateNestedRepository = spy(new VoAggregateNestedRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        ));
    }


    @Test
    public void testInsertEmpty() {
        //given
        VoAggregateNested r = TestDataGenerator.buildVoNestedEmpty();
        VoAggregateNested copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateNested inserted = voAggregateNestedRepository.insert(copy);


        //then
        Optional<VoAggregateNested> found = voAggregateNestedRepository.findResultById(new VoAggregateNestedId(1l))
            .resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);

        log.debug("Neues VO Nested Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertComplete() {
        //given
        VoAggregateNested r = TestDataGenerator.buildVoNestedComplete();
        VoAggregateNested copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateNested inserted = voAggregateNestedRepository.insert(copy);


        //then
        Optional<VoAggregateNested> found = voAggregateNestedRepository.findResultById(new VoAggregateNestedId(2l))
            .resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);

        log.debug("Neues VO Nested Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getNestedEnumSingleValuedList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getNestedIdList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getNestedSimpleVoList().get(0), inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateDoNothing() {
        //given
        VoAggregateNested r = TestDataGenerator.buildVoNestedComplete();
        VoAggregateNested inserted = voAggregateNestedRepository.insert(r);
        Mockito.reset(voAggregateNestedRepository);

        //when
        VoAggregateNested updated = voAggregateNestedRepository.update(inserted);

        persistenceEventTestHelper.resetEventsCaught();
        //then
        Optional<VoAggregateNested> found = voAggregateNestedRepository.findResultById(
            new VoAggregateNestedId(2l)).resultValue();

        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        Mockito.verify(voAggregateNestedRepository, Mockito.times(0)).publish(any());

        log.debug("Neues VO Nested Aggregate Root: \n" + found);
    }

    @Test
    public void testDeleteEmpty() {
        //given
        VoAggregateNested r = TestDataGenerator.buildVoNestedEmpty();
        VoAggregateNested inserted = voAggregateNestedRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        voAggregateNestedRepository.deleteById(new VoAggregateNestedId(1l));

        //then
        Optional<VoAggregateNested> found = voAggregateNestedRepository.findResultById(
            new VoAggregateNestedId(1l)).resultValue();
        Assertions.assertThat(found).isEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteComplete() {
        //given
        VoAggregateNested r = TestDataGenerator.buildVoNestedComplete();
        VoAggregateNested inserted = voAggregateNestedRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        voAggregateNestedRepository.deleteById(new VoAggregateNestedId(2l));

        //then
        Optional<VoAggregateNested> found = voAggregateNestedRepository.findResultById(
            new VoAggregateNestedId(2l)).resultValue();
        Assertions.assertThat(found).isEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getNestedEnumSingleValuedList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getNestedIdList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getNestedSimpleVoList().get(0), inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateEmpty() {
        //given
        VoAggregateNested r = TestDataGenerator.buildVoNestedEmpty();
        VoAggregateNested inserted = voAggregateNestedRepository.insert(r);
        VoAggregateNested copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.setNestedEnumSingleValued(NestedEnumSingleValued.builder().setEnumValue(MyEnum.A).build());
        copy.setNestedSimpleVo(NestedSimpleVo.builder().setNested(
            tests.shared.persistence.domain.valueobjectsNested.SimpleVo.builder().setVal(9l).build()).build());
        copy.setNestedId(NestedId.builder().setIdRef(new VoAggregateNestedId(55l)).build());
        copy.setNestedEnumSingleValuedList(List.of(NestedEnumSingleValued.builder().setEnumValue(MyEnum.B).build()));
        copy.setNestedSimpleVoList(List.of(NestedSimpleVo.builder().setNested(
            tests.shared.persistence.domain.valueobjectsNested.SimpleVo.builder().setVal(90l).build()).build()));
        copy.setNestedIdList(List.of(NestedId.builder().setIdRef(new VoAggregateNestedId(550l)).build()));


        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateNested updated = voAggregateNestedRepository.update(copy);

        //then
        Optional<VoAggregateNested> found = voAggregateNestedRepository.findResultById(
            new VoAggregateNestedId(1l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        log.debug("Neues VO Nested Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getNestedEnumSingleValuedList().get(0), updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getNestedIdList().get(0), updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getNestedSimpleVoList().get(0), updated);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateDeleteVo() {
        //given
        VoAggregateNested r = TestDataGenerator.buildVoNestedComplete();
        VoAggregateNested inserted = voAggregateNestedRepository.insert(r);
        VoAggregateNested copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.setNestedEnumSingleValued(null);
        copy.setNestedSimpleVo(null);
        copy.setNestedId(null);
        copy.setNestedEnumSingleValuedList(null);
        copy.setNestedSimpleVoList(null);
        copy.setNestedIdList(null);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        VoAggregateNested updated = voAggregateNestedRepository.update(copy);
        //then
        Optional<VoAggregateNested> found = voAggregateNestedRepository.findResultById(
            new VoAggregateNestedId(2l)).resultValue();

        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getNestedEnumSingleValuedList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getNestedIdList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getNestedSimpleVoList().get(0), inserted);

        persistenceEventTestHelper.assertEvents();
        log.debug("Neues VO Nested Aggregate Root: \n" + found);
    }

}
