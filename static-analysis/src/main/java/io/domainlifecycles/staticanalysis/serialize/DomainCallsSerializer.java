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

package io.domainlifecycles.staticanalysis.serialize;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.staticanalysis.DomainCalls;

/**
 * Generic interface to de-/serialize a {@link DomainCalls} analysis result, so it can be produced
 * once (e.g. in a build step, alongside the {@link DomainMirror} it was analyzed against) and
 * consumed elsewhere without re-running the static analysis - for instance by a tool that
 * visualizes the domain.
 * <p>
 * A {@link DomainCalls} node ({@link io.domainlifecycles.staticanalysis.DomainMethod}) is not a
 * self-contained value: it references a {@link io.domainlifecycles.mirror.api.MethodMirror}, which
 * in turn is only meaningful as part of a fully initialized {@link DomainMirror}. Deserializing a
 * {@link DomainCalls} therefore needs that {@link DomainMirror} - the very one the serialized
 * result was analyzed against - to resolve its methods back against.
 *
 * @author Mario Herb
 */
public interface DomainCallsSerializer {

    /**
     * Serializes a given {@link DomainCalls} instance into its string representation.
     *
     * @param domainCalls the DomainCalls instance to be serialized
     * @return a string representation of the serialized DomainCalls
     */
    String serialize(DomainCalls domainCalls);

    /**
     * Deserializes the given string representation of a {@link DomainCalls} result back into a
     * {@link DomainCalls} instance, resolving its methods against the given {@link DomainMirror}.
     *
     * @param serializedDomainCalls the string representation of a serialized DomainCalls result
     * @param domainMirror          the DomainMirror the serialized result was analyzed against
     * @return the deserialized DomainCalls instance
     */
    DomainCalls deserialize(String serializedDomainCalls, DomainMirror domainMirror);
}
