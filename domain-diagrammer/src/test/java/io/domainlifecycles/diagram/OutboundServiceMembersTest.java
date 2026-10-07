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

package io.domainlifecycles.diagram;

import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.config.GeneralVisualSettings;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Outbound services show their methods but not their fields by default, like domain services and repositories.
 * The fixture {@code fixtures.nondomain.SomeOutboundService} has the field {@code client} and the method
 * {@code execute()}.
 *
 * @author Mario Herb
 */
public class OutboundServiceMembersTest {

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("fixtures.nondomain"));
    }

    @Test
    void testMethodsButNoFieldsAreShownByDefault() {
        var box = outboundServiceBox(GeneralVisualSettings.builder().build());

        assertThat(box).contains("execute()").doesNotContain("client");
    }

    @Test
    void testFieldsAndMethodsCanBeSwitchedIndividually() {
        var box = outboundServiceBox(GeneralVisualSettings.builder()
            .withShowOutboundServiceFields(true)
            .withShowOutboundServiceMethods(false)
            .build());

        assertThat(box).contains("client").doesNotContain("execute()");
    }

    private static String outboundServiceBox(GeneralVisualSettings generalVisualSettings) {
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(DiagramTrimSettings.builder()
                .withExplicitlyIncludedPackageNames(List.of("fixtures.nondomain"))
                .build())
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        var diagramText = new DomainDiagramGenerator(config, Domain.getDomainMirror()).generateDiagramText();
        int start = diagramText.indexOf("[<OS> SomeOutboundService <<OutboundService>>");
        assertThat(start).as("box of the outbound service").isNotNegative();
        return diagramText.substring(start, diagramText.indexOf(']', start) + 1);
    }
}
