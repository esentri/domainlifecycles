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

package tests.shared.complete.onlinehandel.bestellung;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import tests.shared.complete.onlinehandel.zustellung.AuslieferungGestartet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BestellungBv3 extends AggregateRootBase<BestellungIdBv3> {

    @NotNull
    private BestellungIdBv3 id;

    @NotNull
    @DecimalMax(value = "3.0")
    @DecimalMin(value = "1.0")
    private Byte prioritaet;

    @NotNull
    private KundennummerBv3 kundennummer;

    @NotNull
    private LieferadresseBv3 lieferadresse;

    @NotNull
    @Size(min = 1)
    private final List<BestellPositionBv3> bestellPositionen;

    @NotNull
    private BestellStatusBv3 bestellStatus;

    @NotNull
    private PreisBv3 gesamtPreis;

    @NotNull
    private final List<BestellKommentarBv3> bestellKommentare;
    private List<AktionsCodeBv3> aktionsCodes;

    @Builder(setterPrefix = "set")
    public BestellungBv3(BestellungIdBv3 id,
                         long concurrencyVersion,
                         Byte prioritaet,
                         LieferadresseBv3 lieferadresse,
                         KundennummerBv3 kundennummer,
                         List<BestellPositionBv3> bestellPositionen,
                         BestellStatusBv3 bestellStatus,
                         List<BestellKommentarBv3> bestellKommentare,
                         List<AktionsCodeBv3> aktionsCodes,
                         PreisBv3 gesamtPreis
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

    public void setKundennummer(KundennummerBv3 kundennummer) {
        this.kundennummer = kundennummer;
    }

    private void calculateGesamtPreis() {
        var gesamtPreisBetrag = BigDecimal.ZERO;
        var waehrung = bestellPositionen.get(0).getStueckPreis().waehrung();
        for (BestellPositionBv3 p : bestellPositionen) {
            gesamtPreisBetrag = gesamtPreisBetrag.add(
                p.getStueckPreis().betrag().multiply(BigDecimal.valueOf(p.getStueckzahl())));
        }
        this.gesamtPreis = new PreisBv3(waehrung, gesamtPreisBetrag);
    }

    public void setBestellStatus(BestellStatusBv3 bestellStatus) {
        this.bestellStatus = bestellStatus;
    }

    public void addBestellKommentar(BestellKommentarBv3 kommentar) {
        this.bestellKommentare.add(kommentar);
    }

    public void removeBestellKommentar(BestellKommentarBv3 kommentar) {
        this.bestellKommentare.remove(kommentar);
    }

    public void addBestellPosition(BestellPositionBv3 position) {
        this.bestellPositionen.add(position);
        calculateGesamtPreis();
    }

    public void removeBestellPosition(BestellPositionBv3 position) {
        this.bestellPositionen.remove(position);
        calculateGesamtPreis();
    }

    public void setLieferadresse(LieferadresseBv3 lieferadresse) {
        this.lieferadresse = lieferadresse;
    }

    @Override
    public void validate() throws DomainAssertionException {
        BestellPositionBv3 erstePosition = bestellPositionen.get(0);
        long positionenMitGleicherWaehrung = bestellPositionen.stream()
            .map(p -> p.getStueckPreis())
            .filter(p -> erstePosition.getStueckPreis().waehrung().equals(p.waehrung()))
            .count();
        DomainAssertions.isTrue(positionenMitGleicherWaehrung == bestellPositionen.size(),
            "Alle Bestellpositionen müssen dieselbe Währung haben!");
    }

    public void setAktionsCodes(List<AktionsCodeBv3> aktionsCodes) {
        this.aktionsCodes = aktionsCodes;
    }

    @Publishes(domainEventTypes = AuslieferungGestartet.class)
    public void starteLieferung() {
        getBestellStatus().setStatusAenderungAm(LocalDateTime.now());
        getBestellStatus().setStatusCode(BestellStatusCodeEnumBv3.ZUSTELLUNG_LAEUFT);
        AuslieferungGestartet event = new AuslieferungGestartet(this, false);
        //Domain.publish(event);
    }

}
