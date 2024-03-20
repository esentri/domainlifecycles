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

import nitrox.dlc.jooq.util.NamingUtil;
import nitrox.dlc.persistence.exception.DLCPersistenceException;
import nitrox.dlc.persistence.records.NewRecordInstanceProvider;
import nitrox.dlc.persistence.records.RecordProperty;
import nitrox.dlc.persistence.records.RecordPropertyProvider;
import org.jooq.ForeignKey;
import org.jooq.UpdatableRecord;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * jOOQ specific implementation of a {@link RecordPropertyProvider}.
 *
 * @author Mario Herb
 */
public class JooqRecordPropertyProvider implements RecordPropertyProvider {

    private final NewRecordInstanceProvider newRecordInstanceProvider;

    public JooqRecordPropertyProvider(NewRecordInstanceProvider newRecordInstanceProvider) {
        this.newRecordInstanceProvider = newRecordInstanceProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RecordProperty> provideProperties(String recordClassName) {

        UpdatableRecord<?> r = newRecordInstanceProvider.provideNewRecord(recordClassName);
        if (Objects.requireNonNull(r.getTable().getPrimaryKey()).getFields().size() > 1) {
            throw DLCPersistenceException.fail("Only single fielded primary keys are supported! Contradicting table=" + r.getTable().getName());
        }
        String pkFieldName = NamingUtil.snakeCaseToCamelCase((r.getTable().getPrimaryKey().getFields().get(0)).getName().toLowerCase());
        Class<?> recordClass;
        try {
            recordClass = Class.forName(recordClassName);
        } catch (ClassNotFoundException e) {
            throw DLCPersistenceException.fail(String.format("RecordClass %s not found", recordClassName), e);
        }
        return Arrays.stream(recordClass.getDeclaredMethods())
            .filter(m -> m.getName().startsWith("get"))
            .map(m -> {
                String propName = m.getName().substring(3);
                propName = propName.substring(0, 1).toLowerCase() + propName.substring(1);
                final String propNameCopy = propName;
                Class<?> valueType = m.getReturnType();
                boolean nonNullForeignKey = r.getTable()
                    .getReferences()
                    .stream()
                    .flatMap(fk -> ((ForeignKey<?,?>) fk).getFields().stream())
                    .filter(tf -> {
                        String name = NamingUtil.snakeCaseToCamelCase(tf.getUnqualifiedName().last());
                        return name.equals(propNameCopy);
                    })
                    .anyMatch(tf -> !tf.getDataType().nullable());
                return new RecordProperty(propName, recordClassName, valueType, propName.equals(pkFieldName), nonNullForeignKey);
            })
            .toList();
    }

}
