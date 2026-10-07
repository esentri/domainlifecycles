package io.domainlifecycles.persistence.cache;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoOpTransactionCacheProviderTest {

    @Test
    void handsOutNoCacheAndClearsNothing() {
        var provider = new NoOpTransactionCacheProvider<Object>();

        provider.clearCurrentTransactionCache();

        assertThat(provider.currentTransactionCache()).isEmpty();
    }
}
