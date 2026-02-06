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
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;

import java.util.List;
import java.util.Objects;

/**
 * Model implementation of a {@link BoundedContextMirror}.
 *
 * @author Mario Herb
 */
public class BoundedContextModel implements BoundedContextMirror, ProvidedDomain {

    private final String packageName;
    DomainMirror domainMirror;
    private boolean domainMirrorSet = false;

    /**
     * Constructs an instance of the BoundedContextModel class.
     *
     * @param packageName the package name of the bounded context, cannot be null.
     */
    public BoundedContextModel(String packageName) {
        this.packageName = Objects.requireNonNull(packageName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AggregateRootMirror> getAggregateRoots() {
        return domainMirror.getAllAggregateRootMirrors()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainServiceMirror> getDomainServices() {
        return domainMirror.getAllDomainServiceMirrors()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RepositoryMirror> getRepositories() {
        return domainMirror.getAllRepositoryMirrors()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReadModelMirror> getReadModels() {
        return domainMirror
            .getAllReadModelMirrors()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainCommandMirror> getDomainCommands() {
        return domainMirror
            .getAllDomainCommandMirrors()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DomainEventMirror> getDomainEvents() {
        return domainMirror
            .getAllDomainEventMirrors()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ApplicationServiceMirror> getApplicationServices() {
        return domainMirror
            .getAllApplicationServiceMirrors()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QueryHandlerMirror> getQueryHandlers() {
        return domainMirror
            .getAllQueryHandlerMirrors()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OutboundServiceMirror> getOutboundServices() {
        return domainMirror
            .getAllOutboundServiceMirrors()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceKindMirror> getServiceKinds() {
        return domainMirror
            .getAllServiceKindMirrors()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPackageName() {
        return packageName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BoundedContextModel{" +
            "packageName='" + packageName + '\'' +
            '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundedContextModel that = (BoundedContextModel) o;
        return packageName.equals(that.packageName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(packageName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDomainMirror(DomainMirror domainMirror) {
        if(!domainMirrorSet) {
            this.domainMirror = domainMirror;
            this.domainMirrorSet = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean domainMirrorSet() {
        return this.domainMirrorSet;
    }
}
