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
import java.util.Optional;

/**
 * A DomainCommandMirror mirrors a {@link nitrox.dlc.domain.types.DomainCommand}.
 *
 * @author Mario Herb
 */
public interface DomainCommandMirror extends DomainTypeMirror{

    /**
     * Returns the basic fields of the mirrored {@link nitrox.dlc.domain.types.DomainCommand}.
     * @return list of {@link FieldMirror} instances, that are not Value references, Entity references or AggregateRoot references
     */
    List<FieldMirror> getBasicFields();

    /**
     * Returns a list mirrors for value references (references of {@link nitrox.dlc.domain.types.ValueObject}, Enums or {@link nitrox.dlc.domain.types.Identity}) of the mirrored DomainCommand.
     */
    List<ValueReferenceMirror> getValueReferences();

    /**
     * Returns a list mirrors for {@link nitrox.dlc.domain.types.Entity} references  of the mirrored DomainCommand.
     */
    List<EntityReferenceMirror> getEntityReferences();

    /**
     * Returns a list mirrors for {@link nitrox.dlc.domain.types.AggregateRoot} references of the mirrored DomainCommand.
     */
    List<AggregateRootReferenceMirror> getAggregateRootReferences();

    /**
     * If the mirrored DomainCommand is targets a specific Aggregate, the corresponding mirror for the {@link nitrox.dlc.domain.types.AggregateRoot}
     * is returned wrapped in a Java Optional, else it is empty.
     */
    Optional<AggregateRootMirror> getAggregateTarget();

    /**
     * If the mirrored DomainCommand is targets a specific {@link nitrox.dlc.domain.types.DomainService}, the corresponding mirror
     * is returned wrapped in a Java Optional, else it is empty.
     */
    Optional<DomainServiceMirror> getDomainServiceTarget();

}
