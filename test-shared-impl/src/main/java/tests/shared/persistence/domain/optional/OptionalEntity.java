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

package tests.shared.persistence.domain.optional;

import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.EntityBase;

import java.util.List;
import java.util.Optional;

@Getter
public class OptionalEntity extends EntityBase<OptionalEntityId> {

    private OptionalEntityId id;
    private String mandatoryText;
    private Optional<String> optionalText;
    private MySimpleValueObject mandatorySimpleValueObject;
    private Optional<MySimpleValueObject> optionalSimpleValueObject;
    private MyComplexValueObject mandatoryComplexValueObject;
    private Optional<MyComplexValueObject> optionalComplexValueObject;
    private List<MyComplexValueObject> complexValueObjectList;


    @Builder(setterPrefix = "set")
    public OptionalEntity(OptionalEntityId id,
                          long concurrencyVersion,
                          String mandatoryText,
                          String optionalText,
                          MySimpleValueObject mandatorySimpleValueObject,
                          MySimpleValueObject optionalSimpleValueObject,
                          MyComplexValueObject mandatoryComplexValueObject,
                          MyComplexValueObject optionalComplexValueObject,
                          List<MyComplexValueObject> complexValueObjectList
    ) {
        super(concurrencyVersion);
        this.id = id;
        setMandatoryText(mandatoryText);
        setOptionalText(optionalText);
        setMandatorySimpleValueObject(mandatorySimpleValueObject);
        setOptionalSimpleValueObject(optionalSimpleValueObject);
        setMandatoryComplexValueObject(mandatoryComplexValueObject);
        setOptionalComplexValueObject(optionalComplexValueObject);
        this.complexValueObjectList = complexValueObjectList;

    }

    public void setMandatoryText(String mandatoryText) {
        DomainAssertions.isNotNull(mandatoryText, "'mandatoryText' cannot be null");
        this.mandatoryText = mandatoryText;
    }

    public void setOptionalText(String optionalText) {
        this.optionalText = Optional.ofNullable(optionalText);
    }

    public void setMandatorySimpleValueObject(MySimpleValueObject mandatorySimpleValueObject) {
        DomainAssertions.isNotNull(mandatorySimpleValueObject, "'mandatorySimpleValueObject' cannot be null");
        this.mandatorySimpleValueObject = mandatorySimpleValueObject;
    }

    public void setOptionalSimpleValueObject(MySimpleValueObject optionalSimpleValueObject) {
        this.optionalSimpleValueObject = Optional.ofNullable(optionalSimpleValueObject);
    }

    public void setMandatoryComplexValueObject(MyComplexValueObject mandatoryComplexValueObject) {
        DomainAssertions.isNotNull(mandatoryComplexValueObject, "'mandatoryComplexValueObject' cannot be null");
        this.mandatoryComplexValueObject = mandatoryComplexValueObject;
    }

    public void setOptionalComplexValueObject(MyComplexValueObject optionalComplexValueObject) {
        this.optionalComplexValueObject = Optional.ofNullable(optionalComplexValueObject);
    }

}
