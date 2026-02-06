package io.domainlifecycles.boot3.autoconfig.features.multiplechannels;

import io.domainlifecycles.boot3.autoconfig.model.events.ADomainEvent;
import io.domainlifecycles.boot3.autoconfig.model.events.ADomainService;
import io.domainlifecycles.boot3.autoconfig.model.events.AQueryHandler;
import io.domainlifecycles.boot3.autoconfig.model.events.ARepository;
import io.domainlifecycles.boot3.autoconfig.model.events.AnAggregateDomainEvent;
import io.domainlifecycles.boot3.autoconfig.model.events.AnApplicationService;
import io.domainlifecycles.boot3.autoconfig.model.events.AnOutboundService;
import io.domainlifecycles.events.api.DomainEvents;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = TestApplicationEvents.class, properties = {"dlc.events.inmemory.enabled=true", "dlc.events.springbus.enabled=true"})
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
public class MultipleEventChannelsTest {

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private ADomainService aDomainService;

    @Autowired
    private ARepository aRepository;

    @Autowired
    private AnApplicationService anApplicationService;

    @Autowired
    private AQueryHandler queryHandler;

    @Autowired
    private AnOutboundService outboundService;

    @Test
    @DirtiesContext
    public void testIntegrationNoTx() {
        //when
        var evt = new ADomainEvent("TestNoTx");
        DomainEvents.publish(evt);
        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.received).contains(evt);
                softly.assertThat(aRepository.received).contains(evt);
                softly.assertThat(anApplicationService.received).contains(evt);
                softly.assertThat(queryHandler.received).contains(evt);
                softly.assertThat(outboundService.received).contains(evt);
                softly.assertAll();
            });
    }

    @Test
    @DirtiesContext
    public void testIntegrationTx() {
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        var evt = new AnAggregateDomainEvent("TestCommit");
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then

        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                var agg = aRepository.findById(evt.targetId());
                softly.assertThat(agg).isPresent();
                agg.ifPresent(anAggregate -> softly.assertThat(anAggregate.received).contains(evt));
                softly.assertAll();
            });
    }
}
