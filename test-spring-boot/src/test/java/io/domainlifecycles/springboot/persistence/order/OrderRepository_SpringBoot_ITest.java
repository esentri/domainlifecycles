package io.domainlifecycles.springboot.persistence.order;

import io.domainlifecycles.springboot.config.PersistenceConfig;
import io.domainlifecycles.springboot.persistence.base.SpringTestEventListener;
import org.assertj.core.api.Assertions;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
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

@SpringBootTest
@Import(PersistenceConfig.class)
@ActiveProfiles({"test"})
public class OrderRepository_SpringBoot_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OrderRepository_SpringBoot_ITest.class);

    @Autowired
    private OrderBv3Repository orderRepository;

    @Autowired
    private SpringTestEventListener springTestEventListener;


    @Transactional
    @Test
    public void testInsert() {
        //given
        OrderBv3 b = TestDataGenerator.buildOrderBv3();

        //when
        OrderBv3 inserted = orderRepository.insert(b);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultOrder(inserted, found.get());

        log.debug("Neue Bestellung: \n" + found);
    }

    @Transactional
    @Test
    public void testUpdateStatus() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());

        //when
        inserted.getOrderStatus().setStatusCode(OrderStatusCodeEnumBv3.DELIVERY_IN_PROGRESS);
        OrderBv3 updated = orderRepository.update(inserted);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultOrder(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung: \n" + found);
    }

    @Transactional
    @Test
    public void testUpdateAddPromoCodes() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());

        //when
        inserted.setPromoCodes(TestDataGenerator.newArrayListOf(
            PromoCodeBv3.builder().setValue("ABC").build(),
            PromoCodeBv3.builder().setValue("DEF").build()
        ));
        OrderBv3 updated = orderRepository.update(inserted);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultOrder(updated, found.get());

        assertThat(found.get().getPromoCodes()).isNotEmpty();
        assertThat(found.get().getPromoCodes().size()).isEqualTo(2);

        log.debug("Bestellung mit Status in Zustellung: \n" + found);
    }


    @Transactional
    @Test
    public void testUpdateStatusAndPriority() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());

        //when
        inserted.getOrderStatus().setStatusCode(OrderStatusCodeEnumBv3.DELIVERY_IN_PROGRESS);
        inserted.setPriority(Byte.valueOf("3"));
        OrderBv3 updated = orderRepository.update(inserted);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultOrder(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung und Prio 3: \n" + found);
    }

    @Transactional
    @Test
    public void testDeleteOrderItemAddComment() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());

        //when
        inserted.removeOrderItem(inserted.getOrderItems().get(0));
        inserted.getOrderComments().add(
            OrderCommentBv3.builder()
                .setCommentText("Den Scheiß will ich doch nicht!")
                .setCommentedAt(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new OrderCommentIdBv3(3l))
                .build()
        );
        OrderBv3 updated = orderRepository.update(inserted);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultOrder(updated, found.get());

        log.debug("Bestellung mit nur 1 Position: \n" + found);

    }

    @Transactional
    @Test
    public void testUpdateComplexScenarioUniqueConstraint() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());

        //when
        inserted.removeOrderItem(inserted.getOrderItems().get(0));
        inserted.getOrderComments().add(
            OrderCommentBv3.builder()
                .setCommentText("Den Scheiß will ich doch nicht!")
                .setCommentedAt(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new OrderCommentIdBv3(3l))
                .build()
        );
        inserted.addOrderItem(
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
        inserted.getOrderComments().add(
            OrderCommentBv3.builder()
                .setCommentText("Ne ich nehm's doch, aber nur die Hälfte! (Weil's jetzt billiger ist....hähähähä)")
                .setCommentedAt(LocalDateTime.of(2021, 01, 2, 12, 3))
                .setId(new OrderCommentIdBv3(4l))
                .build()
        );
        OrderBv3 updated = orderRepository.update(inserted);

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultOrder(updated, found.get());

        log.debug("Bestellung mit nur 2 Position und anderem Preis (billiger): \n" + found);
    }

    @Transactional
    @Test
    public void testUpdateComplexScenarioUniqueConstraintFail() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());

        //when
        inserted.getOrderItems().add(
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
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                OrderBv3 updated = orderRepository.update(inserted);
            }
        });
    }

    @Test
    @Transactional
    public void testDelete() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        Optional<OrderBv3> foundAfter = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        assertThat(foundAfter).isPresent();
        //when
        orderRepository.deleteById(new OrderIdBv3(1l));

        //then
        Optional<OrderBv3> found = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        assertThat(found).isEmpty();
    }

    @Test
    @Transactional
    public void testFetchById() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        //when
        Optional<OrderBv3> foundAfter = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        //then
        assertThat(foundAfter).isPresent();

        assertResultOrder(inserted, foundAfter.get());
    }

    @Test
    @Transactional
    public void testFetchByStatus() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        Optional<OrderBv3> foundAfter = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        assertThat(foundAfter).isPresent();
        //when
        List<OrderBv3> confirmedOrders = orderRepository.findByStatusCode(
            OrderStatusCodeEnumBv3.CONFIRMED);
        //then
        assertThat(confirmedOrders).isEmpty();
        //when
        List<OrderBv3> initialOrders = orderRepository.findByStatusCode(
            OrderStatusCodeEnumBv3.INITIAL);
        //then
        assertThat(initialOrders).isNotEmpty();

        assertResultOrder(initialOrders.get(0), foundAfter.get());
    }

    @Test
    @Transactional
    public void testFetchAll() {
        //given
        List<OrderBv3> orders = TestDataGenerator.buildManyOrdersBv3();
        orders.stream().forEach(b -> orderRepository.insert(b));

        //when
        List<OrderBv3> found = orderRepository.findAllOrders();

        //then
        assertThat(orders.size() == found.size());
        assertThat(orders)
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
    @Transactional
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
            assertThat(orders
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
    @Transactional
    public void testFetchCustomSubquery() {
        //given
        OrderBv3 inserted = orderRepository.insert(TestDataGenerator.buildOrderBv3());
        Optional<OrderBv3> foundAfter = orderRepository.findResultById(new OrderIdBv3(1l)).resultValue();
        assertThat(foundAfter).isPresent();
        assertResultOrder(inserted, foundAfter.get());

        assertThat(foundAfter
            .get()
            .getOrderItems().size()).isEqualTo(2);

        //when

        //Die Subquery ergänzt in den Bestellungen nur Positionen mit ArticleId 1
        //Nicht fachlich sinnvoll -> nur Demozwecke
        Optional<OrderBv3> foundSubquery = orderRepository.findWithSubquery(new OrderIdBv3(1l));

        //then
        assertThat(foundSubquery).isPresent();
        Assertions.assertThat(foundSubquery.get().getId()).isEqualTo(foundAfter.get().getId());
        assertThat(foundSubquery
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
    @Transactional
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
        assertThat(found).hasSize(3);
    }

    private void assertResultOrder(OrderBv3 result, OrderBv3 found) {
        assertThat(result)
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
