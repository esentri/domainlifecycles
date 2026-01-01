package io.domainlifecycles.autoconfig.features.all;

import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.services.api.ServiceProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
public class AllFeaturesAutoConfigTest {

    @Autowired(required = false)
    DomainObjectBuilderProvider domainObjectBuilderProvider;

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
    JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Autowired(required = false)
    EntityIdentityProvider entityIdentityProvider;

    @Autowired(required = false)
    DlcOpenApiCustomizer dlcOpenApiCustomizer;

    @Autowired(required = false)
    ResponseEntityBuilder responseEntityBuilder;

    @Test
    void testAllBeansPresent() {
        assertThat(Domain.isInitialized()).isTrue();
        assertThat(domainObjectBuilderProvider).isNotNull();
        assertThat(serviceProvider).isNotNull();
        assertThat(jooqDomainPersistenceProvider).isNotNull();
        assertThat(transactionalHandlerExecutor).isNotNull();
        assertThat(classProvider).isNotNull();
        assertThat(router).isNotNull();
        assertThat(routingConfiguration).isNotNull();
        assertThat(publishingChannel).isNotNull();
        assertThat(dlcJacksonModule).isNotNull();

        assertThat(entityIdentityProvider).isNotNull();
        assertThat(dlcOpenApiCustomizer).isNotNull();
        assertThat(responseEntityBuilder).isNotNull();
    }
}
