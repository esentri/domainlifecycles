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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.persistence.cache;

import java.util.Optional;

/**
 * {@link TransactionCacheProvider} used when the transaction cache feature is disabled
 * ({@code transactionCacheEnabled = false}).
 * <p>
 * Always reports no active transaction cache, so every consumer transparently falls back to fetching from
 * the database - behavior is then identical to a build without the transaction cache feature at all.
 *
 * @param <BASE_RECORD_TYPE> the base record type of the persistence technology this provider is used with
 * @author Mario Herb
 */
public final class NoOpTransactionCacheProvider<BASE_RECORD_TYPE> implements TransactionCacheProvider<BASE_RECORD_TYPE> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TransactionCache<BASE_RECORD_TYPE>> currentTransactionCache() {
        return Optional.empty();
    }
}
