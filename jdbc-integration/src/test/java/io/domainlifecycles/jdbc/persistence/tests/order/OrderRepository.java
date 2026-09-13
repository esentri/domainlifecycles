package io.domainlifecycles.jdbc.persistence.tests.order;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateFetcher;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.complete.ecommerce.order.OrderItemBv3;
import tests.shared.complete.ecommerce.order.OrderStatusCodeEnumBv3;
import tests.shared.complete.ecommerce.order.OrderCommentIdBv3;
import tests.shared.complete.ecommerce.order.OrderItemIdBv3;
import tests.shared.complete.ecommerce.order.OrderStatusIdBv3;
import tests.shared.complete.ecommerce.order.OrderBv3;
import tests.shared.complete.ecommerce.order.OrderIdBv3;
import tests.shared.complete.ecommerce.order.DeliveryAddressIdBv3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderRepository extends JdbcAggregateRepository<OrderBv3, OrderIdBv3> {

    private final JdbcConnectionProvider connectionProvider;
    private final JdbcDialect dialect;
    private final JdbcSchemaMetadata schemaMetadata;
    private final JdbcDomainPersistenceProvider domainPersistenceProvider;

    public OrderRepository(JdbcConnectionProvider connectionProvider,
                                 JdbcDialect dialect,
                                 JdbcSchemaMetadata schemaMetadata,
                                 JdbcDomainPersistenceProvider domainPersistenceProvider,
                                 PersistenceEventPublisher persistenceEventPublisher) {
        super(OrderBv3.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
        this.connectionProvider = connectionProvider;
        this.dialect = dialect;
        this.schemaMetadata = schemaMetadata;
        this.domainPersistenceProvider = domainPersistenceProvider;
    }

    public Optional<OrderBv3> findOrderById(OrderIdBv3 id) {
        return getFetcher().fetchDeep(id).resultValue();
    }

    public List<OrderBv3> findAllOrders() {
        return selectMany("ORDER_BV3", null)
            .stream()
            .map(r -> getFetcher().fetchDeep(r).resultValue().get())
            .collect(Collectors.toList());
    }

    public List<OrderBv3> findOrdersPaged(int offset, int pageSize) {
        var table = schemaMetadata.table("ORDER_BV3");
        var sql = "SELECT * FROM " + table.qualifiedName() + " ORDER BY ID LIMIT ? OFFSET ?";
        return selectWithSql(table, sql, pageSize, offset)
            .stream()
            .map(r -> getFetcher().fetchDeep(r).resultValue().get())
            .collect(Collectors.toList());
    }

    public List<OrderBv3> findByStatusCode(OrderStatusCodeEnumBv3 statusCode) {
        var table = schemaMetadata.table("ORDER_BV3");
        var statusTable = schemaMetadata.table("ORDER_STATUS_BV3");
        // mirrors jooq-integration's own (unconditional, not FK-linked) cross join - purely to demonstrate
        // a custom finder, not a semantically meaningful query
        var sql = "SELECT b.* FROM " + table.qualifiedName() + " b, " + statusTable.qualifiedName()
            + " s WHERE s.STATUS_CODE = ?";
        return selectWithSql(table, sql, statusCode.name())
            .stream()
            .map(r -> getFetcher().fetchDeep(r).resultValue().get())
            .collect(Collectors.toList());
    }

    public Optional<OrderBv3> findWithSubquery(OrderIdBv3 id) {
        var fetcher = new JdbcAggregateFetcher<OrderBv3, OrderIdBv3>(
            OrderBv3.class, connectionProvider, schemaMetadata, domainPersistenceProvider);

        var itemTable = schemaMetadata.table("ORDER_ITEM_BV3");
        fetcher.withRecordProvider(
            new RecordProvider<JdbcRecord, JdbcRecord>() {
                @Override
                public Collection<JdbcRecord> provideCollection(JdbcRecord parentRecord) {
                    var sql = "SELECT * FROM " + itemTable.qualifiedName()
                        + " WHERE ORDER_ID = ? AND ARTICLE_ID = ?";
                    return selectWithSql(itemTable, sql, parentRecord.get("ID"), 1L);
                }
            },
            OrderBv3.class,
            OrderItemBv3.class,
            List.of("orderItems"));
        return fetcher.fetchDeep(id).resultValue();
    }

    /**
     * Not literally query-optimized (this module has no jOOQ-style single-join batch fetch machinery to hook
     * into) - kept only functionally equivalent to jooq-integration's own method of the same name, which this
     * scenario's tests only check the result size of.
     */
    public Stream<OrderBv3> findOrdersOptimized(int offset, int pageSize) {
        return findOrdersPaged(offset, pageSize).stream();
    }

    public OrderIdBv3 newOrderId() {
        return new OrderIdBv3(nextVal("ORDER_ID_BV3_SEQ"));
    }

    public OrderItemIdBv3 newOrderItemId() {
        return new OrderItemIdBv3(nextVal("ORDER_ITEM_ID_BV3_SEQ"));
    }

    public OrderCommentIdBv3 newOrderCommentId() {
        return new OrderCommentIdBv3(nextVal("ORDER_COMMENT_ID_BV3_SEQ"));
    }

    public OrderStatusIdBv3 newOrderStatusId() {
        return new OrderStatusIdBv3(nextVal("ORDER_STATUS_ID_BV3_SEQ"));
    }

    public DeliveryAddressIdBv3 newDeliveryAddressId() {
        return new DeliveryAddressIdBv3(nextVal("DELIVERY_ADDRESS_ID_BV3_SEQ"));
    }

    private long nextVal(String sequenceName) {
        try {
            return dialect.nextSequenceValue(connectionProvider.getConnection(), sequenceName);
        } catch (SQLException e) {
            throw DLCPersistenceException.fail("Could not obtain next value of sequence '%s'.", e, sequenceName);
        }
    }

    private List<JdbcRecord> selectMany(String tableName, String whereClauseIgnored) {
        var table = schemaMetadata.table(tableName);
        return selectWithSql(table, "SELECT * FROM " + table.qualifiedName());
    }

    private List<JdbcRecord> selectWithSql(TableMetadata table, String sql, Object... params) {
        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                List<JdbcRecord> rows = new ArrayList<>();
                while (resultSet.next()) {
                    var record = new JdbcRecord(table.name());
                    for (var column : table.columns()) {
                        record.set(column.name(), resultSet.getObject(column.name(), column.javaType()));
                    }
                    rows.add(record);
                }
                return rows;
            }
        } catch (SQLException e) {
            throw DLCPersistenceException.fail("Query on '%s' failed.", e, table.name());
        }
    }
}
