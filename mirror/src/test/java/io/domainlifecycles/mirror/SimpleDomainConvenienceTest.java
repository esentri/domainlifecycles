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

package io.domainlifecycles.mirror;

import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryClientMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.mirror.AggregateRootImpl;
import tests.mirror.AggregateRootInterface;
import tests.mirror.ApplicationServiceImpl;
import tests.mirror.ApplicationServiceInterface;
import tests.mirror.DomainEventImpl;
import tests.mirror.DomainEventInterface;
import tests.mirror.DomainServiceImpl;
import tests.mirror.DomainServiceInterface;
import tests.mirror.EntityImpl;
import tests.mirror.EntityInterface;
import tests.mirror.IdentityImpl;
import tests.mirror.IdentityInterface;
import tests.mirror.OutboundServiceImpl;
import tests.mirror.OutboundServiceInterface;
import tests.mirror.QueryClientImpl;
import tests.mirror.RepositoryImpl;
import tests.mirror.RepositoryInterface;
import tests.mirror.ServiceKindImpl;
import tests.mirror.ServiceKindInterface;
import tests.mirror.ValueObjectImpl;
import tests.mirror.ValueObjectInterface;
import static org.assertj.core.api.Assertions.assertThat;

public class SimpleDomainConvenienceTest {

    @BeforeAll
    public static void init() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        Domain.initialize(factory);
    }

    @Test
    void testAggregateRootMirrorByInstance() {

        AggregateRootInterface aggregateRoot = new AggregateRootImpl();

        AggregateRootMirror aggregateRootMirror = Domain.aggregateRootMirrorFor(aggregateRoot);

        assertThat(aggregateRootMirror).isNotNull();
    }

    @Test
    void testAggregateRootMirrorByTypeName() {

        AggregateRootMirror aggregateRootMirror = Domain.aggregateRootMirrorFor("tests.mirror.AggregateRootImpl");

        assertThat(aggregateRootMirror).isNotNull();
    }

    @Test
    void testEntityMirrorByInstance() {

        EntityInterface entity = new EntityImpl(new IdentityImpl(1L));

        EntityMirror entityMirror = Domain.entityMirrorFor(entity);

        assertThat(entityMirror).isNotNull();
    }

    @Test
    void testEntityMirrorByTypeName() {

        EntityMirror entityMirror = Domain.entityMirrorFor("tests.mirror.EntityImpl");

        assertThat(entityMirror).isNotNull();
    }

    @Test
    void testEntityMirrorByIdentityTypeName() {

        EntityMirror entityMirror = Domain.entityMirrorForIdentityTypeName("tests.mirror.IdentityInterface");

        assertThat(entityMirror).isNotNull();
    }

    @Test
    void testDomainEventMirrorByInstance() {

        DomainEventInterface domainEvent = new DomainEventImpl();

        DomainEventMirror domainEventMirror = Domain.domainEventMirrorFor(domainEvent);

        assertThat(domainEventMirror).isNotNull();
    }

    @Test
    void testDomainEventMirrorByTypeName() {

        DomainEventMirror domainEventMirror = Domain.domainEventMirrorFor("tests.mirror.DomainEventImpl");

        assertThat(domainEventMirror).isNotNull();
    }

    @Test
    void testDomainServiceMirrorByInstance() {

        DomainServiceInterface domainService = new DomainServiceImpl();

        DomainServiceMirror domainServiceMirror = Domain.domainServiceMirrorFor(domainService);

        assertThat(domainServiceMirror).isNotNull();
    }

    @Test
    void testDomainServiceMirrorByTypeName() {

        DomainServiceMirror domainServiceMirror = Domain.domainServiceMirrorFor("tests.mirror.DomainServiceImpl");

        assertThat(domainServiceMirror).isNotNull();
    }

    @Test
    void testServiceKindMirrorByInstance() {

        ServiceKindInterface serviceKind = new ServiceKindImpl();

        ServiceKindMirror serviceKindMirror = Domain.serviceKindMirrorFor(serviceKind);

        assertThat(serviceKindMirror).isNotNull();
    }

    @Test
    void testServiceKindMirrorByTypeName() {

        ServiceKindMirror serviceKindMirror = Domain.serviceKindMirrorFor("tests.mirror.ServiceKindImpl");

        assertThat(serviceKindMirror).isNotNull();
    }

    @Test
    void testApplicationServiceMirrorByInstance() {

        ApplicationServiceInterface applicationService = new ApplicationServiceImpl();

        ApplicationServiceMirror applicationServiceMirror = Domain.applicationServiceMirrorFor(applicationService);

        assertThat(applicationServiceMirror).isNotNull();
    }

    @Test
    void testApplicationServiceMirrorByTypeName() {

        ApplicationServiceMirror applicationServiceMirror = Domain.applicationServiceMirrorFor("tests.mirror.ApplicationServiceImpl");

        assertThat(applicationServiceMirror).isNotNull();
    }

    @Test
    void testRepositoryMirrorByAggregateRootMirror() {

        AggregateRootMirror aggregateRootMirror = Domain.aggregateRootMirrorFor("tests.mirror.AggregateRootInterface");

        RepositoryMirror repositoryMirror = Domain.repositoryMirrorFor(aggregateRootMirror);

        assertThat(repositoryMirror).isNotNull();
    }

    @Test
    void testRepositoryMirrorByInstance() {

        RepositoryInterface repository = new RepositoryImpl();

        RepositoryMirror repositoryMirror = Domain.repositoryMirrorFor(repository);

        assertThat(repositoryMirror).isNotNull();
    }

    @Test
    void testRepositoryMirrorByTypeName() {

        RepositoryMirror repositoryMirror = Domain.repositoryMirrorFor("tests.mirror.RepositoryImpl");

        assertThat(repositoryMirror).isNotNull();
    }

    @Test
    void testIdentityMirrorByInstance() {

        IdentityInterface identity = new IdentityImpl(1L);

        IdentityMirror identityMirror = Domain.identityMirrorFor(identity);

        assertThat(identityMirror).isNotNull();
    }

    @Test
    void testIdentityMirrorByTypeName() {

        IdentityMirror identityMirror = Domain.identityMirrorFor("tests.mirror.IdentityImpl");

        assertThat(identityMirror).isNotNull();
    }

    @Test
    void testValueObjectMirrorByInstance() {

        ValueObjectInterface valueObject = new ValueObjectImpl();

        ValueObjectMirror valueObjectMirror = Domain.valueObjectMirrorFor(valueObject);

        assertThat(valueObjectMirror).isNotNull();
    }

    @Test
    void testValueObjectMirrorByTypeName() {

        ValueObjectMirror valueObjectMirror = Domain.valueObjectMirrorFor("tests.mirror.ValueObjectImpl");

        assertThat(valueObjectMirror).isNotNull();
    }

    @Test
    void testOutboundServiceMirrorByInstance() {

        OutboundServiceInterface outboundService = new OutboundServiceImpl();

        OutboundServiceMirror outboundServiceMirror = Domain.outboundServiceMirrorFor(outboundService);

        assertThat(outboundServiceMirror).isNotNull();
    }

    @Test
    void testOutboundServiceMirrorByTypeName() {

        OutboundServiceMirror outboundServiceMirror = Domain.outboundServiceMirrorFor("tests.mirror.OutboundServiceImpl");

        assertThat(outboundServiceMirror).isNotNull();
    }

    @Test
    void testQueryClientMirrorByInstance() {

        QueryClient<?> queryClient = new QueryClientImpl();

        QueryClientMirror queryClientMirror = Domain.queryClientMirrorFor(queryClient);

        assertThat(queryClientMirror).isNotNull();
    }

    @Test
    void testQueryClientMirrorByTypeName() {

        QueryClientMirror queryClientMirror = Domain.queryClientMirrorFor("tests.mirror.QueryClientImpl");

        assertThat(queryClientMirror).isNotNull();
    }
}
