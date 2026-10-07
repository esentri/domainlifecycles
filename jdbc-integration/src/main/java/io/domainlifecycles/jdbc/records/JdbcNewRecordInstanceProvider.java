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

package io.domainlifecycles.jdbc.records;

import io.domainlifecycles.persistence.records.NewRecordInstanceProvider;

/**
 * {@link NewRecordInstanceProvider} for plain JDBC based persistence. Unlike the jOOQ based
 * implementation, which loads and instantiates a generated {@code UpdatableRecord} subclass by
 * {@code recordClassName}, this implementation simply creates a new, empty {@link JdbcRecord} for the given
 * table: throughout this module, the String passed as {@code recordClassName} (a name inherited from the
 * shared {@link NewRecordInstanceProvider} contract) is always the physical table name.
 *
 * @author Mario Herb
 */
public final class JdbcNewRecordInstanceProvider implements NewRecordInstanceProvider {

    /**
     * {@inheritDoc}
     *
     * @param recordClassName the physical table name
     */
    @Override
    @SuppressWarnings("unchecked")
    public <RECORD> RECORD provideNewRecord(String recordClassName) {
        return (RECORD) new JdbcRecord(recordClassName);
    }
}
