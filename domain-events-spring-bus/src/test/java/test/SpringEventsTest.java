package test;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@SpringBootTest(
    classes = TestApplicationSpringEvents.class,
    properties = {"spring.modulith.events.jdbc.schema-initialization.enabled=true", "logging.level.org.springframework.modulith=DEBUG"}
)
public class SpringEventsTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private ADomainService aDomainService;

    @Autowired
    private ARepository aRepository;

    @Test
    void testDomainEventToServiceTransactional(){
        // given
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        //when
        var cnt = aDomainService.cnt;
        var evt = new ADomainEvent("TestCommit");
        applicationEventPublisher.publishEvent(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.cnt).isEqualTo(cnt + 1);
                softly.assertAll();
            });
    }

    @Test
    void testDomainEventToServiceTransactionalFail(){
        // given
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        //when
        var cnt = aDomainService.cnt;
        var evt = new ADomainEvent("DomainEventWithException");
        applicationEventPublisher.publishEvent(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .pollDelay(2, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.cnt).isEqualTo(cnt);
                softly.assertAll();
            });
    }

    @Test
    void testDomainEventToServiceFailNoTransaction(){
        //when
        var cnt = aDomainService.cnt;
        var evt = new ADomainEvent("TestCommit");
        applicationEventPublisher.publishEvent(evt);

        //then
        await()
            .pollDelay(2, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.cnt).isEqualTo(cnt);
                softly.assertAll();
            });
    }

    @Test
    void testAggregateDomainEventTransactional(){
        // given
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        //when
        var evt = new AnAggregateDomainEvent("TestCommit", new AnAggregate.AggregateId(1L));
        applicationEventPublisher.publishEvent(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                var agg = aRepository.findById(new AnAggregate.AggregateId(1l));
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(agg).isNotEmpty();
                softly.assertThat(agg.get().received).contains(evt);
                softly.assertAll();
            });
    }

    @Test
    void testAggregateDomainEventTransactionalFail(){
        // given
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        //when
        var evt = new AnAggregateDomainEvent("AggregateDomainEventWithException", new AnAggregate.AggregateId(1L));
        applicationEventPublisher.publishEvent(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .pollDelay(2, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                var agg = aRepository.findById(new AnAggregate.AggregateId(1l));
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(agg).isNotEmpty();
                softly.assertThat(agg.get().received).doesNotContain(evt);
                softly.assertAll();
            });
    }

    @Test
    void testAggregateDomainEventNoTransaction(){
        // given
        //when
        var evt = new AnAggregateDomainEvent("TestCommit", new AnAggregate.AggregateId(1L));
        applicationEventPublisher.publishEvent(evt);
        //then
        await()
            .pollDelay(2, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                var agg = aRepository.findById(new AnAggregate.AggregateId(1l));
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(agg).isNotEmpty();
                softly.assertThat(agg.get().received).doesNotContain(evt);
                softly.assertAll();
            });
    }

    @Test
    void testDomainEventToServiceOverrideNonTransactional(){
        //when
        var cnt = aDomainService.cnt;
        var evt = new A2ndDomainEvent("TestCommit");
        applicationEventPublisher.publishEvent(evt);
        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.cnt).isEqualTo(cnt + 1);
                softly.assertAll();
            });
    }

    @Test
    void testMulticastedDomainEventToServiceTransactional(){
        // given
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        //when
        var cntSrv = aDomainService.cnt;
        var cntRep = aRepository.cnt;
        var evt = new MulticastedDomainEvent("TestCommit");
        applicationEventPublisher.publishEvent(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(aDomainService.cnt).isEqualTo(cntSrv + 1);
                softly.assertThat(aRepository.cnt).isEqualTo(cntRep + 1);
                softly.assertAll();
            });
    }

    @Test
    void testAggregateDomainEventTransactionalNoListenerMethod(){
        // given
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        //when
        var evt = new A2ndDomainEvent("TestCommit");
        applicationEventPublisher.publishEvent(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .pollDelay(2, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                var agg = aRepository.findById(new AnAggregate.AggregateId(1l));
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(agg).isNotEmpty();
                softly.assertThat(agg.get().received).doesNotContain(evt);
                softly.assertAll();
            });
    }

    @Test
    void testAggregateDomainEventTransactionalNoAggregateFound(){
        // given
        var status = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());

        //when
        var evt = new AnAggregateDomainEvent("TestCommit", new AnAggregate.AggregateId(2L));
        applicationEventPublisher.publishEvent(evt);

        platformTransactionManager.commit(status);

        //then
        await()
            .pollDelay(2, SECONDS)
            .atMost(10, SECONDS)
            .untilAsserted(()-> {
                var agg = aRepository.findById(new AnAggregate.AggregateId(2l));
                SoftAssertions softly = new SoftAssertions();
                softly.assertThat(agg).isEmpty();
                softly.assertAll();
            });
    }
}
