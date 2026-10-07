package io.domainlifecycles.jooq.persistence.tests.order;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.complete.ecommerce.order.PromoCodeBv3;
import tests.shared.complete.ecommerce.order.ArticleIdBv3;
import tests.shared.complete.ecommerce.order.OrderCommentBv3;
import tests.shared.complete.ecommerce.order.OrderCommentIdBv3;
import tests.shared.complete.ecommerce.order.OrderItemBv3;
import tests.shared.complete.ecommerce.order.OrderItemIdBv3;
import tests.shared.complete.ecommerce.order.OrderStatusCodeEnumBv3;
import tests.shared.complete.ecommerce.order.OrderBv3;
import tests.shared.complete.ecommerce.order.OrderIdBv3;
import tests.shared.complete.ecommerce.order.PriceBv3;
import tests.shared.complete.ecommerce.order.CurrencyEnumBv3;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrderRepository_ITest.class);

    private static OrderRepository orderRepository;

    @BeforeAll
    public void init() {

        orderRepository = new OrderRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider);
    }

    @Test
    public void testInsert() {
        //given
        OrderBv3 b = TestDataGenerator.buildOrderBv3();
        OrderBv3 copy = persistenceEventTestHelper.kryo.copy(b);

        //when
        OrderBv3 inserted = orderRepository.insert(copy);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertResultOrder(inserted, found.get());

        log.debug("Neue Bestellung: \n" + found);
    }

    @Test
    public void testUpdateStatus() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        OrderBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.getOrderStatus().setStatusCode(OrderStatusCodeEnumBv3.DELIVERY_IN_PROGRESS);
        OrderBv3 updated = orderRepository.update(insertedCopy);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertResultOrder(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung: \n" + found);
    }

    @Test
    public void testUpdateAddPromoCodes() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        OrderBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.setPromoCodes(TestDataGenerator.newArrayListOf(
            PromoCodeBv3.builder().setValue("ABC").build(),
            PromoCodeBv3.builder().setValue("DEF").build()
        ));
        OrderBv3 updated = orderRepository.update(insertedCopy);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultOrder(updated, found.get());

        Assertions.assertThat(found.get().getPromoCodes()).isNotEmpty();
        Assertions.assertThat(found.get().getPromoCodes().size()).isEqualTo(2);

        log.debug("Bestellung mit Status in Zustellung: \n" + found);
    }

    @Test
    public void testUpdateStatusUndPrio() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        OrderBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.getOrderStatus().setStatusCode(OrderStatusCodeEnumBv3.DELIVERY_IN_PROGRESS);
        insertedCopy.setPriority(Byte.valueOf("3"));
        OrderBv3 updated = orderRepository.update(insertedCopy);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultOrder(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung und Prio 3: \n" + found);
    }

    @Test
    public void testDeleteOrderItemAddComment() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        OrderBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.removeOrderItem(insertedCopy.getOrderItems().get(0));
        insertedCopy.getOrderComments().add(
            OrderCommentBv3.builder()
                .setCommentText("Den Scheiß will ich doch nicht!")
                .setCommentedAt(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new OrderCommentIdBv3(3l))
                .build()
        );
        OrderBv3 updated = orderRepository.update(insertedCopy);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultOrder(updated, found.get());

        log.debug("Bestellung mit nur 1 Position: \n" + found);

    }

    @Test
    public void testUpdateComplexScenarioUniqueConstraint() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        OrderBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.removeOrderItem(insertedCopy.getOrderItems().get(0));
        insertedCopy.getOrderComments().add(
            OrderCommentBv3.builder()
                .setCommentText("Den Scheiß will ich doch nicht!")
                .setCommentedAt(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new OrderCommentIdBv3(3l))
                .build()
        );
        insertedCopy.addOrderItem(
            OrderItemBv3.builder()
                .setId(new OrderItemIdBv3(3l))
                .setUnitPrice(PriceBv3.builder()
                    .setAmount(BigDecimal.ONE)
                    .setCurrency(CurrencyEnumBv3.EUR)
                    .build())
                .setQuantity(50)
                .setArticleId(new ArticleIdBv3(1l))
                .build()
        );
        insertedCopy.getOrderComments().add(
            OrderCommentBv3.builder()
                .setCommentText("Ne ich nehm's doch, aber nur die Hälfte! (Weil's jetzt billiger ist....hähähähä)")
                .setCommentedAt(LocalDateTime.of(2021, 01, 2, 12, 3))
                .setId(new OrderCommentIdBv3(4l))
                .build()
        );
        OrderBv3 updated = orderRepository.update(insertedCopy);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultOrder(updated, found.get());

        log.debug("Bestellung mit nur 2 Position und anderem Preis (billiger): \n" + found);
    }

    @Test
    public void testUpdateComplexScenarioUniqueConstraintFail() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        OrderBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.getOrderItems().add(
            OrderItemBv3.builder()
                .setId(new OrderItemIdBv3(3l))
                .setUnitPrice(PriceBv3.builder()
                    .setAmount(BigDecimal.ONE)
                    .setCurrency(CurrencyEnumBv3.EUR)
                    .build())
                .setQuantity(50)
                .setArticleId(new ArticleIdBv3(1l))
                .build()
        );

        //when
        //we expect a unique constraint exception
        //ATTENTION: Do not write the assertion with a lambda expression, that will create a class loading conflict
        // with our
        // byte buddy extension
        DataAccessException ex = assertThrows(DataAccessException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                OrderBv3 updated = orderRepository.update(insertedCopy);
            }
        });
    }

    @Test
    public void testDelete() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        Optional<OrderBv3> foundAfter = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        Assertions.assertThat(foundAfter).isPresent();
        //when
        orderRepository.deleteById(new OrderIdBv3(1l));

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void testFetchById() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        //when
        Optional<OrderBv3> foundAfter = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        //then
        Assertions.assertThat(foundAfter).isPresent();

        assertResultOrder(inserted, foundAfter.get());
    }

    @Test
    public void testFetchByStatus() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        Optional<OrderBv3> foundAfter = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        Assertions.assertThat(foundAfter).isPresent();
        //when
        List<OrderBv3> confirmedOrders = orderRepository.findByStatusCode(
            OrderStatusCodeEnumBv3.CONFIRMED);
        //then
        Assertions.assertThat(confirmedOrders).isEmpty();
        //when
        List<OrderBv3> initialOrders = orderRepository.findByStatusCode(OrderStatusCodeEnumBv3.INITIAL);
        //then
        Assertions.assertThat(initialOrders).isNotEmpty();

        assertResultOrder(initialOrders.get(0), foundAfter.get());
    }

    @Test
    public void testFetchAll() {
        //given
        List<OrderBv3> orders = TestDataGenerator.buildManyOrdersBv3();
        orders.stream().forEach(b -> orderRepository.insert(b));

        //when
        List<OrderBv3> found = orderRepository.findAllOrders();

        //then
        assertThat(orders.size() == found.size());
        Assertions.assertThat(orders)
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .ignoringFieldsOfTypes(UUID.class)
            .withStrictTypeChecking()
            .withComparatorForType(new Comparator<BigDecimal>() {
                @Override
                public int compare(BigDecimal o1, BigDecimal o2) {
                    return Double.compare(o1.doubleValue(), o2.doubleValue());
                }
            }, BigDecimal.class)
            .isEqualTo(found);

    }

    @Test
    public void testFetchPaged() {
        //given
        List<OrderBv3> orders = TestDataGenerator.buildManyOrdersBv3();
        orders.stream().forEach(b -> orderRepository.insert(b));


        int pageSize = 3;
        int currentOffset = 0;
        while (currentOffset < orders.size()) {
            int offsetInLoop = currentOffset;
            //when
            List<OrderBv3> found = orderRepository.findOrdersPaged(currentOffset, pageSize);

            //then
            assertThat(found.size() <= pageSize);
            Assertions.assertThat(orders
                    .stream()
                    .filter(b -> orders.indexOf(b) >= offsetInLoop
                        && orders.indexOf(b) < (offsetInLoop + pageSize))
                    .collect(Collectors.toList()))
                .usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .ignoringCollectionOrder()
                .ignoringFieldsOfTypes(UUID.class)
                .withStrictTypeChecking()
                .withComparatorForType(new Comparator<BigDecimal>() {
                    @Override
                    public int compare(BigDecimal o1, BigDecimal o2) {
                        return Double.compare(o1.doubleValue(), o2.doubleValue());
                    }
                }, BigDecimal.class)
                .isEqualTo(found);
            currentOffset += pageSize;
        }
        assertThat(currentOffset == 12);

    }

    @Test
    public void testFetchCustomSubquery() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        Optional<OrderBv3> foundAfter = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        Assertions.assertThat(foundAfter).isPresent();
        assertResultOrder(inserted, foundAfter.get());

        Assertions.assertThat(foundAfter
            .get()
            .getOrderItems().size()).isEqualTo(2);

        //when

        //Die Subquery ergänzt in den Bestellungen nur Positionen mit ArtikelId 1
        //Nicht fachlich sinnvoll -> nur Demozwecke
        Optional<OrderBv3> foundSubquery = orderRepository.findWithSubquery(new OrderIdBv3(1l));

        //then
        Assertions.assertThat(foundSubquery).isPresent();
        Assertions.assertThat(foundSubquery.get().getId()).isEqualTo(foundAfter.get().getId());
        Assertions.assertThat(foundSubquery
            .get()
            .getOrderItems()
            .size()
        ).isEqualTo(1);
        Assertions.assertThat(foundSubquery
            .get()
            .getOrderItems()
            .get(0)
            .getArticleId()
            .value()
        ).isEqualTo(1);
    }

    @Test
    public void testFetchOptimized() {
        //given
        List<OrderBv3> orders = TestDataGenerator.buildManyOrdersBv3();
        orders.stream().forEach(b -> orderRepository.insert(b));

        int pageSize = 3;
        int currentOffset = 0;

        //when
        List<OrderBv3> found = orderRepository
            .findOrdersOptimized(currentOffset, pageSize)
            .collect(Collectors.toList());

        //then
        Assertions.assertThat(found).hasSize(3);
    }

    private void assertResultOrder(OrderBv3 result, OrderBv3 found) {
        Assertions.assertThat(result)
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()

            .ignoringCollectionOrder()
            .withStrictTypeChecking()
            .withComparatorForType(new Comparator<BigDecimal>() {
                @Override
                public int compare(BigDecimal o1, BigDecimal o2) {
                    return Double.compare(o1.doubleValue(), o2.doubleValue());
                }
            }, BigDecimal.class)
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(found);
    }

}
