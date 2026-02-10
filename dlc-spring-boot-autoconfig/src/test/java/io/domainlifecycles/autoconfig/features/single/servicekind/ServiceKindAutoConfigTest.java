package io.domainlifecycles.autoconfig.features.single.servicekind;

import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.jackson3.module.DlcJacksonModule;
import io.domainlifecycles.services.api.ServiceProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes= TestApplicationServiceKindSingleAutoConfig.class)
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
@Import(ServiceKindAutoConfigTestConfiguration.class)
public class ServiceKindAutoConfigTest {

    @Autowired
    List<ServiceKind> allServiceKinds;

    @Autowired(required = false)
    ServiceProvider serviceProvider;

    @Autowired(required = false)
    TransactionalHandlerExecutor transactionalHandlerExecutor;

    @Autowired(required = false)
    ClassProvider classProvider;

    @Autowired(required = false)
    DomainEventTypeBasedRouter router;

    @Autowired(required = false)
    ChannelRoutingConfiguration routingConfiguration;

    @Autowired(required = false)
    PublishingChannel publishingChannel;

    @Autowired(required = false)
    DlcJacksonModule dlcJacksonModule;

    @Autowired(required = false)
    DlcOpenApiCustomizer dlcOpenApiCustomizer;

    @Autowired(required = false)
    ResponseEntityBuilder responseEntityBuilder;

    @Test
    public void testServiceKindsPresent() {
        assertThat(allServiceKinds).hasSize(6);
    }

    @Test
    void testNoOtherBeansPresent() {
        assertThat(serviceProvider).isNull();
        assertThat(transactionalHandlerExecutor).isNull();
        assertThat(classProvider).isNull();
        assertThat(router).isNull();
        assertThat(routingConfiguration).isNull();
        assertThat(publishingChannel).isNull();
        assertThat(dlcJacksonModule).isNull();
        assertThat(dlcOpenApiCustomizer).isNull();
        assertThat(responseEntityBuilder).isNull();
    }
}
