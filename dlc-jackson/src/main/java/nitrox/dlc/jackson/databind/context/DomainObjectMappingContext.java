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

package nitrox.dlc.jackson.databind.context;

import com.fasterxml.jackson.databind.JsonNode;
import nitrox.dlc.builder.DomainObjectBuilder;

import java.util.Objects;

/**
 * The DomainObjectMappingContext is associated to a {@code contextNode}, which is a JSON node,
 * that represents a DomainObject instance of a given {@code domainObjectType} in the deserialization process.
 *
 * It provides access to associated {@link DomainObjectBuilder} instances, that are filled with vaules throughout the
 * deserialization process.
 *
 * @author Mario Herb
 */
public class DomainObjectMappingContext {

    /**
     * The associated JsonNode
     */
    public final JsonNode contextNode;

    /**
     * The target type being mapped currently
     */
    public final String domainObjectTypeName;

    /**
     * The currently DomianObjectBuilder instance associated with the node
     */
    public final DomainObjectBuilder<?> domainObjectBuilder;

    /**
     * The parent context. The contexts form a hierarchy corresponding to the deserialized hierarchical object structure.
     */
    public final DomainObjectMappingContext parentContext;

    public DomainObjectMappingContext(
        JsonNode contextNode,
        String domainObjectTypeName,
        DomainObjectBuilder<?> domainObjectBuilder,
        DomainObjectMappingContext parentContext) {
        this.contextNode = contextNode;
        this.domainObjectTypeName = domainObjectTypeName;
        this.domainObjectBuilder = domainObjectBuilder;
        this.parentContext = parentContext;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObjectMappingContext that)) return false;
        return Objects.equals(contextNode, that.contextNode) &&
            Objects.equals(domainObjectTypeName, that.domainObjectTypeName) &&
            Objects.equals(domainObjectBuilder, that.domainObjectBuilder) &&
            Objects.equals(parentContext, that.parentContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(contextNode, domainObjectTypeName, domainObjectBuilder, parentContext);
    }

}
