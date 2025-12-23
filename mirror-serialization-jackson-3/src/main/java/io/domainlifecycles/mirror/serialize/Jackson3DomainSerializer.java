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
import io.domainlifecycles.mirror.serialize.mirror.AggregateRootMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.AggregateRootReferenceMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.ApplicationServiceMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.AssertedContainableTypeMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.AssertionMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.BoundedContextMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.DomainCommandMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.DomainEventMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.DomainMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.DomainObjectMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.DomainServiceMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.DomainTypeMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.EntityMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.EntityReferenceMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.EnumMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.EnumOptionMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.FieldMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.IdentityMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.MethodMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.OutboundServiceMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.ParamMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.QueryHandlerMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.ReadModelMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.ResolvedGenericTypeMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.ServiceKindMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.ValueObjectMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.mirror.ValueReferenceMirrorMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.AggregateRootModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.AggregateRootReferenceModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.ApplicationServiceModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.AssertedContainableTypeModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.AssertionModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.BoundedContextModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.DomainCommandModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.DomainEventModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.DomainModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.DomainObjectModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.DomainServiceModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.DomainTypeModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.EntityModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.EntityReferenceModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.EnumModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.EnumOptionModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.FieldModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.IdentityModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.MethodModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.OutboundServiceModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.ParamModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.QueryHandlerModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.ReadModelModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.ServiceKindModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.ValueObjectModelMixinJackson3;
import io.domainlifecycles.mirror.serialize.model.ValueReferenceModelMixinJackson3;
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

            .addMixIn(AggregateRootMirror.class, AggregateRootMirrorMixinJackson3.class)
            .addMixIn(AggregateRootModel.class, AggregateRootModelMixinJackson3.class)

            .addMixIn(AggregateRootMirror.class, AggregateRootMirrorMixinJackson3.class)
            .addMixIn(AggregateRootModel.class, AggregateRootModelMixinJackson3.class)

            .addMixIn(AggregateRootReferenceMirror.class, AggregateRootReferenceMirrorMixinJackson3.class)
            .addMixIn(AggregateRootReferenceModel.class, AggregateRootReferenceModelMixinJackson3.class)

            .addMixIn(ApplicationServiceMirror.class, ApplicationServiceMirrorMixinJackson3.class)
            .addMixIn(ApplicationServiceModel.class, ApplicationServiceModelMixinJackson3.class)

            .addMixIn(AssertedContainableTypeMirror.class, AssertedContainableTypeMirrorMixinJackson3.class)
            .addMixIn(AssertedContainableTypeModel.class, AssertedContainableTypeModelMixinJackson3.class)

            .addMixIn(AssertionMirror.class, AssertionMirrorMixinJackson3.class)
            .addMixIn(AssertionModel.class, AssertionModelMixinJackson3.class)

            .addMixIn(BoundedContextMirror.class, BoundedContextMirrorMixinJackson3.class)
            .addMixIn(BoundedContextModel.class, BoundedContextModelMixinJackson3.class)

            .addMixIn(DomainCommandMirror.class, DomainCommandMirrorMixinJackson3.class)
            .addMixIn(DomainCommandModel.class, DomainCommandModelMixinJackson3.class)

            .addMixIn(DomainEventMirror.class, DomainEventMirrorMixinJackson3.class)
            .addMixIn(DomainEventModel.class, DomainEventModelMixinJackson3.class)

            .addMixIn(DomainMirror.class, DomainMirrorMixinJackson3.class)
            .addMixIn(DomainModel.class, DomainModelMixinJackson3.class)

            .addMixIn(DomainObjectMirror.class, DomainObjectMirrorMixinJackson3.class)
            .addMixIn(DomainObjectModel.class, DomainObjectModelMixinJackson3.class)

            .addMixIn(DomainServiceMirror.class, DomainServiceMirrorMixinJackson3.class)
            .addMixIn(DomainServiceModel.class, DomainServiceModelMixinJackson3.class)

            .addMixIn(DomainTypeMirror.class, DomainTypeMirrorMixinJackson3.class)
            .addMixIn(DomainTypeModel.class, DomainTypeModelMixinJackson3.class)

            .addMixIn(EntityMirror.class, EntityMirrorMixinJackson3.class)
            .addMixIn(EntityModel.class, EntityModelMixinJackson3.class)

            .addMixIn(EntityReferenceMirror.class, EntityReferenceMirrorMixinJackson3.class)
            .addMixIn(EntityReferenceModel.class, EntityReferenceModelMixinJackson3.class)

            .addMixIn(EnumMirror.class, EnumMirrorMixinJackson3.class)
            .addMixIn(EnumModel.class, EnumModelMixinJackson3.class)

            .addMixIn(EnumOptionMirror.class, EnumOptionMirrorMixinJackson3.class)
            .addMixIn(EnumOptionModel.class, EnumOptionModelMixinJackson3.class)

            .addMixIn(FieldMirror.class, FieldMirrorMixinJackson3.class)
            .addMixIn(FieldModel.class, FieldModelMixinJackson3.class)

            .addMixIn(IdentityMirror.class, IdentityMirrorMixinJackson3.class)
            .addMixIn(IdentityModel.class, IdentityModelMixinJackson3.class)

            .addMixIn(MethodMirror.class, MethodMirrorMixinJackson3.class)
            .addMixIn(MethodModel.class, MethodModelMixinJackson3.class)

            .addMixIn(OutboundServiceMirror.class, OutboundServiceMirrorMixinJackson3.class)
            .addMixIn(OutboundServiceModel.class, OutboundServiceModelMixinJackson3.class)

            .addMixIn(ParamMirror.class, ParamMirrorMixinJackson3.class)
            .addMixIn(ParamModel.class, ParamModelMixinJackson3.class)

            .addMixIn(QueryHandlerMirror.class, QueryHandlerMirrorMixinJackson3.class)
            .addMixIn(QueryHandlerModel.class, QueryHandlerModelMixinJackson3.class)

            .addMixIn(ReadModelMirror.class, ReadModelMirrorMixinJackson3.class)
            .addMixIn(ReadModelModel.class, ReadModelModelMixinJackson3.class)

            .addMixIn(ResolvedGenericTypeMirror.class, ResolvedGenericTypeMirrorMixinJackson3.class)

            .addMixIn(ServiceKindMirror.class, ServiceKindMirrorMixinJackson3.class)
            .addMixIn(ServiceKindModel.class, ServiceKindModelMixinJackson3.class)

            .addMixIn(ValueObjectMirror.class, ValueObjectMirrorMixinJackson3.class)
            .addMixIn(ValueObjectModel.class, ValueObjectModelMixinJackson3.class)

            .addMixIn(ValueReferenceMirror.class, ValueReferenceMirrorMixinJackson3.class)
            .addMixIn(ValueReferenceModel.class, ValueReferenceModelMixinJackson3.class)

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
