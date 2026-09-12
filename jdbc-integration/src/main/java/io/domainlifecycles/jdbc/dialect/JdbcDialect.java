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

package io.domainlifecycles.jdbc.dialect;

/**
 * Encapsulates the small set of SQL differences between database products that this module's otherwise
 * database-agnostic SQL generation needs to know about. jOOQ absorbs these differences internally; plain
 * JDBC does not, so they are made explicit here instead.
 *
 * @author Mario Herb
 */
public interface JdbcDialect {

    /**
     * Returns a short, descriptive name of this dialect (e.g. {@code "H2"}), used in log and error messages.
     *
     * @return the dialect name
     */
    String name();

    /**
     * Returns a SQL statement that selects the next value of the given sequence.
     * <p>
     * The statement must produce a single row with a single column containing the next sequence value.
     *
     * @param sequenceName the physical name of the sequence
     * @return the dialect specific SQL statement to obtain the next sequence value
     */
    String nextSequenceValueSql(String sequenceName);
}
