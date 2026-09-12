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

package io.domainlifecycles.persistence.mapping;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.converter.ConverterRegistry;
import io.domainlifecycles.persistence.mapping.converter.TypeConverter;
import io.domainlifecycles.persistence.mapping.util.BoxTypeNameConverter;
import io.domainlifecycles.persistence.records.NewRecordInstanceProvider;
import io.domainlifecycles.persistence.records.RecordClassProvider;
import io.domainlifecycles.persistence.records.RecordProperty;
import io.domainlifecycles.persistence.records.RecordPropertyAccessor;
import io.domainlifecycles.persistence.records.RecordPropertyProvider;

import java.util.Collection;
import java.util.Objects;

/**
 * A bespoke {@link RecordMapper} for a {@link ScalarListElement}, i.e. a single element of a
 * {@code List<Identity>} or {@code List<Enum>} field, mapped to and from a child record that has
 * exactly one data column (named {@code value} by convention) in addition to the usual technical
 * {@code id}/{@code containerId} columns shared with value object list child records.
 * <p>
 * This is deliberately not built on top of {@link AutoRecordMapper}: an {@code Identity} or {@code
 * Enum} implementation is not reflectively "buildable" the way an {@link
 * io.domainlifecycles.domain.types.ValueObject} is (there is no {@code DomainObjectBuilder} for a bare
 * scalar type), and {@code AutoRecordMapper} assumes a {@code DomainObjectMirror}, which {@code
 * Identity}/{@code Enum} mirrors are not. Conversion instead mirrors the existing inline
 * single-Identity/single-Enum handling already used for plain (non-list) fields in {@link
 * AutoRecordMapper#from} / {@link AutoRecordMapper#recordToDomainObjectBuilder}.
 *
 * @param <R> the record type
 * @author Mario Herb
 */
public class ScalarListElementRecordMapper<R> extends AbstractRecordMapper<R, ScalarListElement, AggregateRoot<?>> {

    private final String elementTypeName;
    private final DomainType elementDomainType;
    private final String recordTypeName;
    private final RecordProperty valueProperty;
    private final ConverterRegistry converterRegistry;
    private final NewRecordInstanceProvider newRecordInstanceProvider;
    private final RecordPropertyAccessor<R> recordPropertyAccessor;
    private final RecordClassProvider<?> recordClassProvider;

    /**
     * Constructs an instance of ScalarListElementRecordMapper.
     *
     * @param elementTypeName           full qualified type name of the wrapped {@code Identity} or {@code Enum}
     *                                  implementation
     * @param recordTypeName            the name of the record type for this scalar list's child table
     * @param converterRegistry         a registry for converters used to transform values between record and
     *                                  domain object
     * @param newRecordInstanceProvider a provider for new record instances
     * @param recordPropertyAccessor    used to access the properties of a record
     * @param recordPropertyProvider    a provider for the properties of a record
     * @param recordClassProvider       a provider for record class information
     */
    public ScalarListElementRecordMapper(
        String elementTypeName,
        String recordTypeName,
        ConverterRegistry converterRegistry,
        NewRecordInstanceProvider newRecordInstanceProvider,
        RecordPropertyAccessor<R> recordPropertyAccessor,
        RecordPropertyProvider recordPropertyProvider,
        RecordClassProvider<?> recordClassProvider
    ) {
        this.elementTypeName = Objects.requireNonNull(elementTypeName);
        this.recordTypeName = Objects.requireNonNull(recordTypeName);
        this.converterRegistry = Objects.requireNonNull(converterRegistry);
        this.newRecordInstanceProvider = Objects.requireNonNull(newRecordInstanceProvider);
        this.recordPropertyAccessor = Objects.requireNonNull(recordPropertyAccessor);
        this.recordClassProvider = Objects.requireNonNull(recordClassProvider);
        this.elementDomainType = Domain.typeMirror(elementTypeName)
            .map(DomainTypeMirror::getDomainType)
            .orElseThrow(() -> DLCPersistenceException.fail("DomainTypeMirror not found for '%s'!", elementTypeName));
        if (!DomainType.IDENTITY.equals(elementDomainType) && !DomainType.ENUM.equals(elementDomainType)) {
            throw DLCPersistenceException.fail(
                "'%s' is neither an Identity nor an Enum type, ScalarListElementRecordMapper is not applicable!",
                elementTypeName);
        }
        Objects.requireNonNull(recordPropertyProvider);
        var properties = recordPropertyProvider.provideProperties(recordTypeName);
        var valueProperties = properties.stream()
            .filter(rp -> !"id".equals(rp.getName()) && !"containerId".equals(rp.getName()))
            .toList();
        if (valueProperties.size() != 1) {
            throw DLCPersistenceException.fail(
                "Expected exactly one data column (besides 'id'/'containerId') on record '%s' to map the scalar " +
                    "list element '%s', but found %s!",
                recordTypeName, elementTypeName, valueProperties.size());
        }
        this.valueProperty = valueProperties.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public DomainObjectBuilder<ScalarListElement> recordToDomainObjectBuilder(R record) {
        if (record == null) {
            return null;
        }
        Object value = this.recordPropertyAccessor.getPropertyValue(this.valueProperty, record);
        if (value != null) {
            if (DomainType.IDENTITY.equals(elementDomainType)) {
                value = DlcAccess.newIdentityInstance(value, elementTypeName);
            } else if (value instanceof String stringValue) {
                value = DlcAccess.newEnumInstance(stringValue, elementTypeName);
            }
        }
        return new ScalarListElementBuilder(value, elementTypeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public R from(ScalarListElement domainObject, AggregateRoot<?> root) {
        R record = this.newRecordInstanceProvider.provideNewRecord(this.recordTypeName);
        Object value = domainObject.value();
        if (value != null) {
            if (DomainType.IDENTITY.equals(elementDomainType)) {
                value = ((Identity<?>) value).value();
            }
            var valueTypeName = BoxTypeNameConverter.convertToBoxedType(value.getClass().getName());
            var recordPropertyTypeName = BoxTypeNameConverter.convertToBoxedType(
                this.valueProperty.getPropertyType().getName());
            if (!recordPropertyTypeName.equals(valueTypeName)) {
                if (DomainType.ENUM.equals(elementDomainType) && String.class.getName().equals(
                    recordPropertyTypeName)) {
                    value = ((Enum<?>) value).name();
                } else {
                    TypeConverter tc = this.converterRegistry.getTypeConverter(valueTypeName, recordPropertyTypeName);
                    value = tc.convert(value);
                }
            }
        }
        this.recordPropertyAccessor.setPropertyValue(this.valueProperty, record, value);
        return record;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Class<ScalarListElement> domainObjectType() {
        return ScalarListElement.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Class<R> recordType() {
        return (Class<R>) this.recordClassProvider
            .provideRecordClasses()
            .stream()
            .filter(rc -> rc.getName().equals(this.recordTypeName))
            .findFirst()
            .orElseThrow(() -> DLCPersistenceException.fail("Record class not found '%s'", this.recordTypeName));
    }

    @SuppressWarnings("rawtypes")
    private static class ScalarListElementBuilder implements DomainObjectBuilder<ScalarListElement> {

        private final Object value;
        private final String elementTypeName;

        private ScalarListElementBuilder(Object value, String elementTypeName) {
            this.value = value;
            this.elementTypeName = elementTypeName;
        }

        @Override
        public ScalarListElement build() {
            return new ScalarListElement(value, elementTypeName);
        }

        @Override
        public void addValueToCollection(Object object, String fieldName) {
            throw DLCPersistenceException.fail("A scalar list element does not have any collection fields!");
        }

        @Override
        public Collection<DomainObject> newCollectionInstanceForField(String fieldName) {
            throw DLCPersistenceException.fail("A scalar list element does not have any collection fields!");
        }

        @Override
        public void setFieldValue(Object value, String fieldName) {
            throw DLCPersistenceException.fail("A scalar list element does not have any settable fields!");
        }

        @Override
        public boolean canInstantiateField(String fieldName) {
            return false;
        }

        @Override
        public Object getFieldValue(String fieldName) {
            return null;
        }

        @Override
        public <ID extends Identity<?>> ID getPrimaryIdentity() {
            return null;
        }

        @Override
        public String getPrimaryIdentityFieldName() {
            return null;
        }

        @Override
        public Class<ScalarListElement> instanceType() {
            return ScalarListElement.class;
        }

        @Override
        public Object getBuilderInstance() {
            return this;
        }
    }
}
