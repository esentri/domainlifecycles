package io.domainlifecycles.jdbc.persistence.tests.hierarchical;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.imp.JdbcPersister;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.persistence.PhysicalNames;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.jdbc.util.JdbcRecordMapper;
import io.domainlifecycles.persistence.fetcher.AggregateFetcher;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.fetcher.simple.SimpleAggregateFetcher;
import io.domainlifecycles.persistence.fetcher.simple.SimpleFetcherContext;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.repository.PersistenceActionPublishingRepository;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchical;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchicalId;

import java.util.List;

/**
 * Self-referencing aggregate root (see {@code TestRootHierarchicalJdbcRecordMapper}): {@link
 * io.domainlifecycles.jdbc.imp.JdbcAggregateFetcher} explicitly rejects self-references between parent and
 * child table, so - exactly like jooq-integration's own {@code HierarchicalAggregateRootRepository} - this
 * repository fetches the recursive parent/child chain itself via a hand-written {@code findByIdCustom} instead
 * of using {@link io.domainlifecycles.jdbc.imp.JdbcAggregateRepository}.
 */
public class HierarchicalAggregateRootRepository
    extends PersistenceActionPublishingRepository<TestRootHierarchicalId, TestRootHierarchical, JdbcRecord> {

    private final JdbcConnectionProvider connectionProvider;
    private final JdbcSchemaMetadata schemaMetadata;
    private final JdbcDomainPersistenceProvider domainPersistenceProvider;
    private final SimpleAggregateFetcher<Long, TestRootHierarchical, TestRootHierarchicalId, JdbcRecord>
        simpleAggregateFetcher;

    public HierarchicalAggregateRootRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                                                PersistenceEventPublisher persistenceEventPublisher) {
        super(
            new JdbcPersister(domainPersistenceProvider),
            domainPersistenceProvider,
            persistenceEventPublisher
        );
        this.connectionProvider = domainPersistenceProvider.connectionProvider;
        this.schemaMetadata = domainPersistenceProvider.schemaMetadata;
        this.domainPersistenceProvider = domainPersistenceProvider;
        this.simpleAggregateFetcher = provideFetcher();
    }

    private SimpleAggregateFetcher<Long, TestRootHierarchical, TestRootHierarchicalId, JdbcRecord> provideFetcher() {
        return new SimpleAggregateFetcher<>() {
            @Override
            public AggregateFetcher<TestRootHierarchical, TestRootHierarchicalId, JdbcRecord> withRecordProvider(
                RecordProvider<? extends JdbcRecord, ? extends JdbcRecord> recordProvider,
                Class<? extends Entity<?>> containingEntityClass,
                Class<? extends DomainObject> propertyClass,
                List<String> propertyPath) {
                throw new IllegalStateException("Not implemented!");
            }

            @Override
            public TestRootHierarchical fetchBasicByIdValue(Long aLong,
                                                              SimpleFetcherContext<JdbcRecord> fetcherContext) {
                return findByIdCustom(aLong);
            }

            @Override
            public TestRootHierarchical fetchBasicByRecord(JdbcRecord aggregateRecord,
                                                             SimpleFetcherContext<JdbcRecord> fetcherContext) {
                throw new IllegalStateException("Not implemented!");
            }
        };
    }

    @SuppressWarnings("unchecked")
    public TestRootHierarchical findByIdCustom(Long testRootHierarchicalId) {
        var table = schemaMetadata.table(PhysicalNames.name("TEST_ROOT_HIERARCHICAL"));
        JdbcRecord record = selectByColumn(table, PhysicalNames.name("ID"), testRootHierarchicalId);
        if (record == null) {
            return null;
        }

        DomainObjectBuilder<TestRootHierarchical> b =
            ((RecordMapper<JdbcRecord, TestRootHierarchical, TestRootHierarchical>)
                domainPersistenceProvider.persistenceMirror
                    .getEntityRecordMapper(TestRootHierarchical.class.getName()))
                .recordToDomainObjectBuilder(record);

        JdbcRecord childRecord = selectByColumn(table, PhysicalNames.name("PARENT_ID"), testRootHierarchicalId);
        if (childRecord != null) {
            TestRootHierarchical child = findByIdCustom((Long) childRecord.get(PhysicalNames.name("ID")));
            b.setFieldValue(child, "child");
        }

        return b.build();
    }

    private JdbcRecord selectByColumn(TableMetadata table, String columnName, Object value) {
        return JdbcRecordMapper.selectOneByColumn(connectionProvider, domainPersistenceProvider.dialect, table, columnName, value);
    }

    @Override
    public FetcherResult<TestRootHierarchical, JdbcRecord> findResultById(TestRootHierarchicalId rootId) {
        return simpleAggregateFetcher.fetchDeep(rootId);
    }
}
