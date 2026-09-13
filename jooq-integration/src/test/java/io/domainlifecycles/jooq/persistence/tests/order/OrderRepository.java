package io.domainlifecycles.jooq.persistence.tests.order;

import io.domainlifecycles.jooq.imp.JooqAggregateFetcher;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.test.jooq.Sequences;
import io.domainlifecycles.test.jooq.Tables;
import io.domainlifecycles.test.jooq.tables.records.PromoCodeBv3Record;
import io.domainlifecycles.test.jooq.tables.records.OrderCommentBv3Record;
import io.domainlifecycles.test.jooq.tables.records.OrderItemBv3Record;
import io.domainlifecycles.test.jooq.tables.records.OrderStatusBv3Record;
import io.domainlifecycles.test.jooq.tables.records.OrderBv3Record;
import io.domainlifecycles.test.jooq.tables.records.DeliveryAddressBv3Record;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import tests.shared.complete.ecommerce.order.PromoCodeBv3;
import tests.shared.complete.ecommerce.order.OrderCommentBv3;
import tests.shared.complete.ecommerce.order.OrderCommentIdBv3;
import tests.shared.complete.ecommerce.order.OrderItemBv3;
import tests.shared.complete.ecommerce.order.OrderItemIdBv3;
import tests.shared.complete.ecommerce.order.OrderStatusBv3;
import tests.shared.complete.ecommerce.order.OrderStatusCodeEnumBv3;
import tests.shared.complete.ecommerce.order.OrderStatusIdBv3;
import tests.shared.complete.ecommerce.order.OrderBv3;
import tests.shared.complete.ecommerce.order.OrderIdBv3;
import tests.shared.complete.ecommerce.order.DeliveryAddressBv3;
import tests.shared.complete.ecommerce.order.DeliveryAddressIdBv3;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderRepository extends JooqAggregateRepository<OrderBv3, OrderIdBv3> {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrderRepository.class);
    private final JooqDomainPersistenceProvider jooqDomainPersistenceProvider;


    public OrderRepository(DSLContext dslContext,
                                PersistenceEventPublisher persistenceEventPublisher,
                                JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            OrderBv3.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
        this.jooqDomainPersistenceProvider = jooqDomainPersistenceProvider;
    }

    public Optional<OrderBv3> findOrderById(OrderIdBv3 id) {
        return getFetcher().fetchDeep(id).resultValue();
    }

    public List<OrderBv3> findAllOrders() {
        List<OrderBv3> result = dslContext.select()
            .from(Tables.ORDER_BV3)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(Tables.ORDER_BV3)).resultValue().get()).collect(
                Collectors.toList());
        return result;
    }

    public List<OrderBv3> findOrdersPaged(int offset, int pageSize) {
        List<OrderBv3> result = dslContext.select()
            .from(Tables.ORDER_BV3)
            .orderBy(Tables.ORDER_BV3.ID)
            .offset(offset)
            .limit(pageSize)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(Tables.ORDER_BV3)).resultValue().get()).collect(
                Collectors.toList());
        return result;
    }

    public List<OrderBv3> findByStatusCode(OrderStatusCodeEnumBv3 statusCode) {
        List<OrderBv3> result = dslContext.select()
            .from(Tables.ORDER_BV3)
            .join(Tables.ORDER_STATUS_BV3)
            .on(Tables.ORDER_STATUS_BV3.STATUS_CODE.equal(statusCode.name()))
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(Tables.ORDER_BV3)).resultValue().get()).collect(
                Collectors.toList());
        return result;
    }

    //Achtung aus Sicht fachlich/inhaltlich korrekter Domänenlogik macht diese Methode keinen Sinn
    //Es geht lediglich um die Subquery Demonstration
    public Optional<OrderBv3> findWithSubquery(OrderIdBv3 id) {
        var fetcher = new JooqAggregateFetcher<OrderBv3, OrderIdBv3>(OrderBv3.class, dslContext,
            jooqDomainPersistenceProvider);

        //Wir registrieren einen RecordProvider um nur noch Bestellpositionen mit ArtikelId 1 zu fetchen
        // Per FK Auto Fetch würden normalerweise alle Positionen zu einer Bestellung gefetcht
        fetcher.withRecordProvider(
            new RecordProvider<OrderItemBv3Record, OrderBv3Record>() {
                @Override
                public Collection<OrderItemBv3Record> provideCollection(OrderBv3Record parentRecord) {
                    return dslContext.select()
                        .from(Tables.ORDER_ITEM_BV3)
                        .where(Tables.ORDER_ITEM_BV3.ORDER_ID.equal(parentRecord.getId())
                            .and(Tables.ORDER_ITEM_BV3.ARTICLE_ID.equal(1l)))
                        .fetch()
                        .into(Tables.ORDER_ITEM_BV3);
                }
            },
            OrderBv3.class,
            OrderItemBv3.class,
            List.of("orderItems"));
        return fetcher.fetchDeep(id).resultValue();
    }

    /**
     * This method ist optimized in the way, that only one select statement is issued to the database for fetching
     * all records
     * instead of the default and fetching several subselects.
     *
     * @param offset
     * @param pageSize
     */
    public Stream<OrderBv3> findOrdersOptimized(int offset, int pageSize) {
        var fetcher = new JooqAggregateFetcher<OrderBv3, OrderIdBv3>(OrderBv3.class, dslContext,
            jooqDomainPersistenceProvider);

        io.domainlifecycles.test.jooq.tables.OrderBv3 b = Tables.ORDER_BV3.as("b");

        var joinedRecords = dslContext.select()
            .from(
                dslContext.select()
                    .from(Tables.ORDER_BV3)
                    .orderBy(Tables.ORDER_BV3.ID)
                    .offset(offset)
                    .limit(pageSize)
                    .asTable("b")
            )
            .join(Tables.DELIVERY_ADDRESS_BV3)
            .on(b.DELIVERY_ADDRESS_ID.eq(Tables.DELIVERY_ADDRESS_BV3.ID))
            .join(Tables.ORDER_STATUS_BV3)
            .on(b.ID.eq(Tables.ORDER_STATUS_BV3.ORDER_ID))
            .leftJoin(Tables.ORDER_ITEM_BV3)
            .on(b.ID.eq(Tables.ORDER_ITEM_BV3.ORDER_ID))
            .leftJoin(Tables.ORDER_COMMENT_BV3)
            .on(b.ID.eq(Tables.ORDER_COMMENT_BV3.ORDER_ID))
            .leftJoin(Tables.PROMO_CODE_BV3)
            .on(Tables.PROMO_CODE_BV3.CONTAINER_ID.eq(b.ID));

        var records = dslContext.fetch(joinedRecords);

        var deliveryAddressRecords = records.into(Tables.DELIVERY_ADDRESS_BV3).stream().filter(r -> r.getId() != null).collect(
            Collectors.toSet());
        var orderRecords = records.into(b).stream().filter(r -> r.getId() != null).collect(Collectors.toSet());
        var orderItemRecords = records.into(Tables.ORDER_ITEM_BV3).stream().filter(
            r -> r.getId() != null).collect(Collectors.toSet());
        var orderCommentRecords = records.into(Tables.ORDER_COMMENT_BV3).stream().filter(
            r -> r.getId() != null).collect(Collectors.toSet());
        var promoCodeRecords = records.into(Tables.PROMO_CODE_BV3).stream().filter(r -> r.getId() != null).collect(
            Collectors.toSet());
        var statusRecords = records.into(Tables.ORDER_STATUS_BV3).stream().filter(r -> r.getId() != null).collect(
            Collectors.toSet());

        fetcher.withRecordProvider(
                new RecordProvider<OrderItemBv3Record, OrderBv3Record>() {
                    @Override
                    public Collection<OrderItemBv3Record> provideCollection(OrderBv3Record parentRecord) {
                        return orderItemRecords
                            .stream()
                            .filter(p -> p.getOrderId().equals(parentRecord.getId()))
                            .collect(Collectors.toList());
                    }
                },
                OrderBv3.class,
                OrderItemBv3.class,
                List.of("orderItems"))
            .withRecordProvider(new RecordProvider<DeliveryAddressBv3Record, OrderBv3Record>() {
                                    @Override
                                    public DeliveryAddressBv3Record provide(OrderBv3Record parentRecord) {
                                        return deliveryAddressRecords
                                            .stream()
                                            .filter(l -> l.getId().equals(parentRecord.getDeliveryAddressId()))
                                            .findFirst().orElse(null);
                                    }
                                },
                OrderBv3.class,
                DeliveryAddressBv3.class,
                List.of("deliveryAddress"))
            .withRecordProvider(new RecordProvider<OrderCommentBv3Record, OrderBv3Record>() {
                                    @Override
                                    public Collection<OrderCommentBv3Record> provideCollection(OrderBv3Record parentRecord) {
                                        return orderCommentRecords.stream()
                                            .filter(k -> k.getOrderId().equals(parentRecord.getId()))
                                            .collect(Collectors.toList());
                                    }
                                },
                OrderBv3.class,
                OrderCommentBv3.class,
                List.of("orderComments"))
            .withRecordProvider(new RecordProvider<OrderStatusBv3Record, OrderBv3Record>() {
                                    @Override
                                    public OrderStatusBv3Record provide(OrderBv3Record parentRecord) {
                                        return statusRecords
                                            .stream()
                                            .filter(s -> s.getOrderId().equals(parentRecord.getId()))
                                            .findFirst().orElse(null);
                                    }
                                },
                OrderBv3.class,
                OrderStatusBv3.class,
                List.of("orderStatus"))
            .withRecordProvider(new RecordProvider<PromoCodeBv3Record, OrderBv3Record>() {
                                    @Override
                                    public List<PromoCodeBv3Record> provideCollection(OrderBv3Record parentRecord) {
                                        return promoCodeRecords
                                            .stream()
                                            .filter(ac -> ac.getContainerId().equals(parentRecord.getId()))
                                            .collect(Collectors.toList());
                                    }
                                },
                OrderBv3.class,
                PromoCodeBv3.class,
                List.of("promoCodes"))
        ;
        var orders = orderRecords.stream().map(br -> fetcher.fetchDeep(br).resultValue().get());

        return orders;
    }

    public OrderIdBv3 newOrderId() {
        return new OrderIdBv3(dslContext.nextval(Sequences.ORDER_ID_BV3_SEQ));
    }

    public OrderItemIdBv3 newOrderItemId() {
        return new OrderItemIdBv3(dslContext.nextval(Sequences.ORDER_ITEM_ID_BV3_SEQ));
    }

    public OrderCommentIdBv3 newOrderCommentId() {
        return new OrderCommentIdBv3(dslContext.nextval(Sequences.ORDER_COMMENT_ID_BV3_SEQ));
    }

    public OrderStatusIdBv3 newOrderStatusId() {
        return new OrderStatusIdBv3(dslContext.nextval(Sequences.ORDER_STATUS_ID_BV3_SEQ));
    }

    public DeliveryAddressIdBv3 newDeliveryAddressId() {
        return new DeliveryAddressIdBv3(dslContext.nextval(Sequences.DELIVERY_ADDRESS_ID_BV3_SEQ));
    }


}
