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
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.Schema;
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
 * the default Springdoc behaviour currently (2.8.13) doesn't support those types.
 * <p>
 * The temporal type extension works as well as for domain object types as for all other classes which require Open
 * API support by SpringDoc.
 *
 * Supports OpenAPI 3.0 and 3.1.
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
            modifyTemporalSchemaReferences(openAPI);
        }
    }

    private static void modifyTemporalSchemaReferences(OpenAPI openAPI) {
        final AtomicBoolean needToAddLocalTimeSchema = new AtomicBoolean();
        needToAddLocalTimeSchema.set(false);

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
                                    || containsType(f, LocalTime.class)
                            )
                        )
                        .forEach(f -> {
                            if (containsType(f, OffsetTime.class)) {
                                needToAddOffsetTimeSchema.set(true);
                                modifyPropertySchemaReference(entry.getValue(), f.getName(),
                                    OffsetTime.class.getName(), Optional.class.isAssignableFrom(f.getType()));
                            } else if (containsType(f, YearMonth.class)) {
                                needToAddYearMonthSchema.set(true);
                                modifyPropertySchemaReference(entry.getValue(), f.getName(), YearMonth.class.getName(), Optional.class.isAssignableFrom(f.getType()));
                            } else if (containsType(f, MonthDay.class)) {
                                needToAddMonthDaySchema.set(true);
                                modifyPropertySchemaReference(entry.getValue(), f.getName(), MonthDay.class.getName(), Optional.class.isAssignableFrom(f.getType()));
                            } else if (containsType(f, Year.class)) {
                                needToAddYearSchema.set(true);
                                modifyPropertySchemaReference(entry.getValue(), f.getName(), Year.class.getName(), Optional.class.isAssignableFrom(f.getType()));
                            } else if (containsType(f, LocalTime.class)) {
                                needToAddLocalTimeSchema.set(true);
                                modifyPropertySchemaReference(entry.getValue(), f.getName(), LocalTime.class.getName(), Optional.class.isAssignableFrom(f.getType()));
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
        if (needToAddLocalTimeSchema.get()) {
            addLocalTimeSchema(openAPI);
        }
    }

    private static boolean containsType(Field f, Class<?> type) {
        if (Optional.class.isAssignableFrom(f.getType()) || Collection.class.isAssignableFrom(f.getType())) {
            return f.getGenericType().getTypeName().contains("<" + type.getName() + ">");
        } else {
            return type.isAssignableFrom(f.getType());
        }
    }

    private static void addLocalTimeSchema(OpenAPI openAPI) {
        if (openAPI.getComponents().getSchemas().get(LocalTime.class.getName()) == null) {
            Schema<?> newLocalTimeSchema = new Schema<LocalTime>();
            newLocalTimeSchema.setSpecVersion(openAPI.getSpecVersion());
            if(openAPI.getSpecVersion().equals(SpecVersion.V30)) {
                newLocalTimeSchema.setType(Constants.TYPE_STRING);
            }
            if(openAPI.getSpecVersion().equals(SpecVersion.V31)) {
                newLocalTimeSchema.addType(Constants.TYPE_STRING);
            }
            newLocalTimeSchema.setPattern(Constants.PATTERN_LOCAL_TIME);
            openAPI.getComponents().getSchemas().put(LocalTime.class.getName(), newLocalTimeSchema);
        }
    }

    private static void addOffsetTimeSchema(OpenAPI openAPI) {
        if (openAPI.getComponents().getSchemas().get(OffsetTime.class.getName()) == null) {
            Schema<?> offsetTimeSchema = new Schema<>();
            offsetTimeSchema.setSpecVersion(openAPI.getSpecVersion());
            if(openAPI.getSpecVersion().equals(SpecVersion.V30)) {
                offsetTimeSchema.setType(Constants.TYPE_STRING);
            }
            if(openAPI.getSpecVersion().equals(SpecVersion.V31)) {
                offsetTimeSchema.addType(Constants.TYPE_STRING);
            }
            offsetTimeSchema.setPattern(Constants.PATTERN_OFFSET_TIME);
            openAPI.getComponents().addSchemas(OffsetTime.class.getName(), offsetTimeSchema);
        }
    }

    private static void addYearMonthSchema(OpenAPI openAPI) {
        if (openAPI.getComponents().getSchemas().get(YearMonth.class.getName()) == null) {
            Schema<?> yearMonthSchema = new Schema<>();
            yearMonthSchema.setSpecVersion(openAPI.getSpecVersion());
            if(openAPI.getSpecVersion().equals(SpecVersion.V30)) {
                yearMonthSchema.setType(Constants.TYPE_STRING);
            }
            if(openAPI.getSpecVersion().equals(SpecVersion.V31)) {
                yearMonthSchema.addType(Constants.TYPE_STRING);
            }
            yearMonthSchema.setPattern(Constants.PATTERN_YEAR_MONTH);
            openAPI.getComponents().addSchemas(YearMonth.class.getName(), yearMonthSchema);
        }
    }

    private static void addMonthDaySchema(OpenAPI openAPI) {
        if (openAPI.getComponents().getSchemas().get(MonthDay.class.getName()) == null) {
            Schema<?> monthDaySchema = new Schema<>();
            monthDaySchema.setSpecVersion(openAPI.getSpecVersion());
            if(openAPI.getSpecVersion().equals(SpecVersion.V30)) {
                monthDaySchema.setType(Constants.TYPE_STRING);
            }
            if(openAPI.getSpecVersion().equals(SpecVersion.V31)) {
                monthDaySchema.addType(Constants.TYPE_STRING);
            }
            monthDaySchema.setPattern(Constants.PATTERN_MONTH_DAY);
            openAPI.getComponents().addSchemas(MonthDay.class.getName(), monthDaySchema);
        }
    }

    private static void addYearSchema(OpenAPI openAPI) {
        if (openAPI.getComponents().getSchemas().get(Year.class.getName()) == null) {
            Schema<?> yearSchema = new Schema<>();
            yearSchema.setSpecVersion(openAPI.getSpecVersion());
            if(openAPI.getSpecVersion().equals(SpecVersion.V30)) {
                yearSchema.setType(Constants.TYPE_STRING);
            }
            if(openAPI.getSpecVersion().equals(SpecVersion.V31)) {
                yearSchema.addType(Constants.TYPE_STRING);
            }
            yearSchema.setPattern(Constants.PATTERN_YEAR);
            openAPI.getComponents().addSchemas(Year.class.getName(), yearSchema);
        }
    }

    private static void modifyPropertySchemaReference(Schema<?> typeSchema, String propertyName,
                                                      String propertyTypeFullQualifiedName, boolean optional) {
        if (typeSchema.getProperties() != null) {
            Schema<?> propertySchema = typeSchema.getProperties().get(propertyName);
            if (propertySchema != null) {
                Utils.eraseSchemaSettings(propertySchema, false);
                propertySchema.set$ref(REF_PREFIX + propertyTypeFullQualifiedName);
            }
        }
    }
}
