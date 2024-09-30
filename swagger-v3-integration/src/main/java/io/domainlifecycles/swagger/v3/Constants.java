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

/**
 * Constants used by the DLC Open API extension (SpringDoc / Open API specific constants).
 *
 * @author Mario Herb
 */
public class Constants {

    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_ARRAY = "array";

    public static final String FORMAT_TYPE_EMAIL = "email"; //not specified in OPEN API, but should work
    public static final String FORMAT_TYPE_BYTE = "byte";
    public static final String FORMAT_TYPE_DATE = "date";
    public static final String FORMAT_TYPE_DATE_TIME = "date-time";

    public static final String PATTERN_LOCAL_TIME = "^([0-1][0-9]|[2][0-4])[:]([0-5][0-9])[:]([0-5][0-9])(\\.[0-9]{1," +
        "6})?$";

    public static final String PATTERN_OFFSET_TIME = "^([0-1][0-9]|[2][0-4])[:]([0-5][0-9])[:]([0-5][0-9])(\\" +
        ".[0-9]{1,6})?([+]|[-])[0-9]{2,2}[:][0-9]{2,2}$";

    public static final String PATTERN_YEAR_MONTH = "^[0-9]{4,4}[-][0-1][0-9]$";

    public static final String PATTERN_MONTH_DAY = "^--[0-1][0-9]-[0-3][0-9]$";

    public static final String PATTERN_YEAR = "^([+]|[-])?[0-9]{4,4}$";

    public static final String $REF_LOCAL_TIME = "#/components/schemas/java.time.LocalTime";
    public static final String $REF_OFFSET_TIME = "#/components/schemas/java.time.OffsetTime";
    public static final String $REF_YEAR_MONTH = "#/components/schemas/java.time.YearMonth";
    public static final String $REF_MONTH_DAY = "#/components/schemas/java.time.MonthDay";
    public static final String $REF_YEAR = "#/components/schemas/java.time.Year";

    public static final String IGNORED_RESPONSE_OBJECT_FQN = "io.domainlifecycles.spring.http.ResponseObject";

}
