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
        BestellungBv3 a = buildBestellung();
        BestellungBv3 b = buildBestellung();
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
        BestellungBv3 a = buildBestellung();
        BestellungBv3 b = buildBestellung();
        b.setKundennummer(new KundennummerBv3("88888"));

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
        BestellungBv3 a = buildBestellung();
        BestellungBv3 b = buildBestellung();
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
        BestellungBv3 a = buildBestellung();
        BestellungBv3 b = buildBestellung();
        PersistenceAction<?> action = new PersistenceAction<>(
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(b), PersistenceAction.ActionType.UPDATE,
            persistenceConfiguration.domainPersistenceProvider.buildAccessModel(a));
        Set<String> changes = calculateChangedProperties(action);
        assertThat(changes).isEmpty();
    }

    @Test
    public void testChangeComplexVo() {
        BestellungBv3 a = buildBestellung();
        BestellungBv3 b = buildBestellung();
        b.getBestellPositionen().get(0).setStueckPreis(PreisBv3
            .builder()
            .setBetrag(BigDecimal.valueOf(44))
            .setWaehrung(WaehrungEnumBv3.EUR)
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

    private BestellungBv3 buildBestellung() {
        BestellungBv3 b = BestellungBv3.builder()
            .setId(new BestellungIdBv3(1l))
            .setKundennummer(new KundennummerBv3("777777"))
            .setPrioritaet(Byte.valueOf("1"))
            .setLieferadresse(
                LieferadresseBv3.builder()
                    .setId(new LieferadresseIdBv3(1l))
                    .setName("Thor")
                    .setOrt("Donnerberg")
                    .setPostleitzahl("77777")
                    .setStrasse("Hammerallee 7")
                    .build()
            )
            .setBestellKommentare(newArrayListOf(
                BestellKommentarBv3.builder()
                    .setId(new BestellKommentarIdBv3(1l))
                    .setKommentarAm(LocalDateTime.of(2021, 01, 1, 12, 0))
                    .setKommentarText("Mach schnell sonst kommt der Hammer!")
                    .build(),
                BestellKommentarBv3.builder()
                    .setId(new BestellKommentarIdBv3(2l))
                    .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 0))
                    .setKommentarText("Der Donnergott grüßt!")
                    .build()
            ))
            .setBestellStatus(
                BestellStatusBv3.builder()
                    .setStatusAenderungAm(LocalDateTime.of(2021, 01, 1, 12, 1))
                    .setStatusCode(BestellStatusCodeEnumBv3.INITIAL)
                    .setId(new BestellStatusIdBv3(1l))
                    .build()
            ).setBestellPositionen(
                newArrayListOf(
                    BestellPositionBv3.builder()
                        .setId(new BestellPositionIdBv3(1l))
                        .setArtikelId(new ArtikelIdBv3(1l))
                        .setStueckzahl(100)
                        .setStueckPreis(PreisBv3.builder()
                            .setBetrag(BigDecimal.ONE)
                            .setWaehrung(WaehrungEnumBv3.EUR)
                            .build())
                        .build(),
                    BestellPositionBv3.builder()
                        .setId(new BestellPositionIdBv3(2l))
                        .setArtikelId(new ArtikelIdBv3(2l))
                        .setStueckzahl(10)
                        .setStueckPreis(PreisBv3.builder()
                            .setBetrag(BigDecimal.TEN)
                            .setWaehrung(WaehrungEnumBv3.EUR)
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
