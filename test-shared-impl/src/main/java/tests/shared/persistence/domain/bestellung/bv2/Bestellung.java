/*
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

package tests.shared.persistence.domain.bestellung.bv2;

import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
public class Bestellung extends AggregateRootBase<BestellungId> {

    @NotNull
    private BestellungId id;

    @NotNull
    @DecimalMax(value = "3.0")
    @DecimalMin(value = "1.0")
    private Byte prioritaet;

    @NotNull
    private Kundennummer kundennummer;

    @NotNull
    private Lieferadresse lieferadresse;

    @NotNull
    @Size(min = 1)
    private final List<BestellPosition> bestellPositionen;

    @NotNull
    private BestellStatus bestellStatus;

    @NotNull
    private Preis gesamtPreis;

    @NotNull
    private final List<BestellKommentar> bestellKommentare;
    private List<AktionsCode> aktionsCodes;

    @Builder(setterPrefix = "set")
    public Bestellung(BestellungId id,
                      long concurrencyVersion,
                      Byte prioritaet,
                      Lieferadresse lieferadresse,
                      Kundennummer kundennummer,
                      List<BestellPosition> bestellPositionen,
                      BestellStatus bestellStatus,
                      List<BestellKommentar> bestellKommentare,
                      List<AktionsCode> aktionsCodes,
                      Preis gesamtPreis
    ) {
        super(concurrencyVersion);
        this.id = id;
        this.prioritaet = prioritaet;
        this.kundennummer = kundennummer;
        this.bestellPositionen = bestellPositionen;
        this.bestellStatus = bestellStatus;
        this.bestellKommentare = bestellKommentare;
        this.aktionsCodes = aktionsCodes;
        this.lieferadresse = lieferadresse;
        calculateGesamtPreis();

    }

    public void setPrioritaet(Byte prioritaet) {
        this.prioritaet = prioritaet;
    }

    public void setKundennummer(Kundennummer kundennummer) {
        this.kundennummer = kundennummer;
    }

    private void calculateGesamtPreis() {
        var gesamtPreisBetrag = BigDecimal.ZERO;
        var waehrung = bestellPositionen.get(0).getStueckPreis().getWaehrung();
        for (BestellPosition p : bestellPositionen) {
            gesamtPreisBetrag = gesamtPreisBetrag.add(
                p.getStueckPreis().getBetrag().multiply(BigDecimal.valueOf(p.getStueckzahl())));
        }
        this.gesamtPreis = new Preis(gesamtPreisBetrag, waehrung);
    }

    public void setBestellStatus(BestellStatus bestellStatus) {
        this.bestellStatus = bestellStatus;
    }

    public void addBestellKommentar(BestellKommentar kommentar) {
        this.bestellKommentare.add(kommentar);
    }

    public void removeBestellKommentar(BestellKommentar kommentar) {
        this.bestellKommentare.remove(kommentar);
    }

    public void addBestellPosition(BestellPosition position) {
        this.bestellPositionen.add(position);
        calculateGesamtPreis();
    }

    public void removeBestellPosition(BestellPosition position) {
        this.bestellPositionen.remove(position);
        calculateGesamtPreis();
    }

    public void setLieferadresse(Lieferadresse lieferadresse) {
        this.lieferadresse = lieferadresse;
    }

    @Override
    public void validate() throws DomainAssertionException {
        BestellPosition erstePosition = bestellPositionen.get(0);
        long positionenMitGleicherWaehrung = bestellPositionen.stream()
            .map(p -> p.getStueckPreis())
            .filter(p -> erstePosition.getStueckPreis().getWaehrung().equals(p.getWaehrung()))
            .count();
        DomainAssertions.isTrue(positionenMitGleicherWaehrung == bestellPositionen.size(),
            "Alle Bestellpositionen müssen dieselbe Währung haben!");
    }

    public void setAktionsCodes(List<AktionsCode> aktionsCodes) {
        this.aktionsCodes = aktionsCodes;
    }

}
