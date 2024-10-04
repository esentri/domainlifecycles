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

package io.domainlifecycles.jooq.persistence.events;


import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.access.DlcAccess;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.companions.Entities;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import io.domainlifecycles.persistence.provider.DomainObjectInstanceAccessModel;
import io.domainlifecycles.persistence.repository.actions.PersistenceAction;
import org.jooq.UpdatableRecord;
import org.junit.jupiter.api.Test;
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
        Bestellung a = buildBestellung();
        Bestellung b = buildBestellung();
        b.setPrioritaet(Byte.valueOf("2"));

        PersistenceAction<?> action = new PersistenceAction<>(
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(b), PersistenceAction.ActionType.UPDATE,
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(a));
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).containsExactlyInAnyOrder("prioritaet");

        assertThat(getNewValue(action, "prioritaet")).isEqualTo(b.getPrioritaet());
        assertThat(getOldValue(action, "prioritaet")).isEqualTo(a.getPrioritaet());
    }

    @Test
    public void testChangeSimpleRefIdentity() {
        Bestellung a = buildBestellung();
        Bestellung b = buildBestellung();
        b.setKundennummer(new Kundennummer("88888"));

        PersistenceAction<?> action = new PersistenceAction<>(
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(b), PersistenceAction.ActionType.UPDATE,
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(a));
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).containsExactlyInAnyOrder("kundennummer");
        assertThat(getNewValue(action, "kundennummer")).isEqualTo(b.getKundennummer());
        assertThat(getOldValue(action, "kundennummer")).isEqualTo(a.getKundennummer());
    }

    @Test
    public void testChangeSimplePropertyPrimitive() {
        Bestellung a = buildBestellung();
        Bestellung b = buildBestellung();
        b.getBestellPositionen().get(0).setStueckzahl(66);
        DomainObjectInstanceAccessModel<?> bModel = persistenceConfiguration.domainPersistenceProvider.buildAccessModel(
            b);
        DomainObjectInstanceAccessModel<?> bPosModel = bModel.children.stream()
            .filter(c -> c.domainObject().equals(b.getBestellPositionen().get(0)))
            .findFirst()
            .get();
        DomainObjectInstanceAccessModel<?> aModel = persistenceConfiguration.domainPersistenceProvider.buildAccessModel(
            a);
        DomainObjectInstanceAccessModel<?> aPosModel = aModel.children.stream()
            .filter(c -> c.domainObject().equals(a.getBestellPositionen().get(0)))
            .findFirst()
            .get();
        PersistenceAction<?> action = new PersistenceAction(bPosModel, PersistenceAction.ActionType.UPDATE, aPosModel);
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).containsExactlyInAnyOrder("stueckzahl");
        assertThat(getNewValue(action, "stueckzahl")).isEqualTo(b.getBestellPositionen().get(0).getStueckzahl());
        assertThat(getOldValue(action, "stueckzahl")).isEqualTo(a.getBestellPositionen().get(0).getStueckzahl());
    }

    @Test
    public void testNoChanges() {
        Bestellung a = buildBestellung();
        Bestellung b = buildBestellung();
        PersistenceAction<?> action = new PersistenceAction<>(
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(b), PersistenceAction.ActionType.UPDATE,
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(a));
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).isEmpty();
    }

    @Test
    public void testChangeComplexVo() {
        Bestellung a = buildBestellung();
        Bestellung b = buildBestellung();
        b.getBestellPositionen().get(0).setStueckPreis(Preis
            .builder()
            .setBetrag(BigDecimal.valueOf(44))
            .setWaehrung(WaehrungEnum.EUR)
            .build());
        DomainObjectInstanceAccessModel<UpdatableRecord<?>> bModel =
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(
                b);
        DomainObjectInstanceAccessModel<UpdatableRecord<?>> bPosModel = bModel.children.stream()
            .filter(c -> c.domainObject().equals(b.getBestellPositionen().get(0)))
            .findFirst()
            .get();
        DomainObjectInstanceAccessModel<UpdatableRecord<?>> aModel =
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(
                a);
        DomainObjectInstanceAccessModel<UpdatableRecord<?>> aPosModel = aModel.children.stream()
            .filter(c -> c.domainObject().equals(a.getBestellPositionen().get(0)))
            .findFirst()
            .get();
        PersistenceAction<UpdatableRecord<?>> action = new PersistenceAction<>(bPosModel,
            PersistenceAction.ActionType.UPDATE, aPosModel);
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).containsExactlyInAnyOrder("stueckPreis");
        assertThat(getNewValue(action, "stueckPreis")).isEqualTo(b.getBestellPositionen().get(0).getStueckPreis());
        assertThat(getOldValue(action, "stueckPreis")).isEqualTo(a.getBestellPositionen().get(0).getStueckPreis());

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

    private Bestellung buildBestellung() {
        Bestellung b = Bestellung.builder()
            .setId(new BestellungId(1l))
            .setKundennummer(new Kundennummer("777777"))
            .setPrioritaet(Byte.valueOf("1"))
            .setLieferadresse(
                Lieferadresse.builder()
                    .setId(new LieferadresseId(1l))
                    .setName("Thor")
                    .setOrt("Donnerberg")
                    .setPostleitzahl("77777")
                    .setStrasse("Hammerallee 7")
                    .build()
            )
            .setBestellKommentare(newArrayListOf(
                BestellKommentar.builder()
                    .setId(new BestellKommentarId(1l))
                    .setKommentarAm(LocalDateTime.of(2021, 01, 1, 12, 0))
                    .setKommentarText("Mach schnell sonst kommt der Hammer!")
                    .build(),
                BestellKommentar.builder()
                    .setId(new BestellKommentarId(2l))
                    .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 0))
                    .setKommentarText("Der Donnergott grüßt!")
                    .build()
            ))
            .setBestellStatus(
                BestellStatus.builder()
                    .setStatusAenderungAm(LocalDateTime.of(2021, 01, 1, 12, 1))
                    .setStatusCode(BestellStatusCodeEnum.INITIAL)
                    .setId(new BestellStatusId(1l))
                    .build()
            ).setBestellPositionen(
                newArrayListOf(
                    BestellPosition.builder()
                        .setId(new BestellPositionId(1l))
                        .setArtikelId(new ArtikelId(1l))
                        .setStueckzahl(100)
                        .setStueckPreis(Preis.builder()
                            .setBetrag(BigDecimal.ONE)
                            .setWaehrung(WaehrungEnum.EUR)
                            .build())
                        .build(),
                    BestellPosition.builder()
                        .setId(new BestellPositionId(2l))
                        .setArtikelId(new ArtikelId(2l))
                        .setStueckzahl(10)
                        .setStueckPreis(Preis.builder()
                            .setBetrag(BigDecimal.TEN)
                            .setWaehrung(WaehrungEnum.EUR)
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
