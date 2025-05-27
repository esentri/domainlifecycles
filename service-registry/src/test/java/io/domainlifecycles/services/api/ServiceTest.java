package io.domainlifecycles.services.api;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryHandler;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.services.Services;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sample.TestApplicationService;
import sample.TestDomainService;
import sample.TestOutboundService;
import sample.TestQueryHandler;
import sample.TestRepository;
import sample.TestServiceKind;

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
        services.registerServiceKindInstance(new TestQueryHandler());
        services.registerServiceKindInstance(new TestOutboundService());
        services.registerServiceKindInstance(new TestServiceKind());

        return services;
    }

    @Test
    void testGetApplicationServiceInstanceOk() {

        ApplicationService service = provider.getServiceKindInstance("sample.TestApplicationService");

        assertThat(service).isNotNull();
        assertThat(service).isInstanceOf(TestApplicationService.class);
    }

    @Test
    void testGetDomainServiceInstanceOk() {

        DomainService service = provider.getServiceKindInstance("sample.TestDomainService");

        assertThat(service).isNotNull();
        assertThat(service).isInstanceOf(TestDomainService.class);
    }

    @Test
    void testGetRepositoryInstanceOk() {

        Repository<?, ?> repository = provider.getServiceKindInstance("sample.TestRepository");

        assertThat(repository).isNotNull();
        assertThat(repository).isInstanceOf(TestRepository.class);
    }

    @Test
    void testGetQueryHandlerOk() {

        QueryHandler<?> handler = provider.getServiceKindInstance("sample.TestQueryHandler");

        assertThat(handler).isNotNull();
        assertThat(handler).isInstanceOf(TestQueryHandler.class);
    }

    @Test
    void testGetOutboundServiceOk() {

        OutboundService service = provider.getServiceKindInstance("sample.TestOutboundService");

        assertThat(service).isNotNull();
        assertThat(service).isInstanceOf(TestOutboundService.class);
    }

    @Test
    void testGetServiceKindOk() {

        ServiceKind service = provider.getServiceKindInstance("sample.TestServiceKind");

        assertThat(service).isNotNull();
        assertThat(service).isInstanceOf(ServiceKind.class);
    }
}
