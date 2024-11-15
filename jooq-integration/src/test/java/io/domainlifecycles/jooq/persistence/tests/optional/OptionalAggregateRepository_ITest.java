package io.domainlifecycles.jooq.persistence.tests.optional;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.optional.MyComplexValueObject;
import tests.shared.persistence.domain.optional.MySimpleValueObject;
import tests.shared.persistence.domain.optional.OptionalAggregate;
import tests.shared.persistence.domain.optional.OptionalAggregateId;
import tests.shared.persistence.domain.optional.OptionalEntity;
import tests.shared.persistence.domain.optional.OptionalEntityId;
import tests.shared.persistence.domain.optional.RefAgg;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OptionalAggregateRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = LoggerFactory.getLogger(OptionalAggregateRepository_ITest.class);
    private OptionalAggregateRepository optionalAggregateRepository;
    private RefAggRepository refAggRepository;

    @BeforeAll
    public void init() {
        optionalAggregateRepository = new OptionalAggregateRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
        refAggRepository = new RefAggRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertMin() {
        //given
        RefAgg ref = TestDataGenerator.buildRefAgg();
        refAggRepository.insert(ref);
        OptionalAggregate r = TestDataGenerator.buildOptionalAggregateMin();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        OptionalAggregate inserted = optionalAggregateRepository.insert(r);
        //then
        Optional<OptionalAggregate> found = optionalAggregateRepository.findResultById(
            new OptionalAggregateId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(found.get().getOptionalEntity()).isEmpty();
        Assertions.assertThat(found.get().getOptionalText()).isEmpty();
        Assertions.assertThat(found.get().getOptionalComplexValueObject()).isEmpty();
        Assertions.assertThat(found.get().getOptionalSimpleValueObject()).isEmpty();
        Assertions.assertThat(found.get().getOptionalRefId()).isEmpty();
        Assertions.assertThat(found.get().getRefValueObject().getOptionalRef()).isEmpty();
    }

    @Test
    public void testInsertMax() {
        //given
        RefAgg ref = TestDataGenerator.buildRefAgg();
        refAggRepository.insert(ref);

        OptionalAggregate r = TestDataGenerator.buildOptionalAggregateMax();
        persistenceEventTestHelper.resetEventsCaught();
        //when
        OptionalAggregate inserted = optionalAggregateRepository.insert(r);
        //then
        Optional<OptionalAggregate> found = optionalAggregateRepository.findResultById(
            new OptionalAggregateId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getOptionalEntity().get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getOptionalEntity().get().getComplexValueObjectList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getOptionalEntity().get().getComplexValueObjectList().get(1), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getRefValueObjectList().get(0), inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            inserted.getRefValueObjectList().get(1), inserted);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(found.get().getOptionalEntity()).isPresent();
        Assertions.assertThat(found.get().getOptionalText()).isPresent();
        Assertions.assertThat(found.get().getOptionalComplexValueObject()).isPresent();
        Assertions.assertThat(found.get().getOptionalComplexValueObject().get().getOptionalText()).isPresent();
        Assertions.assertThat(
            found.get().getOptionalComplexValueObject().get().getOptionalSimpleValueObject()).isPresent();
        Assertions.assertThat(found.get().getOptionalSimpleValueObject()).isPresent();
        Assertions.assertThat(found.get().getOptionalEntity().get().getOptionalText()).isPresent();
        Assertions.assertThat(found.get().getOptionalEntity().get().getOptionalSimpleValueObject()).isPresent();
        Assertions.assertThat(found.get().getOptionalEntity().get().getOptionalComplexValueObject()).isPresent();
        Assertions.assertThat(
            found.get().getOptionalEntity().get().getOptionalComplexValueObject().get().getOptionalSimpleValueObject()).isPresent();
        Assertions.assertThat(
            found.get().getOptionalEntity().get().getOptionalComplexValueObject().get().getOptionalText()).isPresent();
        Assertions.assertThat(found.get().getOptionalRefId()).isPresent();
        Assertions.assertThat(found.get().getRefValueObject().getOptionalRef()).isPresent();
        Assertions.assertThat(found.get().getOptionalEntity().get().getComplexValueObjectList()).hasSize(2);
    }


    @Test
    public void testUpdateMin() {
        //given
        RefAgg ref = TestDataGenerator.buildRefAgg();
        refAggRepository.insert(ref);

        OptionalAggregate r = TestDataGenerator.buildOptionalAggregateMin();
        OptionalAggregate inserted = optionalAggregateRepository.insert(r);
        OptionalAggregate insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setMandatoryText("UPDATED");
        insertedCopy.setOptionalText("UPDATED");
        persistenceEventTestHelper.resetEventsCaught();

        //when
        OptionalAggregate updated = optionalAggregateRepository.update(insertedCopy);
        //then
        Optional<OptionalAggregate> found = optionalAggregateRepository.findResultById(
            new OptionalAggregateId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMinAddEntity() {
        //given
        RefAgg ref = TestDataGenerator.buildRefAgg();
        refAggRepository.insert(ref);

        OptionalAggregate r = TestDataGenerator.buildOptionalAggregateMin();
        OptionalAggregate inserted = optionalAggregateRepository.insert(r);
        OptionalAggregate insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setOptionalEntity(
            OptionalEntity.builder()
                .setId(new OptionalEntityId(5l))
                .setMandatoryText("New")
                .setMandatorySimpleValueObject(MySimpleValueObject
                    .builder()
                    .setValue("New")
                    .build())
                .setMandatoryComplexValueObject(
                    MyComplexValueObject
                        .builder()
                        .setMandatoryText("New")
                        .setOptionalText("New2")
                        .setMandatorySimpleValueObject(
                            MySimpleValueObject
                                .builder()
                                .setValue("New3")
                                .build())
                        .build()
                )
                .build()
        );
        persistenceEventTestHelper.resetEventsCaught();

        //when
        OptionalAggregate updated = optionalAggregateRepository.update(insertedCopy);
        //then
        Optional<OptionalAggregate> found = optionalAggregateRepository.findResultById(
            new OptionalAggregateId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED,
            updated.getOptionalEntity().get());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMinAddSimpleValueObject() {
        //given
        RefAgg ref = TestDataGenerator.buildRefAgg();
        refAggRepository.insert(ref);

        OptionalAggregate r = TestDataGenerator.buildOptionalAggregateMin();
        OptionalAggregate inserted = optionalAggregateRepository.insert(r);
        OptionalAggregate insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setOptionalSimpleValueObject(
            MySimpleValueObject
                .builder()
                .setValue("New")
                .build()
        );

        persistenceEventTestHelper.resetEventsCaught();

        //when
        OptionalAggregate updated = optionalAggregateRepository.update(insertedCopy);
        //then
        Optional<OptionalAggregate> found = optionalAggregateRepository.findResultById(
            new OptionalAggregateId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testUpdateMinAddComplexValueObject() {
        //given
        RefAgg ref = TestDataGenerator.buildRefAgg();
        refAggRepository.insert(ref);

        OptionalAggregate r = TestDataGenerator.buildOptionalAggregateMin();
        OptionalAggregate inserted = optionalAggregateRepository.insert(r);
        OptionalAggregate insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);
        insertedCopy.setOptionalComplexValueObject(
            MyComplexValueObject
                .builder()
                .setMandatoryText("New")
                .setOptionalSimpleValueObject(
                    MySimpleValueObject
                        .builder()
                        .setValue("New2")
                        .build())
                .setMandatorySimpleValueObject(
                    MySimpleValueObject
                        .builder()
                        .setValue("New3")
                        .build()
                )
                .build()
        );

        persistenceEventTestHelper.resetEventsCaught();

        //when
        OptionalAggregate updated = optionalAggregateRepository.update(insertedCopy);
        //then
        Optional<OptionalAggregate> found = optionalAggregateRepository.findResultById(
            new OptionalAggregateId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteMin() {
        //given
        RefAgg ref = TestDataGenerator.buildRefAgg();
        refAggRepository.insert(ref);

        OptionalAggregate r = TestDataGenerator.buildOptionalAggregateMin();
        OptionalAggregate inserted = optionalAggregateRepository.insert(r);
        Optional<OptionalAggregate> found = optionalAggregateRepository.findResultById(
            new OptionalAggregateId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<OptionalAggregate> deleted = optionalAggregateRepository.deleteById(new OptionalAggregateId(1l));
        //then
        found = optionalAggregateRepository.findResultById(new OptionalAggregateId(1l)).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    public void testDeleteMax() {
        //given
        RefAgg ref = TestDataGenerator.buildRefAgg();
        refAggRepository.insert(ref);

        OptionalAggregate r = TestDataGenerator.buildOptionalAggregateMax();
        OptionalAggregate inserted = optionalAggregateRepository.insert(r);
        Optional<OptionalAggregate> found = optionalAggregateRepository.findResultById(
            new OptionalAggregateId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<OptionalAggregate> deleted = optionalAggregateRepository.deleteById(new OptionalAggregateId(1l));
        //then
        found = optionalAggregateRepository.findResultById(new OptionalAggregateId(1l)).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getOptionalEntity().get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getOptionalEntity().get().getComplexValueObjectList().get(0), deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getOptionalEntity().get().getComplexValueObjectList().get(1), deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getRefValueObjectList().get(0), deleted.get());
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED,
            deleted.get().getRefValueObjectList().get(1), deleted.get());

        persistenceEventTestHelper.assertEvents();
    }
}
