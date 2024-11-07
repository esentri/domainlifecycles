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
import io.domainlifecycles.persistence.mapping.util.BoxTypeNameConverter;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.persistence.mapping.converter.ConverterRegistry;
import io.domainlifecycles.persistence.mapping.converter.TypeConverter;
import io.domainlifecycles.persistence.mapping.util.BiMap;
import io.domainlifecycles.persistence.records.RecordProperty;
import io.domainlifecycles.persistence.records.RecordPropertyAccessor;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class enables the mapping of nested ValueObjects from and into fields of DomainObject instances.
 * <p>
 * It enables to fetch complete valid ValueObject instances that were saved in a Record before.
 * It also enables to identify and return record values from a DomainObject instance.
 *
 * @param <R>  type of record
 * @param <DO> type of domain object
 *
 * @author Mario Herb
 */
public class AutoMapperNestedValueObjectAccessor<R, DO extends DomainObject> implements MapperNestedValueObjectAccessor<R, DO> {

    private final DomainObjectBuilderProvider domainObjectBuilderProvider;
    private final BiMap<ValuePath, RecordProperty> valuePathToRecordProperty;
    private final RecordPropertyAccessor<R> recordPropertyAccessor;
    private final ConverterRegistry converterRegistry;

    public AutoMapperNestedValueObjectAccessor(DomainObjectBuilderProvider domainObjectBuilderProvider,
                                               BiMap<ValuePath, RecordProperty> valuePathToRecordProperty,
                                               RecordPropertyAccessor<R> recordPropertyAccessor,
                                               ConverterRegistry converterRegistry
    ) {
        this.domainObjectBuilderProvider = Objects.requireNonNull(domainObjectBuilderProvider);
        this.valuePathToRecordProperty = Objects.requireNonNull(valuePathToRecordProperty);
        this.recordPropertyAccessor = Objects.requireNonNull(recordPropertyAccessor);
        this.converterRegistry = Objects.requireNonNull(converterRegistry);
    }

    /**
     * Creates a ValueObject from the values that are provided by the record.
     * Creates the ValueObject that ist adressed by the given {@code valueObjectFieldName}.
     *
     * @return the mapped ValueObject instance
     */
    @Override
    public ValueObject getMappedValueObject(R record, String valueObjectFieldName) {
        LeveledPathContainer container = new LeveledPathContainer(valuePathToRecordProperty.keySet(), record,
            valueObjectFieldName);
        return container.getValue();
    }

    private BuilderWrapper newBuilderWrapperForPath(ValuePath path) {
        String targetType;
        if (path.pathElements().size() > 1) {
            targetType = path.containerValuePath().getFinalFieldMirror().getType().getTypeName();
        } else {
            targetType = path.getFinalFieldMirror().getType().getTypeName();
        }
        return new BuilderWrapper(this.domainObjectBuilderProvider.provide(targetType));
    }

    private Object getPathValue(ValuePath path, R record){
        RecordProperty p = valuePathToRecordProperty.get(path);
        if (p != null) {
            var recordPropertyValue = recordPropertyAccessor.getPropertyValue(p, record);
            String valueType = path.getFinalFieldMirror().getType().getTypeName();
            DomainType valueDomainType = path.getFinalFieldMirror().getType().getDomainType();
            if (recordPropertyValue != null) {
                String recordPropertyType = recordPropertyValue.getClass().getName();
                if (!DomainType.IDENTITY.equals(valueDomainType)) {
                    recordPropertyType = BoxTypeNameConverter.convertToBoxedType(recordPropertyType);
                    valueType = BoxTypeNameConverter.convertToBoxedType(valueType);
                    if (!recordPropertyType.equals(valueType)
                        && !(DomainType.ENTITY.equals(valueDomainType) || DomainType.AGGREGATE_ROOT.equals(
                        valueDomainType))) {
                        if (DomainType.ENUM.equals(valueDomainType) && recordPropertyValue instanceof String) {
                            recordPropertyValue = DlcAccess.newEnumInstance((String) recordPropertyValue, valueType);
                        } else {
                            @SuppressWarnings("rawtypes")
                            TypeConverter tc = this.converterRegistry.getTypeConverter(recordPropertyType, valueType);
                            recordPropertyValue = tc.convert(recordPropertyValue);
                        }
                    }
                } else {
                    recordPropertyValue = DlcAccess.newIdentityInstance(recordPropertyValue, valueType);
                }

            }
            return recordPropertyValue;
        }
        return null;
    }


    /**
     * Identifies a value from a given DomainObject by the given RecordProperty.
     * The identified value is finally returned.
     *
     * @return the value addressed by the RecordProperty within the given DomainObject instance
     */
    @Override
    public Object getMappedRecordPropertyValue(RecordProperty rp, DO domainObject) {
        var path = this.valuePathToRecordProperty.getInverse(rp);

        Object currentObject = domainObject;
        for (FieldMirror fm : path.pathElements()) {
            if (currentObject == null) {
                return null;
            }
            if (isDomainObject(currentObject)) {
                currentObject = DlcAccess.accessorFor((DomainObject) currentObject).peek(fm.getName());
            } else {
                return currentObject;
            }
            if (fm.getType().hasOptionalContainer()) {
                var opt = (Optional<?>) currentObject;
                currentObject = opt.orElse(null);
            }
        }
        return currentObject;

    }

    private boolean isDomainObject(Object currentObject) {
        var domainTypeMirror = Domain.typeMirror(currentObject.getClass().getName());
        if (domainTypeMirror.isPresent()) {
            var dtm = domainTypeMirror.get();
            return dtm.getDomainType().equals(DomainType.ENTITY)
                || dtm.getDomainType().equals(DomainType.AGGREGATE_ROOT)
                || dtm.getDomainType().equals(DomainType.VALUE_OBJECT);
        }
        return false;
    }

    private class LeveledPathContainer {
        private final Map<Integer, LeveledPaths> leveledPathsMap;
        private final R record;

        private LeveledPathContainer(Collection<ValuePath> allPaths, R r, String valueObjectPropertyName) {
            record = r;
            leveledPathsMap = new HashMap<>();
            allPaths.stream()
                .filter(path -> path.pathElements().getFirst().getName().equals(valueObjectPropertyName))
                .forEach(p -> {
                    var leveledPaths = leveledPathsMap.get(p.pathElements().size() - 1);
                    if (leveledPaths == null) {
                        var level = p.pathElements().size() - 1;
                        leveledPaths = new LeveledPaths(record);
                        leveledPathsMap.put(level, leveledPaths);
                    }
                    leveledPaths.addPath(p);
                });
            addMissingLevelsAndPaths();
        }

        private int topLevel() {
            return leveledPathsMap.keySet()
                .stream()
                .max(Comparator.comparingInt(k -> k))
                .orElseThrow();
        }

        private void addMissingLevelsAndPaths() {
            for (int l = topLevel() - 1; l >= 0; l--) {
                var leveledPaths = leveledPathsMap.get(l);
                if (leveledPaths == null) {
                    leveledPaths = new LeveledPaths(record);
                    leveledPathsMap.put(l, leveledPaths);
                }
                var upperLevel = leveledPathsMap.get(l + 1);
                var containerPaths = upperLevel.paths.stream().map(ValuePath::containerValuePath).collect(
                    Collectors.toSet());
                for (var c : containerPaths) {
                    leveledPaths.addPath(c);
                }
            }
        }

        private ValueObject getValue() {
            var tl = topLevel();
            Map<String, ValueObject> built = new HashMap<>();
            ValueObject returnValueObject = null;
            for (int l = tl; l >= 1; l--) {
                var level = leveledPathsMap.get(l);
                level.initializeBuilders(built);
                if (l == 1) {
                    returnValueObject = level.buildValueObjects()
                        .values()
                        .stream()
                        .findFirst()
                        .orElse(null);
                } else {
                    built.putAll(level.buildValueObjects());
                }
            }
            return returnValueObject;
        }

    }

    private class LeveledPaths {
        private final Set<ValuePath> paths;
        private final Map<String, BuilderWrapper> buildersByPath;
        private final R record;

        private LeveledPaths(R record) {
            this.paths = new HashSet<>();
            this.buildersByPath = new HashMap<>();
            this.record = record;
        }

        private void addPath(ValuePath path) {
            this.paths.add(path);
        }

        private void initializeBuilders(Map<String, ValueObject> builtMap) {
            paths.forEach(p -> {
                var builderWrapper = buildersByPath.get(p.containerPath());
                if (builderWrapper == null) {
                    builderWrapper = newBuilderWrapperForPath(p);
                    buildersByPath.put(p.containerPath(), builderWrapper);
                }
                if (DomainType.VALUE_OBJECT.equals(p.getFinalFieldMirror().getType().getDomainType())) {
                    if (p.pathElements().size() > 1) {
                        var val = builtMap.get(p.path() + ".");
                        builderWrapper.setPropertyValue(val, p.getFinalFieldMirror().getName());

                    }
                } else {
                    builderWrapper.setPropertyValue(getPathValue(p, record), p.getFinalFieldMirror().getName());
                }

            });
        }

        private Map<String, ? extends ValueObject> buildValueObjects() {
            return buildersByPath.entrySet()
                .stream()
                .filter(e -> e.getValue().nonNullValueSet)
                .map(e -> Map.entry(e.getKey(), e.getValue().build()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

    }

    private static class BuilderWrapper {
        private final DomainObjectBuilder<? extends ValueObject> builder;
        private boolean nonNullValueSet;

        private BuilderWrapper(DomainObjectBuilder<? extends ValueObject> builder) {
            this.builder = builder;
            this.nonNullValueSet = false;
        }

        private void setPropertyValue(Object value, String propertyName) {
            if (value != null) {
                this.nonNullValueSet = true;
            }
            builder.setFieldValue(value, propertyName);
        }

        private ValueObject build() {
            return builder.build();
        }
    }

}
