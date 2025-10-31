package io.domainlifecycles.autoconfig.features.single.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.access.classes.ClassProvider;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimple;
import io.domainlifecycles.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.events.api.ChannelRoutingConfiguration;
import io.domainlifecycles.events.api.DomainEventTypeBasedRouter;
import io.domainlifecycles.events.api.PublishingChannel;
import io.domainlifecycles.events.consume.execution.handler.TransactionalHandlerExecutor;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.services.api.ServiceProvider;
import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = TestApplicationJacksonSingleAutoConfig.class
)
@Import(JacksonAutoConfigTestConfiguration.class)
@ActiveProfiles({"test"})
public class JacksonAutoConfigTests {

    @Autowired
    private DlcJacksonModule dlcJacksonModule;

    @Autowired
    private ObjectMapper objectMapper;

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
    JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Autowired(required = false)
    EntityIdentityProvider entityIdentityProvider;

    @Autowired(required = false)
    DlcOpenApiCustomizer dlcOpenApiCustomizer;

    @Autowired(required = false)
    ResponseEntityBuilder responseEntityBuilder;

    @Test
    public void testDlcJacksonModuleIsPresent() {
        assertThat(dlcJacksonModule).isNotNull();
    }

    @Test
    public void testTestRootSimpleJacksonMapping() throws JsonProcessingException {
        TestRootSimple testRootSimple = TestRootSimple.builder().setId(new TestRootSimpleId(1L)).setName("TEST").build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testRootSimple);

        TestRootSimple mappedTestRootSimple = objectMapper.readValue(json, TestRootSimple.class);
        Assertions.assertThat(mappedTestRootSimple).isEqualTo(testRootSimple);
    }

    @Test
    void testNoOtherBeansPresent() {
        assertThat(serviceProvider).isNull();
        assertThat(transactionalHandlerExecutor).isNull();
        assertThat(classProvider).isNull();
        assertThat(router).isNull();
        assertThat(routingConfiguration).isNull();
        assertThat(publishingChannel).isNull();
        assertThat(jooqDomainPersistenceProvider).isNull();
        assertThat(entityIdentityProvider).isNull();
        assertThat(dlcOpenApiCustomizer).isNull();
        assertThat(responseEntityBuilder).isNull();
    }
}


