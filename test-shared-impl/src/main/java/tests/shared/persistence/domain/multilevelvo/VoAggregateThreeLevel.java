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

package tests.shared.persistence.domain.multilevelvo;

import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import lombok.Builder;
import lombok.Getter;
import tests.shared.persistence.domain.valueobjects.ComplexVo;

@Getter
public class VoAggregateThreeLevel extends AggregateRootBase<VoAggregateThreeLevelId> {

    private VoAggregateThreeLevelId identifikationsNummer;
    private String info;
    private ThreeLevelVo threeLevelVo;
    private ComplexVo myComplexVo;

    @Builder(setterPrefix = "set")
    public VoAggregateThreeLevel(VoAggregateThreeLevelId identifikationsNummer,
                                 long concurrencyVersion,
                                 String info,
                                 ThreeLevelVo threeLevelVo,
                                 ComplexVo myComplexVo
    ) {
        super(concurrencyVersion);
        this.identifikationsNummer = identifikationsNummer;

        DomainAssertions.isNotNull(identifikationsNummer, "Eine Identifikationsnummer muss angegeben sein!");
        setInfo(info);
        setThreeLevelVo(threeLevelVo);
        setComplexVo(myComplexVo);
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setThreeLevelVo(ThreeLevelVo threeLevelVo) {
        this.threeLevelVo = threeLevelVo;
    }

    public void setComplexVo(ComplexVo complexVo) {
        this.myComplexVo = complexVo;
    }

}
