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

package nitrox.dlc.mirror.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nitrox.dlc.mirror.api.AggregateRootMirror;
import nitrox.dlc.mirror.api.ApplicationServiceMirror;
import nitrox.dlc.mirror.api.BoundedContextMirror;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.DomainCommandMirror;
import nitrox.dlc.mirror.api.DomainEventMirror;
import nitrox.dlc.mirror.api.DomainServiceMirror;
import nitrox.dlc.mirror.api.DomainType;
import nitrox.dlc.mirror.api.OutboundServiceMirror;
import nitrox.dlc.mirror.api.QueryClientMirror;
import nitrox.dlc.mirror.api.ReadModelMirror;
import nitrox.dlc.mirror.api.RepositoryMirror;

import java.util.List;
import java.util.Objects;

/**
 * Model implementation of a {@link BoundedContextMirror}.
 *
 * @author Mario Herb
 */
public class BoundedContextModel implements BoundedContextMirror {

    private final String packageName;

    @JsonCreator
    public BoundedContextModel(@JsonProperty("packageName") String packageName) {
        this.packageName = Objects.requireNonNull(packageName);

    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<AggregateRootMirror> getAggregateRoots(){
        return Domain
            .getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName)
                && DomainType.AGGREGATE_ROOT.equals(dt.getDomainType()))
            .map(dt -> (AggregateRootMirror) dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<DomainServiceMirror> getDomainServices(){
        return Domain
            .getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName)
                && DomainType.DOMAIN_SERVICE.equals(dt.getDomainType()))
            .map(dt -> (DomainServiceMirror) dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<RepositoryMirror> getRepositories(){
        return Domain
            .getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName)
                && DomainType.REPOSITORY.equals(dt.getDomainType()))
            .map(dt -> (RepositoryMirror) dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<ReadModelMirror> getReadModels(){
        return Domain
            .getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName)
                && DomainType.READ_MODEL.equals(dt.getDomainType()))
            .map(dt -> (ReadModelMirror) dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<DomainCommandMirror> getDomainCommands(){
        return Domain
            .getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName)
                && DomainType.DOMAIN_COMMAND.equals(dt.getDomainType()))
            .map(dt -> (DomainCommandMirror) dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<DomainEventMirror> getDomainEvents(){
        return Domain
            .getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName)
                && DomainType.DOMAIN_EVENT.equals(dt.getDomainType()))
            .map(dt -> (DomainEventMirror) dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<ApplicationServiceMirror> getApplicationServices(){
        return Domain
            .getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName)
                && DomainType.APPLICATION_SERVICE.equals(dt.getDomainType()))
            .map(dt -> (ApplicationServiceMirror) dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<QueryClientMirror> getQueryClients(){
        return Domain
            .getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName)
                && DomainType.QUERY_CLIENT.equals(dt.getDomainType()))
            .map(dt -> (QueryClientMirror) dt)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public List<OutboundServiceMirror> getOutboundServices(){
        return Domain
            .getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dt -> dt.getTypeName().startsWith(packageName)
                && DomainType.OUTBOUND_SERVICE.equals(dt.getDomainType()))
            .map(dt -> (OutboundServiceMirror) dt)
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
}