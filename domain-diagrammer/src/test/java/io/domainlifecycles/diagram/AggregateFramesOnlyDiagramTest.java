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
import io.domainlifecycles.diagram.domain.notes.DomainClassNote;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The central switch {@code showOnlyAggregateFrames} draws all Aggregates as their frame only - without the classes,
 * the relationships and the notes inside - while the relationships from outside still connect the frames.
 *
 * @author Mario Herb
 */
class AggregateFramesOnlyDiagramTest {

    private static final String ORDER = "tests.shared.complete.ecommerce.order.OrderBv3";

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared"));
    }

    @Test
    void testAggregatesAreDrawnWithTheirContentByDefault() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(diagramText)
            .contains("[<AF> OrderBv3 <<Aggregate>>|")
            .contains("[<AR> OrderBv3 <<AggregateRoot>> |")
            .contains("[<E> OrderItemBv3 <<Entity>> |")
            .contains("[<AR>OrderBv3 <<AggregateRoot>>]  +-[<label> orderItems");
    }

    @Test
    void testOnlyTheFramesOfTheAggregatesAreDrawn() {
        var diagramText = generate(GeneralVisualSettings.builder().withShowOnlyAggregateFrames(true).build());

        assertThat(diagramText.lines())
            .contains("// !!! {Frame} " + ORDER + " !!!")
            .contains("[<AF> OrderBv3 <<Aggregate>>]")
            .noneMatch(line -> line.startsWith("[<AF>") && line.endsWith("|"));
        assertThat(diagramText)
            .doesNotContain("<<AggregateRoot>>")
            .doesNotContain("<<Entity>>")
            .doesNotContain("[<VO>")
            .doesNotContain("[<I>")
            .doesNotContain("+-[<label> orderItems");
    }

    @Test
    void testTheRelationshipsFromOutsideStillConnectTheFrames() {
        var diagramText = generate(GeneralVisualSettings.builder().withShowOnlyAggregateFrames(true).build());

        assertThat(diagramText.lines())
            .contains("[<R>OrderRepository <<Repository>>]  --> [<AF> OrderBv3 <<Aggregate>>]")
            .contains("[<AF> OrderBv3 <<Aggregate>>]  --[<label> <<publishes>> OrderBv3.startDelivery] --> "
                + "[<DE>DeliveryStarted <<DomainEvent>>]")
            .contains("[<AF> OptionalAggregate <<Aggregate>>]  --[<label> <<id-ref>> OptionalAggregate.optionalRefId] "
                + "--> [<AF> RefAgg <<Aggregate>>]");
    }

    @Test
    void testTheNotesOfTheClassesInsideAnAggregateAreNotDrawn() {
        var settings = GeneralVisualSettings.builder().withShowNotes(true);
        var notes = List.of(new DomainClassNote(ORDER, "a note on the root"));

        assertThat(generate(settings.build(), notes)).contains("a note on the root");
        assertThat(generate(settings.withShowOnlyAggregateFrames(true).build(), notes))
            .doesNotContain("a note on the root");
    }

    private static String generate(GeneralVisualSettings generalVisualSettings) {
        return generate(generalVisualSettings, List.of());
    }

    private static String generate(GeneralVisualSettings generalVisualSettings, List<DomainClassNote> notes) {
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(DiagramTrimSettings.builder()
                .withExplicitlyIncludedPackageNames(List.of("tests.shared"))
                .build())
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror(), notes).generateDiagramText();
    }
}
