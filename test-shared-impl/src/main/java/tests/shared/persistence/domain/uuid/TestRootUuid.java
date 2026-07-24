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

package tests.shared.persistence.domain.uuid;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class TestRootUuid extends AggregateRootBase<TestRootUuid.TestRootUuidId> {

    private final TestRootUuidId id;
    private String name;
    private List<TestVoUuid> voList;
    /**
     * Constructs a skeleton {@code AggregateRoot} with
     * the given {@code concurrencyVersion}.
     *
     * @param concurrencyVersion used initially.
     */
    @Builder
    public TestRootUuid(TestRootUuidId id, String name, List<TestVoUuid> voList, long concurrencyVersion) {
        super(concurrencyVersion);
        this.name = name;
        this.id = id;
        this.voList = voList;
    }

    public record TestRootUuidId(UUID value) implements Identity<UUID> {

    }

}
