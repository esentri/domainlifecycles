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

import java.io.InputStream;
import java.io.OutputStream;

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
     * Serializes a given {@link DomainCalls} instance directly to the given output stream, without
     * ever holding the complete serialized representation in memory as a single String. Useful for
     * large analysis results, or when the output is itself being streamed on, e.g. compressed or
     * sent over the network as it is produced.
     * <p>
     * The stream is written to, but not closed; the caller remains responsible for it.
     *
     * @param domainCalls  the DomainCalls instance to be serialized
     * @param outputStream the stream the serialized DomainCalls is written to
     */
    void serialize(DomainCalls domainCalls, OutputStream outputStream);

    /**
     * Deserializes the given string representation of a {@link DomainCalls} result back into a
     * {@link DomainCalls} instance, resolving its methods against the given {@link DomainMirror}.
     *
     * @param serializedDomainCalls the string representation of a serialized DomainCalls result
     * @param domainMirror          the DomainMirror the serialized result was analyzed against
     * @return the deserialized DomainCalls instance
     */
    DomainCalls deserialize(String serializedDomainCalls, DomainMirror domainMirror);

    /**
     * Deserializes a {@link DomainCalls} result, read directly from the given input stream, back
     * into a {@link DomainCalls} instance, resolving its methods against the given
     * {@link DomainMirror}, without ever holding the complete serialized representation in memory as
     * a single String.
     * <p>
     * The stream is read from, but not closed; the caller remains responsible for it.
     *
     * @param serializedDomainCalls the stream a serialized DomainCalls result is read from
     * @param domainMirror          the DomainMirror the serialized result was analyzed against
     * @return the deserialized DomainCalls instance
     */
    DomainCalls deserialize(InputStream serializedDomainCalls, DomainMirror domainMirror);
}
