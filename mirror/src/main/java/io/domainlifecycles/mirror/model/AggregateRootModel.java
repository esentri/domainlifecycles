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
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;

import java.util.List;
import java.util.Optional;

/**
 * Model implementation of an {@link AggregateRootMirror}.
 *
 * @author Mario Herb
 */
public class AggregateRootModel extends EntityModel implements AggregateRootMirror {

    /**
     * Constructs an instance of {@code AggregateRootModel}.
     *
     * @param typeName the name of the aggregate root type being mirrored
     * @param isAbstract indicates whether the type is abstract
     * @param allFields a list of all fields in the mirrored aggregate root type
     * @param methods a list of methods in the mirrored aggregate root type
     * @param identityField the optional field representing the unique identity of the aggregate root
     * @param concurrencyVersionField the optional field used for concurrency versioning in the aggregate root
     * @param inheritanceHierarchyTypeNames a list of type names in the inheritance hierarchy of the mirrored aggregate root
     * @param allInterfaceTypeNames a list of all interface type names implemented by the mirrored aggregate root
     */
    public AggregateRootModel(
        String typeName,
        boolean isAbstract,
        List<FieldMirror> allFields,
        List<MethodMirror> methods,
        Optional<FieldMirror> identityField,
        Optional<FieldMirror> concurrencyVersionField,
        List<String> inheritanceHierarchyTypeNames,
        List<String> allInterfaceTypeNames
    ) {
        super(
            typeName,
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
     * @return domain type
     */
    @Override
    public DomainType getDomainType() {
        return DomainType.AGGREGATE_ROOT;
    }

    /**
     * {@inheritDoc}
     * @return string representation of aggregate root model
     */
    @Override
    public String toString() {
        return "AggregateRootModel{} " + super.toString();
    }
}
