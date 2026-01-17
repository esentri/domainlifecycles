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

package io.domainlifecycles.mirror.serialize.jackson2;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.ApplicationServiceMirror;
import io.domainlifecycles.mirror.api.AssertedContainableTypeMirror;
import io.domainlifecycles.mirror.api.AssertionMirror;
import io.domainlifecycles.mirror.api.BoundedContextMirror;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainObjectMirror;
import io.domainlifecycles.mirror.api.DomainServiceMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.EnumMirror;
import io.domainlifecycles.mirror.api.EnumOptionMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.ReadModelMirror;
import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.model.AggregateRootModel;
import io.domainlifecycles.mirror.model.AggregateRootReferenceModel;
import io.domainlifecycles.mirror.model.ApplicationServiceModel;
import io.domainlifecycles.mirror.model.AssertedContainableTypeModel;
import io.domainlifecycles.mirror.model.AssertionModel;
import io.domainlifecycles.mirror.model.BoundedContextModel;
import io.domainlifecycles.mirror.model.DomainCommandModel;
import io.domainlifecycles.mirror.model.DomainEventModel;
import io.domainlifecycles.mirror.model.DomainModel;
import io.domainlifecycles.mirror.model.DomainObjectModel;
import io.domainlifecycles.mirror.model.DomainServiceModel;
import io.domainlifecycles.mirror.model.DomainTypeModel;
import io.domainlifecycles.mirror.model.EntityModel;
import io.domainlifecycles.mirror.model.EntityReferenceModel;
import io.domainlifecycles.mirror.model.EnumModel;
import io.domainlifecycles.mirror.model.EnumOptionModel;
import io.domainlifecycles.mirror.model.FieldModel;
import io.domainlifecycles.mirror.model.IdentityModel;
import io.domainlifecycles.mirror.model.MethodModel;
import io.domainlifecycles.mirror.model.OutboundServiceModel;
import io.domainlifecycles.mirror.model.ParamModel;
import io.domainlifecycles.mirror.model.QueryHandlerModel;
import io.domainlifecycles.mirror.model.ReadModelModel;
import io.domainlifecycles.mirror.model.ServiceKindModel;
import io.domainlifecycles.mirror.model.ValueObjectModel;
import io.domainlifecycles.mirror.model.ValueReferenceModel;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.AggregateRootMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.AggregateRootReferenceMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.ApplicationServiceMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.AssertedContainableTypeMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.AssertionMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.BoundedContextMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.DomainCommandMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.DomainEventMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.DomainMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.DomainObjectMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.DomainServiceMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.DomainTypeMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.EntityMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.EntityReferenceMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.EnumMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.EnumOptionMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.FieldMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.IdentityMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.MethodMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.OutboundServiceMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.ParamMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.QueryHandlerMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.ReadModelMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.ResolvedGenericTypeMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.ServiceKindMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.ValueObjectMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.mirror.ValueReferenceMirrorMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.AggregateRootModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.AggregateRootReferenceModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.ApplicationServiceModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.AssertedContainableTypeModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.AssertionModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.BoundedContextModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.DomainCommandModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.DomainEventModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.DomainModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.DomainObjectModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.DomainServiceModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.DomainTypeModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.EntityModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.EntityReferenceModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.EnumModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.EnumOptionModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.FieldModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.IdentityModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.MethodModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.OutboundServiceModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.ParamModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.QueryHandlerModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.ReadModelModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.ServiceKindModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.ValueObjectModelMixin;
import io.domainlifecycles.mirror.serialize.jackson2.model.ValueReferenceModelMixin;
import io.domainlifecycles.mirror.serialize.DomainSerializer;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Jackson 2 specific implementation of a {@link DomainSerializer}.
 * <p>
 * It can serialize all mirrors of given domain {@link DomainModel} into a String.
 * And it also can deserialize it back to the original state of an {@link DomainModel} (without having to use
 * Java reflection).
 *
 * @author Mario Herb
 */
public class JacksonDomainSerializer implements DomainSerializer {

    private final ObjectMapper objectMapper;

    /**
     * Initialize Jackson specific settings for the Serializer.
     *
     * @param prettyPrint when serializing to a String
     */
    public JacksonDomainSerializer(boolean prettyPrint) {
        JsonMapper.Builder jsonMapperBuilder = JsonMapper.builder()

            .addMixIn(AggregateRootMirror.class, AggregateRootMirrorMixin.class)
            .addMixIn(AggregateRootModel.class, AggregateRootModelMixin.class)

            .addMixIn(AggregateRootMirror.class, AggregateRootMirrorMixin.class)
            .addMixIn(AggregateRootModel.class, AggregateRootModelMixin.class)

            .addMixIn(AggregateRootReferenceMirror.class, AggregateRootReferenceMirrorMixin.class)
            .addMixIn(AggregateRootReferenceModel.class, AggregateRootReferenceModelMixin.class)

            .addMixIn(ApplicationServiceMirror.class, ApplicationServiceMirrorMixin.class)
            .addMixIn(ApplicationServiceModel.class, ApplicationServiceModelMixin.class)

            .addMixIn(AssertedContainableTypeMirror.class, AssertedContainableTypeMirrorMixin.class)
            .addMixIn(AssertedContainableTypeModel.class, AssertedContainableTypeModelMixin.class)

            .addMixIn(AssertionMirror.class, AssertionMirrorMixin.class)
            .addMixIn(AssertionModel.class, AssertionModelMixin.class)

            .addMixIn(BoundedContextMirror.class, BoundedContextMirrorMixin.class)
            .addMixIn(BoundedContextModel.class, BoundedContextModelMixin.class)

            .addMixIn(DomainCommandMirror.class, DomainCommandMirrorMixin.class)
            .addMixIn(DomainCommandModel.class, DomainCommandModelMixin.class)

            .addMixIn(DomainEventMirror.class, DomainEventMirrorMixin.class)
            .addMixIn(DomainEventModel.class, DomainEventModelMixin.class)

            .addMixIn(DomainMirror.class, DomainMirrorMixin.class)
            .addMixIn(DomainModel.class, DomainModelMixin.class)

            .addMixIn(DomainObjectMirror.class, DomainObjectMirrorMixin.class)
            .addMixIn(DomainObjectModel.class, DomainObjectModelMixin.class)

            .addMixIn(DomainServiceMirror.class, DomainServiceMirrorMixin.class)
            .addMixIn(DomainServiceModel.class, DomainServiceModelMixin.class)

            .addMixIn(DomainTypeMirror.class, DomainTypeMirrorMixin.class)
            .addMixIn(DomainTypeModel.class, DomainTypeModelMixin.class)

            .addMixIn(EntityMirror.class, EntityMirrorMixin.class)
            .addMixIn(EntityModel.class, EntityModelMixin.class)

            .addMixIn(EntityReferenceMirror.class, EntityReferenceMirrorMixin.class)
            .addMixIn(EntityReferenceModel.class, EntityReferenceModelMixin.class)

            .addMixIn(EnumMirror.class, EnumMirrorMixin.class)
            .addMixIn(EnumModel.class, EnumModelMixin.class)

            .addMixIn(EnumOptionMirror.class, EnumOptionMirrorMixin.class)
            .addMixIn(EnumOptionModel.class, EnumOptionModelMixin.class)

            .addMixIn(FieldMirror.class, FieldMirrorMixin.class)
            .addMixIn(FieldModel.class, FieldModelMixin.class)

            .addMixIn(IdentityMirror.class, IdentityMirrorMixin.class)
            .addMixIn(IdentityModel.class, IdentityModelMixin.class)

            .addMixIn(MethodMirror.class, MethodMirrorMixin.class)
            .addMixIn(MethodModel.class, MethodModelMixin.class)

            .addMixIn(OutboundServiceMirror.class, OutboundServiceMirrorMixin.class)
            .addMixIn(OutboundServiceModel.class, OutboundServiceModelMixin.class)

            .addMixIn(ParamMirror.class, ParamMirrorMixin.class)
            .addMixIn(ParamModel.class, ParamModelMixin.class)

            .addMixIn(QueryHandlerMirror.class, QueryHandlerMirrorMixin.class)
            .addMixIn(QueryHandlerModel.class, QueryHandlerModelMixin.class)

            .addMixIn(ReadModelMirror.class, ReadModelMirrorMixin.class)
            .addMixIn(ReadModelModel.class, ReadModelModelMixin.class)

            .addMixIn(ResolvedGenericTypeMirror.class, ResolvedGenericTypeMirrorMixin.class)

            .addMixIn(ServiceKindMirror.class, ServiceKindMirrorMixin.class)
            .addMixIn(ServiceKindModel.class, ServiceKindModelMixin.class)

            .addMixIn(ValueObjectMirror.class, ValueObjectMirrorMixin.class)
            .addMixIn(ValueObjectModel.class, ValueObjectModelMixin.class)

            .addMixIn(ValueReferenceMirror.class, ValueReferenceMirrorMixin.class)
            .addMixIn(ValueReferenceModel.class, ValueReferenceModelMixin.class)
            .addModule(new Jdk8Module())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (prettyPrint) {
            jsonMapperBuilder.enable(SerializationFeature.INDENT_OUTPUT);
        }

        this.objectMapper = jsonMapperBuilder.build();
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
    @SuppressWarnings("unchecked")
    public <T extends DomainTypeMirror> T deserializeTypeMirror(String serializedTypeMirror) {
        try {
            var dm =  (T) objectMapper.readValue(serializedTypeMirror, DomainTypeMirror.class);
            return dm;
        } catch (JacksonException e) {
            throw MirrorException.fail("Jackson deserialization failed!", e);
        }
    }
}
