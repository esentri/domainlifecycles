package io.domainlifecycles.jooq.persistence.tests.bestellung;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.complete.onlinehandel.bestellung.AktionsCodeBv3;
import tests.shared.complete.onlinehandel.bestellung.ArtikelIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellKommentarBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellKommentarIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusCodeEnumBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungIdBv3;
import tests.shared.complete.onlinehandel.bestellung.PreisBv3;
import tests.shared.complete.onlinehandel.bestellung.WaehrungEnumBv3;


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
public class BestellungRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BestellungRepository_ITest.class);

    private static BestellungRepository bestellungRepository;

    @BeforeAll
    public void init() {

        bestellungRepository = new BestellungRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider);
    }

    @Test
    public void testInsert() {
        //given
        BestellungBv3 b = TestDataGenerator.buildBestellungBv3();
        BestellungBv3 copy = persistenceEventTestHelper.kryo.copy(b);

        //when
        BestellungBv3 inserted = bestellungRepository.insert(copy);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(inserted, found.get());

        log.debug("Neue Bestellung: \n" + found);
    }

    @Test
    public void testUpdateStatus() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        BestellungBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.getBestellStatus().setStatusCode(BestellStatusCodeEnumBv3.ZUSTELLUNG_LAEUFT);
        BestellungBv3 updated = bestellungRepository.update(insertedCopy);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung: \n" + found);
    }

    @Test
    public void testUpdateAddAktionsCodes() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        BestellungBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.setAktionsCodes(TestDataGenerator.newArrayListOf(
            AktionsCodeBv3.builder().setValue("ABC").build(),
            AktionsCodeBv3.builder().setValue("DEF").build()
        ));
        BestellungBv3 updated = bestellungRepository.update(insertedCopy);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        Assertions.assertThat(found.get().getAktionsCodes()).isNotEmpty();
        Assertions.assertThat(found.get().getAktionsCodes().size()).isEqualTo(2);

        log.debug("Bestellung mit Status in Zustellung: \n" + found);
    }

    @Test
    public void testUpdateStatusUndPrio() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        BestellungBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.getBestellStatus().setStatusCode(BestellStatusCodeEnumBv3.ZUSTELLUNG_LAEUFT);
        insertedCopy.setPrioritaet(Byte.valueOf("3"));
        BestellungBv3 updated = bestellungRepository.update(insertedCopy);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung und Prio 3: \n" + found);
    }

    @Test
    public void testDeleteBestellpositionAddKommentar() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        BestellungBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.removeBestellPosition(insertedCopy.getBestellPositionen().get(0));
        insertedCopy.getBestellKommentare().add(
            BestellKommentarBv3.builder()
                .setKommentarText("Den Scheiß will ich doch nicht!")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new BestellKommentarIdBv3(3l))
                .build()
        );
        BestellungBv3 updated = bestellungRepository.update(insertedCopy);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit nur 1 Position: \n" + found);

    }

    @Test
    public void testUpdateComplexScenarioUniqueConstraint() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        BestellungBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.removeBestellPosition(insertedCopy.getBestellPositionen().get(0));
        insertedCopy.getBestellKommentare().add(
            BestellKommentarBv3.builder()
                .setKommentarText("Den Scheiß will ich doch nicht!")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new BestellKommentarIdBv3(3l))
                .build()
        );
        insertedCopy.addBestellPosition(
            BestellPositionBv3.builder()
                .setId(new BestellPositionIdBv3(3l))
                .setStueckPreis(PreisBv3.builder()
                    .setBetrag(BigDecimal.ONE)
                    .setWaehrung(WaehrungEnumBv3.EUR)
                    .build())
                .setStueckzahl(50)
                .setArtikelId(new ArtikelIdBv3(1l))
                .build()
        );
        insertedCopy.getBestellKommentare().add(
            BestellKommentarBv3.builder()
                .setKommentarText("Ne ich nehm's doch, aber nur die Hälfte! (Weil's jetzt billiger ist....hähähähä)")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 3))
                .setId(new BestellKommentarIdBv3(4l))
                .build()
        );
        BestellungBv3 updated = bestellungRepository.update(insertedCopy);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit nur 2 Position und anderem Preis (billiger): \n" + found);
    }

    @Test
    public void testUpdateComplexScenarioUniqueConstraintFail() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        BestellungBv3 insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.getBestellPositionen().add(
            BestellPositionBv3.builder()
                .setId(new BestellPositionIdBv3(3l))
                .setStueckPreis(PreisBv3.builder()
                    .setBetrag(BigDecimal.ONE)
                    .setWaehrung(WaehrungEnumBv3.EUR)
                    .build())
                .setStueckzahl(50)
                .setArtikelId(new ArtikelIdBv3(1l))
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
                BestellungBv3 updated = bestellungRepository.update(insertedCopy);
            }
        });
    }

    @Test
    public void testDelete() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        Optional<BestellungBv3> foundAfter = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        Assertions.assertThat(foundAfter).isPresent();
        //when
        bestellungRepository.deleteById(new BestellungIdBv3(1l));

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void testFetchById() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        //when
        Optional<BestellungBv3> foundAfter = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        //then
        Assertions.assertThat(foundAfter).isPresent();

        assertResultBestellung(inserted, foundAfter.get());
    }

    @Test
    public void testFetchByStatus() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        Optional<BestellungBv3> foundAfter = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        Assertions.assertThat(foundAfter).isPresent();
        //when
        List<BestellungBv3> bestaetigteBestellungen = bestellungRepository.findByStatusCode(
            BestellStatusCodeEnumBv3.BESTAETIGT);
        //then
        Assertions.assertThat(bestaetigteBestellungen).isEmpty();
        //when
        List<BestellungBv3> initialeBestellungen = bestellungRepository.findByStatusCode(BestellStatusCodeEnumBv3.INITIAL);
        //then
        Assertions.assertThat(initialeBestellungen).isNotEmpty();

        assertResultBestellung(initialeBestellungen.get(0), foundAfter.get());
    }

    @Test
    public void testFetchAll() {
        //given
        List<BestellungBv3> bestellungen = TestDataGenerator.buildManyBestellungenBv3();
        bestellungen.stream().forEach(b -> bestellungRepository.insert(b));

        //when
        List<BestellungBv3> found = bestellungRepository.findAllBestellungen();

        //then
        assertThat(bestellungen.size() == found.size());
        Assertions.assertThat(bestellungen)
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
        List<BestellungBv3> bestellungen = TestDataGenerator.buildManyBestellungenBv3();
        bestellungen.stream().forEach(b -> bestellungRepository.insert(b));


        int pageSize = 3;
        int currentOffset = 0;
        while (currentOffset < bestellungen.size()) {
            int offsetInLoop = currentOffset;
            //when
            List<BestellungBv3> found = bestellungRepository.findBestellungenPaged(currentOffset, pageSize);

            //then
            assertThat(found.size() <= pageSize);
            Assertions.assertThat(bestellungen
                    .stream()
                    .filter(b -> bestellungen.indexOf(b) >= offsetInLoop
                        && bestellungen.indexOf(b) < (offsetInLoop + pageSize))
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
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        Optional<BestellungBv3> foundAfter = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        Assertions.assertThat(foundAfter).isPresent();
        assertResultBestellung(inserted, foundAfter.get());

        Assertions.assertThat(foundAfter
            .get()
            .getBestellPositionen().size()).isEqualTo(2);

        //when

        //Die Subquery ergänzt in den Bestellungen nur Positionen mit ArtikelId 1
        //Nicht fachlich sinnvoll -> nur Demozwecke
        Optional<BestellungBv3> foundSubquery = bestellungRepository.findWithSubquery(new BestellungIdBv3(1l));

        //then
        Assertions.assertThat(foundSubquery).isPresent();
        Assertions.assertThat(foundSubquery.get().getId()).isEqualTo(foundAfter.get().getId());
        Assertions.assertThat(foundSubquery
            .get()
            .getBestellPositionen()
            .size()
        ).isEqualTo(1);
        Assertions.assertThat(foundSubquery
            .get()
            .getBestellPositionen()
            .get(0)
            .getArtikelId()
            .value()
        ).isEqualTo(1);
    }

    @Test
    public void testFetchOptimized() {
        //given
        List<BestellungBv3> bestellungen = TestDataGenerator.buildManyBestellungenBv3();
        bestellungen.stream().forEach(b -> bestellungRepository.insert(b));

        int pageSize = 3;
        int currentOffset = 0;

        //when
        List<BestellungBv3> found = bestellungRepository
            .findBestellungenOptimized(currentOffset, pageSize)
            .collect(Collectors.toList());

        //then
        Assertions.assertThat(found).hasSize(3);
    }

    private void assertResultBestellung(BestellungBv3 result, BestellungBv3 found) {
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
