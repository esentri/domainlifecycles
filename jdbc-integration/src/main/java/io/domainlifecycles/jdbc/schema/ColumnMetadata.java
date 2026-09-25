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

/**
 * Metadata of a single database column, as discovered via {@link java.sql.DatabaseMetaData}.
 *
 * @param name       the physical column name, exactly as reported by the database
 * @param sqlType    the JDBC SQL type ({@link java.sql.Types}) of the column
 * @param typeName   the database specific type name (e.g. {@code "UUID"} on H2/Postgres), as reported by the
 *                   database
 * @param javaType   the Java type that values of this column are mapped to
 * @param precision  the column's precision ({@code DatabaseMetaData.getColumns()}' {@code COLUMN_SIZE}) -
 *                   for a {@code VARCHAR}/{@code CHAR} column, its declared character length; for a
 *                   {@code BINARY}/{@code VARBINARY} column, its declared byte length
 * @param nullable   whether the column accepts {@code NULL} values
 * @param primaryKey whether the column is (part of) the table's primary key
 * @author Mario Herb
 */
public record ColumnMetadata(
    String name,
    int sqlType,
    String typeName,
    Class<?> javaType,
    int precision,
    boolean nullable,
    boolean primaryKey
) {
}
