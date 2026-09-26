package io.domainlifecycles.boot3.autoconfig.features.multiple.persistence_builder_jdbc;

import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.cache.SpringTransactionCacheAwareConnectionProvider;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
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
 * Covers {@code DlcJdbcPersistenceAutoConfiguration}, the plain-JDBC counterpart of
 * {@code PersistenceAndBuilderAutoConfigTest}: excludes {@code DlcJooqPersistenceAutoConfiguration} (see
 * {@link TestApplicationJdbcPersistenceAndBuilderAutoConfig}) so this is the only persistence autoconfig
 * active, reusing the exact same database/migration/domain model as the jOOQ test - {@code jdbc-integration}
 * needs no generated record classes, so nothing else has to change to point it at the same schema. Mirrors
 * {@code dlc-spring-boot-autoconfig}'s test of the same name.
 */
@SpringBootTest(classes = TestApplicationJdbcPersistenceAndBuilderAutoConfig.class)
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
class JdbcPersistenceAndBuilderAutoConfigTest {

    @Autowired
    private JdbcConnectionProvider jdbcConnectionProvider;

    @Autowired
    private DomainObjectBuilderProvider domainObjectBuilderProvider;

    @Autowired
    private JdbcDomainPersistenceProvider jdbcDomainPersistenceProvider;

    @Test
    @Transactional
    void testInsertSimpleEntity() {
        //given
        PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();
        SimpleJdbcAggregateRootRepository repository = new SimpleJdbcAggregateRootRepository(
            persistenceEventTestHelper.testEventPublisher, jdbcDomainPersistenceProvider
        );

        TestRootSimple trs = TestRootSimple.builder()
            .setId(new TestRootSimpleId(2L))
            .setName("TestRootJdbc")
            .build();
        persistenceEventTestHelper.resetEventsCaught();

        //when
        TestRootSimple inserted = repository.insert(trs);

        //then
        Optional<TestRootSimple> found = repository.findResultById(new TestRootSimpleId(2L)).resultValue();
        persistenceEventTestHelper.assertFoundWithResult(found, inserted);
        persistenceEventTestHelper.addExpectedEvent(PersistenceEvent.PersistenceEventType.INSERTED, inserted);
        persistenceEventTestHelper.assertEvents();
    }

    @Test
    void testBuilderProviderIsPresent() {
        assertThat(domainObjectBuilderProvider).isNotNull();
    }

    @Test
    void testDlcOwnJdbcWiringIsActiveByDefault() {
        //the Spring transaction cache binder for jdbc-integration is wired by DlcJdbcPersistenceAutoConfiguration
        //automatically - no manual configuration needed, unlike jdbc-integration's readme documents for a
        //setup without this autoconfig
        assertThat(jdbcConnectionProvider).isInstanceOf(SpringTransactionCacheAwareConnectionProvider.class);
    }
}
