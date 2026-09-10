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

import io.domainlifecycles.mirror.api.Domain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.domain.MyAbstractService;
import test.domain.MyAggregateRoot;
import test.domain.MyApplicationService;
import test.domain.MyBaseService;
import test.domain.MyConcreteBaseService;
import test.domain.MyDomainCommand;
import test.domain.MyDomainService;
import test.domain.MyOutboundService;
import test.domain.MyOverridingService;
import test.domain.MyPlainInterface;
import test.domain.MyPlainInterfaceImpl;
import test.domain.MyRepository;
import test.domain.MyRepositoryImpl;
import test.domain.MySecondOverridingService;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMethodMatching {

    private static DomainCalls calls;

    @BeforeAll
    public static void before() {
        calls = AnalyzedTestDomain.domainCalls();
    }

    @Test
    public void testCallsMyDomainService(){
        var m = Domain.getDomainMirror().getDomainTypeMirror(MyDomainService.class.getTypeName()).get();
        var onMyDomainEvent = m.getMethods().stream().filter(method -> method.getName().equals("onMyDomainEvent")).findFirst();
        assertThat(onMyDomainEvent).isPresent();

        var called = calls.callsFor(new DomainMethod(m.getTypeName(),onMyDomainEvent.get()));
        assertThat(called).isNotNull();
        assertThat(called.methods()).hasSize(3);
        var repoMirror = Domain.getDomainMirror().getDomainTypeMirror(MyRepository.class.getTypeName()).get();
        assertThat(called.methods()).contains(new DomainMethod(repoMirror.getTypeName(), repoMirror.methodByName("findById")));
        var outboundMirror = Domain.getDomainMirror().getDomainTypeMirror(MyOutboundService.class.getTypeName()).get();
        assertThat(called.methods()).contains(new DomainMethod(outboundMirror.getTypeName(), outboundMirror.methodByName("doSomething")));
    }

    @Test
    public void testCallsMyApplicationService(){
        var m = Domain.getDomainMirror().getDomainTypeMirror(MyApplicationService.class.getTypeName()).get();
        var method = m.getMethods().stream().filter(met -> met.getName().equals("doSomething")).findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(new DomainMethod(m.getTypeName(),method.get()));
        assertThat(called).isNotNull();
        assertThat(called.methods()).hasSize(4);
        var commandMirror = Domain.getDomainMirror().getDomainTypeMirror(MyDomainCommand.class.getTypeName()).get();
        assertThat(called.methods()).contains(new DomainMethod(commandMirror.getTypeName(), commandMirror.methodByName("id")));
        var repoMirror = Domain.getDomainMirror().getDomainTypeMirror(MyRepository.class.getTypeName()).get();
        assertThat(called.methods()).contains(new DomainMethod(repoMirror.getTypeName(), repoMirror.methodByName("findById")));
        assertThat(called.methods()).contains(new DomainMethod(repoMirror.getTypeName(), repoMirror.methodByName("update")));
        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(MyAggregateRoot.class.getTypeName()).get();
        assertThat(called.methods()).contains(new DomainMethod(aggregateRootMirror.getTypeName(), aggregateRootMirror.methodByName("doSomething")));
    }

    @Test
    public void testNestedLambdas() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();
        var method = m.getMethods().stream()
            .filter(met -> met.getName().equals("doNestedLambdas")).findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), method.get()));
        assertThat(called).isNotNull();

        var commandMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyDomainCommand.class.getTypeName()).get();
        var repoMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyRepository.class.getTypeName()).get();
        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAggregateRoot.class.getTypeName()).get();

        // command.id() and repository.findById(...) are at the top level
        assertThat(called.methods()).contains(new DomainMethod(
            commandMirror.getTypeName(), commandMirror.methodByName("id")));
        assertThat(called.methods()).contains(new DomainMethod(
            repoMirror.getTypeName(), repoMirror.methodByName("findById")));

        // a.doSomething(command) is nested three lambda levels deep -
        // this verifies the worklist descends through all invokedynamic levels
        assertThat(called.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("doSomething")));
    }

    @Test
    public void testMethodReference() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();
        var method = m.getMethods().stream()
            .filter(met -> met.getName().equals("doMethodReference")).findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), method.get()));
        assertThat(called).isNotNull();

        var repoMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyRepository.class.getTypeName()).get();
        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAggregateRoot.class.getTypeName()).get();

        // repository::update is a direct method reference to a domain method
        assertThat(called.methods()).contains(new DomainMethod(
            repoMirror.getTypeName(), repoMirror.methodByName("update")));

        // this::handle references handle(...), whose body calls a.doSomething(...);
        // whether doSomething shows up depends on whether method references are
        // followed into the referenced method
        assertThat(called.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("doSomething")));
    }

    @Test
    public void testDeferredLambdaDefinition() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();
        var method = m.getMethods().stream()
            .filter(met -> met.getName().equals("buildDeferredLambda")).findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), method.get()));
        assertThat(called).isNotNull();

        var repoMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyRepository.class.getTypeName()).get();

        // the lambda body calls repository.update(a); it is attributed to the
        // method that DEFINES the lambda (buildDeferredLambda), not the one that
        // later runs it
        assertThat(called.methods()).contains(new DomainMethod(
            repoMirror.getTypeName(), repoMirror.methodByName("update")));
    }

    @Test
    public void testPrivateHelperAsTarget() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();

        var doViaHelper = m.getMethods().stream()
            .filter(met -> met.getName().equals("doViaHelper")).findFirst();
        assertThat(doViaHelper).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), doViaHelper.get()));
        assertThat(called).isNotNull();

        var commandMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyDomainCommand.class.getTypeName()).get();
        var repoMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyRepository.class.getTypeName()).get();

        // doViaHelper directly calls command.id(), repository.findById(...)
        // and the private helper(...) of its own class
        assertThat(called.methods()).contains(new DomainMethod(
            commandMirror.getTypeName(), commandMirror.methodByName("id")));
        assertThat(called.methods()).contains(new DomainMethod(
            repoMirror.getTypeName(), repoMirror.methodByName("findById")));
        assertThat(called.methods()).contains(new DomainMethod(
            m.getTypeName(), m.methodByName("helper")));
    }

    @Test
    public void testPrivateHelperOwnCalls() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();

        var helper = m.getMethods().stream()
            .filter(met -> met.getName().equals("helper")).findFirst();
        assertThat(helper).isPresent();

        // helper is itself a domain method and thus its own entry: the call it makes
        // (a.doSomething(command)) is recorded under helper, not transitively under doViaHelper
        var calledByHelper = calls.callsFor(
            new DomainMethod(m.getTypeName(), helper.get()));
        assertThat(calledByHelper).isNotNull();

        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAggregateRoot.class.getTypeName()).get();
        assertThat(calledByHelper.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("doSomething")));
    }

    @Test
    public void testUnboundMethodReference() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();

        var method = m.getMethods().stream()
            .filter(met -> met.getName().equals("doUnboundMethodReference")).findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), method.get()));
        assertThat(called).isNotNull();

        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAggregateRoot.class.getTypeName()).get();

        // the unbound reference MyAggregateRoot::doSomethingNoArg should resolve to
        // the aggregate's domain method, attributed to MyAggregateRoot
        assertThat(called.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("doSomethingNoArg")));
    }

    @Test
    public void testSuperCall() {
        var serviceMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyOverridingService.class.getTypeName()).get();

        // the overriding process(...) in the subclass
        var overriding = serviceMirror.getMethods().stream()
            .filter(met -> met.getName().equals("process"))
            .findFirst();
        assertThat(overriding).isPresent();

        var called = calls.callsFor(
            new DomainMethod(serviceMirror.getTypeName(), overriding.get()));
        assertThat(called).isNotNull();

        var baseMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyBaseService.class.getTypeName()).get();

        // super.process(...) is an invokespecial on the base class, so the call
        // must be attributed to MyBaseService.process, NOT to the overriding
        // MyApplicationService.process (which would falsely imply self-recursion)
        assertThat(called.methods()).contains(new DomainMethod(
            baseMirror.getTypeName(), baseMirror.methodByName("process")));

        // and it must NOT be attributed to the subclass itself
        assertThat(called.methods()).doesNotContain(new DomainMethod(
            serviceMirror.getTypeName(), serviceMirror.methodByName("process")));
    }

    @Test
    public void testInheritedBodyIsResolvedPerConcreteOwner() {
        // baseTemplate(...) is declared and implemented once, in the abstract MyBaseService, and
        // inherited unchanged by both subclasses. Its body calls process(...) on `this`, which
        // dispatches to the override of whoever owns the instance - so the two owners of the very
        // same body must end up with different targets.
        var baseMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyBaseService.class.getTypeName()).get();
        var firstMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyOverridingService.class.getTypeName()).get();
        var secondMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MySecondOverridingService.class.getTypeName()).get();

        var calledByFirst = calls.callsFor(new DomainMethod(
            firstMirror.getTypeName(), firstMirror.methodByName("baseTemplate")));
        var calledBySecond = calls.callsFor(new DomainMethod(
            secondMirror.getTypeName(), secondMirror.methodByName("baseTemplate")));

        assertThat(calledByFirst.methods()).containsExactly(new DomainMethod(
            firstMirror.getTypeName(), firstMirror.methodByName("process")));
        assertThat(calledBySecond.methods()).containsExactly(new DomainMethod(
            secondMirror.getTypeName(), secondMirror.methodByName("process")));

        // in particular NOT the base class' process, which is what the declaring type of the
        // invoke instruction says and what a per-signature analysis would report for both
        assertThat(calledByFirst.methods()).doesNotContain(new DomainMethod(
            baseMirror.getTypeName(), baseMirror.methodByName("process")));

        // the call site is still the body it was found in - the base class
        assertThat(calledByFirst.callSites())
            .allMatch(callSite -> callSite.callSiteTypeName()
                .equals(MyBaseService.class.getTypeName()));
    }

    @Test
    public void testLambdaInAnInheritedBodyKeepsTheConcreteOwner() {
        // baseTemplate dispatches on `this` twice: once directly and once from inside a lambda.
        // The lambda body lives in MyBaseService too, but it runs on the same instance - so
        // descending into it must keep the concrete owner instead of falling back to the
        // declaring class.
        var firstMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyOverridingService.class.getTypeName()).get();
        var secondMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MySecondOverridingService.class.getTypeName()).get();

        var byFirst = calls.callsFor(new DomainMethod(
            firstMirror.getTypeName(), firstMirror.methodByName("baseTemplate")));
        var bySecond = calls.callsFor(new DomainMethod(
            secondMirror.getTypeName(), secondMirror.methodByName("baseTemplate")));

        // two call sites, one target: both dispatches land on the owner's own override
        assertThat(byFirst.callSites()).hasSize(2);
        assertThat(byFirst.methods()).containsExactly(new DomainMethod(
            firstMirror.getTypeName(), firstMirror.methodByName("process")));

        assertThat(bySecond.callSites()).hasSize(2);
        assertThat(bySecond.methods()).containsExactly(new DomainMethod(
            secondMirror.getTypeName(), secondMirror.methodByName("process")));
    }

    @Test
    public void testCallOnAConcreteBaseIsReportedOnTheStaticTypeOnly() {
        // the counterpart to the flow's IMPLEMENTATION edge: the call graph stays with the type
        // the code actually names. Expanding it here would merge "what is written" with "what
        // can run" into one flat list.
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();
        var baseMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyConcreteBaseService.class.getTypeName()).get();

        var called = calls.callsFor(new DomainMethod(
            m.getTypeName(), m.methodByName("doCallOnConcreteBase")));

        assertThat(called.methods()).containsExactly(new DomainMethod(
            baseMirror.getTypeName(), baseMirror.methodByName("execute")));
    }

    @Test
    public void testDefaultMethodIsAnalyzedForItsImplementation() {
        // a default method has a body, and that body is inherited through the INTERFACE, not
        // through a superclass - so it belongs to every implementation not overriding it
        var interfaceMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyPlainInterface.class.getTypeName()).get();
        var implMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyPlainInterfaceImpl.class.getTypeName()).get();

        var called = calls.callsFor(new DomainMethod(
            implMirror.getTypeName(), implMirror.methodByName("defaultOperation")));

        // declaredOnly(...) is an invokeinterface on `this`, so it resolves to the
        // implementation, not to the interface it is declared on
        assertThat(called.methods()).containsExactly(new DomainMethod(
            implMirror.getTypeName(), implMirror.methodByName("declaredOnly")));

        // the instruction itself stands in the interface's body
        assertThat(called.callSites()).allMatch(callSite -> callSite.callSiteTypeName()
            .equals(MyPlainInterface.class.getTypeName()));

        // the interface is no entry point of its own - it cannot be instantiated
        assertThat(calls.callsFor(new DomainMethod(interfaceMirror.getTypeName(),
            interfaceMirror.methodByName("defaultOperation"))).isEmpty()).isTrue();
    }

    @Test
    public void testStaticBodyIsAnalyzedWithoutAThisDispatch() {
        // a static body has no `this`, so nothing in it can be redispatched - reading the
        // enclosing instance must not fail either
        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAggregateRoot.class.getTypeName()).get();

        var called = calls.callsFor(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("staticValidate")));

        assertThat(called.methods()).containsExactly(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("doSomethingNoArg")));
    }

    @Test
    public void testAbstractOwnerIsNoEntryPoint() {
        // MyBaseService cannot be instantiated, so its inherited body never runs "as
        // MyBaseService" - the calls are held under the concrete owners only
        var baseMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyBaseService.class.getTypeName()).get();

        assertThat(calls.callsFor(new DomainMethod(
            baseMirror.getTypeName(), baseMirror.methodByName("baseTemplate"))).isEmpty())
            .isTrue();
    }

    @Test
    public void testOverloadedMethods() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();

        var method = m.getMethods().stream()
            .filter(met -> met.getName().equals("doOverloaded")).findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), method.get()));
        assertThat(called).isNotNull();

        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAggregateRoot.class.getTypeName()).get();

        // resolve both overloads from the mirror explicitly by their parameter types
        var handleCommand = aggregateRootMirror.getMethods().stream()
            .filter(met -> met.getName().equals("handle"))
            .filter(met -> met.getParameters().size() == 1)
            .filter(met -> met.getParameters().get(0).getType().getTypeName()
                .equals(MyDomainCommand.class.getTypeName()))
            .findFirst();
        assertThat(handleCommand).isPresent();

        var handleId = aggregateRootMirror.getMethods().stream()
            .filter(met -> met.getName().equals("handle"))
            .filter(met -> met.getParameters().size() == 1)
            .filter(met -> met.getParameters().get(0).getType().getTypeName()
                .equals(MyAggregateRoot.Id.class.getTypeName()))
            .findFirst();
        assertThat(handleId).isPresent();

        // both overloads must appear as distinct targets
        assertThat(called.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(), handleCommand.get()));
        assertThat(called.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(), handleId.get()));

        // and they must be two separate entries, not one collapsed by name
        long handleCount = called.methods().stream()
            .filter(mc -> mc.name().equals("handle"))
            .count();
        assertThat(handleCount).isEqualTo(2);
    }

    @Test
    public void testGenericMethodWithBridge() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();

        var method = m.getMethods().stream()
            .filter(met -> met.getName().equals("doGenericCall")).findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), method.get()));
        assertThat(called).isNotNull();

        var repoMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyRepository.class.getTypeName()).get();

        // the generic findById must be resolved and attributed to MyRepository
        assertThat(called.methods()).contains(new DomainMethod(
            repoMirror.getTypeName(), repoMirror.methodByName("findById")));

        // findById must appear exactly once - not duplicated by a bridge method,
        // and not attributed to a synthetic method that the mirror does not even hold
        long findByIdCount = called.methods().stream()
            .filter(mc -> mc.name().equals("findById"))
            .count();
        assertThat(findByIdCount).isEqualTo(1);

        // no resolved target may point to a synthetic/bridge method that is absent
        // from the mirror - i.e. every resolved call mapped successfully
        // (implicitly checked: if a bridge had been picked and not mapped, findById
        // would be missing entirely; if mapped twice, count would be 2)
    }

    @Test
    public void testStaticMethodReference() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();

        var method = m.getMethods().stream()
            .filter(met -> met.getName().equals("doStaticMethodReference")).findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), method.get()));
        assertThat(called).isNotNull();

        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAggregateRoot.class.getTypeName()).get();

        // the static reference MyAggregateRoot::staticValidate must resolve to the
        // static method, attributed to MyAggregateRoot (via the handle-type fallback)
        var staticValidate = aggregateRootMirror.getMethods().stream()
            .filter(met -> met.getName().equals("staticValidate"))
            .findFirst();
        assertThat(staticValidate).isPresent();

        assertThat(called.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(), staticValidate.get()));

        // doSomething from the trailing ifPresent lambda must also be found
        assertThat(called.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("doSomething")));
    }

    @Test
    public void testStaticMethodReferenceCrossType() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();
        var method = m.getMethods().stream()
            .filter(met -> met.getName().equals("doStaticMethodReferenceCrossType"))
            .findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), method.get()));
        assertThat(called).isNotNull();

        var serviceMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyDomainService.class.getTypeName()).get();
        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAggregateRoot.class.getTypeName()).get();

        // validate must be attributed to MyDomainService (the handle/declaring type),
        // NOT to MyAggregateRoot (which is merely the first functional parameter)
        assertThat(called.methods()).contains(new DomainMethod(
            serviceMirror.getTypeName(), serviceMirror.methodByName("validate")));

        // it must NOT be misattributed to MyAggregateRoot
        assertThat(called.methods()).doesNotContain(new DomainMethod(
            aggregateRootMirror.getTypeName(), serviceMirror.methodByName("validate")));
    }

    @Test
    public void testUnboundMethodReference2() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();

        var method = m.getMethods().stream()
            .filter(met -> met.getName().equals("doUnboundMethodReference2")).findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), method.get()));
        assertThat(called).isNotNull();

        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAggregateRoot.class.getTypeName()).get();

        // the unbound reference MyAggregateRoot::describe must resolve to describe,
        // attributed to MyAggregateRoot (via the handle type, since no receiver is bound)
        assertThat(called.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("describe")));

        // the trailing lambda's call must also be present
        assertThat(called.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("doSomething")));
    }

    @Test
    public void testMethodReferenceOnInheritedMethod() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();
        var method = m.getMethods().stream()
            .filter(met -> met.getName().equals("doMethodRefOnInherited")).findFirst();
        assertThat(method).isPresent();

        var called = calls.callsFor(
            new DomainMethod(m.getTypeName(), method.get()));
        assertThat(called).isNotNull();

        var repoMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyRepository.class.getTypeName()).get();

        // Axis 1 - reporting: the reference must be reported on the receiver type
        // MyRepository (not the base Repository), even though the method is inherited
        assertThat(called.methods()).contains(new DomainMethod(
            repoMirror.getTypeName(), repoMirror.methodByName("someInheritedOperation")));

        var repoImplMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyRepositoryImpl.class.getTypeName()).get();

        // Axis 2 - descent: the call inside the impl's override is made via `this`,
        // so its call-site type is MyRepositoryImpl - the update call is attributed
        // to MyRepositoryImpl, not the MyRepository interface.
        assertThat(called.methods()).contains(new DomainMethod(
            repoImplMirror.getTypeName(), repoImplMirror.methodByName("update")));
    }

    @Test
    @org.junit.jupiter.api.Timeout(value = 30, unit = java.util.concurrent.TimeUnit.SECONDS)
    public void testRecursionTerminates() {
        var m = Domain.getDomainMirror().getDomainTypeMirror(
            MyApplicationService.class.getTypeName()).get();

        var commandMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyDomainCommand.class.getTypeName()).get();
        var repoMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyRepository.class.getTypeName()).get();
        var aggregateRootMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAggregateRoot.class.getTypeName()).get();

        // L1: lambda calls the enclosing method - must terminate and still find
        // the real domain calls (findById, doSomething)
        var l1 = new DomainMethod(
            m.getTypeName(), m.methodByName("recursiveViaLambda"));
        var calledL1 = calls.callsFor(l1);
        assertThat(calledL1).isNotNull();
        assertThat(calledL1.methods()).contains(new DomainMethod(
            repoMirror.getTypeName(), repoMirror.methodByName("findById")));
        assertThat(calledL1.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("doSomething")));

        // L2: direct self-recursion - must terminate, findById present,
        // and the self-call must NOT appear as a target (self-reference removed)
        var l2 = new DomainMethod(m.getTypeName(), m.methodByName("directRecursion"));
        var calledL2 = calls.callsFor(l2);
        assertThat(calledL2).isNotNull();
        assertThat(calledL2.methods()).contains(new DomainMethod(
            repoMirror.getTypeName(), repoMirror.methodByName("findById")));
        assertThat(calledL2.methods())
            .as("direct self-recursion must not list itself as a target")
            .doesNotContain(new DomainMethod(
                m.getTypeName(), m.methodByName("directRecursion")));

        // L3: mutual recursion across two methods - both must terminate and each
        // must reference the other plus its domain calls
        var mutualA = new DomainMethod(m.getTypeName(), m.methodByName("mutualA"));
        var calledA = calls.callsFor(mutualA);
        assertThat(calledA).isNotNull();
        assertThat(calledA.methods()).contains(new DomainMethod(
            m.getTypeName(), m.methodByName("mutualB")));

        var mutualB = new DomainMethod(m.getTypeName(), m.methodByName("mutualB"));
        var calledB = calls.callsFor(mutualB);
        assertThat(calledB).isNotNull();
        assertThat(calledB.methods()).contains(new DomainMethod(
            m.getTypeName(), m.methodByName("mutualA")));
        assertThat(calledB.methods()).contains(new DomainMethod(
            aggregateRootMirror.getTypeName(),
            aggregateRootMirror.methodByName("doSomething")));
    }

    @Test
    public void testAbstractAndBodylessMethodsProduceNoTargets() {
        // The whole analysis already ran in setup; reaching here without an exception
        // during analyze() is itself part of the assertion. We additionally verify
        // that abstract / interface-only methods simply carry no targets.

        var abstractMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyAbstractService.class.getTypeName());
        assertThat(abstractMirror).isPresent();

        var abstractMethod = abstractMirror.get().getMethods().stream()
            .filter(met -> met.getName().equals("abstractOperation"))
            .findFirst();
        assertThat(abstractMethod).isPresent();

        // an abstract method has no body: it must resolve to no called methods.
        // callsFor may return null (no entry created) or an empty CalledMethods -
        // both are acceptable; what matters is no exception and no targets.
        var abstractCall = new DomainMethod(
            abstractMirror.get().getTypeName(), abstractMethod.get());
        var calledAbstract = calls.callsFor(abstractCall);
        if (calledAbstract != null) {
            assertThat(calledAbstract.methods()).isEmpty();
        }

        var interfaceMirror = Domain.getDomainMirror().getDomainTypeMirror(
            MyPlainInterface.class.getTypeName());
        assertThat(interfaceMirror).isPresent();

        var interfaceMethod = interfaceMirror.get().getMethods().stream()
            .filter(met -> met.getName().equals("declaredOnly"))
            .findFirst();
        assertThat(interfaceMethod).isPresent();

        var interfaceCall = new DomainMethod(
            interfaceMirror.get().getTypeName(), interfaceMethod.get());
        var calledInterface = calls.callsFor(interfaceCall);
        if (calledInterface != null) {
            assertThat(calledInterface.methods()).isEmpty();
        }
    }
}
