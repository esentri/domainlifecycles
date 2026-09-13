package io.domainlifecycles.jdbc.persistence.tests.multilevelvo;


import io.domainlifecycles.jdbc.persistence.JdbcBasePersistence_ITest;
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
import tests.shared.persistence.domain.valueobjects.ComplexVo;
import tests.shared.persistence.domain.valueobjects.SimpleVo;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoAggregateThreeLevelRepository_ITest extends JdbcBasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoAggregateThreeLevelRepository_ITest.class);

    private VoAggregateThreeLevelRepository voAggregateThreeLevelRepository;

    @BeforeAll
    public void init() {
        voAggregateThreeLevelRepository = Mockito.spy(new VoAggregateThreeLevelRepository(
            persistenceConfiguration.connectionProvider,
            persistenceConfiguration.dialect,
            persistenceConfiguration.schemaMetadata,
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
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
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
                inserted.getIdentificationNumber())
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
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
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
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();

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
        voAggregateThreeLevelRepository.deleteById(inserted.getIdentificationNumber());

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
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
        voAggregateThreeLevelRepository.deleteById(inserted.getIdentificationNumber());

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
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
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
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
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
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
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
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
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
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
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
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
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
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
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
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
        voAggregateThreeLevelRepository.deleteById(inserted.getIdentificationNumber());

        //then
        Optional<VoAggregateThreeLevel> found = voAggregateThreeLevelRepository.findResultById(
            inserted.getIdentificationNumber()).resultValue();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

}
