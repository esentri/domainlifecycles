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
import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.api.MappingAction;
import io.domainlifecycles.jackson.databind.context.DomainObjectMappingContext;
import io.domainlifecycles.jackson.databind.context.DomainObjectMappingContextHolder;
import io.domainlifecycles.jackson.exception.DLCJacksonException;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.ValueObjectMirror;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * {@link Domain} based deserialization of {@link ValueObject} instances.
 *
 * @author Mario Herb
 * @see StdDeserializer
 */
public class ValueObjectDeserializer extends StdDeserializer<ValueObject> {

    private final JacksonMappingCustomizer<?> customizer;
    private final DomainObjectBuilderProvider domainObjectBuilderProvider;


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
     * @throws IOException if deserialization failed
     */
    @Override
    public ValueObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        DomainObjectMappingContext mappingContext = DomainObjectMappingContextHolder.getContext(node,
            this._valueType.getRawClass().getName(), deserializationContext, domainObjectBuilderProvider);
        if (customizer != null) {
            MappingAction mappingAction = customizer.beforeObjectRead(mappingContext, jsonParser.getCodec());
            if (MappingAction.SKIP_DEFAULT_ACTION.equals(mappingAction)) {
                return buildWithExceptionHandling(mappingContext);
            }
        }
        return readValueObject(
            mappingContext,
            jsonParser.getCodec(),
            deserializationContext);

    }

    private void readAndSetValueObjectField(DomainObjectMappingContext mappingContext,
                                            TreeNode fieldNode,
                                            String fieldName,
                                            String fieldTypeName,
                                            ObjectCodec codec,
                                            DeserializationContext deserializationContext

    ) {
        if (fieldNode != null) {
            Class<?> fieldType = DlcAccess.getClassForName(fieldTypeName);
            try {
                MappingAction mappingAction = MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
                if (customizer != null) {
                    mappingAction = customizer.beforeFieldRead(mappingContext, fieldNode, fieldName, fieldType, codec);
                }
                if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
                    Object value;
                    if (ValueObject.class.isAssignableFrom(fieldType) || Entity.class.isAssignableFrom(fieldType)) {
                        var p = fieldNode.traverse(codec);
                        value = deserializationContext.readValue(p, fieldType);
                    } else {
                        value = fieldNode.traverse(codec).readValueAs(fieldType);
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
                                        value = codec.treeToValue(fieldNode, fieldType);
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
            } catch (IOException e) {
                throw DLCJacksonException.fail("Not able to read and set field '%s' for '%s'!", e, fieldName,
                    this._valueType.getRawClass().getName());
            }
        }
    }

    private ValueObject readValueObject(DomainObjectMappingContext mappingContext,
                                        ObjectCodec codec,
                                        DeserializationContext deserializationContext
    ) {
        if (mappingContext.contextNode != null) {
            ValueObjectMirror valueObjectMirror = Domain.valueObjectMirrorFor(this._valueType.getRawClass().getName());
            var singleValueFieldMirror = valueObjectMirror.singledValuedField();
            if (singleValueFieldMirror.isPresent()) {
                TreeNode myNode = mappingContext.contextNode;
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
                        codec,
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
                        TreeNode fieldNode = mappingContext.contextNode.get(fm.getName());
                        readAndSetValueObjectField(
                            mappingContext,
                            fieldNode,
                            fm.getName(),
                            fm.getType().getTypeName(),
                            codec,
                            deserializationContext
                        );
                    }
                );

            valueObjectMirror.getValueReferences()
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
                                    readAndSetValueObjectField(
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
                            readAndSetValueObjectField(
                                mappingContext,
                                valueObjectCompositionNode,
                                vrm.getName(),
                                vrm.getType().getTypeName(),
                                codec,
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
