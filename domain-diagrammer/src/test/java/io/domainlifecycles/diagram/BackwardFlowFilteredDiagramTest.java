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

import fixtures.backwardflow.EntryApplicationService;
import fixtures.backwardflow.TargetAggregate;
import fixtures.backwardflow.TargetCommand;
import fixtures.backwardflow.TargetDomainService;
import fixtures.backwardflow.TargetRepositoryImpl;
import fixtures.backwardflow.UnrelatedService;
import io.domainlifecycles.diagram.domain.DomainDiagramGenerator;
import io.domainlifecycles.diagram.domain.config.DiagramTrimSettings;
import io.domainlifecycles.diagram.domain.config.DomainDiagramConfig;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.staticanalysis.DomainCalls;
import io.domainlifecycles.staticanalysis.DomainMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@code DiagramTrimSettings.includeFlowsTo}: the backward counterpart of {@code includeFlowsFrom}
 * - restricts a diagram to the entry channels through which a target is reached, instead of what
 * the target leads to.
 * <p>
 * The fixture in {@code fixtures.backwardflow} models:
 * <pre>
 * EntryApplicationService.trigger() --calls--&gt; TargetDomainService.handle()
 *      --calls--&gt; TargetRepositoryImpl.findById() --manages--&gt; TargetAggregate
 * </pre>
 * {@link DomainCalls} is hand-built for the two CALL edges, matching {@link FlowFilteredDiagramTest}'s
 * approach - the diagrammer only ever reads the result, so no bytecode analysis is needed here.
 *
 * @author Mario Herb
 */
public class BackwardFlowFilteredDiagramTest {

    private static final String ENTRY = EntryApplicationService.class.getName();
    private static final String TARGET_SERVICE = TargetDomainService.class.getName();
    private static final String REPOSITORY = TargetRepositoryImpl.class.getName();
    private static final String AGGREGATE = TargetAggregate.class.getName();
    private static final String UNRELATED = UnrelatedService.class.getName();
    private static final String COMMAND = TargetCommand.class.getName();

    private static final Pattern RENDERED_CLASS =
        Pattern.compile("^// !!! (?:\\{Frame} )?(\\S+) !!!$", Pattern.MULTILINE);

    @BeforeEach
    void initializeMirror() {
        var factory = new ReflectiveDomainMirrorFactory("fixtures.backwardflow");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
    }

    @Test
    void testIncludeFlowsToATypeReachesAllItsEntryChannels() {
        var diagramText = generate(List.of(), List.of(AGGREGATE), analyzedCalls());

        assertThat(renderedClasses(diagramText))
            .contains(AGGREGATE, REPOSITORY, TARGET_SERVICE, ENTRY);
        assertThat(renderedClasses(diagramText)).doesNotContain(UNRELATED);
    }

    @Test
    void testIncludeFlowsToAMethodOnlyReachesItsOwnCallersNotWhatItCallsItself() {
        var diagramText = generate(List.of(), List.of(TARGET_SERVICE + "#handle"), analyzedCalls());

        assertThat(renderedClasses(diagramText)).contains(TARGET_SERVICE, ENTRY);
        // reached FROM handle() (repository, aggregate) is the opposite direction - must not leak in
        assertThat(renderedClasses(diagramText)).doesNotContain(REPOSITORY, AGGREGATE);
    }

    @Test
    void testIncludeFlowsFromAndIncludeFlowsToAreUnited() {
        // forward from the entry point reaches the whole downstream chain; backward from the
        // unrelated service (which nothing calls) only ever reaches itself - both must show up
        var diagramText = generate(List.of(ENTRY), List.of(UNRELATED), analyzedCalls());

        assertThat(renderedClasses(diagramText))
            .contains(ENTRY, TARGET_SERVICE, REPOSITORY, AGGREGATE, UNRELATED);
    }

    @Test
    void testACommandCannotBeABackwardFlowTarget() {
        assertThatThrownBy(() -> generate(List.of(), List.of(COMMAND), analyzedCalls()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be a backward flow target");
    }

    @Test
    void testWithoutAnyFlowSettingNothingIsRestricted() {
        var withoutFlow = generate(List.of(), List.of(), null);

        assertThat(renderedClasses(withoutFlow))
            .contains(ENTRY, TARGET_SERVICE, REPOSITORY, AGGREGATE, UNRELATED);
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static DomainCalls analyzedCalls() {
        var entryTrigger = domainMethod(EntryApplicationService.class, "trigger");
        var serviceHandle = domainMethod(TargetDomainService.class, "handle");
        var repositoryFindById = domainMethod(TargetRepositoryImpl.class, "findById");
        return DomainCalls.builder()
            .add(entryTrigger, List.of(
                new DomainCalls.CallSite(serviceHandle, ENTRY, 10)))
            .add(serviceHandle, List.of(
                new DomainCalls.CallSite(repositoryFindById, TARGET_SERVICE, 20)))
            .build();
    }

    private static DomainMethod domainMethod(Class<?> ownerClass, String methodName) {
        var typeMirror = Domain.getDomainMirror().getDomainTypeMirror(ownerClass.getName()).orElseThrow();
        var method = typeMirror.getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElseThrow();
        return new DomainMethod(typeMirror.getTypeName(), method);
    }

    private static String generate(List<String> includeFlowsFrom, List<String> includeFlowsTo,
                                   DomainCalls domainCalls) {
        var trim = DiagramTrimSettings.builder()
            .withExplicitlyIncludedPackageNames(List.of("fixtures.backwardflow"))
            .withIncludeFlowsFrom(includeFlowsFrom)
            .withIncludeFlowsTo(includeFlowsTo)
            .build();
        var config = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();
        return new DomainDiagramGenerator(config, Domain.getDomainMirror(), domainCalls)
            .generateDiagramText();
    }

    private static List<String> renderedClasses(String diagramText) {
        Matcher matcher = RENDERED_CLASS.matcher(diagramText);
        return matcher.results()
            .map(result -> result.group(1))
            .distinct()
            .toList();
    }
}
