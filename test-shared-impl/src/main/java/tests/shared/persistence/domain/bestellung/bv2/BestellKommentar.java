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

import io.domainlifecycles.domain.types.base.EntityBase;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Getter
public class BestellKommentar extends EntityBase<BestellKommentarId> {

    @NotNull
    private final BestellKommentarId id;

    @Size(max = 1000)
    @NotEmpty
    private String kommentarText;

    @NotNull
    @Past
    private LocalDateTime kommentarAm;


    @Builder(setterPrefix = "set")
    public BestellKommentar(BestellKommentarId id,
                            long concurrencyVersion,
                            String kommentarText,
                            LocalDateTime kommentarAm
    ) {
        super(concurrencyVersion);
        this.id = id;
        this.kommentarText = kommentarText;
        this.kommentarAm = kommentarAm;
    }

    public void setKommentarText(String kommentarText) {
        this.kommentarText = kommentarText;
    }

    public void setKommentarAm(LocalDateTime kommentarAm) {
        this.kommentarAm = kommentarAm;
    }

}
