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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.jdbc.schema;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.UUID;

/**
 * Maps JDBC SQL types ({@link java.sql.Types}) to the Java types record properties are exposed as.
 * <p>
 * Native {@code UUID} columns (reported by H2 and PostgreSQL with the database specific type name
 * {@code "UUID"} rather than a standard {@link java.sql.Types} constant) are special-cased, since plain JDBC
 * has no {@link java.sql.Types} constant of its own for them.
 *
 * @author Mario Herb
 */
public final class JdbcSqlTypeMapping {

    private JdbcSqlTypeMapping() {
    }

    /**
     * Determines the Java type used to represent values of a column with the given JDBC SQL type.
     *
     * @param sqlType  the JDBC SQL type ({@link java.sql.Types}) as reported by the database
     * @param typeName the database specific type name as reported by the database, used to detect
     *                 native {@code UUID} columns
     * @return the Java type values of such a column are mapped to
     */
    public static Class<?> javaType(int sqlType, String typeName) {
        if (typeName != null && "UUID".equalsIgnoreCase(typeName)) {
            return UUID.class;
        }
        return switch (sqlType) {
            case Types.BIT, Types.BOOLEAN -> Boolean.class;
            case Types.TINYINT -> Byte.class;
            case Types.SMALLINT -> Short.class;
            case Types.INTEGER -> Integer.class;
            case Types.BIGINT -> Long.class;
            case Types.REAL -> Float.class;
            case Types.FLOAT, Types.DOUBLE -> Double.class;
            case Types.DECIMAL, Types.NUMERIC -> BigDecimal.class;
            case Types.CHAR, Types.VARCHAR, Types.LONGVARCHAR,
                 Types.NCHAR, Types.NVARCHAR, Types.LONGNVARCHAR -> String.class;
            case Types.DATE -> LocalDate.class;
            case Types.TIME -> LocalTime.class;
            case Types.TIMESTAMP -> LocalDateTime.class;
            case Types.TIME_WITH_TIMEZONE -> OffsetTime.class;
            case Types.TIMESTAMP_WITH_TIMEZONE -> OffsetDateTime.class;
            case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY -> byte[].class;
            default -> Object.class;
        };
    }
}
