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

package tests.shared.persistence.domain.records;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.List;

@Getter
@Setter
public class RecordTest extends AggregateRootBase<RecordTestId> {

    private final RecordTestId id;
    private String myValue;
    private RecordVo myVo;
    private List<RecordVo> myVoList;
    private Set<RecordVo> myVoSet;

    @Builder(setterPrefix = "set")
    public RecordTest(RecordTestId id, String myValue, RecordVo myVo, List<RecordVo> myVoList, Set<RecordVo> myVoSet,
                      long concurrencyVersion) {
        super(concurrencyVersion);
        this.myValue = myValue;
        this.myVo = myVo;
        this.myVoList = myVoList;
        this.myVoSet = myVoSet;
        this.id = id;
    }
}
