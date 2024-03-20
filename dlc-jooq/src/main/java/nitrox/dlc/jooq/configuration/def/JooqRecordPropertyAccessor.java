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

package nitrox.dlc.jooq.configuration.def;

import nitrox.dlc.persistence.exception.DLCPersistenceException;
import nitrox.dlc.persistence.records.RecordProperty;
import nitrox.dlc.persistence.records.RecordPropertyAccessor;
import org.jooq.UpdatableRecord;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * jOOQ specific implementation of a {@link RecordPropertyAccessor}.
 *
 * @author Mario Herb
 */
public class JooqRecordPropertyAccessor implements RecordPropertyAccessor<UpdatableRecord<?>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPropertyValue(RecordProperty property, UpdatableRecord<?> updatableRecord, Object value) {
        Class<? extends UpdatableRecord> recordClass = updatableRecord.getClass();
        try {
            var m = Arrays.stream(recordClass.getDeclaredMethods())
                .filter(dm -> dm.getName().equals(methodName("set", property)))
                .findFirst()
                .orElseThrow();
            m.invoke(updatableRecord, value);
        } catch (IllegalArgumentException | NoSuchElementException |
                 IllegalAccessException | InvocationTargetException e) {
            throw DLCPersistenceException.fail("Setting record value failed for '%s' with value '%s'.", e, property, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPropertyValue(RecordProperty property, UpdatableRecord<?> updatableRecord) {
        Class<? extends UpdatableRecord<?>> recordClass = (Class<? extends UpdatableRecord<?>>) updatableRecord.getClass();
        try {
            Method m = recordClass.getDeclaredMethod(methodName("get", property));
            return m.invoke(updatableRecord);
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw DLCPersistenceException.fail("Getting record value failed for '%s'.", e, property);
        }
    }


    private String methodName(String prefix, RecordProperty property) {
        return prefix + property.getName().substring(0, 1).toUpperCase() + property.getName().substring(1);
    }
}
