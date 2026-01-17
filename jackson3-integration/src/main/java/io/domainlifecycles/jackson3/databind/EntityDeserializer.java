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

package io.domainlifecycles.jackson3.databind;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jackson3.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson3.api.MappingAction;
import io.domainlifecycles.jackson3.databind.context.DomainObjectMappingContext;
import io.domainlifecycles.jackson3.databind.context.DomainObjectMappingContextHolder;
import io.domainlifecycles.jackson3.exception.DLCJacksonException;
import io.domainlifecycles.jackson3.module.DlcJacksonModule;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * {@link Domain} based deserialization of {@link Entity} instances.
 *
 * @author Mario Herb
 * @see StdDeserializer
 */
public class EntityDeserializer extends StdDeserializer<Entity<?>> {

    /**
     * A container that manages customizers for domain object serialization
     * and deserialization processes.
     *
     * This field is used to store and access instances of {@code DlcJacksonModule.CustomizerContainer},
     * which map domain object types to their associated {@link JacksonMappingCustomizer} instances.
     * It facilitates the customization of the deserialization process based on specific domain
     * requirements.
     */
    private final DlcJacksonModule.CustomizerContainer customizerContainer;
    /**
     * Represents the {@link EntityMirror} associated with the deserialization process in the {@code EntityDeserializer}.
     * This instance provides metadata and reflective capabilities for interacting with {@link Entity} instances.
     *
     * The {@code entityMirror} is used to access and manipulate information about the structure and relationships
     * of the entity being deserialized, such as identity fields, concurrency version fields, and entity references.
     */
    private final EntityMirror entityMirror;
    /**
     * Represents a customizer used to modify the mapping process of {@link DomainObject}s
     * within the deserialization context. This field is utilized to apply specific
     * customizations or behaviors during the mapping process.
     *
     * The customizer interacts with the mapping process to accommodate specific needs
     * for certain types of domain objects, as defined by the logic implemented
     * in its respective methods.
     *
     * It is an immutable, thread-safe reference to ensure consistent behavior
     * during the deserialization process.
     */
    private final JacksonMappingCustomizer<?> customizer;
    /**
     * Provider for obtaining {@link DomainObjectBuilder} instances, facilitating the creation
     * and configuration of domain objects during deserialization processes.
     * Used within the {@code EntityDeserializer} to manage domain object construction
     * based on provided metadata or JSON input.
     */
    private final DomainObjectBuilderProvider domainObjectBuilderProvider;
    /**
     * Provides access to an {@link EntityIdentityProvider} instance for managing
     * identity information of entities during deserialization.
     *
     * This field is utilized within the deserialization process of the containing class
     * to ensure entities are correctly identified based on their specific identity
     * characteristics. It integrates with the domain model and identity mechanisms
     * to enable accurate mapping and reconstruction of entity objects from JSON.
     *
     * The {@code entityIdentityProvider} is immutable, ensuring consistency and
     * reliability throughout the lifecycle of the deserialization process.
     */
    private final EntityIdentityProvider entityIdentityProvider;


    /**
     * Initializes a new EntityDeserializer with provided parameters.
     *
     * @param valueType                The JavaType of the entity to be deserialized
     * @param customizerContainer      The container for customizers
     * @param domainObjectBuilderProvider The provider for DomainObjectBuilders
     * @param entityIdentityProvider   The provider for entity identity information
     */
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
     * @param jsonParser             Parsed used for reading JSON content
     * @param deserializationContext Context that can be used to access information about
     *                               this deserialization activity.
     * @throws JacksonException if parsing fails
     */
    @Override
    public Entity<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
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
            MappingAction mappingAction = customizer.beforeObjectRead(mappingContext, jsonParser.objectReadContext());
            if (MappingAction.SKIP_DEFAULT_ACTION.equals(mappingAction)) {
                return buildWithExceptionHandling(mappingContext);
            }
        }
        return deserializeEntityNode(mappingContext, jsonParser.objectReadContext(), deserializationContext);
    }

    private Entity<?> deserializeEntityNode(DomainObjectMappingContext mappingContext,
                                            ObjectReadContext readContext,
                                            DeserializationContext deserializationContext) {

        if (entityMirror.getIdentityField().isPresent()) {
            var idField = entityMirror.getIdentityField().get();
            String fieldName = idField.getName();
            var fieldTypeName = idField.getType().getTypeName();
            var fieldNode = mappingContext.contextNode.get(fieldName);

            readAndSetEntityField(
                mappingContext,
                fieldNode,
                fieldName,
                fieldTypeName,
                readContext,
                deserializationContext
            );
        }

        entityMirror.getBasicFields().stream().filter(fm -> !fm.isStatic()).forEach(fm -> {
            if (mappingContext.contextNode == null) {
                throw DLCJacksonException.fail("Node missing in mapping context!");
            }

            String fieldTypeName = fm.getType().getTypeName();
            String fieldName = fm.getName();
            JsonNode fieldNode = mappingContext.contextNode.get(fieldName);

            readAndSetEntityField(
                mappingContext,
                fieldNode,
                fieldName,
                fieldTypeName,
                readContext,
                deserializationContext
            );
        });

        entityMirror.getValueReferences()
            .stream()
            .filter(vrm -> !vrm.isStatic())
            .forEach(vrm -> {
                    JsonNode valueObjectCompositionNode = mappingContext.contextNode.get(vrm.getName());
                    if (vrm.getType().hasCollectionContainer()) {
                        if (valueObjectCompositionNode != null && valueObjectCompositionNode.isArray()) {
                            mappingContext.domainObjectBuilder.setFieldValue(mappingContext.domainObjectBuilder
                                .newCollectionInstanceForField(vrm.getName()), vrm.getName());
                            for (int i = 0; i < valueObjectCompositionNode.size(); i++) {
                                JsonNode valueObjectNode = valueObjectCompositionNode.get(i);
                                readAndSetEntityField(
                                    mappingContext,
                                    valueObjectNode,
                                    vrm.getName(),
                                    vrm.getType().getTypeName(),
                                    readContext,
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
                            readContext, deserializationContext
                        );
                    }
                }
            );

        entityMirror.getEntityReferences()
            .forEach(erm -> {
                    JsonNode entityReferenceNode = mappingContext.contextNode.get(erm.getName());
                    if (erm.getType().hasCollectionContainer()) {
                        if (entityReferenceNode != null && entityReferenceNode.isArray()) {
                            mappingContext.domainObjectBuilder.setFieldValue(mappingContext.domainObjectBuilder
                                .newCollectionInstanceForField(erm.getName()), erm.getName());
                            for (int i = 0; i < entityReferenceNode.size(); i++) {
                                JsonNode innerEntityNode = entityReferenceNode.get(i);
                                readAndSetEntityField(
                                    mappingContext,
                                    innerEntityNode,
                                    erm.getName(),
                                    erm.getType().getTypeName(),
                                    readContext,
                                    deserializationContext);
                            }
                        }
                    } else {
                        readAndSetEntityField(
                            mappingContext,
                            entityReferenceNode,
                            erm.getName(),
                            erm.getType().getTypeName(),
                            readContext,
                            deserializationContext);
                    }
                }
            );

        if (customizer != null) {
            customizer.afterObjectRead(mappingContext, readContext);
        }
        return buildWithExceptionHandling(mappingContext);
    }

    private void readAndSetEntityField(DomainObjectMappingContext mappingContext,
                                       JsonNode jsonNode,
                                       String fieldName,
                                       String fieldTypeName,
                                       ObjectReadContext readContext,
                                       DeserializationContext deserializationContext
    ) {
        if (jsonNode != null) {
            Class<?> fieldType = DlcAccess.getClassForName(fieldTypeName);
            MappingAction mappingAction = MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
            if (customizer != null) {
                mappingAction = customizer.beforeFieldRead(
                    mappingContext,
                    jsonNode,
                    fieldName,
                    fieldType,
                    readContext);
            }
            if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
                try {
                    Object value;
                    if (ValueObject.class.isAssignableFrom(fieldType)
                        || Entity.class.isAssignableFrom(fieldType)) {
                        var p = jsonNode.traverse(readContext);
                        value = deserializationContext.readValue(p, fieldType);
                    } else {
                        value = jsonNode.traverse(readContext).readValueAs(fieldType);
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
                            if (value instanceof Optional<?> opt) {
                                if (opt.isPresent()) {
                                    value = opt.get();
                                    if (!fieldType.isAssignableFrom(value.getClass())) {
                                        value = deserializationContext.readTreeAsValue(jsonNode, fieldType);
                                    }
                                } else {
                                    value = null;
                                }
                            }
                            var fm = entityMirror.fieldByName(fieldName);
                            if (fm.getType().hasCollectionContainer()) {
                                mappingContext.domainObjectBuilder.addValueToCollection(value, fieldName);
                            } else {
                                mappingContext.domainObjectBuilder.setFieldValue(value, fieldName);
                            }
                        }
                    }
                } catch (JacksonException e) {
                    throw DLCJacksonException.fail("Not able to read and set field '%s' for '%s' !", e, fieldName,
                        fieldTypeName);
                }
            }
        }
    }

    private Entity<?> buildWithExceptionHandling(DomainObjectMappingContext mappingContext) {
        try {
            if (!mappingContext.contextNode.isNull()) {
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

    /**
     * Injects identifier fields into the provided ObjectNode for the entity's corresponding type.
     * This method ensures that IDs for associated entities and collections are also recursively injected.
     *
     * @param objectNode the ObjectNode representing the JSON structure of the entity.
     */
    public void injectIds(ObjectNode objectNode) {
        injectIds(objectNode, this._valueType.getRawClass().getName());
    }

    private void injectIds(ObjectNode objectNode, String targetEntityTypeName) {

        EntityMirror em = Domain.entityMirrorFor(targetEntityTypeName);

        if (em.getIdentityField().isPresent()) {
            var idFieldName = em.getIdentityField().get().getName();
            JsonNode idNode = objectNode.get(idFieldName);
            if(entityIdentityProvider != null) {
                if (idNode == null || idNode.isNull()) {
                    Identity<?> identity = entityIdentityProvider.provideFor(targetEntityTypeName);
                    objectNode.set(idFieldName, JsonNodeFactory.instance.stringNode(identity.value().toString()));
                }
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
