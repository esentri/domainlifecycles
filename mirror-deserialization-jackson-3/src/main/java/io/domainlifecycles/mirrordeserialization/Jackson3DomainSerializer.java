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

package io.domainlifecycles.mirrordeserialization;

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
import io.domainlifecycles.mirror.serialize.DomainSerializer;
import io.domainlifecycles.mirrordeserialization.mirror.AggregateRootMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.AggregateRootReferenceMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.ApplicationServiceMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.AssertedContainableTypeMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.AssertionMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.BoundedContextMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.DomainCommandMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.DomainEventMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.DomainMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.DomainObjectMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.DomainServiceMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.DomainTypeMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.EntityMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.EntityReferenceMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.EnumMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.EnumOptionMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.FieldMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.IdentityMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.MethodMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.OutboundServiceMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.ParamMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.QueryHandlerMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.ReadModelMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.ResolvedGenericTypeMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.ServiceKindMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.ValueObjectMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.mirror.ValueReferenceMirrorMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.AggregateRootModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.AggregateRootReferenceModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.ApplicationServiceModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.AssertedContainableTypeModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.AssertionModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.BoundedContextModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.DomainCommandModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.DomainEventModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.DomainModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.DomainObjectModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.DomainServiceModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.DomainTypeModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.EntityModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.EntityReferenceModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.EnumModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.EnumOptionModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.FieldModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.IdentityModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.MethodModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.OutboundServiceModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.ParamModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.QueryHandlerModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.ReadModelModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.ServiceKindModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.ValueObjectModelMixinJackson2;
import io.domainlifecycles.mirrordeserialization.model.ValueReferenceModelMixinJackson2;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Jackson 2 specific implementation of a {@link DomainSerializer}.
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
        JsonMapper.Builder jsonMapperBuilder = JsonMapper.builder()

            .addMixIn(AggregateRootMirror.class, AggregateRootMirrorMixinJackson2.class)
            .addMixIn(AggregateRootModel.class, AggregateRootModelMixinJackson2.class)

            .addMixIn(AggregateRootMirror.class, AggregateRootMirrorMixinJackson2.class)
            .addMixIn(AggregateRootModel.class, AggregateRootModelMixinJackson2.class)

            .addMixIn(AggregateRootReferenceMirror.class, AggregateRootReferenceMirrorMixinJackson2.class)
            .addMixIn(AggregateRootReferenceModel.class, AggregateRootReferenceModelMixinJackson2.class)

            .addMixIn(ApplicationServiceMirror.class, ApplicationServiceMirrorMixinJackson2.class)
            .addMixIn(ApplicationServiceModel.class, ApplicationServiceModelMixinJackson2.class)

            .addMixIn(AssertedContainableTypeMirror.class, AssertedContainableTypeMirrorMixinJackson2.class)
            .addMixIn(AssertedContainableTypeModel.class, AssertedContainableTypeModelMixinJackson2.class)

            .addMixIn(AssertionMirror.class, AssertionMirrorMixinJackson2.class)
            .addMixIn(AssertionModel.class, AssertionModelMixinJackson2.class)

            .addMixIn(BoundedContextMirror.class, BoundedContextMirrorMixinJackson2.class)
            .addMixIn(BoundedContextModel.class, BoundedContextModelMixinJackson2.class)

            .addMixIn(DomainCommandMirror.class, DomainCommandMirrorMixinJackson2.class)
            .addMixIn(DomainCommandModel.class, DomainCommandModelMixinJackson2.class)

            .addMixIn(DomainEventMirror.class, DomainEventMirrorMixinJackson2.class)
            .addMixIn(DomainEventModel.class, DomainEventModelMixinJackson2.class)

            .addMixIn(DomainMirror.class, DomainMirrorMixinJackson2.class)
            .addMixIn(DomainModel.class, DomainModelMixinJackson2.class)

            .addMixIn(DomainObjectMirror.class, DomainObjectMirrorMixinJackson2.class)
            .addMixIn(DomainObjectModel.class, DomainObjectModelMixinJackson2.class)

            .addMixIn(DomainServiceMirror.class, DomainServiceMirrorMixinJackson2.class)
            .addMixIn(DomainServiceModel.class, DomainServiceModelMixinJackson2.class)

            .addMixIn(DomainTypeMirror.class, DomainTypeMirrorMixinJackson2.class)
            .addMixIn(DomainTypeModel.class, DomainTypeModelMixinJackson2.class)

            .addMixIn(EntityMirror.class, EntityMirrorMixinJackson2.class)
            .addMixIn(EntityModel.class, EntityModelMixinJackson2.class)

            .addMixIn(EntityReferenceMirror.class, EntityReferenceMirrorMixinJackson2.class)
            .addMixIn(EntityReferenceModel.class, EntityReferenceModelMixinJackson2.class)

            .addMixIn(EnumMirror.class, EnumMirrorMixinJackson2.class)
            .addMixIn(EnumModel.class, EnumModelMixinJackson2.class)

            .addMixIn(EnumOptionMirror.class, EnumOptionMirrorMixinJackson2.class)
            .addMixIn(EnumOptionModel.class, EnumOptionModelMixinJackson2.class)

            .addMixIn(FieldMirror.class, FieldMirrorMixinJackson2.class)
            .addMixIn(FieldModel.class, FieldModelMixinJackson2.class)

            .addMixIn(IdentityMirror.class, IdentityMirrorMixinJackson2.class)
            .addMixIn(IdentityModel.class, IdentityModelMixinJackson2.class)

            .addMixIn(MethodMirror.class, MethodMirrorMixinJackson2.class)
            .addMixIn(MethodModel.class, MethodModelMixinJackson2.class)

            .addMixIn(OutboundServiceMirror.class, OutboundServiceMirrorMixinJackson2.class)
            .addMixIn(OutboundServiceModel.class, OutboundServiceModelMixinJackson2.class)

            .addMixIn(ParamMirror.class, ParamMirrorMixinJackson2.class)
            .addMixIn(ParamModel.class, ParamModelMixinJackson2.class)

            .addMixIn(QueryHandlerMirror.class, QueryHandlerMirrorMixinJackson2.class)
            .addMixIn(QueryHandlerModel.class, QueryHandlerModelMixinJackson2.class)

            .addMixIn(ReadModelMirror.class, ReadModelMirrorMixinJackson2.class)
            .addMixIn(ReadModelModel.class, ReadModelModelMixinJackson2.class)

            .addMixIn(ResolvedGenericTypeMirror.class, ResolvedGenericTypeMirrorMixinJackson2.class)

            .addMixIn(ServiceKindMirror.class, ServiceKindMirrorMixinJackson2.class)
            .addMixIn(ServiceKindModel.class, ServiceKindModelMixinJackson2.class)

            .addMixIn(ValueObjectMirror.class, ValueObjectMirrorMixinJackson2.class)
            .addMixIn(ValueObjectModel.class, ValueObjectModelMixinJackson2.class)

            .addMixIn(ValueReferenceMirror.class, ValueReferenceMirrorMixinJackson2.class)
            .addMixIn(ValueReferenceModel.class, ValueReferenceModelMixinJackson2.class)

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
    public <T extends DomainTypeMirror> T deserializeTypeMirror(String serializedTypeMirror) {
        try {
            var dm =  (T) objectMapper.readValue(serializedTypeMirror, DomainTypeMirror.class);
            return dm;
        } catch (JacksonException e) {
            throw MirrorException.fail("Jackson deserialization failed!", e);
        }
    }
}
