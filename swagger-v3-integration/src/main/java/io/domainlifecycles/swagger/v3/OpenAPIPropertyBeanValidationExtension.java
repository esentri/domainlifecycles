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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ContainerElementTypeDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * If a bean validation implementation is provided in the classpath of the target application, then
 * this customization extends all classes which have bean validation annotations on their properties (also non dlc
 * domain object classes, like DTOs)
 * with a corresponding additional open api description, if they are used in controller interfaces which provide Open
 * API documentation {@link OpenAPIPropertyBeanValidationExtension}.
 * <p>
 * In contrast to SpringDoc default behaviour this extension adds additional detailed Open API doc information for
 * every single default
 * annotation of the Java Bean Validation standard 2.0 or 3.0. Additionally, this extension corrects the (in our
 * point of view wrong) default behaviour of spring doc
 * if properties (fields) of API classes wrap their types in {@link Optional}.
 * </p>
 * If no bean validation implementation is provided, only an info level log entry occurs. The behaviour of the
 * application is not affected in any negative way.
 *
 * @author Mario Herb
 */
public class OpenAPIPropertyBeanValidationExtension {

    private static final Logger log = LoggerFactory.getLogger(OpenAPIPropertyBeanValidationExtension.class);

    private static Validator validatorJavax;
    private static jakarta.validation.Validator validatorJakarta;

    private static final String BV_2_0_NOT_EMPTY = "javax.validation.constraints.NotEmpty";
    private static final String BV_3_0_NOT_EMPTY = "jakarta.validation.constraints.NotEmpty";

    private static final String BV_2_0_NOT_BLANK = "javax.validation.constraints.NotBlank";

    private static final String BV_3_0_NOT_BLANK = "jakarta.validation.constraints.NotBlank";

    private static final String BV_2_0_EMAIL = "javax.validation.constraints.Email";
    private static final String BV_3_0_EMAIL = "jakarta.validation.constraints.Email";

    private static final String BV_2_0_PATTERN = "javax.validation.constraints.Pattern";
    private static final String BV_3_0_PATTERN = "jakarta.validation.constraints.Pattern";

    private static final String BV_PATTERN_ATTRIBUTE_REGEXP = "regexp";

    private static final String BV_2_0_SIZE = "javax.validation.constraints.Size";
    private static final String BV_3_0_SIZE = "jakarta.validation.constraints.Size";

    private static final String BV_SIZE_ATTRIBUTE_MAX = "max";
    private static final String BV_SIZE_ATTRIBUTE_MIN = "min";

    private static final String BV_2_0_MIN = "javax.validation.constraints.Min";
    private static final String BV_3_0_MIN = "jakarta.validation.constraints.Min";

    private static final String BV_MIN_ATTRIBUTE_VALUE = "value";

    private static final String BV_2_0_MAX = "javax.validation.constraints.Max";
    private static final String BV_3_0_MAX = "jakarta.validation.constraints.Max";

    private static final String BV_MAX_ATTRIBUTE_VALUE = "value";

    private static final String BV_2_0_DECIMAL_MIN = "javax.validation.constraints.DecimalMin";
    private static final String BV_3_0_DECIMAL_MIN = "jakarta.validation.constraints.DecimalMin";

    private static final String BV_DECIMAL_MIN_ATTRIBUTE_VALUE = "value";
    private static final String BV_DECIMAL_MIN_ATTRIBUTE_INCLUSIVE = "inclusive";

    private static final String BV_2_0_DECIMAL_MAX = "javax.validation.constraints.DecimalMax";
    private static final String BV_3_0_DECIMAL_MAX = "jakarta.validation.constraints.DecimalMax";

    private static final String BV_DECIMAL_MAX_ATTRIBUTE_VALUE = "value";
    private static final String BV_DECIMAL_MAX_ATTRIBUTE_INCLUSIVE = "inclusive";

    private static final String BV_2_0_NEGATIVE = "javax.validation.constraints.Negative";
    private static final String BV_3_0_NEGATIVE = "jakarta.validation.constraints.Negative";

    private static final String BV_2_0_POSITIVE = "javax.validation.constraints.Positive";
    private static final String BV_3_0_POSITIVE = "jakarta.validation.constraints.Positive";

    private static final String BV_2_0_NEGATIVE_OR_ZERO = "javax.validation.constraints.NegativeOrZero";
    private static final String BV_3_0_NEGATIVE_OR_ZERO = "jakarta.validation.constraints.NegativeOrZero";

    private static final String BV_2_0_POSITIVE_OR_ZERO = "javax.validation.constraints.PositiveOrZero";
    private static final String BV_3_0_POSITIVE_OR_ZERO = "jakarta.validation.constraints.PositiveOrZero";

    private static final String BV_2_0_DIGITS = "javax.validation.constraints.Digits";
    private static final String BV_3_0_DIGITS = "jakarta.validation.constraints.Digits";

    private static final String BV_DIGITS_ATTRIBUTE_INTEGER = "integer";
    private static final String BV_DIGITS_ATTRIBUTE_FRACTION = "fraction";

    private static final String BV_2_0_PAST = "javax.validation.constraints.Past";
    private static final String BV_3_0_PAST = "jakarta.validation.constraints.Past";
    private static final String BV_2_0_FUTURE = "javax.validation.constraints.Future";
    private static final String BV_3_0_FUTURE = "jakarta.validation.constraints.Future";
    private static final String BV_2_0_PAST_OR_PRESENT = "javax.validation.constraints.PastOrPresent";
    private static final String BV_3_0_PAST_OR_PRESENT = "jakarta.validation.constraints.PastOrPresent";

    private static final String BV_2_0_FUTURE_OR_PRESENT = "javax.validation.constraints.FutureOrPresent";
    private static final String BV_3_0_FUTURE_OR_PRESENT = "jakarta.validation.constraints.FutureOrPresent";

    static {
        try {
            validatorJavax = Validation.buildDefaultValidatorFactory().getValidator();
            log.info("Bean validation 2.0 supported!");
        } catch (Throwable t) {
            validatorJavax = null;
            log.info("No bean validation 2.0 provider found or validation factory could not be built. " +
                "Bean validation 2.0 extensions are disabled!");
        }
        try {
            validatorJakarta = jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator();
            log.info("Bean validation 3.0 supported!");
        } catch (Throwable t) {
            validatorJakarta = null;
            log.info("No bean validation provider 3.0 found or validation factory could not be built. " +
                "Bean validation 3.0 extensions are disabled!");
        }
    }

    /**
     * Entry point for all Open API extension regarding bean validation annotations on class properties.
     *
     * @param openAPI {@link OpenAPI} instance to be extended
     */
    public static void extendWithBeanValidationInformation(OpenAPI openAPI) {
        if ((validatorJavax != null || validatorJakarta != null) && openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) {
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
                    Field f = getFieldForTypeByPropertyName(finalType, pName);
                    if (f != null) {
                        extendSchemaForField(f, pSchema);
                    }
                });
            }
        }
    }

    private static boolean isOptional(Field field) {
        return Optional.class.isAssignableFrom(field.getType());
    }

    private static Set<ConstraintDescriptor<?>> getConstraintDescriptorsJavax(Field field) {
        var desc = validatorJavax.getConstraintsForClass(field.getDeclaringClass());
        var prop = desc.getConstraintsForProperty(field.getName());
        if (prop != null) {
            if (isOptional(field)) {
                var container =
                    (ContainerElementTypeDescriptor) prop.getConstrainedContainerElementTypes().toArray()[0];
                return container.getConstraintDescriptors();
            } else {
                return prop.getConstraintDescriptors();
            }
        }
        return null;
    }

    private static Set<jakarta.validation.metadata.ConstraintDescriptor<?>> getConstraintDescriptorsJakarta(Field field) {
        var desc = validatorJakarta.getConstraintsForClass(field.getDeclaringClass());
        var prop = desc.getConstraintsForProperty(field.getName());
        if (prop != null) {
            if (isOptional(field)) {
                var container =
                    (jakarta.validation.metadata.ContainerElementTypeDescriptor) prop.getConstrainedContainerElementTypes().toArray()[0];
                return container.getConstraintDescriptors();
            } else {
                return prop.getConstraintDescriptors();
            }
        }
        return null;
    }

    private static void extendSchemaForField(Field field, Schema<?> propertySchema) {
        Objects.requireNonNull(field, "A field must be given to extend its Open API schema!");
        Objects.requireNonNull(propertySchema, "A property schema must be given to be extended!");
        if (validatorJavax != null) {
            Set<ConstraintDescriptor<?>> constraintDescriptors = getConstraintDescriptorsJavax(field);
            if (constraintDescriptors != null) {
                constraintDescriptors.forEach(cd -> extendSchemaForConstraintJavax(cd, propertySchema));
            }
        } else if (validatorJakarta != null) {
            Set<jakarta.validation.metadata.ConstraintDescriptor<?>> constraintDescriptors =
                getConstraintDescriptorsJakarta(
                field);
            if (constraintDescriptors != null) {
                constraintDescriptors.forEach(cd -> extendSchemaForConstraintJakarta(cd, propertySchema));
            }
        }
    }

    private static void extendSchemaForConstraintJavax(ConstraintDescriptor<?> cd, Schema<?> schema) {
        switch (cd.getAnnotation().annotationType().getName()) {
            case BV_2_0_NOT_EMPTY -> extendNotEmpty(schema);
            case BV_2_0_NOT_BLANK -> extendNotBlank(schema);
            case BV_2_0_EMAIL -> extendEmail(schema);
            case BV_2_0_PATTERN -> {
                String regEx = (String) cd.getAttributes().get(BV_PATTERN_ATTRIBUTE_REGEXP);
                extendPattern(schema, regEx);
            }
            case BV_2_0_SIZE -> {
                Integer minLengthAnnotated = (Integer) cd.getAttributes().get(BV_SIZE_ATTRIBUTE_MIN);
                Integer maxLengthAnnotated = (Integer) cd.getAttributes().get(BV_SIZE_ATTRIBUTE_MAX);
                extendSize(schema, minLengthAnnotated, maxLengthAnnotated);
            }
            case BV_2_0_MIN -> {
                Long minAnnotated = (Long) cd.getAttributes().get(BV_MIN_ATTRIBUTE_VALUE);
                extendMin(schema, minAnnotated);
            }
            case BV_2_0_MAX -> {
                Long maxAnnotated = (Long) cd.getAttributes().get(BV_MAX_ATTRIBUTE_VALUE);
                extendMax(schema, maxAnnotated);
            }
            case BV_2_0_DECIMAL_MIN -> {
                String minAnnotated = (String) cd.getAttributes().get(BV_DECIMAL_MIN_ATTRIBUTE_VALUE);
                BigDecimal bigDecimalMinAnnotated = new BigDecimal(minAnnotated);
                Boolean inclusiveAnnotated = (Boolean) cd.getAttributes().get(BV_DECIMAL_MIN_ATTRIBUTE_INCLUSIVE);
                extendDecimalMin(schema, bigDecimalMinAnnotated, inclusiveAnnotated);
            }
            case BV_2_0_DECIMAL_MAX -> {
                String maxAnnotated = (String) cd.getAttributes().get(BV_DECIMAL_MAX_ATTRIBUTE_VALUE);
                BigDecimal bigDecimalMaxAnnotated = new BigDecimal(maxAnnotated);
                Boolean inclusiveAnnotated = (Boolean) cd.getAttributes().get(BV_DECIMAL_MAX_ATTRIBUTE_INCLUSIVE);
                extendDecimalMax(schema, bigDecimalMaxAnnotated, inclusiveAnnotated);
            }
            case BV_2_0_NEGATIVE -> extendNegative(schema);
            case BV_2_0_POSITIVE -> extendPositive(schema);
            case BV_2_0_NEGATIVE_OR_ZERO -> extendNegativeOrZero(schema);
            case BV_2_0_POSITIVE_OR_ZERO -> extendPositiveOrZero(schema);
            case BV_2_0_DIGITS -> {
                Integer integerAnnotated = (Integer) cd.getAttributes().get(BV_DIGITS_ATTRIBUTE_INTEGER);
                Integer fractionAnnotated = (Integer) cd.getAttributes().get(BV_DIGITS_ATTRIBUTE_FRACTION);
                extendDigits(schema, integerAnnotated, fractionAnnotated);
            }
            case BV_2_0_PAST -> extendPast(schema);
            case BV_2_0_FUTURE -> extendFuture(schema);
            case BV_2_0_PAST_OR_PRESENT -> extendPastOrPresent(schema);
            case BV_2_0_FUTURE_OR_PRESENT -> extendFutureOrPresent(schema);
        }
    }

    private static void extendSchemaForConstraintJakarta(jakarta.validation.metadata.ConstraintDescriptor<?> cd,
                                                         Schema<?> schema) {
        switch (cd.getAnnotation().annotationType().getName()) {
            case BV_3_0_NOT_EMPTY -> extendNotEmpty(schema);
            case BV_3_0_NOT_BLANK -> extendNotBlank(schema);
            case BV_3_0_EMAIL -> extendEmail(schema);
            case BV_3_0_PATTERN -> {
                String regEx = (String) cd.getAttributes().get(BV_PATTERN_ATTRIBUTE_REGEXP);
                extendPattern(schema, regEx);
            }
            case BV_3_0_SIZE -> {
                Integer minLengthAnnotated = (Integer) cd.getAttributes().get(BV_SIZE_ATTRIBUTE_MIN);
                Integer maxLengthAnnotated = (Integer) cd.getAttributes().get(BV_SIZE_ATTRIBUTE_MAX);
                extendSize(schema, minLengthAnnotated, maxLengthAnnotated);
            }
            case BV_3_0_MIN -> {
                Long minAnnotated = (Long) cd.getAttributes().get(BV_MIN_ATTRIBUTE_VALUE);
                extendMin(schema, minAnnotated);
            }
            case BV_3_0_MAX -> {
                Long maxAnnotated = (Long) cd.getAttributes().get(BV_MAX_ATTRIBUTE_VALUE);
                extendMax(schema, maxAnnotated);
            }
            case BV_3_0_DECIMAL_MIN -> {
                String minAnnotated = (String) cd.getAttributes().get(BV_DECIMAL_MIN_ATTRIBUTE_VALUE);
                BigDecimal bigDecimalMinAnnotated = new BigDecimal(minAnnotated);
                Boolean inclusiveAnnotated = (Boolean) cd.getAttributes().get(BV_DECIMAL_MIN_ATTRIBUTE_INCLUSIVE);
                extendDecimalMin(schema, bigDecimalMinAnnotated, inclusiveAnnotated);
            }
            case BV_3_0_DECIMAL_MAX -> {
                String maxAnnotated = (String) cd.getAttributes().get(BV_DECIMAL_MAX_ATTRIBUTE_VALUE);
                BigDecimal bigDecimalMaxAnnotated = new BigDecimal(maxAnnotated);
                Boolean inclusiveAnnotated = (Boolean) cd.getAttributes().get(BV_DECIMAL_MAX_ATTRIBUTE_INCLUSIVE);
                extendDecimalMax(schema, bigDecimalMaxAnnotated, inclusiveAnnotated);
            }
            case BV_3_0_NEGATIVE -> extendNegative(schema);
            case BV_3_0_POSITIVE -> extendPositive(schema);
            case BV_3_0_NEGATIVE_OR_ZERO -> extendNegativeOrZero(schema);
            case BV_3_0_POSITIVE_OR_ZERO -> extendPositiveOrZero(schema);
            case BV_3_0_DIGITS -> {
                Integer integerAnnotated = (Integer) cd.getAttributes().get(BV_DIGITS_ATTRIBUTE_INTEGER);
                Integer fractionAnnotated = (Integer) cd.getAttributes().get(BV_DIGITS_ATTRIBUTE_FRACTION);
                extendDigits(schema, integerAnnotated, fractionAnnotated);
            }
            case BV_3_0_PAST -> extendPast(schema);
            case BV_3_0_FUTURE -> extendFuture(schema);
            case BV_3_0_PAST_OR_PRESENT -> extendPastOrPresent(schema);
            case BV_3_0_FUTURE_OR_PRESENT -> extendFutureOrPresent(schema);
        }
    }

    private static void extendNotEmpty(Schema<?> schema) {
        if (Constants.TYPE_STRING.equals(schema.getType())) {
            //spring doc open api 1.6.7 and lower doesn't do that
            if (schema.getMinLength() == null || schema.getMinLength() < 1) {
                schema.setMinLength(1);
            }
            addSchemaDescription("beanValidationNotEmpty", schema);
        } else if (Constants.TYPE_ARRAY.equals(schema.getType())) {
            //spring doc open api 1.6.7 and lower doesn't do that
            if (schema.getMinItems() == null || schema.getMinItems() < 1) {
                schema.setMinItems(1);
            }
            addSchemaDescription("beanValidationNotEmpty", schema);
        }
    }

    private static void extendNotBlank(Schema<?> schema) {
        if (Constants.TYPE_STRING.equals(schema.getType())) {
            //spring doc open api 1.6.7 and lower doesn't do that
            if (schema.getMinLength() == null || schema.getMinLength() < 1) {
                schema.setMinLength(1);
            }
            addSchemaDescription("beanValidationNotBlank", schema);
        }
    }

    private static void extendEmail(Schema<?> schema) {
        if (Constants.TYPE_STRING.equals(schema.getType())) {
            //spring doc open api 1.6.7 and lower doesn't do that
            if (schema.getFormat() == null) {
                schema.setFormat(Constants.FORMAT_TYPE_EMAIL);
            }
            addSchemaDescription("beanValidationEmail", schema);
        }
    }

    private static void extendPattern(Schema<?> schema, String regEx) {
        if (Constants.TYPE_STRING.equals(schema.getType())) {
            //spring doc open api 1.6.7 and lower doesn't do that for optional Strings
            if (schema.getPattern() == null) {
                schema.setPattern(regEx);
            }
            addSchemaDescription("beanValidationPattern", schema);
        }
    }

    private static void extendSize(Schema<?> schema, Integer minLengthAnnotated, Integer maxLengthAnnotated) {
        if (Constants.TYPE_STRING.equals(schema.getType())) {
            //spring doc open api 1.6.7 and lower doesn't do that for optional Strings
            if (schema.getMinLength() == null || schema.getMinLength() < minLengthAnnotated) {
                schema.setMinLength(minLengthAnnotated);
            }
            addSchemaDescription("beanValidationSizeMin", schema, minLengthAnnotated.toString());
            if (schema.getMaxLength() == null || schema.getMaxLength() > maxLengthAnnotated) {
                schema.setMaxLength(maxLengthAnnotated);
            }
            addSchemaDescription("beanValidationSizeMax", schema, maxLengthAnnotated.toString());

        }else if(Constants.TYPE_ARRAY.equals(schema.getType())){
            if (schema.getMinItems() == null || schema.getMinItems() < minLengthAnnotated) {
                schema.setMinItems(minLengthAnnotated);
            }
            if (schema.getMaxItems() == null || schema.getMaxItems() > maxLengthAnnotated) {
                schema.setMaxItems(maxLengthAnnotated);
            }
        }
    }

    private static void extendMin(Schema<?> schema, Long minAnnotated) {
        if (Constants.TYPE_NUMBER.equals(schema.getType()) || Constants.TYPE_INTEGER.equals(schema.getType())
            || (Constants.TYPE_STRING.equals(schema.getType()) && Constants.FORMAT_TYPE_BYTE.equals(
            schema.getFormat()))) {
            //spring doc open api 1.6.7 and lower doesn't do that for optional numeric types
            if (schema.getMinimum() == null || schema.getMinimum().intValue() < minAnnotated) {
                schema.setMinimum(BigDecimal.valueOf(minAnnotated));
            }
            addSchemaDescription("beanValidationDecimalMin", schema, minAnnotated.toString());
        }
    }

    private static void extendMax(Schema<?> schema, Long maxAnnotated) {
        if (Constants.TYPE_NUMBER.equals(schema.getType()) || Constants.TYPE_INTEGER.equals(schema.getType())
            || (Constants.TYPE_STRING.equals(schema.getType()) && Constants.FORMAT_TYPE_BYTE.equals(
            schema.getFormat()))) {
            //spring doc open api 1.6.7 and lower doesn't do that for optional numeric types
            if (schema.getMaximum() == null || schema.getMaximum().intValue() > maxAnnotated) {
                schema.setMaximum(BigDecimal.valueOf(maxAnnotated));
            }
            addSchemaDescription("beanValidationDecimalMax", schema, maxAnnotated.toString());
        }
    }

    private static void extendDecimalMin(Schema<?> schema, BigDecimal bigDecimalMinAnnotated,
                                         Boolean inclusiveAnnotated) {
        if (Constants.TYPE_NUMBER.equals(schema.getType()) || Constants.TYPE_INTEGER.equals(schema.getType())
            || (Constants.TYPE_STRING.equals(schema.getType()) && Constants.FORMAT_TYPE_BYTE.equals(
            schema.getFormat()))) {
            //spring doc open api 1.6.7 and lower doesn't do that for optional numeric types
            if (schema.getMinimum() == null || schema.getMinimum().compareTo(bigDecimalMinAnnotated) < 0) {
                schema.setMinimum(bigDecimalMinAnnotated);

            }
            if (!inclusiveAnnotated) {
                schema.setExclusiveMinimum(true);
                addSchemaDescription("beanValidationDecimalMin", schema, bigDecimalMinAnnotated.toPlainString());
            } else {
                addSchemaDescription("beanValidationDecimalMinInclusive", schema,
                    bigDecimalMinAnnotated.toPlainString());
            }
        }
    }

    private static void extendDecimalMax(Schema<?> schema, BigDecimal bigDecimalMaxAnnotated,
                                         Boolean inclusiveAnnotated) {
        if (Constants.TYPE_NUMBER.equals(schema.getType()) || Constants.TYPE_INTEGER.equals(schema.getType())
            || (Constants.TYPE_STRING.equals(schema.getType()) && Constants.FORMAT_TYPE_BYTE.equals(
            schema.getFormat()))) {
            //spring doc open api 1.6.7 and lower doesn't do that for optional numeric types
            if (schema.getMaximum() == null || schema.getMaximum().compareTo(bigDecimalMaxAnnotated) > 0) {
                schema.setMaximum(bigDecimalMaxAnnotated);
            }
            if (!inclusiveAnnotated) {
                schema.setExclusiveMaximum(true);
                addSchemaDescription("beanValidationDecimalMax", schema, bigDecimalMaxAnnotated.toPlainString());
            } else {
                addSchemaDescription("beanValidationDecimalMaxInclusive", schema,
                    bigDecimalMaxAnnotated.toPlainString());
            }

        }
    }

    private static void extendNegative(Schema<?> schema) {
        if (Constants.TYPE_NUMBER.equals(schema.getType()) || Constants.TYPE_INTEGER.equals(schema.getType())
            || (Constants.TYPE_STRING.equals(schema.getType()) && Constants.FORMAT_TYPE_BYTE.equals(
            schema.getFormat()))) {
            //spring doc open api 1.6.7 and lower doesn't do that
            if (schema.getMaximum() == null || schema.getMaximum().intValue() > 0) {
                schema.setMaximum(BigDecimal.valueOf(0));
                schema.setExclusiveMaximum(true);
            }
            addSchemaDescription("beanValidationNegative", schema);
        }
    }

    private static void extendPositive(Schema<?> schema) {
        if (Constants.TYPE_NUMBER.equals(schema.getType()) || Constants.TYPE_INTEGER.equals(schema.getType())
            || (Constants.TYPE_STRING.equals(schema.getType()) && Constants.FORMAT_TYPE_BYTE.equals(
            schema.getFormat()))) {
            //spring doc open api 1.6.7 and lower doesn't do that
            if (schema.getMinimum() == null || schema.getMinimum().intValue() < 0) {
                schema.setMinimum(BigDecimal.valueOf(0));
                schema.setExclusiveMinimum(true);
            }
            addSchemaDescription("beanValidationPositive", schema);
        }
    }

    private static void extendNegativeOrZero(Schema<?> schema) {
        if (Constants.TYPE_NUMBER.equals(schema.getType()) || Constants.TYPE_INTEGER.equals(schema.getType())
            || (Constants.TYPE_STRING.equals(schema.getType()) && Constants.FORMAT_TYPE_BYTE.equals(
            schema.getFormat()))) {
            //spring doc open api 1.6.7 and lower doesn't do that
            if (schema.getMaximum() == null || schema.getMaximum().intValue() > 0) {
                schema.setMaximum(BigDecimal.valueOf(0));
                schema.setExclusiveMaximum(false);
            }
            addSchemaDescription("beanValidationNegativeOrZero", schema);
        }
    }

    private static void extendPositiveOrZero(Schema<?> schema) {
        if (Constants.TYPE_NUMBER.equals(schema.getType()) || Constants.TYPE_INTEGER.equals(schema.getType())
            || (Constants.TYPE_STRING.equals(schema.getType()) && Constants.FORMAT_TYPE_BYTE.equals(
            schema.getFormat()))) {
            //spring doc open api 1.6.7 and lower doesn't do that
            if (schema.getMinimum() == null || schema.getMinimum().intValue() < 0) {
                schema.setMinimum(BigDecimal.valueOf(0));
                schema.setExclusiveMinimum(false);
            }
            addSchemaDescription("beanValidationPositiveOrZero", schema);
        }
    }

    private static void extendDigits(Schema<?> schema, Integer integerAnnotated, Integer fractionAnnotated) {
        if (Constants.TYPE_NUMBER.equals(schema.getType()) || Constants.TYPE_INTEGER.equals(schema.getType())
            || (Constants.TYPE_STRING.equals(schema.getType()) && Constants.FORMAT_TYPE_BYTE.equals(
            schema.getFormat()))) {
            //spring doc open api 1.6.7 and lower doesn't do that
            if (integerAnnotated < 1) {
                log.warn("Annotating the integer part of '" + schema.getName() + "' with a value < 1 makes no sense!");
            }
            if (schema.getPattern() == null) {
                String patternInteger = "[-]?[1-9]{1,1}[0-9]{0," + (integerAnnotated - 1) + "}|[0]";
                String patternIntegerZeroNegativeOption = "|[-][0]";
                String patternFraction = "[0-9]{1," + (fractionAnnotated) + "}";
                String patternSeparator = "\\.";
                String pattern;
                if (Constants.TYPE_INTEGER.equals(schema.getType())
                    || (Constants.TYPE_STRING.equals(schema.getType()) && Constants.FORMAT_TYPE_BYTE.equals(
                    schema.getFormat()))) {
                    pattern = "^" + patternInteger + "$";
                } else {
                    if (fractionAnnotated > 0) {
                        pattern =
                            "^(" + patternInteger + patternIntegerZeroNegativeOption + "){1,1}" + "(" + patternSeparator + patternFraction + ")?$";
                        addSchemaDescription("beanValidationDigitsIntegerFraction", schema, integerAnnotated.toString(),
                            fractionAnnotated.toString());
                    } else {
                        pattern = "^" + patternInteger + "$";
                        addSchemaDescription("beanValidationDigitsInteger", schema, integerAnnotated.toString());
                    }
                }
                schema.setPattern(pattern);
            }
        }
    }

    private static void extendPast(Schema<?> schema) {
        if (isTemporalType(schema)) {
            //spring doc open api 1.6.7 and lower doesn't do that
            addSchemaDescription("beanValidationPast", schema);
        }
    }

    private static void extendPastOrPresent(Schema<?> schema) {
        if (isTemporalType(schema)) {
            //spring doc open api 1.6.7 and lower doesn't do that
            addSchemaDescription("beanValidationPastOrPresent", schema);
        }
    }

    private static void extendFuture(Schema<?> schema) {
        if (isTemporalType(schema)) {
            //spring doc open api 1.6.7 and lower doesn't do that
            addSchemaDescription("beanValidationFuture", schema);
        }
    }

    private static void extendFutureOrPresent(Schema<?> schema) {
        if (isTemporalType(schema)) {
            //spring doc open api 1.6.7 and lower doesn't do that
            addSchemaDescription("beanValidationFutureOrPresent", schema);
        }
    }

    private static boolean isTemporalType(Schema<?> schema) {
        return (
            Constants.TYPE_STRING.equals(schema.getType()) &&
                (Constants.FORMAT_TYPE_DATE.equals(schema.getFormat())
                    || Constants.FORMAT_TYPE_DATE_TIME.equals(schema.getFormat()))
        )
            || Constants.$REF_LOCAL_TIME.equals(schema.get$ref())
            || Constants.$REF_OFFSET_TIME.equals(schema.get$ref())
            || Constants.$REF_YEAR_MONTH.equals(schema.get$ref())
            || Constants.$REF_MONTH_DAY.equals(schema.get$ref())
            || Constants.$REF_YEAR.equals(schema.get$ref());
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

    private static void addSchemaDescription(String bundleKey, Schema<?> schema, String... replacement) {
        var bundle = ResourceBundle.getBundle(io.domainlifecycles.swagger.v3.resource.ResourceBundle.class.getName(),
            Locale.getDefault());
        if (schema.getDescription() == null) {
            schema.setDescription(String.format(bundle.getString(bundleKey), (Object[]) replacement));
        } else {
            var newDesc = String.format(bundle.getString(bundleKey), (Object[]) replacement);
            if (!schema.getDescription().contains(newDesc)) {
                schema.setDescription(schema.getDescription() + " " + newDesc);
            }

        }
    }


}
