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

package tests.shared.complete.ecommerce.order;

import io.domainlifecycles.domain.types.base.EntityBase;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeliveryAddressBv3 extends EntityBase<DeliveryAddressIdBv3> {

    @NotNull
    private DeliveryAddressIdBv3 id;

    @NotNull
    @Size(max = 200)
    private String name;

    @NotNull
    @Size(max = 200)
    private String street;

    @NotNull
    @Size(max = 200)
    private String postalCode;

    @NotNull
    @Size(max = 200)
    private String city;

    @Builder(setterPrefix = "set")
    public DeliveryAddressBv3(DeliveryAddressIdBv3 id,
                            long concurrencyVersion,
                            String name,
                            String street,
                            String postalCode,
                            String city
    ) {
        super(concurrencyVersion);
        this.id = id;
        this.name = name;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
