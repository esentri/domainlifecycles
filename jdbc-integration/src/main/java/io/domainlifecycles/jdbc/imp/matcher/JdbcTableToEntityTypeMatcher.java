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

package io.domainlifecycles.jdbc.imp.matcher;

import io.domainlifecycles.mirror.api.Domain;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

/**
 * Matches a physical table name to an entity type.
 * <p>
 * This deliberately does not implement the shared {@code RecordTypeToEntityTypeMatcher<RECORD_TYPE>}
 * interface (used by the jOOQ based integration): that interface returns a {@code Class<? extends
 * RECORD_TYPE>}, which presupposes one distinct Java class per table. In this module every table is
 * represented by the very same {@link io.domainlifecycles.jdbc.records.JdbcRecord} class, so the only
 * meaningful per-table identifier is the physical table name String returned here.
 * <p>
 * The matching rule mirrors the jOOQ based integration's convention (entity simple name, case-insensitively
 * equal to the table name once underscores are stripped, e.g. entity {@code TestRoot} matches table
 * {@code TEST_ROOT}), minus the "Record" suffix jOOQ's generated class names carry.
 *
 * @author Mario Herb
 */
public class JdbcTableToEntityTypeMatcher {

    /**
     * Finds the matching table name for an entity.
     *
     * @param availableTableNames the physical names of all tables known to the schema
     * @param entityTypeName      the full qualified entity type name
     * @return the matching table name
     */
    public Optional<String> findMatchingTable(Set<String> availableTableNames, String entityTypeName) {
        var matchedTable = availableTableNames
            .stream()
            .sorted(Comparator.naturalOrder())
            .filter(t -> exactMatch(t, entityTypeName))
            .findFirst();
        if (matchedTable.isPresent()) {
            return matchedTable;
        }
        var em = Domain.entityMirrorFor(entityTypeName);
        for (String superType : em.getInheritanceHierarchyTypeNames()) {
            matchedTable = availableTableNames
                .stream()
                .sorted(Comparator.naturalOrder())
                .filter(t -> exactMatch(t, superType))
                .findFirst();
            if (matchedTable.isPresent()) {
                return matchedTable;
            }
        }
        return Optional.empty();
    }

    private boolean exactMatch(String tableName, String fullQualifiedEntityTypeName) {
        var simpleEntityTypeName = fullQualifiedEntityTypeName;
        var dotPlusOne = fullQualifiedEntityTypeName.lastIndexOf(".") + 1;
        if (fullQualifiedEntityTypeName.contains(".") && fullQualifiedEntityTypeName.length() > dotPlusOne) {
            simpleEntityTypeName = fullQualifiedEntityTypeName.substring(dotPlusOne);
        }
        return tableName.replaceAll("_", "").equalsIgnoreCase(simpleEntityTypeName);
    }
}
