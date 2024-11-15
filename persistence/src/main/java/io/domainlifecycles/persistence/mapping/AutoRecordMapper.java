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

package io.domainlifecycles.persistence.mapping;

import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.ValueMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.visitor.ContextDomainObjectVisitor;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.converter.ConverterRegistry;
import io.domainlifecycles.persistence.mapping.converter.TypeConverter;
import io.domainlifecycles.persistence.mapping.util.BiMap;
import io.domainlifecycles.persistence.mapping.util.BoxTypeNameConverter;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordClassProvider;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordTypeConfiguration;
import io.domainlifecycles.persistence.records.NewRecordInstanceProvider;
import io.domainlifecycles.persistence.records.RecordClassProvider;
import io.domainlifecycles.persistence.records.RecordProperty;
import io.domainlifecycles.persistence.records.RecordPropertyAccessor;
import io.domainlifecycles.persistence.records.RecordPropertyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class provides the default mapping behaviour for mapping {@link DomainObject} instances to R (records)
 * and R records to a corresponding {@link DomainObjectBuilder} for the DomainObject type.
 * <p>
 * This implementation defines the auto-mapping behaviour for DomainObjects.
 * </P>
 *
 * @param <R>  type of Record
 * @param <DO> type of DomainObject
 * @param <A>  type of AggregateRoot
 * @author Mario Herb
 */

public class AutoRecordMapper<R, DO extends DomainObject, A extends AggregateRoot<?>> extends AbstractRecordMapper<R,
    DO, A> {

    private static final Logger log = LoggerFactory.getLogger(AutoRecordMapper.class);

    private final String typeName;

    private final String recordTypeName;

    private final DomainType domainType;

    private final List<RecordProperty> recordProperties;

    private final RecordPropertyMatcher recordPropertyMatcher;

    private final DomainObjectBuilderProvider domainObjectBuilderProvider;

    private final BiMap<ValuePath, RecordProperty> valuePathToRecordProperty;

    private final IgnoredFieldProvider ignoredFields;

    private final IgnoredRecordPropertyProvider ignoredRecordPropertyProvider;

    private final ConverterRegistry converterRegistry;

    private final NewRecordInstanceProvider newRecordInstanceProvider;

    private final RecordPropertyAccessor<R> recordPropertyAccessor;

    private final MapperNestedValueObjectAccessor<R, DO> mapperNestedValueObjectAccessor;

    private final List<EntityValueObjectRecordTypeConfiguration<?>> relevantValueObjectRecordConfigs;

    private final RecordClassProvider<?> recordClassProvider;

    public AutoRecordMapper(
        String typeName,
        String recordTypeName,
        RecordPropertyMatcher recordPropertyMatcher,
        DomainObjectBuilderProvider domainObjectBuilderProvider,
        IgnoredFieldProvider ignoredFields,
        IgnoredRecordPropertyProvider ignoredRecordPropertyProvider,
        ConverterRegistry converterRegistry,
        NewRecordInstanceProvider newRecordInstanceProvider,
        RecordPropertyAccessor<R> recordPropertyAccessor,
        RecordPropertyProvider recordPropertyProvider,
        EntityValueObjectRecordClassProvider entityValueObjectRecordClassProvider,
        RecordClassProvider<?> recordClassProvider
    ) {
        this.typeName = Objects.requireNonNull(typeName);
        this.domainType = Domain.typeMirror(typeName).map(DomainTypeMirror::getDomainType).orElse(
            DomainType.NON_DOMAIN);
        this.recordTypeName = Objects.requireNonNull(recordTypeName);
        this.recordPropertyMatcher = Objects.requireNonNull(recordPropertyMatcher);
        this.domainObjectBuilderProvider = Objects.requireNonNull(domainObjectBuilderProvider);
        this.ignoredFields = ignoredFields;
        this.ignoredRecordPropertyProvider = ignoredRecordPropertyProvider;
        this.converterRegistry = Objects.requireNonNull(converterRegistry);
        this.newRecordInstanceProvider = Objects.requireNonNull(newRecordInstanceProvider);
        this.recordPropertyAccessor = Objects.requireNonNull(recordPropertyAccessor);
        Objects.requireNonNull(recordPropertyProvider);
        this.recordClassProvider = Objects.requireNonNull(recordClassProvider);
        relevantValueObjectRecordConfigs = new ArrayList<>();
        if (entityValueObjectRecordClassProvider != null) {
            relevantValueObjectRecordConfigs.addAll(
                entityValueObjectRecordClassProvider.provideContainedValueObjectRecordClassConfigurations()
                    .stream()
                    .filter(c -> c.containingEntityType().getName().equals(typeName))
                    .toList());
        }
        this.recordProperties = recordPropertyProvider.provideProperties(recordTypeName);
        this.valuePathToRecordProperty = initializeMappedValuePaths();
        checkMappingComplete();
        this.mapperNestedValueObjectAccessor = new AutoMapperNestedValueObjectAccessor<>(
            domainObjectBuilderProvider,
            valuePathToRecordProperty,
            recordPropertyAccessor,
            converterRegistry
        );
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */

    @Override
    @SuppressWarnings({"rawtypes"})
    public DomainObjectBuilder<DO> recordToDomainObjectBuilder(R record) {
        if (record == null) {
            return null;
        }
        final DomainObjectBuilder<DO> b = this.domainObjectBuilderProvider.provide(this.typeName);
        this.valuePathToRecordProperty.valueSet()
            .forEach(
                recordProperty -> {
                    var path = valuePathToRecordProperty.getInverse(recordProperty);
                    if (path.pathElements().size() == 1) {
                        var fm = path.pathElements().getFirst();
                        if (DomainType.ENTITY.equals(fm.getType().getDomainType()) || DomainType.AGGREGATE_ROOT.equals(
                            fm.getType().getDomainType())) {
                            return;
                        }
                        Object value = this.recordPropertyAccessor.getPropertyValue(recordProperty, record);
                        if (value != null) {
                            var fieldTypeName = fm.getType().getTypeName();
                            var domainType = fm.getType().getDomainType();

                            if (DomainType.IDENTITY.equals(domainType)) {
                                value = DlcAccess.newIdentityInstance(value, fieldTypeName);
                            }
                            var recordPropertyType = recordProperty.getPropertyType();

                            if (!DomainType.IDENTITY.equals(domainType)) {
                                var recordPropertyTypeName = BoxTypeNameConverter.convertToBoxedType(
                                    recordPropertyType.getName());
                                fieldTypeName = BoxTypeNameConverter.convertToBoxedType(fieldTypeName);
                                if (!recordPropertyTypeName.equals(fieldTypeName)) {
                                    if (DomainType.ENUM.equals(domainType) && value instanceof String) {
                                        value = DlcAccess.newEnumInstance((String) value, fieldTypeName);
                                    } else {
                                        TypeConverter tc = this.converterRegistry.getTypeConverter(
                                            recordPropertyTypeName, fieldTypeName);
                                        value = tc.convert(value);
                                    }
                                }
                            }
                            b.setFieldValue(value, fm.getName());
                        }
                    } else {
                        //ValueObjectPath
                        String fieldName = path.pathElements().getFirst().getName();
                        ValueObject vo = mapperNestedValueObjectAccessor.getMappedValueObject(record, fieldName);
                        b.setFieldValue(vo, fieldName);
                    }
                }
            );

        return b;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public R from(DO domainObject, A root) {
        R record = this.newRecordInstanceProvider.provideNewRecord(this.recordTypeName);
        this.valuePathToRecordProperty.keySet().forEach(
            path -> {
                var recordProperty = this.valuePathToRecordProperty.get(path);
                if (path.pathElements().size() == 1) {
                    var fm = path.getFinalFieldMirror();
                    var domainObjectFieldType = fm.getType().getTypeName();
                    Object value = DlcAccess.accessorFor(domainObject).peek(fm.getName());
                    var domainType = fm.getType().getDomainType();

                    if (value != null && fm.getType().hasOptionalContainer()) {
                        var opt = (Optional<?>) value;
                        value = opt.orElse(null);
                    }
                    if (value != null && DomainType.IDENTITY.equals(domainType)) {
                        value = ((Identity<?>) value).value();
                        domainObjectFieldType = value.getClass().getName();
                    }
                    if (value == null) {
                        this.recordPropertyAccessor.setPropertyValue(recordProperty, record, value);
                        return;
                    }
                    domainObjectFieldType = BoxTypeNameConverter.convertToBoxedType(domainObjectFieldType);
                    var recordPropertyType = recordProperty.getPropertyType().getName();
                    recordPropertyType = BoxTypeNameConverter.convertToBoxedType(recordPropertyType);
                    if (!recordPropertyType.equals(domainObjectFieldType)) {

                        var fieldTypeMirror = Domain.typeMirror(domainObjectFieldType);

                        if ((DomainType.ENTITY.equals(domainType) || DomainType.AGGREGATE_ROOT.equals(
                            domainType)) && recordProperty.getName().toLowerCase().contains(
                            fm.getName().toLowerCase())) {
                            var em = (EntityMirror) fieldTypeMirror.orElseThrow();
                            if (em.getIdentityField().isPresent()) {
                                Identity<?> identity = DlcAccess.accessorFor((Entity<?>) value).peek(
                                    em.getIdentityField().get().getName());
                                value = identity.value();
                            }
                        } else if (DomainType.ENUM.equals(domainType) && String.class.getName().equals(
                            recordPropertyType)) {
                            value = ((Enum<?>) value).name();
                        } else {
                            TypeConverter tc = this.converterRegistry.getTypeConverter(domainObjectFieldType,
                                recordPropertyType);
                            value = tc.convert(value);
                        }
                    }
                    this.recordPropertyAccessor.setPropertyValue(recordProperty, record, value);
                } else {
                    Object value = mapperNestedValueObjectAccessor.getMappedRecordPropertyValue(recordProperty,
                        domainObject);
                    if (value != null) {
                        String valueType = value.getClass().getName();
                        var valueTypeMirror = Domain.typeMirror(valueType);
                        var domainType = DomainType.NON_DOMAIN;
                        if (valueTypeMirror.isPresent()) {
                            domainType = valueTypeMirror.get().getDomainType();
                        }
                        if (DomainType.IDENTITY.equals(domainType)) {
                            value = ((Identity<?>) value).value();
                            valueType = value.getClass().getName();
                        }
                        valueType = BoxTypeNameConverter.convertToBoxedType(valueType);
                        String recordPropertyType = recordProperty.getPropertyType().getName();
                        recordPropertyType = BoxTypeNameConverter.convertToBoxedType(recordPropertyType);
                        if (!recordPropertyType.equals(valueType)) {
                            if (DomainType.ENUM.equals(domainType) && String.class.getName().equals(
                                recordPropertyType)) {
                                value = ((Enum<?>) value).name();
                            } else {
                                TypeConverter tc = this.converterRegistry.getTypeConverter(valueType,
                                    recordPropertyType);
                                value = tc.convert(value);
                            }
                        }
                    }
                    this.recordPropertyAccessor.setPropertyValue(recordProperty, record, value);
                }
            }
        );
        return record;
    }

    private BiMap<ValuePath, RecordProperty> initializeMappedValuePaths() {
        var biMap = new BiMap<ValuePath, RecordProperty>();
        var visitor = new ContextDomainObjectVisitor(typeName, false) {
            @Override
            public void visitBasic(FieldMirror basicFieldMirror) {
                if (isFieldMapped()) {
                    var valuePath = new ValuePath(getVisitorContext().getCurrentPath());
                    RecordProperty rp;
                    if (valuePath.pathElements().size() > 1) {
                        rp = findRecordPropertyForValuePath(valuePath);
                    } else {
                        rp = findRecordPropertyForField(basicFieldMirror);
                    }
                    if (isRecordPropertyMapped(rp)) {
                        biMap.put(valuePath, rp);
                    }
                }
            }

            @Override
            public void visitEntityId(FieldMirror idFieldMirror) {
                var rp = findRecordPropertyForField(idFieldMirror);
                if (isRecordPropertyMapped(rp)) {
                    var valuePath = new ValuePath(getVisitorContext().getCurrentPath());
                    biMap.put(valuePath, rp);
                }
            }

            @Override
            public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
                if (isFieldMapped()
                    && (valueReferenceMirror.getValue().isIdentity() || valueReferenceMirror.getValue().isEnum())
                ) {
                    var valuePath = new ValuePath(getVisitorContext().getCurrentPath());
                    var rp = findRecordPropertyForValuePath(valuePath);
                    if (isRecordPropertyMapped(rp)) {
                        biMap.put(valuePath, rp);
                    }
                }
            }

            @Override
            public void visitEntityReference(EntityReferenceMirror entityReferenceMirror) {
                if (isFieldMapped()) {
                    var rp = findRecordPropertyForForwardReference(entityReferenceMirror);
                    if (isRecordPropertyMapped(rp)) {
                        var valuePath = new ValuePath(getVisitorContext().getCurrentPath());
                        biMap.put(valuePath, rp);
                    }
                }
            }

            @Override
            public boolean visitEnterEntity(EntityMirror entityMirror) {
                var context = getVisitorContext();
                return context.startingTypeName.equals(
                    entityMirror.getTypeName()) && context.getCurrentPath().size() == 0;
            }

            @Override
            public boolean visitEnterValue(ValueMirror valueMirror) {
                if (valueMirror.isValueObject()) {
                    if (DomainType.ENTITY.equals(domainType) || DomainType.AGGREGATE_ROOT.equals(domainType)) {
                        List<String> contextPath = getVisitorContext()
                            .getCurrentPath()
                            .stream()
                            .map(FieldMirror::getName)
                            .toList();
                        return relevantValueObjectRecordConfigs.stream()
                            .noneMatch(conf ->
                                Arrays.stream(conf.pathFromEntityToValueObject()).toList().equals(contextPath)
                            );
                    }
                    return true;
                }
                return false;
            }

            private boolean isFieldMapped() {
                var contextPath = getVisitorContext().getCurrentPath();
                return contextPath.stream().noneMatch(
                    fm -> fm.isStatic()
                        || fm.getType().hasCollectionContainer()
                        || (ignoredFields != null && ignoredFields.isIgnored(fm))
                );
            }

            private boolean isRecordPropertyMapped(RecordProperty rp) {
                return rp != null && (ignoredRecordPropertyProvider == null || !ignoredRecordPropertyProvider.isIgnored(
                    rp));
            }

        };
        visitor.start();
        return biMap;
    }


    /**
     * This method tries to match exactly one of the given recordProperties (e.g. a field of a database table)
     * with the given {@link FieldMirror}, which represents a field in the given object structure.
     *
     * @param fieldMirror the FieldMirror to be matched
     * @return the matching RecordProperty, otherwise null
     */
    private RecordProperty findRecordPropertyForField(FieldMirror fieldMirror){
        if(this.ignoredFields != null && this.ignoredFields.isIgnored(fieldMirror)){
            return null;
        }
        var matchedRecordProperties = recordProperties
            .stream()
            .filter(rp -> recordPropertyMatcher.matchProperty(rp, fieldMirror)).toList();

        if (matchedRecordProperties.size() > 1) {
            var matchedNames = new StringBuilder();
            matchedRecordProperties.forEach(rp -> matchedNames.append(" ").append(rp.getName()));
            var msg = String.format("The field '%s'"
                    + " of the DomainObject class '%s'"
                    + " could not be matched to a single record property! '%s'"
                    + " matching record properties were found! ['%s']",
                fieldMirror.getName(),
                this.typeName,
                matchedRecordProperties.size(),
                matchedNames);
            log.error(msg);
            return null;
        } else if (matchedRecordProperties.isEmpty()) {
            var msg = String.format("The field '%s'"
                    + " of the DomainObject class '%s'"
                    + " could not be matched to a single record property! No match found!",
                fieldMirror.getName(),
                this.typeName);
            log.error(msg);
            return null;
        }
        return matchedRecordProperties.get(0);
    }

    /**
     * This method tries to match exactly one of the given recordProperties (e.g. a field of a database table)
     * with the given {@link ValuePath}, which represents a field in the given object structure.
     *
     * @param path the ValuePath to be matched
     * @return the matching RecordProperty, otherwise null
     */
    private RecordProperty findRecordPropertyForValuePath(ValuePath path){
        if(this.ignoredFields != null
            && path.pathElements().stream().anyMatch(this.ignoredFields::isIgnored)
        ) {
            return null;
        }
        var matchedRecordProperties = recordProperties
            .stream()
            .filter(
                rp -> recordPropertyMatcher.matchValueObjectPath(rp, path.pathElements().stream().toList())).toList();

        if (matchedRecordProperties.size() > 1) {
            var matchedNames = new StringBuilder();
            matchedRecordProperties.forEach(rp -> matchedNames.append(" ").append(rp.getName()));
            var msg = String.format("The path '%s'"
                + " of the DomainObject class '%s'"
                + " could not be matched to a single record property! '%s'"
                + " matching record properties were found! ['%s']", path.path(), this.typeName, matchedRecordProperties.size(), matchedNames);
            log.error(msg);
            return null;
        } else if (matchedRecordProperties.isEmpty()) {
            var msg = String.format("The path '%s'"
                + " of the DomainObject class '%s'"
                + " could not be matched to a single record property! No match found!", path.path(), this.typeName);
            log.error(msg);
            return null;
        }
        return matchedRecordProperties.get(0);
    }

    /**
     * This method tries to match the given {@link EntityReferenceMirror} with exactly one {@link RecordProperty}.
     *
     * @param entityReferenceMirror the given entity reference
     * @return the matching {@link RecordProperty}, otherwise null
     */
    private RecordProperty findRecordPropertyForForwardReference(EntityReferenceMirror entityReferenceMirror){
        if(this.ignoredFields != null && this.ignoredFields.isIgnored(entityReferenceMirror)){
            return null;
        }
        var matchedRecordProperties = recordProperties
            .stream()
            .filter(rp -> recordPropertyMatcher.matchForwardReference(rp, entityReferenceMirror)).toList();

        if (matchedRecordProperties.size() > 1) {
            var matchedNames = new StringBuilder();
            matchedRecordProperties.forEach(rp -> matchedNames.append(" ").append(rp.getName()));
            var msg = String.format("The field '%s'"
                + " of the DomainObject class '%s'"
                + " could not be matched to a single record property! '%s'"
                + "properties were found! ['%s']",
                entityReferenceMirror.getName(),
                this.typeName,
                matchedRecordProperties.size(),
                matchedNames
            );
            log.error(msg);
            return null;
        } else if (matchedRecordProperties.isEmpty()) {
            var msg = String.format("The field '%s'"
                    + " of the DomainObject class '%s'"
                    + " could not be matched to a single record property! No match found!",
                entityReferenceMirror.getName(),
                this.typeName);
            log.error(msg);
            return null;
        }
        return matchedRecordProperties.get(0);
    }

    private void checkMappingComplete() {
        var nonMappedRecordProperties = this.recordProperties
            .stream()
            .filter(rp -> this.valuePathToRecordProperty.getInverse(rp) == null)
            .filter(rp -> (!domainType.equals(DomainType.ENTITY) && !domainType.equals(
                DomainType.AGGREGATE_ROOT)) || !rp.isNonNullForeignKey())
            .filter(
                rp -> !(domainType.equals(DomainType.VALUE_OBJECT) && (rp.getName().equals("id") || rp.getName().equals(
                    "containerId"))))
            .filter(
                rp -> this.ignoredRecordPropertyProvider == null || !this.ignoredRecordPropertyProvider.isIgnored(rp))
            .toList();
        if(!nonMappedRecordProperties.isEmpty()){
            throw DLCPersistenceException.fail(String.format("The record properties '%s' of '%s' were not matched within the DomainObject" +
                "'%s' for auto mapping!",
                nonMappedRecordProperties.stream().map(RecordProperty::getName).collect(Collectors.joining(", ")),
                this.recordTypeName,
                this.typeName
                ));
        }
    }

    @Override
    public Class<DO> domainObjectType() {
        return (Class<DO>) DlcAccess.getClassForName(this.typeName);
    }

    @Override
    public Class<R> recordType() {
        return (Class<R>) recordClassProvider
            .provideRecordClasses()
            .stream()
            .filter(rc -> rc.getName().equals(recordTypeName))
            .findFirst()
            .orElseThrow(() -> DLCPersistenceException.fail("Record class not found '%s'", this.recordTypeName));
    }
}
