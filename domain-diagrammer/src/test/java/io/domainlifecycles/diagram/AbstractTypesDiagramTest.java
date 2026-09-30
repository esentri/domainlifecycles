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

import fixtures.abstracttypes.api.Address;
import fixtures.abstracttypes.api.Person;
import fixtures.abstracttypes.api.PricingService;
import fixtures.abstracttypes.impl.PersonRecord;
import fixtures.abstracttypes.impl.PricingServiceImpl;
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
 * Without the inheritance structures of its kind shown, an abstract type stands in for its implementations: it is
 * hidden as long as one of its implementations is shown in the diagram, and shown if none is - also if implementations
 * exist, but are left out of the diagram. With the inheritance structures shown, abstract and concrete types are
 * shown both.
 * <p>
 * The fixture in {@code fixtures.abstracttypes}: the read model {@code api.Person} implemented by
 * {@code impl.PersonRecord} and anonymously in {@code impl.PersonClient}, the read model {@code api.Address}
 * implemented by nothing, and the domain service {@code api.PricingService} implemented by
 * {@code impl.PricingServiceImpl}.
 *
 * @author Mario Herb
 */
public class AbstractTypesDiagramTest {

    private static final String PACKAGE = "fixtures.abstracttypes";

    /** Each rendered class is preceded by a comment naming it in full. */
    private static final Pattern RENDERED_CLASS =
        Pattern.compile("^// !!! (?:\\{Frame} )?(\\S+) !!!$", Pattern.MULTILINE);

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory(PACKAGE));
    }

    @Test
    void testAnAbstractTypeIsHidden_When_AnImplementationIsShown() {
        var rendered = rendered(DiagramTrimSettings.builder(), GeneralVisualSettings.builder().build(), null);

        assertThat(rendered).contains(PersonRecord.class.getName()).doesNotContain(Person.class.getName());
    }

    @Test
    void testNothingIsConnectedToAnAbstractTypeThatIsHidden() {
        var diagramText = new DomainDiagramGenerator(DomainDiagramConfig.builder()
            .withDiagramTrimSettings(DiagramTrimSettings.builder()
                .withExplicitlyIncludedPackageNames(List.of(PACKAGE)).build())
            .build(), Domain.getDomainMirror()).generateDiagramText();

        // Person is hidden, its implementation PersonRecord is shown in its place under its own name
        assertThat(diagramText).doesNotContain("[<RM>Person <<ReadModel>>]");
    }

    @Test
    void testAnAbstractTypeWithoutImplementationIsShown() {
        var rendered = rendered(DiagramTrimSettings.builder(), GeneralVisualSettings.builder().build(), null);

        assertThat(rendered).contains(Address.class.getName());
    }

    @Test
    void testAnAnonymousClassIsNeverShown() {
        var rendered = rendered(DiagramTrimSettings.builder(), GeneralVisualSettings.builder().build(), null);

        assertThat(rendered).doesNotContain(anonymousPerson());
    }

    @Test
    void testAnAbstractTypeIsShown_When_ItsImplementationsAreBlacklisted() {
        // its other implementation is anonymous, which is never shown: the interface is shown in its place
        var rendered = rendered(
            DiagramTrimSettings.builder().withClassesBlacklist(List.of(PersonRecord.class.getName())),
            GeneralVisualSettings.builder().build(), null);

        assertThat(rendered).contains(Person.class.getName()).doesNotContain(PersonRecord.class.getName());
    }

    @Test
    void testAbstractTypesAreShown_When_TheirImplementationsAreOutsideTheIncludedPackages() {
        var rendered = rendered(
            DiagramTrimSettings.builder().withExplicitlyIncludedPackageNames(List.of(PACKAGE + ".api")),
            GeneralVisualSettings.builder().build(), null);

        assertThat(rendered).containsExactlyInAnyOrder(
            Person.class.getName(), Address.class.getName(), PricingService.class.getName(),
            PACKAGE + ".api.PersonLookupService");
    }

    @Test
    void testAnAbstractFlowTargetIsShown_When_TheFlowReachesNoneOfItsImplementations() {
        // like a read model interface implemented anonymously by a client: the flow reaches the interface only
        assertThat(Domain.getDomainMirror().getDomainTypeMirror(anonymousPerson()))
            .as("the anonymous implementation is mirrored as read model")
            .isPresent();
        var rendered = rendered(
            DiagramTrimSettings.builder().withIncludeFlowsTo(List.of(Person.class.getName())),
            GeneralVisualSettings.builder().build(), DomainCalls.builder().build());

        assertThat(rendered).contains(Person.class.getName())
            .doesNotContain(PersonRecord.class.getName(), anonymousPerson());
    }

    @Test
    void testAbstractAndConcreteTypesAreShownBoth_When_TheInheritanceStructuresOfTheirKindAreShown() {
        var rendered = rendered(DiagramTrimSettings.builder(),
            GeneralVisualSettings.builder().withShowInheritanceStructuresForReadModels(true).build(), null);

        assertThat(rendered).contains(Person.class.getName(), PersonRecord.class.getName());
        assertThat(rendered)
            .as("the service kinds' inheritance structures are still hidden")
            .doesNotContain(PricingService.class.getName());
    }

    @Test
    void testAbstractAndConcreteTypesAreShownBoth_When_AllInheritanceStructuresAreShown() {
        var rendered = rendered(DiagramTrimSettings.builder(),
            GeneralVisualSettings.builder().withShowAllInheritanceStructures(true).build(), null);

        assertThat(rendered).contains(Person.class.getName(), PersonRecord.class.getName(),
            PricingService.class.getName(), PricingServiceImpl.class.getName());
    }

    @Test
    void testAServiceInterfaceIsShown_When_ItsImplementationIsBlacklisted() {
        var hidden = rendered(DiagramTrimSettings.builder(), GeneralVisualSettings.builder().build(), null);
        var blacklisted = rendered(
            DiagramTrimSettings.builder().withClassesBlacklist(List.of(PricingServiceImpl.class.getName())),
            GeneralVisualSettings.builder().build(), null);

        assertThat(hidden)
            .as("drawn as the implementation, named like the interface")
            .contains(PricingServiceImpl.class.getName())
            .doesNotContain(PricingService.class.getName());
        assertThat(blacklisted).contains(PricingService.class.getName())
            .doesNotContain(PricingServiceImpl.class.getName());
    }

    private static String anonymousPerson() {
        return PACKAGE + ".impl.PersonClient$1";
    }

    private static List<String> rendered(DiagramTrimSettings.DiagramTrimSettingsBuilder trim,
                                         GeneralVisualSettings generalVisualSettings,
                                         DomainCalls domainCalls) {
        var trimSettings = trim.build();
        if (trimSettings.getExplicitlyIncludedPackageNames().isEmpty()) {
            trimSettings = trim.withExplicitlyIncludedPackageNames(List.of(PACKAGE)).build();
        }
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trimSettings)
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        var diagramText = new DomainDiagramGenerator(config, Domain.getDomainMirror(), domainCalls).generateDiagramText();
        Matcher matcher = RENDERED_CLASS.matcher(diagramText);
        return matcher.results()
            .map(result -> result.group(1))
            .distinct()
            .toList();
    }
}
