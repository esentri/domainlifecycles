package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.autoconfig.exception.DLCAutoConfigException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionCachePropertiesTest {

    @Test
    void theCacheIsEnabledAndHoldsAtMost256AggregatesByDefault() {
        var environment = new MockEnvironment();

        assertThat(TransactionCacheProperties.enabled(environment)).isTrue();
        assertThat(TransactionCacheProperties.maxSize(environment)).isEqualTo(256);
    }

    @Test
    void theConfiguredValuesAreUsed() {
        var environment = new MockEnvironment()
            .withProperty("dlc.features.persistence.transaction-cache.enabled", "false")
            .withProperty("dlc.features.persistence.transaction-cache.max-size", "42");

        assertThat(TransactionCacheProperties.enabled(environment)).isFalse();
        assertThat(TransactionCacheProperties.maxSize(environment)).isEqualTo(42);
    }

    @Test
    void aMaximalSizeNotGreaterThan0IsRejected() {
        var environment = new MockEnvironment()
            .withProperty("dlc.features.persistence.transaction-cache.max-size", "0");

        assertThatThrownBy(() -> TransactionCacheProperties.maxSize(environment))
            .isInstanceOf(DLCAutoConfigException.class)
            .hasMessageContaining("dlc.features.persistence.transaction-cache.max-size")
            .hasMessageContaining("greater than 0");
    }
}
