package io.domainlifecycles.autoconfig.features.all;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.api.PublishingRouter;
import io.domainlifecycles.jackson3.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplicationAllFeaturesAutoConfig.class)
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
@Import(AllFeaturesAutoConfigTestConfiguration.class)
public class AllFeaturesAutoConfigTest {

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

    @Autowired(required = false)
    List<ServiceKind> allServiceKinds;

    @Test
    void testAllBeansPresent() {
        assertThat(Domain.isInitialized()).isTrue();
        assertThat(domainObjectBuilderProvider).isNotNull();
        assertThat(jooqDomainPersistenceProvider).isNotNull();
        assertThat(router).isNotNull();
        assertThat(routingConfiguration).isNotNull();
        assertThat(publishingChannel).isNotNull();
        assertThat(dlcJacksonModule).isNotNull();
        assertThat(dlcJackson2Module).isNotNull();
        assertThat(entityIdentityProvider).isNotNull();
        assertThat(dlcOpenApiCustomizer).isNotNull();
        assertThat(responseEntityBuilder).isNotNull();
        assertThat(allServiceKinds).hasSize(6);
    }
}
