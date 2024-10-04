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

package tests.shared.persistence.domain.valueobjectsPrimitive;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.Optional;


@Getter
public class VoAggregatePrimitive extends AggregateRootBase<VoAggregatePrimitiveId> {

    private final VoAggregatePrimitiveId id;

    @Setter
    private SimpleVoPrimitive simple;

    @Setter
    private ComplexVoPrimitive complex;

    @Setter
    private NestedVoPrimitive nested;

    @Setter
    private Optional<SimpleVoPrimitive> optionalSimple;

    @Setter
    private Optional<ComplexVoPrimitive> optionalComplex;

    @Setter
    private Optional<NestedVoPrimitive> optionalNested;

    @Setter
    private SimpleVoPrimitive recordMappedSimple;

    @Setter
    private ComplexVoPrimitive recordMappedComplex;

    @Setter
    private NestedVoPrimitive recordMappedNested;


    @Builder(setterPrefix = "set")
    public VoAggregatePrimitive(VoAggregatePrimitiveId id,
                                long concurrencyVersion,
                                SimpleVoPrimitive simple,
                                ComplexVoPrimitive complex,
                                NestedVoPrimitive nested,
                                SimpleVoPrimitive recordMappedSimple,
                                ComplexVoPrimitive recordMappedComplex,
                                NestedVoPrimitive recordMappedNested,
                                SimpleVoPrimitive optionalSimple,
                                ComplexVoPrimitive optionalComplex,
                                NestedVoPrimitive optionalNested

    ) {
        super(concurrencyVersion);
        this.id = id;
        this.simple = simple;
        this.complex = complex;
        this.nested = nested;
        this.recordMappedSimple = recordMappedSimple;
        this.recordMappedComplex = recordMappedComplex;
        this.recordMappedNested = recordMappedNested;
        this.optionalSimple = Optional.ofNullable(optionalSimple);
        this.optionalComplex = Optional.ofNullable(optionalComplex);
        this.optionalNested = Optional.ofNullable(optionalNested);
    }

}
