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
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.domain.types.base.EntityBase;

@Getter
public class LieferadresseBv3 extends EntityBase<LieferadresseIdBv3> {

    @NotNull
    private LieferadresseIdBv3 id;

    @NotNull
    @Size(max = 200)
    private String name;

    @NotNull
    @Size(max = 200)
    private String strasse;

    @NotNull
    @Size(max = 200)
    private String postleitzahl;

    @NotNull
    @Size(max = 200)
    private String ort;

    @Builder(setterPrefix = "set")
    public LieferadresseBv3(LieferadresseIdBv3 id,
                            long concurrencyVersion,
                            String name,
                            String strasse,
                            String postleitzahl,
                            String ort
    ) {
        super(concurrencyVersion);
        this.id = id;
        this.name = name;
        this.strasse = strasse;
        this.postleitzahl = postleitzahl;
        this.ort = ort;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

}
