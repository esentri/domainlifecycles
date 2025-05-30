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

package tests.shared.persistence.domain.hierarchicalBackRef;

import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import lombok.Builder;
import lombok.Getter;


@Getter
public class TestRootHierarchicalBackref extends AggregateRootBase<TestRootHierarchicalBackrefId> {

    @Id
    private TestRootHierarchicalBackrefId id;
    private String name;
    private TestRootHierarchicalBackref parent;
    private TestRootHierarchicalBackref child;


    @Builder(setterPrefix = "set")
    public TestRootHierarchicalBackref(TestRootHierarchicalBackrefId id,
                                       TestRootHierarchicalBackref parent,
                                       long concurrencyVersion,
                                       String name,
                                       TestRootHierarchicalBackref child

    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine Root ID muss angegeben sein!");
        this.parent = parent;
        setName(name);
        setChild(child);
    }

    public void setName(String name) {
        DomainAssertions.isNotNull(name, "Ein Name muss angegeben sein");
        this.name = name;
    }

    public void setChild(TestRootHierarchicalBackref child) {
        this.child = child;
    }

    public void setParent(TestRootHierarchicalBackref parent) {
        this.parent = parent;
    }

}
