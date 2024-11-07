package io.domainlifecycles.jooq.persistence;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tests.shared.persistence.PersistenceEventTestHelper;

@Slf4j
public class BasePersistence_ITest {

    protected BaseDLCTestPersistenceConfiguration persistenceConfiguration = new BaseDLCTestPersistenceConfiguration();
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

