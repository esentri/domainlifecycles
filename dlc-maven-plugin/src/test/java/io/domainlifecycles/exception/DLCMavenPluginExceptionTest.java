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

package io.domainlifecycles.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DLCMavenPluginExceptionTest {

    @Test
    void failWithDetailOnlyKeepsMessageUnchanged() {
        DLCMavenPluginException exception = DLCMavenPluginException.fail("something went wrong");

        assertThat(exception.getMessage()).isEqualTo("something went wrong");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void failWithDetailAndCauseKeepsCause() {
        RuntimeException cause = new RuntimeException("root cause");

        DLCMavenPluginException exception = DLCMavenPluginException.fail("wrapped failure", cause);

        assertThat(exception.getMessage()).isEqualTo("wrapped failure");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void failWithArgsFormatsMessage() {
        DLCMavenPluginException exception = DLCMavenPluginException.fail("Could not find %s for %s", "type", "name");

        assertThat(exception.getMessage()).isEqualTo("Could not find type for name");
    }

    @Test
    void failWithCauseAndArgsFormatsMessageAndKeepsCause() {
        RuntimeException cause = new RuntimeException("root cause");

        DLCMavenPluginException exception = DLCMavenPluginException.fail("Failed for %s", cause, "context");

        assertThat(exception.getMessage()).isEqualTo("Failed for context");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void failRejectsNullDetail() {
        assertThatThrownBy(() -> DLCMavenPluginException.fail((String) null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void failRejectsNullCause() {
        assertThatThrownBy(() -> DLCMavenPluginException.fail("detail", (Throwable) null))
            .isInstanceOf(NullPointerException.class);
    }
}
