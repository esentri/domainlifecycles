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

package io.domainlifecycles.swagger.v3;

import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

/**
 * Utility class providing various helper methods for working with schema objects,
 * fields, and optional types used in OpenAPI or schema-related processing.
 *
 * @author Mario Herb
 */
public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    /**
     * Creates a deep copy of the given schema, sets a new name for the copied schema,
     * and replicates all properties and attributes from the source schema.
     *
     * @param source the source Schema object to be copied
     * @param newName the new name to assign to the copied schema
     * @return a new Schema object with the same properties as the source schema, but with the specified new name
     */
    public static Schema<?> copySchema(Schema<?> source, String newName) {
        Schema<?> newSchema = new Schema<>();
        newSchema.setName(newName);
        newSchema.setType(source.getType());
        newSchema.setTypes(source.getTypes());
        newSchema.setPattern(source.getPattern());
        newSchema.setDescription(source.getDescription());
        newSchema.setFormat(source.getFormat());
        if (source.getExample() != null) {
            newSchema.setExample(source.getExample());
        }
        newSchema.setExclusiveMinimum(source.getExclusiveMinimum());
        newSchema.setExclusiveMaximum(source.getExclusiveMaximum());
        newSchema.setMinimum(source.getMinimum());
        newSchema.setMaximum(source.getMaximum());
        newSchema.setDefault(source.getDefault());
        newSchema.setMaxLength(source.getMaxLength());
        newSchema.setMinLength(source.getMinLength());
        newSchema.setJsonSchema(source.getJsonSchema());
        newSchema.setNullable(source.getNullable());
        newSchema.setReadOnly(source.getReadOnly());
        newSchema.setWriteOnly(source.getWriteOnly());
        newSchema.setDeprecated(source.getDeprecated());
        newSchema.setExternalDocs(source.getExternalDocs());
        newSchema.set$ref(source.get$ref());
        newSchema.set$schema(source.get$schema());
        newSchema.set$id(source.get$id());
        newSchema.set$anchor(source.get$anchor());
        newSchema.set$comment(source.get$comment());
        newSchema.setSpecVersion(source.getSpecVersion());
        newSchema.setExclusiveMinimumValue(source.getExclusiveMinimumValue());
        newSchema.setExclusiveMaximumValue(source.getExclusiveMaximumValue());
        newSchema.set$dynamicAnchor(source.get$dynamicAnchor());
        newSchema.set$dynamicRef(source.get$dynamicRef());
        newSchema.setAdditionalItems(source.getAdditionalItems());
        newSchema.setAdditionalProperties(source.getAdditionalProperties());
        newSchema.setAnyOf(source.getAnyOf());
        newSchema.setOneOf(source.getOneOf());
        newSchema.setNot(source.getNot());
        newSchema.setProperties(source.getProperties());
        newSchema.setRequired(source.getRequired());
        newSchema.setAllOf(source.getAllOf());
        newSchema.set$vocabulary(source.get$vocabulary());
        newSchema.setBooleanSchemaValue(source.getBooleanSchemaValue());
        newSchema.setConst(source.getConst());
        newSchema.setContains(source.getContains());
        newSchema.setContentEncoding(source.getContentEncoding());
        newSchema.setContentMediaType(source.getContentMediaType());
        newSchema.setDiscriminator(source.getDiscriminator());
        newSchema.setDefault(source.getDefault());
        newSchema.setElse(source.getElse());
        newSchema.setExternalDocs(source.getExternalDocs());
        newSchema.setPatternProperties(source.getPatternProperties());
        newSchema.setMaxContains(source.getMaxContains());
        newSchema.setMinContains(source.getMinContains());
        newSchema.setMinProperties(source.getMinProperties());
        newSchema.setContentEncoding(source.getContentEncoding());
        newSchema.setThen(source.getThen());
        newSchema.setUniqueItems(source.getUniqueItems());
        newSchema.setExtensions(source.getExtensions());
        newSchema.setItems(source.getItems());
        newSchema.setContentSchema(source.getContentSchema());
        newSchema.setPrefixItems(source.getPrefixItems());
        newSchema.setUnevaluatedItems(source.getUnevaluatedItems());
        newSchema.setUnevaluatedProperties(source.getUnevaluatedProperties());
        newSchema.setWriteOnly(source.getWriteOnly());
        newSchema.setXml(source.getXml());
        newSchema.setTitle(source.getTitle());
        return newSchema;
    }

    /**
     * Erases multiple settings and attributes of the provided schema object. Optionally, it can also erase the schema's name.
     *
     * @param schema the Schema object whose settings are to be erased
     * @param eraseName a boolean indicating whether the schema's name should also be erased
     */
    public static void eraseSchemaSettings(Schema<?> schema, boolean eraseName) {
        if (eraseName) {
            schema.setName(null);
        }
        schema.setType(null);
        schema.setTypes(null);
        schema.setPattern(null);
        schema.setDescription(null);
        schema.setFormat(null);
        schema.setExample(null);
        schema.setExclusiveMinimum(null);
        schema.setExclusiveMaximum(null);
        schema.setMinimum(null);
        schema.setMaximum(null);
        schema.setDefault(null);
        schema.setMaxLength(null);
        schema.setMinLength(null);
        schema.setJsonSchema(null);
        schema.setNullable(null);
        schema.setReadOnly(null);
        schema.setWriteOnly(null);
        schema.setDeprecated(null);
        schema.setExternalDocs(null);
        schema.set$ref(null);
        schema.set$schema(null);
        schema.set$id(null);
        schema.set$anchor(null);
        schema.set$comment(null);
        schema.setExclusiveMinimumValue(null);
        schema.setExclusiveMaximumValue(null);
        schema.set$dynamicAnchor(null);
        schema.set$dynamicRef(null);
        schema.setAdditionalItems(null);
        schema.setAdditionalProperties(null);
        schema.setAnyOf(null);
        schema.setOneOf(null);
        schema.setNot(null);
        schema.setProperties(null);
        schema.setRequired(null);
        schema.setAllOf(null);
        schema.set$vocabulary(null);
        schema.setBooleanSchemaValue(null);
        schema.setConst(null);
        schema.setContains(null);
        schema.setContentEncoding(null);
        schema.setContentMediaType(null);
        schema.setDiscriminator(null);
        schema.setDefault(null);
        schema.setElse(null);
        schema.setExternalDocs(null);
        schema.setPatternProperties(null);
        schema.setMaxContains(null);
        schema.setMinContains(null);
        schema.setMinProperties(null);
        schema.setContentEncoding(null);
        schema.setThen(null);
        schema.setUniqueItems(null);
        schema.setExtensions(null);
        schema.setItems(null);
        schema.setContentSchema(null);
        schema.setPrefixItems(null);
        schema.setUnevaluatedItems(null);
        schema.setUnevaluatedProperties(null);
        schema.setWriteOnly(null);
        schema.setXml(null);
        schema.setTitle(null);
    }

    /**
     * Retrieves a {@link Field} object representing a non-static field within the specified class type
     * that matches the given property name. The search is case-sensitive, but if no match is found,
     * a case-insensitive search is performed. If multiple matches are found, the first match is returned.
     *
     * @param type the {@link Class} object representing the type in which the field should be searched
     * @param propertyName the name of the property (field) to search for
     * @return the {@link Field} object corresponding to the property name if found, or {@code null} if no match is found
     */
    public static Field getFieldForTypeByPropertyName(Class<?> type, String propertyName) {
        Field[] fieldCandidates = JavaReflect.fields(type, MemberSelect.HIERARCHY)
            .stream()
            .filter(
                f -> !Modifier.isStatic(f.getModifiers()) && f.getName().equals(propertyName)
            ).toArray(Field[]::new);
        if (fieldCandidates.length == 0) {
            //property names in spring doc schema description sometimes have wrong case, try to find with ignored case
            //weird bug in spring doc !!!!
            fieldCandidates = JavaReflect.fields(type, MemberSelect.HIERARCHY)
                .stream()
                .filter(
                    f -> !Modifier.isStatic(f.getModifiers()) && f.getName().equalsIgnoreCase(propertyName)
                ).toArray(Field[]::new);
            if (fieldCandidates.length == 0) {
                log.warn("Failed to modify Open API schema for property '" + propertyName + "' within '"
                    + type.getName() + "'! Field not found!");
                return null;
            } else if (fieldCandidates.length > 1) {
                log.warn("When modifying Open API schema for property '" + propertyName + "' within '"
                    + type.getName() + "'! Multiple filed candidates found (ignoring case), returning first hit!");
            }
        } else if (fieldCandidates.length > 1) {
            log.warn("When modifying Open API schema for property '" + propertyName + "' within '"
                + type.getName() + "'! Multiple filed candidates found, returning first hit!");
        }
        return fieldCandidates[0];
    }

    /**
     * Determines if the specified {@link Field} is of type {@link Optional}.
     *
     * @param field the {@link Field} object to be checked
     * @return {@code true} if the field's type is assignable from {@link Optional}, {@code false} otherwise
     */
    public static boolean isOptional(Field field) {
        return Optional.class.isAssignableFrom(field.getType());
    }
}
