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

package io.domainlifecycles.swagger.v3;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import static io.domainlifecycles.swagger.v3.Constants.IGNORED_RESPONSE_OBJECT_FQN;

/**
 * If omitted, primitive properties are initialized by Jackson with primitive default values.
 * That's ok, but it can lead to unintended behaviour. So in case of DLC we want to make the
 * requested behaviour explicit. Therefore, we mark primitive values as required in the API documentation.
 * So the API user is requested to provide explicit value for the initialization of primitive field, when
 * deserializing.
 *
 * @author Mario Herb
 */
public class OpenAPIPrimitivePropertyExtension {

    private static final Logger log = LoggerFactory.getLogger(OpenAPIPrimitivePropertyExtension.class);

    /**
     * Entry point for all Open API extension regarding primitive class properties.
     *
     * @param openAPI {@link OpenAPI} instance to be extended
     */
    public static void extendPrimitiveProperties(OpenAPI openAPI) {
        if (openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) {
            openAPI.getComponents().getSchemas()
                .entrySet()
                .stream()
                .filter(e -> !e.getKey().startsWith(IGNORED_RESPONSE_OBJECT_FQN))
                .forEach(entry -> extendOpenAPISchemaForPrimitivePropertyTypes(entry.getKey(), entry.getValue()));
        }
    }

    private static void extendOpenAPISchemaForPrimitivePropertyTypes(String fullQualifiedTypeName,
                                                                     Schema<?> typeSchema) {
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
                    Field f = getFieldForTypeByPropertyName(finalType, pName);
                    if (f != null && f.getType().isPrimitive()) {
                        if (typeSchema.getRequired() == null) {
                            var reqList = new ArrayList<String>();
                            reqList.add(pName);
                            typeSchema.setRequired(reqList);
                        } else {
                            typeSchema.getRequired().add(pName);
                        }

                    }
                });
            }
        }
    }

    private static Field getFieldForTypeByPropertyName(Class<?> type, String propertyName) {
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

}
