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

package io.domainlifecycles.jooq.persistence.tests.valueobjectsPrimitive;


import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.valueobjectsPrimitive.ComplexVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.NestedVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.SimpleVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitiveId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoAggregatePrimitiveRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoAggregatePrimitiveRepository_ITest.class);

    private VoAggregatePrimitiveRepository voAggregatePrimitiveRepository;

    @BeforeAll
    public void init() {
        voAggregatePrimitiveRepository = spy(new VoAggregatePrimitiveRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        ));
    }


    @Test
    public void testInsertEmpty() {
        //given
        VoAggregatePrimitive r = TestDataGenerator.buildVoPrimitiveEmpty();
        VoAggregatePrimitive copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregatePrimitive inserted = voAggregatePrimitiveRepository.insert(copy);


        //then
        Optional<VoAggregatePrimitive> found = voAggregatePrimitiveRepository.findResultById(
                new VoAggregatePrimitiveId(1l))
            .resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);

        log.debug("Neue VO Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertComplete() {
        //given
        VoAggregatePrimitive r = TestDataGenerator.buildVoPrimitiveComplete();
        VoAggregatePrimitive copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregatePrimitive inserted = voAggregatePrimitiveRepository.insert(copy);


        //then
        Optional<VoAggregatePrimitive> found = voAggregatePrimitiveRepository.findResultById(
                new VoAggregatePrimitiveId(2l))
            .resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);

        log.debug("Neue VO Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getRecordMappedSimple(), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getRecordMappedComplex(), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getRecordMappedNested(), inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateDoNothing() {
        //given
        VoAggregatePrimitive r = TestDataGenerator.buildVoPrimitiveComplete();
        VoAggregatePrimitive inserted = voAggregatePrimitiveRepository.insert(r);
        Mockito.reset(voAggregatePrimitiveRepository);

        //when
        VoAggregatePrimitive updated = voAggregatePrimitiveRepository.update(inserted);

        persistenceEventTestHelper.resetEventsCaught();
        //then
        Optional<VoAggregatePrimitive> found = voAggregatePrimitiveRepository.findResultById(
            new VoAggregatePrimitiveId(2l)).resultValue();

        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        Mockito.verify(voAggregatePrimitiveRepository, Mockito.times(0)).publish(any());

        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testDeleteEmpty() {
        //given
        VoAggregatePrimitive r = TestDataGenerator.buildVoPrimitiveEmpty();
        VoAggregatePrimitive inserted = voAggregatePrimitiveRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        voAggregatePrimitiveRepository.deleteById(new VoAggregatePrimitiveId(1l));

        //then
        Optional<VoAggregatePrimitive> found = voAggregatePrimitiveRepository.findResultById(
            new VoAggregatePrimitiveId(1l)).resultValue();
        Assertions.assertThat(found).isEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteComplete() {
        //given
        VoAggregatePrimitive r = TestDataGenerator.buildVoPrimitiveComplete();
        VoAggregatePrimitive inserted = voAggregatePrimitiveRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        voAggregatePrimitiveRepository.deleteById(new VoAggregatePrimitiveId(2l));

        //then
        Optional<VoAggregatePrimitive> found = voAggregatePrimitiveRepository.findResultById(
            new VoAggregatePrimitiveId(2l)).resultValue();
        Assertions.assertThat(found).isEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getRecordMappedNested(), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getRecordMappedSimple(), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getRecordMappedComplex(), inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateEmpty() {
        //given
        VoAggregatePrimitive r = TestDataGenerator.buildVoPrimitiveEmpty();
        VoAggregatePrimitive inserted = voAggregatePrimitiveRepository.insert(r);
        VoAggregatePrimitive copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.setComplex(ComplexVoPrimitive.builder()
            .setNum(5l)
            .setVal(88l)
            .build());
        copy.setNested(NestedVoPrimitive.builder()
            .setSimple(SimpleVoPrimitive.builder()
                .setVal(88l)
                .build())
            .setComplex(ComplexVoPrimitive.builder()
                .setNum(6l)
                .setVal(88l)
                .build())
            .build());
        copy.setSimple(SimpleVoPrimitive.builder()
            .setVal(88l)
            .build());
        copy.setOptionalSimple(Optional.of(SimpleVoPrimitive.builder()
            .setVal(88l)
            .build()));
        copy.setOptionalNested(Optional.of(
            NestedVoPrimitive.builder()
                .setSimple(SimpleVoPrimitive.builder()
                    .setVal(88l)
                    .build())
                .setComplex(ComplexVoPrimitive.builder()
                    .setNum(6l)
                    .setVal(88l)
                    .build())
                .build()
        ));
        copy.setOptionalComplex(Optional.of(
            ComplexVoPrimitive.builder()
                .setNum(5l)
                .setVal(88l)
                .build()
        ));
        copy.setRecordMappedComplex(ComplexVoPrimitive.builder()
            .setNum(5l)
            .setVal(88l)
            .build());
        copy.setRecordMappedNested(NestedVoPrimitive.builder()
            .setSimple(SimpleVoPrimitive.builder()
                .setVal(88l)
                .build())
            .setComplex(ComplexVoPrimitive.builder()
                .setNum(6l)
                .setVal(88l)
                .build())
            .build());
        copy.setRecordMappedSimple(SimpleVoPrimitive.builder()
            .setVal(88l)
            .build());
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregatePrimitive updated = voAggregatePrimitiveRepository.update(copy);

        //then
        Optional<VoAggregatePrimitive> found = voAggregatePrimitiveRepository.findResultById(
            new VoAggregatePrimitiveId(1l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        log.debug("Neue VO Aggregate Root: \n" + found);

        //strange assertion errors sometimes --> seems to be a heisenbug in AssertJ
        /*persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated
        .getRecordMappedComplex(), updated );
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated
        .getRecordMappedNested(), updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, updated
        .getRecordMappedSimple(), updated);
        persistenceEventTestHelper.assertEvents();*/
    }


    @Test
    public void testUpdateDeleteVo() {
        //given
        VoAggregatePrimitive r = TestDataGenerator.buildVoPrimitiveComplete();
        VoAggregatePrimitive inserted = voAggregatePrimitiveRepository.insert(r);
        VoAggregatePrimitive copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.setComplex(null);
        copy.setNested(null);
        copy.setSimple(null);
        copy.setOptionalNested(Optional.empty());
        copy.setOptionalComplex(Optional.empty());
        copy.setOptionalSimple(Optional.empty());
        copy.setRecordMappedComplex(null);
        copy.setRecordMappedNested(null);
        copy.setRecordMappedSimple(null);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        VoAggregatePrimitive updated = voAggregatePrimitiveRepository.update(copy);
        //then
        Optional<VoAggregatePrimitive> found = voAggregatePrimitiveRepository.findResultById(
            new VoAggregatePrimitiveId(2l)).resultValue();

        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getRecordMappedComplex(), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getRecordMappedSimple(), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            inserted.getRecordMappedNested(), inserted);
        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

}
