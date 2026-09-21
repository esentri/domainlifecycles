package io.domainlifecycles.jdbc.persistence.tests.order;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateFetcher;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.persistence.PhysicalNames;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.jdbc.util.JdbcRecordMapper;
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

import java.sql.SQLException;
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

    public OrderRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                                 PersistenceEventPublisher persistenceEventPublisher) {
        super(OrderBv3.class, domainPersistenceProvider, persistenceEventPublisher);
        this.connectionProvider = domainPersistenceProvider.connectionProvider;
        this.dialect = domainPersistenceProvider.dialect;
        this.schemaMetadata = domainPersistenceProvider.schemaMetadata;
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
        var table = schemaMetadata.table(PhysicalNames.name("ORDER_BV3"));
        var pagedSelect = dialect.pagedSelectSql(table, "ID", offset, pageSize);
        return selectWithSql(table, pagedSelect.sql(), pagedSelect.params())
            .stream()
            .map(r -> getFetcher().fetchDeep(r).resultValue().get())
            .collect(Collectors.toList());
    }

    public List<OrderBv3> findByStatusCode(OrderStatusCodeEnumBv3 statusCode) {
        var table = schemaMetadata.table(PhysicalNames.name("ORDER_BV3"));
        var statusTable = schemaMetadata.table(PhysicalNames.name("ORDER_STATUS_BV3"));
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
            OrderBv3.class, domainPersistenceProvider);

        var itemTable = schemaMetadata.table(PhysicalNames.name("ORDER_ITEM_BV3"));
        fetcher.withRecordProvider(
            new RecordProvider<JdbcRecord, JdbcRecord>() {
                @Override
                public Collection<JdbcRecord> provideCollection(JdbcRecord parentRecord) {
                    var sql = "SELECT * FROM " + itemTable.qualifiedName()
                        + " WHERE ORDER_ID = ? AND ARTICLE_ID = ?";
                    return selectWithSql(itemTable, sql, parentRecord.get(PhysicalNames.name("ID")), 1L);
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
        return new OrderIdBv3(nextVal(PhysicalNames.name("ORDER_ID_BV3_SEQ")));
    }

    public OrderItemIdBv3 newOrderItemId() {
        return new OrderItemIdBv3(nextVal(PhysicalNames.name("ORDER_ITEM_ID_BV3_SEQ")));
    }

    public OrderCommentIdBv3 newOrderCommentId() {
        return new OrderCommentIdBv3(nextVal(PhysicalNames.name("ORDER_COMMENT_ID_BV3_SEQ")));
    }

    public OrderStatusIdBv3 newOrderStatusId() {
        return new OrderStatusIdBv3(nextVal(PhysicalNames.name("ORDER_STATUS_ID_BV3_SEQ")));
    }

    public DeliveryAddressIdBv3 newDeliveryAddressId() {
        return new DeliveryAddressIdBv3(nextVal(PhysicalNames.name("DELIVERY_ADDRESS_ID_BV3_SEQ")));
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
        return JdbcRecordMapper.selectWithSql(connectionProvider, table, sql, params);
    }
}
