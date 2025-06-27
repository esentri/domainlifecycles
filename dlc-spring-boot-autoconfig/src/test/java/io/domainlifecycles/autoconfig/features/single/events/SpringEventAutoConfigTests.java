package io.domainlifecycles.autoconfig.features.single.events;


import io.domainlifecycles.autoconfig.features.single.events.model.ADomainEvent;
import io.domainlifecycles.autoconfig.features.single.events.model.ADomainService;
import io.domainlifecycles.autoconfig.features.single.events.model.AQueryHandler;
import io.domainlifecycles.autoconfig.features.single.events.model.ARepository;
import io.domainlifecycles.autoconfig.features.single.events.model.AnApplicationService;
import io.domainlifecycles.autoconfig.features.single.events.model.AnOutboundService;
import io.domainlifecycles.events.api.DomainEvents;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class SpringEventAutoConfigTests {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

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
    public void testIntegrationCommit() {
        // given
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        //when
        var evt = new ADomainEvent("TestCommit");
        DomainEvents.publish(evt);

        platformTransactionManager.commit(status);

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
}
