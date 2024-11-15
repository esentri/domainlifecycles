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

package tests.shared.persistence.domain.valueobjectAutoMapping;

import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.List;

@Getter
public class AutoMappedVoAggregateRoot extends AggregateRootBase<AutoMappedVoAggregateRootId> {

    private AutoMappedVoAggregateRootId id;
    private String text;
    private List<AutoMappedSimpleVoOneToMany> valueObjectsOneToMany;
    private AutoMappedSimpleVo mySimpleVo;
    private AutoMappedComplexVo myComplexVo;
    private List<AutoMappedSimpleVoOneToMany2> valueObjectsOneToMany2;
    private List<AutoMappedVoEntity> entities;
    private AutoMappedVoIdentityRef voIdentityRef;

    @Builder(setterPrefix = "set")
    public AutoMappedVoAggregateRoot(AutoMappedVoAggregateRootId id,
                                     long concurrencyVersion,
                                     String text,
                                     List<AutoMappedSimpleVoOneToMany> valueObjectsOneToMany,
                                     AutoMappedSimpleVo mySimpleVo,
                                     AutoMappedComplexVo myComplexVo,
                                     List<AutoMappedSimpleVoOneToMany2> valueObjectsOneToMany2,
                                     List<AutoMappedVoEntity> entities,
                                     AutoMappedVoIdentityRef voIdentityRef
    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine ID muss angegeben sein!");
        setSimpleVo(mySimpleVo);
        setText(text);
        setValueObjectsOneToMany(valueObjectsOneToMany);
        setComplexVo(myComplexVo);
        this.valueObjectsOneToMany2 = valueObjectsOneToMany2;
        setEntities(entities);
        setVoIdentityRef(voIdentityRef);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValueObjectsOneToMany(List<AutoMappedSimpleVoOneToMany> valueObjectsOneToMany) {
        this.valueObjectsOneToMany = valueObjectsOneToMany;
    }

    public void setSimpleVo(AutoMappedSimpleVo simpleVo) {
        DomainAssertions.isNotNull(simpleVo, "We need a simple VO one to one");
        this.mySimpleVo = simpleVo;
    }

    public void setComplexVo(AutoMappedComplexVo complexVo) {
        this.myComplexVo = complexVo;
    }

    public void setEntities(List<AutoMappedVoEntity> entities) {
        this.entities = entities;
    }

    public void setVoIdentityRef(AutoMappedVoIdentityRef autoMappedVoIdentityRef) {
        this.voIdentityRef = autoMappedVoIdentityRef;
    }

}
