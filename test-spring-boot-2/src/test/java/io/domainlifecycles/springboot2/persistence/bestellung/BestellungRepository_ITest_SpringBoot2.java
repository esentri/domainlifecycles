package io.domainlifecycles.springboot2.persistence.bestellung;

import io.domainlifecycles.springboot2.persistence.base.SpringBoot2TestEventListener;
import org.assertj.core.api.Assertions;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.bestellung.bv2.AktionsCode;
import tests.shared.persistence.domain.bestellung.bv2.ArtikelId;
import tests.shared.persistence.domain.bestellung.bv2.BestellKommentar;
import tests.shared.persistence.domain.bestellung.bv2.BestellKommentarId;
import tests.shared.persistence.domain.bestellung.bv2.BestellPosition;
import tests.shared.persistence.domain.bestellung.bv2.BestellPositionId;
import tests.shared.persistence.domain.bestellung.bv2.BestellStatusCodeEnum;
import tests.shared.persistence.domain.bestellung.bv2.Bestellung;
import tests.shared.persistence.domain.bestellung.bv2.BestellungId;
import tests.shared.persistence.domain.bestellung.bv2.Preis;
import tests.shared.persistence.domain.bestellung.bv2.WaehrungEnum;

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
@ActiveProfiles({"test"})
public class BestellungRepository_ITest_SpringBoot2 {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BestellungRepository_ITest_SpringBoot2.class);
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BestellungRepository bestellungRepository;

    @Autowired
    private SpringBoot2TestEventListener springTestEventListener;


    @Transactional
    @Test
    public void testInsert() {
        //given
        Bestellung b = TestDataGenerator.buildBestellung();

        //when
        Bestellung inserted = bestellungRepository.insert(b);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(inserted, found.get());

        log.debug("Neue Bestellung: \n" + found);
    }

    @Transactional
    @Test
    public void testUpdateStatus() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());

        //when
        inserted.getBestellStatus().setStatusCode(BestellStatusCodeEnum.ZUSTELLUNG_LAEUFT);
        Bestellung updated = bestellungRepository.update(inserted);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung: \n" + found);
    }

    @Transactional
    @Test
    public void testUpdateAddAktionsCodes() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());

        //when
        inserted.setAktionsCodes(TestDataGenerator.newArrayListOf(
            AktionsCode.builder().setValue("ABC").build(),
            AktionsCode.builder().setValue("DEF").build()
        ));
        Bestellung updated = bestellungRepository.update(inserted);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
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
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());

        //when
        inserted.getBestellStatus().setStatusCode(BestellStatusCodeEnum.ZUSTELLUNG_LAEUFT);
        inserted.setPrioritaet(Byte.valueOf("3"));
        Bestellung updated = bestellungRepository.update(inserted);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung und Prio 3: \n" + found);
    }

    @Transactional
    @Test
    public void testDeleteBestellpositionAddKommentar() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());

        //when
        inserted.removeBestellPosition(inserted.getBestellPositionen().get(0));
        inserted.getBestellKommentare().add(
            BestellKommentar.builder()
                .setKommentarText("Den Scheiß will ich doch nicht!")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new BestellKommentarId(3l))
                .build()
        );
        Bestellung updated = bestellungRepository.update(inserted);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit nur 1 Position: \n" + found);

    }

    @Transactional
    @Test
    public void testUpdateComplexScenarioUniqueConstraint() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());

        //when
        inserted.removeBestellPosition(inserted.getBestellPositionen().get(0));
        inserted.getBestellKommentare().add(
            BestellKommentar.builder()
                .setKommentarText("Den Scheiß will ich doch nicht!")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new BestellKommentarId(3l))
                .build()
        );
        inserted.addBestellPosition(
            BestellPosition.builder()
                .setId(new BestellPositionId(3l))
                .setStueckPreis(Preis.builder()
                    .setBetrag(BigDecimal.ONE)
                    .setWaehrung(WaehrungEnum.EUR)
                    .build())
                .setStueckzahl(50)
                .setArtikelId(new ArtikelId(1l))
                .build()
        );
        inserted.getBestellKommentare().add(
            BestellKommentar.builder()
                .setKommentarText("Ne ich nehm's doch, aber nur die Hälfte! (Weil's jetzt billiger ist....hähähähä)")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 3))
                .setId(new BestellKommentarId(4l))
                .build()
        );
        Bestellung updated = bestellungRepository.update(inserted);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit nur 2 Position und anderem Preis (billiger): \n" + found);
    }

    @Transactional
    @Test
    public void testUpdateComplexScenarioUniqueConstraintFail() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());

        //when
        inserted.getBestellPositionen().add(
            BestellPosition.builder()
                .setId(new BestellPositionId(3l))
                .setStueckPreis(Preis.builder()
                    .setBetrag(BigDecimal.ONE)
                    .setWaehrung(WaehrungEnum.EUR)
                    .build())
                .setStueckzahl(50)
                .setArtikelId(new ArtikelId(1l))
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
                Bestellung updated = bestellungRepository.update(inserted);
            }
        });
    }

    @Test
    @Transactional
    public void testDelete() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Optional<Bestellung> foundAfter = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        assertThat(foundAfter).isPresent();
        //when
        bestellungRepository.deleteById(new BestellungId(1l));

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        assertThat(found).isEmpty();
    }

    @Test
    @Transactional
    public void testFetchById() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        //when
        Optional<Bestellung> foundAfter = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        //then
        assertThat(foundAfter).isPresent();

        assertResultBestellung(inserted, foundAfter.get());
    }

    @Test
    @Transactional
    public void testFetchByStatus() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Optional<Bestellung> foundAfter = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        assertThat(foundAfter).isPresent();
        //when
        List<Bestellung> bestaetigteBestellungen = bestellungRepository.findByStatusCode(
            BestellStatusCodeEnum.BESTAETIGT);
        //then
        assertThat(bestaetigteBestellungen).isEmpty();
        //when
        List<Bestellung> initialeBestellungen = bestellungRepository.findByStatusCode(BestellStatusCodeEnum.INITIAL);
        //then
        assertThat(initialeBestellungen).isNotEmpty();

        assertResultBestellung(initialeBestellungen.get(0), foundAfter.get());
    }

    @Test
    @Transactional
    public void testFetchAll() {
        //given
        List<Bestellung> bestellungen = TestDataGenerator.buildManyBestellungen();
        bestellungen.stream().forEach(b -> bestellungRepository.insert(b));

        //when
        List<Bestellung> found = bestellungRepository.findAllBestellungen();

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
        List<Bestellung> bestellungen = TestDataGenerator.buildManyBestellungen();
        bestellungen.stream().forEach(b -> bestellungRepository.insert(b));


        int pageSize = 3;
        int currentOffset = 0;
        while (currentOffset < bestellungen.size()) {
            int offsetInLoop = currentOffset;
            //when
            List<Bestellung> found = bestellungRepository.findBestellungenPaged(currentOffset, pageSize);

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
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Optional<Bestellung> foundAfter = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        assertThat(foundAfter).isPresent();
        assertResultBestellung(inserted, foundAfter.get());

        assertThat(foundAfter
            .get()
            .getBestellPositionen().size()).isEqualTo(2);

        //when

        //Die Subquery ergänzt in den Bestellungen nur Positionen mit ArtikelId 1
        //Nicht fachlich sinnvoll -> nur Demozwecke
        Optional<Bestellung> foundSubquery = bestellungRepository.findWithSubquery(new BestellungId(1l));

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
        List<Bestellung> bestellungen = TestDataGenerator.buildManyBestellungen();
        bestellungen.stream().forEach(b -> bestellungRepository.insert(b));

        int pageSize = 3;
        int currentOffset = 0;

        //when
        List<Bestellung> found = bestellungRepository
            .findBestellungenOptimized(currentOffset, pageSize)
            .collect(Collectors.toList());

        //then
        assertThat(found).hasSize(3);
    }

    private void assertResultBestellung(Bestellung result, Bestellung found) {
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
