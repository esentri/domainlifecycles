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

package io.domainlifecycles.jooq.imp.matcher;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.persistence.records.RecordTypeToEntityTypeMatcher;
import org.jooq.UpdatableRecord;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

/**
 * jOOQ specific implementation of a {@link RecordTypeToEntityTypeMatcher}.
 *
 * @author Mario Herb
 */
public class JooqRecordTypeToEntityTypeMatcher implements RecordTypeToEntityTypeMatcher<UpdatableRecord<?>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Class<? extends UpdatableRecord<?>>> findMatchingRecordType(Set<Class<? extends UpdatableRecord<?>>> availableRecordTypes, String entityTypeName) {
        var matchedRecordType = availableRecordTypes
            .stream()
            .sorted(Comparator.comparing(Class::getSimpleName))
            .filter(r -> exactMatch(r, entityTypeName))
            .findFirst();
        if (matchedRecordType.isPresent()) {
            return matchedRecordType;
        } else {
            var em = Domain.entityMirrorFor(entityTypeName);
            for (String superType : em.getInheritanceHierarchyTypeNames()) {
                matchedRecordType = availableRecordTypes
                    .stream()
                    .sorted(Comparator.comparing(Class::getSimpleName))
                    .filter(r -> exactMatch(r, superType))
                    .findFirst();
                if (matchedRecordType.isPresent()) {
                    return matchedRecordType;
                }

            }
        }
        return Optional.empty();
    }

    private boolean exactMatch(Class<? extends UpdatableRecord<?>> recordType, String fullQualifiedEntityTypeName) {
        var simpleEntityTypeName = fullQualifiedEntityTypeName;
        var dotPlusOne = fullQualifiedEntityTypeName.lastIndexOf(".")+1;
        if(fullQualifiedEntityTypeName.contains(".") && fullQualifiedEntityTypeName.length()>dotPlusOne) {
            simpleEntityTypeName = fullQualifiedEntityTypeName.substring(dotPlusOne);
        }
        return recordType.getSimpleName().replaceAll("_", "").equals(simpleEntityTypeName + "Record");
    }
}
