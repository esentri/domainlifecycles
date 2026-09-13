package io.domainlifecycles.jooq.persistence.events;


import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.companions.Entities;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import lombok.extern.slf4j.Slf4j;
import org.jooq.UpdatableRecord;
import org.junit.jupiter.api.Test;
import tests.shared.complete.ecommerce.order.ArticleIdBv3;
import tests.shared.complete.ecommerce.order.OrderCommentBv3;
import tests.shared.complete.ecommerce.order.OrderCommentIdBv3;
import tests.shared.complete.ecommerce.order.OrderItemBv3;
import tests.shared.complete.ecommerce.order.OrderItemIdBv3;
import tests.shared.complete.ecommerce.order.OrderStatusBv3;
import tests.shared.complete.ecommerce.order.OrderStatusCodeEnumBv3;
import tests.shared.complete.ecommerce.order.OrderStatusIdBv3;
import tests.shared.complete.ecommerce.order.OrderBv3;
import tests.shared.complete.ecommerce.order.OrderIdBv3;
import tests.shared.complete.ecommerce.order.CustomerNumberBv3;
import tests.shared.complete.ecommerce.order.DeliveryAddressBv3;
import tests.shared.complete.ecommerce.order.DeliveryAddressIdBv3;
import tests.shared.complete.ecommerce.order.PriceBv3;
import tests.shared.complete.ecommerce.order.CurrencyEnumBv3;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UpdateEventChangeCalculationTest extends BasePersistence_ITest {

    private Object getOldValue(PersistenceAction<?> action, String propertyName) {
        var accessor = DlcAccess.accessorFor(action.instanceAccessModelBeforeUpdate.domainObject());
        return accessor.peek(propertyName);
    }

    private Object getNewValue(PersistenceAction<?> action, String propertyName) {
        var accessor = DlcAccess.accessorFor(action.instanceAccessModel.domainObject());
        return accessor.peek(propertyName);
    }

    private Set<String> calculateChangedProperties(PersistenceAction<?> action) {
        return Entities.detectChanges((Entity<?>) action.instanceAccessModelBeforeUpdate.domainObject(),
                (Entity<?>) action.instanceAccessModel.domainObject(), false)
            .stream()
            .map(c -> c.changedField().getName())
            .collect(Collectors.toSet());
    }

    @Test
    public void testChangeSimpleProperty() {
        OrderBv3 a = buildOrder();
        OrderBv3 b = buildOrder();
        b.setPriority(Byte.valueOf("2"));

        PersistenceAction<?> action = new PersistenceAction<>(
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(b), PersistenceAction.ActionType.UPDATE,
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(a));
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).containsExactlyInAnyOrder("priority");

        assertThat(getNewValue(action, "priority")).isEqualTo(b.getPriority());
        assertThat(getOldValue(action, "priority")).isEqualTo(a.getPriority());
    }

    @Test
    public void testChangeSimpleRefIdentity() {
        OrderBv3 a = buildOrder();
        OrderBv3 b = buildOrder();
        b.setCustomerNumber(new CustomerNumberBv3("88888"));

        PersistenceAction<?> action = new PersistenceAction<>(
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(b), PersistenceAction.ActionType.UPDATE,
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(a));
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).containsExactlyInAnyOrder("customerNumber");
        assertThat(getNewValue(action, "customerNumber")).isEqualTo(b.getCustomerNumber());
        assertThat(getOldValue(action, "customerNumber")).isEqualTo(a.getCustomerNumber());
    }

    @Test
    public void testChangeSimplePropertyPrimitive() {
        OrderBv3 a = buildOrder();
        OrderBv3 b = buildOrder();
        b.getOrderItems().get(0).setQuantity(66);
        DomainObjectInstanceAccessModel<?> bModel = persistenceConfiguration.domainPersistenceProvider.buildAccessModel(
            b);
        DomainObjectInstanceAccessModel<?> bPosModel = bModel.children.stream()
            .filter(c -> c.domainObject().equals(b.getOrderItems().get(0)))
            .findFirst()
            .get();
        DomainObjectInstanceAccessModel<?> aModel = persistenceConfiguration.domainPersistenceProvider.buildAccessModel(
            a);
        DomainObjectInstanceAccessModel<?> aPosModel = aModel.children.stream()
            .filter(c -> c.domainObject().equals(a.getOrderItems().get(0)))
            .findFirst()
            .get();
        PersistenceAction<?> action = new PersistenceAction(bPosModel, PersistenceAction.ActionType.UPDATE, aPosModel);
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).containsExactlyInAnyOrder("quantity");
        assertThat(getNewValue(action, "quantity")).isEqualTo(b.getOrderItems().get(0).getQuantity());
        assertThat(getOldValue(action, "quantity")).isEqualTo(a.getOrderItems().get(0).getQuantity());
    }

    @Test
    public void testNoChanges() {
        OrderBv3 a = buildOrder();
        OrderBv3 b = buildOrder();
        PersistenceAction<?> action = new PersistenceAction<>(
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(b), PersistenceAction.ActionType.UPDATE,
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(a));
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).isEmpty();
    }

    @Test
    public void testChangeComplexVo() {
        OrderBv3 a = buildOrder();
        OrderBv3 b = buildOrder();
        b.getOrderItems().get(0).setUnitPrice(PriceBv3
            .builder()
            .setAmount(BigDecimal.valueOf(44))
            .setCurrency(CurrencyEnumBv3.EUR)
            .build());
        DomainObjectInstanceAccessModel<UpdatableRecord<?>> bModel =
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(
                b);
        DomainObjectInstanceAccessModel<UpdatableRecord<?>> bPosModel = bModel.children.stream()
            .filter(c -> c.domainObject().equals(b.getOrderItems().get(0)))
            .findFirst()
            .get();
        DomainObjectInstanceAccessModel<UpdatableRecord<?>> aModel =
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(
                a);
        DomainObjectInstanceAccessModel<UpdatableRecord<?>> aPosModel = aModel.children.stream()
            .filter(c -> c.domainObject().equals(a.getOrderItems().get(0)))
            .findFirst()
            .get();
        PersistenceAction<UpdatableRecord<?>> action = new PersistenceAction<>(bPosModel,
            PersistenceAction.ActionType.UPDATE, aPosModel);
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).containsExactlyInAnyOrder("unitPrice");
        assertThat(getNewValue(action, "unitPrice")).isEqualTo(b.getOrderItems().get(0).getUnitPrice());
        assertThat(getOldValue(action, "unitPrice")).isEqualTo(a.getOrderItems().get(0).getUnitPrice());

    }

    @Test
    public void testChangeToNull() {
        TestRootSimple a = buildTestRootSimple();
        TestRootSimple b = buildTestRootSimple();
        b.setName(null);
        PersistenceAction<?> action = new PersistenceAction<>(
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(b), PersistenceAction.ActionType.UPDATE,
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(a));
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).containsExactlyInAnyOrder("name");
        assertThat(getNewValue(action, "name")).isEqualTo(b.getName());
        assertThat(getOldValue(action, "name")).isEqualTo(a.getName());

    }

    @Test
    public void testChangeFromNull() {
        TestRootSimple a = buildTestRootSimple();
        TestRootSimple b = buildTestRootSimple();
        a.setName(null);
        PersistenceAction<?> action = new PersistenceAction<>(
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(b), PersistenceAction.ActionType.UPDATE,
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(a));
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).containsExactlyInAnyOrder("name");
        assertThat(getNewValue(action, "name")).isEqualTo(b.getName());
        assertThat(getOldValue(action, "name")).isEqualTo(a.getName());
    }

    private OrderBv3 buildOrder() {
        OrderBv3 b = OrderBv3.builder()
            .setId(new OrderIdBv3(1l))
            .setCustomerNumber(new CustomerNumberBv3("777777"))
            .setPriority(Byte.valueOf("1"))
            .setDeliveryAddress(
                DeliveryAddressBv3.builder()
                    .setId(new DeliveryAddressIdBv3(1l))
                    .setName("Thor")
                    .setCity("Donnerberg")
                    .setPostalCode("77777")
                    .setStreet("Hammerallee 7")
                    .build()
            )
            .setOrderComments(newArrayListOf(
                OrderCommentBv3.builder()
                    .setId(new OrderCommentIdBv3(1l))
                    .setCommentedAt(LocalDateTime.of(2021, 01, 1, 12, 0))
                    .setCommentText("Mach schnell sonst kommt der Hammer!")
                    .build(),
                OrderCommentBv3.builder()
                    .setId(new OrderCommentIdBv3(2l))
                    .setCommentedAt(LocalDateTime.of(2021, 01, 2, 12, 0))
                    .setCommentText("Der Donnergott grüßt!")
                    .build()
            ))
            .setOrderStatus(
                OrderStatusBv3.builder()
                    .setStatusChangedAt(LocalDateTime.of(2021, 01, 1, 12, 1))
                    .setStatusCode(OrderStatusCodeEnumBv3.INITIAL)
                    .setId(new OrderStatusIdBv3(1l))
                    .build()
            ).setOrderItems(
                newArrayListOf(
                    OrderItemBv3.builder()
                        .setId(new OrderItemIdBv3(1l))
                        .setArticleId(new ArticleIdBv3(1l))
                        .setQuantity(100)
                        .setUnitPrice(PriceBv3.builder()
                            .setAmount(BigDecimal.ONE)
                            .setCurrency(CurrencyEnumBv3.EUR)
                            .build())
                        .build(),
                    OrderItemBv3.builder()
                        .setId(new OrderItemIdBv3(2l))
                        .setArticleId(new ArticleIdBv3(2l))
                        .setQuantity(10)
                        .setUnitPrice(PriceBv3.builder()
                            .setAmount(BigDecimal.TEN)
                            .setCurrency(CurrencyEnumBv3.EUR)
                            .build())
                        .build()
                )
            )
            .build();

        return b;
    }

    private TestRootSimple buildTestRootSimple() {
        TestRootSimple trs = TestRootSimple.builder()
            .setId(new TestRootSimpleId(1l))
            .setName("TestRoot")
            .build();
        return trs;
    }

    private List newArrayListOf(DomainObject... p) {
        return Stream.of(p).collect(Collectors.toList());
    }


}
