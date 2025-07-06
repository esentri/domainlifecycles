package io.domainlifecycles.autoconfig.features.single.events.gruelbox;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.autoconfig.model.events.ADomainEvent;
import io.domainlifecycles.autoconfig.model.events.AnApplicationService;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles({"test", "test-dlc-domain"})
public class GruelboxEventAutoConfigTests {

    @Autowired
    private TransactionOutbox outbox;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AnApplicationService anApplicationService;

    @Test
    @DirtiesContext
    public void testTransactionOutbox(){
        var val = new ADomainEvent("GruelboxEvent");
        transactionTemplate.executeWithoutResult((status) ->
            outbox.with()
                .ordered("topic1")
                .delayForAtLeast(Duration.ZERO)
                .schedule(AnApplicationService.class)
                .onADomainEvent(val)
        );

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(anApplicationService.received)
                    .contains(val)
            );
    }
}
