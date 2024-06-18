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


import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.persistence.mapping.RecordPropertyMatcher;
import io.domainlifecycles.persistence.records.RecordProperty;

import java.util.List;

/**
 * jOOQ specific implementation of a {@link RecordPropertyMatcher}.
 *
 * @author Mario Herb
 */
public class JooqRecordPropertyMatcher implements RecordPropertyMatcher {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matchProperty(RecordProperty recordProperty, FieldMirror fieldMirror) {

        return recordProperty.getName().toLowerCase().replaceAll("_", "")
            .equals(fieldMirror.getName().toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matchValueObjectPath(RecordProperty recordProperty, List<FieldMirror> path) {
        var pathName = new StringBuilder();
        path.forEach(e -> pathName.append(e.getName()));
        return recordProperty.getName()
            .toLowerCase()
            .replaceAll("_", "")
            .equals(pathName.toString().toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matchForwardReference(RecordProperty recordProperty, EntityReferenceMirror entityReferenceMirror) {
        return recordProperty.getName().substring(0, recordProperty.getName().length() - 2).toLowerCase().replaceAll("_", "")
            .equals(entityReferenceMirror.getName().toLowerCase());
    }
}
