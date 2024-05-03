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

import nitrox.dlc.access.DlcAccess;
import nitrox.dlc.builder.DomainObjectBuilder;
import nitrox.dlc.builder.DomainObjectBuilderProvider;
import nitrox.dlc.mirror.api.DomainTypeMirror;
import nitrox.dlc.persistence.exception.DLCPersistenceException;
import nitrox.dlc.persistence.mapping.util.BoxTypeNameConverter;
import nitrox.dlc.domain.types.AggregateRoot;
import nitrox.dlc.domain.types.Entity;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.domain.types.ValueObject;
import nitrox.dlc.domain.types.internal.DomainObject;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.api.DomainType;
import nitrox.dlc.mirror.api.EntityMirror;
import nitrox.dlc.mirror.api.EntityReferenceMirror;
import nitrox.dlc.mirror.api.FieldMirror;
import nitrox.dlc.mirror.api.ValueMirror;
import nitrox.dlc.mirror.api.ValueReferenceMirror;
import nitrox.dlc.mirror.visitor.ContextDomainObjectVisitor;
import nitrox.dlc.persistence.mapping.converter.ConverterRegistry;
import nitrox.dlc.persistence.mapping.converter.TypeConverter;
import nitrox.dlc.persistence.mapping.util.BiMap;
import nitrox.dlc.persistence.records.EntityValueObjectRecordClassProvider;
import nitrox.dlc.persistence.records.EntityValueObjectRecordTypeConfiguration;
import nitrox.dlc.persistence.records.NewRecordInstanceProvider;
import nitrox.dlc.persistence.records.RecordClassProvider;
import nitrox.dlc.persistence.records.RecordProperty;
import nitrox.dlc.persistence.records.RecordPropertyAccessor;
import nitrox.dlc.persistence.records.RecordPropertyProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class provides the default mapping behaviour for mapping {@link DomainObject} instances to R (records)
 * and R records to a corresponding {@link DomainObjectBuilder} for the DomainObject type.
 *
 * This implementation defines the auto-mapping behaviour for DomainObjects.
 *
 * @author Mario Herb
 */

public class AutoRecordMapper<R, DO extends DomainObject, A extends AggregateRoot<?>> extends AbstractRecordMapper<R, DO, A>{

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
        this.domainType = Domain.typeMirror(typeName).map(DomainTypeMirror::getDomainType).orElse(DomainType.NON_DOMAIN);
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
        if(entityValueObjectRecordClassProvider != null) {
            relevantValueObjectRecordConfigs.addAll(entityValueObjectRecordClassProvider.provideContainedValueObjectRecordClassConfigurations()
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
     * @return
     */

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
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
                        if(DomainType.ENTITY.equals(fm.getType().getDomainType()) || DomainType.AGGREGATE_ROOT.equals(fm.getType().getDomainType())){
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
                                var recordPropertyTypeName = BoxTypeNameConverter.convertToBoxedType(recordPropertyType.getName());
                                fieldTypeName = BoxTypeNameConverter.convertToBoxedType(fieldTypeName);
                                if (!recordPropertyTypeName.equals(fieldTypeName)) {
                                    if (DomainType.ENUM.equals(domainType) && value instanceof String) {
                                        value = DlcAccess.newEnumInstance((String) value, fieldTypeName);
                                    } else {
                                        TypeConverter tc = this.converterRegistry.getTypeConverter(recordPropertyTypeName, fieldTypeName);
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
     * @return
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public R from(DO domainObject, A root) {
        R record = this.newRecordInstanceProvider.provideNewRecord(this.recordTypeName);
        this.valuePathToRecordProperty.keySet().forEach(
            path -> {
                var recordProperty = this.valuePathToRecordProperty.get(path);
                if(path.pathElements().size()==1){
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

                        if ((DomainType.ENTITY.equals(domainType) || DomainType.AGGREGATE_ROOT.equals(domainType)) && recordProperty.getName().toLowerCase().contains(fm.getName().toLowerCase())) {
                            var em = (EntityMirror) fieldTypeMirror.orElseThrow();
                            if(em.getIdentityField().isPresent()){
                                Identity<?> identity = DlcAccess.accessorFor((Entity<?>)value).peek(em.getIdentityField().get().getName());
                                value = identity.value();
                            }
                        } else if (DomainType.ENUM.equals(domainType) && String.class.getName().equals(recordPropertyType)) {
                            value = ((Enum<?>) value).name();
                        } else {
                            TypeConverter tc = this.converterRegistry.getTypeConverter(domainObjectFieldType, recordPropertyType);
                            value = tc.convert(value);
                        }
                    }
                    this.recordPropertyAccessor.setPropertyValue(recordProperty, record, value);
                }else{
                    Object value = mapperNestedValueObjectAccessor.getMappedRecordPropertyValue(recordProperty, domainObject);
                    if (value != null) {
                        String valueType = value.getClass().getName();
                        var valueTypeMirror = Domain.typeMirror(valueType);
                        var domainType = DomainType.NON_DOMAIN;
                        if(valueTypeMirror.isPresent()){
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
                            if (DomainType.ENUM.equals(domainType) && String.class.getName().equals(recordPropertyType)) {
                                value = ((Enum<?>) value).name();
                            } else {
                                TypeConverter tc = this.converterRegistry.getTypeConverter(valueType, recordPropertyType);
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

    private BiMap<ValuePath, RecordProperty> initializeMappedValuePaths(){
        var biMap = new BiMap<ValuePath, RecordProperty>();
        var visitor = new ContextDomainObjectVisitor(typeName, false){
            @Override
            public void visitBasic(FieldMirror basicFieldMirror) {
                if(isFieldMapped()) {
                    var valuePath = new ValuePath(getVisitorContext().getCurrentPath());
                    RecordProperty rp;
                    if(valuePath.pathElements().size()>1){
                        rp = findRecordPropertyForValuePath(valuePath);
                    }else{
                        rp = findRecordPropertyForField(basicFieldMirror);
                    }
                    if(isRecordPropertyMapped(rp)) {
                        biMap.put(valuePath, rp);
                    }
                }
            }

            @Override
            public void visitEntityId(FieldMirror idFieldMirror) {
                var rp = findRecordPropertyForField(idFieldMirror);
                if(isRecordPropertyMapped(rp)) {
                    var valuePath = new ValuePath(getVisitorContext().getCurrentPath());
                    biMap.put(valuePath, rp);
                }
            }

            @Override
            public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
                if(isFieldMapped()
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
                if(isFieldMapped()) {
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
                return context.startingTypeName.equals(entityMirror.getTypeName()) && context.getCurrentPath().size() == 0;
            }

            @Override
            public boolean visitEnterValue(ValueMirror valueMirror) {
                if(valueMirror.isValueObject()){
                    if(DomainType.ENTITY.equals(domainType) || DomainType.AGGREGATE_ROOT.equals(domainType)){
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

            private boolean isFieldMapped(){
                var contextPath = getVisitorContext().getCurrentPath();
                return contextPath.stream().noneMatch(
                    fm -> fm.isStatic()
                          || fm.getType().hasCollectionContainer()
                          || (ignoredFields != null && ignoredFields.isIgnored(fm))
                );
            }

            private boolean isRecordPropertyMapped(RecordProperty rp){
                return rp != null && (ignoredRecordPropertyProvider == null || !ignoredRecordPropertyProvider.isIgnored(rp));
            }

        };
        visitor.start();
        return biMap;
    }

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
            throw DLCPersistenceException.fail("The field '%s'"
                + " of the DomainObject class '%s'"
                + " could not be matched to a single record property! '%s'"
                + " matching record properties were found! ['%s']",
                fieldMirror.getName(),
                this.typeName,
                matchedRecordProperties.size(),
                matchedNames
            );
        } else if (matchedRecordProperties.size() == 0) {
            throw DLCPersistenceException.fail("The field '%s'"
                    + " of the DomainObject class '%s'"
                    + " could not be matched to a single record property! No match found!",
                fieldMirror.getName(),
                this.typeName
            );
        }
        return matchedRecordProperties.get(0);
    }

    private RecordProperty findRecordPropertyForValuePath(ValuePath path){
        if(this.ignoredFields != null
            && path.pathElements().stream().anyMatch(this.ignoredFields::isIgnored)
        ){
            return null;
        }
        var matchedRecordProperties = recordProperties
            .stream()
            .filter(rp -> recordPropertyMatcher.matchValueObjectPath(rp, path.pathElements().stream().toList())).toList();

        if (matchedRecordProperties.size() > 1) {
            var matchedNames = new StringBuilder();
            matchedRecordProperties.forEach(rp -> matchedNames.append(" ").append(rp.getName()));
            throw DLCPersistenceException.fail("The path '%s'"
                + " of the DomainObject class '%s'"
                + " could not be matched to a single record property! '%s'"
                + " matching record properties were found! ['%s']", path.path(), this.typeName, matchedRecordProperties.size(), matchedNames);

        } else if (matchedRecordProperties.size() == 0) {
            throw DLCPersistenceException.fail("The path '%s'"
                + " of the DomainObject class '%s'"
                + " could not be matched to a single record property! No match found!", path.path(), this.typeName);
        }
        return matchedRecordProperties.get(0);
    }

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
            throw DLCPersistenceException.fail("The field '%s'"
                + " of the DomainObject class '%s'"
                + " could not be matched to a single record property! '%s'"
                + "properties were found! ['%s']", entityReferenceMirror.getName(), this.typeName, matchedRecordProperties.size(), matchedNames);

        } else if (matchedRecordProperties.size() == 0) {
            return null;
        }
        return matchedRecordProperties.get(0);
    }

    private void checkMappingComplete(){
        var nonMappedRecordProperties = this.recordProperties
            .stream()
            .filter(rp -> this.valuePathToRecordProperty.getInverse(rp) == null)
            .filter(rp -> (!domainType.equals(DomainType.ENTITY) && !domainType.equals(DomainType.AGGREGATE_ROOT)) || !rp.isNonNullForeignKey())
            .filter(rp -> !(domainType.equals(DomainType.VALUE_OBJECT) && (rp.getName().equals("id") || rp.getName().equals("containerId"))))
            .filter(rp -> this.ignoredRecordPropertyProvider == null || !this.ignoredRecordPropertyProvider.isIgnored(rp))
            .toList();
        if(nonMappedRecordProperties.size()>0){
            throw DLCPersistenceException.fail(String.format("The record properties '%s' of '%s' were not matched within the DomainObject" +
                "'%s' for auto mapping!",
                nonMappedRecordProperties.stream().map(RecordProperty::getName).collect(Collectors.joining(", ")),
                this.recordTypeName,
                this.typeName
                ));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<DO> domainObjectType() {
        return (Class<DO>) DlcAccess.getClassForName(this.typeName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<R> recordType() {
        return (Class<R>) recordClassProvider
                .provideRecordClasses()
                .stream()
                .filter(rc -> rc.getName().equals(recordTypeName))
                .findFirst()
                .orElseThrow(() -> DLCPersistenceException.fail("Record class not found '%s'", this.recordTypeName));
    }
}
