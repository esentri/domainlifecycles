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

package nitrox.dlc.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nitrox.dlc.access.DlcAccess;
import nitrox.dlc.domain.types.Entity;
import nitrox.dlc.jackson.api.JacksonMappingCustomizer;
import nitrox.dlc.jackson.api.MappingAction;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.EntityMirror;
import nitrox.dlc.mirror.api.EntityReferenceMirror;
import nitrox.dlc.mirror.api.FieldMirror;
import nitrox.dlc.mirror.api.ValueReferenceMirror;

import java.io.IOException;

/**
 * {@link Domain} based serialization of {@link Entity} instances.
 * @see StdSerializer
 *
 * @author Mario Herb
 */
@SuppressWarnings("rawtypes")
public class EntitySerializer extends StdSerializer<Entity> {

    private final JacksonMappingCustomizer<Entity> customizer;

    public EntitySerializer(JacksonMappingCustomizer<Entity> customizer) {
        super(Entity.class);
        this.customizer = customizer;
    }

    /**
     * Serialize Entities;
     *
     * @param entity Value to serialize; can <b>not</b> be null.
     * @param jsonGenerator Generator used to output resulting Json content
     * @param serializerProvider Provider that can be used to get serializers for
     *   serializing Objects value contains, if any.
     * @throws IOException if serialization fails
     */
    @Override
    public void serialize(Entity entity, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        MappingAction mappingAction = MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
        if (customizer != null) {
            mappingAction = customizer.beforeObjectWrite(jsonGenerator, entity);
        }
        if (MappingAction.CONTINUE_WITH_DEFAULT_ACTION.equals(mappingAction)) {
            var entityMirror = Domain.entityMirrorFor(entity);

            jsonGenerator.writeStartObject();
            writeIdentity(jsonGenerator, entityMirror, entity);
            writeBasicFields(jsonGenerator, entityMirror, entity);
            writeValues(jsonGenerator, entityMirror, entity);
            writeEntityReferences(jsonGenerator, entityMirror, entity);
            jsonGenerator.writeEndObject();
        }

    }

    private void writeIdentity(JsonGenerator jsonGenerator, EntityMirror entityMirror, Entity<?> entity) throws IOException{
        if(entityMirror.getIdentityField().isPresent()){
            var idField = entityMirror.getIdentityField().get();
            String fieldName = idField.getName();
            Object toWrite = DlcAccess.accessorFor(entity).peek(fieldName);
            writeCustomized(jsonGenerator, fieldName, toWrite);
        }
    }

    private void writeBasicFields(JsonGenerator jsonGenerator, EntityMirror entityMirror, Entity<?> entity) throws IOException{
        for (FieldMirror field : entityMirror.getBasicFields()) {
            if (!field.isStatic() && field.isPublicReadable()) {
                String fieldName = field.getName();
                Object toWrite = DlcAccess.accessorFor(entity).peek(fieldName);
                writeCustomized(jsonGenerator, fieldName, toWrite);
            }
        }
    }


    private void writeValues(JsonGenerator jsonGenerator, EntityMirror entityMirror, Entity<?> entity) throws IOException{
        for (ValueReferenceMirror ref : entityMirror.getValueReferences()) {
            if (ref.isPublicReadable() && !ref.isStatic()) {
                Object toWrite = DlcAccess.accessorFor(entity).peek(ref.getName());
                writeCustomized(jsonGenerator, ref.getName(), toWrite);
            }
        }
    }

    private void writeEntityReferences(JsonGenerator jsonGenerator, EntityMirror entityMirror, Entity<?> entity) throws IOException{
        for (EntityReferenceMirror ref : entityMirror.getEntityReferences()) {
            if (ref.isPublicReadable() && !ref.isStatic()) {
                Object toWrite = DlcAccess.accessorFor(entity).peek(ref.getName());
                writeCustomized(jsonGenerator, ref.getName(), toWrite);
            }
        }
    }

    private void writeCustomized(JsonGenerator jsonGenerator, String fieldName, Object fieldValue) throws IOException{
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
