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
 *  Copyright 2019-2026 the original author or authors.
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
import io.domainlifecycles.mirror.api.NonDomainTypeMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents the model of a class that does not belong to any recognized {@link DomainType},
 * implementing the functionality described by the {@link NonDomainTypeMirror} interface.
 *
 * @author Mario Herb
 */
public class NonDomainTypeModel extends DomainTypeModel implements NonDomainTypeMirror {

    private static final Set<DomainType> SERVICE_KIND_DOMAIN_TYPES = EnumSet.of(
        DomainType.SERVICE_KIND,
        DomainType.REPOSITORY,
        DomainType.DOMAIN_SERVICE,
        DomainType.OUTBOUND_SERVICE,
        DomainType.FACTORY,
        DomainType.QUERY_HANDLER,
        DomainType.APPLICATION_SERVICE
    );

    /**
     * Constructs a new instance of the NonDomainTypeModel.
     *
     * @param typeName the fully qualified name of the type represented by this model.
     * @param isAbstract a boolean indicating whether the represented type is abstract.
     * @param allFields a list of {@code FieldMirror} instances representing all fields in the type.
     * @param methods a list of {@code MethodMirror} instances representing all methods in the type.
     * @param inheritanceHierarchyTypeNames a list of fully qualified type names representing the inheritance
     *                                       hierarchy of the represented type.
     * @param allInterfaceTypeNames a list of fully qualified type names representing all interfaces implemented
     *                               by the represented type.
     */
    public NonDomainTypeModel(String typeName,
                              boolean isAbstract,
                              List<FieldMirror> allFields,
                              List<MethodMirror> methods,
                              List<String> inheritanceHierarchyTypeNames,
                              List<String> allInterfaceTypeNames) {

        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getDomainType() {
        return DomainType.NON_DOMAIN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceKindMirror> getReferencedServiceKinds() {
        return resolveReferencedTypes(SERVICE_KIND_DOMAIN_TYPES::contains)
            .stream()
            .map(dtm -> (ServiceKindMirror) dtm)
            .collect(Collectors.toList());
    }
}
