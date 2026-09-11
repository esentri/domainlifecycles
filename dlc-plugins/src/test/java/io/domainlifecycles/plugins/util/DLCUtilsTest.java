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

package io.domainlifecycles.plugins.util;

import io.domainlifecycles.plugins.exception.DLCPluginsException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@link DLCUtils#initializeDomainMirrorFromClassPath(java.util.List, String...)} needs a real,
 * compiled domain on the classpath to build a {@code DomainMirror} from, which is out of scope for a
 * unit test. What can be verified in isolation is the documented failure contract: without any
 * classpath entries to load classes from, initialization must fail fast with a {@link DLCPluginsException}.
 */
public class DLCUtilsTest {

    @Test
    void throwsWhenClassPathFilesIsNull() {
        assertThatThrownBy(() -> DLCUtils.initializeDomainMirrorFromClassPath(null, "some.package"))
            .isInstanceOf(DLCPluginsException.class)
            .hasMessageContaining("DomainMirror could not be initialized");
    }

    @Test
    void throwsWhenClassPathFilesIsNullAndNoPackagesGiven() {
        assertThatThrownBy(() -> DLCUtils.initializeDomainMirrorFromClassPath(null))
            .isInstanceOf(DLCPluginsException.class);
    }
}
