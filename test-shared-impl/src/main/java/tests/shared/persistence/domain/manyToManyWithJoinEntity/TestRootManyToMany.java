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

package tests.shared.persistence.domain.manyToManyWithJoinEntity;

import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TestRootManyToMany extends AggregateRootBase<TestRootManyToManyId> {

    private TestRootManyToManyId id;
    private String name;

    private List<TestEntityManyToManyA> testEntityManyToManyAList;

    @Builder(setterPrefix = "set")
    public TestRootManyToMany(TestRootManyToManyId id,
                              long concurrencyVersion,
                              String name,
                              List<TestEntityManyToManyA> testEntityManyToManyAList

    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine Root ID muss angegeben sein!");
        setName(name);
        setTestEntityManyToManyAList(testEntityManyToManyAList);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTestEntityManyToManyAList(List<TestEntityManyToManyA> testEntityManyToManyAList) {
        if (testEntityManyToManyAList == null) {
            this.testEntityManyToManyAList = new ArrayList<>();
        } else {
            this.testEntityManyToManyAList = testEntityManyToManyAList;
        }
    }

}
