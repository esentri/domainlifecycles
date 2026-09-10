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

package io.domainlifecycles.plugins.diagram;

import io.domainlifecycles.plugins.exception.DLCPluginsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FileTypeTest {

    @ParameterizedTest
    @CsvSource({
        "PDF, .pdf",
        "SVG, .svg",
        "PNG, .png",
        "JPG, .jpg",
        "NOMNOML, .nomnoml"
    })
    void byNameResolvesExactMatch(String name, String expectedSuffix) {
        FileType fileType = FileType.byName(name);

        assertThat(fileType.name()).isEqualTo(name);
        assertThat(fileType.getFileSuffix()).isEqualTo(expectedSuffix);
    }

    @ParameterizedTest
    @CsvSource({"pdf", "Pdf", "sVg", "png", "jpg", "nomnoml"})
    void byNameIsCaseInsensitive(String name) {
        assertThat(FileType.byName(name)).isEqualTo(FileType.valueOf(name.toUpperCase()));
    }

    @Test
    void byNameThrowsForUnknownName() {
        assertThatThrownBy(() -> FileType.byName("bogus"))
            .isInstanceOf(DLCPluginsException.class)
            .hasMessageContaining("bogus");
    }

    @Test
    void byNameThrowsForNullName() {
        assertThatThrownBy(() -> FileType.byName(null))
            .isInstanceOf(DLCPluginsException.class);
    }
}
