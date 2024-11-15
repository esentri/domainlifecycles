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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Model implementation of a {@link QueryHandlerMirror}.
 *
 * @author Mario Herb
 */
public class QueryHandlerModel extends ServiceKindModel implements QueryHandlerMirror {

    @JsonProperty
    private final String providedReadModelTypeName;
    @JsonProperty
    private final List<String> queryHandlerInterfaceTypeNames;

    @JsonCreator
    public QueryHandlerModel(
        @JsonProperty("typeName") String typeName,
        @JsonProperty("abstract") boolean isAbstract,
        @JsonProperty("allFields") List<FieldMirror> allFields,
        @JsonProperty("methods") List<MethodMirror> methods,
        @JsonProperty("queryHandlerInterfaceTypeNames") List<String> queryHandlerInterfaceTypeNames,
        @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
        @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames,
        @JsonProperty("providedReadModelTypeName") String providedReadModelTypeName
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
        this.providedReadModelTypeName = Objects.requireNonNull(providedReadModelTypeName);
        this.queryHandlerInterfaceTypeNames = Collections.unmodifiableList(queryHandlerInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ReadModelMirror> getProvidedReadModel() {
        return Optional.ofNullable((ReadModelMirror) Domain.typeMirror(providedReadModelTypeName).orElse(null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getQueryHandlerInterfaceTypeNames() {
        return queryHandlerInterfaceTypeNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getDomainType() {
        return DomainType.QUERY_HANDLER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "QueryHandlerModel{" +
            "providedReadModelTypeName='" + providedReadModelTypeName + '\'' +
            ", queryHandlerInterfaceTypeNames=" + queryHandlerInterfaceTypeNames +
            "} " + super.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QueryHandlerModel that = (QueryHandlerModel) o;
        return providedReadModelTypeName.equals(that.providedReadModelTypeName)
            && queryHandlerInterfaceTypeNames.equals(
            that.queryHandlerInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), providedReadModelTypeName, queryHandlerInterfaceTypeNames);
    }
}
