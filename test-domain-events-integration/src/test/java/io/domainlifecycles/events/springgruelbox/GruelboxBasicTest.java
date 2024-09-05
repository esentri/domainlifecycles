package io.domainlifecycles.events.springgruelbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.events.AComplexDomainEvent;
import io.domainlifecycles.events.ADomainEvent;
import io.domainlifecycles.events.ADomainEventCarryingAnId;
import io.domainlifecycles.events.AValueObject;
import io.domainlifecycles.events.AnApplicationService;
import io.domainlifecycles.events.AnIdentity;
import io.domainlifecycles.events.AnotherApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@SpringBootTest(classes = TestApplicationGruelbox.class)
@Slf4j
public class GruelboxBasicTest {

    @Autowired
    private TransactionOutbox outbox;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AnApplicationService anApplicationService;

    @Autowired
    private AnotherApplicationService anotherApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    public void testTransactionOutbox(){
        var val = new ADomainEvent("GruelboxEvent");
        transactionTemplate.executeWithoutResult((status)->{
            outbox.with().ordered("topic1").delayForAtLeast(Duration.ZERO).schedule(AnApplicationService.class).onADomainEvent(val);
        });

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(anApplicationService.received)
                    .contains(val)
            );
    }

    @Test
    @DirtiesContext
    public void testTransactionOutboxDeSerialization(){
        var val = new ADomainEventCarryingAnId(new AnIdentity(5l));
        transactionTemplate.executeWithoutResult((status)->{
            outbox.with().ordered("topicId").delayForAtLeast(Duration.ZERO).schedule(AnotherApplicationService.class).onADomainEvent(val);
        });

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(anotherApplicationService.received)
                    .contains(val)
            );
    }

    @Test
    @DirtiesContext
    public void testTransactionOutboxDeSerializationComplexEvent() {
        var val = new AComplexDomainEvent(
            new AnIdentity(44l),
            new AValueObject("test", 6l, new AnIdentity(77l)),
            List.of(new AnIdentity(88l)),
            List.of(
                new AValueObject("inList1", 0l, new AnIdentity(456l)),
                new AValueObject("inList2", 1l, new AnIdentity(4567l))
            )
        );

        transactionTemplate.executeWithoutResult((status)->{
            outbox.with().ordered("topicComplex").delayForAtLeast(Duration.ZERO).schedule(AnotherApplicationService.class).processComplexEvent(val);
        });

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()->
                assertThat(anotherApplicationService.received)
                    .contains(val)
            );
    }

    @Test
    @DirtiesContext
    public void testComplexDomainEventDeserialization() throws Exception{
        var val = new AComplexDomainEvent(
            new AnIdentity(44l),
            new AValueObject("test", 6l, new AnIdentity(77l)),
            List.of(new AnIdentity(88l)),
            List.of(
                new AValueObject("inList1", 0l, new AnIdentity(456l)),
                new AValueObject("inList2", 1l, new AnIdentity(4567l))
            )
        );

        var serialized = objectMapper.writeValueAsString(val);
        log.debug("Serialized= " + serialized);
        var deserial = objectMapper.readValue(serialized, AComplexDomainEvent.class);
        log.debug("Deserialized= " + deserial);
        assertThat(deserial).isEqualTo(val);
    }





}
