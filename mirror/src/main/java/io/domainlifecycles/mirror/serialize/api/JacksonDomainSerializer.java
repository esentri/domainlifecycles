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

package io.domainlifecycles.mirror.serialize.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EnumOptionMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.InitializedDomain;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.api.BoundedContextMirror;
import io.domainlifecycles.mirror.exception.MirrorException;

/**
 * Jackson specific implementation a {@link DomainSerializer}.
 *
 * It can serialize all mirrors of given domain {@link InitializedDomain} into a String.
 * And it also can deserialize it back to the original state of an {@link InitializedDomain} (without having to use Java reflection).
 *
 * @author Mario Herb
 */
public class JacksonDomainSerializer implements DomainSerializer{

    private final ObjectMapper objectMapper;

    /**
     * Initialize Jackson specific settings for the Serializer.
     *
     * @param prettyPrint when serializing to a String
     */
    public JacksonDomainSerializer(boolean prettyPrint) {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.addMixIn(DomainTypeMirror.class, DomainTypeMirrorMixin.class);
        objectMapper.addMixIn(FieldMirror.class, FieldMirrorMixin.class);
        objectMapper.addMixIn(AssertedContainableTypeMirror.class, AssertedContainableTypeMirrorMixin.class);
        objectMapper.addMixIn(MethodMirror.class, MethodMirrorMixin.class);
        objectMapper.addMixIn(ParamMirror.class, ParamMirrorMixin.class);
        objectMapper.addMixIn(EnumOptionMirror.class, EnumOptionMirrorMixin.class);
        objectMapper.addMixIn(AssertionMirror.class, AssertionMirrorMixin.class);
        objectMapper.addMixIn(BoundedContextMirror.class, BoundedContextMirrorMixin.class);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if(prettyPrint){
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    /**
     * Serializes the given domain mirrors.
     *
     * @return JSON String of the mirror information
     */
    @Override
    public String serialize(InitializedDomain initializedDomain) {
        try {
            return objectMapper.writeValueAsString(initializedDomain);
        } catch (JsonProcessingException e) {
            throw MirrorException.fail("Jackson serialization failed!", e);
        }
    }

    /**
     * Deserializes a given serialized domain String, which was created by this Serializer.
     */
    @Override
    public InitializedDomain deserialize(String serializedDomain) {
        try {
            return objectMapper.readValue(serializedDomain, InitializedDomain.class);
        } catch (JsonProcessingException e) {
            throw MirrorException.fail("Jackson deserialization failed!", e);
        }
    }
}
