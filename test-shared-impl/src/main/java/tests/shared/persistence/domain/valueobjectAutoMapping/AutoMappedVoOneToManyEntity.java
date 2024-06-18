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

package tests.shared.persistence.domain.valueobjectAutoMapping;

import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.ValueObjectBase;

import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class AutoMappedVoOneToManyEntity extends ValueObjectBase {

    private final String value;
    private final Set<AutoMappedVoOneToManyEntity2> oneToManySet;

    @Builder(setterPrefix = "set")
    public AutoMappedVoOneToManyEntity(String value,
                                       Set<AutoMappedVoOneToManyEntity2> oneToManySet
    ) {
        this.value = value;
        if (oneToManySet != null) {
            this.oneToManySet = oneToManySet.stream().collect(Collectors.toUnmodifiableSet());
        } else {
            this.oneToManySet = null;
        }
        DomainAssertions.isNotEmpty(value, "We need a value!");
    }

}