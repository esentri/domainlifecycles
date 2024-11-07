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

package io.domainlifecycles.jooq.imp.provider;

import io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.api.ValueMirror;
import io.domainlifecycles.mirror.api.ValueReferenceMirror;
import io.domainlifecycles.mirror.visitor.ContextDomainObjectVisitor;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.AutoRecordMapper;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mirror.PersistenceModel;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.mirror.api.PersistenceMirror;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordTypeConfiguration;
import org.jooq.UpdatableRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * jOOQ specific implementation of a {@link DomainPersistenceProvider}.
 *
 * @author Mario Herb
 */
public class JooqDomainPersistenceProvider extends DomainPersistenceProvider<UpdatableRecord<?>> {

    public JooqDomainPersistenceProvider(JooqDomainPersistenceConfiguration jooqPersistenceConfiguration) {
        super(jooqPersistenceConfiguration);
        if (jooqPersistenceConfiguration.typeConverterProvider != null) {
            jooqPersistenceConfiguration.typeConverterProvider.provideConverters().forEach(
                converterRegistry::registerConverter
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PersistenceMirror<UpdatableRecord<?>> buildPersistenceMirror() {
        JooqDomainPersistenceConfiguration jooqPersistenceConfiguration =
            (JooqDomainPersistenceConfiguration) domainPersistenceConfiguration;

        Map<String, List<String>> recordCanonicalNameToDomainObjectTypeMap = new HashMap<>();
        Map<String, String> entityToRecordTypeMap = new HashMap<>();

        List<EntityValueObjectRecordTypeConfiguration<?>> recordMappedValueObjectConfigurations = new ArrayList<>();
        if (jooqPersistenceConfiguration.entityValueObjectRecordClassProvider != null) {
            List<EntityValueObjectRecordTypeConfiguration<?>> providedConfigurations =
                jooqPersistenceConfiguration.entityValueObjectRecordClassProvider
                .provideContainedValueObjectRecordClassConfigurations();
            if (providedConfigurations != null) {
                recordMappedValueObjectConfigurations.addAll(providedConfigurations);
            }
        }

        var allEntityMirrors = Domain.getInitializedDomain()
            .allTypeMirrors()
            .values()
            .stream()
            .filter(dtm -> DomainType.ENTITY.equals(dtm.getDomainType()) || DomainType.AGGREGATE_ROOT.equals(
                dtm.getDomainType()))
            .filter(dtm -> !dtm.isAbstract())
            .map(dtm -> (EntityMirror) dtm)
            .toList();

        allEntityMirrors.forEach(em -> {

                Set<Class<? extends UpdatableRecord<?>>> recordTypeSet = jooqPersistenceConfiguration
                    .recordClassProvider.provideRecordClasses();
                Optional<Class<? extends UpdatableRecord<?>>> recordType;
                if (recordTypeSet != null && !recordTypeSet.isEmpty()) {
                    recordType = jooqPersistenceConfiguration.recordTypeToEntityTypeMatcher.findMatchingRecordType(
                        recordTypeSet, em.getTypeName());
                    if (recordType.isEmpty()) {
                        throw DLCPersistenceException.fail("Couldn't find record type for '%s'. " +
                            "You must obey the naming conventions or override RecordTypeToDomainObjectTypeMatcher! " +
                            "+\n" +
                            "Make sure that the records have a primary key defined in the database!", em.getTypeName());
                    }
                } else {
                    throw DLCPersistenceException.fail("No record types are defined. Rethink your " +
                        "'recordClassProvider'!");
                }
                //add entity record and entity type to the record -> domain object type map
                addRecordToDomainObjectTypeEntry(recordType.get().getName(), em.getTypeName(),
                    recordCanonicalNameToDomainObjectTypeMap);
                entityToRecordTypeMap.put(em.getTypeName(), recordType.get().getName());
                //for all custom "record mapped" value object configurations: add record and valueobject type to the
            // record --> domain object type map
                recordMappedValueObjectConfigurations
                    .stream()
                    .filter(c -> c.containingEntityType().getName().equals(em.getTypeName()))
                    .forEach(conf -> addRecordToDomainObjectTypeEntry(conf.valueObjectRecordType().getName(),
                        conf.containedValueObjectType().getName(), recordCanonicalNameToDomainObjectTypeMap));
            }
        );


        EntityRecordMirror<?>[] entityRecordMirrors = allEntityMirrors
            .stream()
            .map(em -> {
                List<ValueObjectRecordMirror<UpdatableRecord<?>>> valueObjectRecordMirrors = new ArrayList<>();
                var v = new ContextDomainObjectVisitor(em.getTypeName(), true) {
                    @Override
                    public boolean visitEnterEntity(EntityMirror entityMirror) {
                        var context = getVisitorContext();
                        return entityMirror.getTypeName().equals(context.startingTypeName)
                            && !context.isAlreadyVisited(context.startingTypeName);
                    }

                    @Override
                    public boolean visitEnterValue(ValueMirror valueMirror) {
                        return valueMirror.isValueObject();
                    }

                    @Override
                    public void visitValueReference(ValueReferenceMirror valueReferenceMirror) {
                        if (valueReferenceMirror.getType().getDomainType().equals(DomainType.VALUE_OBJECT)) {
                            var context = getVisitorContext();
                            var valueObjectRecordDefinition =
                                findCustomValueObjectRecordDefinition(
                                    em.getTypeName(),
                                    valueReferenceMirror.getType().getTypeName(),
                                    context.getCurrentPath().stream().map(FieldMirror::getName).toList(),
                                    jooqPersistenceConfiguration
                                );

                            if (valueObjectRecordDefinition == null && valueReferenceMirror.getType().hasCollectionContainer()) {
                                //if we have no custom record and record mapper definition and we have a to-many
                                // relationship,
                                // we need to build a valueObjectRecordDefinition for the auto mapping of the value
                                // object
                                valueObjectRecordDefinition = createAutoMappingValueObjectRecordDefinition(
                                    em.getTypeName(),
                                    valueReferenceMirror.getType().getTypeName(),
                                    context.getCurrentPath().stream().map(FieldMirror::getName).toList(),
                                    jooqPersistenceConfiguration);
                            }

                            if (valueObjectRecordDefinition != null) {
                                addRecordToDomainObjectTypeEntry(
                                    valueObjectRecordDefinition.valueObjectRecordType().getName(),
                                    valueObjectRecordDefinition.containedValueObjectTypeName(),
                                    recordCanonicalNameToDomainObjectTypeMap
                                );
                                ValueObjectRecordMirror<UpdatableRecord<?>> vorm = jooqPersistenceConfiguration
                                    .recordMirrorInstanceProvider
                                    .provideValueObjectRecordMirror(
                                        valueObjectRecordDefinition.containingEntityTypeName,
                                        valueObjectRecordDefinition.containedValueObjectTypeName,
                                        (Class<? extends UpdatableRecord<?>>) valueObjectRecordDefinition.valueObjectRecordType(),
                                        Arrays.asList(valueObjectRecordDefinition.pathFromEntityToValueObject),
                                        getValueObjectRecordMapperFor(
                                            valueObjectRecordDefinition,
                                            jooqPersistenceConfiguration
                                        ),
                                        recordCanonicalNameToDomainObjectTypeMap
                                    );

                                valueObjectRecordMirrors.add(vorm);
                            }
                        }
                    }
                };
                v.start();

                List<ValueObjectRecordMirror<UpdatableRecord<?>>> entityVorms = valueObjectRecordMirrors
                    .stream()
                    .filter(vorm -> vorm.containingEntityTypeName().equals(em.getTypeName()))
                    .collect(Collectors.toList());
                return jooqPersistenceConfiguration
                    .recordMirrorInstanceProvider
                    .provideEntityRecordMirror(
                        entityToRecordTypeMap.get(em.getTypeName()),
                        em.getTypeName(),
                        getEntityRecordMapperFor(entityToRecordTypeMap.get(em.getTypeName()), em.getTypeName(),
                            domainPersistenceConfiguration.customRecordMappers, jooqPersistenceConfiguration),
                        entityVorms,
                        recordCanonicalNameToDomainObjectTypeMap
                    );
            })
            .toArray(i -> new EntityRecordMirror[i]);

        return new PersistenceModel(entityRecordMirrors);
    }

    private void addRecordToDomainObjectTypeEntry(String recordName, String domainObjectTypeName, Map<String,
        List<String>> recordToDomainObjectTypeMap) {
        var list = recordToDomainObjectTypeMap.get(recordName);
        if (list == null) {
            list = new ArrayList<>();
            recordToDomainObjectTypeMap.put(recordName, list);
        }
        list.add(domainObjectTypeName);
    }

    private InternalValueObjectRecordDefinition findCustomValueObjectRecordDefinition(
        String entityTypeName,
        String valueObjectTypeName,
        List<String> accessPath,
        JooqDomainPersistenceConfiguration jooqPersistenceConfiguration) {

        if (jooqPersistenceConfiguration.entityValueObjectRecordClassProvider == null
            || jooqPersistenceConfiguration.entityValueObjectRecordClassProvider
            .provideContainedValueObjectRecordClassConfigurations() == null) {
            return null;
        }

        var foundConfigurations = jooqPersistenceConfiguration.entityValueObjectRecordClassProvider
            .provideContainedValueObjectRecordClassConfigurations().stream()
            .filter(c -> {
                final var x = Arrays.asList(c.pathFromEntityToValueObject());
                return c.containingEntityType().getName().equals(entityTypeName)
                    && c.containedValueObjectType().getName().equals(valueObjectTypeName)
                    && x.containsAll(accessPath)
                    && accessPath.containsAll(x);
            }).toList();

        if (foundConfigurations.size() > 1) {
            throw DLCPersistenceException.fail(
                "Multiple value object record class configurations (" + foundConfigurations + ") found for " +
                    "composition of " +
                    valueObjectTypeName + " within " + entityTypeName);
        } else if (foundConfigurations.size() == 0) {
            return null;
        }
        return InternalValueObjectRecordDefinition.of(foundConfigurations.get(0));
    }

    private InternalValueObjectRecordDefinition createAutoMappingValueObjectRecordDefinition(
        String entityTypeName,
        String valueObjectTypeName,
        List<String> accessPath,
        JooqDomainPersistenceConfiguration jooqPersistenceConfiguration) {

        Set<Class<? extends UpdatableRecord<?>>> recordTypeSet = jooqPersistenceConfiguration
            .recordClassProvider.provideRecordClasses();
        if (recordTypeSet != null && !recordTypeSet.isEmpty()) {
            var simpleEntityTypeName = entityTypeName.substring(entityTypeName.lastIndexOf(".") + 1);
            var recordTypeName = simpleEntityTypeName + accessPath
                .stream()
                .map(n -> n.substring(0, 1).toUpperCase() + n.substring(1))
                .collect(Collectors.joining("")) + "Record";
            var recordTypes = recordTypeSet
                .stream()
                .filter(c -> c.getSimpleName().equals(recordTypeName)).toList();
            if (recordTypes.size() == 0) {
                throw DLCPersistenceException.fail("No value object record type found for composition of " +
                        " '%1$s' within '%2$s', when trying to initiate value object auto mapping. Expected a record " +
                        "with the name '%3$s'",
                    valueObjectTypeName,
                    entityTypeName,
                    recordTypeName
                );
            } else if (recordTypes.size() > 1) {
                throw DLCPersistenceException.fail(
                    "Multiple record types found for '%1$s' when trying to initiate value object auto mapping for " +
                        "'%2$s' within '%3$s'!",
                    recordTypeName,
                    valueObjectTypeName,
                    entityTypeName
                );
            }
            Class<UpdatableRecord<?>> voRecordType = (Class<UpdatableRecord<?>>) recordTypes.get(0);
            // configuration for auto mapping
            return new InternalValueObjectRecordDefinition(
                entityTypeName,
                valueObjectTypeName,
                voRecordType,
                accessPath.toArray(String[]::new));
        }
        throw DLCPersistenceException.fail("No record types are defined. Rethink your 'recordClassProvider'!");
    }

    private RecordMapper<UpdatableRecord<?>, ?, ?> getEntityRecordMapperFor(
        String recordTypeName,
        String entityTypeName,
        Set<RecordMapper<?, ?, ?>> customMapperSet,
        JooqDomainPersistenceConfiguration jooqPersistenceConfiguration
    ) {
        RecordMapper<?, ?, ?> mapper = null;
        if (customMapperSet != null && !customMapperSet.isEmpty()) {
            var mapperOptional = customMapperSet.stream()
                .filter(m -> m.recordType().getName().equals(recordTypeName)
                    && m.domainObjectType().getName().equals(entityTypeName))
                .findFirst();
            if (mapperOptional.isPresent()) {
                mapper = mapperOptional.get();
            } else {
                //check hierarchy
                var em = Domain.entityMirrorFor(entityTypeName);
                var entitySuperClasses = em.getInheritanceHierarchyTypeNames();
                for (String superType : entitySuperClasses) {
                    mapperOptional = customMapperSet.stream()
                        .filter(m -> m.recordType().getName().equals(recordTypeName)
                            && m.domainObjectType().getName().equals(superType))
                        .findFirst();
                    if (mapperOptional.isPresent()) {
                        mapper = mapperOptional.get();
                        break;
                    }
                }
            }

        }

        if (mapper == null) {
            //Use default mapper, built upon conventions

            mapper = new AutoRecordMapper(
                entityTypeName,
                recordTypeName,
                jooqPersistenceConfiguration.recordPropertyMatcher,
                domainPersistenceConfiguration.domainObjectBuilderProvider,
                jooqPersistenceConfiguration.ignoredDomainObjectFields,
                jooqPersistenceConfiguration.ignoredRecordProperties,
                this.converterRegistry,
                jooqPersistenceConfiguration.newRecordInstanceProvider,
                jooqPersistenceConfiguration.recordPropertyAccessor,
                jooqPersistenceConfiguration.recordPropertyProvider,
                jooqPersistenceConfiguration.entityValueObjectRecordClassProvider,
                jooqPersistenceConfiguration.recordClassProvider
            );
        }
        return (RecordMapper<UpdatableRecord<?>, ?, ?>) mapper;
    }


    private RecordMapper<UpdatableRecord<?>, ?, ?> getValueObjectRecordMapperFor(
        InternalValueObjectRecordDefinition entityValueObjectRecordTypeConfiguration,
        JooqDomainPersistenceConfiguration jooqPersistenceConfiguration

    ) {
        RecordMapper<?, ?, ?> mapper = null;
        var customMapperSet = jooqPersistenceConfiguration.customRecordMappers;
        if (customMapperSet != null && !customMapperSet.isEmpty()) {

            var mapperTuple = customMapperSet.stream()
                .filter(m -> m.recordType().getName().equals(
                    entityValueObjectRecordTypeConfiguration.valueObjectRecordType().getName())
                    && m.domainObjectType().getName().equals(
                    entityValueObjectRecordTypeConfiguration.containedValueObjectTypeName()))
                .findFirst();
            if (mapperTuple.isPresent()) {
                mapper = mapperTuple.get();
            }
        }
        if (mapper == null) {
            mapper = new AutoRecordMapper<>(
                entityValueObjectRecordTypeConfiguration.containedValueObjectTypeName(),
                entityValueObjectRecordTypeConfiguration.valueObjectRecordType().getName(),
                jooqPersistenceConfiguration.recordPropertyMatcher,
                domainPersistenceConfiguration.domainObjectBuilderProvider,
                jooqPersistenceConfiguration.ignoredDomainObjectFields,
                jooqPersistenceConfiguration.ignoredRecordProperties,
                this.converterRegistry,
                jooqPersistenceConfiguration.newRecordInstanceProvider,
                jooqPersistenceConfiguration.recordPropertyAccessor,
                jooqPersistenceConfiguration.recordPropertyProvider,
                jooqPersistenceConfiguration.entityValueObjectRecordClassProvider,
                jooqPersistenceConfiguration.recordClassProvider
            );
        }
        return (RecordMapper<UpdatableRecord<?>, ?, ?>) mapper;
    }


    private record InternalValueObjectRecordDefinition(
        String containingEntityTypeName,
        String containedValueObjectTypeName,
        Class<?> valueObjectRecordType,
        String... pathFromEntityToValueObject
    ) {

        static InternalValueObjectRecordDefinition of(EntityValueObjectRecordTypeConfiguration<?> entityValueObjectRecordTypeConfiguration) {
            return new InternalValueObjectRecordDefinition(
                entityValueObjectRecordTypeConfiguration.containingEntityType().getName(),
                entityValueObjectRecordTypeConfiguration.containedValueObjectType().getName(),
                entityValueObjectRecordTypeConfiguration.valueObjectRecordType(),
                entityValueObjectRecordTypeConfiguration.pathFromEntityToValueObject()
            );
        }
    }

}
