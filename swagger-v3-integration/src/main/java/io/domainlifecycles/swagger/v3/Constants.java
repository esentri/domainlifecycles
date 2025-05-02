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

/**
 * Constants used by the DLC Open API extension (SpringDoc / Open API specific constants).
 *
 * @author Mario Herb
 */
public class Constants {

    /**
     * Represents the OpenAPI type "number".
     * This is typically used in API schemas to define fields that accept numeric values,
     * including both integer and floating-point representations.
     */
    public static final String TYPE_NUMBER = "number";

    /**
     * Represents the OpenAPI type "integer".
     * This constant is used to define schema fields that accept integer values.
     */
    public static final String TYPE_INTEGER = "integer";

    /**
     * Represents the OpenAPI type "string".
     * This constant is used in API schemas to define fields that accept string values.
     */
    public static final String TYPE_STRING = "string";

    /**
     * Represents the OpenAPI type "array".
     * This constant is used in API schemas to define fields that accept array values.
     */
    public static final String TYPE_ARRAY = "array";

    /**
     * Represents the OpenAPI format type "email".
     * This format is typically used for fields that are expected to contain email addresses,
     * adhering to standard email format validation rules.
     *
     * Note: While not explicitly outlined in the OpenAPI specification,
     * this value can be utilized to describe "email" formatted string properties in an API schema.
     */
    public static final String FORMAT_TYPE_EMAIL = "email";

    /**
     * Represents the OpenAPI format type "byte".
     * This format is typically used for fields that are expected to contain Base64-encoded binary data.
     * It is often used in API schemas for transferring binary content in a text-based format.
     */
    public static final String FORMAT_TYPE_BYTE = "byte";

    /**
     * Defines a constant representing the format type for date values.
     * This is used to indicate that a particular data type or field conforms
     * to a date format.
     */
    public static final String FORMAT_TYPE_DATE = "date";

    /**
     * Specifies the format type for date-time values.
     * The value represents a combined date and time format as defined in
     * various data exchange and validation standards.
     */
    public static final String FORMAT_TYPE_DATE_TIME = "date-time";

    /**
     * A regex pattern string representing the validation format for a local time value.
     *
     * This pattern enforces the format HH:mm:ss, where:
     * - HH represents the hour in 24-hour format, allowing valid values between 00 and 24.
     * - mm represents the minute, allowing valid values between 00 and 59.
     * - ss represents the second, allowing valid values between 00 and 59.
     *
     * Additionally, the pattern supports an optional fractional second component represented
     * by a decimal point followed by up to 6 digits (e.g., .123456).
     */
    public static final String PATTERN_LOCAL_TIME = "^([0-1][0-9]|[2][0-4])[:]([0-5][0-9])[:]([0-5][0-9])(\\.[0-9]{1," +
        "6})?$";

    /**
     * A regular expression pattern for matching an offset time format.
     * The expected format corresponds to HH:mm:ss[.SSSSSS][+|-]HH:mm, where:
     *  - HH represents the hour in two digits (00-24).
     *  - mm represents the minutes in two digits (00-59).
     *  - ss represents the seconds in two digits (00-59).
     *  - .SSSSSS (optional) represents fractional seconds with up to six digits.
     *  - [+|-]HH:mm represents the time zone offset with hours and minutes.
     */
    public static final String PATTERN_OFFSET_TIME = "^([0-1][0-9]|[2][0-4])[:]([0-5][0-9])[:]([0-5][0-9])(\\" +
        ".[0-9]{1,6})?([+]|[-])[0-9]{2,2}[:][0-9]{2,2}$";

    /**
     * A regular expression pattern representing the format of a year and month.
     * The pattern matches strings in the "YYYY-MM" format, where:
     * - "YYYY" is a 4-digit representation of the year.
     * - "MM" is a 2-digit representation of the month, ranging from 01 to 12.
     * The format strictly enforces the presence of a hyphen ('-') as a separator
     * between the year and month components.
     */
    public static final String PATTERN_YEAR_MONTH = "^[0-9]{4,4}[-][0-1][0-9]$";

    /**
     * A regular expression pattern that validates the format of a date string representing
     * only the month and day in the format "--MM-DD". The pattern ensures:
     * - It matches strings starting with "--".
     * - The "MM" portion represents a valid two-digit month (01-12).
     * - The "DD" portion represents a valid two-digit day (00-39).
     */
    public static final String PATTERN_MONTH_DAY = "^--[0-1][0-9]-[0-3][0-9]$";

    /**
     * A regular expression pattern representing a year format.
     *
     * The pattern allows an optional '+' or '-' sign followed by exactly four digits.
     * This can be used to validate or match strings representing years, with or without signed offsets.
     */
    public static final String PATTERN_YEAR = "^([+]|[-])?[0-9]{4,4}$";

    /**
     * A constant that holds the JSON Reference (JSON Pointer) path string for the
     * schema definition of the Java `java.time.LocalTime` class. This reference is
     * intended to be used in OpenAPI or JSON Schema specifications to define or point
     * to the schema representing a `LocalTime` object.
     */
    public static final String $REF_LOCAL_TIME = "#/components/schemas/java.time.LocalTime";

    /**
     * A constant reference to the OffsetTime schema definition in the components section
     * of a specification file. This is typically used to represent a date-time with an
     * offset from UTC in ISO-8601 format, adhering to the `java.time.OffsetTime` type.
     */
    public static final String $REF_OFFSET_TIME = "#/components/schemas/java.time.OffsetTime";

    /**
     * Represents a reference to the schema definition for the {@code java.time.YearMonth} type
     * within the components section of an OpenAPI specification.
     * This variable is used to standardize and ensure consistent referencing of the YearMonth schema
     * definition throughout the codebase.
     */
    public static final String $REF_YEAR_MONTH = "#/components/schemas/java.time.YearMonth";

    /**
     * A constant reference to the JSON schema representation of a {@code java.time.MonthDay}.
     * This reference can be used to define the format and structure expected for this type
     * in a standardized schema specification.
     */
    public static final String $REF_MONTH_DAY = "#/components/schemas/java.time.MonthDay";

    /**
     * A reference string pointing to the JSON Schema definition for a {@code java.time.Year}.
     * This is used to provide a standardized schema location for the representation
     * of a {@code java.time.Year} object in API specifications or related contexts.
     */
    public static final String $REF_YEAR = "#/components/schemas/java.time.Year";

    /**
     * A fully qualified name (FQN) of the class `ResponseObject` that represents
     * a specific type within the domain's lifecycle or HTTP-related operations.
     * This constant is used to identify or ignore this particular class in specific contexts
     * where such identification or filtering is required.
     */
    public static final String IGNORED_RESPONSE_OBJECT_FQN = "io.domainlifecycles.spring.http.ResponseObject";

}
