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

    private final String providedReadModelTypeName;
    private final List<String> queryHandlerInterfaceTypeNames;

    /**
     * Constructs an instance of QueryHandlerModel.
     *
     * @param typeName the fully qualified name of the type represented by this model.
     * @param isAbstract a boolean indicating whether the represented type is abstract.
     * @param allFields a list of {@code FieldMirror} instances representing all fields in the type.
     * @param methods a list of {@code MethodMirror} instances representing all methods in the type.
     * @param queryHandlerInterfaceTypeNames a list of fully qualified type names representing all query handler interfaces
     *                                        implemented by the represented type.
     * @param inheritanceHierarchyTypeNames a list of fully qualified type names representing the inheritance hierarchy
     *                                       of the represented type.
     * @param allInterfaceTypeNames a list of fully qualified type names representing all interfaces implemented
     *                               by the represented type.
     * @param providedReadModelTypeName the fully qualified type name of the read model provided by the query handler.
     */
    public QueryHandlerModel(
        String typeName,
        boolean isAbstract,
        List<FieldMirror> allFields,
        List<MethodMirror> methods,
        List<String> queryHandlerInterfaceTypeNames,
        List<String> inheritanceHierarchyTypeNames,
        List<String> allInterfaceTypeNames,
        String providedReadModelTypeName
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
        return Optional.ofNullable((ReadModelMirror) domainMirror.getDomainTypeMirror(providedReadModelTypeName).orElse(null));
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
