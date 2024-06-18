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

package tests.shared.persistence.domain.complex;

import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.EntityBase;

import java.util.List;


@Getter
public class TestEntity2 extends EntityBase<TestEntity2Id> {

    private TestEntity2Id id;
    private String name;
    private TestRootId testRootId;
    private List<TestEntity3> testEntity3List;

    @Builder(setterPrefix = "set")
    public TestEntity2(TestEntity2Id id,
                       TestRootId testRootId,
                       String name,
                       List<TestEntity3> testEntity3List,
                       long concurrencyVersion

    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine ID muss angegeben sein!");
        DomainAssertions.isNotNull(testRootId, "Eine Root ID muss angegeben sein!");
        this.testRootId = testRootId;
        setName(name);
        this.testEntity3List = testEntity3List;
    }


    public void setName(String name) {
        this.name = name;
    }

}
