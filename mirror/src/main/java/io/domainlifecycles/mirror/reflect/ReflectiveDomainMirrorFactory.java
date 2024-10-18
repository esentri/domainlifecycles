/*
 *
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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainMirrorFactory;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EnumMirror;
import io.domainlifecycles.mirror.api.InitializedDomain;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.domainlifecycles.mirror.model.BoundedContextModel;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainCommand;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.BoundedContextMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.QueryClientMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Uses Java reflection to initialize a mirror of the domain by analyzing the given bounded context packages.
 *
 * @author Mario Herb
 */
public class ReflectiveDomainMirrorFactory implements DomainMirrorFactory {
    private static final Logger log = LoggerFactory.getLogger(ReflectiveDomainMirrorFactory.class);
    private final DomainTypesScanner domainTypesScanner;
    private final String[] boundedContextPackages;

    /**
     * Initialize the factory with the boundedContextPackages to be scanned.
     *
     * @param boundedContextPackages the bounded context packages
     */
    public ReflectiveDomainMirrorFactory(String... boundedContextPackages) {
        this.boundedContextPackages = boundedContextPackages;
        this.domainTypesScanner = new DomainTypesScanner();
    }

    /**
     * Initializes the domain with the scanned classes.
     *
     * @return InitializedDomain - a container for all mirrors that are available in the analyzed bounded contexts.
     */
    @Override
    public InitializedDomain initializeDomain(GenericTypeResolver typeResolver) {
        domainTypesScanner.scan(boundedContextPackages);
        var builtTypeMirrors = new HashMap<String, DomainTypeMirror>();

        buildIdentityMirrors(domainTypesScanner.getScannedIdentities())
            .forEach(id -> builtTypeMirrors.put(id.getTypeName(), id));

        buildEnumMirrors(domainTypesScanner.getScannedEnums())
            .forEach(e -> builtTypeMirrors.put(e.getTypeName(), e));

        buildValueObjectMirrors(domainTypesScanner.getScannedValueObjects())
            .forEach(vo -> builtTypeMirrors.put(vo.getTypeName(), vo));

        buildEntityMirrors(domainTypesScanner.getScannedEntities())
            .forEach(e -> builtTypeMirrors.put(e.getTypeName(), e));

        buildAggregateRootMirrors(domainTypesScanner.getScannedAggregateRoots())
            .forEach(a -> builtTypeMirrors.put(a.getTypeName(), a));

        buildDomainCommandMirrors(domainTypesScanner.getScannedDomainCommands())
            .forEach(c -> builtTypeMirrors.put(c.getTypeName(), c));

        buildDomainEventMirrors(domainTypesScanner.getScannedDomainEvents())
            .forEach(d -> builtTypeMirrors.put(d.getTypeName(), d));

        buildRepositoryMirrors(domainTypesScanner.getScannedRepositories())
            .forEach(r -> builtTypeMirrors.put(r.getTypeName(), r));

        buildDomainServiceMirrors(domainTypesScanner.getScannedDomainServices())
            .forEach(ds -> builtTypeMirrors.put(ds.getTypeName(), ds));

        buildApplicationServiceMirrors(domainTypesScanner.getScannedApplicationServices())
            .forEach(as -> builtTypeMirrors.put(as.getTypeName(), as));

        buildReadModelMirrors(domainTypesScanner.getScannedReadModels())
            .forEach(rm -> builtTypeMirrors.put(rm.getTypeName(), rm));

        buildQueryClientMirrors(domainTypesScanner.getScannedQueryClients())
            .forEach(qc -> builtTypeMirrors.put(qc.getTypeName(), qc));

        buildOutboundServiceMirrors(domainTypesScanner.getScannedOutboundServices())
            .forEach(om -> builtTypeMirrors.put(om.getTypeName(), om));

        buildServiceKindMirrors(domainTypesScanner.getScannedServiceKinds())
            .forEach(sk -> builtTypeMirrors.put(sk.getTypeName(), sk));

        builtTypeMirrors
            .values()
            .forEach(m -> log.debug("Created Mirror:" + m));

        return new InitializedDomain(builtTypeMirrors, buildBoundedContextMirrors());
    }

    private List<BoundedContextMirror> buildBoundedContextMirrors() {
        return Arrays.stream(boundedContextPackages)
            .map(BoundedContextModel::new
            )
            .collect(Collectors.toList());
    }

    private List<ValueObjectMirror> buildValueObjectMirrors(List<Class<? extends ValueObject>> valueObjectClassList) {
        return valueObjectClassList.stream()
            .map(c -> new ValueObjectMirrorBuilder(c).build()
            )
            .toList();
    }

    private List<EnumMirror> buildEnumMirrors(List<Class<? extends Enum<?>>> enumClassList) {
        return enumClassList
            .stream()
            .map(ei -> new EnumMirrorBuilder(ei).build())
            .toList();
    }

    private List<IdentityMirror> buildIdentityMirrors(List<Class<? extends Identity<?>>> identityClassList) {
        return identityClassList
            .stream()
            .map(ci -> new IdentityMirrorBuilder(ci).build())
            .toList();

    }

    private List<EntityMirror> buildEntityMirrors(List<Class<? extends Entity<?>>> entityClassList) {
        return entityClassList
            .stream()
            .map(em -> new EntityMirrorBuilder(em).build())
            .toList();
    }

    private List<AggregateRootMirror> buildAggregateRootMirrors(List<Class<? extends AggregateRoot<?>>> aggregateRootClassList) {
        return aggregateRootClassList
            .stream()
            .map(am -> new AggregateRootMirrorBuilder(am).build()
            )
            .toList();
    }

    private List<DomainServiceMirror> buildDomainServiceMirrors(List<Class<? extends DomainService>> domainServiceClassList) {
        return domainServiceClassList
            .stream()
            .map(ds -> new DomainServiceMirrorBuilder(ds).build()
            )
            .toList();
    }

    private List<ApplicationServiceMirror> buildApplicationServiceMirrors(List<Class<? extends ApplicationService>> applicationServiceClassList) {
        return applicationServiceClassList
            .stream()
            .map(ds -> new ApplicationServiceMirrorBuilder(ds).build()
            )
            .toList();
    }

    private List<DomainEventMirror> buildDomainEventMirrors(List<Class<? extends DomainEvent>> domainEventClassList) {
        return domainEventClassList
            .stream()
            .map(de -> new DomainEventMirrorBuilder(de).build()
            )
            .toList();
    }

    private List<DomainCommandMirror> buildDomainCommandMirrors(List<Class<? extends DomainCommand>> domainCommandClassList) {
        return domainCommandClassList
            .stream()
            .map(c -> new DomainCommandMirrorBuilder(c).build()
            )
            .toList();
    }

    private List<ReadModelMirror> buildReadModelMirrors(List<Class<? extends ReadModel>> readModelClassList) {
        return readModelClassList
            .stream()
            .map(rm -> new ReadModelMirrorBuilder(rm).build()
            )
            .toList();
    }

    private List<RepositoryMirror> buildRepositoryMirrors(List<Class<? extends Repository<?, ?>>> repositoryClassList) {
        return repositoryClassList
            .stream()
            .map(r -> new RepositoryMirrorBuilder(r).build()
            )
            .toList();
    }

    private List<QueryClientMirror> buildQueryClientMirrors(List<Class<? extends QueryClient<?>>> queryClientClassList) {
        return queryClientClassList
            .stream()
            .map(r -> new QueryClientMirrorBuilder(r).build()
            )
            .toList();
    }

    private List<OutboundServiceMirror> buildOutboundServiceMirrors(List<Class<? extends OutboundService>> outboundServiceClassList) {
        return outboundServiceClassList
            .stream()
            .map(o -> new OutboundServiceMirrorBuilder(o).build()
            )
            .toList();
    }

    private List<ServiceKindMirror> buildServiceKindMirrors(List<Class<? extends ServiceKind>> serviceKindClassList) {
        return serviceKindClassList
            .stream()
            .map(s -> new ServiceKindMirrorBuilder(s).build())
            .toList();
    }

}
