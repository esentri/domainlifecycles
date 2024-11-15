package io.domainlifecycles.jooq.persistence.tests.valueobjectTwoLevel;


import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoAggregateTwoLevel;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoAggregateTwoLevelId;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoLevelOne;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoLevelTwoA;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoAggregateTwoLevelRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoAggregateTwoLevelRepository_ITest.class);

    private VoAggregateTwoLevelRepository voAggregateTwoLevelRepository;

    @BeforeAll
    public void init() {
        voAggregateTwoLevelRepository = Mockito.spy(new VoAggregateTwoLevelRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        ));
    }


    @Test
    public void testInsert() {
        //given
        VoAggregateTwoLevel r = TestDataGenerator.buildVoAggregateTwoLevel();
        VoAggregateTwoLevel copy = persistenceEventTestHelper.kryo.copy(r);
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateTwoLevel inserted = voAggregateTwoLevelRepository.insert(copy);


        //then
        Optional<VoAggregateTwoLevel> found = voAggregateTwoLevelRepository.findResultById(
                new VoAggregateTwoLevelId(1l))
            .resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);

        log.debug("Neue VO Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateDoNothing() {
        //given
        VoAggregateTwoLevel r = TestDataGenerator.buildVoAggregateTwoLevel();
        VoAggregateTwoLevel inserted = voAggregateTwoLevelRepository.insert(r);
        Mockito.reset(voAggregateTwoLevelRepository);

        //when
        VoAggregateTwoLevel updated = voAggregateTwoLevelRepository.update(inserted);

        persistenceEventTestHelper.resetEventsCaught();
        //then
        Optional<VoAggregateTwoLevel> found = voAggregateTwoLevelRepository.findResultById(
            new VoAggregateTwoLevelId(1l)).resultValue();

        persistenceEventTestHelper.assertFoundWithResult(found, updated);

        Mockito.verify(voAggregateTwoLevelRepository, Mockito.times(0)).publish(ArgumentMatchers.any());

        log.debug("Neue VO Aggregate Root: \n" + found);
    }

    @Test
    public void testDelete() {
        //given
        VoAggregateTwoLevel r = TestDataGenerator.buildVoAggregateTwoLevel();
        VoAggregateTwoLevel inserted = voAggregateTwoLevelRepository.insert(r);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        voAggregateTwoLevelRepository.deleteById(new VoAggregateTwoLevelId(1l));

        //then
        Optional<VoAggregateTwoLevel> found = voAggregateTwoLevelRepository.findResultById(
            new VoAggregateTwoLevelId(1l)).resultValue();
        Assertions.assertThat(found).isEmpty();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, inserted);

        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdate() {
        //given
        VoAggregateTwoLevel r = TestDataGenerator.buildVoAggregateTwoLevel();
        VoAggregateTwoLevel inserted = voAggregateTwoLevelRepository.insert(r);
        VoAggregateTwoLevel copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.setLevelOne(VoLevelOne.builder().setSecond(VoLevelTwoA.builder().setText("MIDDLE").build()).build());
        persistenceEventTestHelper.resetEventsCaught();

        //when
        VoAggregateTwoLevel updated = voAggregateTwoLevelRepository.update(copy);

        //then
        Optional<VoAggregateTwoLevel> found = voAggregateTwoLevelRepository.findResultById(
            new VoAggregateTwoLevelId(1l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        Assertions.assertThat(found.get().getLevelOne()).isNotNull();
        Assertions.assertThat(found.get().getLevelOne().first()).isNull();
        Assertions.assertThat(found.get().getLevelOne().third()).isNull();
        log.debug("Neue VO Aggregate Root: \n" + found);

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateDeleteVo() {
        //given
        VoAggregateTwoLevel r = TestDataGenerator.buildVoAggregateTwoLevel();
        VoAggregateTwoLevel inserted = voAggregateTwoLevelRepository.insert(r);
        VoAggregateTwoLevel copy = persistenceEventTestHelper.kryo.copy(inserted);
        copy.setLevelOne(null);


        persistenceEventTestHelper.resetEventsCaught();
        //when
        VoAggregateTwoLevel updated = voAggregateTwoLevelRepository.update(copy);

        //then
        Optional<VoAggregateTwoLevel> found = voAggregateTwoLevelRepository.findResultById(
            new VoAggregateTwoLevelId(1l)).resultValue();

        assertThat(updated == copy);
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        Assertions.assertThat(found.get().getLevelOne()).isNull();

        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);

        persistenceEventTestHelper.assertEvents();
        log.debug("Neue VO Aggregate Root: \n" + found);
    }

}
