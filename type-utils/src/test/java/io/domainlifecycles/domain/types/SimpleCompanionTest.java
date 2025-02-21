package io.domainlifecycles.domain.types;

import io.domainlifecycles.domain.types.companions.Entities;
import io.domainlifecycles.domain.types.companions.ValueObjects;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainModelFactory;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.bestellung.bv2.AktionsCode;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRoot;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoEntity;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoEntityId;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SimpleCompanionTest {

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainModelFactory("tests", "io.domainlifecycles"));
    }

    @Test
    public void testEntityEqualsFalse() {
        TestEntity e1 = new TestEntity(new TestEntityId(1L), 0);
        TestEntity e2 = new TestEntity(new TestEntityId(2L), 0);
        assertThat(e1 != e2).isTrue();
        AssertionsForClassTypes.assertThat(Entities.equals(e1, e2)).isFalse();
    }

    @Test
    public void testEntityEqualsTrue() {
        TestEntity e1 = new TestEntity(new TestEntityId(1L), 0);
        TestEntity e2 = new TestEntity(new TestEntityId(1L), 0);
        assertThat(e1 != e2).isTrue();
        assertThat(Entities.equals(e1, e2)).isTrue();
    }

    @Test
    public void testId() {
        TestEntity e1 = new TestEntity(new TestEntityId(1L), 0);

        AssertionsForClassTypes.assertThat(Entities.id(e1)).isEqualTo(new TestEntityId(1L));
    }

    @Test
    public void testEntityHashCode() {
        TestEntity e1 = new TestEntity(new TestEntityId(1L), 0);
        TestEntity e2 = new TestEntity(new TestEntityId(1L), 2);
        assertThat(Entities.hashCode(e1)).isEqualTo(Entities.hashCode(e2));
        assertThat(Entities.hashCode(e1)).isEqualTo(new TestEntityId(1L).hashCode());
    }

    @Test
    public void testEntityToString() {
        TestEntity e1 = new TestEntity(new TestEntityId(1L), 0);
        assertThat(Entities.toString(e1)).isEqualTo(
            TestEntity.class.getName() + "@" + System.identityHashCode(e1) + "(id="
                + TestEntityId.class.getName() + "@" + System.identityHashCode(e1.id) + "(value=1))");
    }

    @Test
    public void testNotEqualsValueObject() {
        AktionsCode a = AktionsCode.builder().setValue("blubb").build();
        AktionsCode a2 = AktionsCode.builder().setValue("blubb2").build();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(ValueObjects.equals(a, a2)).isFalse();
    }

    @Test
    public void testEqualsValueObject() {
        AktionsCode a = AktionsCode.builder().setValue("blubb").build();
        AktionsCode a2 = AktionsCode.builder().setValue("blubb").build();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(ValueObjects.equals(a, a2)).isTrue();
    }

    @Test
    public void testToStringValueObject() {
        AktionsCode a = AktionsCode.builder().setValue("blubb").build();
        Assertions.assertThat(ValueObjects.toString(a)).isEqualTo(
            AktionsCode.class.getName() + "@" + System.identityHashCode(a) + "(value=blubb)");
    }

    @Test
    public void testHashCodeEqualsValueObject() {
        AktionsCode a = AktionsCode.builder().setValue("blubb").build();
        AktionsCode a2 = AktionsCode.builder().setValue("blubb").build();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(ValueObjects.hashCode(a)).isEqualTo(ValueObjects.hashCode(a2));
    }


    @Test
    public void testDetectChangesNoChange() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedVoAggregateRoot a2 = TestDataGenerator.buildAutoMappedVoAggregateMax();
        var changes = Entities.detectChanges(a, a2, true);
        assertThat(changes.size()).isEqualTo(0);
    }

    @Test
    public void testDetectChangesSimple() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedVoAggregateRoot a2 = TestDataGenerator.buildAutoMappedVoAggregateMax();
        a2.setText("NEU");
        var changes = Entities.detectChanges(a, a2, true);
        assertThat(changes.size()).isEqualTo(1);
        assertThat(changes.stream().toList().get(0).changedField().getName()).isEqualTo("text");
    }

    @Test
    public void testDetectChangesAdded() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedVoAggregateRoot a2 = TestDataGenerator.buildAutoMappedVoAggregateMax();
        a2.setEntities(List.of(AutoMappedVoEntity.builder()
            .setId(new AutoMappedVoEntityId(22L))
            .setText("Added")
            .setRootId(a.getId())
            .build()));
        var changes = Entities.detectChanges(a, a2, true);
        assertThat(changes.size()).isEqualTo(1);
        var change = changes.stream().toList().get(0);
        assertThat(change.changedField().getName()).isEqualTo("entities");
        assertThat(change.changeType()).isEqualTo(Entities.DetectedChange.ChangeType.ADDED);
    }


}
