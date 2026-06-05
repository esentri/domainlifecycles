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

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * This class provides functionality to extend the nullability handling of OpenAPI specifications
 * for schemas with {@code Optional} fields. Its primary purpose is to modify OpenAPI
 * schema representations to indicate nullable properties where applicable.
 *
 * Utilities in this class allow schema adjustments for {@code Optional} properties,
 * including compatibility handling for different versions of the OpenAPI specification.
 *
 * @author Mario Herb
 */
public class OpenAPIOptionalNullabilityExtension {

    private static final Logger log = LoggerFactory.getLogger(OpenAPIOptionalNullabilityExtension.class);

    /**
     * Extends nullability for optional types in the provided OpenAPI specification.
     * This method modifies the schema definitions in the OpenAPI object by iterating
     * over its components and extending their nullability definitions for applicable types.
     *
     * @param openAPI the OpenAPI object containing schemas and components for which
     *                nullability for optional types needs to be extended. If the
     *                components or schemas are null, the method will exit without
     *                making modifications.
     */
    public static void extendNullabilityForOptionals(OpenAPI openAPI) {
        if (openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) {
            openAPI.getComponents().getSchemas()
                .entrySet()
                .stream()
                .filter(e -> !e.getKey().startsWith(Constants.IGNORED_RESPONSE_OBJECT_FQN))
                .forEach(entry -> extendOpenAPISchemaForType(entry.getKey(), entry.getValue()));
        }
    }

    private static void extendOpenAPISchemaForType(String fullQualifiedTypeName, Schema<?> typeSchema) {
        log.debug("Extending Open API schema for '" + fullQualifiedTypeName + "'!");
        Class<?> type = null;
        try {
            type = Thread.currentThread().getContextClassLoader().loadClass(fullQualifiedTypeName);
        } catch (ClassNotFoundException e) {
            log.warn("Failed to modify Open API schema for '" + fullQualifiedTypeName + "'!");
        }
        if (type != null) {
            final Class<?> finalType = type;
            if (typeSchema.getProperties() != null) {
                typeSchema.getProperties().forEach((pName, pSchema) -> {
                    Field f = Utils.getFieldForTypeByPropertyName(finalType, pName);
                    if (f != null && Utils.isOptional(f)) {
                        extendSchemaForField(f, pSchema);
                    }
                });
            }
        }
    }

    private static void extendSchemaForField(Field field, Schema<?> propertySchema) {
        Objects.requireNonNull(field, "A field must be given to extend its Open API schema!");
        Objects.requireNonNull(propertySchema, "A property schema must be given to be extended!");
        extendNullable(propertySchema);
    }

    private static void extendNullable(Schema<?> schema){
        //spring doc open api 2.8.13 and lower doesn't do that
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            schema.setNullable(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            if(schema.getTypes() != null && !schema.getTypes().isEmpty() && !schema.getTypes().contains(Constants.TYPE_NULL)) {
                schema.addType(Constants.TYPE_NULL);
            }else if(schema.get$ref() != null){
                var refSchema = Utils.copySchema(schema, null);
                Utils.eraseSchemaSettings(schema, false);
                var nullSchema = new Schema<>();
                nullSchema.setSpecVersion(schema.getSpecVersion());
                nullSchema.addType(Constants.TYPE_NULL);
                schema.setOneOf(List.of(refSchema, nullSchema));
            }
        }
    }

}
