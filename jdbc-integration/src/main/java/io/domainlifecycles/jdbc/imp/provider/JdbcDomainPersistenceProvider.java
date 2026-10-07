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

package io.domainlifecycles.jdbc.imp.provider;

import io.domainlifecycles.jdbc.configuration.JdbcDomainPersistenceConfiguration;
import io.domainlifecycles.jdbc.configuration.JdbcEntityValueObjectRecordTypeConfiguration;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
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
import io.domainlifecycles.persistence.mapping.ScalarListElementRecordMapper;
import io.domainlifecycles.persistence.mirror.PersistenceModel;
import io.domainlifecycles.persistence.mirror.api.EntityRecordMirror;
import io.domainlifecycles.persistence.mirror.api.PersistenceMirror;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;
import io.domainlifecycles.persistence.provider.DomainPersistenceProvider;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordClassProvider;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordTypeConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Plain JDBC based implementation of a {@link DomainPersistenceProvider}.
 * <p>
 * Structurally this mirrors {@code JooqDomainPersistenceProvider}: for every non-abstract entity/aggregate
 * root type it resolves the table representing it, then walks the type's object graph to discover every
 * {@code List<ValueObject>}/{@code List<Identity>}/{@code List<Enum>} field that is persisted in its own
 * child table (possibly nested several levels deep), building one {@link EntityRecordMirror} per entity type
 * and one {@link ValueObjectRecordMirror} per such child table.
 * <p>
 * The two differences to the jOOQ based implementation both follow from every table being represented by
 * the same {@link JdbcRecord} class rather than a per-table generated class:
 * <ul>
 *     <li>tables are resolved by {@link io.domainlifecycles.jdbc.imp.matcher.JdbcTableToEntityTypeMatcher}
 *     purely by naming convention, since there is no explicit "record class" to configure or discover;</li>
 *     <li>a custom record mapper is matched to a domain object type by domain object type name alone,
 *     without also requiring {@code RecordMapper.recordType()} to match a per-table {@code Class} (every
 *     mapper's {@code recordType()} is {@code JdbcRecord.class}, so that check would be vacuous).</li>
 * </ul>
 *
 * @author Mario Herb
 */
public class JdbcDomainPersistenceProvider extends DomainPersistenceProvider<JdbcRecord> {

    /**
     * Supplies the connection used for all database interaction. Registered centrally here (via the
     * configuration this provider is built from) so that repositories, persisters and fetchers can obtain it
     * from the provider instead of requiring it as a separate constructor parameter of their own.
     */
    public final JdbcConnectionProvider connectionProvider;

    /**
     * The dialect used for sequence access. Registered centrally for the same reason as {@link
     * #connectionProvider}.
     */
    public final JdbcDialect dialect;

    /**
     * The schema metadata snapshot used to resolve tables and foreign keys. Registered centrally for the same
     * reason as {@link #connectionProvider}.
     */
    public final JdbcSchemaMetadata schemaMetadata;

    /**
     * Constructs an instance of {@code JdbcDomainPersistenceProvider} using the provided configuration.
     * Registers converters provided by the type converter provider within the configuration, if available.
     *
     * @param jdbcPersistenceConfiguration the configuration object containing settings and dependencies
     *                                     required for setting up the JDBC domain persistence provider
     */
    public JdbcDomainPersistenceProvider(JdbcDomainPersistenceConfiguration jdbcPersistenceConfiguration) {
        super(jdbcPersistenceConfiguration, jdbcPersistenceConfiguration.transactionCacheProvider);
        this.connectionProvider = jdbcPersistenceConfiguration.connectionProvider;
        this.dialect = jdbcPersistenceConfiguration.dialect;
        this.schemaMetadata = jdbcPersistenceConfiguration.schemaMetadata;
        if (jdbcPersistenceConfiguration.typeConverterProvider != null) {
            jdbcPersistenceConfiguration.typeConverterProvider.provideConverters().forEach(
                converterRegistry::registerConverter
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected PersistenceMirror<JdbcRecord> buildPersistenceMirror() {
        JdbcDomainPersistenceConfiguration jdbcPersistenceConfiguration =
            (JdbcDomainPersistenceConfiguration) domainPersistenceConfiguration;

        // Table-name-keyed, and thus - like every physical-name lookup in this module (see
        // JdbcSchemaMetadata.findTable) - case-insensitive: entries added for a plain entity are already
        // schema-metadata-derived (correct physical case), but entries added for an explicit
        // JdbcEntityValueObjectRecordTypeConfiguration use whatever case the caller wrote the table name
        // literal in (this module's own tests always write it upper case), while enforcedReferences() below
        // looks entries up by ForeignKeyMetadata.referencedTableName() - always schema-metadata-derived, i.e.
        // physical case (upper on H2/Oracle, lower on Postgres/MySQL/SQL Server). A case-sensitive HashMap
        // silently failed that lookup on every non-upper-case-folding dialect, which
        // JdbcValueObjectRecordMirrorImpl then read as "value object has no FK to its container" and dropped
        // the whole nested value-object-record-mirror without any other symptom until fetch time.
        Map<String, List<String>> recordCanonicalNameToDomainObjectTypeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        Map<String, String> entityToRecordTypeMap = new HashMap<>();

        var allEntityMirrors = Domain.getDomainMirror()
            .getAllDomainTypeMirrors()
            .stream()
            .filter(dtm -> DomainType.ENTITY.equals(dtm.getDomainType()) || DomainType.AGGREGATE_ROOT.equals(
                dtm.getDomainType()))
            .filter(dtm -> !dtm.isAbstract())
            .map(dtm -> (EntityMirror) dtm)
            .toList();

        var availableTableNames = jdbcPersistenceConfiguration.schemaMetadata.tableNames();
        allEntityMirrors.forEach(em -> {
            var tableName = jdbcPersistenceConfiguration.tableToEntityTypeMatcher.findMatchingTable(
                    availableTableNames, em.getTypeName())
                .orElseThrow(() -> DLCPersistenceException.fail("Couldn't find table for '%s'. " +
                    "You must obey the naming conventions or override JdbcTableToEntityTypeMatcher! " +
                    "Make sure that the table has a primary key defined in the database!", em.getTypeName()));
            addRecordToDomainObjectTypeEntry(tableName, em.getTypeName(), recordCanonicalNameToDomainObjectTypeMap);
            entityToRecordTypeMap.put(em.getTypeName(), tableName);
        });

        EntityRecordMirror<JdbcRecord>[] entityRecordMirrors = allEntityMirrors
            .stream()
            .map(em -> {
                List<ValueObjectRecordMirror<JdbcRecord>> valueObjectRecordMirrors = new ArrayList<>();
                var v = new ContextDomainObjectVisitor(em, true) {
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
                        //fields explicitly excluded from auto mapping (e.g. handled by a custom record mapper)
                        //must not require a value object table definition of their own - neither must any
                        //field nested within them, so the whole path from the entity is checked, not just the
                        //current field (the path always ends with the current field)
                        if (jdbcPersistenceConfiguration.ignoredDomainObjectFields != null
                            && getVisitorContext().getCurrentPath().stream()
                            .anyMatch(jdbcPersistenceConfiguration.ignoredDomainObjectFields::isIgnored)) {
                            return;
                        }
                        var referencedDomainType = valueReferenceMirror.getType().getDomainType();
                        //a "scalar list element" is a field like List<SomeIdentity>/List<SomeEnum>: the raw
                        //element is not a ValueObject, but it is persisted as a child record exactly like a
                        //List<ValueObject> field is (own table, id + containerId, single "value" column)
                        boolean isScalarListElement =
                            (DomainType.IDENTITY.equals(referencedDomainType) || DomainType.ENUM.equals(
                                referencedDomainType))
                                && valueReferenceMirror.getType().hasCollectionContainer();
                        if (DomainType.VALUE_OBJECT.equals(referencedDomainType) || isScalarListElement) {
                            var context = getVisitorContext();
                            var accessPath = context.getCurrentPath().stream().map(FieldMirror::getName).toList();
                            var definition = findCustomValueObjectRecordDefinition(
                                em.getTypeName(),
                                valueReferenceMirror.getType().getTypeName(),
                                accessPath,
                                jdbcPersistenceConfiguration);
                            if (definition == null && valueReferenceMirror.getType().hasCollectionContainer()) {
                                //no explicit configuration found for a to-many relationship: fall back to
                                //auto-mapping by naming convention
                                definition = createAutoMappingValueObjectRecordDefinition(
                                    em.getTypeName(),
                                    valueReferenceMirror.getType().getTypeName(),
                                    accessPath,
                                    jdbcPersistenceConfiguration);
                            }
                            if (definition != null) {
                                addRecordToDomainObjectTypeEntry(
                                    definition.tableName(),
                                    definition.containedValueObjectTypeName(),
                                    recordCanonicalNameToDomainObjectTypeMap
                                );
                                RecordMapper<JdbcRecord, ?, ?> mapper = isScalarListElement
                                    ? getScalarListElementRecordMapperFor(definition, jdbcPersistenceConfiguration)
                                    : getValueObjectRecordMapperFor(definition, jdbcPersistenceConfiguration);
                                ValueObjectRecordMirror<JdbcRecord> vorm = jdbcPersistenceConfiguration
                                    .recordMirrorInstanceProvider
                                    .provideValueObjectRecordMirror(
                                        definition.containingEntityTypeName(),
                                        definition.containedValueObjectTypeName(),
                                        definition.tableName(),
                                        accessPath,
                                        mapper,
                                        recordCanonicalNameToDomainObjectTypeMap
                                    );
                                valueObjectRecordMirrors.add(vorm);
                            }
                            //else: a value object without a collection container and without an explicit
                            //table configuration is embedded inline in its containing record and mapped by
                            //that record's own AutoRecordMapper; the visitor still descends into it (see
                            //visitEnterValue) to discover any collection-valued fields nested deeper
                        }
                    }
                };
                v.start();

                List<ValueObjectRecordMirror<JdbcRecord>> entityVorms = valueObjectRecordMirrors
                    .stream()
                    .filter(vorm -> vorm.containingEntityTypeName().equals(em.getTypeName()))
                    .collect(Collectors.toList());

                return jdbcPersistenceConfiguration
                    .recordMirrorInstanceProvider
                    .provideEntityRecordMirror(
                        entityToRecordTypeMap.get(em.getTypeName()),
                        em.getTypeName(),
                        getEntityRecordMapperFor(entityToRecordTypeMap.get(em.getTypeName()), em.getTypeName(),
                            domainPersistenceConfiguration.customRecordMappers, jdbcPersistenceConfiguration),
                        entityVorms,
                        recordCanonicalNameToDomainObjectTypeMap
                    );
            })
            .toArray(EntityRecordMirror[]::new);

        return new PersistenceModel<JdbcRecord>(entityRecordMirrors);
    }

    private void addRecordToDomainObjectTypeEntry(String recordName, String domainObjectTypeName, Map<String,
        List<String>> recordToDomainObjectTypeMap) {
        var list = recordToDomainObjectTypeMap.computeIfAbsent(recordName, k -> new ArrayList<>());
        list.add(domainObjectTypeName);
    }

    private InternalValueObjectRecordDefinition findCustomValueObjectRecordDefinition(
        String entityTypeName,
        String valueObjectTypeName,
        List<String> accessPath,
        JdbcDomainPersistenceConfiguration jdbcPersistenceConfiguration) {

        if (jdbcPersistenceConfiguration.entityValueObjectRecordClassProvider == null) {
            return null;
        }
        var configs = jdbcPersistenceConfiguration.entityValueObjectRecordClassProvider
            .provideContainedValueObjectRecordClassConfigurations();
        if (configs == null) {
            return null;
        }
        var matches = configs.stream()
            .filter(c -> c.containingEntityType().getName().equals(entityTypeName)
                && c.containedValueObjectType().getName().equals(valueObjectTypeName)
                && Arrays.asList(c.pathFromEntityToValueObject()).equals(accessPath))
            .toList();
        if (matches.size() > 1) {
            throw DLCPersistenceException.fail(
                "Multiple value object table configurations found for composition of '%1$s' within '%2$s' at " +
                    "path '%3$s'!", valueObjectTypeName, entityTypeName, String.join(".", accessPath));
        }
        if (matches.isEmpty()) {
            return null;
        }
        var config = matches.get(0);
        return new InternalValueObjectRecordDefinition(
            entityTypeName, valueObjectTypeName, config.tableName(), accessPath);
    }

    private InternalValueObjectRecordDefinition createAutoMappingValueObjectRecordDefinition(
        String entityTypeName,
        String valueObjectTypeName,
        List<String> accessPath,
        JdbcDomainPersistenceConfiguration jdbcPersistenceConfiguration) {

        var simpleEntityTypeName = simpleName(entityTypeName);
        var candidateTableName = simpleEntityTypeName + accessPath
            .stream()
            .map(n -> n.substring(0, 1).toUpperCase() + n.substring(1))
            .collect(Collectors.joining(""));
        var matchedTables = jdbcPersistenceConfiguration.schemaMetadata.tableNames()
            .stream()
            .filter(t -> t.replaceAll("_", "").equalsIgnoreCase(candidateTableName))
            .toList();
        if (matchedTables.isEmpty()) {
            throw DLCPersistenceException.fail("No table found for composition of "
                    + " '%1$s' within '%2$s', when trying to initiate value object auto mapping. Expected a table " +
                    "matching the name '%3$s' (case-insensitive, ignoring underscores)",
                valueObjectTypeName,
                entityTypeName,
                candidateTableName
            );
        } else if (matchedTables.size() > 1) {
            throw DLCPersistenceException.fail(
                "Multiple tables found matching '%1$s' when trying to initiate value object auto mapping for " +
                    "'%2$s' within '%3$s'!",
                candidateTableName,
                valueObjectTypeName,
                entityTypeName
            );
        }
        return new InternalValueObjectRecordDefinition(
            entityTypeName,
            valueObjectTypeName,
            matchedTables.get(0),
            accessPath);
    }

    private String simpleName(String fullQualifiedTypeName) {
        var dotIndex = fullQualifiedTypeName.lastIndexOf(".");
        return dotIndex >= 0 ? fullQualifiedTypeName.substring(dotIndex + 1) : fullQualifiedTypeName;
    }

    private RecordMapper<JdbcRecord, ?, ?> getEntityRecordMapperFor(
        String tableName,
        String entityTypeName,
        Set<RecordMapper<?, ?, ?>> customMapperSet,
        JdbcDomainPersistenceConfiguration jdbcPersistenceConfiguration
    ) {
        RecordMapper<?, ?, ?> mapper = findCustomMapperByDomainObjectType(customMapperSet, entityTypeName);
        if (mapper == null && customMapperSet != null && !customMapperSet.isEmpty()) {
            //check hierarchy
            var em = Domain.entityMirrorFor(entityTypeName);
            for (String superType : em.getInheritanceHierarchyTypeNames()) {
                mapper = findCustomMapperByDomainObjectType(customMapperSet, superType);
                if (mapper != null) {
                    break;
                }
            }
        }

        if (mapper == null) {
            //Use default mapper, built upon conventions
            mapper = new AutoRecordMapper<>(
                entityTypeName,
                tableName,
                jdbcPersistenceConfiguration.recordPropertyMatcher,
                domainPersistenceConfiguration.domainObjectBuilderProvider,
                jdbcPersistenceConfiguration.ignoredDomainObjectFields,
                jdbcPersistenceConfiguration.ignoredRecordProperties,
                this.converterRegistry,
                jdbcPersistenceConfiguration.newRecordInstanceProvider,
                jdbcPersistenceConfiguration.recordPropertyAccessor,
                jdbcPersistenceConfiguration.recordPropertyProvider,
                adaptEntityValueObjectRecordClassProvider(jdbcPersistenceConfiguration),
                JdbcRecord.class
            );
        }
        return (RecordMapper<JdbcRecord, ?, ?>) mapper;
    }

    private RecordMapper<JdbcRecord, ?, ?> getValueObjectRecordMapperFor(
        InternalValueObjectRecordDefinition definition,
        JdbcDomainPersistenceConfiguration jdbcPersistenceConfiguration
    ) {
        RecordMapper<?, ?, ?> mapper = findCustomMapperByDomainObjectType(
            domainPersistenceConfiguration.customRecordMappers, definition.containedValueObjectTypeName());
        if (mapper == null) {
            mapper = new AutoRecordMapper<>(
                definition.containedValueObjectTypeName(),
                definition.tableName(),
                jdbcPersistenceConfiguration.recordPropertyMatcher,
                domainPersistenceConfiguration.domainObjectBuilderProvider,
                jdbcPersistenceConfiguration.ignoredDomainObjectFields,
                jdbcPersistenceConfiguration.ignoredRecordProperties,
                this.converterRegistry,
                jdbcPersistenceConfiguration.newRecordInstanceProvider,
                jdbcPersistenceConfiguration.recordPropertyAccessor,
                jdbcPersistenceConfiguration.recordPropertyProvider,
                adaptEntityValueObjectRecordClassProvider(jdbcPersistenceConfiguration),
                JdbcRecord.class
            );
        }
        return (RecordMapper<JdbcRecord, ?, ?>) mapper;
    }

    /**
     * Adapts this module's table-name-based {@code JdbcEntityValueObjectRecordClassProvider} to the shared,
     * {@code Class}-based {@link EntityValueObjectRecordClassProvider} that {@link AutoRecordMapper} expects.
     * <p>
     * {@code AutoRecordMapper} only ever reads {@link EntityValueObjectRecordTypeConfiguration#pathFromEntityToValueObject()}
     * from the configurations this yields (to know which nested value object paths are mapped in their own,
     * separate record rather than inline) - it never reads {@code valueObjectRecordType()} - so passing a
     * constant, unused {@link JdbcRecord} class for that field is safe and requires no change to the shared
     * {@code persistence} module.
     */
    private EntityValueObjectRecordClassProvider adaptEntityValueObjectRecordClassProvider(
        JdbcDomainPersistenceConfiguration jdbcPersistenceConfiguration
    ) {
        if (jdbcPersistenceConfiguration.entityValueObjectRecordClassProvider == null) {
            return null;
        }
        return () -> {
            var configs = jdbcPersistenceConfiguration.entityValueObjectRecordClassProvider
                .provideContainedValueObjectRecordClassConfigurations();
            if (configs == null) {
                return List.of();
            }
            return configs.stream()
                .map(c -> new EntityValueObjectRecordTypeConfiguration(
                    c.containingEntityType(),
                    c.containedValueObjectType(),
                    JdbcRecord.class,
                    c.pathFromEntityToValueObject()))
                .toList();
        };
    }

    private RecordMapper<JdbcRecord, ?, ?> getScalarListElementRecordMapperFor(
        InternalValueObjectRecordDefinition definition,
        JdbcDomainPersistenceConfiguration jdbcPersistenceConfiguration
    ) {
        return new ScalarListElementRecordMapper<>(
            definition.containedValueObjectTypeName(),
            definition.tableName(),
            this.converterRegistry,
            jdbcPersistenceConfiguration.newRecordInstanceProvider,
            jdbcPersistenceConfiguration.recordPropertyAccessor,
            jdbcPersistenceConfiguration.recordPropertyProvider,
            JdbcRecord.class
        );
    }

    private RecordMapper<?, ?, ?> findCustomMapperByDomainObjectType(
        Set<RecordMapper<?, ?, ?>> customMapperSet,
        String domainObjectTypeName
    ) {
        if (customMapperSet == null || customMapperSet.isEmpty()) {
            return null;
        }
        return customMapperSet.stream()
            .filter(m -> m.domainObjectType().getName().equals(domainObjectTypeName))
            .findFirst()
            .orElse(null);
    }

    private record InternalValueObjectRecordDefinition(
        String containingEntityTypeName,
        String containedValueObjectTypeName,
        String tableName,
        List<String> pathFromEntityToValueObject
    ) {
    }
}
