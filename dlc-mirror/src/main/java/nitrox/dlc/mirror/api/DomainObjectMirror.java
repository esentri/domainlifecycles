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

package nitrox.dlc.mirror.api;

import java.util.List;

/**
 * The basic mirror for all {@link nitrox.dlc.domain.types.internal.DomainObject} instances.
 * DomainObjects are either instances of {@link nitrox.dlc.domain.types.ValueObject} or {@link nitrox.dlc.domain.types.Entity}
 * (Every {@link nitrox.dlc.domain.types.AggregateRoot} is an {@link nitrox.dlc.domain.types.Entity}).
 *
 * @author Mario Herb
 */
public interface DomainObjectMirror extends DomainTypeMirror {
    /**
     * Return a list mirrored fields of the mirrored DomainObject (all fields that are not in the category of other kinds of references).
     */
    List<FieldMirror> getBasicFields();
    /**
     * Return a list mirrors for value references (references of {@link nitrox.dlc.domain.types.ValueObject}, Enums or {@link nitrox.dlc.domain.types.Identity}) of the mirrored DomainObject.
     */
    List<ValueReferenceMirror> getValueReferences();

    /**
     * Returns a {@link ValueReferenceMirror} for the reference with the given name.
     */
    ValueReferenceMirror valueReferenceByName(String name);

}
