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

package nitrox.dlc.builder;

import nitrox.dlc.builder.exception.DLCBuilderException;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.domain.types.internal.DomainObject;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.DomainType;
import nitrox.dlc.mirror.api.DomainTypeMirror;
import nitrox.dlc.mirror.api.EntityMirror;
import nitrox.dlc.mirror.api.FieldMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;


/**
 * Common class for domain object builders.
 *
 * @param <T> type for which the domain object builder delivers new domain object instances
 *
 * @author Dominik Galler
 */
public abstract class AbstractDomainObjectBuilder<T extends DomainObject> implements DomainObjectBuilder<T> {
    private final static Logger log = LoggerFactory.getLogger(AbstractDomainObjectBuilder.class);
    private final Class<T> instanceType;
    private final boolean isEntityType;
    private final DomainTypeMirror domainTypeMirror;

    /**
     * This implementation relies on the domain structure information within the domain mirror.
     *
     * @param instanceType instance type of built domain objects
     */
    public AbstractDomainObjectBuilder(Class<T> instanceType) {
        log.debug("New domain object builder for '{}'", instanceType.getName());
        this.instanceType = Objects.requireNonNull(instanceType, "A type definition is needed to create a builder instance!");
        this.domainTypeMirror = Domain.typeMirror(instanceType.getName())
            .orElseThrow(() -> DLCBuilderException.fail("DomainTypeMirror for '%s' not found!", instanceType.getName()));
        this.isEntityType = DomainType.ENTITY.equals(domainTypeMirror.getDomainType()) || DomainType.AGGREGATE_ROOT.equals(domainTypeMirror.getDomainType());
    }

    /**
     * Adds Object to collection that is the value of the field.
     * If the collection is null, a new instance is initialized.
     *
     * @param object added
     * @param fieldName of field containing the collection
     */
    @Override
    public void addValueToCollection(Object object, String fieldName) {
        log.debug("Adding {} ot collection in field '{}' at domain object builder for '{}'", object, fieldName, instanceType.getName());
        final var fm = domainTypeMirror.fieldByName(fieldName);
        if (!fm.getType().hasCollectionContainer()) {
            throw DLCBuilderException.fail("Field '%s' does not contain a collection.", fieldName);
        } else {
            addWithLazyInit(object, fieldName);
        }
    }

    /**
     * In case of a to-many reference this method provides a new container (java collection instance)
     * <p>
     * for the field with the given field name. If no container could be provided an exception is thrown.
     *
     * @param fieldName the name of the field for which a new collection instance should be created
     * @return the new Collection instance for the given field
     * @see DomainObjectBuilder
     */
    @Override
    public Collection<DomainObject> newCollectionInstanceForField(String fieldName) {
        log.debug("Domain object builder for '{}' providing a new collection instance for the field '{}'", instanceType.getName(), fieldName);
        Collection<DomainObject> newCollection = null;
        FieldMirror fm = domainTypeMirror.fieldByName(fieldName);
        if (fm.getType().hasListContainer()) {
            newCollection = new ArrayList<>();
        } else if (fm.getType().hasSetContainer()) {
            newCollection = new HashSet<>();
        }
        if (newCollection == null) {
            throw DLCBuilderException.fail("Was not able to create new collection instance for DomainObjectBuilder for field %s within %s", fieldName, this.instanceType.getName());
        }
        return newCollection;
    }

    /**
     * Sets a field value for a given field name.
     *
     * @param value        concrete object to be set in the builder for the given property name
     * @param fieldName name of the field to be set
     * @see DomainObjectBuilder
     */
    @Override
    public void setFieldValue(Object value, String fieldName) {
        log.debug("Domain object builder for '{}' setting a new field value '{}' for the field '{}'", instanceType.getName(), value, fieldName);
        Object o = value;
        if (o instanceof Optional) {
            Optional<Object> optional = uncheckedCast(o);
            o = optional.orElse(null);
        }
        setValue(fieldName, o);
    }

    /**
     * Queries the current field value set in the builder for the given field name.
     *
     * @param fieldName the name of the field being queried
     * @return the object instance that is currently set in the builder instance
     * @see DomainObjectBuilder
     */
    @Override
    public Object getFieldValue(String fieldName) {
        log.debug("Domain object builder for '{}' returning current field value for the field '{}'", instanceType.getName(), fieldName);
        return getValue(fieldName);
    }

    /**
     * Convenience method to obtain the primary identity value currently set within the builder
     *
     * @param <ID> the type of the identity to be returned
     * @return current primary identity value
     * @see DomainObjectBuilder
     */
    @Override
    public <ID extends Identity<?>> ID getPrimaryIdentity() {
        var idName = getPrimaryIdentityFieldName();
        return uncheckedCast(getValue(idName));
    }

    /**
     * Convenience method to return the primary identity field name for the instanceType assigned to the builder instance.
     *
     * @return the property name of the primary identity property of the instanceType
     * @see DomainObjectBuilder
     */
    @Override
    public String getPrimaryIdentityFieldName() {
        log.debug("Domain object builder for '{}' returning current primary identity value", instanceType.getName());
        if (this.isEntityType) {
            EntityMirror em = (EntityMirror) domainTypeMirror;
            var idField = em.getIdentityField()
                .orElseThrow(() -> DLCBuilderException.fail("%s has no primary Identity!", this.instanceType.getName()));
            return idField.getName();
        } else {
            throw DLCBuilderException.fail("%s is not an Entity and has no primary Identity!", this.instanceType.getName());
        }
    }

    /**
     * The instance type for this builder. This means the enclosing class of the builder class itself.
     *
     * @return instance type (domain object type)
     * @see DomainObjectBuilder
     */
    @Override
    public Class<T> instanceType() {
        return this.instanceType;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void addWithLazyInit(Object object, String fieldName) {
        Collection collection = (Collection) getValue(fieldName);
        if (collection == null) {
            collection = newCollectionInstanceForField(fieldName);
            setValue(fieldName, collection);
        }
        collection.add(object);
    }

    @SuppressWarnings({ "unchecked" })
    protected <K> K uncheckedCast(Object obj) {
        return (K) obj;
    }

    protected abstract Object getValue(String name);

    protected abstract void setValue(String name, Object value);
}
