package io.domainlifecycles.jooq.topological;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import io.domainlifecycles.persistence.repository.order.TopologicalPersistenceActionOrderProvider;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import tests.shared.complete.onlinehandel.bestellung.AktionsCodeBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellKommentarBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.complete.onlinehandel.bestellung.LieferadresseBv3;
import tests.shared.persistence.domain.complex.TestEntity1;
import tests.shared.persistence.domain.complex.TestEntity2;
import tests.shared.persistence.domain.complex.TestEntity3;
import tests.shared.persistence.domain.complex.TestEntity4;
import tests.shared.persistence.domain.complex.TestEntity5;
import tests.shared.persistence.domain.complex.TestEntity6;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchical;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackref;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyA;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyB;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoin;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevel;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestEntityOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityAOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityBOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestEntityOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;
import tests.shared.persistence.domain.shared.another.AnotherConfiguration;
import tests.shared.persistence.domain.shared.another.TangibleConfigurationTableEntry;
import tests.shared.persistence.domain.shared.one.Configuration;
import tests.shared.persistence.domain.shared.one.GlobalConfigurationTableEntry;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany2;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany3;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;
import tests.shared.persistence.domain.valueobjects.VoEntity;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity2;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


public class TopologicalOrder_ITest extends BasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TopologicalOrder_ITest.class);

    @Test
    public void testOrderingTestRootComplex() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(TestRoot.class.getName());
        log.info("INSERT/UPDATE Order for: " + TestRoot.class.getName());
        order.forEach(log::info);

        assertThat(order.indexOf(TestEntity5.class.getName())).isGreaterThan(
            order.indexOf(TestEntity4.class.getName()));
        assertThat(order.indexOf(TestEntity5.class.getName())).isGreaterThan(
            order.indexOf(TestEntity6.class.getName()));
        assertThat(order.indexOf(TestEntity5.class.getName())).isGreaterThan(order.indexOf(TestRoot.class.getName()));
        assertThat(order.indexOf(TestEntity1.class.getName())).isGreaterThan(order.indexOf(TestRoot.class.getName()));
        assertThat(order.indexOf(TestEntity2.class.getName())).isGreaterThan(order.indexOf(TestRoot.class.getName()));
        assertThat(order.indexOf(TestEntity3.class.getName())).isGreaterThan(order.indexOf(TestRoot.class.getName()));
        assertThat(order.indexOf(TestEntity1.class.getName())).isGreaterThan(
            order.indexOf(TestEntity2.class.getName()));
        assertThat(order.indexOf(TestEntity3.class.getName())).isGreaterThan(
            order.indexOf(TestEntity2.class.getName()));
        assertThat(order.indexOf(TestEntity4.class.getName())).isGreaterThan(
            order.indexOf(TestEntity3.class.getName()));
    }

    @Test
    public void testOrderingBestellung() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(BestellungBv3.class.getName());
        log.info("INSERT/UPDATE Order for: " + BestellungBv3.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyInAnyOrder(
            LieferadresseBv3.class.getName(),
            BestellungBv3.class.getName(),
            BestellStatusBv3.class.getName(),
            BestellKommentarBv3.class.getName(),
            AktionsCodeBv3.class.getName(),
            BestellPositionBv3.class.getName()
        );

        assertThat(order.get(0)).isEqualTo(LieferadresseBv3.class.getName());
        assertThat(order.get(1)).isEqualTo(BestellungBv3.class.getName());
        assertThat(order.indexOf(BestellStatusBv3.class.getName())).isGreaterThan(
            order.indexOf(BestellungBv3.class.getName()));
        assertThat(order.indexOf(BestellKommentarBv3.class.getName())).isGreaterThan(
            order.indexOf(BestellungBv3.class.getName()));
        assertThat(order.indexOf(AktionsCodeBv3.class.getName())).isGreaterThan(order.indexOf(BestellungBv3.class.getName()));
        assertThat(order.indexOf(BestellPositionBv3.class.getName())).isGreaterThan(
            order.indexOf(BestellungBv3.class.getName()));
    }

    @Test
    public void testOrderingHierarchical() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(TestRootHierarchical.class.getName());
        log.info("INSERT/UPDATE Order for: " + TestRootHierarchical.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            TestRootHierarchical.class.getName()
        ));
    }

    @Test
    public void testOrderingHierarchicalBackref() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(TestRootHierarchicalBackref.class.getName());
        log.info("INSERT/UPDATE Order for: " + TestRootHierarchicalBackref.class.getName());
        order.forEach(p -> log.info(p));
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            TestRootHierarchicalBackref.class.getName()
        ));
    }

    @Test
    public void testOrderingManyToMany() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(TestRootManyToMany.class.getName());
        log.info("INSERT/UPDATE Order for: " + TestRootManyToMany.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            TestEntityManyToManyB.class.getName(),
            TestRootManyToMany.class.getName(),
            TestEntityManyToManyA.class.getName(),
            TestEntityManyToManyJoin.class.getName()
        ));
    }

    @Test
    public void testOrderingMultiLevelVo() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(VoAggregateThreeLevel.class.getName());
        log.info("INSERT/UPDATE Order for: " + VoAggregateThreeLevel.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            VoAggregateThreeLevel.class.getName()
        ));
    }

    @Test
    public void testOrderingOneToMany() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(TestRootOneToMany.class.getName());
        log.info("INSERT/UPDATE Order for: " + TestRootOneToMany.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            TestRootOneToMany.class.getName(),
            TestEntityOneToMany.class.getName()
        ));
    }

    @Test
    public void testOrderingOneToOneFollowing() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(TestRootOneToOneFollowing.class.getName());
        log.info("INSERT/UPDATE Order for: " + TestRootOneToOneFollowing.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            TestRootOneToOneFollowing.class.getName(),
            TestEntityOneToOneFollowing.class.getName()
        ));
    }

    @Test
    public void testOrderingOneToOneFollowingLeading() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(TestRootOneToOneFollowingLeading.class.getName());
        log.info("INSERT/UPDATE Order for: " + TestRootOneToOneFollowingLeading.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            TestEntityBOneToOneFollowingLeading.class.getName(),
            TestRootOneToOneFollowingLeading.class.getName(),
            TestEntityAOneToOneFollowingLeading.class.getName()
        ));
    }

    @Test
    public void testOrderingOneToOneLeading() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(TestRootOneToOneLeading.class.getName());
        log.info("INSERT/UPDATE Order for: " + TestRootOneToOneLeading.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            TestEntityOneToOneLeading.class.getName(),
            TestRootOneToOneLeading.class.getName()
        ));
    }

    @Test
    public void testOrderingSharedKernel() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(AnotherConfiguration.class.getName());
        log.info("INSERT/UPDATE Order for: " + AnotherConfiguration.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            AnotherConfiguration.class.getName(),
            TangibleConfigurationTableEntry.class.getName()
        ));

        order = orderProvider.insertionOrder(Configuration.class.getName());
        log.info("INSERT/UPDATE Order for: " + Configuration.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            Configuration.class.getName(),
            GlobalConfigurationTableEntry.class.getName()
        ));
    }

    @Test
    public void testOrderingSimple() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(TestRootSimple.class.getName());
        log.info("INSERT/UPDATE Order for: " + TestRootSimple.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            TestRootSimple.class.getName()
        ));
    }

    @Test
    public void testOrderingSimpleUUID() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(TestRootSimpleUuid.class.getName());
        log.info("INSERT/UPDATE Order for: " + TestRootSimpleUuid.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyElementsOf(Arrays.asList(
            TestRootSimpleUuid.class.getName()
        ));
    }

    @Test
    public void testOrderingValueObjects() {
        TopologicalPersistenceActionOrderProvider orderProvider = new TopologicalPersistenceActionOrderProvider(
            persistenceConfiguration.domainPersistenceProvider);
        var order = orderProvider.insertionOrder(VoAggregateRoot.class.getName());
        log.info("INSERT/UPDATE Order for: " + VoAggregateRoot.class.getName());
        order.forEach(log::info);
        assertThat(order).containsExactlyInAnyOrder(
            VoAggregateRoot.class.getName(),
            SimpleVoOneToMany2.class.getName(),
            VoEntity.class.getName(),
            SimpleVoOneToMany.class.getName(),
            SimpleVoOneToMany3.class.getName(),
            VoOneToManyEntity.class.getName(),
            VoOneToManyEntity2.class.getName()
        );

        assertThat(order.get(0)).isEqualTo(VoAggregateRoot.class.getName());
        assertThat(order.indexOf(VoAggregateRoot.class.getName())).isLessThan(order.indexOf(VoEntity.class.getName()));
        assertThat(order.indexOf(VoEntity.class.getName())).isLessThan(
            order.indexOf(VoOneToManyEntity.class.getName()));
        assertThat(order.indexOf(VoEntity.class.getName())).isLessThan(
            order.indexOf(VoOneToManyEntity2.class.getName()));
        assertThat(order.indexOf(VoOneToManyEntity.class.getName())).isLessThan(
            order.indexOf(VoOneToManyEntity2.class.getName()));
        assertThat(order.indexOf(VoAggregateRoot.class.getName())).isLessThan(
            order.indexOf(SimpleVoOneToMany2.class.getName()));
        assertThat(order.indexOf(VoAggregateRoot.class.getName())).isLessThan(
            order.indexOf(SimpleVoOneToMany3.class.getName()));
        assertThat(order.indexOf(SimpleVoOneToMany2.class.getName())).isLessThan(
            order.indexOf(SimpleVoOneToMany3.class.getName()));
    }

}
