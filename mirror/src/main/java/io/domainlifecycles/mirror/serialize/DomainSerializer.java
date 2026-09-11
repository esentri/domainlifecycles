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
import io.domainlifecycles.mirror.api.DomainTypeMirror;

import java.io.InputStream;
import java.io.OutputStream;

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
     * Serializes a given DomainMirror instance directly to the given output stream, without ever
     * holding the complete serialized representation in memory as a single String. Useful for large
     * domains, or when the output is itself being streamed on, e.g. compressed or sent over the
     * network as it is produced.
     * <p>
     * The stream is written to, but not closed; the caller remains responsible for it.
     *
     * @param domainMirror the DomainMirror instance to be serialized
     * @param outputStream the stream the serialized DomainMirror is written to
     */
    void serialize(DomainMirror domainMirror, OutputStream outputStream);

    /**
     * Deserializes the given string representation of a domain into a DomainMirror object.
     *
     * @param serializedDomain the string representation of a serialized domain
     * @return the deserialized DomainMirror object
     */
    DomainMirror deserialize(String serializedDomain);

    /**
     * Deserializes a domain, read directly from the given input stream, into a DomainMirror object,
     * without ever holding the complete serialized representation in memory as a single String.
     * Useful for large domains, or when the input is itself being streamed from, e.g. decompressed or
     * received over the network as it is consumed.
     * <p>
     * The stream is read from, but not closed; the caller remains responsible for it.
     *
     * @param serializedDomain the stream a serialized domain is read from
     * @return the deserialized DomainMirror object
     */
    DomainMirror deserialize(InputStream serializedDomain);

    /**
     * Deserializes the given string representation of a serialized type into its corresponding
     * {@link DomainTypeMirror} implementation.
     *
     * @param <T>                the specific type of {@link DomainTypeMirror} expected as the result
     * @param serializedTypeMirror the string representation of the serialized type mirror
     * @return the deserialized {@link DomainTypeMirror} instance of the specified type
     */
    <T extends DomainTypeMirror> T deserializeTypeMirror(String serializedTypeMirror);
}
