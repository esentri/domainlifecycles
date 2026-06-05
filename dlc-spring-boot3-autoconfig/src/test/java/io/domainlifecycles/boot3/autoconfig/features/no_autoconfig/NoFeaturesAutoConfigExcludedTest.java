package io.domainlifecycles.boot3.autoconfig.features.no_autoconfig;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.jackson2.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplicationNoFeaturesAutoConfig.class)
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
@DirtiesContext
public class NoFeaturesAutoConfigExcludedTest {

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
    io.domainlifecycles.jackson2.module.DlcJacksonModule dlcJackson2Module;

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
        assertThat(domainObjectBuilderProvider).isNull();
        assertThat(jooqDomainPersistenceProvider).isNull();
        assertThat(router).isNull();
        assertThat(routingConfiguration).isNull();
        assertThat(publishingChannel).isNull();
        assertThat(dlcJacksonModule).isNull();
        assertThat(dlcJackson2Module).isNull();
        assertThat(entityIdentityProvider).isNull();
        assertThat(dlcOpenApiCustomizer).isNull();
        assertThat(responseEntityBuilder).isNull();
    }
}
