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

package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.domain.MyAggregateRoot;
import test.domain.MyApplicationService;
import test.domain.MyBaseService;
import test.domain.MyOverridingService;
import test.domain.MyRepository;
import test.domain.MyRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDomainCalls {

    private static DomainMirror domainMirror;

    private static DomainCalls calls;

    @BeforeAll
    public static void before() {
        calls = AnalyzedTestDomain.domainCalls();
        domainMirror = AnalyzedTestDomain.domainMirror();
    }

    @Test
    public void testCallsForAnUnknownCallerIsEmptyNotNull() {
        var unknown = methodCall(MyBaseService.class, "process");

        var called = calls.callsFor(unknown);
        assertThat(called).isNotNull();
        assertThat(called.isEmpty()).isTrue();
        assertThat(called.methods()).isEmpty();
        assertThat(called.callSites()).isEmpty();
        assertThat(calls.callers()).doesNotContain(unknown);
    }

    @Test
    public void testCallersOfIsTheReverseOfCallsFor() {
        var aggregateDoSomething = methodCall(MyAggregateRoot.class, "doSomething");

        var callers = calls.callersOf(aggregateDoSomething);

        assertThat(callers).contains(methodCall(MyApplicationService.class, "doSomething"));
        assertThat(callers).contains(methodCall(MyApplicationService.class, "helper"));

        // every reported caller really lists the method among its calls
        assertThat(callers).allSatisfy(caller ->
            assertThat(calls.callsFor(caller).methods()).contains(aggregateDoSomething));
    }

    @Test
    public void testCallersOfANeverCalledMethodIsEmpty() {
        assertThat(calls.callersOf(methodCall(MyOverridingService.class, "process"))).isEmpty();
    }

    @Test
    public void testCallSitesCarrySourcePositions() {
        var caller = methodCall(MyApplicationService.class, "doSomething");
        var called = calls.callsFor(caller);

        assertThat(called.callSites()).isNotEmpty();
        // doSomething and its lambda both live in MyApplicationService
        assertThat(called.callSites())
            .allMatch(callSite -> callSite.callSiteTypeName()
                .equals(MyApplicationService.class.getTypeName()));
        assertThat(called.callSites()).allMatch(callSite -> callSite.line().isPresent());
        assertThat(called.callSites()).allMatch(callSite -> callSite.lineNumber() > 0);
    }

    @Test
    public void testCallSiteTypeNameIsTheBodyTheCallWasFoundIn() {
        // doMethodRefOnInherited only references repository::someInheritedOperation. The update
        // call is found by descending into the implementation, so its call site is in
        // MyRepositoryImpl - not in the calling MyApplicationService.
        var caller = methodCall(MyApplicationService.class, "doMethodRefOnInherited");
        var updateOnImpl = methodCall(MyRepositoryImpl.class, "update");

        var updateCallSites = calls.callsFor(caller).callSites().stream()
            .filter(callSite -> callSite.called().equals(updateOnImpl))
            .toList();

        assertThat(updateCallSites).isNotEmpty();
        assertThat(updateCallSites)
            .allMatch(callSite -> callSite.callSiteTypeName()
                .equals(MyRepositoryImpl.class.getTypeName()));
    }

    @Test
    public void testSharedBodyIsAttributedToEveryEntryPointThatReachesIt() {
        // someInheritedOperation is scanned twice: once as an entry point of its own, and once by
        // descending into it from doMethodRefOnInherited. The body scan is memoized, so this
        // verifies the cached result is handed to both consumers, not just the first one.
        var updateOnImpl = methodCall(MyRepositoryImpl.class, "update");

        var asEntryPoint = calls.callsFor(
            methodCall(MyRepositoryImpl.class, "someInheritedOperation"));
        assertThat(asEntryPoint.methods()).contains(updateOnImpl);

        var viaDescent = calls.callsFor(
            methodCall(MyApplicationService.class, "doMethodRefOnInherited"));
        assertThat(viaDescent.methods()).contains(updateOnImpl);
    }

    @Test
    public void testMethodsAreDeduplicatedCallSitesAreNot() {
        // doOverloaded calls findById once and both handle overloads once each
        var called = calls.callsFor(methodCall(MyApplicationService.class, "doOverloaded"));

        assertThat(called.callSites()).hasSizeGreaterThanOrEqualTo(called.methods().size());
        assertThat(called.methods()).doesNotHaveDuplicates();
    }

    @Test
    public void testDomainMethodSignatureAndToString() {
        var call = methodCall(MyRepository.class, "findById");

        assertThat(call.signature()).isEqualTo("findById("
            + call.mirror().getParameters().get(0).getType().getTypeName() + ")");
        assertThat(call.toString())
            .startsWith(MyRepository.class.getTypeName() + ".")
            .contains("findById(")
            .endsWith(")");
    }

    @Test
    public void testBuilderMergesInsteadOfReplacing() {
        var caller = methodCall(MyApplicationService.class, "doSomething");
        var first = new DomainCalls.CallSite(methodCall(MyRepository.class, "findById"),
            MyApplicationService.class.getTypeName(), 17);
        var second = new DomainCalls.CallSite(methodCall(MyRepository.class, "update"),
            MyApplicationService.class.getTypeName(), 19);

        var merged = DomainCalls.builder()
            .add(caller, List.of(first))
            .add(caller, List.of(second))
            // the very same call site again must not be duplicated
            .add(caller, List.of(first))
            .build();

        assertThat(merged.size()).isEqualTo(1);
        assertThat(merged.callsFor(caller).callSites()).containsExactly(first, second);
        assertThat(merged.callsFor(caller).methods()).hasSize(2);
    }

    @Test
    public void testUnknownLineIsReportedAsAbsent() {
        var callSite = new DomainCalls.CallSite(methodCall(MyRepository.class, "update"),
            MyApplicationService.class.getTypeName(), DomainCalls.CallSite.UNKNOWN_LINE);

        assertThat(callSite.line()).isEmpty();
    }

    @Test
    public void testResultIsImmutable() {
        assertThat(calls.callers()).isNotEmpty();
        assertThrows(UnsupportedOperationException.class,
            () -> calls.callers().remove(methodCall(MyApplicationService.class, "doSomething")));
        assertThrows(UnsupportedOperationException.class,
            () -> calls.callsFor(methodCall(MyApplicationService.class, "doSomething"))
                .methods().clear());
    }

    private static DomainMethod methodCall(Class<?> type, String methodName) {
        Optional<DomainTypeMirror> typeMirror =
            domainMirror.getDomainTypeMirror(type.getTypeName());
        assertThat(typeMirror).isPresent();
        var method = typeMirror.get().getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst();
        assertThat(method).as("%s.%s", type.getTypeName(), methodName).isPresent();
        return new DomainMethod(typeMirror.get().getTypeName(), method.get());
    }
}
