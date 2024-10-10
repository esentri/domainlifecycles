package io.domainlifecycles.services.api;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.Repository;
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

class ServiceTest {

    private static ServiceProvider provider;

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("sample", "io.domainlifecycles"));
        provider = initServices();
    }

    private static ServiceProvider initServices() {

        Services services = new Services();

        services.registerServiceKindInstance(new TestDomainService());
        services.registerServiceKindInstance(new TestApplicationService());
        services.registerServiceKindInstance(new TestRepository());
        services.registerServiceKindInstance(new TestQueryClient());
        services.registerServiceKindInstance(new TestOutboundService());

        return services;
    }

    @Test
    void testGetApplicationServiceInstanceOk() {

        ApplicationService service = provider.getApplicationServiceInstance("sample.TestApplicationService");

        assertThat(service).isNotNull();
        assertThat(service).isInstanceOf(TestApplicationService.class);
    }

    @Test
    void testGetDomainServiceInstanceOk() {

        DomainService service = provider.getDomainServiceInstance("sample.TestDomainService");

        assertThat(service).isNotNull();
        assertThat(service).isInstanceOf(TestDomainService.class);
    }

    @Test
    void testGetRepositoryInstanceOk() {

        Repository<?, ?> repository = provider.getRepositoryInstance("sample.TestRepository");

        assertThat(repository).isNotNull();
        assertThat(repository).isInstanceOf(TestRepository.class);
    }

    @Test
    void testGetQueryClientOk() {

        QueryClient<?> client = provider.getQueryClientInstance("sample.TestQueryClient");

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(TestQueryClient.class);
    }

    @Test
    void testGetOutboundServiceOk() {

        OutboundService service = provider.getOutboundServiceInstance("sample.TestOutboundService");

        assertThat(service).isNotNull();
        assertThat(service).isInstanceOf(TestOutboundService.class);
    }
}
