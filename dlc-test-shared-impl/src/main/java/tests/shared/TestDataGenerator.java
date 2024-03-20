/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tests.shared;

import tests.shared.complete.onlinehandel.bestellung.AktionsCodeBv3;
import tests.shared.complete.onlinehandel.bestellung.ArtikelIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellKommentarBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellKommentarIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusCodeEnumBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungIdBv3;
import tests.shared.complete.onlinehandel.bestellung.KundennummerBv3;
import tests.shared.complete.onlinehandel.bestellung.LieferadresseBv3;
import tests.shared.complete.onlinehandel.bestellung.LieferadresseIdBv3;
import tests.shared.complete.onlinehandel.bestellung.PreisBv3;
import tests.shared.complete.onlinehandel.bestellung.WaehrungEnumBv3;
import tests.shared.persistence.domain.bestellung.bv2.AktionsCode;
import tests.shared.persistence.domain.bestellung.bv2.ArtikelId;
import tests.shared.persistence.domain.bestellung.bv2.BestellKommentar;
import tests.shared.persistence.domain.bestellung.bv2.BestellKommentarId;
import tests.shared.persistence.domain.bestellung.bv2.BestellPosition;
import tests.shared.persistence.domain.bestellung.bv2.BestellPositionId;
import tests.shared.persistence.domain.bestellung.bv2.BestellStatus;
import tests.shared.persistence.domain.bestellung.bv2.BestellStatusCodeEnum;
import tests.shared.persistence.domain.bestellung.bv2.BestellStatusId;
import tests.shared.persistence.domain.bestellung.bv2.Bestellung;
import tests.shared.persistence.domain.bestellung.bv2.BestellungId;
import tests.shared.persistence.domain.bestellung.bv2.Kundennummer;
import tests.shared.persistence.domain.bestellung.bv2.Lieferadresse;
import tests.shared.persistence.domain.bestellung.bv2.LieferadresseId;
import tests.shared.persistence.domain.bestellung.bv2.Preis;
import tests.shared.persistence.domain.bestellung.bv2.WaehrungEnum;
import tests.shared.persistence.domain.complex.TestEntity1;
import tests.shared.persistence.domain.complex.TestEntity1Id;
import tests.shared.persistence.domain.complex.TestEntity2;
import tests.shared.persistence.domain.complex.TestEntity2Id;
import tests.shared.persistence.domain.complex.TestEntity3;
import tests.shared.persistence.domain.complex.TestEntity3Id;
import tests.shared.persistence.domain.complex.TestEntity4;
import tests.shared.persistence.domain.complex.TestEntity4Id;
import tests.shared.persistence.domain.complex.TestEntity5;
import tests.shared.persistence.domain.complex.TestEntity5Id;
import tests.shared.persistence.domain.complex.TestEntity6;
import tests.shared.persistence.domain.complex.TestEntity6Id;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchical;
import tests.shared.persistence.domain.hierarchical.TestRootHierarchicalId;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackref;
import tests.shared.persistence.domain.hierarchicalBackRef.TestRootHierarchicalBackrefId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyA;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyAId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyB;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyBId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoin;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestEntityManyToManyJoinId;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToMany;
import tests.shared.persistence.domain.manyToManyWithJoinEntity.TestRootManyToManyId;
import tests.shared.persistence.domain.multilevelvo.ThreeLevelVo;
import tests.shared.persistence.domain.multilevelvo.ThreeLevelVoLevelThree;
import tests.shared.persistence.domain.multilevelvo.ThreeLevelVoLevelTwo;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevel;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevelId;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToMany;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToManyId;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestEntityOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestEntityOneToOneFollowingId;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowing;
import tests.shared.persistence.domain.oneToOneFollowingFK.TestRootOneToOneFollowingId;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityAOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityAOneToOneFollowingLeadingId;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityBOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestEntityBOneToOneFollowingLeadingId;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeading;
import tests.shared.persistence.domain.oneToOneFollowingLeadingFK.TestRootOneToOneFollowingLeadingId;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestEntityOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestEntityOneToOneLeadingId;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeading;
import tests.shared.persistence.domain.oneToOneLeadingFK.TestRootOneToOneLeadingId;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.TestRootOneToOneVoDedicated;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.TestRootOneToOneVoDedicatedId;
import tests.shared.persistence.domain.oneToOneVoDedicatedTable.VoDedicated;
import tests.shared.persistence.domain.optional.MyComplexValueObject;
import tests.shared.persistence.domain.optional.MySimpleValueObject;
import tests.shared.persistence.domain.optional.OptionalAggregate;
import tests.shared.persistence.domain.optional.OptionalAggregateId;
import tests.shared.persistence.domain.optional.OptionalEntity;
import tests.shared.persistence.domain.optional.OptionalEntityId;
import tests.shared.persistence.domain.optional.RefAgg;
import tests.shared.persistence.domain.optional.RefAggId;
import tests.shared.persistence.domain.optional.RefValueObject;
import tests.shared.persistence.domain.records.RecordTest;
import tests.shared.persistence.domain.records.RecordTestId;
import tests.shared.persistence.domain.records.RecordVo;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;
import tests.shared.persistence.domain.temporal.TestRootTemporal;
import tests.shared.persistence.domain.temporal.TestRootTemporalId;
import tests.shared.persistence.domain.tree.TreeNode;
import tests.shared.persistence.domain.tree.TreeNodeId;
import tests.shared.persistence.domain.tree.TreeRoot;
import tests.shared.persistence.domain.tree.TreeRootId;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedComplexVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVoOneToMany;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVoOneToMany2;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVoOneToMany3;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRoot;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRootId;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoEntity;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoEntityId;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoIdentityRef;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoOneToManyEntity;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoOneToManyEntity2;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoAggregateTwoLevel;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoAggregateTwoLevelId;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoLevelOne;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoLevelTwoA;
import tests.shared.persistence.domain.valueobjectTwoLevel.VoLevelTwoB;
import tests.shared.persistence.domain.valueobjects.ComplexVo;
import tests.shared.persistence.domain.valueobjects.SimpleVo;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany2;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany3;
import tests.shared.persistence.domain.valueobjects.VoAggregateRoot;
import tests.shared.persistence.domain.valueobjects.VoAggregateRootId;
import tests.shared.persistence.domain.valueobjects.VoEntity;
import tests.shared.persistence.domain.valueobjects.VoEntityId;
import tests.shared.persistence.domain.valueobjects.VoIdentityRef;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity;
import tests.shared.persistence.domain.valueobjects.VoOneToManyEntity2;
import tests.shared.persistence.domain.valueobjectsPrimitive.ComplexVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.NestedVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.SimpleVoPrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitive;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitiveId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataGenerator {

    public static Bestellung buildBestellung() {

        return Bestellung.builder()
            .setId(new BestellungId(1L))
            .setKundennummer(new Kundennummer("777777"))
            .setPrioritaet(Byte.valueOf("1"))
            .setLieferadresse(
                Lieferadresse.builder()
                    .setId(new LieferadresseId(1L))
                    .setName("Thor")
                    .setOrt("Donnerberg")
                    .setPostleitzahl("77777")
                    .setStrasse("Hammerallee 7")
                    .build()
            )
            .setBestellKommentare(newArrayListOf(
                BestellKommentar.builder()
                    .setId(new BestellKommentarId(1L))
                    .setKommentarAm(LocalDateTime.of(2021, 1, 1, 12, 0))
                    .setKommentarText("Mach schnell sonst kommt der Hammer!")
                    .build(),
                BestellKommentar.builder()
                    .setId(new BestellKommentarId(2L))
                    .setKommentarAm(LocalDateTime.of(2021, 1, 2, 12, 0))
                    .setKommentarText("Der Donnergott grüßt!")
                    .build()
            ))
            .setBestellStatus(
                BestellStatus.builder()
                    .setStatusAenderungAm(LocalDateTime.of(2021, 1, 1, 12, 1))
                    .setStatusCode(BestellStatusCodeEnum.INITIAL)
                    .setId(new BestellStatusId(1L))
                    .build()
            ).setBestellPositionen(
                newArrayListOf(
                    BestellPosition.builder()
                        .setId(new BestellPositionId(1L))
                        .setArtikelId(new ArtikelId(1L))
                        .setStueckzahl(100)
                        .setStueckPreis(Preis.builder()
                            .setBetrag(BigDecimal.ONE)
                            .setWaehrung(WaehrungEnum.EUR)
                            .build())
                        .build(),
                    BestellPosition.builder()
                        .setId(new BestellPositionId(2L))
                        .setArtikelId(new ArtikelId(2L))
                        .setStueckzahl(10)
                        .setStueckPreis(Preis.builder()
                            .setBetrag(BigDecimal.TEN)
                            .setWaehrung(WaehrungEnum.EUR)
                            .build())
                        .build()
                )
            )
            .build();
    }

    public static BestellungBv3 buildBestellungBv3() {

        return BestellungBv3.builder()
            .setId(new BestellungIdBv3(1L))
            .setKundennummer(new KundennummerBv3("777777"))
            .setPrioritaet(Byte.valueOf("1"))
            .setLieferadresse(
                LieferadresseBv3.builder()
                    .setId(new LieferadresseIdBv3(1L))
                    .setName("Thor")
                    .setOrt("Donnerberg")
                    .setPostleitzahl("77777")
                    .setStrasse("Hammerallee 7")
                    .build()
            )
            .setBestellKommentare(newArrayListOf(
                BestellKommentarBv3.builder()
                    .setId(new BestellKommentarIdBv3(1L))
                    .setKommentarAm(LocalDateTime.of(2021, 1, 1, 12, 0))
                    .setKommentarText("Mach schnell sonst kommt der Hammer!")
                    .build(),
                BestellKommentarBv3.builder()
                    .setId(new BestellKommentarIdBv3(2L))
                    .setKommentarAm(LocalDateTime.of(2021, 1, 2, 12, 0))
                    .setKommentarText("Der Donnergott grüßt!")
                    .build()
            ))
            .setBestellStatus(
                BestellStatusBv3.builder()
                    .setStatusAenderungAm(LocalDateTime.of(2021, 1, 1, 12, 1))
                    .setStatusCode(BestellStatusCodeEnumBv3.INITIAL)
                    .setId(new BestellStatusIdBv3(1L))
                    .build()
            ).setBestellPositionen(
                newArrayListOf(
                    BestellPositionBv3.builder()
                        .setId(new BestellPositionIdBv3(1L))
                        .setArtikelId(new ArtikelIdBv3(1L))
                        .setStueckzahl(100)
                        .setStueckPreis(PreisBv3.builder()
                            .setBetrag(BigDecimal.ONE)
                            .setWaehrung(WaehrungEnumBv3.EUR)
                            .build())
                        .build(),
                    BestellPositionBv3.builder()
                        .setId(new BestellPositionIdBv3(2L))
                        .setArtikelId(new ArtikelIdBv3(2L))
                        .setStueckzahl(10)
                        .setStueckPreis(PreisBv3.builder()
                            .setBetrag(BigDecimal.TEN)
                            .setWaehrung(WaehrungEnumBv3.EUR)
                            .build())
                        .build()
                )
            )
            .build();
    }

    public static List<Bestellung> buildManyBestellungen() {
        List<Bestellung> bestellungen = new ArrayList<>();
        for (long i = 1; i < 11; i++) {
            Bestellung b = Bestellung.builder()
                .setId(new BestellungId(i))
                .setKundennummer(new Kundennummer("" + 777777 + i))
                .setPrioritaet(Byte.valueOf("1"))
                .setLieferadresse(
                    Lieferadresse.builder()
                        .setId(new LieferadresseId(i))
                        .setName("Thor" + i)
                        .setOrt("Donnerberg")
                        .setPostleitzahl("77777")
                        .setStrasse("Hammerallee 7")
                        .build()
                )
                .setBestellKommentare(newArrayListOf(
                    BestellKommentar.builder()
                        .setId(new BestellKommentarId(i))
                        .setKommentarAm(LocalDateTime.of(2021, 1, 1, 12, 0))
                        .setKommentarText("Mach schnell sonst kommt der Hammer!")
                        .build(),
                    BestellKommentar.builder()
                        .setId(new BestellKommentarId(i + 10))
                        .setKommentarAm(LocalDateTime.of(2021, 1, 2, 12, 0))
                        .setKommentarText("Der Donnergott grüßt!")
                        .build()
                ))
                .setBestellStatus(
                    BestellStatus.builder()
                        .setStatusAenderungAm(LocalDateTime.of(2021, 1, 1, 12, 1))
                        .setStatusCode(BestellStatusCodeEnum.INITIAL)
                        .setId(new BestellStatusId(i))
                        .build()
                ).setBestellPositionen(
                    newArrayListOf(
                        BestellPosition.builder()
                            .setId(new BestellPositionId(i))
                            .setArtikelId(new ArtikelId(1L))
                            .setStueckzahl(100)
                            .setStueckPreis(Preis.builder()
                                .setBetrag(BigDecimal.ONE)
                                .setWaehrung(WaehrungEnum.EUR)
                                .build())
                            .build(),
                        BestellPosition.builder()
                            .setId(new BestellPositionId(i + 10))
                            .setArtikelId(new ArtikelId(2L))
                            .setStueckzahl(10)
                            .setStueckPreis(Preis.builder()
                                .setBetrag(BigDecimal.TEN)
                                .setWaehrung(WaehrungEnum.EUR)
                                .build())
                            .build()
                    )
                )
                .setAktionsCodes(newArrayListOf(
                    AktionsCode.builder().setValue("Code1").build(),
                    AktionsCode.builder().setValue("Code2").build()
                ))
                .build();
            bestellungen.add(b);
        }
        return bestellungen;
    }

    public static List<BestellungBv3> buildManyBestellungenBv3() {
        List<BestellungBv3> bestellungen = new ArrayList<>();
        for (long i = 1; i < 11; i++) {
            BestellungBv3 b = BestellungBv3.builder()
                .setId(new BestellungIdBv3(i))
                .setKundennummer(new KundennummerBv3("" + 777777 + i))
                .setPrioritaet(Byte.valueOf("1"))
                .setLieferadresse(
                    LieferadresseBv3.builder()
                        .setId(new LieferadresseIdBv3(i))
                        .setName("Thor" + i)
                        .setOrt("Donnerberg")
                        .setPostleitzahl("77777")
                        .setStrasse("Hammerallee 7")
                        .build()
                )
                .setBestellKommentare(newArrayListOf(
                    BestellKommentarBv3.builder()
                        .setId(new BestellKommentarIdBv3(i))
                        .setKommentarAm(LocalDateTime.of(2021, 1, 1, 12, 0))
                        .setKommentarText("Mach schnell sonst kommt der Hammer!")
                        .build(),
                    BestellKommentarBv3.builder()
                        .setId(new BestellKommentarIdBv3(i + 10))
                        .setKommentarAm(LocalDateTime.of(2021, 1, 2, 12, 0))
                        .setKommentarText("Der Donnergott grüßt!")
                        .build()
                ))
                .setBestellStatus(
                    BestellStatusBv3.builder()
                        .setStatusAenderungAm(LocalDateTime.of(2021, 1, 1, 12, 1))
                        .setStatusCode(BestellStatusCodeEnumBv3.INITIAL)
                        .setId(new BestellStatusIdBv3(i))
                        .build()
                ).setBestellPositionen(
                    newArrayListOf(
                        BestellPositionBv3.builder()
                            .setId(new BestellPositionIdBv3(i))
                            .setArtikelId(new ArtikelIdBv3(1L))
                            .setStueckzahl(100)
                            .setStueckPreis(PreisBv3.builder()
                                .setBetrag(BigDecimal.ONE)
                                .setWaehrung(WaehrungEnumBv3.EUR)
                                .build())
                            .build(),
                        BestellPositionBv3.builder()
                            .setId(new BestellPositionIdBv3(i + 10))
                            .setArtikelId(new ArtikelIdBv3(2L))
                            .setStueckzahl(10)
                            .setStueckPreis(PreisBv3.builder()
                                .setBetrag(BigDecimal.TEN)
                                .setWaehrung(WaehrungEnumBv3.EUR)
                                .build())
                            .build()
                    )
                )
                .setAktionsCodes(newArrayListOf(
                    AktionsCodeBv3.builder().setValue("Code1").build(),
                    AktionsCodeBv3.builder().setValue("Code2").build()
                ))
                .build();
            bestellungen.add(b);
        }
        return bestellungen;
    }

    public static TestRoot buildTestRootComplex() {
        TestEntity5 testEntity5_1 = TestEntity5.builder()
            .setId(new TestEntity5Id(11L))
            .setTestEntity4Id(new TestEntity4Id(9L))
            .setTestEntity6(TestEntity6.builder()
                .setId(new TestEntity6Id(1L))
                .setName("Test6_1")
                .build())
            .setTestRootId(new TestRootId(1L))
            .setName("Test5_1")
            .build();

        TestEntity5 testEntity5_2 = TestEntity5.builder()
            .setId(new TestEntity5Id(12L))
            .setTestEntity4Id(new TestEntity4Id(9L))
            .setTestEntity6(TestEntity6.builder()
                .setId(new TestEntity6Id(2L))
                .setName("Test6_2")
                .build())
            .setTestRootId(new TestRootId(1L))
            .setName("Test5_2")
            .build();

        TestEntity5 testEntity5_3 = TestEntity5.builder()
            .setId(new TestEntity5Id(13L))
            .setTestEntity4Id(new TestEntity4Id(9L))
            .setTestEntity6(TestEntity6.builder()
                .setId(new TestEntity6Id(3L))
                .setName("Test6_3")
                .build())
            .setTestRootId(new TestRootId(1L))
            .setName("Test5_3")
            .build();

        TestEntity4 testEntity4 = TestEntity4.builder()
            .setId(new TestEntity4Id(9L))
            .setName("TestEntity 4 1")
            .setTestEntity5List(Arrays.asList(testEntity5_1, testEntity5_2, testEntity5_3))
            .setTestEntity3Id(new TestEntity3Id(6L))
            .build();

        TestEntity4 testEntity4_2 = TestEntity4.builder()
            .setId(new TestEntity4Id(10L))
            .setName("TestEntity 4 1")
            .setTestEntity3Id(new TestEntity3Id(6L))
            .build();


        TestEntity3 t2A_3_1 = TestEntity3.builder()
            .setId(new TestEntity3Id(5L))
            .setName("Test2ATest3_1")
            .setTestRootId(new TestRootId(1L))
            .setTestEntity2Id(new TestEntity2Id(3L))
            .build();

        TestEntity3 t2A_3_2 = TestEntity3.builder()
            .setId(new TestEntity3Id(6L))
            .setName("Test2ATest3_2")
            .setTestEntity4List(newArrayListOf(testEntity4, testEntity4_2))
            .setTestRootId(new TestRootId(1L))
            .setTestEntity2Id(new TestEntity2Id(3L))
            .build();

        TestEntity3 t2B_3_1 = TestEntity3.builder()
            .setId(new TestEntity3Id(7L))
            .setName("Test2BTest3_1")
            .setTestRootId(new TestRootId(1L))
            .setTestEntity2Id(new TestEntity2Id(4L))
            .build();

        TestEntity3 t2B_3_2 = TestEntity3.builder()
            .setId(new TestEntity3Id(8L))
            .setName("Test2BTest3_2")
            .setTestRootId(new TestRootId(1L))
            .setTestEntity2Id(new TestEntity2Id(4L))
            .build();

        return TestRoot.builder()
            .setId(new TestRootId(1L))
            .setName("TestRoot")
            .setTestEntity1(TestEntity1.builder()
                .setId(new TestEntity1Id(2L))
                .setName("TestEntity1")
                .setTestRootId(new TestRootId(1L))
                .setTestEntity2A(TestEntity2.builder()
                    .setId(new TestEntity2Id(3L))
                    .setName("TestEntity 2 A")
                    .setTestRootId(new TestRootId(1L))
                    .setTestEntity3List(newArrayListOf(t2A_3_1, t2A_3_2))
                    .build())
                .setTestEntity2B(TestEntity2.builder()
                    .setId(new TestEntity2Id(4L))
                    .setName("TestEntity 2 B")
                    .setTestRootId(new TestRootId(1L))
                    .setTestEntity3List(newArrayListOf(t2B_3_1, t2B_3_2))
                    .build())
                .build())
            .build();
    }

    public static TestRootHierarchical buildTestRootHierarchicalOnlyRoot() {
        return TestRootHierarchical.builder()
            .setId(new TestRootHierarchicalId(1L))
            .setName("TestRoot")
            .build();
    }

    public static TestRootHierarchical buildTestRootHierarchicalCompleteLevel1() {
        return TestRootHierarchical.builder()
            .setId(new TestRootHierarchicalId(1L))
            .setName("TestRoot")
            .setChild(TestRootHierarchical.builder()
                .setId(new TestRootHierarchicalId(2L))
                .setParentId(new TestRootHierarchicalId(1L))
                .setName("TestChild1")
                .build())
            .build();
    }

    public static TestRootHierarchical buildTestRootHierarchicalCompleteLevel2() {
        return TestRootHierarchical.builder()
            .setId(new TestRootHierarchicalId(1L))
            .setName("TestRoot")
            .setChild(TestRootHierarchical.builder()
                .setId(new TestRootHierarchicalId(2L))
                .setParentId(new TestRootHierarchicalId(1L))
                .setName("TestChild1")
                .setChild(TestRootHierarchical.builder()
                    .setId(new TestRootHierarchicalId(3L))
                    .setParentId(new TestRootHierarchicalId(2L))
                    .setName("TestChild2")
                    .build())
                .build())
            .build();
    }

    public static TestRootHierarchical buildTestRootHierarchicalCompleteLevel3() {
        return TestRootHierarchical.builder()
            .setId(new TestRootHierarchicalId(1L))
            .setName("TestRoot")
            .setChild(TestRootHierarchical.builder()
                .setId(new TestRootHierarchicalId(2L))
                .setParentId(new TestRootHierarchicalId(1L))
                .setName("TestChild1")
                .setChild(TestRootHierarchical.builder()
                    .setId(new TestRootHierarchicalId(3L))
                    .setParentId(new TestRootHierarchicalId(2L))
                    .setName("TestChild2")
                    .setChild(TestRootHierarchical.builder()
                        .setId(new TestRootHierarchicalId(4L))
                        .setParentId(new TestRootHierarchicalId(3L))
                        .setName("TestChild3")
                        .build())
                    .build())
                .build())
            .build();
    }

    public static TestRootHierarchicalBackref buildTestRootHierarchicalBackrefOnlyRoot() {
        return TestRootHierarchicalBackref.builder()
            .setId(new TestRootHierarchicalBackrefId(1L))
            .setName("TestRoot")
            .build();
    }

    public static TestRootHierarchicalBackref buildTestRootHierarchicalBackrefCompleteLevel1() {
        TestRootHierarchicalBackref tr = buildTestRootHierarchicalBackrefOnlyRoot();
        TestRootHierarchicalBackref child = TestRootHierarchicalBackref.builder()
            .setId(new TestRootHierarchicalBackrefId(2L))
            .setParent(tr)
            .setName("TestChild1")
            .build();
        tr.setChild(child);
        return tr;
    }

    public static TestRootHierarchicalBackref buildTestRootHierarchicalBackrefCompleteLevel2() {
        TestRootHierarchicalBackref tr = buildTestRootHierarchicalBackrefCompleteLevel1();

        TestRootHierarchicalBackref child2 = TestRootHierarchicalBackref.builder()
            .setId(new TestRootHierarchicalBackrefId(3L))
            .setParent(tr.getChild())
            .setName("TestChild2")
            .build();
        tr.getChild().setChild(child2);
        return tr;
    }

    public static TestRootHierarchicalBackref buildTestRootHierarchicalBackrefCompleteLevel3() {
        TestRootHierarchicalBackref tr = buildTestRootHierarchicalBackrefCompleteLevel2();

        TestRootHierarchicalBackref child3 = TestRootHierarchicalBackref.builder()
            .setId(new TestRootHierarchicalBackrefId(4L))
            .setParent(tr.getChild().getChild())
            .setName("TestChild3")
            .build();
        tr.getChild().getChild().setChild(child3);
        return tr;
    }

    public static TestRootManyToMany buildManyToManyOnlyRoot() {
        return TestRootManyToMany.builder()
            .setId(new TestRootManyToManyId(1L))
            .setName("TestRoot")
            .setTestEntityManyToManyAList(new ArrayList<>())
            .build();
    }

    public static TestRootManyToMany buildManyToManyComplete() {
        TestRootManyToMany tr = TestRootManyToMany.builder()
            .setId(new TestRootManyToManyId(1L))
            .setName("TestRoot")
            .setTestEntityManyToManyAList(new ArrayList<>())
            .build();

        TestEntityManyToManyA a1 = TestEntityManyToManyA.builder()
            .setId(new TestEntityManyToManyAId(2L))
            .setName("TestEntityA1")
            .setTestRootManyToMany(tr)
            .setTestEntityManyToManyJoinList(newArrayListOf(

                TestEntityManyToManyJoin.builder()
                    .setId(new TestEntityManyToManyJoinId(5L))
                    .setTestEntityManyToManyAId(new TestEntityManyToManyAId(2L))
                    .setTestEntityManyToManyB(TestEntityManyToManyB.builder()
                        .setId(new TestEntityManyToManyBId(6L))
                        .setName("TestEntityB1")
                        .build()
                    )
                    .build(),

                TestEntityManyToManyJoin.builder()
                    .setId(new TestEntityManyToManyJoinId(7L))
                    .setTestEntityManyToManyAId(new TestEntityManyToManyAId(2L))
                    .setTestEntityManyToManyB(TestEntityManyToManyB.builder()
                        .setId(new TestEntityManyToManyBId(8L))
                        .setName("TestEntityB2")
                        .build()
                    )
                    .build()
            ))
            .build();

        TestEntityManyToManyA a2 = TestEntityManyToManyA.builder()
            .setId(new TestEntityManyToManyAId(3L))
            .setName("TestEntityA2")
            .setTestRootManyToMany(tr)
            .setTestEntityManyToManyJoinList(newArrayListOf(
                TestEntityManyToManyJoin.builder()
                    .setId(new TestEntityManyToManyJoinId(9L))
                    .setTestEntityManyToManyAId(new TestEntityManyToManyAId(3L))
                    .setTestEntityManyToManyB(TestEntityManyToManyB.builder()
                        .setId(new TestEntityManyToManyBId(10L))
                        .setName("TestEntityB3")
                        .build()
                    )
                    .build(),

                TestEntityManyToManyJoin.builder()
                    .setId(new TestEntityManyToManyJoinId(11L))
                    .setTestEntityManyToManyAId(new TestEntityManyToManyAId(3L))
                    .setTestEntityManyToManyB(TestEntityManyToManyB.builder()
                        .setId(new TestEntityManyToManyBId(8L))
                        .setName("TestEntityB2")
                        .build()
                    )
                    .build()
            )).build();
        tr.getTestEntityManyToManyAList().add(a1);
        tr.getTestEntityManyToManyAList().add(a2);
        return tr;
    }

    public static VoAggregateThreeLevel buildVoAggregateThreeLevelMax() {
        return VoAggregateThreeLevel.builder()
            .setIdentifikationsNummer(new VoAggregateThreeLevelId(3L))
            .setInfo("TestMax")
            .setMyComplexVo(ComplexVo.builder()
                .setValueA("myComplex_ValueA")
                .setValueB(SimpleVo.builder().setValue("myComplex_ValueB").build())
                .build())
            .setThreeLevelVo(
                ThreeLevelVo.builder()
                    .setLevelTwoA(
                        ThreeLevelVoLevelTwo.builder()
                            .setLevelThreeA(ThreeLevelVoLevelThree.builder()
                                .setText("2_A_3_A_test")
                                .setAnother("2_A_3_A_another")
                                .build())
                            .setLevelThreeB(ThreeLevelVoLevelThree.builder()
                                .setText("2_A_3_B_test")
                                .setAnother("2_A_3_B_another")
                                .build())
                            .build()
                    )
                    .setLevelTwoB(
                        ThreeLevelVoLevelTwo.builder()
                            .setLevelThreeA(ThreeLevelVoLevelThree.builder()
                                .setText("2_B_3_A_test")
                                .setAnother("2_B_3_A_another")
                                .build())
                            .setLevelThreeB(ThreeLevelVoLevelThree.builder()
                                .setText("2_B_3_B_test")
                                .setAnother("2_B_3_B_another")
                                .build())
                            .build()
                    )
                    .setOwnValue(5)
                    .build()

            )
            .build();
    }

    public static VoAggregateThreeLevel buildVoAggregateThreeLevelMiddle() {
        return VoAggregateThreeLevel.builder()
            .setIdentifikationsNummer(new VoAggregateThreeLevelId(2L))
            .setInfo("TestMiddle")
            .setThreeLevelVo(
                ThreeLevelVo.builder()
                    .setLevelTwoA(
                        ThreeLevelVoLevelTwo.builder()
                            .setLevelThreeA(ThreeLevelVoLevelThree.builder()
                                .setText("2_A_3_A_test")
                                .setAnother("2_A_3_A_another")
                                .build())
                            .build()
                    )
                    .setOwnValue(5)
                    .build()
            )
            .build();
    }

    public static VoAggregateThreeLevel buildVoAggregateThreeLevelMin() {
        return VoAggregateThreeLevel.builder()
            .setIdentifikationsNummer(new VoAggregateThreeLevelId(1L))
            .build();
    }

    public static TestRootOneToMany buildOneToManyOnlyRoot() {
        return TestRootOneToMany.builder()
            .setId(new TestRootOneToManyId(1L))
            .setName("TestRoot")
            .setTestEntityOneToManyList(new ArrayList<>())
            .build();
    }

    public static TestRootOneToMany buildOneToManyComplete() {
        return TestRootOneToMany.builder()
            .setId(new TestRootOneToManyId(1L))
            .setName("TestRoot")
            .setTestEntityOneToManyList(newArrayListOf(
                TestEntityOneToMany.builder()
                    .setId(new TestEntityOneToManyId(2L))
                    .setName("TestEntity")
                    .setTestRootId(new TestRootOneToManyId(1L))
                    .build(),
                TestEntityOneToMany.builder()
                    .setId(new TestEntityOneToManyId(3L))
                    .setName("TestEntity")
                    .setTestRootId(new TestRootOneToManyId(1L))
                    .build())
            )
            .build();
    }

    public static TestRootOneToOneFollowing buildOneToOneFollowingOnlyRoot() {
        return TestRootOneToOneFollowing.builder()
            .setId(new TestRootOneToOneFollowingId(1L))
            .setName("TestRoot")
            .build();
    }

    public static TestRootOneToOneFollowing buildOneToOneFollowingComplete() {
        return TestRootOneToOneFollowing.builder()
            .setId(new TestRootOneToOneFollowingId(1L))
            .setName("TestRoot")
            .setTestEntityOneToOneFollowing(TestEntityOneToOneFollowing.builder()
                .setId(new TestEntityOneToOneFollowingId(2L))
                .setName("TestEntity")
                .setTestRootId(new TestRootOneToOneFollowingId(1L))
                .build())
            .build();
    }

    public static TestRootOneToOneFollowingLeading buildOneToOneFollowingLeadingOnlyRoot() {
        return TestRootOneToOneFollowingLeading.builder()
            .setId(new TestRootOneToOneFollowingLeadingId(1L))
            .setName("TestRoot")
            .build();
    }

    public static TestRootOneToOneFollowingLeading buildOneToOneFollowingLeadingComplete() {
        return TestRootOneToOneFollowingLeading.builder()
            .setId(new TestRootOneToOneFollowingLeadingId(1L))
            .setName("TestRoot")
            .setTestEntityAOneToOneFollowingLeading(TestEntityAOneToOneFollowingLeading.builder()
                .setId(new TestEntityAOneToOneFollowingLeadingId(2L))
                .setName("TestEntityA")
                .setTestRootId(new TestRootOneToOneFollowingLeadingId(1L))
                .build())
            .setTestEntityBOneToOneFollowingLeading(TestEntityBOneToOneFollowingLeading.builder()
                .setId(new TestEntityBOneToOneFollowingLeadingId(3L))
                .setName("TestEntityB")
                .build())
            .build();
    }

    public static TestRootOneToOneLeading buildOneToOneLeadingOnlyRoot() {
        return TestRootOneToOneLeading.builder()
            .setId(new TestRootOneToOneLeadingId(1L))
            .setName("TestRoot")
            .build();
    }

    public static TestRootOneToOneLeading buildOneToOneLeadingComplete() {
        return TestRootOneToOneLeading.builder()
            .setId(new TestRootOneToOneLeadingId(1L))
            .setName("TestRoot")
            .setTestEntityOneToOneLeading(TestEntityOneToOneLeading.builder()
                .setId(new TestEntityOneToOneLeadingId(2L))
                .setName("TestEntity")
                .build())
            .build();
    }

    public static TestRootSimple buildTestRootSimple() {
        return TestRootSimple.builder()
            .setId(new TestRootSimpleId(1L))
            .setName("TestRoot")
            .build();
    }

    public static TestRootSimpleUuid buildTestRootSimpleUUID() {
        UUID uuid = UUID.randomUUID();
        return TestRootSimpleUuid.builder()
            .setId(new TestRootSimpleUuidId(uuid))
            .setName("TestRoot")
            .build();
    }

    public static VoAggregateRoot buildVoAggregateMaxWithEntity() {
        return VoAggregateRoot.builder()
            .setId(new VoAggregateRootId(4L))
            .setText("Test")
            .setMyComplexVo(ComplexVo.builder()
                .setValueA("ValueA")
                .setValueB(new SimpleVo("ValueB"))
                .build())
            .setMySimpleVo(SimpleVo.builder()
                .setValue("ValueSimpleOneToOe")
                .build())
            .setValueObjectsOneToMany(newArrayListOf(
                SimpleVoOneToMany.builder()
                    .setValue("OneToMany1")
                    .build(),
                SimpleVoOneToMany.builder()
                    .setValue("OneToMany2")
                    .build(),
                SimpleVoOneToMany.builder()
                    .setValue("OneToMany3")
                    .build()
            ))
            .setValueObjectsOneToMany2(newArrayListOf(
                SimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_1")
                    .setOneToMany3Set(new HashSet<>(newArrayListOf(
                        SimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_2_1")
                            .build()
                        ,
                        SimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_2_2")
                            .build()
                    )
                    ))
                    .build(),
                SimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_2")
                    .build(),
                SimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_3")
                    .setOneToMany3Set(new HashSet<>(newArrayListOf(
                        SimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_3_1")
                            .build()
                    )
                    ))
                    .build()
            ))
            .setEntities(
                newArrayListOf(
                    VoEntity.builder()
                        .setId(new VoEntityId(1L))
                        .setRootId(new VoAggregateRootId(4L))
                        .setText("Entity1")
                        .setValueObjectsOneToMany(
                            new HashSet<>(newArrayListOf(
                                VoOneToManyEntity.builder().setValue("vo1")
                                    .setOneToManySet(
                                        new HashSet<>(newArrayListOf(
                                            VoOneToManyEntity2.builder().setValue("innerVo1").build(),
                                            VoOneToManyEntity2.builder().setValue("innerVo2").build()
                                        ))).build(),
                                VoOneToManyEntity.builder().setValue("vo2")
                                    .setOneToManySet(
                                        new HashSet<>(newArrayListOf(
                                            VoOneToManyEntity2.builder().setValue("innerVo3").build(),
                                            VoOneToManyEntity2.builder().setValue("innerVo4").build()
                                        ))).build(),
                                VoOneToManyEntity.builder().setValue("vo3").build()
                            )))
                        .build(),
                    VoEntity.builder()
                        .setRootId(new VoAggregateRootId(4L))
                        .setId(new VoEntityId(2L))
                        .setText("Entity2")
                        .setValueObjectsOneToMany(
                            new HashSet<>(newArrayListOf(
                                VoOneToManyEntity.builder().setValue("vo1")
                                    .setOneToManySet(
                                        new HashSet<>(newArrayListOf(
                                            VoOneToManyEntity2.builder().setValue("innerVo1").build(),
                                            VoOneToManyEntity2.builder().setValue("innerVo2").build()
                                        ))).build()
                            )))
                        .build(),
                    VoEntity.builder()
                        .setId(new VoEntityId(3L))
                        .setRootId(new VoAggregateRootId(4L))
                        .setText("Entity3")
                        .build()
                )
            )
            .build();
    }

    public static AutoMappedVoAggregateRoot buildAutoMappedVoAggregateMaxWithEntity() {
        return AutoMappedVoAggregateRoot.builder()
            .setId(new AutoMappedVoAggregateRootId(4L))
            .setText("Test")
            .setMyComplexVo(AutoMappedComplexVo.builder()
                .setValueA("ValueA")
                .setValueB(new AutoMappedSimpleVo("ValueB"))
                .build())
            .setMySimpleVo(AutoMappedSimpleVo.builder()
                .setValue("ValueSimpleOneToOe")
                .build())
            .setValueObjectsOneToMany(newArrayListOf(
                AutoMappedSimpleVoOneToMany.builder()
                    .setValue("OneToMany1")
                    .build(),
                AutoMappedSimpleVoOneToMany.builder()
                    .setValue("OneToMany2")
                    .build(),
                AutoMappedSimpleVoOneToMany.builder()
                    .setValue("OneToMany3")
                    .build()
            ))
            .setValueObjectsOneToMany2(newArrayListOf(
                AutoMappedSimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_1")
                    .setOneToMany3Set(new HashSet<>(newArrayListOf(
                        AutoMappedSimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_2_1")
                            .build()
                        ,
                        AutoMappedSimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_2_2")
                            .build()
                    )
                    ))
                    .build(),
                AutoMappedSimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_2")
                    .build(),
                AutoMappedSimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_3")
                    .setOneToMany3Set(new HashSet<>(newArrayListOf(
                        AutoMappedSimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_3_1")
                            .build()
                    )
                    ))
                    .build()
            ))
            .setEntities(
                newArrayListOf(
                    AutoMappedVoEntity.builder()
                        .setId(new AutoMappedVoEntityId(1L))
                        .setRootId(new AutoMappedVoAggregateRootId(4L))
                        .setText("Entity1")
                        .setValueObjectsOneToMany(
                            new HashSet<>(newArrayListOf(
                                AutoMappedVoOneToManyEntity.builder().setValue("vo1")
                                    .setOneToManySet(
                                        new HashSet<>(newArrayListOf(
                                            AutoMappedVoOneToManyEntity2.builder().setValue("innerVo1").build(),
                                            AutoMappedVoOneToManyEntity2.builder().setValue("innerVo2").build()
                                        ))).build(),
                                AutoMappedVoOneToManyEntity.builder().setValue("vo2")
                                    .setOneToManySet(
                                        new HashSet<>(newArrayListOf(
                                            AutoMappedVoOneToManyEntity2.builder().setValue("innerVo3").build(),
                                            AutoMappedVoOneToManyEntity2.builder().setValue("innerVo4").build()
                                        ))).build(),
                                AutoMappedVoOneToManyEntity.builder().setValue("vo3").build()
                            )))
                        .build(),
                    AutoMappedVoEntity.builder()
                        .setRootId(new AutoMappedVoAggregateRootId(4L))
                        .setId(new AutoMappedVoEntityId(2L))
                        .setText("Entity2")
                        .setValueObjectsOneToMany(
                            new HashSet<>(newArrayListOf(
                                AutoMappedVoOneToManyEntity.builder().setValue("vo1")
                                    .setOneToManySet(
                                        new HashSet<>(newArrayListOf(
                                            AutoMappedVoOneToManyEntity2.builder().setValue("innerVo1").build(),
                                            AutoMappedVoOneToManyEntity2.builder().setValue("innerVo2").build()
                                        ))).build()
                            )))
                        .build(),
                    AutoMappedVoEntity.builder()
                        .setId(new AutoMappedVoEntityId(3L))
                        .setRootId(new AutoMappedVoAggregateRootId(4L))
                        .setText("Entity3")
                        .build()
                )
            )
            .build();
    }

    public static VoAggregateRoot buildVoAggregateMax() {
        return VoAggregateRoot.builder()
            .setId(new VoAggregateRootId(3L))
            .setText("Test")
            .setMyComplexVo(ComplexVo.builder()
                .setValueA("ValueA")
                .setValueB(new SimpleVo("ValueB"))
                .build())
            .setMySimpleVo(SimpleVo.builder()
                .setValue("ValueSimpleOneToOe")
                .build())
            .setValueObjectsOneToMany(newArrayListOf(
                SimpleVoOneToMany.builder()
                    .setValue("OneToMany1")
                    .build(),
                SimpleVoOneToMany.builder()
                    .setValue("OneToMany2")
                    .build(),
                SimpleVoOneToMany.builder()
                    .setValue("OneToMany3")
                    .build()
            ))
            .setValueObjectsOneToMany2(newArrayListOf(
                SimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_1")
                    .setOneToMany3Set(new HashSet<>(newArrayListOf(
                        SimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_2_1")
                            .build()
                        ,
                        SimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_2_2")
                            .build()
                    )
                    ))
                    .build(),
                SimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_2")
                    .build(),
                SimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_3")
                    .setOneToMany3Set(new HashSet<>(newArrayListOf(
                        SimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_3_1")
                            .build()
                    )
                    ))
                    .build()
            ))
            .setVoIdentityRef(VoIdentityRef.builder()
                .setValue("ID_REF_VALUE")
                .setIdRef(new VoEntityId(18L))
                .build())
            .build();
    }

    public static AutoMappedVoAggregateRoot buildAutoMappedVoAggregateMax() {
        return AutoMappedVoAggregateRoot.builder()
            .setId(new AutoMappedVoAggregateRootId(3L))
            .setText("Test")
            .setMyComplexVo(AutoMappedComplexVo.builder()
                .setValueA("ValueA")
                .setValueB(new AutoMappedSimpleVo("ValueB"))
                .build())
            .setMySimpleVo(AutoMappedSimpleVo.builder()
                .setValue("ValueSimpleOneToOe")
                .build())
            .setValueObjectsOneToMany(newArrayListOf(
                AutoMappedSimpleVoOneToMany.builder()
                    .setValue("OneToMany1")
                    .build(),
                AutoMappedSimpleVoOneToMany.builder()
                    .setValue("OneToMany2")
                    .build(),
                AutoMappedSimpleVoOneToMany.builder()
                    .setValue("OneToMany3")
                    .build()
            ))
            .setValueObjectsOneToMany2(newArrayListOf(
                AutoMappedSimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_1")
                    .setOneToMany3Set(new HashSet<>(newArrayListOf(
                        AutoMappedSimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_2_1")
                            .build()
                        ,
                        AutoMappedSimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_2_2")
                            .build()
                    )
                    ))
                    .build(),
                AutoMappedSimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_2")
                    .build(),
                AutoMappedSimpleVoOneToMany2.builder()
                    .setValue("OneToMany2_3")
                    .setOneToMany3Set(new HashSet<>(newArrayListOf(
                        AutoMappedSimpleVoOneToMany3.builder()
                            .setValue("OneToMany3_3_1")
                            .build()
                    )
                    ))
                    .build()
            ))
            .setVoIdentityRef(AutoMappedVoIdentityRef.builder()
                .setValue("ID_REF_VALUE")
                .setIdRef(new AutoMappedVoEntityId(18L))
                .build())
            .build();
    }

    public static VoAggregateRoot buildVoAggregateMiddle() {
        return VoAggregateRoot.builder()
            .setId(new VoAggregateRootId(1L))
            .setText("Test")
            .setMyComplexVo(ComplexVo.builder()
                .setValueA("ValueA")
                .setValueB(new SimpleVo("ValueB"))
                .build())
            .setMySimpleVo(SimpleVo.builder()
                .setValue("ValueSimpleOneToOe")
                .build())
            .setValueObjectsOneToMany(newArrayListOf(
                SimpleVoOneToMany.builder()
                    .setValue("OneToMany1")
                    .build(),
                SimpleVoOneToMany.builder()
                    .setValue("OneToMany2")
                    .build(),
                SimpleVoOneToMany.builder()
                    .setValue("OneToMany3")
                    .build()
            ))
            .setVoIdentityRef(VoIdentityRef.builder()
                .setValue("ID_REF_VALUE")
                .setIdRef(new VoEntityId(18L))
                .build())
            .build();
    }

    public static AutoMappedVoAggregateRoot buildAutoMappedVoAggregateMiddle() {
        return AutoMappedVoAggregateRoot.builder()
            .setId(new AutoMappedVoAggregateRootId(1L))
            .setText("Test")
            .setMyComplexVo(AutoMappedComplexVo.builder()
                .setValueA("ValueA")
                .setValueB(new AutoMappedSimpleVo("ValueB"))
                .build())
            .setMySimpleVo(AutoMappedSimpleVo.builder()
                .setValue("ValueSimpleOneToOe")
                .build())
            .setValueObjectsOneToMany(newArrayListOf(
                AutoMappedSimpleVoOneToMany.builder()
                    .setValue("OneToMany1")
                    .build(),
                AutoMappedSimpleVoOneToMany.builder()
                    .setValue("OneToMany2")
                    .build(),
                AutoMappedSimpleVoOneToMany.builder()
                    .setValue("OneToMany3")
                    .build()
            ))
            .setVoIdentityRef(AutoMappedVoIdentityRef.builder()
                .setValue("ID_REF_VALUE")
                .setIdRef(new AutoMappedVoEntityId(18L))
                .build())
            .build();
    }

    public static VoAggregateRoot buildVoAggregateMin() {
        return VoAggregateRoot.builder()
            .setId(new VoAggregateRootId(2L))
            .setMySimpleVo(SimpleVo.builder()
                .setValue("ValueSimpleOneToOe2")
                .build()
            )
            .build();
    }

    public static AutoMappedVoAggregateRoot buildAutoMappedVoAggregateMin() {
        return AutoMappedVoAggregateRoot.builder()
            .setId(new AutoMappedVoAggregateRootId(2L))
            .setMySimpleVo(AutoMappedSimpleVo.builder()
                .setValue("ValueSimpleOneToOe2")
                .build()
            )
            .build();
    }

    public static List<TestRootOneToOneFollowing> buildManyOneToOneFollowingComplete() {
        TestRootOneToOneFollowing tr1 = TestRootOneToOneFollowing.builder()
            .setId(new TestRootOneToOneFollowingId(1L))
            .setName("TestRoot1")
            .setTestEntityOneToOneFollowing(TestEntityOneToOneFollowing.builder()
                .setId(new TestEntityOneToOneFollowingId(2L))
                .setName("TestEntity1")
                .setTestRootId(new TestRootOneToOneFollowingId(1L))
                .build())
            .build();
        TestRootOneToOneFollowing tr2 = TestRootOneToOneFollowing.builder()
            .setId(new TestRootOneToOneFollowingId(3L))
            .setName("TestRoot2")
            .setTestEntityOneToOneFollowing(TestEntityOneToOneFollowing.builder()
                .setId(new TestEntityOneToOneFollowingId(4L))
                .setName("TestEntity2")
                .setTestRootId(new TestRootOneToOneFollowingId(3L))
                .build())
            .build();
        return Arrays.asList(tr1, tr2);
    }


    public static OptionalAggregate buildOptionalAggregateMin() {
        return OptionalAggregate.builder()
            .setId(new OptionalAggregateId(1L))
            .setMandatoryText("MandatoryText")
            .setMandatorySimpleValueObject(
                MySimpleValueObject
                    .builder()
                    .setValue("MandatorySimple")
                    .build())
            .setMandatoryComplexValueObject(
                MyComplexValueObject
                    .builder()
                    .setMandatoryText("ComplMandatoryText")
                    .setMandatorySimpleValueObject(
                        MySimpleValueObject
                            .builder()
                            .setValue("ComSimMandatoryText")
                            .build())
                    .build())
            .setRefValueObject(RefValueObject
                .builder()
                .setMandatoryText("TextMandat")
                .build())
            .build();
    }

    public static OptionalAggregate buildOptionalAggregateMax() {
        return OptionalAggregate.builder()
            .setId(new OptionalAggregateId(1L))
            .setMandatoryText("MandatoryText")
            .setMandatorySimpleValueObject(
                MySimpleValueObject
                    .builder()
                    .setValue("MandatorySimple")
                    .build())
            .setMandatoryComplexValueObject(
                MyComplexValueObject
                    .builder()
                    .setMandatoryText("ComplMandatoryText")
                    .setMandatorySimpleValueObject(
                        MySimpleValueObject
                            .builder()
                            .setValue("ComSimMandatoryText")
                            .build())
                    .build())
            .setOptionalText("OptionalText")
            .setOptionalLong(5L)
            .setOptionalSimpleValueObject(
                MySimpleValueObject
                    .builder()
                    .setValue("OptionalSimple")
                    .build()
            )
            .setOptionalComplexValueObject(
                MyComplexValueObject
                    .builder()
                    .setMandatoryText("OComplMandatoryText")
                    .setOptionalText("OComOptionalText")
                    .setOptionalLong(6L)
                    .setMandatorySimpleValueObject(
                        MySimpleValueObject
                            .builder()
                            .setValue("OComSimMandatoryText")
                            .build())
                    .setOptionalSimpleValueObject(MySimpleValueObject
                        .builder()
                        .setValue("OComSimOptionalText")
                        .build()
                    )
                    .build()
            )
            .setOptionalEntity(
                OptionalEntity
                    .builder()
                    .setId(new OptionalEntityId(2L))
                    .setOptionalText("E2OptionalText")
                    .setMandatoryText("E2MandatoryText")
                    .setMandatorySimpleValueObject(
                        MySimpleValueObject
                            .builder()
                            .setValue("E2SimMandatoryText")
                            .build())
                    .setOptionalSimpleValueObject(MySimpleValueObject
                        .builder()
                        .setValue("E2SimOptionalText")
                        .build())
                    .setMandatoryComplexValueObject(
                        MyComplexValueObject
                            .builder()
                            .setMandatoryText("E2ComplMandatoryText")
                            .setOptionalText("E2ComplMandatOT")
                            .setMandatorySimpleValueObject(
                                MySimpleValueObject
                                    .builder()
                                    .setValue("E2ComSimMandatText")
                                    .build())
                            .build()
                    )
                    .setOptionalComplexValueObject(
                        MyComplexValueObject
                            .builder()
                            .setMandatoryText("E2OComplMandatText")
                            .setOptionalText("E2OComplOptText")
                            .setMandatorySimpleValueObject(
                                MySimpleValueObject
                                    .builder()
                                    .setValue("E2OComSimMandatText")
                                    .build())
                            .setOptionalSimpleValueObject(MySimpleValueObject
                                .builder()
                                .setValue("E2OComSimOptText")
                                .build())
                            .build()
                    )
                    .setComplexValueObjectList(newArrayListOf(
                        MyComplexValueObject
                            .builder()
                            .setMandatoryText("EComplListText1")
                            .setOptionalText("EComplListText1")
                            .setMandatorySimpleValueObject(
                                MySimpleValueObject
                                    .builder()
                                    .setValue("EComListText1")
                                    .build())
                            .setOptionalSimpleValueObject(MySimpleValueObject
                                .builder()
                                .setValue("EComListSimText1")
                                .build())
                            .setOptionalLong(88L)
                            .build(),
                        MyComplexValueObject
                            .builder()
                            .setMandatoryText("EComplListText2")
                            .setOptionalText("EComplListText2")
                            .setMandatorySimpleValueObject(
                                MySimpleValueObject
                                    .builder()
                                    .setValue("EComListText2")
                                    .build())
                            .build()))
                    .build()
            )
            .setRefValueObject(RefValueObject
                .builder()
                .setOptionalRef(new RefAggId(1L))
                .setMandatoryText("mandatVoText")
                .build())
            .setOptionalRefId(new RefAggId(1L))
            .setRefValueObjectList(newArrayListOf(
                RefValueObject
                    .builder()
                    .setMandatoryText("Ref1")
                    .setOptionalRef(new RefAggId(1L))
                    .build(),
                RefValueObject
                    .builder()
                    .setMandatoryText("Ref2")
                    .build()
            ))
            .build();
    }

    public static RecordTest buildRecordTest(){
        RecordTest recordTest = RecordTest
            .builder()
            .setId(new RecordTestId(1l))
            .setMyValue("Vlue")
            .setMyVo(
                RecordVo
                    .builder()
                    .setValue1("v1")
                    .setValue2(22l)
                    .build()
            )
            .setMyVoList(newArrayListOf(
                RecordVo
                    .builder()
                    .setValue1("v1_1")
                    .setValue2(23l)
                    .build(),
                RecordVo
                    .builder()
                    .setValue1("v1_2")
                    .setValue2(24l)
                    .build()

            ))
            .build();
        return recordTest;
    }

    public static RefAgg buildRefAgg() {
        return RefAgg
            .builder()
            .setId(new RefAggId(1L))
            .setMandatoryText("mandat")
            .setOptionalText("opt")
            .build();
    }

    public static TestRootTemporal buildTestRootTemporal(){
        return buildTestRootTemporal(OffsetDateTime.now());
    }
    public static TestRootTemporal buildTestRootTemporal(OffsetDateTime now){
        TestRootTemporal t = TestRootTemporal.
            builder()
            .setId(new TestRootTemporalId(1l))
            .setLocalDate(now.toLocalDate())
            .setLocalDateTime(now.toLocalDateTime())
            .setLocalTime(now.toLocalTime())
            .setMonthDay(MonthDay.from(now))
            .setMyCalendar(GregorianCalendar.from(now.toZonedDateTime()))
            .setMyDate(Date.from(now.toInstant()))
            .setOffsetDateTime(now)
            .setMyInstant(now.toInstant())
            .setOffsetTime(now.toOffsetTime())
            .setMyYear(Year.from(now))
            .setYearMonth(YearMonth.from(now))
            .setZonedDateTime(now.toZonedDateTime())
            .build();
        return t;
    }

    public static TreeRoot buildTreeOnlyRoot() {
        return TreeRoot.builder()
            .setId(new TreeRootId(1L))
            .build();
    }

    public static TreeRoot buildTreeLevel1() {
        return TreeRoot.builder()
            .setId(new TreeRootId(1L))
            .setDirectChildNodes(newArrayListOf(
                TreeNode.builder()
                    .setId(new TreeNodeId(2L))
                    .setRootId(new TreeRootId(1L))
                    .setNodeName("TopLevelChild1")
                    .build())
            )
            .build();
    }

    public static TreeRoot buildTreeLevel2() {
        return TreeRoot.builder()
            .setId(new TreeRootId(1L))
            .setDirectChildNodes(newArrayListOf(
                TreeNode.builder()
                    .setId(new TreeNodeId(2L))
                    .setRootId(new TreeRootId(1L))
                    .setNodeName("TopLevelChild1")
                    .build(),
                TreeNode.builder()
                    .setId(new TreeNodeId(3L))
                    .setRootId(new TreeRootId(1L))
                    .setNodeName("TopLevelChild2")
                    .setChildNodes(newArrayListOf(
                            TreeNode.builder()
                                .setId(new TreeNodeId(4L))
                                .setParentNodeId(new TreeNodeId(3L))
                                .setNodeName("TestChildLevel2_2_2")
                                .build())
                    )
                    .build())
            )
            .build();

    }

    public static TreeRoot buildTreeLevel3() {
        return TreeRoot.builder()
            .setId(new TreeRootId(1L))
            .setDirectChildNodes(
                newArrayListOf(
                    TreeNode.builder()
                        .setId(new TreeNodeId(2L))
                        .setRootId(new TreeRootId(1L))
                        .setNodeName("TopLevelChild1")
                        .setChildNodes(
                            newArrayListOf(
                                TreeNode.builder()
                                    .setId(new TreeNodeId(5L))
                                    .setParentNodeId(new TreeNodeId(2L))
                                    .setNodeName("TestChildLevel2_1_1")
                                    .build()
                            )
                        )
                        .build(),
                    TreeNode.builder()
                        .setId(new TreeNodeId(3L))
                        .setRootId(new TreeRootId(1L))
                        .setNodeName("TopLevelChild2")
                        .setChildNodes(
                            newArrayListOf(
                                TreeNode.builder()
                                    .setId(new TreeNodeId(7L))
                                    .setParentNodeId(new TreeNodeId(3L))
                                    .setNodeName("TestChildLevel2_2_2")
                                    .setChildNodes(
                                        newArrayListOf(
                                            TreeNode.builder()
                                                .setId(new TreeNodeId(8L))
                                                .setParentNodeId(new TreeNodeId(7L))
                                                .setNodeName("TestChildLevel3_2_2_2_1")
                                                .build()
                                        )
                                    )
                                    .build()
                            )
                        )
                        .build()
                    )
            )
        .build();
    }

    public static TestRootOneToOneVoDedicated buildOneToOneVoDedicatedOnlyRoot() {
        return TestRootOneToOneVoDedicated.builder()
            .setId(new TestRootOneToOneVoDedicatedId(1L))
            .setName("TestRoot")
            .build();
    }

    public static VoAggregateTwoLevel buildVoAggregateTwoLevel() {
        return VoAggregateTwoLevel.builder()
            .setId(new VoAggregateTwoLevelId(1L))
            .setLevelOne(VoLevelOne.builder()
                .setFirst(VoLevelTwoA.builder().setText("FIRST").build())
                .setSecond(VoLevelTwoA.builder().setText("SECOND").build())
                .setThird(VoLevelTwoB.builder().setBool(true).build())
                .build()
            )
            .build();
    }

    public static TestRootOneToOneVoDedicated buildOneToOneVoDedicatedComplete() {
        return TestRootOneToOneVoDedicated.builder()
            .setId(new TestRootOneToOneVoDedicatedId(1L))
            .setName("TestRoot")
            .setVo(VoDedicated.builder()
                .setName("TestVo")
                .build())
            .build();
    }

    public static VoAggregatePrimitive buildVoPrimitiveEmpty(){
        return VoAggregatePrimitive.builder()
            .setId(new VoAggregatePrimitiveId(1l))
            .build();
    }

    public static VoAggregatePrimitive buildVoPrimitiveComplete(){
        return VoAggregatePrimitive.builder()
            .setId(new VoAggregatePrimitiveId(2l))
            .setSimple(SimpleVoPrimitive.builder().setVal(11l).build())
            .setComplex(ComplexVoPrimitive.builder()
                .setNum(1l)
                .setVal(11l)
                .build())
            .setNested(NestedVoPrimitive
                .builder()
                .setSimple(SimpleVoPrimitive.builder()
                    .setVal(11l)
                    .build())
                .setComplex(ComplexVoPrimitive
                    .builder()
                    .setVal(11l)
                    .setNum(2l)
                    .build())
                .build())
            .setRecordMappedSimple(
                SimpleVoPrimitive.builder().setVal(11l).build()
            )
            .setRecordMappedComplex(ComplexVoPrimitive.builder()
                .setNum(3l)
                .setVal(11l)
                .build()
            )
            .setRecordMappedNested(NestedVoPrimitive
                .builder()
                .setSimple(SimpleVoPrimitive.builder()
                    .setVal(11l)
                    .build())
                .setComplex(ComplexVoPrimitive
                    .builder()
                    .setVal(11l)
                    .setNum(4l)
                    .build())
                .build())
            .build();
    }

/*
    public static RootIdEnumList buildRootIdEnumListOnlyRoot(){
        var r = RootIdEnumList
            .builder()
            .setId(new RootIdEnumListId(1l))
            .setName("OnlyRoot")
            .build();
        return r;
    }

    public static RootIdEnumList buildRootIdEnumListWithEntity(){
        var r = RootIdEnumList
            .builder()
            .setId(new RootIdEnumListId(1l))
            .setName("WithEntity")
            .setEntity(
                new EntityIdEnumList(
                    new EntityIdEnumList.EntityIdEnumListId(1l),
                    1l,
                    newArrayListOf(MyEnum.TWO),
                    newArrayListOf(new MyId(44L)),
                    null
                )
            )
            .build();
        return r;
    }

    public static RootIdEnumList buildRootIdEnumListComplete(){
        var r = RootIdEnumList
            .builder()
            .setId(new RootIdEnumListId(1l))
            .setName("Complete")
            .setEnumList(newArrayListOf(MyEnum.ONE, MyEnum.TWO))
            .setIdList(newArrayListOf(new MyId(1l), new MyId(2l)))
            .setEntity(
                new EntityIdEnumList(
                    new EntityIdEnumList.EntityIdEnumListId(1l),
                    1l,
                    newArrayListOf(MyEnum.TWO),
                    newArrayListOf(new MyId(44L)),
                    new ValueWithLists(newArrayListOf(MyEnum.ONE, MyEnum.TWO), newArrayListOf(new MyId(88l), new MyId(99l), new MyId(111l)))
                )
            )
            .build();
        return r;
    }*/

    @SafeVarargs
    public static <T> List<T> newArrayListOf(final T... p) {
        return Stream.of(p).collect(Collectors.toList());

    }

}
