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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@code GeneralVisualSettings.maxInlinedValueObjectFields} (default 2): a value object with at most that many fields
 * is shown inline, as field of the class referencing it, instead of as class of its own connected by a composition.
 * A field holding a value object shown inline counts as one field; a value object containing one that is not shown
 * inline is not shown inline itself.
 * <p>
 * The fixture in {@code fixtures.inlinedvalueobjects}: the aggregate {@code Order} holding {@code Name} (1 field),
 * {@code Money} (2 fields, one an enum) - also as list -, {@code Address} (3 fields), {@code PriceRange} (2 fields of
 * {@code Money}) and {@code Location} (1 field of {@code Address}).
 *
 * @author Mario Herb
 */
public class InlinedValueObjectsTest {

    private static final String PACKAGE = "fixtures.inlinedvalueobjects";

    /** Each rendered class is preceded by a comment naming it in full. */
    private static final Pattern RENDERED_CLASS =
        Pattern.compile("^// !!! (?:\\{Frame} )?(\\S+) !!!$", Pattern.MULTILINE);

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory(PACKAGE));
    }

    @Test
    void testValueObjectsOfUpToTwoFieldsAreInlinedByDefault() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(orderBox(diagramText)).contains(
            "name:<VO> Name", "price:<VO> Money", "discounts:<VO> List<Money>", "priceRange:<VO> PriceRange");
        assertThat(boxes(diagramText)).doesNotContain(
            PACKAGE + ".Name", PACKAGE + ".Money", PACKAGE + ".PriceRange");
    }

    @Test
    void testValueObjectsOfMoreFieldsAreConnectedByAComposition() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(boxes(diagramText)).contains(PACKAGE + ".Address", PACKAGE + ".Location");
        assertThat(diagramText)
            .contains("[<AR>Order <<AggregateRoot>>]  +-[<label> address 0..1] - [<VO>Address <<ValueObject>>]")
            .as("contains Address, which is not inlined")
            .contains("[<AR>Order <<AggregateRoot>>]  +-[<label> location 0..1] - [<VO>Location <<ValueObject>>]");
        assertThat(orderBox(diagramText)).doesNotContain("address:", "location:");
    }

    @Test
    void testOnlyValueObjectsOfOneFieldAreInlined_When_TheMaximumIsOne() {
        var diagramText = generate(GeneralVisualSettings.builder().withMaxInlinedValueObjectFields(1).build());

        assertThat(orderBox(diagramText)).contains("name:<VO> Name").doesNotContain("price:", "priceRange:");
        assertThat(boxes(diagramText)).contains(PACKAGE + ".Money", PACKAGE + ".PriceRange");
    }

    @Test
    void testLargerValueObjectsAreInlined_When_TheMaximumIsRaised() {
        var diagramText = generate(GeneralVisualSettings.builder().withMaxInlinedValueObjectFields(3).build());

        assertThat(orderBox(diagramText)).contains("address:<VO> Address", "location:<VO> Location");
        assertThat(boxes(diagramText)).doesNotContain(PACKAGE + ".Address", PACKAGE + ".Location");
    }

    @Test
    void testNoValueObjectIsInlined_When_TheMaximumIsZero() {
        var diagramText = generate(GeneralVisualSettings.builder().withMaxInlinedValueObjectFields(0).build());

        assertThat(boxes(diagramText)).contains(PACKAGE + ".Name", PACKAGE + ".Money");
        assertThat(orderBox(diagramText)).doesNotContain("name:", "price:");
    }

    @Test
    void testANegativeMaximumIsRejected() {
        assertThatThrownBy(() -> GeneralVisualSettings.builder().withMaxInlinedValueObjectFields(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static String generate(GeneralVisualSettings generalVisualSettings) {
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(DiagramTrimSettings.builder()
                .withExplicitlyIncludedPackageNames(List.of(PACKAGE))
                .build())
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror()).generateDiagramText();
    }

    private static String orderBox(String diagramText) {
        int start = diagramText.indexOf("[<AR> Order <<AggregateRoot>>");
        assertThat(start).as("box of the order").isNotNegative();
        return diagramText.substring(start, diagramText.indexOf(']', start) + 1);
    }

    private static List<String> boxes(String diagramText) {
        Matcher matcher = RENDERED_CLASS.matcher(diagramText);
        return matcher.results().map(result -> result.group(1)).distinct().toList();
    }
}
