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

package tests.shared.persistence.domain.bestellung.bv2;

import lombok.Builder;
import lombok.Getter;
import nitrox.dlc.domain.types.base.EntityBase;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class BestellPosition extends EntityBase<BestellPositionId> {

    @NotNull
    private BestellPositionId id;

    @NotNull
    private ArtikelId artikelId;

    @NotNull
    private Preis stueckPreis;

    @Positive
    private int stueckzahl;


    @Builder(setterPrefix = "set")
    public BestellPosition(BestellPositionId id,
                           long concurrencyVersion,
                           ArtikelId artikelId,
                           Preis stueckPreis,
                           int stueckzahl
    ) {
        super(concurrencyVersion);
        this.id = id;
        this.artikelId = artikelId;
        this.stueckPreis = stueckPreis;
        this.stueckzahl = stueckzahl;
    }

    public void setArtikelId(ArtikelId artikelId) {
        this.artikelId = artikelId;
    }

    public void setStueckPreis(Preis stueckPreis) {
        this.stueckPreis = stueckPreis;
    }

    public void setStueckzahl(int stueckzahl) {
        this.stueckzahl = stueckzahl;
    }

}
