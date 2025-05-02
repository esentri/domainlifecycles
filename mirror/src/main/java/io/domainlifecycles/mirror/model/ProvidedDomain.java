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

package io.domainlifecycles.mirror.model;

import io.domainlifecycles.mirror.api.DomainMirror;

/**
 * Represents a domain that can be provided for a model.
 * This interface defines methods to set and query the state of the domain mirror
 * associated with the implementing class.
 *
 * @author Mario Herb
 */
public interface ProvidedDomain {

    /**
     * Sets the domain mirror for this model.
     *
     * @param domainMirror the {@link DomainMirror} instance to be set
     */
    void setDomainMirror(DomainMirror domainMirror);



    /**
     * Checks whether the domain mirror has been set.
     *
     * @return true if the domain mirror is set, false otherwise
     */
    boolean domainMirrorSet();
}
