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

package io.domainlifecycles.mirror.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.domainlifecycles.mirror.api.*;
import io.domainlifecycles.mirror.model.*;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.model.AggregateRootModel;
import io.domainlifecycles.mirror.model.DomainModel;
import io.domainlifecycles.mirror.serialize.mirror.AggregateRootMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.AggregateRootReferenceMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.ApplicationServiceMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.AssertedContainableTypeMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.AssertionMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.BoundedContextMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.DomainCommandMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.DomainEventMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.DomainMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.DomainObjectMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.DomainServiceMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.DomainTypeMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.EntityMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.EntityReferenceMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.EnumMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.EnumOptionMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.FieldMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.IdentityMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.MethodMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.OutboundServiceMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.ParamMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.QueryHandlerMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.ReadModelMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.ResolvedGenericTypeMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.ServiceKindMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.ValueObjectMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.mirror.ValueReferenceMirrorMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.AggregateRootModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.AggregateRootReferenceModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.ApplicationServiceModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.AssertedContainableTypeModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.AssertionModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.BoundedContextModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.DomainCommandModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.DomainEventModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.DomainModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.DomainObjectModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.DomainServiceModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.DomainTypeModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.EntityModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.EntityReferenceModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.EnumModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.EnumOptionModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.FieldModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.IdentityModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.MethodModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.OutboundServiceModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.ParamModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.QueryHandlerModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.ReadModelModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.ServiceKindModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.ValueObjectModelMixinJackson2;
import io.domainlifecycles.mirror.serialize.model.ValueReferenceModelMixinJackson2;

/**
 * Jackson 2 specific implementation of a {@link DomainSerializer}.
 * <p>
 * It can serialize all mirrors of given domain {@link DomainModel} into a String.
 * And it also can deserialize it back to the original state of an {@link DomainModel} (without having to use
 * Java reflection).
 *
 * @author Mario Herb
 */
@Deprecated
public class Jackson2DomainSerializer implements DomainSerializer {

    private final ObjectMapper objectMapper;

    /**
     * Initialize Jackson specific settings for the Serializer.
     *
     * @param prettyPrint when serializing to a String
     */
    public Jackson2DomainSerializer(boolean prettyPrint) {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new ParameterNamesModule());

        objectMapper.addMixIn(AggregateRootMirror.class, AggregateRootMirrorMixinJackson2.class);
        objectMapper.addMixIn(AggregateRootModel.class, AggregateRootModelMixinJackson2.class);

        objectMapper.addMixIn(AggregateRootMirror.class, AggregateRootMirrorMixinJackson2.class);
        objectMapper.addMixIn(AggregateRootModel.class, AggregateRootModelMixinJackson2.class);

        objectMapper.addMixIn(AggregateRootReferenceMirror.class, AggregateRootReferenceMirrorMixinJackson2.class);
        objectMapper.addMixIn(AggregateRootReferenceModel.class, AggregateRootReferenceModelMixinJackson2.class);

        objectMapper.addMixIn(ApplicationServiceMirror.class, ApplicationServiceMirrorMixinJackson2.class);
        objectMapper.addMixIn(ApplicationServiceModel.class, ApplicationServiceModelMixinJackson2.class);

        objectMapper.addMixIn(AssertedContainableTypeMirror.class, AssertedContainableTypeMirrorMixinJackson2.class);
        objectMapper.addMixIn(AssertedContainableTypeModel.class, AssertedContainableTypeModelMixinJackson2.class);

        objectMapper.addMixIn(AssertionMirror.class, AssertionMirrorMixinJackson2.class);
        objectMapper.addMixIn(AssertionModel.class, AssertionModelMixinJackson2.class);

        objectMapper.addMixIn(BoundedContextMirror.class, BoundedContextMirrorMixinJackson2.class);
        objectMapper.addMixIn(BoundedContextModel.class, BoundedContextModelMixinJackson2.class);

        objectMapper.addMixIn(DomainCommandMirror.class, DomainCommandMirrorMixinJackson2.class);
        objectMapper.addMixIn(DomainCommandModel.class, DomainCommandModelMixinJackson2.class);

        objectMapper.addMixIn(DomainEventMirror.class, DomainEventMirrorMixinJackson2.class);
        objectMapper.addMixIn(DomainEventModel.class, DomainEventModelMixinJackson2.class);

        objectMapper.addMixIn(DomainMirror.class, DomainMirrorMixinJackson2.class);
        objectMapper.addMixIn(DomainModel.class, DomainModelMixinJackson2.class);

        objectMapper.addMixIn(DomainObjectMirror.class, DomainObjectMirrorMixinJackson2.class);
        objectMapper.addMixIn(DomainObjectModel.class, DomainObjectModelMixinJackson2.class);

        objectMapper.addMixIn(DomainServiceMirror.class, DomainServiceMirrorMixinJackson2.class);
        objectMapper.addMixIn(DomainServiceModel.class, DomainServiceModelMixinJackson2.class);

        objectMapper.addMixIn(DomainTypeMirror.class, DomainTypeMirrorMixinJackson2.class);
        objectMapper.addMixIn(DomainTypeModel.class, DomainTypeModelMixinJackson2.class);

        objectMapper.addMixIn(EntityMirror.class, EntityMirrorMixinJackson2.class);
        objectMapper.addMixIn(EntityModel.class, EntityModelMixinJackson2.class);

        objectMapper.addMixIn(EntityReferenceMirror.class, EntityReferenceMirrorMixinJackson2.class);
        objectMapper.addMixIn(EntityReferenceModel.class, EntityReferenceModelMixinJackson2.class);

        objectMapper.addMixIn(EnumMirror.class, EnumMirrorMixinJackson2.class);
        objectMapper.addMixIn(EnumModel.class, EnumModelMixinJackson2.class);

        objectMapper.addMixIn(EnumOptionMirror.class, EnumOptionMirrorMixinJackson2.class);
        objectMapper.addMixIn(EnumOptionModel.class, EnumOptionModelMixinJackson2.class);

        objectMapper.addMixIn(FieldMirror.class, FieldMirrorMixinJackson2.class);
        objectMapper.addMixIn(FieldModel.class, FieldModelMixinJackson2.class);

        objectMapper.addMixIn(IdentityMirror.class, IdentityMirrorMixinJackson2.class);
        objectMapper.addMixIn(IdentityModel.class, IdentityModelMixinJackson2.class);

        objectMapper.addMixIn(MethodMirror.class, MethodMirrorMixinJackson2.class);
        objectMapper.addMixIn(MethodModel.class, MethodModelMixinJackson2.class);

        objectMapper.addMixIn(OutboundServiceMirror.class, OutboundServiceMirrorMixinJackson2.class);
        objectMapper.addMixIn(OutboundServiceModel.class, OutboundServiceModelMixinJackson2.class);

        objectMapper.addMixIn(ParamMirror.class, ParamMirrorMixinJackson2.class);
        objectMapper.addMixIn(ParamModel.class, ParamModelMixinJackson2.class);

        objectMapper.addMixIn(QueryHandlerMirror.class, QueryHandlerMirrorMixinJackson2.class);
        objectMapper.addMixIn(QueryHandlerModel.class, QueryHandlerModelMixinJackson2.class);

        objectMapper.addMixIn(ReadModelMirror.class, ReadModelMirrorMixinJackson2.class);
        objectMapper.addMixIn(ReadModelModel.class, ReadModelModelMixinJackson2.class);

        objectMapper.addMixIn(ResolvedGenericTypeMirror.class, ResolvedGenericTypeMirrorMixinJackson2.class);

        objectMapper.addMixIn(ServiceKindMirror.class, ServiceKindMirrorMixinJackson2.class);
        objectMapper.addMixIn(ServiceKindModel.class, ServiceKindModelMixinJackson2.class);

        objectMapper.addMixIn(ValueObjectMirror.class, ValueObjectMirrorMixinJackson2.class);
        objectMapper.addMixIn(ValueObjectModel.class, ValueObjectModelMixinJackson2.class);

        objectMapper.addMixIn(ValueReferenceMirror.class, ValueReferenceMirrorMixinJackson2.class);
        objectMapper.addMixIn(ValueReferenceModel.class, ValueReferenceModelMixinJackson2.class);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (prettyPrint) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
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
        } catch (JsonProcessingException e) {
            throw MirrorException.fail("Jackson serialization failed!", e);
        }
    }

    /**
     * Deserializes a given serialized domain String, which was created by this Serializer.
     */
    @Override
    public DomainMirror deserialize(String serializedDomain) {
        try {
            return objectMapper.readValue(serializedDomain, DomainMirror.class);
        } catch (JsonProcessingException e) {
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
    @SuppressWarnings("unchecked")
    public <T extends DomainTypeMirror> T deserializeTypeMirror(String serializedTypeMirror) {
        try {
            return (T) objectMapper.readValue(serializedTypeMirror, DomainTypeMirror.class);
        } catch (JsonProcessingException e) {
            throw MirrorException.fail("Jackson deserialization failed!", e);
        }
    }
}
