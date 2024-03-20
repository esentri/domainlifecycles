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

package nitrox.dlc.persistence.mapping;

import nitrox.dlc.mirror.api.EntityReferenceMirror;
import nitrox.dlc.mirror.api.FieldMirror;
import nitrox.dlc.persistence.records.RecordProperty;

import java.util.List;

/**
 * Matches a record property to an entity field.
 *
 * @author Mario Herb
 */
public interface RecordPropertyMatcher {

    /**
     * Matches a record property to its according property mirror.
     *
     * @param recordProperty the record property
     * @param fieldMirror the mirror for an entity field
     * @return true if the record property matches the value object property path, false otherwise
     */
    boolean matchProperty(RecordProperty recordProperty, FieldMirror fieldMirror);

    /**
     * Matches a record property to a value object property path.
     *
     * @param recordProperty the record property
     * @param path           the value object property path
     * @return true if the record property matches the value object property path, false otherwise
     */
    boolean matchValueObjectPath(RecordProperty recordProperty, List<FieldMirror> path);

    /**
     * Matches a record property to a forward entity reference.
     *
     * @param recordProperty the record property
     * @param entityReferenceMirror           the entity reference mirror
     * @return true if the record property matches the entity reference mirror, false otherwise
     */
    boolean matchForwardReference(RecordProperty recordProperty, EntityReferenceMirror entityReferenceMirror);
}
