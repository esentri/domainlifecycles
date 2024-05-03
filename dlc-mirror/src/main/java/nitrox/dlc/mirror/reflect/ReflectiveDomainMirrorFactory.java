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

package nitrox.dlc.mirror.reflect;

import nitrox.dlc.domain.types.AggregateRoot;
import nitrox.dlc.domain.types.ApplicationService;
import nitrox.dlc.domain.types.DomainCommand;
import nitrox.dlc.domain.types.DomainEvent;
import nitrox.dlc.domain.types.DomainService;
import nitrox.dlc.domain.types.Entity;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.domain.types.OutboundService;
import nitrox.dlc.domain.types.QueryClient;
import nitrox.dlc.domain.types.ReadModel;
import nitrox.dlc.domain.types.Repository;
import nitrox.dlc.domain.types.ValueObject;
import nitrox.dlc.mirror.api.AggregateRootMirror;
import nitrox.dlc.mirror.api.ApplicationServiceMirror;
import nitrox.dlc.mirror.api.BoundedContextMirror;
import nitrox.dlc.mirror.api.DomainCommandMirror;
import nitrox.dlc.mirror.api.DomainEventMirror;
import nitrox.dlc.mirror.api.DomainMirrorFactory;
import nitrox.dlc.mirror.api.DomainServiceMirror;
import nitrox.dlc.mirror.api.DomainTypeMirror;
import nitrox.dlc.mirror.api.EntityMirror;
import nitrox.dlc.mirror.api.EnumMirror;
import nitrox.dlc.mirror.api.IdentityMirror;
import nitrox.dlc.mirror.api.InitializedDomain;
import nitrox.dlc.mirror.api.OutboundServiceMirror;
import nitrox.dlc.mirror.api.ReadModelMirror;
import nitrox.dlc.mirror.api.QueryClientMirror;
import nitrox.dlc.mirror.api.RepositoryMirror;
import nitrox.dlc.mirror.api.ValueObjectMirror;
import nitrox.dlc.mirror.model.BoundedContextModel;
import nitrox.dlc.mirror.resolver.GenericTypeResolver;
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
     */
    public ReflectiveDomainMirrorFactory(String ...boundedContextPackages) {
        this.boundedContextPackages = boundedContextPackages;
        this.domainTypesScanner = new DomainTypesScanner();
    }

    /**
     * Initializes the domain with the scanned classes.
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

        builtTypeMirrors
            .values()
            .forEach(m -> log.debug("Created Mirror:"+ m));

        return new InitializedDomain(builtTypeMirrors, buildBoundedContextMirrors());
    }

    private List<BoundedContextMirror> buildBoundedContextMirrors(){
        return Arrays.stream(boundedContextPackages)
                .map(BoundedContextModel::new
                )
                .collect(Collectors.toList());
    }

    private List<ValueObjectMirror> buildValueObjectMirrors(List<Class<? extends ValueObject>> valueObjectClassList){
        return valueObjectClassList.stream()
            .map(c -> new ValueObjectMirrorBuilder(c).build()
            )
            .toList();
    }

    private List<EnumMirror> buildEnumMirrors(List<Class<? extends Enum<?>>> enumClassList){
        return enumClassList
            .stream()
            .map(ei -> new EnumMirrorBuilder(ei).build())
            .toList();
    }

    private List<IdentityMirror> buildIdentityMirrors(List<Class<? extends Identity<?>>> identityClassList){
        return identityClassList
            .stream()
            .map(ci -> new IdentityMirrorBuilder(ci).build())
            .toList();

    }

    private List<EntityMirror> buildEntityMirrors(List<Class<? extends Entity<?>>> entityClassList){
        return entityClassList
            .stream()
            .map(em -> new EntityMirrorBuilder(em).build())
            .toList();
    }

    private List<AggregateRootMirror> buildAggregateRootMirrors(List<Class<? extends AggregateRoot<?>>> aggregateRootClassList){
        return aggregateRootClassList
            .stream()
            .map(am -> new AggregateRootMirrorBuilder(am).build()
            )
            .toList();
    }

    private List<DomainServiceMirror> buildDomainServiceMirrors(List<Class<? extends DomainService>> domainServiceClassList){
        return domainServiceClassList
            .stream()
            .map(ds -> new DomainServiceMirrorBuilder(ds).build()
            )
            .toList();
    }

    private List<ApplicationServiceMirror> buildApplicationServiceMirrors(List<Class<? extends ApplicationService>> applicationServiceClassList){
        return applicationServiceClassList
            .stream()
            .map(ds -> new ApplicationServiceMirrorBuilder(ds).build()
            )
            .toList();
    }

    private List<DomainEventMirror> buildDomainEventMirrors(List<Class<? extends DomainEvent>> domainEventClassList){
        return domainEventClassList
            .stream()
            .map(de -> new DomainEventMirrorBuilder(de).build()
            )
            .toList();
    }

    private List<DomainCommandMirror> buildDomainCommandMirrors(List<Class<? extends DomainCommand>> domainCommandClassList){
        return domainCommandClassList
            .stream()
            .map(c -> new DomainCommandMirrorBuilder(c).build()
            )
            .toList();
    }

    private List<ReadModelMirror> buildReadModelMirrors(List<Class<? extends ReadModel>> readModelClassList){
        return readModelClassList
            .stream()
            .map(rm -> new ReadModelMirrorBuilder(rm).build()
            )
            .toList();
    }

    private List<RepositoryMirror> buildRepositoryMirrors(List<Class<? extends Repository<?, ?>>> repositoryClassList){
        return repositoryClassList
            .stream()
            .map(r -> new RepositoryMirrorBuilder(r).build()
            )
            .toList();
    }

    private List<QueryClientMirror> buildQueryClientMirrors(List<Class<? extends QueryClient<?>>> queryClientClassList){
        return queryClientClassList
            .stream()
            .map(r -> new QueryClientMirrorBuilder(r).build()
            )
            .toList();
    }

    private List<OutboundServiceMirror> buildOutboundServiceMirrors(List<Class<? extends OutboundService>> outboundServiceClassList){
        return outboundServiceClassList
            .stream()
            .map(o -> new OutboundServiceMirrorBuilder(o).build()
            )
            .toList();
    }

}
