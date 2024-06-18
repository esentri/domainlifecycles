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

package io.domainlifecycles.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.api.MappingAction;
import io.domainlifecycles.jackson.databind.context.DomainObjectMappingContext;
import io.domainlifecycles.jackson.exception.DLCJacksonException;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.jackson.databind.context.DomainObjectMappingContextHolder;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * {@link Domain} based deserialization of {@link Entity} instances.
 * @see StdDeserializer
 *
 * @author Mario Herb
 */
public class EntityDeserializer extends StdDeserializer<Entity<?>> {

    private final DlcJacksonModule.CustomizerContainer customizerContainer;
    private final EntityMirror entityMirror;
    private final JacksonMappingCustomizer<?> customizer;
    private final DomainObjectBuilderProvider domainObjectBuilderProvider;
    private final EntityIdentityProvider entityIdentityProvider;


    public EntityDeserializer(JavaType valueType,
                              DlcJacksonModule.CustomizerContainer customizerContainer,
                              DomainObjectBuilderProvider domainObjectBuilderProvider,
                              EntityIdentityProvider entityIdentityProvider) {
        super(valueType);
        this.domainObjectBuilderProvider = domainObjectBuilderProvider;
        this.customizerContainer = customizerContainer;
        this.entityIdentityProvider = entityIdentityProvider;
        this.customizer = customizerContainer.findCustomizer(this._valueType.getRawClass());
        entityMirror = Domain.entityMirrorFor(this._valueType.getRawClass().getName());
    }

    /**
     * Deserialize Entities
     *
     * @param jsonParser Parsed used for reading JSON content
     * @param deserializationContext Context that can be used to access information about
     *   this deserialization activity.
     *
     * @throws IOException if parsing fails
     */
    @Override
    public Entity<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        if (Entity.class.isAssignableFrom(this._valueType.getRawClass()) && !node.isNull()) {
            injectIds((ObjectNode) node);
        }
        DomainObjectMappingContext mappingContext = DomainObjectMappingContextHolder.getContext(
            node,
            this._valueType.getRawClass().getName(),
            deserializationContext,
            domainObjectBuilderProvider
        );


        if (customizer != null) {
            MappingAction mappingAction = customizer.beforeObjectRead(mappingContext, jsonParser.getCodec());
            if (MappingAction.SKIP_DEFAULT_ACTION.equals(mappingAction)) {
                return buildWithExceptionHandling(mappingContext);
            }
        }
        return deserializeEntityNode(mappingContext, jsonParser.getCodec(), deserializationContext);
    }

    private Entity<?> deserializeEntityNode(DomainObjectMappingContext mappingContext,
                                            ObjectCodec codec,
                                            DeserializationContext deserializationContext) {

        if(entityMirror.getIdentityField().isPresent()){
            var idField = entityMirror.getIdentityField().get();
            String fieldName = idField.getName();
            var fieldTypeName = idField.getType().getTypeName();
            var fieldNode = mappingContext.contextNode.get(fieldName);

            readAndSetEntityField(
                mappingContext,
                fieldNode,
                fieldName,
                fieldTypeName,
                codec,
                deserializationContext
            );
        }

        entityMirror.getBasicFields().stream().filter(fm -> ! fm.isStatic()).forEach(fm -> {
            if (mappingContext.contextNode == null) {
                throw DLCJacksonException.fail("Node missing in mapping context!");
            }

            String fieldTypeName = fm.getType().getTypeName();
            String fieldName = fm.getName();
            TreeNode fieldNode = mappingContext.contextNode.get(fieldName);

            readAndSetEntityField(
                mappingContext,
                fieldNode,
                fieldName,
                fieldTypeName,
                codec,
                deserializationContext
            );
        });

        entityMirror.getValueReferences()
            .stream()
            .filter(vrm -> !vrm.isStatic())
            .forEach(vrm -> {
                TreeNode valueObjectCompositionNode = mappingContext.contextNode.get(vrm.getName());
                if (vrm.getType().hasCollectionContainer()) {
                    if (valueObjectCompositionNode != null && valueObjectCompositionNode.isArray()) {
                        mappingContext.domainObjectBuilder.setFieldValue(mappingContext.domainObjectBuilder
                            .newCollectionInstanceForField(vrm.getName()), vrm.getName());
                        for (int i = 0; i < valueObjectCompositionNode.size(); i++) {
                            TreeNode valueObjectNode = valueObjectCompositionNode.get(i);
                            readAndSetEntityField(
                                mappingContext,
                                valueObjectNode,
                                vrm.getName(),
                                vrm.getType().getTypeName(),
                                codec,
                                deserializationContext
                            );
                        }
                    }
                } else {
                    readAndSetEntityField(
                        mappingContext,
                        valueObjectCompositionNode,
                        vrm.getName(),
                        vrm.getType().getTypeName(),
                        codec, deserializationContext
                    );
                }
            }
        );

        entityMirror.getEntityReferences()
            .forEach(erm -> {
                TreeNode entityReferenceNode = mappingContext.contextNode.get(erm.getName());
                if (erm.getType().hasCollectionContainer()) {
                    if (entityReferenceNode != null && entityReferenceNode.isArray()) {
                        mappingContext.domainObjectBuilder.setFieldValue(mappingContext.domainObjectBuilder
                            .newCollectionInstanceForField(erm.getName()), erm.getName());
                        for (int i = 0; i < entityReferenceNode.size(); i++) {
                            TreeNode innerEntityNode = entityReferenceNode.get(i);
                            readAndSetEntityField(
                                mappingContext,
                                innerEntityNode,
                                erm.getName(),
                                erm.getType().getTypeName(),
                                codec,
                                deserializationContext);
                        }
                    }
                } else {
                    readAndSetEntityField(
                        mappingContext,
                        entityReferenceNode,
                        erm.getName(),
                        erm.getType().getTypeName(),
                        codec,
                        deserializationContext);
                }
            }
        );

        if (customizer != null) {
            customizer.afterObjectRead(mappingContext, codec);
        }
        return buildWithExceptionHandling(mappingContext);
    }

    private void readAndSetEntityField(DomainObjectMappingContext mappingContext,
                                       TreeNode fieldNode,
                                       String fieldName,
                                       String fieldTypeName,
                                       ObjectCodec codec,
                                       DeserializationContext deserializationContext
    ) {
        if (fieldNode != null) {
            Class<?> fieldType = DlcAccess.getClassForName(fieldTypeName);
            MappingAction mappingAction = MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
            if (customizer != null) {
                mappingAction = customizer.beforeFieldRead(
                    mappingContext,
                    fieldNode,
                    fieldName,
                    fieldType,
                    codec);
            }
            if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
                try {
                    Object value;
                    if (ValueObject.class.isAssignableFrom(fieldType)
                        || Entity.class.isAssignableFrom(fieldType)) {
                        var p = fieldNode.traverse(codec);
                        value = deserializationContext.readValue(p, fieldType);
                    } else {
                        value = fieldNode.traverse(codec).readValueAs(fieldType);
                    }
                    if (customizer != null) {
                        mappingAction = customizer.afterFieldRead(
                            mappingContext,
                            value,
                            fieldName,
                            fieldType);
                    }
                    if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
                        if (mappingContext.domainObjectBuilder.canInstantiateField(fieldName)) {
                            if (value instanceof Optional opt) {
                                if (opt.isPresent()) {
                                    value = opt.get();
                                    if (!fieldType.isAssignableFrom(value.getClass())) {
                                        value = codec.treeToValue(fieldNode, fieldType);
                                    }
                                } else {
                                    value = null;
                                }
                            }
                            var fm = entityMirror.fieldByName(fieldName);
                            if(fm.getType().hasCollectionContainer()) {
                                mappingContext.domainObjectBuilder.addValueToCollection(value, fieldName);
                            }else{
                                mappingContext.domainObjectBuilder.setFieldValue(value, fieldName);
                            }
                        }
                    }
                } catch (IOException e) {
                    throw DLCJacksonException.fail("Not able to read and set field '%s' for '%s' !", e, fieldName, fieldTypeName);
                }
            }
        }
    }

    private Entity<?> buildWithExceptionHandling(DomainObjectMappingContext mappingContext) {
        try {
            if (!mappingContext.contextNode.isNull()){
                return (Entity<?>) mappingContext.domainObjectBuilder.build();
            } else {
                return null;
            }
        } catch (Exception ex) {
            if (ex.getCause() != null && ex.getCause() instanceof InvocationTargetException iex) {
                if (iex.getCause() != null && iex.getCause() instanceof DomainAssertionException) {
                    throw (DomainAssertionException) iex.getCause();
                }
            }
            throw ex;
        }
    }

    public void injectIds(ObjectNode objectNode) {
        injectIds(objectNode,  this._valueType.getRawClass().getName());
    }

    private void injectIds(ObjectNode objectNode, String targetEntityTypeName) {

        EntityMirror em = Domain.entityMirrorFor(targetEntityTypeName);

        if(em.getIdentityField().isPresent()) {
            var idFieldName = em.getIdentityField().get().getName();
            JsonNode idNode = objectNode.get(idFieldName);
            if (idNode == null || idNode.isNull()) {
                Identity<?> identity = entityIdentityProvider.provideFor(targetEntityTypeName);
                objectNode.set(idFieldName, JsonNodeFactory.instance.textNode(identity.value().toString()));
            }
        }

        em.getEntityReferences().forEach(
            er -> {
                JsonNode j = objectNode.get(er.getName());
                if (j != null && !j.isNull() && !j.isEmpty()) {
                    if (!er.getType().hasCollectionContainer()) {
                        ObjectNode associationNode = (ObjectNode) j;
                        injectIds(associationNode, er.getType().getTypeName());
                    } else {
                        if (j.isArray()) {
                            ArrayNode an = (ArrayNode) j;
                            an.forEach(en -> injectIds((ObjectNode) en, er.getType().getTypeName()));
                        }
                    }
                }
            }
        );
    }

}
