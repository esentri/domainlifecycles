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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.jackson2.databind.context;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

/**
 * A DomainObjectMappingContextKey identifies a {@link DomainObjectMappingContext} instance, within the
 * {@link DomainObjectMappingContextHolder}.
 *
 * @author Leon Völlinger
 * @author Mario Herb
 */
@Deprecated
public class DomainObjectMappingContextKey {
    private final JsonNode node;
    private final String domainObjectTypeName;

    /**
     * Constructs a DomainObjectMappingContextKey with the provided JSON node and domain object type name.
     *
     * @param node The JSON node representing a DomainObject instance
     * @param domainObjectTypeName The name of the DomainObject type
     */
    public DomainObjectMappingContextKey(JsonNode node, String domainObjectTypeName) {
        Objects.requireNonNull(node);
        Objects.requireNonNull(domainObjectTypeName);
        this.node = node;
        this.domainObjectTypeName = domainObjectTypeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainObjectMappingContextKey that)) return false;
        return node.equals(that.node) &&
            domainObjectTypeName.equals(that.domainObjectTypeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(node, domainObjectTypeName);
    }
}
