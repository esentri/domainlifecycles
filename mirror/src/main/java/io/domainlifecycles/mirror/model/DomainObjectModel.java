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

import io.domainlifecycles.mirror.api.DomainObjectMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.exception.MirrorException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Model implementation of a {@link DomainObjectMirror}.
 *
 * @author Mario Herb
 */
public abstract class DomainObjectModel extends DomainTypeModel implements DomainObjectMirror {

    /**
     * Constructs a new instance of the DomainObjectModel.
     *
     * @param typeName the name of the type being modeled. Must not be null.
     * @param isAbstract indicates whether the type being modeled is abstract.
     * @param allFields a list of all fields in the type being modeled. Must not be null.
     * @param methods a list of methods in the type being modeled. Must not be null.
     * @param inheritanceHierarchyTypeNames a list of type names representing the inheritance hierarchy
     *                                       of the type being modeled. Must not be null.
     * @param allInterfaceTypeNames a list of all interface type names implemented by the type being modeled. Must not be null.
     */
    public DomainObjectModel(String typeName,
                             boolean isAbstract,
                             List<FieldMirror> allFields,
                             List<MethodMirror> methods,
                             List<String> inheritanceHierarchyTypeNames,
                             List<String> allInterfaceTypeNames
    ) {
        super(typeName, isAbstract, allFields, methods, inheritanceHierarchyTypeNames, allInterfaceTypeNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FieldMirror> getBasicFields() {
        return allFields.stream().filter(p ->
                DomainType.NON_DOMAIN.equals(p.getType().getDomainType())
            )
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValueReferenceMirror> getValueReferences() {
        return allFields.stream().filter(p ->
                DomainType.VALUE_OBJECT.equals(p.getType().getDomainType()) ||
                    DomainType.ENUM.equals(p.getType().getDomainType()) ||
                    DomainType.IDENTITY.equals(p.getType().getDomainType())
            )
            .map(p -> (ValueReferenceMirror) p)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueReferenceMirror valueReferenceByName(String name) {
        return allFields.stream().filter(p ->
                p.getName().equals(name) && (
                    DomainType.VALUE_OBJECT.equals(p.getType().getDomainType()) ||
                        DomainType.ENUM.equals(p.getType().getDomainType()) ||
                        DomainType.IDENTITY.equals(p.getType().getDomainType())
                )
            )
            .map(p -> (ValueReferenceMirror) p)
            .findFirst()
            .orElseThrow(() -> MirrorException.fail(
                String.format("ValueReferenceMirror not found for name '%s' within '%s'!", name, typeName)));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "DomainObjectModel{} " + super.toString();
    }

}