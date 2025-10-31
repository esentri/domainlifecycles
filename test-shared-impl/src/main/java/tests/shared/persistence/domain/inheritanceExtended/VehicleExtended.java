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

package tests.shared.persistence.domain.inheritanceExtended;

import io.domainlifecycles.domain.types.base.AggregateRootBase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public abstract class VehicleExtended extends AggregateRootBase<VehicleExtendedId> {

    private final VehicleExtendedId id;

    private final String type = this.getClass().getSimpleName();

    @NotNull
    @Max(value = 1000, message = "Fahrzeuge länger als 10 m sind nicht erlaubt!")
    private Integer lengthCm;


    public VehicleExtended(VehicleExtendedId id, long concurrencyVersion, Integer lengthCm) {
        super(concurrencyVersion);
        this.id = id;
        this.lengthCm = lengthCm;
    }

    @Override
    public void validate() {
        super.validate();
    }

    public void setLengthCm(Integer lengthCm) {
        this.lengthCm = lengthCm;
    }
}
