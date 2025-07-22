package io.domainlifecycles.autoconfig.features.multiple.events_builder.gruelbox;

import com.gruelbox.transactionoutbox.TransactionOutbox;
import io.domainlifecycles.autoconfig.model.events.ADomainEvent;
import io.domainlifecycles.autoconfig.model.events.AnApplicationService;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Import(GruelboxEventAndBuilderAutoConfigTestConfiguration.class)
@ActiveProfiles({"test", "test-dlc-domain"})
public class GruelboxEventAndBuilderAutoConfigTests {

    @Autowired
    private TransactionOutbox outbox;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AnApplicationService anApplicationService;

    @Autowired
    private DomainObjectBuilderProvider domainObjectBuilderProvider;

    @Autowired(required = false)
    private JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Autowired(required = false)
    private EntityIdentityProvider entityIdentityProvider;

    @Autowired(required = false)
    private DlcOpenApiCustomizer dlcOpenApiCustomizer;

    @Autowired(required = false)
    private ResponseEntityBuilder responseEntityBuilder;

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

    @Test
    public void testBuild() {
        var aggregateRootTestBuilder = TestRootSimple.builder().setId(new TestRootSimpleId(5L)).setName("Test-Name");
        var innerBuilder = new InnerClassDomainObjectBuilder<>(aggregateRootTestBuilder);
        var built = innerBuilder.build();
        assertThat(built).isNotNull();
    }

    @Test
    void testBuilderProviderIsPresent() {
        assertThat(domainObjectBuilderProvider).isNotNull();
    }

    @Test
    void testNoOtherBeansPresent() {
        assertThat(jooqDomainPersistenceProvider).isNull();
        assertThat(entityIdentityProvider).isNull();
        assertThat(dlcOpenApiCustomizer).isNull();
        assertThat(responseEntityBuilder).isNull();
    }
}
