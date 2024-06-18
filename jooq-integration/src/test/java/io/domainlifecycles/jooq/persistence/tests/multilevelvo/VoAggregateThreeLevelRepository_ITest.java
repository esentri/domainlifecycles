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

package io.domainlifecycles.jooq.persistence.tests.multilevelvo;


import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.multilevelvo.ThreeLevelVo;
import tests.shared.persistence.domain.multilevelvo.ThreeLevelVoLevelThree;
import tests.shared.persistence.domain.multilevelvo.ThreeLevelVoLevelTwo;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevel;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevelId;
import tests.shared.persistence.domain.valueobjects.ComplexVo;
import tests.shared.persistence.domain.valueobjects.SimpleVo;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Several edge case aspects are tested here A) We have a three entity to VO 1-1
 * Relationship where on vo (ThreeLevelVO) contains 3 nested layers of VOs B)
 * The ThreeLevelVO itself contains a primitive value (long) but is optional
 * itself, so the Database can contain null C) if the complete ThreeLevelVo is
 * null all corresponding database columns have to be null D) We have a Primary
 * Key whose property on Builder Level has a name that is not "id"
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoAggregateThreeLevelRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoAggregateThreeLevelRepository_ITest.class);

    private VoAggregateThreeLevelRepository voAggregateThreeLevelRepository;

    @BeforeAll
    public void init(){
        voAggregateThreeLevelRepository = Mockito.spy(new VoAggregateThreeLevelRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        ));
    }
    @Test
    public void testInsertMin() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMin();
        VoAggregateThreeLevel copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(copy);


        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(1l))
            .resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);

        log.debug("Neue VO Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testInsertMiddle() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMiddle();
        VoAggregateThreeLevel copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(copy);


        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(2l)).resultValue();
        assertThat(inserted == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();

        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMaxDoNothing() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMax();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        Mockito.reset(voAggregateThreeLevelRepository);

        //when
        VoAggregateThreeLevel updated = voAggregateThreeLevelRepository.update(inserted);

        persistenceEventTestHelper.resetEventsCaught();
        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(3l)).resultValue();

        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        Mockito.verify(voAggregateThreeLevelRepository, Mockito.times(0)).publish(any());

        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testDeleteMiddle() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMiddle();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        voAggregateThreeLevelRepository.deleteById(new VoAggregateThreeLevelId(2l));

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(1l)).resultValue();
        Assertions.assertThat(found).isEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteMax() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMax();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        voAggregateThreeLevelRepository.deleteById(new VoAggregateThreeLevelId(3l));

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(3l)).resultValue();
        Assertions.assertThat(found).isEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMiddleAddVo() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMiddle();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        VoAggregateThreeLevel copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.setComplexVo(ComplexVo.builder().setValueA("NEW").build());
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateThreeLevel updated = voAggregateThreeLevelRepository.update(copy);

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(2l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        log.debug("Neue VO Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMaxDeleteVo() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMax();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        VoAggregateThreeLevel copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        copy.setThreeLevelVo(null);
        VoAggregateThreeLevel updated = voAggregateThreeLevelRepository.update(copy);

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(3l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMiddleDeleteVo() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMiddle();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        VoAggregateThreeLevel copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        copy.setThreeLevelVo(null);
        VoAggregateThreeLevel updated = voAggregateThreeLevelRepository.update(copy);

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(2l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMiddleAddVoAndDeleteVoAndUpdateRoot() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMiddle();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        VoAggregateThreeLevel copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when

        copy.setThreeLevelVo(null);
        copy.setComplexVo(ComplexVo.builder().setValueB(SimpleVo.builder().setValue("NEW").build()).build());
        copy.setInfo("Neu1111");

        VoAggregateThreeLevel updated = voAggregateThreeLevelRepository.update(copy);

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(2l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testUpdateMinSimpleVO() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMin();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        VoAggregateThreeLevel copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        copy.setComplexVo(ComplexVo.builder().setValueA("NEW").build());
        VoAggregateThreeLevel updated = voAggregateThreeLevelRepository.update(copy);

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(1l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMinAddThreeLevelVO() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMin();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        VoAggregateThreeLevel copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        copy.setThreeLevelVo(ThreeLevelVo.builder()
            .setOwnValue(5)
            .setLevelTwoA(ThreeLevelVoLevelTwo.builder()
                .setLevelThreeB(ThreeLevelVoLevelThree.builder().setText("heyho").build()
                )
                .build())
            .build());
        VoAggregateThreeLevel updated = voAggregateThreeLevelRepository.update(copy);

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(1l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMinAddComplexVO() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMin();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        VoAggregateThreeLevel copy = persistenceEventTestHelper.kryo.copy(inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        copy.setComplexVo(ComplexVo.builder()
            .setValueA("newA")
            .setValueB(
                SimpleVo
                    .builder()
                    .setValue("newB")
                    .build()
            )
            .build());
        VoAggregateThreeLevel updated = voAggregateThreeLevelRepository.update(copy);

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(1l)).resultValue();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteMin() {
        //given
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMin();
        VoAggregateThreeLevel inserted = voAggregateThreeLevelRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        voAggregateThreeLevelRepository.deleteById(new VoAggregateThreeLevelId(1l));

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(new VoAggregateThreeLevelId(1l)).resultValue();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

}
