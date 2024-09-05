package io.domainlifecycles.events.springgruelboxintegrationidempotency;

import com.gruelbox.transactionoutbox.TransactionOutboxEntry;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.events.IdemProtectedDomainEvent;
import io.domainlifecycles.events.IdemProtectedListener;
import io.domainlifecycles.events.MyTransactionOutboxListener;
import io.domainlifecycles.events.api.DomainEvents;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = TestApplicationGruelboxIntegrationIdempotency.class)
@DirtiesContext
@Slf4j
public class GreulboxIntegrationIdempotencyTests {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private IdemProtectedListener idemProtectedListener;

    @Autowired
    private MyTransactionOutboxListener outboxListener;

    private boolean match(TransactionOutboxEntry entry, DomainEvent domainEvent){
        return domainEvent.equals(entry.getInvocation().getArgs()[0]);
    }

    @Test
    public void testProtectedListenerCommit(){
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new IdemProtectedDomainEvent("TestCommit"+ UUID.randomUUID().toString());
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(1)
            );
        assertThat(idemProtectedListener.received).contains(evt);
    }

    @Test
    public void testProtectedListenerRollback(){
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt = new IdemProtectedDomainEvent("TestRollback"+ UUID.randomUUID().toString());
        DomainEvents.publish(evt);

        platformTransactionManager.rollback(status);

        //then
        await()
            .pollDelay(5, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isEqualTo(0)
            );
        assertThat(idemProtectedListener.received).doesNotContain(evt);
    }

    @Test
    public void testProtectedListenerProtection(){
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var val = "Protected"+ UUID.randomUUID().toString();
        //when
        var evt = new IdemProtectedDomainEvent(val);
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);

        status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var evt2 = new IdemProtectedDomainEvent(val);
        DomainEvents.publish(evt2);

        platformTransactionManager.commit(status);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(outboxListener.successfulEntries.stream().filter(e -> match(e, evt)).count()).isGreaterThan(1)
            );
        assertThat(idemProtectedListener.received).containsOnlyOnceElementsOf(List.of(evt));
    }
}
