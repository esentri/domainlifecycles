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
import nitrox.dlc.assertion.DomainAssertions;
import nitrox.dlc.domain.types.base.EntityBase;

import java.util.List;


@Getter
public class TestEntity3 extends EntityBase<TestEntity3Id> {

    private TestEntity3Id id;
    private String name;
    private TestRootId testRootId;
    private TestEntity2Id testEntity2Id;
    private List<TestEntity4> testEntity4List;

    @Builder(setterPrefix = "set")
    public TestEntity3(TestEntity3Id id,
                       long concurrencyVersion,
                       TestRootId testRootId,
                       TestEntity2Id testEntity2Id,
                       String name,
                       List<TestEntity4> testEntity4List

    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine ID muss angegeben sein!");
        DomainAssertions.isNotNull(testRootId, "Eine Root ID muss angegeben sein!");
        DomainAssertions.isNotNull(testEntity2Id, "Eine Test entity 2 ID muss angegeben sein!");
        this.testRootId = testRootId;
        this.testEntity2Id = testEntity2Id;
        setName(name);
        this.testEntity4List = testEntity4List;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTestEntity4List(List<TestEntity4> testEntity4List) {
        this.testEntity4List = testEntity4List;
    }

}
