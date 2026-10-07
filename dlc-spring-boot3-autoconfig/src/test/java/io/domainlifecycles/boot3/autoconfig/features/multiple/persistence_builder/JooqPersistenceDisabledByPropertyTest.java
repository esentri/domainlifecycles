package io.domainlifecycles.boot3.autoconfig.features.multiple.persistence_builder;

import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.test.boot3.autoconfig.jdbc.SimpleJdbcAggregateRootRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tests.shared.events.PersistenceEvent;
import tests.shared.persistence.PersistenceEventTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers {@code dlc.features.persistence.jooq.enabled=false}: with both jooq-integration and
 * jdbc-integration on the classpath (the same setup {@link PersistenceAndBuilderAutoConfigTest} uses, where
 * jOOQ wins by default), disabling jOOQ's own persistence backend specifically - without excluding
 * {@code DlcJooqPersistenceAutoConfiguration} outright - lets {@code DlcJdbcPersistenceAutoConfiguration}
 * take over the shared {@code DomainPersistenceProvider} slot instead. Mirrors
 * {@code dlc-spring-boot-autoconfig}'s test of the same name.
 */
@SpringBootTest(
    classes = TestApplicationPersistenceAndBuilderAutoConfig.class,
    properties = "dlc.features.persistence.jooq.enabled=false")
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
class JooqPersistenceDisabledByPropertyTest {

    @Autowired
    private DomainObjectBuilderProvider domainObjectBuilderProvider;

    @Autowired
    private JdbcDomainPersistenceProvider jdbcDomainPersistenceProvider;

    @Autowired(required = false)
    private JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Test
    void testJooqDomainPersistenceProviderIsNotPresent() {
        assertThat(jooqDomainPersistenceProvider).isNull();
    }

    @Test
    void testJdbcDomainPersistenceProviderTookOverInstead() {
        assertThat(jdbcDomainPersistenceProvider).isNotNull();
    }

    @Test
    @Transactional
    void testInsertSimpleEntityViaJdbc() {
        //given
        PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();
        SimpleJdbcAggregateRootRepository repository = new SimpleJdbcAggregateRootRepository(
            persistenceEventTestHelper.testEventPublisher, jdbcDomainPersistenceProvider
        );

        TestRootSimple trs = TestRootSimple.builder()
            .setId(new TestRootSimpleId(3L))
            .setName("TestRootJooqDisabled")
            .build();
        persistenceEventTestHelper.resetEventsCaught();

        //when
        TestRootSimple inserted = repository.insert(trs);

        //then
        Optional<TestRootSimple> found = repository.findResultById(new TestRootSimpleId(3L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }
}
