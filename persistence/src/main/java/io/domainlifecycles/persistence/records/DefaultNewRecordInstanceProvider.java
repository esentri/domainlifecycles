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

package io.domainlifecycles.persistence.records;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;

import java.lang.reflect.InvocationTargetException;

/**
 * Provides new record instances.
 *
 * @author Mario Herb
 */
public class DefaultNewRecordInstanceProvider implements NewRecordInstanceProvider {

    /**
     * Provides a new record instance for the given record type.
     *
     * @param recordClassName the full qualified name of record type
     * @return the new record instance
     */
    @Override
    public <RECORD> RECORD provideNewRecord(String recordClassName) {
        try {
            var clazz = DlcAccess.getClassForName(recordClassName);
            return (RECORD) clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 NoSuchMethodException | InvocationTargetException ex) {
            throw DLCPersistenceException.fail("Not able to create new record instance for '%s'.", ex, recordClassName);
        }
    }


}
