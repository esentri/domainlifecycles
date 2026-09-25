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

package tests.shared.persistence.domain.oneToManyIdentityEnum;

import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.EntityBase;
import io.domainlifecycles.domain.types.base.IdentityBase;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EntityIdEnumList extends EntityBase<EntityIdEnumList.EntityIdEnumListId> {

    private EntityIdEnumListId id;
    private List<MyEnum> enumList;
    private List<MyId> idList;
    private ValueWithLists valueWithLists;

    @Builder(setterPrefix = "set")
    public EntityIdEnumList(EntityIdEnumListId id,
                            long concurrencyVersion,
                            List<MyEnum> enumList,
                            List<MyId> idList,
                            ValueWithLists valueWithLists
    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine ID muss angegeben sein!");
        this.enumList = (enumList == null ? new ArrayList<>() : enumList);
        this.idList = (idList == null ? new ArrayList<>() : idList);
        this.valueWithLists = valueWithLists;
    }

    public static class EntityIdEnumListId extends IdentityBase<Long> {

        public EntityIdEnumListId(Long anId) {
            super(anId);
        }

    }

}
