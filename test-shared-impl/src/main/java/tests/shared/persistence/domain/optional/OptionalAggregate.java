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

package tests.shared.persistence.domain.optional;

import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.List;
import java.util.Optional;

@Getter
public class OptionalAggregate extends AggregateRootBase<OptionalAggregateId> {

    private OptionalAggregateId id;
    private String mandatoryText;
    private Optional<String> optionalText;
    private Optional<Long> optionalLong;
    private MySimpleValueObject mandatorySimpleValueObject;
    private Optional<MySimpleValueObject> optionalSimpleValueObject;
    private MyComplexValueObject mandatoryComplexValueObject;
    private Optional<MyComplexValueObject> optionalComplexValueObject;
    private Optional<OptionalEntity> optionalEntity;
    private Optional<RefAggId> optionalRefId;
    private RefValueObject refValueObject;
    private List<RefValueObject> refValueObjectList;


    @Builder(setterPrefix = "set")
    public OptionalAggregate(OptionalAggregateId id,
                             long concurrencyVersion,
                             String mandatoryText,
                             String optionalText,
                             Long optionalLong,
                             MySimpleValueObject mandatorySimpleValueObject,
                             MySimpleValueObject optionalSimpleValueObject,
                             MyComplexValueObject mandatoryComplexValueObject,
                             MyComplexValueObject optionalComplexValueObject,
                             OptionalEntity optionalEntity,
                             RefAggId optionalRefId,
                             RefValueObject refValueObject,
                             List<RefValueObject> refValueObjectList
    ) {
        super(concurrencyVersion);
        this.id = id;
        setMandatoryText(mandatoryText);
        setOptionalText(optionalText);
        setOptionalLong(optionalLong);
        setMandatorySimpleValueObject(mandatorySimpleValueObject);
        setOptionalSimpleValueObject(optionalSimpleValueObject);
        setMandatoryComplexValueObject(mandatoryComplexValueObject);
        setOptionalComplexValueObject(optionalComplexValueObject);
        setOptionalEntity(optionalEntity);
        setOptionalRefId(optionalRefId);
        setRefValueObject(refValueObject);
        this.refValueObjectList = refValueObjectList;
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

    public void setOptionalEntity(OptionalEntity optionalEntity) {
        this.optionalEntity = Optional.ofNullable(optionalEntity);
    }

    public void setOptionalRefId(RefAggId refAggId) {
        this.optionalRefId = Optional.ofNullable(refAggId);
    }

    public void setRefValueObject(RefValueObject refValueObject) {
        DomainAssertions.isNotNull(refValueObject, "'refValueObject' cannot be null");
        this.refValueObject = refValueObject;
    }

    public void setOptionalLong(Long optionalLong) {
        this.optionalLong = Optional.ofNullable(optionalLong);
    }

}
