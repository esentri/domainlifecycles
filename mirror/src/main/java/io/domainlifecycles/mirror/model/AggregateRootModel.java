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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.DomainType;

import java.util.List;
import java.util.Optional;

/**
 * Model implementation of an {@link AggregateRootMirror}.
 *
 * @author Mario Herb
 */
public class AggregateRootModel extends EntityModel implements AggregateRootMirror {


    @JsonCreator
    public AggregateRootModel(@JsonProperty("typeName") String typeName,
                              @JsonProperty("abstract") boolean isAbstract,
                              @JsonProperty("allFields") List<FieldMirror> allFields,
                              @JsonProperty("methods") List<MethodMirror> methods,
                              @JsonProperty("identityField") Optional<FieldMirror> identityField,
                              @JsonProperty("concurrencyVersionField") Optional<FieldMirror> concurrencyVersionField,
                              @JsonProperty("inheritanceHierarchyTypeNames") List<String> inheritanceHierarchyTypeNames,
                              @JsonProperty("allInterfaceTypeNames") List<String> allInterfaceTypeNames
    ) {
        super(typeName,
            isAbstract,
            allFields,
            methods,
            identityField,
            concurrencyVersionField,
            inheritanceHierarchyTypeNames,
            allInterfaceTypeNames
        );
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public DomainType getDomainType() {
        return DomainType.AGGREGATE_ROOT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AggregateRootModel{} " + super.toString();
    }
}
