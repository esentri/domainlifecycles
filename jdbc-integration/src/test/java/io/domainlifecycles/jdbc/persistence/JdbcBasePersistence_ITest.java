package io.domainlifecycles.jdbc.persistence;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tests.shared.persistence.PersistenceEventTestHelper;

/**
 * Test-only counterpart of jooq-integration's {@code BasePersistence_ITest}: wraps every test method in a
 * transaction on the same {@link JdbcTestPersistenceConfiguration}, rolled back afterward, so the shared H2
 * schema can be reused across the whole test suite without tests interfering with each other.
 */
@Slf4j
public class JdbcBasePersistence_ITest {

    protected JdbcTestPersistenceConfiguration persistenceConfiguration = new JdbcTestPersistenceConfiguration();
    protected PersistenceEventTestHelper persistenceEventTestHelper = new PersistenceEventTestHelper();

    @BeforeEach
    public void startTransaction() {
        persistenceConfiguration.startTransaction();
        persistenceEventTestHelper.resetEventsCaught();
        log.debug("New transaction started!");
    }

    @AfterEach
    public void rollbackTransaction() {
        persistenceConfiguration.rollbackTransaction();
        log.debug("Transaction rolled back!");
    }

    public record Result(Object persisted, Object found) {
    }
}
