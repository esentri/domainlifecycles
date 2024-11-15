package io.domainlifecycles.jooq.persistence.tests.temporal;

import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.temporal.TestRootTemporal;
import tests.shared.persistence.domain.temporal.TestRootTemporalId;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TemporalAggregateRootRepository_ITest extends BasePersistence_ITest {

    private TemporalAggregateRootRepository temporalAggregateRootRepository;

    @BeforeAll
    public void init() {
        temporalAggregateRootRepository = new TemporalAggregateRootRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsertTemporalEntity() {
        var now = OffsetDateTime.now(Clock.tickMillis(OffsetDateTime.now().toZonedDateTime().getZone()));
        //given
        TestRootTemporal trs = TestDataGenerator.buildTestRootTemporal(now);
        //when
        TestRootTemporal inserted = temporalAggregateRootRepository.insert(trs);
        //then
        Optional<TestRootTemporal> found = temporalAggregateRootRepository.findResultById(
            new TestRootTemporalId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }


    @Test
    public void testUpdateTemporalEntity() {
        //given
        var now = OffsetDateTime.now(Clock.tickMillis(OffsetDateTime.now().toZonedDateTime().getZone()));

        TestRootTemporal trs = TestDataGenerator.buildTestRootTemporal(now);
        TestRootTemporal inserted = temporalAggregateRootRepository.insert(trs);
        TestRootTemporal insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        var later = OffsetDateTime.now(Clock.tickMillis(OffsetDateTime.now().toZonedDateTime().getZone()));

        LocalDate newLocalDate = later.toLocalDate();
        LocalDateTime newLocalDateTime = later.toLocalDateTime();
        LocalTime newLocalTime = later.toLocalTime();
        MonthDay newMonthDay = MonthDay.from(later);
        Calendar newCalendar = GregorianCalendar.from(later.toZonedDateTime());
        Date newDate = Date.from(later.toInstant());
        Year newYear = Year.from(later);
        Instant newInstant = later.toInstant();
        YearMonth newYearMonth = YearMonth.from(later);
        ZonedDateTime newZonedDatetime = later.toZonedDateTime();
        OffsetDateTime newOffsetDateTime = later;
        OffsetTime newOffsetTime = later.toOffsetTime();

        insertedCopy.setLocalDate(newLocalDate);
        insertedCopy.setLocalDateTime(newLocalDateTime);
        insertedCopy.setLocalTime(newLocalTime);
        insertedCopy.setMonthDay(newMonthDay);
        insertedCopy.setMyCalendar(newCalendar);
        insertedCopy.setMyDate(newDate);
        insertedCopy.setMyYear(newYear);
        insertedCopy.setMyInstant(newInstant);
        insertedCopy.setYearMonth(newYearMonth);
        insertedCopy.setZonedDateTime(newZonedDatetime);
        insertedCopy.setOffsetTime(newOffsetTime);
        insertedCopy.setOffsetDateTime(newOffsetDateTime);

        persistenceEventTestHelper.resetEventsCaught();
        //when
        TestRootTemporal updated = temporalAggregateRootRepository.update(insertedCopy);
        //then
        Optional<TestRootTemporal> found = temporalAggregateRootRepository.findResultById(
            new TestRootTemporalId(1l)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        Assertions.assertThat(found.get().getZonedDateTime()).isEqualTo(newZonedDatetime);
        Assertions.assertThat(found.get().getLocalDate()).isEqualTo(newLocalDate);
        Assertions.assertThat(found.get().getLocalDateTime()).isEqualTo(newLocalDateTime);
        Assertions.assertThat(found.get().getLocalTime()).isEqualTo(newLocalTime);
        Assertions.assertThat(found.get().getMyDate()).isEqualTo(newDate);
        Assertions.assertThat(found.get().getMyInstant()).isEqualTo(newInstant);
        Assertions.assertThat(found.get().getOffsetDateTime()).isEqualTo(newOffsetDateTime);
        Assertions.assertThat(found.get().getOffsetTime()).isEqualTo(newOffsetTime);
        Assertions.assertThat(found.get().getMonthDay()).isEqualTo(newMonthDay);
        Assertions.assertThat(found.get().getMyCalendar().toInstant()).isEqualTo(newCalendar.toInstant());
        Assertions.assertThat(found.get().getMyYear()).isEqualTo(newYear);
        Assertions.assertThat(found.get().getYearMonth()).isEqualTo(newYearMonth);

    }

    @Test
    public void testDeleteTemporalEntity() {
        //given
        var now = OffsetDateTime.now(Clock.tickMillis(OffsetDateTime.now().toZonedDateTime().getZone()));

        TestRootTemporal trs = TestDataGenerator.buildTestRootTemporal(now);
        TestRootTemporal inserted = temporalAggregateRootRepository.insert(trs);
        persistenceEventTestHelper.resetEventsCaught();
        //when
        Optional<TestRootTemporal> deleted = temporalAggregateRootRepository.deleteById(inserted.getId());
        //then
        Optional<TestRootTemporal> found = temporalAggregateRootRepository.findResultById(
            new TestRootTemporalId(1l)).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        persistenceEventTestHelper.assertFoundWithResult(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }


}
