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

package tests.shared.persistence.domain.valueobjects;

import lombok.Builder;
import lombok.Getter;
import nitrox.dlc.assertion.DomainAssertions;
import nitrox.dlc.domain.types.base.EntityBase;

import java.util.Set;

@Getter
public class VoEntity extends EntityBase<VoEntityId> {

    private VoEntityId id;
    private String text;
    private VoAggregateRootId rootId;
    private Set<VoOneToManyEntity> valueObjectsOneToMany;
    private ComplexVo myComplexVo;

    @Builder(setterPrefix = "set")
    public VoEntity(VoEntityId id,
                    VoAggregateRootId rootId,
                    long concurrencyVersion,
                    String text,
                    Set<VoOneToManyEntity> valueObjectsOneToMany,

                    ComplexVo myComplexVo
    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine ID muss angegeben sein!");
        DomainAssertions.isNotNull(rootId, "Eine Root ID muss angegeben sein!");
        this.rootId = rootId;
        setText(text);
        setValueObjectsOneToMany(valueObjectsOneToMany);
        setComplexVo(myComplexVo);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValueObjectsOneToMany(Set<VoOneToManyEntity> valueObjectsOneToMany) {
        this.valueObjectsOneToMany = valueObjectsOneToMany;
    }

    public void setComplexVo(ComplexVo complexVo) {
        this.myComplexVo = complexVo;
    }

}
