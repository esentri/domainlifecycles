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

package io.domainlifecycles.swagger.v3;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class provides additional temporal type support, which reflects
 * the Jackson default behaviour. The extension adds Open API schemata for
 * several additional temporal types:
 * <p>
 * - {@link LocalTime}
 * - {@link OffsetTime}
 * - {@link Year}
 * - {@link YearMonth}
 * - {@link MonthDay}
 * <p>
 * the default Springdoc behaviour currently (v.1.6.9) doesn't support those types.
 * <p>
 * The temporal type extension works as well as for domain object types as for all other classes which require Open
 * API support by SpringDoc.
 *
 * @author Mario Herb
 */
public class OpenAPITemporalTypeExtension {

    private static final Logger log = LoggerFactory.getLogger(OpenAPITemporalTypeExtension.class);
    private static final String REF_PREFIX = "#/components/schemas/";

    /**
     * Central entry point for the Open API extension with additional temporal type schemata.
     *
     * @param openAPI {@link OpenAPI} instance to be extended
     */
    public static void extendOpenAPISchemaForTemporalTypes(OpenAPI openAPI) {
        if (openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) {
            replaceLocalTimeSchema(openAPI);
            modifyTemporalSchemaReferences(openAPI);
        }
    }

    private static void replaceLocalTimeSchema(OpenAPI openAPI) {
        Schema<?> localTimeSchema = openAPI.getComponents().getSchemas().get(LocalTime.class.getName());
        if (localTimeSchema != null) {
            Schema<?> newLocalTimeSchema = new Schema<LocalTime>();
            newLocalTimeSchema.setName(localTimeSchema.getName());
            newLocalTimeSchema.setType(Constants.TYPE_STRING);
            newLocalTimeSchema.setPattern(Constants.PATTERN_LOCAL_TIME);
            openAPI.getComponents().getSchemas().put(LocalTime.class.getName(), newLocalTimeSchema);
        }
    }

    private static void modifyTemporalSchemaReferences(OpenAPI openAPI) {

        final AtomicBoolean needToAddOffsetTimeSchema = new AtomicBoolean();
        needToAddOffsetTimeSchema.set(false);

        final AtomicBoolean needToAddYearMonthSchema = new AtomicBoolean();
        needToAddYearMonthSchema.set(false);

        final AtomicBoolean needToAddMonthDaySchema = new AtomicBoolean();
        needToAddMonthDaySchema.set(false);

        final AtomicBoolean needToAddYearSchema = new AtomicBoolean();
        needToAddYearSchema.set(false);

        openAPI.getComponents().getSchemas()
            .entrySet()
            .stream()
            .filter(e -> !e.getKey().startsWith(Constants.IGNORED_RESPONSE_OBJECT_FQN))
            .forEach(entry -> {
                try {
                    Class<?> type = Thread.currentThread().getContextClassLoader().loadClass(entry.getKey());
                    JavaReflect.fields(type, MemberSelect.HIERARCHY)
                        .stream()
                        .filter(
                            f -> !Modifier.isStatic(f.getModifiers())
                                && (
                                containsType(f, OffsetTime.class)
                                    || containsType(f, YearMonth.class)
                                    || containsType(f, MonthDay.class)
                                    || containsType(f, Year.class)
                            )
                        )
                        .forEach(f -> {
                            if (containsType(f, OffsetTime.class)) {
                                needToAddOffsetTimeSchema.set(true);
                                modifyPropertySchemaReference(entry.getValue(), f.getName(),
                                    OffsetTime.class.getName());
                            } else if (containsType(f, YearMonth.class)) {
                                needToAddYearMonthSchema.set(true);
                                modifyPropertySchemaReference(entry.getValue(), f.getName(), YearMonth.class.getName());
                            } else if (containsType(f, MonthDay.class)) {
                                needToAddMonthDaySchema.set(true);
                                modifyPropertySchemaReference(entry.getValue(), f.getName(), MonthDay.class.getName());
                            } else if (containsType(f, Year.class)) {
                                needToAddYearSchema.set(true);
                                modifyPropertySchemaReference(entry.getValue(), f.getName(), Year.class.getName());
                            }
                        });
                } catch (ClassNotFoundException e) {
                    log.warn(
                        "Wasn't able to add/modify potential temporal schema information for '" + entry.getKey() +
                            "'!");
                }
            });
        if (needToAddOffsetTimeSchema.get()) {
            addOffsetTimeSchema(openAPI);
        }
        if (needToAddYearMonthSchema.get()) {
            addYearMonthSchema(openAPI);
        }
        if (needToAddMonthDaySchema.get()) {
            addMonthDaySchema(openAPI);
        }
        if (needToAddYearSchema.get()) {
            addYearSchema(openAPI);
        }
    }

    private static boolean containsType(Field f, Class<?> type) {
        if (Optional.class.isAssignableFrom(f.getType()) || Collection.class.isAssignableFrom(f.getType())) {
            return f.getGenericType().getTypeName().contains("<" + type.getName() + ">");
        } else {
            return type.isAssignableFrom(f.getType());
        }
    }

    private static void addOffsetTimeSchema(OpenAPI openAPI) {
        if (openAPI.getComponents().getSchemas().get(OffsetTime.class.getName()) == null) {
            Schema<?> offsetTimeSchema = new Schema<>();
            offsetTimeSchema.setType(Constants.TYPE_STRING);
            offsetTimeSchema.setPattern(Constants.PATTERN_OFFSET_TIME);
            openAPI.getComponents().addSchemas(OffsetTime.class.getName(), offsetTimeSchema);
        }
    }

    private static void addYearMonthSchema(OpenAPI openAPI) {
        if (openAPI.getComponents().getSchemas().get(YearMonth.class.getName()) == null) {
            Schema<?> yearMonthSchema = new Schema<>();
            yearMonthSchema.setType(Constants.TYPE_STRING);
            yearMonthSchema.setPattern(Constants.PATTERN_YEAR_MONTH);
            openAPI.getComponents().addSchemas(YearMonth.class.getName(), yearMonthSchema);
        }
    }

    private static void addMonthDaySchema(OpenAPI openAPI) {
        if (openAPI.getComponents().getSchemas().get(MonthDay.class.getName()) == null) {
            Schema<?> monthDaySchema = new Schema<>();
            monthDaySchema.setType(Constants.TYPE_STRING);
            monthDaySchema.setPattern(Constants.PATTERN_MONTH_DAY);
            openAPI.getComponents().addSchemas(MonthDay.class.getName(), monthDaySchema);
        }
    }

    private static void addYearSchema(OpenAPI openAPI) {
        if (openAPI.getComponents().getSchemas().get(Year.class.getName()) == null) {
            Schema<?> yearSchema = new Schema<>();
            yearSchema.setType(Constants.TYPE_STRING);
            yearSchema.setPattern(Constants.PATTERN_YEAR);
            openAPI.getComponents().addSchemas(Year.class.getName(), yearSchema);
        }
    }

    private static void modifyPropertySchemaReference(Schema<?> typeSchema, String propertyName,
                                                      String propertyTypeFullQualifiedName) {
        if (typeSchema.getProperties() != null) {
            Schema<?> propertySchema = typeSchema.getProperties().get(propertyName);
            if (propertySchema != null) {
                propertySchema.setType(null);
                propertySchema.setJsonSchema(null);
                propertySchema.setProperties(null);
                propertySchema.set$ref(REF_PREFIX + propertyTypeFullQualifiedName);
            }
        }
    }
}
