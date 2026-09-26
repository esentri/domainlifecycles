package io.domainlifecycles.test.boot3.autoconfig.jdbc;

import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.boot3.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;

/**
 * Deliberately outside {@code io.domainlifecycles.boot3.autoconfig} (unlike its jOOQ counterpart
 * {@code SimpleAggregateRootRepository}): {@code DlcServiceKindAutoConfiguration}'s
 * {@code ServiceKindTwoPassPostProcessor} scans {@code dlc.features.mirror.base-packages} (set to
 * {@code io.domainlifecycles.boot3.autoconfig} across this module's test suite) for every concrete
 * {@code Repository}/{@code ServiceKind} implementation and registers it as a bean in <em>any</em> test whose
 * context activates that autoconfig - regardless of which persistence backend (if any) that particular test
 * actually configured. A class living there would get swept into unrelated tests lacking a
 * {@link JdbcDomainPersistenceProvider} bean, failing them with an unsatisfied constructor dependency; this
 * package is never scanned, so only the JDBC persistence tests that construct this class directly ever touch
 * it. Mirrors {@code dlc-spring-boot-autoconfig}'s {@code SimpleJdbcAggregateRootRepository}.
 */
public class SimpleJdbcAggregateRootRepository extends JdbcAggregateRepository<TestRootSimple, TestRootSimpleId> {

    public SimpleJdbcAggregateRootRepository(PersistenceEventPublisher persistenceEventPublisher,
                                             JdbcDomainPersistenceProvider jdbcDomainPersistenceProvider) {
        super(
            TestRootSimple.class,
            jdbcDomainPersistenceProvider,
            persistenceEventPublisher);
    }
}
