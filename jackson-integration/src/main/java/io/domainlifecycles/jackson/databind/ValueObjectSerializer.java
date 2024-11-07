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

package io.domainlifecycles.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.api.MappingAction;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;

import java.io.IOException;

/**
 * {@link Domain} based serialization of {@link ValueObject} instances.
 *
 * @author Mario Herb
 * @see StdSerializer
 */
public class ValueObjectSerializer extends StdSerializer<ValueObject> {

    private final JacksonMappingCustomizer<ValueObject> customizer;

    public ValueObjectSerializer(JacksonMappingCustomizer<ValueObject> customizer) {
        super(ValueObject.class);
        this.customizer = customizer;
    }

    /**
     * Serialize ValueObjects
     *
     * @param valueObject        Value to serialize; can <b>not</b> be null.
     * @param jsonGenerator      Generator used to output resulting Json content
     * @param serializerProvider Provider that can be used to get serializers for
     *                           serializing Objects value contains, if any.
     * @throws IOException if serialization failed
     */
    @Override
    public void serialize(ValueObject valueObject, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        MappingAction mappingAction = MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
        if (customizer != null) {
            mappingAction = customizer.beforeObjectWrite(jsonGenerator, valueObject);
        }
        if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
            var valueObjectMirror = Domain.valueObjectMirrorFor(valueObject);
            if (valueObjectMirror.isSingledValued() && !jsonGenerator.getOutputContext().inRoot()) {
                Object value = DlcAccess.accessorFor(valueObject).peek(
                    valueObjectMirror.singledValuedField().get().getName());
                jsonGenerator.writeObject(value);
            } else {
                jsonGenerator.writeStartObject();
                writeBasicFields(jsonGenerator, valueObjectMirror, valueObject);
                writeValues(jsonGenerator, valueObjectMirror, valueObject);
                jsonGenerator.writeEndObject();
            }
        }
    }

    private void writeBasicFields(JsonGenerator jsonGenerator, ValueObjectMirror valueObjectMirror,
                                  ValueObject valueObject) throws IOException {
        for (FieldMirror field : valueObjectMirror.getBasicFields()) {
            if (!field.isStatic() && field.isPublicReadable()) {
                Object toWrite = DlcAccess.accessorFor(valueObject).peek(field.getName());
                writeCustomized(jsonGenerator, field.getName(), toWrite);
            }
        }

    }

    private void writeValues(JsonGenerator jsonGenerator, ValueObjectMirror valueObjectMirror,
                             ValueObject valueObject) throws IOException {
        for (FieldMirror field : valueObjectMirror.getValueReferences()) {
            if (field.isPublicReadable() && !field.isStatic()) {
                Object toWrite = DlcAccess.accessorFor(valueObject).peek(field.getName());
                writeCustomized(jsonGenerator, field.getName(), toWrite);
            }
        }
    }

    private void writeCustomized(JsonGenerator jsonGenerator, String fieldName, Object fieldValue) throws IOException {
        MappingAction mappingAction = MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
        if (customizer != null) {
            mappingAction = customizer.beforeFieldWrite(jsonGenerator, fieldName, fieldValue);
        }
        if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
            jsonGenerator.writeFieldName(fieldName);
            jsonGenerator.writeObject(fieldValue);
        }
    }
}
