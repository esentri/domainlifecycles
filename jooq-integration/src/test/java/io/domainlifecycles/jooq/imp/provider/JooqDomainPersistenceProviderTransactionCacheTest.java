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

package io.domainlifecycles.jooq.imp.provider;

import io.domainlifecycles.jooq.cache.TransactionCacheJooqBinder;
import io.domainlifecycles.jooq.imp.JooqAggregateFetcher;
import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import org.jooq.TransactionListenerProvider;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.simple.TestRootSimple;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers bug 9 of {@code known_bugs_transaction_cache_2026_09}: the transaction cache's
 * {@link TransactionCacheJooqBinder} used to be registered only from {@code JooqAggregateRepository}'s
 * constructor, so any access path that never built one - such as using a {@link JooqAggregateFetcher}
 * directly - silently never got the cache wired in.
 * <p>
 * {@link BasePersistence_ITest} builds its shared {@code persistenceConfiguration} through
 * {@code JooqDomainPersistenceProvider}'s {@code DSLContext}-taking constructor, which now registers the
 * binder centrally, once, right there - before any repository or fetcher exists at all.
 */
class JooqDomainPersistenceProviderTransactionCacheTest extends BasePersistence_ITest {

    private boolean binderIsRegisteredForTheProvidersCache() {
        var expectedProvider = persistenceConfiguration.domainPersistenceProvider.transactionCacheProvider;
        return Arrays.stream(persistenceConfiguration.dslContext.configuration().transactionListenerProviders())
            .map(TransactionListenerProvider::provide)
            .filter(TransactionCacheJooqBinder.class::isInstance)
            .map(TransactionCacheJooqBinder.class::cast)
            .anyMatch(binder -> boundTransactionCacheProviderOf(binder) == expectedProvider);
    }

    private static Object boundTransactionCacheProviderOf(TransactionCacheJooqBinder binder) {
        try {
            var field = TransactionCacheJooqBinder.class.getDeclaredField("transactionCacheProvider");
            field.setAccessible(true);
            return field.get(binder);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void theBinderIsAlreadyRegisteredRightAfterProviderConstruction() {
        assertThat(persistenceConfiguration.domainPersistenceProvider.transactionCacheProvider)
            .isInstanceOf(ThreadBoundTransactionCacheProvider.class);
        assertThat(binderIsRegisteredForTheProvidersCache()).isTrue();
    }

    @Test
    void theBinderStaysRegisteredWhenOnlyAFetcherIsBuiltDirectly() {
        var fetcher = new JooqAggregateFetcher<>(
            TestRootSimple.class,
            persistenceConfiguration.dslContext,
            persistenceConfiguration.domainPersistenceProvider);

        assertThat(fetcher).isNotNull();
        assertThat(binderIsRegisteredForTheProvidersCache())
            .as("no JooqAggregateRepository was ever built for this provider - the binder must still be "
                + "registered, since JooqDomainPersistenceProvider wires it centrally at construction time")
            .isTrue();
    }
}
