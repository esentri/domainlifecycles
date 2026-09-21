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
import java.util.Locale;
import java.util.UUID;

/**
 * Maps JDBC SQL types ({@link java.sql.Types}) to the Java types record properties are exposed as.
 * <p>
 * Native {@code UUID} columns (reported by H2 and PostgreSQL with the database specific type name
 * {@code "UUID"} rather than a standard {@link java.sql.Types} constant) are special-cased, since plain JDBC
 * has no {@link java.sql.Types} constant of its own for them. Postgres' {@code timestamptz}/{@code timetz}
 * columns are special-cased the same way: PostgreSQL's JDBC driver reports their {@code DATA_TYPE} as plain
 * {@link Types#TIMESTAMP}/{@link Types#TIME} rather than the {@code _WITH_TIMEZONE} variants (a
 * long-standing pgjdbc {@code DatabaseMetaData.getColumns()} limitation), which would otherwise make this
 * mapping resolve them to {@link LocalDateTime}/{@link LocalTime} and then fail every read with "Cannot
 * convert the column of type TIMESTAMPTZ to requested type java.time.LocalDateTime."
 * <p>
 * MySQL's native {@code TIMESTAMP} column is special-cased the same way again, but for a different reason:
 * unlike Postgres, MySQL's JDBC driver reports the correct {@code DATA_TYPE} ({@link Types#TIMESTAMP}), and
 * {@code TYPE_NAME} is genuinely the only signal distinguishing it from a plain, non-offset-aware {@code
 * DATETIME} column (both report {@code DATA_TYPE}, since MySQL has no {@code TIMESTAMP_WITH_TIMEZONE}
 * concept of its own) - unlike Postgres' {@code timestamptz}, this can only be resolved to {@link
 * OffsetDateTime} when the {@code databaseProductName} is actually MySQL, since H2/Oracle/SQL Server also
 * report plain {@code TYPE_NAME} {@code "TIMESTAMP"} for their own, genuinely non-offset-aware columns
 * (distinguished there by {@code DATA_TYPE} alone, i.e. the {@code switch} below).
 * <p>
 * SQL Server's {@code datetimeoffset} column is special-cased the same way again: mssql-jdbc reports its
 * {@code DATA_TYPE} as the vendor-specific {@code microsoft.sql.Types.DATETIMEOFFSET} ({@code -155}) rather
 * than the standard {@link Types#TIMESTAMP_WITH_TIMEZONE}, which the {@code switch} below has no case for
 * (see <a href="https://github.com/microsoft/mssql-jdbc/issues/1976">mssql-jdbc#1976</a>).
 * <p>
 * Oracle's {@code TIMESTAMP WITH TIME ZONE} column is special-cased for the exact same reason: the Oracle
 * JDBC driver reports its {@code DATA_TYPE} as the vendor-specific {@code oracle.jdbc.OracleTypes.TIMESTAMPTZ}
 * ({@code -101}), and its {@code TYPE_NAME} carries the column's precision inline (e.g. {@code "TIMESTAMP(6)
 * WITH TIME ZONE"}), hence the prefix/suffix match below rather than an exact one.
 *
 * @author Mario Herb
 */
public final class JdbcSqlTypeMapping {

    private JdbcSqlTypeMapping() {
    }

    /**
     * Determines the Java type used to represent values of a column with the given JDBC SQL type.
     * <p>
     * A {@code NUMERIC}/{@code DECIMAL} column with zero decimal digits (e.g. Oracle-style {@code NUMBER(18)},
     * as used for ids and version columns throughout this module's own test schema) is mapped to the
     * narrowest integer type its precision fits into, rather than to {@link BigDecimal} - the same "force
     * integer types on zero-scale decimals" convention jOOQ's code generator offers as the {@code
     * forceIntegerTypesOnZeroScaleDecimals} option (with jOOQ's own precision-to-width thresholds, so that a
     * type mapped here agrees with what jOOQ would have generated for the same column - this matters wherever
     * a converter is registered by Java type, e.g. {@code java.time.Year} only converts against {@link
     * Integer}/{@link Short}, not {@link Long}). Only a column with actual decimal digits (e.g. a monetary
     * amount) is mapped to {@link BigDecimal}.
     *
     * @param sqlType            the JDBC SQL type ({@link java.sql.Types}) as reported by the database
     * @param typeName           the database specific type name as reported by the database, used to detect
     *                           native {@code UUID} columns, Postgres/MySQL columns whose {@code DATA_TYPE}
     *                           alone does not distinguish an offset-aware column from a plain one, and SQL
     *                           Server's {@code datetimeoffset}/Oracle's {@code TIMESTAMP WITH TIME ZONE},
     *                           whose {@code DATA_TYPE} the {@code switch} below has no case for
     * @param decimalDigits      the number of decimal digits ({@code DatabaseMetaData.getColumns()}'
     *                           {@code DECIMAL_DIGITS}) the column was reported with; only meaningful for
     *                           {@code NUMERIC}/{@code DECIMAL} columns
     * @param precision          the column's precision ({@code DatabaseMetaData.getColumns()}' {@code
     *                           COLUMN_SIZE}), used to pick an integer width for a zero-scale {@code
     *                           NUMERIC}/{@code DECIMAL} column; only meaningful for {@code NUMERIC}/{@code
     *                           DECIMAL} columns
     * @param databaseProductName {@code DatabaseMetaData.getDatabaseProductName()}, used to scope the
     *                           MySQL-only {@code TIMESTAMP} special case above to actual MySQL connections
     * @return the Java type values of such a column are mapped to
     */
    public static Class<?> javaType(
        int sqlType, String typeName, int decimalDigits, int precision, String databaseProductName
    ) {
        if (typeName != null) {
            var normalizedTypeName = typeName.toLowerCase(Locale.ROOT);
            if ("uuid".equals(normalizedTypeName)) {
                return UUID.class;
            }
            if ("timestamptz".equals(normalizedTypeName)) {
                return OffsetDateTime.class;
            }
            if ("timetz".equals(normalizedTypeName)) {
                return OffsetTime.class;
            }
            if ("timestamp".equals(normalizedTypeName) && "MySQL".equalsIgnoreCase(databaseProductName)) {
                return OffsetDateTime.class;
            }
            if ("datetimeoffset".equals(normalizedTypeName)) {
                return OffsetDateTime.class;
            }
            if (normalizedTypeName.startsWith("timestamp") && normalizedTypeName.endsWith("with time zone")) {
                return OffsetDateTime.class;
            }
        }
        return switch (sqlType) {
            case Types.BIT, Types.BOOLEAN -> Boolean.class;
            case Types.TINYINT -> Byte.class;
            case Types.SMALLINT -> Short.class;
            case Types.INTEGER -> Integer.class;
            case Types.BIGINT -> Long.class;
            case Types.REAL -> Float.class;
            case Types.FLOAT, Types.DOUBLE -> Double.class;
            case Types.DECIMAL, Types.NUMERIC -> decimalDigits == 0 ? integerTypeForPrecision(precision) : BigDecimal.class;
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

    private static Class<?> integerTypeForPrecision(int precision) {
        if (precision <= 2) {
            return Byte.class;
        }
        if (precision <= 4) {
            return Short.class;
        }
        if (precision <= 9) {
            return Integer.class;
        }
        return Long.class;
    }
}
