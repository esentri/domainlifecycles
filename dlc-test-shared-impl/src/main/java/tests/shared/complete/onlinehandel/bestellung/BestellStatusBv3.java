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
import lombok.Builder;
import lombok.Getter;
import nitrox.dlc.domain.types.base.EntityBase;

import java.time.LocalDateTime;

@Getter
public class BestellStatusBv3 extends EntityBase<BestellStatusIdBv3> {

    @NotNull
    private BestellStatusIdBv3 id;

    @NotNull
    private BestellStatusCodeEnumBv3 statusCode;

    @NotNull
    private LocalDateTime statusAenderungAm;

    @Builder(setterPrefix = "set")
    public BestellStatusBv3(BestellStatusIdBv3 id,
                            long concurrencyVersion,
                            BestellStatusCodeEnumBv3 statusCode,
                            LocalDateTime statusAenderungAm
    ) {
        super(concurrencyVersion);
        this.id = id;
        this.statusCode = statusCode;
        this.statusAenderungAm = statusAenderungAm;
    }

    public void setStatusCode(BestellStatusCodeEnumBv3 statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusAenderungAm(LocalDateTime statusAenderungAm) {
        this.statusAenderungAm = statusAenderungAm;
    }

}
