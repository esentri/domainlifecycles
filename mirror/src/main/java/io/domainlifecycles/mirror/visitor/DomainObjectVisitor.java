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

package io.domainlifecycles.mirror.visitor;

import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.ValueMirror;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;

/**
 * A {@link DomainObjectVisitor} visits the complete object tree (types and fields) of any domain type that
 * is mirrored by an instance of {@link DomainTypeMirror}.
 *
 * @author Mario Herb
 */
public interface DomainObjectVisitor {

    /**
     * Called when entering any domain type {@link DomainTypeMirror}
     * @param domainTypeMirror mirror for domain type
     */
    void visitEnterAnyDomainType(DomainTypeMirror domainTypeMirror);

    /**
     * Called when entering an {@link AggregateRootMirror}
     *
     * @param aggregateRootMirror AggregateRoot mirror
     * @return true, to continue visiting the children, false to stop descending
     */
    boolean visitEnterAggregateRoot(AggregateRootMirror aggregateRootMirror);

    /**
     * Called when entering an {@link EntityMirror}
     *
     * @param entityMirror entity mirror
     * @return true, to continue visiting the children, false to stop descending
     */
    boolean visitEnterEntity(EntityMirror entityMirror);

    /**
     * Called when entering a {@link ValueMirror}
     *
     * @param valueMirror value mirror
     * @return true, to continue visiting the children, false to stop descending
     */
    boolean visitEnterValue(ValueMirror valueMirror);

    /**
     * Called when leaving an {@link AggregateRootMirror}
     * @param aggregateRootMirror mirror for AggregateRoot
     */
    void visitLeaveAggregateRoot(AggregateRootMirror aggregateRootMirror);

    /**
     * Called when leaving an {@link EntityMirror}
     * @param entityMirror entity mirror
     */
    void visitLeaveEntity(EntityMirror entityMirror);

    /**
     * Called when leaving a {@link ValueMirror}
     * @param valueMirror value mirror
     */
    void visitLeaveValue(ValueMirror valueMirror);


    /**
     * Called when passing a {@link AggregateRootReferenceMirror}
     * @param aggregateRootReferenceMirror mirror for AggregateRoot reference
     */
    void visitAggregateRootReference(AggregateRootReferenceMirror aggregateRootReferenceMirror);

    /**
     * Called when passing a {@link EntityReferenceMirror}
     * @param entityReferenceMirror mirror for entity reference
     */
    void visitEntityReference(EntityReferenceMirror entityReferenceMirror);

    /**
     * Called when passing a {@link ValueReferenceMirror}
     * @param valueReferenceMirror mirror for value reference
     */
    void visitValueReference(ValueReferenceMirror valueReferenceMirror);


    /**
     * Called when passing a basic {@link FieldMirror}
     * @param basicFieldMirror mirror for basic field
     */
    void visitBasic(FieldMirror basicFieldMirror);

    /**
     * Called when passing an identity property {@link FieldMirror}
     * @param idPropertyMirror mirror for identity property
     */
    void visitEntityId(FieldMirror idPropertyMirror);
}
