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

package io.domainlifecycles.mirror.serialize;

import io.domainlifecycles.mirror.api.DomainMirror;

/**
 * Generic interface to de-/serialize the DomainModel.
 *
 * @author Mario Herb
 */
public interface DomainSerializer {

    /**
     * Serializes a given DomainMirror instance into its string representation.
     *
     * @param domainMirror the DomainMirror instance to be serialized
     * @return a string representation of the serialized DomainMirror
     */
    String serialize(DomainMirror domainMirror);

    /**
     * Deserializes the given string representation of a domain into a DomainMirror object.
     *
     * @param serializedDomain the string representation of a serialized domain
     * @return the deserialized DomainMirror object
     */
    DomainMirror deserialize(String serializedDomain);
}
