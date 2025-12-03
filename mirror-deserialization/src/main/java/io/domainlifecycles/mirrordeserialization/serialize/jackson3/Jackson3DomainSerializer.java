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

package io.domainlifecycles.mirrordeserialization.serialize.jackson3;

import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.api.BoundedContextMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EnumOptionMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.model.DomainModel;
import io.domainlifecycles.mirrordeserialization.serialize.DomainSerializer;
import io.domainlifecycles.mirrordeserialization.serialize.jackson2.ResolvedGenericTypeMirrorMixinJackson2;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.json.JsonMapper.Builder;

/**
 * Jackson 3 specific implementation of a {@link DomainSerializer}.
 * <p>
 * It can serialize all mirrors of given domain {@link DomainModel} into a String.
 * And it also can deserialize it back to the original state of an {@link DomainModel} (without having to use
 * Java reflection).
 *
 * @author Mario Herb
 */
public class Jackson3DomainSerializer implements DomainSerializer {

    private final ObjectMapper objectMapper;

    /**
     * Initialize Jackson specific settings for the Serializer.
     *
     * @param prettyPrint when serializing to a String
     */
    public Jackson3DomainSerializer(boolean prettyPrint) {

        Builder objectMapperBuilder = JsonMapper.builder()
            .addMixIn(ResolvedGenericTypeMirror.class, ResolvedGenericTypeMirrorMixinJackson2.class);

        if (prettyPrint) {
            objectMapperBuilder.enable(SerializationFeature.INDENT_OUTPUT);
        }

        this.objectMapper = objectMapperBuilder.build();
    }

    /**
     * Serializes the given domain mirrors.
     *
     * @return JSON String of the mirror information
     */
    @Override
    public String serialize(DomainMirror domainMirror) {
        try {
            return objectMapper.writeValueAsString(domainMirror);
        } catch (JacksonException e) {
            throw MirrorException.fail("Jackson serialization failed!", e);
        }
    }

    /**
     * Deserializes a given serialized domain String, which was created by this Serializer.
     */
    @Override
    public DomainMirror deserialize(String serializedDomain) {
        try {
            var dm =  objectMapper.readValue(serializedDomain, DomainMirror.class);
            return dm;
        } catch (JacksonException e) {
            throw MirrorException.fail("Jackson deserialization failed!", e);
        }
    }

    /**
     * Deserializes a serialized string representation of a {@code DomainTypeMirror} into an object of the specified type.
     *
     * @param <T> the target type of the deserialized object, which must extend {@code DomainTypeMirror}
     * @param serializedTypeMirror the string representation of the serialized {@code DomainTypeMirror}
     * @return the deserialized {@code DomainTypeMirror} object of type {@code T}
     * @throws MirrorException if the deserialization process encounters an error
     */
    public <T extends DomainTypeMirror> T deserializeTypeMirror(String serializedTypeMirror) {
        try {
            var dm =  (T) objectMapper.readValue(serializedTypeMirror, DomainTypeMirror.class);
            return dm;
        } catch (JacksonException e) {
            throw MirrorException.fail("Jackson deserialization failed!", e);
        }
    }
}
