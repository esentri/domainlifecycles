package io.domainlifecycles.autoconfig.features.no_annotation;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.jackson3.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
public class NoFeaturesAutoConfigTest {

    @Autowired(required = false)
    DomainObjectBuilderProvider domainObjectBuilderProvider;

    @Autowired(required = false)
    PublishingRouter router;

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
    void testNoBeansPresent() {
        assertThat(Domain.isInitialized()).isFalse();
        assertThat(domainObjectBuilderProvider).isNull();
        assertThat(jooqDomainPersistenceProvider).isNull();
        assertThat(router).isNull();
        assertThat(routingConfiguration).isNull();
        assertThat(publishingChannel).isNull();
        assertThat(dlcJacksonModule).isNull();
        assertThat(entityIdentityProvider).isNull();
        assertThat(dlcOpenApiCustomizer).isNull();
        assertThat(responseEntityBuilder).isNull();
    }
}
