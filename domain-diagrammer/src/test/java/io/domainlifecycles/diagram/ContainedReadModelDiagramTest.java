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

import fixtures.containedreadmodel.CustomerView;
import fixtures.containedreadmodel.OrderOverview;
import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.config.GeneralVisualSettings;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.staticanalysis.DomainCalls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A ReadModel containing another ReadModel is connected to it by a composition - like a ValueObject containing
 * another one - with the multiplicity of the containing field, instead of listing it as field. A contained ReadModel
 * is shown together with the one containing it.
 * <p>
 * The fixture in {@code fixtures.containedreadmodel}:
 * <pre>
 * OrderOverview --customer (required)--&gt; CustomerView
 *               --delivery (Optional)--&gt; DeliveryView
 *               --lines (List, not empty)--&gt; LineView --product--&gt; ProductView
 *               --notes (List)--&gt; NoteView
 * OrderOverviewQueryHandler provides OrderOverview; UnrelatedView is contained in nothing
 * </pre>
 *
 * @author Mario Herb
 */
public class ContainedReadModelDiagramTest {

    private static final String PACKAGE = "fixtures.containedreadmodel";

    /** Each rendered class is preceded by a comment naming it in full. */
    private static final Pattern RENDERED_CLASS =
        Pattern.compile("^// !!! (?:\\{Frame} )?(\\S+) !!!$", Pattern.MULTILINE);

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory(PACKAGE));
    }

    @Test
    void testContainedReadModelsAreConnectedWithTheMultiplicityOfTheirField() {
        var diagramText = generate(DiagramTrimSettings.builder(), GeneralVisualSettings.builder().build(), null);

        assertThat(diagramText)
            .contains("[<RM>OrderOverview <<ReadModel>>]  +-[<label> customer 1] - [<RM>CustomerView <<ReadModel>>]")
            .contains("[<RM>OrderOverview <<ReadModel>>]  +-[<label> delivery 0..1] - [<RM>DeliveryView <<ReadModel>>]")
            .contains("[<RM>OrderOverview <<ReadModel>>]  +-[<label> lines 1..*] - [<RM>LineView <<ReadModel>>]")
            .contains("[<RM>OrderOverview <<ReadModel>>]  +-[<label> notes 0..*] - [<RM>NoteView <<ReadModel>>]");
    }

    @Test
    void testAReadModelContainedInAContainedOneIsConnectedToIt() {
        var diagramText = generate(DiagramTrimSettings.builder(), GeneralVisualSettings.builder().build(), null);

        assertThat(diagramText)
            .contains("[<RM>LineView <<ReadModel>>]  +-[<label> product 0..1] - [<RM>ProductView <<ReadModel>>]");
    }

    @Test
    void testContainedReadModelsAreNoFieldsOfTheContainingOne() {
        var diagramText = generate(DiagramTrimSettings.builder(), GeneralVisualSettings.builder().build(), null);

        assertThat(readModelBox(diagramText, "OrderOverview"))
            .contains("title:String")
            .doesNotContain("customer", "delivery", "lines", "notes");
    }

    @Test
    void testTheMultiplicityIsShownAtTheEndOfTheRelationship_When_NotInTheLabel() {
        var diagramText = generate(DiagramTrimSettings.builder(),
            GeneralVisualSettings.builder().withMultiplicityInLabel(false).build(), null);

        assertThat(diagramText)
            .contains("[<RM>OrderOverview <<ReadModel>>]  +-[<label> lines] -1..* [<RM>LineView <<ReadModel>>]");
    }

    @Test
    void testABlacklistedContainedReadModelStaysAField() {
        var diagramText = generate(
            DiagramTrimSettings.builder().withClassesBlacklist(List.of(CustomerView.class.getName())),
            GeneralVisualSettings.builder().build(), null);

        assertThat(renderedClasses(diagramText)).doesNotContain(CustomerView.class.getName());
        assertThat(readModelBox(diagramText, "OrderOverview")).contains("customer:CustomerView");
        assertThat(diagramText).doesNotContain("customer 1");
    }

    @Test
    void testContainedReadModelsAreShownWithTheContainingOne_When_TheFlowDoesNotReachThem() {
        // the backward flow to the overview reaches its query handler, no call reaches the read models it contains
        var diagramText = generate(
            DiagramTrimSettings.builder().withIncludeFlowsTo(List.of(OrderOverview.class.getName())),
            GeneralVisualSettings.builder().build(), DomainCalls.builder().build());

        assertThat(renderedClasses(diagramText)).contains(
            PACKAGE + ".OrderOverview", PACKAGE + ".OrderOverviewQueryHandler", PACKAGE + ".CustomerView",
            PACKAGE + ".DeliveryView", PACKAGE + ".LineView", PACKAGE + ".ProductView", PACKAGE + ".NoteView");
        assertThat(renderedClasses(diagramText))
            .as("contained in no read model the flow reaches")
            .doesNotContain(PACKAGE + ".UnrelatedView");
        assertThat(diagramText)
            .contains("[<RM>LineView <<ReadModel>>]  +-[<label> product 0..1] - [<RM>ProductView <<ReadModel>>]");
    }

    private static String generate(DiagramTrimSettings.DiagramTrimSettingsBuilder trim,
                                   GeneralVisualSettings generalVisualSettings,
                                   DomainCalls domainCalls) {
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trim.withExplicitlyIncludedPackageNames(List.of(PACKAGE)).build())
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror(), domainCalls).generateDiagramText();
    }

    private static String readModelBox(String diagramText, String simpleName) {
        int start = diagramText.indexOf("[<RM> " + simpleName + " <<ReadModel>>");
        assertThat(start).as("box of " + simpleName).isNotNegative();
        return diagramText.substring(start, diagramText.indexOf(']', start) + 1);
    }

    private static List<String> renderedClasses(String diagramText) {
        Matcher matcher = RENDERED_CLASS.matcher(diagramText);
        return matcher.results()
            .map(result -> result.group(1))
            .distinct()
            .toList();
    }
}
