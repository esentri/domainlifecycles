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

package tests.shared.complete.onlinehandel.bestellung;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.domain.types.base.EntityBase;

@Getter
public class BestellPositionBv3 extends EntityBase<BestellPositionIdBv3> {

    @NotNull
    private BestellPositionIdBv3 id;

    @NotNull
    private ArtikelIdBv3 artikelId;

    @NotNull
    private PreisBv3 stueckPreis;

    @Positive
    private int stueckzahl;


    @Builder(setterPrefix = "set")
    public BestellPositionBv3(BestellPositionIdBv3 id,
                              long concurrencyVersion,
                              ArtikelIdBv3 artikelId,
                              PreisBv3 stueckPreis,
                              int stueckzahl
    ) {
        super(concurrencyVersion);
        this.id = id;
        this.artikelId = artikelId;
        this.stueckPreis = stueckPreis;
        this.stueckzahl = stueckzahl;
    }

    public void setArtikelId(ArtikelIdBv3 artikelId) {
        this.artikelId = artikelId;
    }

    public void setStueckPreis(PreisBv3 stueckPreis) {
        this.stueckPreis = stueckPreis;
    }

    public void setStueckzahl(int stueckzahl) {
        this.stueckzahl = stueckzahl;
    }

}
