package io.domainlifecycles.springboot.events;

import io.domainlifecycles.events.api.DomainEvents;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles({"test"})
public class Events_SpringBoot_ITest {

    @Autowired
    private TestService testService;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Test
    public void testEventHandling() {
        //given
        var evt = new TestEvent("test");
        //when
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        DomainEvents.publish(evt);
        platformTransactionManager.commit(status);
        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(testService.received).contains(evt);
                softly.assertAll();
            });
    }
}
