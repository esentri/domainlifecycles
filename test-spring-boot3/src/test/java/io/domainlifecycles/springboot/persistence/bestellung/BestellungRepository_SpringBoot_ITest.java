package io.domainlifecycles.springboot.persistence.bestellung;

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

@SpringBootTest
@Import(PersistenceConfig.class)
@ActiveProfiles({"test"})
public class BestellungRepository_SpringBoot_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BestellungRepository_SpringBoot_ITest.class);

    @Autowired
    private BestellungBv3Repository bestellungRepository;

    @Autowired
    private SpringTestEventListener springTestEventListener;


    @Transactional
    @Test
    public void testInsert() {
        //given
        BestellungBv3 b = TestDataGenerator.buildBestellungBv3();

        //when
        BestellungBv3 inserted = bestellungRepository.insert(b);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(inserted, found.get());

        log.debug("Neue Bestellung: \n" + found);
    }

    @Transactional
    @Test
    public void testUpdateStatus() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());

        //when
        inserted.getBestellStatus().setStatusCode(BestellStatusCodeEnumBv3.ZUSTELLUNG_LAEUFT);
        BestellungBv3 updated = bestellungRepository.update(inserted);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung: \n" + found);
    }

    @Transactional
    @Test
    public void testUpdateAddAktionsCodes() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());

        //when
        inserted.setAktionsCodes(TestDataGenerator.newArrayListOf(
            AktionsCodeBv3.builder().setValue("ABC").build(),
            AktionsCodeBv3.builder().setValue("DEF").build()
        ));
        BestellungBv3 updated = bestellungRepository.update(inserted);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        assertThat(found.get().getAktionsCodes()).isNotEmpty();
        assertThat(found.get().getAktionsCodes().size()).isEqualTo(2);

        log.debug("Bestellung mit Status in Zustellung: \n" + found);
    }


    @Transactional
    @Test
    public void testUpdateStatusUndPrio() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());

        //when
        inserted.getBestellStatus().setStatusCode(BestellStatusCodeEnumBv3.ZUSTELLUNG_LAEUFT);
        inserted.setPrioritaet(Byte.valueOf("3"));
        BestellungBv3 updated = bestellungRepository.update(inserted);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung und Prio 3: \n" + found);
    }

    @Transactional
    @Test
    public void testDeleteBestellpositionAddKommentar() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());

        //when
        inserted.removeBestellPosition(inserted.getBestellPositionen().get(0));
        inserted.getBestellKommentare().add(
            BestellKommentarBv3.builder()
                .setKommentarText("Den Scheiß will ich doch nicht!")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new BestellKommentarIdBv3(3l))
                .build()
        );
        BestellungBv3 updated = bestellungRepository.update(inserted);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit nur 1 Position: \n" + found);

    }

    @Transactional
    @Test
    public void testUpdateComplexScenarioUniqueConstraint() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());

        //when
        inserted.removeBestellPosition(inserted.getBestellPositionen().get(0));
        inserted.getBestellKommentare().add(
            BestellKommentarBv3.builder()
                .setKommentarText("Den Scheiß will ich doch nicht!")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new BestellKommentarIdBv3(3l))
                .build()
        );
        inserted.addBestellPosition(
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
        inserted.getBestellKommentare().add(
            BestellKommentarBv3.builder()
                .setKommentarText("Ne ich nehm's doch, aber nur die Hälfte! (Weil's jetzt billiger ist....hähähähä)")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 3))
                .setId(new BestellKommentarIdBv3(4l))
                .build()
        );
        BestellungBv3 updated = bestellungRepository.update(inserted);

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit nur 2 Position und anderem Preis (billiger): \n" + found);
    }

    @Transactional
    @Test
    public void testUpdateComplexScenarioUniqueConstraintFail() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());

        //when
        inserted.getBestellPositionen().add(
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
        assertThrows(IntegrityConstraintViolationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                BestellungBv3 updated = bestellungRepository.update(inserted);
            }
        });
    }

    @Test
    @Transactional
    public void testDelete() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        Optional<BestellungBv3> foundAfter = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        assertThat(foundAfter).isPresent();
        //when
        bestellungRepository.deleteById(new BestellungIdBv3(1l));

        //then
        Optional<BestellungBv3> found = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        assertThat(found).isEmpty();
    }

    @Test
    @Transactional
    public void testFetchById() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        //when
        Optional<BestellungBv3> foundAfter = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        //then
        assertThat(foundAfter).isPresent();

        assertResultBestellung(inserted, foundAfter.get());
    }

    @Test
    @Transactional
    public void testFetchByStatus() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        Optional<BestellungBv3> foundAfter = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        assertThat(foundAfter).isPresent();
        //when
        List<BestellungBv3> bestaetigteBestellungen = bestellungRepository.findByStatusCode(
            BestellStatusCodeEnumBv3.BESTAETIGT);
        //then
        assertThat(bestaetigteBestellungen).isEmpty();
        //when
        List<BestellungBv3> initialeBestellungen = bestellungRepository.findByStatusCode(
            BestellStatusCodeEnumBv3.INITIAL);
        //then
        assertThat(initialeBestellungen).isNotEmpty();

        assertResultBestellung(initialeBestellungen.get(0), foundAfter.get());
    }

    @Test
    @Transactional
    public void testFetchAll() {
        //given
        List<BestellungBv3> bestellungen = TestDataGenerator.buildManyBestellungenBv3();
        bestellungen.stream().forEach(b -> bestellungRepository.insert(b));

        //when
        List<BestellungBv3> found = bestellungRepository.findAllBestellungen();

        //then
        assertThat(bestellungen.size() == found.size());
        assertThat(bestellungen)
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
            assertThat(bestellungen
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
    @Transactional
    public void testFetchCustomSubquery() {
        //given
        BestellungBv3 inserted = bestellungRepository.insert(TestDataGenerator.buildBestellungBv3());
        Optional<BestellungBv3> foundAfter = bestellungRepository.findResultById(new BestellungIdBv3(1l)).resultValue();
        assertThat(foundAfter).isPresent();
        assertResultBestellung(inserted, foundAfter.get());

        assertThat(foundAfter
            .get()
            .getBestellPositionen().size()).isEqualTo(2);

        //when

        //Die Subquery ergänzt in den Bestellungen nur Positionen mit ArtikelId 1
        //Nicht fachlich sinnvoll -> nur Demozwecke
        Optional<BestellungBv3> foundSubquery = bestellungRepository.findWithSubquery(new BestellungIdBv3(1l));

        //then
        assertThat(foundSubquery).isPresent();
        Assertions.assertThat(foundSubquery.get().getId()).isEqualTo(foundAfter.get().getId());
        assertThat(foundSubquery
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
    @Transactional
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
        assertThat(found).hasSize(3);
    }

    private void assertResultBestellung(BestellungBv3 result, BestellungBv3 found) {
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
