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

import fixtures.topcommand.InnerDomainService;
import fixtures.topcommand.OuterApplicationService;
import fixtures.topcommand.TopAggregate;
import fixtures.topcommand.UnrelatedConsumer;
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
 * {@code GeneralVisualSettings.showOnlyTopLevelDomainCommandRelations} (default {@code true}):
 * restricts the "is processed by" relationship drawn from a DomainCommand to only its outermost
 * consumer, when several domain types in a delegation chain all technically process the same
 * command (an ApplicationService forwarding to a DomainService forwarding into an Aggregate method,
 * each of which takes the command as its sole parameter).
 * <p>
 * The fixture in {@code fixtures.topcommand} models exactly such a chain:
 * <pre>
 * TopCommand --processed by--&gt; OuterApplicationService.handle
 *                                   --field--&gt; InnerDomainService.handle
 *                                                  --（no further delegation)--&gt; TopAggregate.handle
 * </pre>
 * {@code InnerDomainService} and {@code TopAggregate} both also declare a method taking
 * {@code TopCommand}, so all three "process" it as far as the mirror is concerned -
 * {@code OuterApplicationService} is the only one meant to get a drawn relationship by default.
 *
 * @author Mario Herb
 */
public class TopLevelDomainCommandRelationsTest {

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("fixtures.topcommand"));
    }

    @Test
    void testByDefaultOnlyTheOutermostConsumerGetsACommandRelationship() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(hasProcessingRelationshipTo(diagramText, OuterApplicationService.class.getSimpleName()))
            .as("the outermost consumer, referenced by nothing else, keeps its relationship")
            .isTrue();
        assertThat(hasProcessingRelationshipTo(diagramText, InnerDomainService.class.getSimpleName()))
            .as("referenced by OuterApplicationService, which also processes the command")
            .isFalse();
        assertThat(hasProcessingRelationshipTo(diagramText, TopAggregate.class.getSimpleName()))
            .as("some ServiceKind (both, in fact) in the domain also processes the command")
            .isFalse();
    }

    @Test
    void testDisablingTheSettingShowsEveryConsumerThatProcessesTheCommand() {
        var diagramText = generate(
            GeneralVisualSettings.builder().withShowOnlyTopLevelDomainCommandRelations(false).build());

        assertThat(hasProcessingRelationshipTo(diagramText, OuterApplicationService.class.getSimpleName())).isTrue();
        assertThat(hasProcessingRelationshipTo(diagramText, InnerDomainService.class.getSimpleName())).isTrue();
        assertThat(hasProcessingRelationshipTo(diagramText, TopAggregate.class.getSimpleName())).isTrue();
    }

    @Test
    void testAnUnrelatedSecondReferencerDoesNotPreventCorrectSuppression() {
        // fixtures.topcommand.UnrelatedConsumer also holds a field of type InnerDomainService, but
        // never processes TopCommand at all - a regression test for a real bug where checking only
        // the first referencing type found (in effectively arbitrary classpath scan order) let such
        // an unrelated reference incorrectly "protect" the referenced type from being suppressed.
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(hasProcessingRelationshipTo(diagramText, InnerDomainService.class.getSimpleName()))
            .as("OuterApplicationService still processes the same command, "
                + "regardless of the unrelated extra reference from UnrelatedConsumer")
            .isFalse();
        assertThat(hasProcessingRelationshipTo(diagramText, OuterApplicationService.class.getSimpleName()))
            .isTrue();
        assertThat(hasProcessingRelationshipTo(diagramText, UnrelatedConsumer.class.getSimpleName()))
            .as("UnrelatedConsumer never processes TopCommand, so it never gets a relationship to it")
            .isFalse();
    }

    private static String generate(GeneralVisualSettings generalVisualSettings) {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("fixtures.topcommand"))
            .build();
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim)
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror()).generateDiagramText();
    }

    private static boolean hasProcessingRelationshipTo(String diagramText, String consumerSimpleTypeName) {
        return diagramText.lines()
            .anyMatch(line -> line.contains("is processed by") && line.contains(consumerSimpleTypeName));
    }
}
