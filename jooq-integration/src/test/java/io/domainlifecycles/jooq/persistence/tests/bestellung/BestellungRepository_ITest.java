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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BestellungRepository_ITest extends BasePersistence_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BestellungRepository_ITest.class);

    private static BestellungRepository bestellungRepository;

    @BeforeAll
    public void init(){

        bestellungRepository = new BestellungRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider);
    }

    @Test
    public void testInsert() {
        //given
        Bestellung b = TestDataGenerator.buildBestellung();
        Bestellung copy = persistenceEventTestHelper.kryo.copy(b);

        //when
        Bestellung inserted = bestellungRepository.insert(copy);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(inserted, found.get());

        log.debug("Neue Bestellung: \n" + found);
    }

    @Test
    public void testUpdateStatus() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Bestellung insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.getBestellStatus().setStatusCode(BestellStatusCodeEnum.ZUSTELLUNG_LAEUFT);
        Bestellung updated = bestellungRepository.update(insertedCopy);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung: \n" + found);
    }

    @Test
    public void testUpdateAddAktionsCodes() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Bestellung insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.setAktionsCodes(TestDataGenerator.newArrayListOf(
            AktionsCode.builder().setValue("ABC").build(),
            AktionsCode.builder().setValue("DEF").build()
        ));
        Bestellung updated = bestellungRepository.update(insertedCopy);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
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
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Bestellung insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.getBestellStatus().setStatusCode(BestellStatusCodeEnum.ZUSTELLUNG_LAEUFT);
        insertedCopy.setPrioritaet(Byte.valueOf("3"));
        Bestellung updated = bestellungRepository.update(insertedCopy);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit Status in Zustellung und Prio 3: \n" + found);
    }

    @Test
    public void testDeleteBestellpositionAddKommentar() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Bestellung insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.removeBestellPosition(insertedCopy.getBestellPositionen().get(0));
        insertedCopy.getBestellKommentare().add(
            BestellKommentar.builder()
                .setKommentarText("Den Scheiß will ich doch nicht!")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new BestellKommentarId(3l))
                .build()
        );
        Bestellung updated = bestellungRepository.update(insertedCopy);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit nur 1 Position: \n" + found);

    }

    @Test
    public void testUpdateComplexScenarioUniqueConstraint() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Bestellung insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.removeBestellPosition(insertedCopy.getBestellPositionen().get(0));
        insertedCopy.getBestellKommentare().add(
            BestellKommentar.builder()
                .setKommentarText("Den Scheiß will ich doch nicht!")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 2))
                .setId(new BestellKommentarId(3l))
                .build()
        );
        insertedCopy.addBestellPosition(
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
        insertedCopy.getBestellKommentare().add(
            BestellKommentar.builder()
                .setKommentarText("Ne ich nehm's doch, aber nur die Hälfte! (Weil's jetzt billiger ist....hähähähä)")
                .setKommentarAm(LocalDateTime.of(2021, 01, 2, 12, 3))
                .setId(new BestellKommentarId(4l))
                .build()
        );
        Bestellung updated = bestellungRepository.update(insertedCopy);

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        Assertions.assertThat(updated.concurrencyVersion()).isGreaterThan(inserted.concurrencyVersion());
        Assertions.assertThat(found).isPresent();
        assertResultBestellung(updated, found.get());

        log.debug("Bestellung mit nur 2 Position und anderem Preis (billiger): \n" + found);
    }

    @Test
    public void testUpdateComplexScenarioUniqueConstraintFail() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Bestellung insertedCopy = persistenceEventTestHelper.kryo.copy(inserted);

        //when
        insertedCopy.getBestellPositionen().add(
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
        //ATTENTION: Do not write the assertion with a lambda expression, that will create a class loading conflict with our
        // byte buddy extension
        DataAccessException ex = assertThrows(DataAccessException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Bestellung updated = bestellungRepository.update(insertedCopy);
            }
        });
    }

    @Test
    public void testDelete() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Optional<Bestellung> foundAfter = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        Assertions.assertThat(foundAfter).isPresent();
        //when
        bestellungRepository.deleteById(new BestellungId(1l));

        //then
        Optional<Bestellung> found = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void testFetchById() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        //when
        Optional<Bestellung> foundAfter = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        //then
        Assertions.assertThat(foundAfter).isPresent();

        assertResultBestellung(inserted, foundAfter.get());
    }

    @Test
    public void testFetchByStatus() {
        //given
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Optional<Bestellung> foundAfter = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        Assertions.assertThat(foundAfter).isPresent();
        //when
        List<Bestellung> bestaetigteBestellungen = bestellungRepository.findByStatusCode(BestellStatusCodeEnum.BESTAETIGT);
        //then
        Assertions.assertThat(bestaetigteBestellungen).isEmpty();
        //when
        List<Bestellung> initialeBestellungen = bestellungRepository.findByStatusCode(BestellStatusCodeEnum.INITIAL);
        //then
        Assertions.assertThat(initialeBestellungen).isNotEmpty();

        assertResultBestellung(initialeBestellungen.get(0), foundAfter.get());
    }

    @Test
    public void testFetchAll() {
        //given
        List<Bestellung> bestellungen = TestDataGenerator.buildManyBestellungen();
        bestellungen.stream().forEach(b -> bestellungRepository.insert(b));

        //when
        List<Bestellung> found = bestellungRepository.findAllBestellungen();

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
        Bestellung inserted = bestellungRepository.insert(TestDataGenerator.buildBestellung());
        Optional<Bestellung> foundAfter = bestellungRepository.findResultById(new BestellungId(1l)).resultValue();
        Assertions.assertThat(foundAfter).isPresent();
        assertResultBestellung(inserted, foundAfter.get());

        Assertions.assertThat(foundAfter
            .get()
            .getBestellPositionen().size()).isEqualTo(2);

        //when

        //Die Subquery ergänzt in den Bestellungen nur Positionen mit ArtikelId 1
        //Nicht fachlich sinnvoll -> nur Demozwecke
        Optional<Bestellung> foundSubquery = bestellungRepository.findWithSubquery(new BestellungId(1l));

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
        List<Bestellung> bestellungen = TestDataGenerator.buildManyBestellungen();
        bestellungen.stream().forEach(b -> bestellungRepository.insert(b));

        int pageSize = 3;
        int currentOffset = 0;

        //when
        List<Bestellung> found = bestellungRepository
            .findBestellungenOptimized(currentOffset, pageSize)
            .collect(Collectors.toList());

        //then
        Assertions.assertThat(found).hasSize(3);
    }

    private void assertResultBestellung(Bestellung result, Bestellung found) {
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
