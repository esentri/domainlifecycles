package io.domainlifecycles.jdbc.persistence.tests.hierarchical;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcPersister;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public HierarchicalAggregateRootRepository(JdbcConnectionProvider connectionProvider,
                                                JdbcDialect dialect,
                                                JdbcSchemaMetadata schemaMetadata,
                                                JdbcDomainPersistenceProvider domainPersistenceProvider,
                                                PersistenceEventPublisher persistenceEventPublisher) {
        super(
            new JdbcPersister(connectionProvider, dialect, schemaMetadata, domainPersistenceProvider),
            domainPersistenceProvider,
            persistenceEventPublisher
        );
        this.connectionProvider = connectionProvider;
        this.schemaMetadata = schemaMetadata;
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
        var table = schemaMetadata.table("TEST_ROOT_HIERARCHICAL");
        JdbcRecord record = selectByColumn(table, "ID", testRootHierarchicalId);
        if (record == null) {
            return null;
        }

        DomainObjectBuilder<TestRootHierarchical> b =
            ((RecordMapper<JdbcRecord, TestRootHierarchical, TestRootHierarchical>)
                domainPersistenceProvider.persistenceMirror
                    .getEntityRecordMapper(TestRootHierarchical.class.getName()))
                .recordToDomainObjectBuilder(record);

        JdbcRecord childRecord = selectByColumn(table, "PARENT_ID", testRootHierarchicalId);
        if (childRecord != null) {
            TestRootHierarchical child = findByIdCustom((Long) childRecord.get("ID"));
            b.setFieldValue(child, "child");
        }

        return b.build();
    }

    private JdbcRecord selectByColumn(TableMetadata table, String columnName, Object value) {
        var sql = "SELECT * FROM " + table.qualifiedName() + " WHERE " + columnName + " = ?";
        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            statement.setObject(1, value);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                JdbcRecord record = new JdbcRecord(table.name());
                for (var column : table.columns()) {
                    record.set(column.name(), resultSet.getObject(column.name(), column.javaType()));
                }
                return record;
            }
        } catch (SQLException e) {
            throw DLCPersistenceException.fail("Query on '%s' failed.", e, table.name());
        }
    }

    @Override
    public FetcherResult<TestRootHierarchical, JdbcRecord> findResultById(TestRootHierarchicalId rootId) {
        return simpleAggregateFetcher.fetchDeep(rootId);
    }
}
