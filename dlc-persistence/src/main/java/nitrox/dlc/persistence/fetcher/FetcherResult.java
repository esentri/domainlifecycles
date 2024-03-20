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

package nitrox.dlc.persistence.fetcher;

import nitrox.dlc.domain.types.AggregateRoot;

import java.util.Optional;

/**
 * FetcherResult is the result of a fetcher operation.
 *
 * @param <A>      the aggregate root type
 * @param <RECORD> the record type
 *
 * @author Mario Herb
 */
public class FetcherResult<A extends AggregateRoot<?>, RECORD> {

    final A resultValue;
    final FetcherContext<RECORD> fetcherContext;

    /**
     * Creates a new FetcherResult.
     *
     * @param resultValue    the result value
     * @param fetcherContext the fetcher context
     */
    public FetcherResult(A resultValue, FetcherContext<RECORD> fetcherContext) {
        this.resultValue = resultValue;
        this.fetcherContext = fetcherContext;

    }

    /**
     * Returns the result value.
     *
     * @return the result value
     */
    public Optional<A> resultValue() {
        if (resultValue == null) {
            return Optional.empty();
        }
        return Optional.of(resultValue);
    }

    /**
     * Returns the fetcher context.
     *
     * @return the fetcher context
     */
    public FetcherContext<RECORD> fetchedContext() {
        return fetcherContext;
    }
}
