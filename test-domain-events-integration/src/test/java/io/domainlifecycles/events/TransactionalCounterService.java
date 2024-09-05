package io.domainlifecycles.events;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.ListensTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class TransactionalCounterService implements ApplicationService {

    private final JdbcTemplate jdbcTemplate;

    public TransactionalCounterService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        createCounterTableIfNotExists();
    }

    public void createCounterTableIfNotExists(){
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS CNT (VAL INTEGER)");
        jdbcTemplate.execute("DELETE FROM CNT");
        jdbcTemplate.execute("INSERT INTO CNT(VAL) VALUES (1)");
    }

    public void increaseCounterInTransaction(){
        if(TransactionSynchronizationManager.isActualTransactionActive()) {
            log.info("Increasing counter!");
            jdbcTemplate.execute("UPDATE CNT SET VAL = VAL + 1");
        }
    }

    public int getCurrentCounterValue(){
        int result = jdbcTemplate.queryForObject(
            "SELECT VAL FROM CNT", Integer.class);
        return result;
    }

    @ListensTo(domainEventType = CounterDomainEvent.class)
    public void counterEventSuccess(CounterDomainEvent event){
        increaseCounterInTransaction();
    }

    @ListensTo(domainEventType = CounterDomainEvent.class)
    public void counterEventFail(CounterDomainEvent event){
        increaseCounterInTransaction();
        throw new IllegalStateException("Forced failure!");
    }
}
