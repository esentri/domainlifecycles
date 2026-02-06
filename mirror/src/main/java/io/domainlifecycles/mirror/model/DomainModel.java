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

package io.domainlifecycles.mirror.model;

import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.BoundedContextMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EnumMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.domainlifecycles.mirror.exception.MirrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class is a container for all mirrors in a mirrored Domain.
 *
 * @author Mario Herb
 */
public class DomainModel implements DomainMirror {

    private final Map<String, ? extends DomainTypeMirror> allTypeMirrors;
    private final List<BoundedContextMirror> boundedContextMirrors;

    private static final Pattern packagePattern = Pattern.compile("^[a-z]+(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");

    /**
     * Constructs a new {@code DomainModel} which serves as a container for type mirrors
     * and bounded context mirrors within a domain.
     *
     * @param allTypeMirrors a map containing the fully qualified name of domain types
     *                       as keys and their corresponding {@code DomainTypeMirror} values.
     * @param boundedContextPackages an array of package names to define bounded contexts
     *                                within the domain.
     * @throws MirrorException if any of the provided package names in
     *                         {@code boundedContextPackages} are invalid.
     */
    public DomainModel(Map<String, ? extends DomainTypeMirror> allTypeMirrors, String... boundedContextPackages) {
        this.allTypeMirrors = new HashMap<>(allTypeMirrors);
        this.boundedContextMirrors = buildBoundedContextMirrors(boundedContextPackages);
        initDomainModelReferences();
    }

    private void initDomainModelReferences(){
        allTypeMirrors
            .values()
            .stream()
            .map(m -> (DomainTypeModel)m)
            .forEach(m -> {
                m.setDomainMirror(this);
                if(m instanceof EntityModel e) {
                    e.getIdentityField()
                        .map(f -> (FieldModel) f)
                        .ifPresent(f -> f.setDomainMirror(this));
                }
                m.getAllFields()
                    .stream()
                    .map(f -> (FieldModel) f)
                    .forEach(f -> f.setDomainMirror(this));
                m.getMethods()
                    .stream()
                    .map(meth -> (MethodModel) meth)
                    .forEach(meth -> meth.setDomainMirror(this));

            });
        boundedContextMirrors.forEach(m -> {
            var bc = (BoundedContextModel)m;
            bc.setDomainMirror(this);
        });
    }

    /**
     * Constructs a new {@code DomainModel} which serves as a container for type mirrors
     * and bounded context mirrors within a domain.
     *
     * @param allTypeMirrors a map containing the fully qualified name of domain types
     *                       as keys and their corresponding {@code DomainTypeMirror} values.
     * @param boundedContextMirrors an array of {@code BoundedContextMirror} instances
     *                               representing bounded contexts within the domain.
     */
    public DomainModel(Map<String, ? extends DomainTypeMirror> allTypeMirrors,
                       List<BoundedContextMirror> boundedContextMirrors) {
        this.allTypeMirrors = new HashMap<>(allTypeMirrors);
        this.boundedContextMirrors = boundedContextMirrors;
        initDomainModelReferences();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DomainTypeMirror> Optional<T> getDomainTypeMirror(String typeName) {
        return  Optional.ofNullable((T)allTypeMirrors.get(typeName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainTypeMirror> getAllDomainTypeMirrors() {
        return new ArrayList<>(allTypeMirrors.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BoundedContextMirror> getAllBoundedContextMirrors() {
        return this.boundedContextMirrors;
    }

    private List<BoundedContextMirror> buildBoundedContextMirrors(String ... boundedContextPackages) {
        if(boundedContextPackages != null && boundedContextPackages.length > 0) {
            validatePackages(boundedContextPackages);
            return Arrays.stream(boundedContextPackages)
                .map(BoundedContextModel::new
                )
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AggregateRootMirror> getAllAggregateRootMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.AGGREGATE_ROOT))
            .map(dt -> (AggregateRootMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EntityMirror> getAllEntityMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.ENTITY))
            .map(dt -> (EntityMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValueObjectMirror> getAllValueObjectMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.VALUE_OBJECT))
            .map(dt -> (ValueObjectMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EnumMirror> getAllEnumMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.ENUM))
            .map(dt -> (EnumMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValueMirror> getAllValueMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt ->
                dt.getDomainType().equals(DomainType.ENUM)
                    || dt.getDomainType().equals(DomainType.VALUE_OBJECT)
                    || dt.getDomainType().equals(DomainType.IDENTITY)
            )
            .map(dt -> (ValueMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainCommandMirror> getAllDomainCommandMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.DOMAIN_COMMAND))
            .map(dt -> (DomainCommandMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainEventMirror> getAllDomainEventMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.DOMAIN_EVENT))
            .map(dt -> (DomainEventMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationServiceMirror> getAllApplicationServiceMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.APPLICATION_SERVICE))
            .map(dt -> (ApplicationServiceMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainServiceMirror> getAllDomainServiceMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.DOMAIN_SERVICE))
            .map(dt -> (DomainServiceMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RepositoryMirror> getAllRepositoryMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.REPOSITORY))
            .map(dt -> (RepositoryMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReadModelMirror> getAllReadModelMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.READ_MODEL))
            .map(dt -> (ReadModelMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QueryHandlerMirror> getAllQueryHandlerMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.QUERY_HANDLER))
            .map(dt -> (QueryHandlerMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OutboundServiceMirror> getAllOutboundServiceMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.OUTBOUND_SERVICE))
            .map(dt -> (OutboundServiceMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IdentityMirror> getAllIdentityMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.IDENTITY))
            .map(dt -> (IdentityMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceKindMirror> getAllUnspecifiedServiceKindMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.SERVICE_KIND))
            .map(dt -> (ServiceKindMirror)dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceKindMirror> getAllServiceKindMirrors() {
        return getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt.getDomainType().equals(DomainType.SERVICE_KIND)
                || dt.getDomainType().equals(DomainType.APPLICATION_SERVICE)
                || dt.getDomainType().equals(DomainType.DOMAIN_SERVICE)
                || dt.getDomainType().equals(DomainType.REPOSITORY)
                || dt.getDomainType().equals(DomainType.QUERY_HANDLER)
                || dt.getDomainType().equals(DomainType.OUTBOUND_SERVICE)
            )
            .map(dt -> (ServiceKindMirror)dt)
            .toList();
    }

    private static void validatePackages(final String... packageNames) {
        for (String packageName : packageNames) {
            if(!packagePattern.matcher(packageName).matches()){
                throw MirrorException.fail("Invalid package name: " + packageName);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DomainModel that = (DomainModel) o;
        return Objects.equals(allTypeMirrors, that.allTypeMirrors) && Objects.equals(boundedContextMirrors, that.boundedContextMirrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allTypeMirrors, boundedContextMirrors);
    }
}