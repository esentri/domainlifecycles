package io.domainlifecycles.jdbc.persistence.tests.hierarchicalBackRef;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.imp.JdbcPersister;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
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
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackref;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackrefId;

import java.util.List;

/**
 * Same self-reference situation as {@code HierarchicalAggregateRootRepository}, plus a {@code parent}
 * back-reference field that this custom fetch also has to wire up manually after building each level, mirroring
 * jooq-integration's own {@code HierarchicalAggregateRootBackrefRepository}.
 */
public class HierarchicalAggregateRootBackrefRepository
    extends PersistenceActionPublishingRepository<TestRootHierarchicalBackrefId, TestRootHierarchicalBackref,
    JdbcRecord> {

    private final JdbcConnectionProvider connectionProvider;
    private final JdbcSchemaMetadata schemaMetadata;
    private final JdbcDomainPersistenceProvider domainPersistenceProvider;
    private final SimpleAggregateFetcher<Long, TestRootHierarchicalBackref, TestRootHierarchicalBackrefId,
        JdbcRecord> simpleAggregateFetcher;

    public HierarchicalAggregateRootBackrefRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
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

    private SimpleAggregateFetcher<Long, TestRootHierarchicalBackref, TestRootHierarchicalBackrefId, JdbcRecord>
        provideFetcher() {
        return new SimpleAggregateFetcher<>() {
            @Override
            public AggregateFetcher<TestRootHierarchicalBackref, TestRootHierarchicalBackrefId, JdbcRecord> withRecordProvider(
                RecordProvider<? extends JdbcRecord, ? extends JdbcRecord> recordProvider,
                Class<? extends Entity<?>> containingEntityClass,
                Class<? extends DomainObject> propertyClass,
                List<String> propertyPath) {
                throw new IllegalStateException("Not implemented!");
            }

            @Override
            public TestRootHierarchicalBackref fetchBasicByIdValue(Long aLong,
                                                                     SimpleFetcherContext<JdbcRecord> fetcherContext) {
                return findByIdCustom(aLong);
            }

            @Override
            public TestRootHierarchicalBackref fetchBasicByRecord(JdbcRecord aggregateRecord,
                                                                    SimpleFetcherContext<JdbcRecord> fetcherContext) {
                throw new IllegalStateException("Not implemented!");
            }
        };
    }

    @SuppressWarnings("unchecked")
    public TestRootHierarchicalBackref findByIdCustom(Long testRootHierarchicalBackrefId) {
        var table = schemaMetadata.table("TEST_ROOT_HIERARCHICAL_BACKREF");
        JdbcRecord record = selectByColumn(table, "ID", testRootHierarchicalBackrefId);
        if (record == null) {
            return null;
        }

        DomainObjectBuilder<TestRootHierarchicalBackref> b =
            ((RecordMapper<JdbcRecord, TestRootHierarchicalBackref, TestRootHierarchicalBackref>)
                domainPersistenceProvider.persistenceMirror
                    .getEntityRecordMapper(TestRootHierarchicalBackref.class.getName()))
                .recordToDomainObjectBuilder(record);

        JdbcRecord childRecord = selectByColumn(table, "PARENT_ID", testRootHierarchicalBackrefId);
        if (childRecord != null) {
            TestRootHierarchicalBackref child = findByIdCustom((Long) childRecord.get("ID"));
            b.setFieldValue(child, "child");
        }

        TestRootHierarchicalBackref tr = b.build();
        if (tr.getChild() != null) {
            tr.getChild().setParent(tr);
        }
        return tr;
    }

    private JdbcRecord selectByColumn(TableMetadata table, String columnName, Object value) {
        return JdbcRecordMapper.selectOneByColumn(connectionProvider, table, columnName, value);
    }

    @Override
    public FetcherResult<TestRootHierarchicalBackref, JdbcRecord> findResultById(
        TestRootHierarchicalBackrefId rootId) {
        return simpleAggregateFetcher.fetchDeep(rootId);
    }
}
