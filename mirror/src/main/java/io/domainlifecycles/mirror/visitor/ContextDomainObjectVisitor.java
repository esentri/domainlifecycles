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

package io.domainlifecycles.mirror.visitor;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.AggregateRootReferenceMirror;
import io.domainlifecycles.mirror.api.DomainObjectMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.ValueMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Visits the complete domain type tree (type and field callback) in a depths-first manner.
 *
 * @author Mario Herb
 */
public abstract class ContextDomainObjectVisitor implements DomainObjectVisitor {

    private final static Logger log = LoggerFactory.getLogger(ContextDomainObjectVisitor.class);
    private final VisitorContext visitorContext;
    private final DomainObjectMirror startingMirror;
    private boolean visitTypesOnlyOnce = true;
    private boolean ignoreStaticFields = true;
    private boolean ignoreHiddenFields = true;

    /**
     * Creates a new visitor instance by passing a starting domain object mirror.
     *
     * @param domainTypeMirror   mirror for domain object
     * @param visitTypesOnlyOnce prevents endless loops, by assuring that every type is visited only once
     */

    public ContextDomainObjectVisitor(DomainObjectMirror domainTypeMirror, boolean visitTypesOnlyOnce) {
        this(domainTypeMirror);
        this.visitTypesOnlyOnce = visitTypesOnlyOnce;
    }

    /**
     * Creates a new visitor instance by passing a starting domain type mirror.
     *
     * @param domainObjectMirror mirror for DomainObject
     */
    public ContextDomainObjectVisitor(DomainObjectMirror domainObjectMirror) {
        this.visitorContext = new VisitorContext(domainObjectMirror.getTypeName());
        this.startingMirror = domainObjectMirror;
    }

    /**
     * Start the visiting process.
     */
    public void start() {
        walkType(startingMirror);
    }

    private void walkType(DomainTypeMirror domainTypeMirror) {
        var currentTypeMirror = visitorContext.getCurrentType();
        var descend = enterDomainType(domainTypeMirror);
        visitorContext.enterType(domainTypeMirror);
        if (descend) {
            domainTypeMirror
                .getAllFields()
                .stream()
                .filter(fm -> !ignoreStaticFields || !fm.isStatic())
                .filter(fm -> !ignoreHiddenFields || !fm.isHidden())
                .forEach(this::walkField);
        }
        leaveDomainType(domainTypeMirror);
        visitorContext.leaveType(currentTypeMirror);
    }

    private boolean enterDomainType(DomainTypeMirror domainTypeMirror) {
        visitEnterAnyDomainType(domainTypeMirror);
        return switch (domainTypeMirror.getDomainType()) {
            case ENTITY -> visitEnterEntity((EntityMirror) domainTypeMirror);
            case ENUM, VALUE_OBJECT, IDENTITY -> visitEnterValue((ValueMirror) domainTypeMirror);
            case AGGREGATE_ROOT -> visitEnterAggregateRoot((AggregateRootMirror) domainTypeMirror);
            default -> true;
        };
    }

    private void leaveDomainType(DomainTypeMirror domainTypeMirror) {
        switch (domainTypeMirror.getDomainType()) {
            case ENTITY -> visitLeaveEntity((EntityMirror) domainTypeMirror);
            case ENUM, VALUE_OBJECT, IDENTITY -> visitLeaveValue((ValueMirror) domainTypeMirror);
            case AGGREGATE_ROOT -> visitLeaveAggregateRoot((AggregateRootMirror) domainTypeMirror);
        }
    }

    private void walkField(FieldMirror fieldMirror) {
        visitorContext.visitFieldStart(fieldMirror);
        switch (fieldMirror.getType().getDomainType()) {
            case ENTITY -> walkEntityReference((EntityReferenceMirror) fieldMirror);
            case ENUM, VALUE_OBJECT, IDENTITY -> walkValueReference((ValueReferenceMirror) fieldMirror);
            case AGGREGATE_ROOT -> walkAggregateRootReference((AggregateRootReferenceMirror) fieldMirror);
            default -> walkBasicField(fieldMirror);
        }
        visitorContext.visitFieldEnd(fieldMirror);
    }

    private void walkBasicField(FieldMirror fieldMirror) {
        visitBasic(fieldMirror);
    }

    private void walkValueReference(ValueReferenceMirror valueReferenceMirror) {
        boolean isIdentityField = false;
        if (DomainType.ENTITY.equals(getVisitorContext().getCurrentType().getDomainType())
            || DomainType.AGGREGATE_ROOT.equals(getVisitorContext().getCurrentType().getDomainType())
        ) {
            var entityMirror = (EntityMirror) getVisitorContext().getCurrentType();
            if (entityMirror.getIdentityField().isPresent() && entityMirror.getIdentityField().get().getName().equals(
                valueReferenceMirror.getName())) {
                isIdentityField = true;
            }
        }
        if (isIdentityField) {
            visitEntityId(valueReferenceMirror);
        } else {
            visitValueReference(valueReferenceMirror);
        }
        if (!visitTypesOnlyOnce || !visitorContext.isAlreadyVisited(valueReferenceMirror.getType().getTypeName())) {
            if (!Identity.class.getName().equals(valueReferenceMirror.getType().getTypeName())) {
                try {
                    walkType(valueReferenceMirror.getValue());
                } catch (Throwable t) {
                    log.warn(
                        "Couldn't walk down " + valueReferenceMirror.getType().getTypeName() + "." + valueReferenceMirror.getName(),
                        t);
                }
            }
        }
    }


    private void walkEntityReference(EntityReferenceMirror entityReferenceMirror) {
        visitEntityReference(entityReferenceMirror);
        if (!visitTypesOnlyOnce || !visitorContext.isAlreadyVisited(entityReferenceMirror.getType().getTypeName())) {
            try {
                walkType(entityReferenceMirror.getEntity());
            } catch (Throwable t) {
                log.warn(
                    "Couldn't walk down " + entityReferenceMirror.getType().getTypeName() + "." + entityReferenceMirror.getName(),
                    t);
            }
        }
    }

    private void walkAggregateRootReference(AggregateRootReferenceMirror aggregateRootReferenceMirror) {
        visitAggregateRootReference(aggregateRootReferenceMirror);
        if (!visitTypesOnlyOnce || !visitorContext.isAlreadyVisited(
            aggregateRootReferenceMirror.getType().getTypeName())) {
            try {
                walkType(aggregateRootReferenceMirror.getAggregateRoot());
            } catch (Throwable t) {
                log.warn(
                    "Couldn't walk down " + aggregateRootReferenceMirror.getType().getTypeName() + "." + aggregateRootReferenceMirror.getName(),
                    t);
            }
        }
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public void visitEnterAnyDomainType(DomainTypeMirror domainTypeMirror) {
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public boolean visitEnterAggregateRoot(AggregateRootMirror aggregateRootMirror) {
        return true;
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public boolean visitEnterEntity(EntityMirror entityMirror) {
        return true;
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public boolean visitEnterValue(ValueMirror valueMirror) {
        return true;
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public void visitLeaveAggregateRoot(AggregateRootMirror aggregateRootMirror) {
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public void visitLeaveEntity(EntityMirror entityMirror) {
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public void visitLeaveValue(ValueMirror valueMirror) {
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public void visitAggregateRootReference(AggregateRootReferenceMirror aggregateRootReferenceMirror) {
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public void visitEntityReference(EntityReferenceMirror entityReferenceMirror) {
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public void visitBasic(FieldMirror basicFieldMirror) {
    }

    /**
     * Default callback implementation. Override to implement custom callback logic.
     */
    @Override
    public void visitEntityId(FieldMirror idFieldMirror) {
    }

    /**
     * @return the current visitor context.
     */
    public VisitorContext getVisitorContext() {
        return visitorContext;
    }

    /**
     * Sets the flag to ignore static fields during the visiting process of the object tree.
     * Default is {@code true}.
     *
     * @param ignoreStaticFields true to ignore static fields, false otherwise
     */
    public void setIgnoreStaticFields(boolean ignoreStaticFields) {
        this.ignoreStaticFields = ignoreStaticFields;
    }

    /**
     * Sets the flag to ignore hidden fields during the visiting process of the
     * object tree.
     * Default is {@code true}.
     *
     * @param ignoreHiddenFields true to ignore hidden fields, false otherwise
     */
    public void setIgnoreHiddenFields(boolean ignoreHiddenFields) {
        this.ignoreHiddenFields = ignoreHiddenFields;
    }

    /**
     * Sets the flag to visit the types only once during the visiting process of the object tree.
     * By setting this flag to true, it prevents endless loops by ensuring that every type is visited only once.
     * Default is {@code true}.
     *
     * @param visitTypesOnlyOnce true to visit the types only once, false otherwise
     */
    public void setVisitTypesOnlyOnce(boolean visitTypesOnlyOnce) {
        this.visitTypesOnlyOnce = visitTypesOnlyOnce;
    }
}
