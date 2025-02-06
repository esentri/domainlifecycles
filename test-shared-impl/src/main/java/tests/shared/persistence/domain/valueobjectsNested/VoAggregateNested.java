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

package tests.shared.persistence.domain.valueobjectsNested;

import io.domainlifecycles.domain.types.base.AggregateRootBase;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
public class VoAggregateNested extends AggregateRootBase<VoAggregateNestedId> {

    private final VoAggregateNestedId id;

    @Setter
    private NestedEnumSingleValued nestedEnumSingleValued;

    @Setter
    private NestedSimpleVo nestedSimpleVo;

    @Setter
    private NestedId nestedId;

    @Setter
    private List<NestedEnumSingleValued> nestedEnumSingleValuedList;

    @Setter
    private List<NestedSimpleVo> nestedSimpleVoList;

    @Setter
    private List<NestedId> nestedIdList;

    @Builder(setterPrefix = "set")
    public VoAggregateNested(VoAggregateNestedId id,
                             long concurrencyVersion,
                             NestedEnumSingleValued nestedEnumSingleValued,
                             NestedSimpleVo nestedSimpleVo,
                             NestedId nestedId,
                             List<NestedEnumSingleValued> nestedEnumSingleValuedList,
                             List<NestedSimpleVo> nestedSimpleVoList,
                             List<NestedId> nestedIdList

    ) {
        super(concurrencyVersion);
        this.id = id;
        this.nestedEnumSingleValued = nestedEnumSingleValued;
        this.nestedSimpleVo = nestedSimpleVo;
        this.nestedId = nestedId;
        this.nestedEnumSingleValuedList = nestedEnumSingleValuedList;
        this.nestedSimpleVoList = nestedSimpleVoList;
        this.nestedIdList = nestedIdList;
    }

}
