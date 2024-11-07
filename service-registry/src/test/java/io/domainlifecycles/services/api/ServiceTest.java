/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.services.api;

import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
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
import sample.TestQueryClient;
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
        services.registerServiceKindInstance(new TestQueryClient());
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
    void testGetQueryClientOk() {

        QueryClient<?> client = provider.getServiceKindInstance("sample.TestQueryClient");

        assertThat(client).isNotNull();
        assertThat(client).isInstanceOf(TestQueryClient.class);
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
