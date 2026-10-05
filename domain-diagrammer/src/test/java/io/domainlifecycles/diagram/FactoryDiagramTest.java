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

import fixtures.factories.BookingFactory;
import fixtures.factories.SchedulingService;
import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.diagram.domain.config.GeneralVisualSettings;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.staticanalysis.DomainCalls;
import io.domainlifecycles.staticanalysis.DomainMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Factories are shown with their own stereotype, factory methods of other classes are marked, and a class creating
 * another domain type by its factory methods is connected to it by a {@code <<creates>>} relationship.
 * <p>
 * The fixture in {@code fixtures.factories}:
 * <pre>
 * CalendarFactory (Factory)      create, createFor, copy, createEmpty --&gt; Calendar; createBooking --&gt; Booking
 * Calendar (aggregate)           &#64;FactoryMethod open --&gt; Calendar (itself), planAppointment --&gt; Appointment (its entity)
 * Appointment (entity of Calendar) &#64;FactoryMethod book --&gt; Booking (another aggregate)
 * SchedulingService (DomainService) &#64;FactoryMethod book --&gt; Booking; cancel
 * BookingFactory (Factory interface), implemented by BookingFactoryImpl: create --&gt; Booking
 * </pre>
 *
 * @author Mario Herb
 */
class FactoryDiagramTest {

    private static final String PACKAGE = "fixtures.factories";

    private static final String BOOK = PACKAGE + ".SchedulingService#book";

    @BeforeEach
    void initializeMirror() {
        Domain.initialize(new ReflectiveDomainMirrorFactory(PACKAGE));
    }

    @Test
    void testAFactoryIsShownWithItsStereotypeAndStyle() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(diagramText).contains("#.F:fill=#E0F0E0 bold");
        assertThat(diagramText).contains("[<F> CalendarFactory <<Factory>> |");
    }

    @Test
    void testFactoriesCanBeHidden() {
        var diagramText = generate(GeneralVisualSettings.builder().withShowFactories(false).build());

        assertThat(diagramText).doesNotContain("CalendarFactory");
    }

    @Test
    void testTheMethodsButNotTheFieldsOfAFactoryAreShownByDefault() {
        var box = box(generate(GeneralVisualSettings.builder().build()), "[<F> CalendarFactory");

        assertThat(box).contains("Calendar create()").doesNotContain("idGenerator");
    }

    @Test
    void testTheFieldsAndMethodsOfAFactoryCanBeSwitchedIndividually() {
        var box = box(generate(GeneralVisualSettings.builder()
            .withShowFactoryFields(true)
            .withShowFactoryMethods(false)
            .build()), "[<F> CalendarFactory");

        assertThat(box).contains("idGenerator").doesNotContain("create()");
    }

    @Test
    void testTheFactoryMethodsOfOtherClassesAreMarked() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(box(diagramText, "[<AR> Calendar"))
            .contains("+ «factory» Appointment planAppointment(String)")
            .contains("+ «factory» Calendar open(CalendarId)")
            .contains("+ Appointment firstAppointment()");
        assertThat(box(diagramText, "[<DS> SchedulingService"))
            .contains("+ «factory» Booking book(Calendar)")
            .contains("+ void cancel(Booking)");
    }

    @Test
    void testTheMethodsOfAFactoryAreNotMarked_AsItsStereotypeTellsThem() {
        var box = box(generate(GeneralVisualSettings.builder().build()), "[<F> CalendarFactory");

        assertThat(box).doesNotContain("«factory»");
    }

    @Test
    void testAFactoryCreatesTheAggregatesOfItsFactoryMethods() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(relationship(diagramText, "<F>CalendarFactory", "creates CalendarFactory.createBooking", "<AF> Booking <<Aggregate>>"))
            .isTrue();
    }

    @Test
    void testTheLabelOfACreatesRelationshipNamesAtMostThreeMethods() {
        var line = relationshipLine(generate(GeneralVisualSettings.builder().build()),
            "<F>CalendarFactory", "<AF> Calendar <<Aggregate>>");

        assertThat(line).contains("<<creates>>").endsWith("…] --> [<AF> Calendar <<Aggregate>>]");
        assertThat(line.substring(line.indexOf("<<creates>>"), line.indexOf('…')).split(",")).hasSize(4);
    }

    @Test
    void testADomainServiceCreatesTheAggregatesOfItsFactoryMethods() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(relationship(diagramText, "<DS>SchedulingService", "creates SchedulingService.book", "<AF> Booking <<Aggregate>>"))
            .isTrue();
    }

    @Test
    void testAClassCreatingAClassOfItsOwnAggregateHasNoCreatesRelationship_AsTheCompositionConnectsThem() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(diagramText.lines().filter(line -> line.contains("<<creates>>") && line.contains("planAppointment"))).isEmpty();
        assertThat(relationship(diagramText, "<AR>Calendar", "appointments 0..*", "<E>Appointment <<Entity>>"))
            .isTrue();
    }

    @Test
    void testAnEntityCreatingAnotherAggregateConnectsTheFramesOfBoth() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(relationship(diagramText, "<AF> Calendar <<Aggregate>>", "creates Appointment.book", "<AF> Booking <<Aggregate>>"))
            .isTrue();
    }

    @Test
    void testAClassCreatingItsOwnInstancesHasNoCreatesRelationship() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(diagramText.lines().filter(line -> line.contains("<<creates>>") && line.contains("open"))).isEmpty();
    }

    @Test
    void testTheCreatesRelationshipsCanBeHidden() {
        var diagramText = generate(GeneralVisualSettings.builder().withShowFactoryRelations(false).build());

        assertThat(diagramText).doesNotContain("<<creates>>");
        assertThat(diagramText).contains("«factory»");
    }

    @Test
    void testTheCreatesRelationshipsBelongToTheConnectedTypes() {
        var diagramText = generate(GeneralVisualSettings.builder().build(),
            DiagramTrimSettings.builder()
                .withExplicitlyIncludedPackageNames(List.of(PACKAGE))
                .withIncludeConnectedToOutgoing(List.of("fixtures.factories.SchedulingService"))
                .build());

        assertThat(diagramText).contains("[<AR> Booking <<AggregateRoot>>").doesNotContain("CalendarFactory");
    }

    // ---------------------------------------------------------------------
    // A factory known by its interface
    // ---------------------------------------------------------------------

    @Test
    void testAFactoryImplementingAnInterfaceIsShownAsItsInterface() {
        var diagramText = generate(GeneralVisualSettings.builder().build());

        assertThat(diagramText).contains("[<F> BookingFactory <<Factory>> |").doesNotContain("[<F> BookingFactoryImpl");
        assertThat(relationship(diagramText, "<F>BookingFactory", "creates BookingFactory.create", "<AF> Booking <<Aggregate>>"))
            .isTrue();
    }

    @Test
    void testTheInterfaceAndTheImplementationOfAFactoryAreShown_When_TheInheritanceStructuresOfServiceKindsAre() {
        var diagramText = generate(GeneralVisualSettings.builder().withShowInheritanceStructuresForServiceKinds(true).build());

        assertThat(diagramText).contains("[<F> BookingFactory <<Factory>> |", "[<F> BookingFactoryImpl <<Factory>> |");
        assertThat(diagramText).contains("[<F>BookingFactory <<Factory>>]  <:- [<F>BookingFactoryImpl <<Factory>>]");
        assertThat(relationship(diagramText, "<F>BookingFactoryImpl", "creates BookingFactoryImpl.create",
            "<AF> Booking <<Aggregate>>")).isTrue();
    }

    // ---------------------------------------------------------------------
    // Flows through a factory
    // ---------------------------------------------------------------------

    @Test
    void testAFlowLeadsThroughTheFactoryItCalls() {
        var diagramText = generateFlows(List.of(BOOK), GeneralVisualSettings.builder().build());

        assertThat(diagramText).contains("[<DS> SchedulingService <<DomainService>> |", "[<F> BookingFactory <<Factory>> |")
            .doesNotContain("CalendarFactory");
    }

    @Test
    void testAServiceCallingAFactoryInAFlowIsConnectedToIt_When_TheCallsOfTheFlowsAreShown() {
        var diagramText = generateFlows(List.of(BOOK), GeneralVisualSettings.builder().withShowFlowCallRelations(true).build());

        assertThat(relationship(diagramText, "<DS>SchedulingService", "calls BookingFactory.create", "<F>BookingFactory <<Factory>>"))
            .isTrue();
    }

    @Test
    void testAFlowLeadsFromAFactoryMethodToTheAggregateItCreates() {
        var diagramText = generateFlows(List.of(BOOK), GeneralVisualSettings.builder().build());

        assertThat(diagramText).contains("[<AR> Booking <<AggregateRoot>> |");
        assertThat(relationship(diagramText, "<F>BookingFactory", "creates BookingFactory.create", "<AF> Booking <<Aggregate>>"))
            .isTrue();
        assertThat(relationship(diagramText, "<DS>SchedulingService", "creates SchedulingService.book",
            "<AF> Booking <<Aggregate>>")).isTrue();
    }

    @Test
    void testAFlowToAnAggregateLeadsBackToTheFactoryMethodsCreatingIt() {
        var diagramText = generateFlowsTo(List.of(PACKAGE + ".Booking"), GeneralVisualSettings.builder().build());

        assertThat(diagramText).contains("[<F> BookingFactory <<Factory>> |", "[<F> CalendarFactory <<Factory>> |",
            "[<DS> SchedulingService <<DomainService>> |", "[<AR> Calendar <<AggregateRoot>> |");
        // only the factory methods creating it are shown, as the methods of the flow
        assertThat(box(diagramText, "[<F> CalendarFactory")).contains("createBooking()").doesNotContain("createEmpty()");
        assertThat(relationship(diagramText, "<AF> Calendar <<Aggregate>>", "creates Appointment.book", "<AF> Booking <<Aggregate>>"))
            .isTrue();
    }

    // ---------------------------------------------------------------------
    // Diagrams restricted to connected types
    // ---------------------------------------------------------------------

    @Test
    void testTheIngoingConnectionsOfAnAggregateLeadToEverythingCreatingIt() {
        var rendered = generate(GeneralVisualSettings.builder().build(), DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of(PACKAGE))
            .withIncludeConnectedToIngoing(List.of(PACKAGE + ".Booking"))
            .build());

        // the factories and the domain service creating it, and the aggregate one of whose entities does
        assertThat(rendered).contains("[<F> CalendarFactory <<Factory>> |", "[<F> BookingFactory <<Factory>> |",
            "[<DS> SchedulingService <<DomainService>> |", "[<AR> Calendar <<AggregateRoot>> |");
        assertThat(relationship(rendered, "<AF> Calendar <<Aggregate>>", "creates Appointment.book", "<AF> Booking <<Aggregate>>"))
            .isTrue();
    }

    @Test
    void testTheOutgoingConnectionsOfAnAggregateLeadToTheAggregatesItsEntitiesCreate() {
        var rendered = generate(GeneralVisualSettings.builder().build(), DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of(PACKAGE))
            .withIncludeConnectedToOutgoing(List.of(PACKAGE + ".Calendar"))
            .build());

        assertThat(rendered).contains("[<AR> Booking <<AggregateRoot>> |").doesNotContain("Factory");
    }

    @Test
    void testExcludingTheIngoingConnectionsOfAnAggregateExcludesWhatCreatesIt() {
        var rendered = generate(GeneralVisualSettings.builder().build(), DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of(PACKAGE))
            .withExcludeConnectedToIngoing(List.of(PACKAGE + ".Booking"))
            .build());

        assertThat(rendered).doesNotContain("BookingFactory", "SchedulingService", "[<AR> Booking", "[<AR> Calendar");
    }

    private static String generate(GeneralVisualSettings generalVisualSettings) {
        return generate(generalVisualSettings, DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of(PACKAGE))
            .build());
    }

    private static String generate(GeneralVisualSettings generalVisualSettings, DiagramTrimSettings trimSettings) {
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(trimSettings)
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror()).generateDiagramText();
    }

    private static String generateFlows(List<String> flowsFrom, GeneralVisualSettings generalVisualSettings) {
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(DiagramTrimSettings.builder()
                .withExplicitlyIncludedPackageNames(List.of(PACKAGE))
                .withIncludeFlowsFrom(flowsFrom)
                .build())
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror(), calls()).generateDiagramText();
    }

    private static String generateFlowsTo(List<String> flowsTo, GeneralVisualSettings generalVisualSettings) {
        var config = DomainDiagramConfig.builder()
            .withDiagramTrimSettings(DiagramTrimSettings.builder()
                .withExplicitlyIncludedPackageNames(List.of(PACKAGE))
                .withIncludeFlowsTo(flowsTo)
                .build())
            .withGeneralVisualSettings(generalVisualSettings)
            .build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror(), calls()).generateDiagramText();
    }

    /**
     * SchedulingService.book calls BookingFactory.create - without a field, so nothing else connects them.
     */
    private static DomainCalls calls() {
        return DomainCalls.builder()
            .add(method(SchedulingService.class, "book"), List.of(
                new DomainCalls.CallSite(method(BookingFactory.class, "create"), SchedulingService.class.getName(), 1)))
            .build();
    }

    private static DomainMethod method(Class<?> ownerClass, String methodName) {
        var typeMirror = Domain.getDomainMirror().getDomainTypeMirror(ownerClass.getName()).orElseThrow();
        var method = typeMirror.getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElseThrow();
        return new DomainMethod(typeMirror.getTypeName(), method);
    }

    /**
     * The box of the class whose declaration starts with the given text.
     */
    private static String box(String diagramText, String declarationStart) {
        int start = diagramText.indexOf(declarationStart + " ");
        assertThat(start).as("box " + declarationStart).isNotNegative();
        return diagramText.substring(start, diagramText.indexOf(']', start) + 1);
    }

    /**
     * Whether a relationship line starts at the given node and ends at the given one, with the given stereotype and
     * label, e.g. {@code creates SchedulingService.book}.
     */
    private static boolean relationship(String diagramText, String from, String stereotypeAndLabel, String to) {
        return diagramText.lines()
            .filter(line -> line.startsWith("[" + from + (from.contains("<<") ? "]" : " ")))
            .filter(line -> line.endsWith("[" + to + "]"))
            .anyMatch(line -> line.replaceAll("<<|>>", "").contains(stereotypeAndLabel));
    }

    private static String relationshipLine(String diagramText, String from, String to) {
        return diagramText.lines()
            .filter(line -> line.startsWith("[" + from + " ") && line.endsWith("[" + to + "]"))
            .findFirst()
            .orElseThrow();
    }
}
