package io.domainlifecycles.services.api;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sample.TestApplicationService;
import sample.TestDomainService;
import sample.TestOutboundService;
import sample.TestQueryClient;
import sample.TestRepository;
import static org.assertj.core.api.Assertions.assertThat;

class ServiceProviderTest {

    private static ServiceProvider provider;

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "io.domainlifecycles"));
        provider = initServices();
    }

    private static ServiceProvider initServices() {

        Services services = new Services();

        services.registerDomainServiceInstance(new TestDomainService());
        services.registerApplicationServiceInstance(new TestApplicationService());
        services.registerRepositoryInstance(new TestRepository());
        services.registerQueryClientInstance(new TestQueryClient());
        services.registerOutboundServiceInstance(new TestOutboundService());

        return services;
    }

    @Test
    void testGetApplicationServiceInstanceOk() {

        ApplicationService service = provider.getApplicationServiceInstance("sample.TestApplicationService");

        assertThat(service).isNotNull();
        assertThat(service).isInstanceOf(TestApplicationService.class);
    }
}
