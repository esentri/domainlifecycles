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

package tests.shared.persistence.domain.inheritance;

import io.domainlifecycles.domain.types.base.AggregateRootBase;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;


@Getter
public abstract class Vehicle extends AggregateRootBase<VehicleId> {

    private final VehicleId id;

    @NotNull
    @Max(value = 1000, message = "Fahrzeuge länger als 10 m sind nicht erlaubt!")
    private Integer lengthCm;


    public Vehicle(VehicleId id, long concurrencyVersion, Integer lengthCm) {
        super(concurrencyVersion);
        this.id = id;
        this.lengthCm = lengthCm;
    }

    @Override
    public void validate() {
        super.validate();
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public void setLengthCm(Integer lengthCm) {
        this.lengthCm = lengthCm;
    }
}
