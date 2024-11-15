package io.domainlifecycles.domain.types;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.bestellung.bv2.AktionsCode;
import tests.shared.persistence.domain.bestellung.bv2.BestellStatusId;
import tests.shared.persistence.domain.bestellung.bv2.Bestellung;
import tests.shared.persistence.domain.bestellung.bv2.BestellungId;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedComplexVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRoot;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoOneToManyEntity;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoOneToManyEntity2;

import java.util.HashSet;
import java.util.Set;


public class BaseClassCompanionTest {

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "io.domainlifecycles"));
    }

    @Test
    public void testEqualsFalse() {
        TestEntity e1 = new TestEntity(new TestEntityId(1L), 0);
        TestEntity e2 = new TestEntity(new TestEntityId(2L), 0);
        Assertions.assertThat(e1).isNotEqualTo(e2);
    }

    @Test
    public void testToString() {
        TestEntity e1 = new TestEntity(new TestEntityId(1L), 0);
        Assertions.assertThat(e1.toString()).isEqualTo(
            TestEntity.class.getName() + "@" + System.identityHashCode(e1) + "(id="
                + TestEntityId.class.getName() + "@" + System.identityHashCode(e1.id) + "(value=1))");
    }

    @Test
    public void testEqualsTrue() {
        TestEntity e1 = new TestEntity(new TestEntityId(1L), 0);
        TestEntity e2 = new TestEntity(new TestEntityId(1L), 0);
        Assertions.assertThat(e1).isEqualTo(e2);
    }

    @Test
    public void testHashcode() {
        TestEntity e1 = new TestEntity(new TestEntityId(1L), 0);
        Assertions.assertThat(e1.hashCode()).isEqualTo(1);
    }


    @Test
    public void testEqualsDifferentIdentities() {
        Assertions.assertThat(new BestellStatusId(1L)).isNotEqualTo(new BestellungId(1L));
        Assertions.assertThat(new BestellungId(1L)).isNotEqualTo(new BestellungId(2L));
        Assertions.assertThat(new BestellungId(1L)).isEqualTo(new BestellungId(1L));
    }

    @Test
    public void testToStringIdentities() {
        BestellStatusId id = new BestellStatusId(1L);
        Assertions.assertThat(id.toString()).isEqualTo(
            BestellStatusId.class.getName() + "@" + System.identityHashCode(id)
                + "(value=1)");

    }

    @Test
    public void testToStringBestellung() {
        Bestellung b = TestDataGenerator.buildBestellung();
        Assertions.assertThat(b.toString()).isEqualTo(Bestellung.class.getName() + "@" + System.identityHashCode(b)
            + "(id=" + BestellungId.class.getName() + "@" + System.identityHashCode(b.getId()) + "(value=1))");
    }

    @Test
    public void testStringAktionsCode() {
        AktionsCode a = AktionsCode.builder().setValue("blubb").build();
        Assertions.assertThat(a.toString()).isEqualTo(
            AktionsCode.class.getName() + "@" + System.identityHashCode(a) + "(value=blubb)");
    }

    @Test
    public void testEqualsAutoMappedVoOneToManyEntity() {
        AutoMappedVoAggregateRoot ag = TestDataGenerator.buildAutoMappedVoAggregateMaxWithEntity();
        AutoMappedVoAggregateRoot ag2 = TestDataGenerator.buildAutoMappedVoAggregateMaxWithEntity();
        Assertions.assertThat(ag.getEntities().get(0).getValueObjectsOneToMany().toArray()[0])
            .isEqualTo(ag2.getEntities().get(0).getValueObjectsOneToMany().toArray()[0]);
        Assertions.assertThat(ag.getEntities().get(0).getValueObjectsOneToMany().toArray()[1])
            .isEqualTo(ag2.getEntities().get(0).getValueObjectsOneToMany().toArray()[1]);
        Assertions.assertThat(ag.getEntities().get(0).getValueObjectsOneToMany().toArray()[2])
            .isEqualTo(ag2.getEntities().get(0).getValueObjectsOneToMany().toArray()[2]);
        Assertions.assertThat(ag.getEntities().get(0))
            .isEqualTo(ag2.getEntities().get(0));
        Assertions.assertThat(ag.getEntities().get(1))
            .isEqualTo(ag2.getEntities().get(1));
        Assertions.assertThat(ag.getEntities().get(2))
            .isEqualTo(ag2.getEntities().get(2));
    }

    @Test
    public void testEqualsAktionsCode() {
        AktionsCode a = AktionsCode.builder().setValue("blubb").build();
        AktionsCode a2 = AktionsCode.builder().setValue("blubb").build();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(a).isEqualTo(a2);
    }

    @Test
    public void testNotEqualsAktionsCode() {
        AktionsCode a = AktionsCode.builder().setValue("blubb").build();
        AktionsCode a2 = AktionsCode.builder().setValue("blubb2").build();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(a).isNotEqualTo(a2);
    }

    @Test
    public void testHashcodeAktionsCode() {
        AktionsCode a = AktionsCode.builder().setValue("blubb").build();
        AktionsCode a2 = AktionsCode.builder().setValue("blubb").build();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(a.hashCode()).isEqualTo(a2.hashCode());
    }

    @Test
    public void testNotEqualsHashCodeAktionsCode() {
        AktionsCode a = AktionsCode.builder().setValue("blubb").build();
        AktionsCode a2 = AktionsCode.builder().setValue("blubb2").build();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(a.hashCode()).isNotEqualTo(a2.hashCode());
    }

    @Test
    public void testEqualsHashCodeAutoMappedVoAggregateRoot() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedVoAggregateRoot a2 = TestDataGenerator.buildAutoMappedVoAggregateMax();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(a.hashCode()).isEqualTo(a2.hashCode());
    }

    @Test
    public void testEqualsAutoMappedVoAggregateRoot() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedVoAggregateRoot a2 = TestDataGenerator.buildAutoMappedVoAggregateMax();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(a).isEqualTo(a2);
    }

    @Test
    public void testEqualsHashCodeAutoMappedVoAggregateRootLowerVODiff() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedVoAggregateRoot a2 = TestDataGenerator.buildAutoMappedVoAggregateMax();
        a2.getValueObjectsOneToMany().clear();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(a.hashCode()).isEqualTo(a2.hashCode());
    }

    @Test
    public void testEqualsAutoMappedVoAggregateRootLowerVODiff() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedVoAggregateRoot a2 = TestDataGenerator.buildAutoMappedVoAggregateMax();
        a2.getValueObjectsOneToMany().clear();
        Assertions.assertThat(a != a2).isTrue();
        Assertions.assertThat(a).isEqualTo(a2);
    }

    @Test
    public void testEqualsAutoMappedComplexVo() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedVoAggregateRoot a2 = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedComplexVo c1 = a.getMyComplexVo();
        AutoMappedComplexVo c2 = a2.getMyComplexVo();

        Assertions.assertThat(c1 != c2).isTrue();
        Assertions.assertThat(c1).isEqualTo(c2);
    }

    @Test
    public void testHashcodeAutoMappedComplexVo() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedVoAggregateRoot a2 = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedComplexVo c1 = a.getMyComplexVo();
        AutoMappedComplexVo c2 = a2.getMyComplexVo();

        Assertions.assertThat(c1 != c2).isTrue();
        Assertions.assertThat(c1.hashCode()).isEqualTo(c2.hashCode());
    }

    @Test
    public void testNotEqualsAutoMappedComplexVo() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedComplexVo c1 = a.getMyComplexVo();
        AutoMappedComplexVo c2 = AutoMappedComplexVo.builder().setValueA("A1NEW").build();

        Assertions.assertThat(c1 != c2).isTrue();
        Assertions.assertThat(c1).isNotEqualTo(c2);
    }

    @Test
    public void testNotEqualsHashcodeAutoMappedComplexVo() {
        AutoMappedVoAggregateRoot a = TestDataGenerator.buildAutoMappedVoAggregateMax();
        AutoMappedComplexVo c1 = a.getMyComplexVo();
        AutoMappedComplexVo c2 = AutoMappedComplexVo.builder().setValueA("A1NEW").build();

        Assertions.assertThat(c1 != c2).isTrue();
        Assertions.assertThat(c1.hashCode()).isNotEqualTo(c2.hashCode());
    }

    @Test
    public void testEqualsAutoMappedVoOneToManyEntity2() {
        AutoMappedVoOneToManyEntity vo1 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel")
                    .build()
            ))
            .build();
        AutoMappedVoOneToManyEntity vo2 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel")
                    .build()
            ))
            .build();

        Assertions.assertThat(vo1 != vo2).isTrue();
        Assertions.assertThat(vo1).isEqualTo(vo2);
    }

    @Test
    public void testEqualsHashcodeAutoMappedVoOneToManyEntity() {
        AutoMappedVoOneToManyEntity vo1 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel")
                    .build()
            ))
            .build();
        AutoMappedVoOneToManyEntity vo2 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel")
                    .build()
            ))
            .build();

        Assertions.assertThat(vo1 != vo2).isTrue();
        Assertions.assertThat(vo1.hashCode()).isEqualTo(vo2.hashCode());
    }

    @Test
    public void testNotEqualsAutoMappedVoOneToManyEntityLowerLevelDiff() {
        AutoMappedVoOneToManyEntity vo1 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel")
                    .build()
            ))
            .build();
        AutoMappedVoOneToManyEntity vo2 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel2")
                    .build()
            ))
            .build();

        Assertions.assertThat(vo1 != vo2).isTrue();
        Assertions.assertThat(vo1).isNotEqualTo(vo2);
    }

    @Test
    public void testNotEqualsHashcodeAutoMappedVoOneToManyEntityLowerLevelDiff() {
        AutoMappedVoOneToManyEntity vo1 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel")
                    .build()
            ))
            .build();
        AutoMappedVoOneToManyEntity vo2 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel2")
                    .build()
            ))
            .build();

        Assertions.assertThat(vo1 != vo2).isTrue();
        Assertions.assertThat(vo1.hashCode()).isNotEqualTo(vo2.hashCode());
    }

    @Test
    public void testNotEqualsAutoMappedVoOneToManyEntityLowerAdd() {
        AutoMappedVoOneToManyEntity vo1 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(AutoMappedVoOneToManyEntity2
                .builder()
                .setValue("VoLowerLevel")
                .build()))
            .build();
        AutoMappedVoOneToManyEntity vo2 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel")
                    .build(),
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel2")
                    .build()
            ))
            .build();

        Assertions.assertThat(vo1 != vo2).isTrue();
        Assertions.assertThat(vo1).isNotEqualTo(vo2);
    }

    @Test
    public void testNotEqualsHashcodeAutoMappedVoOneToManyEntityLowerAdd() {
        AutoMappedVoOneToManyEntity vo1 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(AutoMappedVoOneToManyEntity2
                .builder()
                .setValue("VoLowerLevel")
                .build()))
            .build();
        AutoMappedVoOneToManyEntity vo2 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel")
                    .build(),
                AutoMappedVoOneToManyEntity2
                    .builder()
                    .setValue("VoLowerLevel2")
                    .build()
            ))
            .build();

        Assertions.assertThat(vo1 != vo2).isTrue();
        Assertions.assertThat(vo1.hashCode()).isNotEqualTo(vo2.hashCode());
    }

    @Test
    public void testNotEqualsAutoMappedVoOneToManyEntityLowerLess() {
        AutoMappedVoOneToManyEntity vo1 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(AutoMappedVoOneToManyEntity2
                .builder()
                .setValue("VoLowerLevel")
                .build()))
            .build();
        AutoMappedVoOneToManyEntity vo2 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(new HashSet<>())
            .build();

        Assertions.assertThat(vo1 != vo2).isTrue();
        Assertions.assertThat(vo1).isNotEqualTo(vo2);
    }

    @Test
    public void testNotEqualsHashcodeAutoMappedVoOneToManyEntityLowerLess() {
        AutoMappedVoOneToManyEntity vo1 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(AutoMappedVoOneToManyEntity2
                .builder()
                .setValue("VoLowerLevel")
                .build()))
            .build();
        AutoMappedVoOneToManyEntity vo2 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(new HashSet<>())
            .build();

        Assertions.assertThat(vo1 != vo2).isTrue();
        Assertions.assertThat(vo1.hashCode()).isNotEqualTo(vo2.hashCode());
    }

    @Test
    public void testNotEqualsAutoMappedVoOneToManyEntityLowerNull() {
        AutoMappedVoOneToManyEntity vo1 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(AutoMappedVoOneToManyEntity2
                .builder()
                .setValue("VoLowerLevel")
                .build()))
            .build();
        AutoMappedVoOneToManyEntity vo2 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .build();

        Assertions.assertThat(vo1 != vo2).isTrue();
        Assertions.assertThat(vo1).isNotEqualTo(vo2);
    }

    @Test
    public void testNotEqualsHashcodeAutoMappedVoOneToManyEntityLowerNull() {
        AutoMappedVoOneToManyEntity vo1 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(
                Set.of(
                    AutoMappedVoOneToManyEntity2
                        .builder()
                        .setValue("VoLowerLevel")
                        .build()))
            .build();
        AutoMappedVoOneToManyEntity vo2 = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .build();

        Assertions.assertThat(vo1 != vo2).isTrue();
        Assertions.assertThat(vo1.hashCode()).isNotEqualTo(vo2.hashCode());
    }

    @Test
    public void testToStringAutoMappedVoOneToManyEntityLowerAdd() {
        AutoMappedVoOneToManyEntity2 lowerVo1 = AutoMappedVoOneToManyEntity2
            .builder()
            .setValue("VoLowerLevel")
            .build();

        AutoMappedVoOneToManyEntity2 lowerVo2 = AutoMappedVoOneToManyEntity2
            .builder()
            .setValue("VoLowerLevel2")
            .build();

        AutoMappedVoOneToManyEntity vo = AutoMappedVoOneToManyEntity
            .builder()
            .setValue("Value")
            .setOneToManySet(Set.of(
                lowerVo1,
                lowerVo2
            ))
            .build();

        //because set cannot guarantee order ;-)
        Assertions.assertThat(vo.toString()).isIn(AutoMappedVoOneToManyEntity.class.getName() + "@"
                + System.identityHashCode(
                vo) + "(value=Value, oneToManySet=[" + AutoMappedVoOneToManyEntity2.class.getName() + "@"
                + System.identityHashCode(
                lowerVo1) + "(value=VoLowerLevel), " + AutoMappedVoOneToManyEntity2.class.getName() + "@"
                + System.identityHashCode(lowerVo2) + "(value=VoLowerLevel2)])",
            AutoMappedVoOneToManyEntity.class.getName() + "@"
                + System.identityHashCode(
                vo) + "(value=Value, oneToManySet=[" + AutoMappedVoOneToManyEntity2.class.getName() + "@"
                + System.identityHashCode(
                lowerVo2) + "(value=VoLowerLevel2), " + AutoMappedVoOneToManyEntity2.class.getName() + "@"
                + System.identityHashCode(lowerVo1) + "(value=VoLowerLevel)])"
        );
    }

}
