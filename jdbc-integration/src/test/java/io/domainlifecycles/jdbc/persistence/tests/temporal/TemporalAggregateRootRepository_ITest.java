package io.domainlifecycles.jdbc.persistence.tests.temporal;

import io.domainlifecycles.jdbc.persistence.JdbcBasePersistence_ITest;
import io.domainlifecycles.jdbc.persistence.containers.TestDatabaseDialect;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.TestDataGenerator;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.domain.temporal.TestRootTemporal;

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
import java.util.Set;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TemporalAggregateRootRepository_ITest extends JdbcBasePersistence_ITest {

    private TemporalAggregateRootRepository temporalAggregateRootRepository;

    @BeforeAll
    public void init() {
        temporalAggregateRootRepository = new TemporalAggregateRootRepository(
            persistenceConfiguration.domainPersistenceProvider,
            persistenceEventTestHelper.testEventPublisher
        );
    }

    /**
     * Fields whose physical column carries an offset/zone-aware SQL type ({@code TIMESTAMP WITH TIME ZONE}
     * on H2/Oracle, {@code DATETIMEOFFSET} on SQL Server) that Postgres' {@code timestamptz} and MySQL's
     * {@code DATETIME} have no equivalent for: both only ever round-trip the *instant*, never the original
     * UTC offset/zone the value was inserted with - standard, documented behavior for those two engines, not
     * a mapping bug (see {@code JdbcSqlTypeMapping}/{@code db/migration-postgres}/{@code db/migration-mysql}).
     */
    private static final Set<String> OFFSET_SENSITIVE_FIELDS = Set.of(
        "offsetDateTime", "offsetTime", "zonedDateTime", "myCalendar");

    /**
     * Same recursive comparison as {@code PersistenceEventTestHelper#assertFoundWithResult}, except that on
     * Postgres/MySQL - where {@link #OFFSET_SENSITIVE_FIELDS} cannot round-trip exactly, see there - those
     * fields are excluded from the recursive (exact-equality) comparison and asserted by instant instead;
     * every other dialect (H2, Oracle, SQL Server) keeps the exact comparison unchanged, so a real regression
     * there would still fail this test.
     */
    private void assertFoundTemporalEntity(Optional<TestRootTemporal> found, TestRootTemporal expected) {
        Assertions.assertThat(found).isPresent();
        var actual = found.get();
        var dialect = TestDatabaseDialect.fromSystemProperty();
        boolean offsetPreserved = dialect != TestDatabaseDialect.POSTGRES && dialect != TestDatabaseDialect.MYSQL;

        var comparison = Assertions.assertThat(expected)
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .ignoringFieldsOfTypes(UUID.class)
            .withStrictTypeChecking();
        if (!offsetPreserved) {
            comparison = comparison.ignoringFields(OFFSET_SENSITIVE_FIELDS.toArray(new String[0]));
        }
        comparison.isEqualTo(actual);

        if (!offsetPreserved) {
            Assertions.assertThat(actual.getOffsetDateTime().toInstant())
                .isEqualTo(expected.getOffsetDateTime().toInstant());
            // OffsetTime has no toInstant() (no date component) - isEqual() is its instant-equivalent
            // comparison, normalizing both sides to the same offset before comparing, exactly like
            // OffsetDateTime/ZonedDateTime's toInstant() does here.
            Assertions.assertThat(actual.getOffsetTime().isEqual(expected.getOffsetTime())).isTrue();
            Assertions.assertThat(actual.getZonedDateTime().toInstant())
                .isEqualTo(expected.getZonedDateTime().toInstant());
            Assertions.assertThat(actual.getMyCalendar().toInstant())
                .isEqualTo(expected.getMyCalendar().toInstant());
        }
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
            inserted.getId()).resultValue();
        assertFoundTemporalEntity(found, inserted);
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
            inserted.getId()).resultValue();
        assertFoundTemporalEntity(found, updated);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.UPDATED, updated);
        persistenceEventTestHelper.assertEvents();
        var dialect = TestDatabaseDialect.fromSystemProperty();
        boolean offsetPreserved = dialect != TestDatabaseDialect.POSTGRES && dialect != TestDatabaseDialect.MYSQL;
        if (offsetPreserved) {
            Assertions.assertThat(found.get().getZonedDateTime()).isEqualTo(newZonedDatetime);
            Assertions.assertThat(found.get().getOffsetDateTime()).isEqualTo(newOffsetDateTime);
            Assertions.assertThat(found.get().getOffsetTime()).isEqualTo(newOffsetTime);
        } else {
            // see OFFSET_SENSITIVE_FIELDS/assertFoundTemporalEntity: Postgres/MySQL only preserve the
            // instant, not the original UTC offset/zone
            Assertions.assertThat(found.get().getZonedDateTime().toInstant()).isEqualTo(newZonedDatetime.toInstant());
            Assertions.assertThat(found.get().getOffsetDateTime().toInstant()).isEqualTo(newOffsetDateTime.toInstant());
            Assertions.assertThat(found.get().getOffsetTime().isEqual(newOffsetTime)).isTrue();
        }
        Assertions.assertThat(found.get().getLocalDate()).isEqualTo(newLocalDate);
        Assertions.assertThat(found.get().getLocalDateTime()).isEqualTo(newLocalDateTime);
        Assertions.assertThat(found.get().getLocalTime()).isEqualTo(newLocalTime);
        Assertions.assertThat(found.get().getMyDate()).isEqualTo(newDate);
        Assertions.assertThat(found.get().getMyInstant()).isEqualTo(newInstant);
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
            inserted.getId()).resultValue();
        Assertions.assertThat(deleted).isPresent();
        Assertions.assertThat(found).isEmpty();
        assertFoundTemporalEntity(deleted, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.DELETED, deleted.get());
        persistenceEventTestHelper.assertEvents();
    }


}
