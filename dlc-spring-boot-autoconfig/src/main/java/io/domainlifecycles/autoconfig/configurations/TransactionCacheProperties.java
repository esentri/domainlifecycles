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

package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.autoconfig.exception.DLCAutoConfigException;
import org.springframework.core.env.Environment;

/**
 * The properties of DLC's Transaction Cache, shared by the jOOQ and the JDBC persistence autoconfig - a project only
 * ever activates one of the two persistence backends:
 * <ul>
 *     <li>{@value #ENABLED} - whether the cache is used, {@code true} by default,</li>
 *     <li>{@value #MAX_SIZE} - the maximum number of aggregates held per transaction, 256 by default.</li>
 * </ul>
 *
 * @author Mario Herb
 */
final class TransactionCacheProperties {

    static final String PREFIX = "dlc.features.persistence.transaction-cache";

    static final String ENABLED = PREFIX + ".enabled";

    static final String MAX_SIZE = PREFIX + ".max-size";

    private static final int DEFAULT_MAX_SIZE = 256;

    private TransactionCacheProperties() {
    }

    static boolean enabled(Environment environment) {
        return environment.getProperty(ENABLED, Boolean.class, true);
    }

    static int maxSize(Environment environment) {
        int maxSize = environment.getProperty(MAX_SIZE, Integer.class, DEFAULT_MAX_SIZE);
        if (maxSize <= 0) {
            throw DLCAutoConfigException.fail(
                "Property '%s' must be greater than 0, but was %d.", MAX_SIZE, maxSize);
        }
        return maxSize;
    }
}
