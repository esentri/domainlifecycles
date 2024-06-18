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

package io.domainlifecycles.jackson.api;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.jackson.databind.context.DomainObjectMappingContext;
import io.domainlifecycles.jackson.exception.DLCJacksonException;

/**
 * Simplified access to the current {@link DomainObjectBuilder} instance
 * from a {@link JacksonMappingCustomizer} instance. Just implement this interface in a JacksonMappingCustomizer, to use it.
 *
 * @author Mario Herb
 */
public interface BuilderConverter {

    /**
     * Returns the current DomainObjectBuilder instance from the current mapping context.
     */
    @SuppressWarnings("unchecked")
    default <B> B getDomainObjectBuilder(DomainObjectMappingContext domainObjectMappingContext) {
        if (domainObjectMappingContext.domainObjectBuilder != null) {
            return (B) domainObjectMappingContext.domainObjectBuilder.getBuilderInstance();
        }
        throw DLCJacksonException.fail("The given DomainObjectBuilder instance is not a valid implementation of " +
            "DomainObjectBuilder: '%s'", domainObjectMappingContext.domainObjectBuilder.getClass().getName());
    }

}
