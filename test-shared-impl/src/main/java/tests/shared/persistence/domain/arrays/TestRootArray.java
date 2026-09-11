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
 *  Copyright 2019-2026 the original author or authors.
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

package tests.shared.persistence.domain.arrays;

import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import lombok.Builder;
import lombok.Getter;

/**
 * Aggregate root covering array typed fields, both directly on the entity ({@code payload}) and
 * nested inside a value object ({@code cryptoVo}). Both go through different branches of the
 * auto record mapping.
 */
@Getter
public class TestRootArray extends AggregateRootBase<TestRootArrayId> {

    private TestRootArrayId id;
    private String name;
    private byte[] payload;
    private CryptoVo cryptoVo;

    @Builder(setterPrefix = "set")
    public TestRootArray(TestRootArrayId id,
                         long concurrencyVersion,
                         String name,
                         byte[] payload,
                         CryptoVo cryptoVo
    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine Root ID muss angegeben sein!");
        setName(name);
        setPayload(payload);
        setCryptoVo(cryptoVo);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public void setCryptoVo(CryptoVo cryptoVo) {
        this.cryptoVo = cryptoVo;
    }

}
