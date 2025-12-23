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

package io.domainlifecycles.jackson3.databind;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.core.TreeNode;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.jackson3.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson3.api.MappingAction;
import io.domainlifecycles.jackson3.databind.context.DomainObjectMappingContext;
import io.domainlifecycles.jackson3.databind.context.DomainObjectMappingContextHolder;
import io.domainlifecycles.jackson3.exception.DLCJacksonException;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.ValueObjectMirror;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * {@link Domain} based deserialization of {@link ValueObject} instances.
 *
 * @author Leon Völlinger
 * @author Mario Herb
 * @see StdDeserializer
 */
public class ValueObjectDeserializer extends StdDeserializer<ValueObject> {

    /**
     * A JacksonMappingCustomizer instance used for customizing the mapping process
     * during the deserialization or serialization of DomainObjects. This variable
     * plays a key role in altering default behavior by providing hooks for various
     * stages of the JSON-to-object and object-to-JSON transformation processes.
     *
     * Defined as a final variable, it guarantees immutability within this
     * class's context once assigned during instantiation.
     */
    private final JacksonMappingCustomizer<?> customizer;

    /**
     * A provider used to obtain {@link DomainObjectBuilder} instances for constructing domain objects.
     * This field holds an implementation of {@link DomainObjectBuilderProvider} that facilitates the
     * creation of domain objects during the deserialization process.
     */
    private final DomainObjectBuilderProvider domainObjectBuilderProvider;


    /**
     * This constructor creates a ValueObjectDeserializer object.
     *
     * @param valueType the JavaType of the value to be deserialized
     * @param customizer the JacksonMappingCustomizer used for customizing the mapping process
     * @param domainObjectBuilderProvider the DomainObjectBuilderProvider used to obtain DomainObjectBuilders
     */
    public ValueObjectDeserializer(JavaType valueType,
                                   JacksonMappingCustomizer<?> customizer,
                                   DomainObjectBuilderProvider domainObjectBuilderProvider) {
        super(valueType);
        this.customizer = customizer;
        this.domainObjectBuilderProvider = domainObjectBuilderProvider;
    }

    /**
     * Deserialize ValueObjects.
     *
     * @param jsonParser             Parsed used for reading JSON content
     * @param deserializationContext Context that can be used to access information about
     *                               this deserialization activity.
     * @throws JacksonException if deserialization failed
     */
    @Override
    public ValueObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
        JsonNode node = jsonParser.readValueAsTree();
        DomainObjectMappingContext mappingContext = DomainObjectMappingContextHolder.getContext(node,
            this._valueType.getRawClass().getName(), deserializationContext, domainObjectBuilderProvider);
        if (customizer != null) {
            MappingAction mappingAction = customizer.beforeObjectRead(mappingContext, jsonParser.objectReadContext());
            if (MappingAction.SKIP_DEFAULT_ACTION.equals(mappingAction)) {
                return buildWithExceptionHandling(mappingContext);
            }
        }
        return readValueObject(
            mappingContext,
            jsonParser.objectReadContext(),
            deserializationContext);

    }

    private void readAndSetValueObjectField(DomainObjectMappingContext mappingContext,
                                            JsonNode jsonNode,
                                            String fieldName,
                                            String fieldTypeName,
                                            ObjectReadContext readContext,
                                            DeserializationContext deserializationContext

    ) {
        if (jsonNode != null) {
            Class<?> fieldType = DlcAccess.getClassForName(fieldTypeName);
            try {
                MappingAction mappingAction = MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
                if (customizer != null) {
                    mappingAction = customizer.beforeFieldRead(mappingContext, jsonNode, fieldName, fieldType, readContext);
                }
                if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
                    Object value;
                    if (ValueObject.class.isAssignableFrom(fieldType) || Entity.class.isAssignableFrom(fieldType)) {
                        var p = jsonNode.traverse(readContext);
                        value = deserializationContext.readValue(p, fieldType);
                    } else {
                        value = jsonNode.traverse(readContext).readValueAs(fieldType);
                    }
                    if (mappingContext.domainObjectBuilder.canInstantiateField(fieldName)) {
                        if (customizer != null) {
                            mappingAction = customizer.afterFieldRead(mappingContext, value, fieldName, fieldType);
                        }
                        if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
                            var vm = Domain.valueObjectMirrorFor(_valueClass.getName());
                            var fm = vm.fieldByName(fieldName);
                            if (value instanceof Optional opt) {
                                if (opt.isPresent()) {
                                    value = opt.get();
                                    if (!fm.getType().getTypeName().equals(value.getClass().getName())) {
                                        value = deserializationContext.readTreeAsValue(jsonNode, fieldType);
                                    }
                                } else {
                                    value = null;
                                }
                            }
                            if (fm.getType().hasCollectionContainer()) {
                                mappingContext.domainObjectBuilder.addValueToCollection(value, fieldName);
                            } else {
                                mappingContext.domainObjectBuilder.setFieldValue(value, fieldName);
                            }
                        }
                    }
                }
            } catch (JacksonException e) {
                throw DLCJacksonException.fail("Not able to read and set field '%s' for '%s'!", e, fieldName,
                    this._valueType.getRawClass().getName());
            }
        }
    }

    private ValueObject readValueObject(DomainObjectMappingContext mappingContext,
                                        ObjectReadContext readContext,
                                        DeserializationContext deserializationContext
    ) {
        if (mappingContext.contextNode != null) {
            ValueObjectMirror valueObjectMirror = Domain.valueObjectMirrorFor(this._valueType.getRawClass().getName());
            var singleValueFieldMirror = valueObjectMirror.singledValuedField();
            if (singleValueFieldMirror.isPresent()) {
                JsonNode myNode = mappingContext.contextNode;
                var fieldMirror = singleValueFieldMirror.get();
                if (myNode.get(fieldMirror.getName()) != null) {
                    myNode = myNode.get(fieldMirror.getName());
                }
                if (myNode.isValueNode()) {
                    readAndSetValueObjectField(
                        mappingContext,
                        myNode,
                        fieldMirror.getName(),
                        fieldMirror.getType().getTypeName(),
                        readContext,
                        deserializationContext
                    );
                    if (mappingContext.domainObjectBuilder.getFieldValue(fieldMirror.getName()) == null) {
                        return null;
                    }
                    return buildWithExceptionHandling(mappingContext);
                }
            }

            valueObjectMirror.getBasicFields()
                .stream()
                .filter(fm -> !fm.isStatic())
                .forEach(fm -> {
                        JsonNode fieldNode = mappingContext.contextNode.get(fm.getName());
                        readAndSetValueObjectField(
                            mappingContext,
                            fieldNode,
                            fm.getName(),
                            fm.getType().getTypeName(),
                            readContext,
                            deserializationContext
                        );
                    }
                );

            valueObjectMirror.getValueReferences()
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
                                    readAndSetValueObjectField(
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
                            readAndSetValueObjectField(
                                mappingContext,
                                valueObjectCompositionNode,
                                vrm.getName(),
                                vrm.getType().getTypeName(),
                                readContext,
                                deserializationContext
                            );
                        }
                    }
                );

            return buildWithExceptionHandling(mappingContext);
        }
        return null;
    }


    private ValueObject buildWithExceptionHandling(DomainObjectMappingContext mappingContext) {
        try {
            if (!mappingContext.contextNode.isNull()) {
                return (ValueObject) mappingContext.domainObjectBuilder.build();
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

}
