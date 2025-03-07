package io.domainlifecycles.events.springoutbox;

import io.domainlifecycles.events.api.ProcessingChannel;
import io.domainlifecycles.events.spring.outbox.api.ProcessingResult;
import io.domainlifecycles.events.spring.outbox.api.SpringOutboxConsumingConfiguration;
import io.domainlifecycles.events.spring.outbox.api.SpringTransactionalOutboxChannelFactory;
import io.domainlifecycles.events.spring.outbox.impl.SpringJdbcOutbox;
import lombok.extern.slf4j.Slf4j;
import org.h2.jdbc.JdbcSQLTimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = OutboxTestApplication.class)
@Slf4j
public class SpringJdbcOutboxTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SpringTransactionalOutboxChannelFactory factory;

    private final String SELECT_OUTBOX = "SELECT * FROM outbox";

    private final String DELETE_OUTBOX = "DELETE FROM outbox";

    private final String INSERT_OUTBOX_OLD_SUCCESS = "INSERT INTO outbox (id, domain_event, inserted, batch_id, " +
        "processing_result, delivery_started) VALUES (" +
        "'" + UUID.randomUUID().toString() + "'," +
        "'TEST'," +
        "DATEADD(DAY, -2, CURRENT_TIMESTAMP)," +
        "'" + UUID.randomUUID().toString() + "'," +
        "'OK'," +
        "CURRENT_TIMESTAMP" +
        ");";

    private final String INSERT_OUTBOX_DELIVERY_TIMEOUT = "INSERT INTO outbox (id, domain_event, inserted, batch_id, " +
        "processing_result, delivery_started) VALUES (" +
        "'" + UUID.randomUUID().toString() + "'," +
        "'TEST'," +
        "CURRENT_TIMESTAMP," +
        "'" + UUID.randomUUID().toString() + "'," +
        "'OK'," +
        "DATEADD(DAY, -2, CURRENT_TIMESTAMP)" +
        ");";

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private ProcessingChannel channel;

    @BeforeEach
    public void init() {
        var config = (SpringOutboxConsumingConfiguration)channel.getConsumingConfiguration();
        config.getOutboxPoller().setPollingDelayMilliseconds(100000L);
    }

    @AfterEach
    public void after() {
        var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var statusDel = platformTransactionManager.getTransaction(def);
        jdbcTemplate.execute(DELETE_OUTBOX);
        platformTransactionManager.commit(statusDel);
    }

    @Test
    @Transactional
    public void testInsert() {
        var domainEvent = new OutboxTestEvent("OutboxTest");
        factory.getTransactionalOutbox().insert(domainEvent);

        var res = jdbcTemplate.query(SELECT_OUTBOX, (r, i) -> mapResultsetOutbox(r));

        assertThat(res.size()).isGreaterThan(0);
        log.debug("OutboxEntry: " + res.get(0));
        assertThat(res.get(0).domainEvent).contains("OutboxTest");
    }

    @Test
    public void testInsertFailed() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var domainEvent = new OutboxTestEvent("OutboxTestFailed");
        factory.getTransactionalOutbox().insert(domainEvent);
        platformTransactionManager.commit(status);

        factory.getTransactionalOutbox().markFailed(domainEvent, ProcessingResult.FAILED);
        var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var statusSelect = platformTransactionManager.getTransaction(def);
        var res = jdbcTemplate.query(SELECT_OUTBOX, (r, i) -> mapResultsetOutbox(r));
        platformTransactionManager.commit(statusSelect);

        assertThat(res.size()).isGreaterThan(0);
        log.debug("OutboxEntry: " + res.get(0));
        assertThat(res.get(0).domainEvent).contains("OutboxTest");
        assertThat(res.get(0).processingResult).isEqualTo(ProcessingResult.FAILED.name());
    }

    @Test
    public void testInsertSuccess() {
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var domainEvent = new OutboxTestEvent("OutboxTest");
        factory.getTransactionalOutbox().insert(domainEvent);
        platformTransactionManager.commit(status);
        var batch = factory.getTransactionalOutbox().fetchBatchForSending(1);
        factory.getTransactionalOutbox().sentSuccessfully(batch);
        var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var statusSelect = platformTransactionManager.getTransaction(def);
        var res = jdbcTemplate.query(SELECT_OUTBOX, (r, i) -> mapResultsetOutbox(r));
        platformTransactionManager.commit(statusSelect);

        assertThat(res.size()).isGreaterThan(0);
        log.debug("OutboxEntry: " + res.get(0));
        assertThat(res.get(0).domainEvent).contains("OutboxTest");
        assertThat(res.get(0).processingResult).isEqualTo(ProcessingResult.OK.name());
        assertThat(res.get(0).batchId).isEqualTo(batch.getBatchId().toString());
    }

    @Test
    public void testFetchConcurrently() {
        var defReqNew = new DefaultTransactionDefinition();
        defReqNew.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var statusInsert = platformTransactionManager.getTransaction(defReqNew);
        var domainEvent = new OutboxTestEvent("OutboxTest");
        factory.getTransactionalOutbox().insert(domainEvent);
        platformTransactionManager.commit(statusInsert);

        var batchA = factory.getTransactionalOutbox().fetchBatchForSending(1);
        assertThat(batchA.getDomainEvents()).hasSize(1);
        var batchB = factory.getTransactionalOutbox().fetchBatchForSending(1);
        assertThat(batchB.getDomainEvents()).hasSize(0);
    }

    @Test
    public void testFetchConcurrentlyNonStrict(){
        var springOutbox = (SpringJdbcOutbox)this.factory.getTransactionalOutbox();
        springOutbox.setStrictBatchOrder(false);
        var defReqNew = new DefaultTransactionDefinition();
        defReqNew.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var statusInsert = platformTransactionManager.getTransaction(defReqNew);
        var domainEvent = new OutboxTestEvent("OutboxTest");
        factory.getTransactionalOutbox().insert(domainEvent);
        platformTransactionManager.commit(statusInsert);

        var batchA = factory.getTransactionalOutbox().fetchBatchForSending(1);
        assertThat(batchA.getDomainEvents()).hasSize(1);
        var batchB = factory.getTransactionalOutbox().fetchBatchForSending(1);
        assertThat(batchB.getDomainEvents()).hasSize(0);
        springOutbox.setStrictBatchOrder(true);
    }

    @Test
    public void testFetchConcurrentlyNoWait() {
        var defReqNew = new DefaultTransactionDefinition();
        defReqNew.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var statusInsert = platformTransactionManager.getTransaction(defReqNew);
        var domainEvent = new OutboxTestEvent("OutboxTest");
        factory.getTransactionalOutbox().insert(domainEvent);
        platformTransactionManager.commit(statusInsert);

        var select = "select from outbox for update nowait;";
        var statusFetchA = platformTransactionManager.getTransaction(defReqNew);
        jdbcTemplate.execute(select);
        var statusFetchB = platformTransactionManager.getTransaction(defReqNew);
        assertThatThrownBy(() -> {
            jdbcTemplate.execute(select);
        }).hasCauseInstanceOf(JdbcSQLTimeoutException.class);
        platformTransactionManager.commit(statusFetchA);
    }

    @Test
    public void testFetchOrder() {
        var defReqNew = new DefaultTransactionDefinition();
        defReqNew.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var statusInsert = platformTransactionManager.getTransaction(defReqNew);
        var domainEvent1 = new OutboxTestEvent("OutboxTestOrder1");
        factory.getTransactionalOutbox().insert(domainEvent1);
        platformTransactionManager.commit(statusInsert);

        var statusInsert2 = platformTransactionManager.getTransaction(defReqNew);
        var domainEvent2 = new OutboxTestEvent("OutboxTestOrder2");
        factory.getTransactionalOutbox().insert(domainEvent2);
        platformTransactionManager.commit(statusInsert2);

        var batchA = factory.getTransactionalOutbox().fetchBatchForSending(10);

        assertThat(batchA.getDomainEvents().get(0)).isEqualTo(domainEvent1);
        assertThat(batchA.getDomainEvents().get(1)).isEqualTo(domainEvent2);
    }

    @Test
    public void testCleanup() {
        var defReqNew = new DefaultTransactionDefinition();
        defReqNew.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var statusInsert = platformTransactionManager.getTransaction(defReqNew);
        jdbcTemplate.execute(INSERT_OUTBOX_OLD_SUCCESS);
        platformTransactionManager.commit(statusInsert);
        SpringJdbcOutbox springJdbcOutbox = ((SpringJdbcOutbox)factory.getTransactionalOutbox());
        springJdbcOutbox.cleanup(springJdbcOutbox.getCleanUpAgeDays());
        var res = jdbcTemplate.query(SELECT_OUTBOX, (r, i) -> mapResultsetOutbox(r));
        assertThat(res.size()).isEqualTo(0);
    }

    @Test
    public void testDeliveryCheck() {
        var defReqNew = new DefaultTransactionDefinition();
        defReqNew.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        var statusInsert = platformTransactionManager.getTransaction(defReqNew);
        jdbcTemplate.execute(INSERT_OUTBOX_DELIVERY_TIMEOUT);
        platformTransactionManager.commit(statusInsert);
        SpringJdbcOutbox springJdbcOutbox = ((SpringJdbcOutbox)factory.getTransactionalOutbox());
        springJdbcOutbox.deliveryCheck(springJdbcOutbox.getBatchDeliveryTimeoutSeconds());
        var res = jdbcTemplate.query(SELECT_OUTBOX, (r, i) -> mapResultsetOutbox(r));
        assertThat(res.size()).isEqualTo(1);
        assertThat(res.get(0).processingResult).isEqualTo("DELIVERY_TIMED_OUT");
    }


    private OutboxEntry mapResultsetOutbox(ResultSet rs) throws SQLException {
        return new OutboxEntry(
            rs.getString(1),
            rs.getString(2),
            rs.getTimestamp(3).toLocalDateTime(),
            rs.getString(4),
            rs.getString(5),
            rs.getTimestamp(6) == null ? null : rs.getTimestamp(6).toLocalDateTime()
        );
    }

    private record OutboxEntry(String id, String domainEvent, LocalDateTime inserted, String batchId,
                               String processingResult, LocalDateTime deliveryStarted) {
    }

}
