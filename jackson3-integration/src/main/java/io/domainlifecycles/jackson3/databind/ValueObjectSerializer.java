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

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.jackson3.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson3.api.MappingAction;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

/**
 * {@link Domain} based serialization of {@link ValueObject} instances.
 *
 * @author Leon Völlinger
 * @author Mario Herb
 * @see StdSerializer
 */
public class ValueObjectSerializer extends StdSerializer<ValueObject> {

    /**
     * A customizer used to modify the JSON mapping process for {@link ValueObject} instances.
     * <p>
     * This field holds an instance of {@link JacksonMappingCustomizer}, which provides
     * hooks for customizing the behavior during JSON serialization and deserialization
     * of {@link ValueObject} types. The customizer defines various entry points
     * for intervening in the mapping process before and after reading or writing
     * JSON fields and objects.
     */
    private final JacksonMappingCustomizer<ValueObject> customizer;

    /**
     * Constructs a ValueObjectSerializer with the provided customizer.
     *
     * @param customizer The customizer used to customize the mapping process for ValueObject instances.
     */
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
     * @throws JacksonException  if serialization failed
     */
    @Override
    public void serialize(ValueObject valueObject, JsonGenerator jsonGenerator,
                          SerializationContext serializerProvider) throws JacksonException {
        MappingAction mappingAction = MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
        if (customizer != null) {
            mappingAction = customizer.beforeObjectWrite(jsonGenerator, valueObject);
        }
        if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
            var valueObjectMirror = Domain.valueObjectMirrorFor(valueObject);
            if (valueObjectMirror.isSingledValued() && !jsonGenerator.streamWriteContext().inRoot()) {
                Object value = DlcAccess.accessorFor(valueObject).peek(
                    valueObjectMirror.singledValuedField().get().getName());
                jsonGenerator.writePOJO(value);
            } else {
                jsonGenerator.writeStartObject();
                writeBasicFields(jsonGenerator, valueObjectMirror, valueObject);
                writeValues(jsonGenerator, valueObjectMirror, valueObject);
                jsonGenerator.writeEndObject();
            }
        }
    }

    private void writeBasicFields(JsonGenerator jsonGenerator, ValueObjectMirror valueObjectMirror,
                                  ValueObject valueObject) throws JacksonException {
        for (FieldMirror field : valueObjectMirror.getBasicFields()) {
            if (!field.isStatic() && field.isPublicReadable()) {
                Object toWrite = DlcAccess.accessorFor(valueObject).peek(field.getName());
                writeCustomized(jsonGenerator, field.getName(), toWrite);
            }
        }

    }

    private void writeValues(JsonGenerator jsonGenerator, ValueObjectMirror valueObjectMirror,
                             ValueObject valueObject) throws JacksonException {
        for (FieldMirror field : valueObjectMirror.getValueReferences()) {
            if (field.isPublicReadable() && !field.isStatic()) {
                Object toWrite = DlcAccess.accessorFor(valueObject).peek(field.getName());
                writeCustomized(jsonGenerator, field.getName(), toWrite);
            }
        }
    }

    private void writeCustomized(JsonGenerator jsonGenerator, String fieldName, Object fieldValue) throws JacksonException {
        MappingAction mappingAction = MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
        if (customizer != null) {
            mappingAction = customizer.beforeFieldWrite(jsonGenerator, fieldName, fieldValue);
        }
        if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
            jsonGenerator.writeName(fieldName);
            jsonGenerator.writePOJO(fieldValue);
        }
    }
}
