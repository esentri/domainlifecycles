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
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.jimple.basic.Immediate;
import sootup.core.jimple.basic.Local;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.constant.MethodHandle;
import sootup.core.jimple.common.expr.AbstractInstanceInvokeExpr;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.expr.JDynamicInvokeExpr;
import sootup.core.jimple.common.expr.JSpecialInvokeExpr;
import sootup.core.jimple.common.stmt.AbstractDefinitionStmt;
import sootup.core.jimple.common.stmt.JInvokeStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.Body;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.typehierarchy.TypeHierarchy;
import sootup.core.types.ClassType;
import sootup.core.types.Type;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.views.JavaView;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link StaticAnalyzer} implementation on top of SootUp.
 * <p>
 * No global call graph is built. Instead the invoke instructions are read directly out of the
 * bodies of the mirrored methods, which keeps the analysis confined to the domain: only calls
 * whose target resolves to a mirrored type are kept, and only the bodies actually inspected are
 * translated to Jimple.
 * <p>
 * Out of scope by design: constructors and static initializers. The mirror does not model them as
 * {@link MethodMirror}, so {@code new SomeAggregate(...)} and {@code super(...)} do not appear in
 * the result and are not reported as a gap either.
 * <p>
 * Calls are resolved once per concrete owner, not once per {@link MethodSignature}. That matters
 * for an inherited, non-overridden method: its body lives in the base class, but a virtual
 * {@code this.someOverridable()} call inside it dispatches to the override of the concrete
 * subtype at runtime, and that is what is reported. The body itself is still read only once, see
 * {@link BodyScan}. {@code super.x()} is an {@code invokespecial} and therefore genuinely not
 * virtual: it stays attributed to the base class.
 * <p>
 * Remaining imprecision: a call through a <i>reference</i> whose static type is an interface or
 * an abstract class is reported on that type, not on the implementations it may dispatch to at
 * runtime. That is deliberate - it is the honest information about the code - and the
 * polymorphic continuation is added by the {@link FlowAnalyzer} as a separate kind of edge,
 * instead of being merged into the call graph.
 * <p>
 * Everything else that could not be analyzed is reported as a {@link Diagnostic} on the result -
 * most importantly a mirrored type missing from the classpath, which would otherwise silently
 * shrink the result.
 *
 * @author Mario Herb
 */
public class SootupStaticAnalyzer implements StaticAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(SootupStaticAnalyzer.class);

    /**
     * Everything the resolution steps need, so it does not have to be threaded through every
     * method signature. Also holds the caches and the diagnostics of one analysis run; a fresh
     * instance is created per {@link #analyze(DomainMirror, List)} call, which keeps the analyzer
     * itself stateless.
     */
    private static final class AnalysisContext {

        private final JavaView view;

        private final JavaIdentifierFactory idf;

        private final TypeHierarchy typeHierarchy;

        /**
         * The full qualified names of all mirrored types, used to keep the analysis inside the
         * domain.
         */
        private final Set<String> domainTypeNames;

        /** Memoizes the scan of a single method body, see {@link #scanBody}. */
        private final Map<MethodSignature, BodyScan> bodyScans = new HashMap<>();

        /** Memoizes the concrete realizations of a type. */
        private final Map<ClassType, Set<SootClass>> concreteImplementations = new HashMap<>();

        /** Memoizes the own and inherited methods of a concrete class. */
        private final Map<ClassType, Collection<MethodSignature>> methodsWithInherited =
            new HashMap<>();

        private final Set<Diagnostic> diagnostics = new LinkedHashSet<>();

        /** How often a body scan was requested, to tell the cache hit ratio. */
        private int bodyScanRequests;

        private AnalysisContext(JavaView view, Set<String> domainTypeNames) {
            this.view = view;
            this.idf = view.getIdentifierFactory();
            this.typeHierarchy = view.getTypeHierarchy();
            this.domainTypeNames = domainTypeNames;
        }

        private JavaView view() {
            return view;
        }

        private JavaIdentifierFactory idf() {
            return idf;
        }

        private TypeHierarchy typeHierarchy() {
            return typeHierarchy;
        }

        private Set<String> domainTypeNames() {
            return domainTypeNames;
        }

        private Set<Diagnostic> diagnostics() {
            return diagnostics;
        }

        private void report(Diagnostic diagnostic) {
            diagnostics.add(diagnostic);
        }
    }

    /**
     * The result of scanning one method body: the invoke instructions it contains, and the further
     * bodies that have to be scanned to attribute all of its calls (lambda bodies, method
     * reference targets).
     * <p>
     * This is a pure function of the body - deliberately so: it is memoized per method signature,
     * while resolving its invokes against the domain happens once per concrete owner. Without the
     * memoization a method reachable from many entry points has its bytecode translated to Jimple
     * again for every one of them, which is the expensive part of the analysis.
     */
    private record BodyScan(List<RawInvoke> invokes, List<Descent> descendInto) {

        private static final BodyScan EMPTY = new BodyScan(List.of(), List.of());
    }

    /**
     * One invoke instruction as it stands in the body, before it is resolved against the domain.
     *
     * @param target           the statically resolved target of the invoke
     * @param callSiteTypeName the type whose body contains the instruction
     * @param lineNumber       the source line of the instruction
     * @param thisDispatch     whether this is a virtual or interface dispatch on the enclosing
     *                         instance, i.e. a call whose actual target depends on the concrete
     *                         owner the body runs on
     */
    private record RawInvoke(MethodSignature target, String callSiteTypeName, int lineNumber,
                             boolean thisDispatch) {
    }

    /**
     * A further body to scan, together with the concrete owner it runs on.
     *
     * @param signature     the method whose body is to be scanned
     * @param ownerTypeName the concrete owner the body runs on, or {@code null} to keep the owner
     *                      of the body it was found in - which is what a lambda of the same class
     *                      does, its {@code this} is the enclosing instance
     */
    private record Descent(MethodSignature signature, String ownerTypeName) {
    }

    /**
     * A call target resolved to a concrete domain class, together with the position of the invoke
     * instruction it was resolved from.
     */
    private record ResolvedTarget(String concreteOwnerTypeName,
                                  MethodSignature signature,
                                  String callSiteTypeName,
                                  int lineNumber) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainCalls analyze(DomainMirror domainMirror, List<Path> classpath) {
        AnalysisContext ctx = new AnalysisContext(
            buildView(classpath),
            domainMirror.getAllDomainTypeMirrors().stream()
                .map(dtm -> dtm.getTypeName())
                .collect(Collectors.toCollection(HashSet::new)));

        // Signature -> set of concrete owner classes.
        // Multimap, because an inherited, non-overridden method shares the same
        // MethodSignature (pointing to the superclass) across multiple concrete classes.
        Map<MethodSignature, Set<String>> entryPointOwners = new LinkedHashMap<>();

        domainMirror.getAllDomainTypeMirrors().forEach(domainTypeMirror -> {
            ClassType domainClassType = ctx.idf().getClassType(domainTypeMirror.getTypeName());

            // A mirrored type missing from the classpath yields no calls at all - without a
            // diagnostic that is indistinguishable from a type that simply calls nothing.
            if (ctx.view().getClass(domainClassType).isEmpty()) {
                ctx.report(Diagnostic.typeNotOnClasspath(domainTypeMirror.getTypeName()));
                return;
            }

            for (SootClass concrete : concreteImplementations(ctx, domainClassType)) {
                String ownerName = concrete.getType().getFullyQualifiedName();
                collectMethodsWithInherited(ctx, concrete).forEach(sig ->
                    entryPointOwners
                        .computeIfAbsent(sig, k -> new LinkedHashSet<>())
                        .add(ownerName));
            }
        });

        DomainCalls.Builder builder = DomainCalls.builder();

        for (MethodSignature method : entryPointOwners.keySet()) {
            // Once per concrete owner, not once per signature: an inherited body dispatches
            // differently depending on the subtype it runs on.
            for (String ownerTypeName : entryPointOwners.get(method)) {

                // Read the invoke expressions directly from this method's body
                // (and its lambda bodies).
                Set<ResolvedTarget> targets =
                    resolveDomainCallsFromMethod(ctx, method, ownerTypeName);
                if (targets.isEmpty()) {
                    continue;
                }

                // Map each resolved target to its concrete owner class (handles
                // inherited methods, whose signature points to a base class).
                List<DomainCalls.CallSite> callSites = new ArrayList<>();
                for (ResolvedTarget target : targets) {
                    Optional<DomainMethod> called = mapToOwner(
                        domainMirror, target.signature(), target.concreteOwnerTypeName());
                    if (called.isEmpty()) {
                        if (!isCompilerGenerated(target.signature())) {
                            ctx.report(Diagnostic.targetNotInMirror(target.signature().toString(),
                                target.concreteOwnerTypeName()));
                        }
                        continue;
                    }
                    callSites.add(new DomainCalls.CallSite(
                        called.get(), target.callSiteTypeName(), target.lineNumber()));
                }
                if (callSites.isEmpty()) {
                    continue;
                }

                // Always mapped to the concrete calling class, not the declaring class.
                // Merging, not replacing: several signatures (e.g. a generic method and its
                // bridge) can map onto the same caller node.
                Optional<DomainMethod> caller = mapToOwner(domainMirror, method, ownerTypeName);
                if (caller.isEmpty()) {
                    if (!isCompilerGenerated(method)) {
                        ctx.report(Diagnostic.callerNotInMirror(method.toString(), ownerTypeName));
                    }
                    continue;
                }
                builder.add(caller.get(), callSites);
            }
        }

        log.debug("Analyzed {} entry points: {} body scan requests served by {} actual scans, "
                + "{} diagnostics.",
            entryPointOwners.size(), ctx.bodyScanRequests, ctx.bodyScans.size(),
            ctx.diagnostics().size());

        return builder.addAll(ctx.diagnostics()).build();
    }

    // ---------------------------------------------------------------------
    // View / classpath
    // ---------------------------------------------------------------------

    /**
     * Registers every classpath entry as its own input location. Classes are loaded lazily,
     * so providing the full classpath is cheap as long as we do not force-load
     * everything: only the bodies we actually inspect are translated to Jimple.
     */
    private JavaView buildView(List<Path> classpath) {
        List<AnalysisInputLocation> inputLocations = classpath.stream()
            .map(path -> (AnalysisInputLocation)
                new JavaClassPathAnalysisInputLocation(path.toString()))
            .collect(Collectors.toCollection(ArrayList::new));
        return new JavaView(inputLocations);
    }

    // ---------------------------------------------------------------------
    // Call resolution without a global call graph
    // ---------------------------------------------------------------------

    /**
     * Resolves the calls made from a single method <i>as run by one concrete owner</i>, by reading
     * the invoke expressions out of its body. Calls residing inside a lambda end up in a
     * synthetic method (lambda$...) of the same class; those are traversed and
     * their calls attributed to the original method. Lambdas and method
     * references are emitted as invokedynamic and handled separately.
     * <p>
     * The per-body work is done by {@link #scanBody}, which is memoized and owner independent.
     * Specific to this entry point are only the traversal over the descent targets and the
     * resolution of the invokes, which is where the owner comes in: the owner travels along the
     * worklist, because descending into a body of another class also changes whose instance
     * {@code this} is.
     */
    private Set<ResolvedTarget> resolveDomainCallsFromMethod(
        AnalysisContext ctx, MethodSignature caller, String ownerTypeName) {

        Set<ResolvedTarget> result = new LinkedHashSet<>();
        Deque<Descent> worklist = new ArrayDeque<>();
        Set<Descent> visited = new HashSet<>();

        worklist.add(new Descent(caller, ownerTypeName));

        while (!worklist.isEmpty()) {
            Descent current = worklist.poll();
            if (!visited.add(current)) {
                continue;
            }

            ctx.bodyScanRequests++;
            BodyScan scan =
                ctx.bodyScans.computeIfAbsent(current.signature(), sig -> scanBody(ctx, sig));

            for (RawInvoke invoke : scan.invokes()) {
                result.addAll(resolveInvoke(ctx, invoke, current.ownerTypeName()));
            }
            for (Descent descent : scan.descendInto()) {
                worklist.add(descent.ownerTypeName() == null
                    ? new Descent(descent.signature(), current.ownerTypeName())
                    : descent);
            }
        }

        // remove self-reference (caller calling itself). Comparing the owner and the
        // sub-signature as well, because a redispatched call carries the owner's signature
        // while the entry point may still carry the base class' one.
        result.removeIf(rt -> rt.signature().equals(caller)
            || (rt.concreteOwnerTypeName().equals(ownerTypeName)
            && rt.signature().getSubSignature().equals(caller.getSubSignature())));
        return result;
    }

    /**
     * Resolves one invoke instruction against the domain, for the concrete owner the containing
     * body runs on.
     * <p>
     * A virtual or interface dispatch on {@code this} is the one case where the static target is
     * knowably wrong: the receiver is the concrete owner, so the call is re-resolved against its
     * method table. Everything else is resolved against the static type at the call site, which
     * is the honest information the code gives.
     */
    private Set<ResolvedTarget> resolveInvoke(
        AnalysisContext ctx, RawInvoke invoke, String ownerTypeName) {

        if (invoke.thisDispatch()) {
            Optional<ResolvedTarget> redispatched = redispatchOnOwner(ctx, invoke, ownerTypeName);
            if (redispatched.isPresent()) {
                return Collections.singleton(redispatched.get());
            }
        }
        return resolveTargetsAgainstDomain(
            ctx, invoke.target(), invoke.callSiteTypeName(), invoke.lineNumber());
    }

    /**
     * Re-resolves a {@code this} dispatch against the method table of the concrete owner, so that
     * an overridden method is attributed to the overriding subtype rather than to the base class
     * the body happens to live in.
     * <p>
     * Empty if the owner is unknown to the mirror, or if it does not actually realize the invoked
     * type - in which case {@code this} is not what it was taken for and the static target is
     * kept.
     */
    private Optional<ResolvedTarget> redispatchOnOwner(
        AnalysisContext ctx, RawInvoke invoke, String ownerTypeName) {

        if (ownerTypeName == null || !ctx.domainTypeNames().contains(ownerTypeName)) {
            return Optional.empty();
        }
        ClassType ownerType = ctx.idf().getClassType(ownerTypeName);
        Optional<SootClass> ownerClass =
            concreteImplementations(ctx, invoke.target().getDeclClassType()).stream()
                .filter(impl -> impl.getType().equals(ownerType))
                .findFirst();
        if (ownerClass.isEmpty()) {
            return Optional.empty();
        }

        String subSignature = invoke.target().getSubSignature().toString();
        return collectMethodsWithInherited(ctx, ownerClass.get()).stream()
            .filter(sig -> sig.getSubSignature().toString().equals(subSignature))
            .findFirst()
            .map(sig -> new ResolvedTarget(
                ownerTypeName, sig, invoke.callSiteTypeName(), invoke.lineNumber()));
    }

    /**
     * Reads the invoke expressions out of one method body and splits them into the invokes it
     * contains and the bodies that still have to be scanned to attribute all of its calls.
     * <p>
     * Depends on nothing but the body itself, which is what makes it memoizable - in particular
     * it does not resolve anything against a concrete owner, that happens in
     * {@link #resolveInvoke}. A synthetic lambda invoke is recognized relative to the scanned
     * body's own class, so descending into a body of another class (via a method reference) also
     * picks up that class's lambdas.
     */
    private BodyScan scanBody(AnalysisContext ctx, MethodSignature signature) {
        Optional<Body> bodyOpt = bodyOf(ctx, signature);
        if (bodyOpt.isEmpty()) {
            return BodyScan.EMPTY;
        }
        Body body = bodyOpt.get();
        Local thisLocal = thisLocalOf(body);

        List<RawInvoke> invokes = new ArrayList<>();
        List<Descent> descendInto = new ArrayList<>();
        String callSiteTypeName = signature.getDeclClassType().getFullyQualifiedName();

        for (Stmt stmt : body.getStmts()) {
            AbstractInvokeExpr invokeExpr = invokeExprOf(stmt);
            if (invokeExpr == null) {
                continue;
            }
            int lineNumber = lineNumberOf(stmt);

            if (invokeExpr instanceof JDynamicInvokeExpr dyn) {
                handleDynamicInvoke(ctx, dyn, signature, callSiteTypeName, lineNumber,
                    descendInto, invokes);
                continue;
            }

            MethodSignature target = invokeExpr.getMethodSignature();

            // direct synthetic lambda invoke of the scanned body's own class
            if (isSyntheticLambdaOf(signature, target)) {
                descendInto.add(new Descent(target, null));
                continue;
            }

            invokes.add(new RawInvoke(target, callSiteTypeName, lineNumber,
                isThisDispatch(invokeExpr, thisLocal)));
        }

        return new BodyScan(List.copyOf(invokes), List.copyOf(descendInto));
    }

    /**
     * The local holding the enclosing instance of a body, or {@code null} for a static method,
     * which has none. SootUp signals the absence by throwing.
     */
    private Local thisLocalOf(Body body) {
        try {
            return body.getThisLocal();
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Whether an invoke dispatches virtually on the enclosing instance, i.e. whether its actual
     * target depends on the concrete owner rather than on the type written at the call site.
     * <p>
     * {@code invokespecial} - {@code super.x()}, a private method, a constructor - is excluded:
     * its target is fixed at compile time, so re-resolving it against the owner would turn
     * {@code super.process()} into a call to the overriding {@code process()}, i.e. into
     * self-recursion that does not exist.
     */
    private boolean isThisDispatch(AbstractInvokeExpr invokeExpr, Local thisLocal) {
        if (thisLocal == null || invokeExpr instanceof JSpecialInvokeExpr) {
            return false;
        }
        return invokeExpr instanceof AbstractInstanceInvokeExpr instanceInvoke
            && thisLocal.equals(instanceInvoke.getBase());
    }

    /**
     * Handles a lambda/method-reference invokedynamic. For each method referenced
     * by a method handle in the bootstrap args:
     *  - synthetic lambda$ methods are descended into (their body holds the calls),
     *  - method references to real methods are reported as a domain target AND
     *    descended into.
     * The reporting type is taken from the bound receiver (the first dynamic
     * argument): its static type is the variable type at the call site (e.g. the
     * interface MyRepository), which is what should appear in the result - matching
     * the normal invoke path. The descent still goes into the concrete
     * implementation via resolveTargetsAgainstDomain / the worklist.
     */
    private void handleDynamicInvoke(
        AnalysisContext ctx, JDynamicInvokeExpr dyn, MethodSignature scannedBody,
        String callSiteTypeName, int lineNumber,
        List<Descent> descendInto, List<RawInvoke> invokes) {

        // static type of the bound receiver (first dynamic arg), if any
        Optional<ClassType> receiverType = boundReceiverType(dyn);

        for (Immediate bootstrapArg : dyn.getBootstrapArgs()) {
            if (!(bootstrapArg instanceof MethodHandle methodHandle)) {
                continue;
            }
            MethodSignature handleMethod = referencedMethodSignature(methodHandle);
            if (handleMethod == null) {
                continue; // field handle (record accessor etc.)
            }

            if (handleMethod.getName().startsWith("lambda$")) {
                // synthetic lambda body - its body lives in the (same) class, so the
                // handle signature is directly loadable: descend into it, not a target.
                descendInto.add(descentOn(scannedBody, handleMethod));
                continue;
            }

            // real method reference. Prefer the receiver's static type (e.g.
            // MyRepository) as the call-site type, rebuilding the signature on that
            // type with the handle's sub-signature, so the reported target matches
            // the normal invoke path.
            MethodSignature reportSignature = handleMethod;
            if (receiverType.isPresent()
                && ctx.domainTypeNames().contains(receiverType.get().getFullyQualifiedName())) {
                reportSignature = ctx.idf().getMethodSignature(
                    receiverType.get(), handleMethod.getSubSignature());
            }

            // report against the domain - on the receiver type this yields e.g.
            // MyRepository.someOperation as a direct hit, which is the call-site type we
            // want in the result. Not a `this` dispatch: the receiver is the bound
            // variable, not the enclosing instance.
            invokes.add(new RawInvoke(reportSignature, callSiteTypeName, lineNumber, false));

            // DESCENT is separate from reporting: descend into the concrete
            // implementation bodies. The reporting owner may be an interface (direct
            // hit), whose method has no body - so we explicitly resolve the concrete
            // implementations of the reporting type and descend into their loadable
            // method with the same sub-signature.
            descendInto.addAll(concreteDescentTargets(ctx, reportSignature));
            // also descend into the raw handle method, in case its body genuinely
            // lives on the declaring type (e.g. a static method or a default method)
            descendInto.add(descentOn(scannedBody, handleMethod));
        }
    }

    /**
     * Returns the bodies to descend into for a call whose reporting signature is given, each
     * with the concrete owner it runs on. If the reporting type is concrete, that method is
     * returned. If it is an interface or abstract type, the same method (by sub-signature) on
     * every concrete implementation is returned, each built on the concrete class so its body
     * can actually be loaded. This is used for the descent only - reporting keeps the call-site
     * (interface) type.
     */
    private Set<Descent> concreteDescentTargets(
        AnalysisContext ctx, MethodSignature reportSignature) {

        Set<Descent> descent = new LinkedHashSet<>();
        String subSignature = reportSignature.getSubSignature().toString();

        for (SootClass impl : concreteImplementations(ctx, reportSignature.getDeclClassType())) {
            collectMethodsWithInherited(ctx, impl).stream()
                .filter(sig -> sig.getSubSignature().toString().equals(subSignature))
                .findFirst()
                .ifPresent(sig -> descent.add(
                    new Descent(sig, impl.getType().getFullyQualifiedName())));
        }
        return descent;
    }

    /**
     * Builds the descent into a referenced method. A method of the scanned body's own class
     * keeps the current owner - a lambda or a private helper runs on the very same instance -
     * while a method of another class switches the owner to that class, because its {@code this}
     * is a different object.
     */
    private Descent descentOn(MethodSignature scannedBody, MethodSignature target) {
        return target.getDeclClassType().equals(scannedBody.getDeclClassType())
            ? new Descent(target, null)
            : new Descent(target, target.getDeclClassType().getFullyQualifiedName());
    }

    /**
     * Returns the static type of the bound receiver of a method-reference
     * invokedynamic, i.e. the type of the first dynamic argument, if present and
     * a class type. For unbound/static references there is no such receiver.
     */
    private Optional<ClassType> boundReceiverType(JDynamicInvokeExpr dyn) {
        if (dyn.getArgCount() == 0) {
            return Optional.empty();
        }
        Type t = dyn.getArg(0).getType();
        if (t instanceof ClassType ct) {
            return Optional.of(ct);
        }
        return Optional.empty();
    }

    /**
     * Reads the method signature referenced by a MethodHandle constant, or null
     * if the handle references a field (e.g. a record accessor) rather than a
     * method.
     */
    private MethodSignature referencedMethodSignature(MethodHandle methodHandle) {
        Object ref = methodHandle.getReferenceSignature();
        if (ref instanceof MethodSignature ms) {
            return ms;
        }
        return null;
    }

    /**
     * Resolves a static call-site target to the matching domain method signatures.
     * If the target's declaring type is itself a domain type, it is returned as is.
     * Otherwise, if it is an interface or abstract type, the same method is looked
     * up on every concrete implementation that is a domain type, so that calls made
     * through an interface reference resolve to the concrete domain implementations.
     * The method may be inherited, so its signature can point to a base class; it is
     * nevertheless attributed to the concrete implementation.
     */
    private Set<ResolvedTarget> resolveTargetsAgainstDomain(
        AnalysisContext ctx, MethodSignature target,
        String callSiteTypeName, int lineNumber) {

        String declName = target.getDeclClassType().getFullyQualifiedName();

        // direct hit: declaring type is itself a domain type
        if (ctx.domainTypeNames().contains(declName)) {
            return Collections.singleton(
                new ResolvedTarget(declName, target, callSiteTypeName, lineNumber));
        }

        // otherwise expand to concrete implementations that are domain types
        Set<ResolvedTarget> resolved = new LinkedHashSet<>();
        String subSignature = target.getSubSignature().toString();

        for (SootClass impl : concreteImplementations(ctx, target.getDeclClassType())) {

            String implName = impl.getType().getFullyQualifiedName();
            if (!ctx.domainTypeNames().contains(implName)) {
                continue;
            }
            // find the method with the same sub-signature on the implementation
            // (own or inherited) and attribute it to the concrete implementation
            collectMethodsWithInherited(ctx, impl).stream()
                .filter(sig -> sig.getSubSignature().toString().equals(subSignature))
                .findFirst()
                .ifPresent(sig -> resolved.add(
                    new ResolvedTarget(implName, sig, callSiteTypeName, lineNumber)));
        }

        return resolved;
    }

    /**
     * Extracts the invoke expression of a statement, or null if it has none.
     * Invoke-bearing statements are either a plain invoke statement, or a
     * definition/assignment whose right-hand side is an invoke expression.
     * This goes through the concrete statement types instead of Stmt's
     * invoke API, which changed across SootUp versions.
     */
    private AbstractInvokeExpr invokeExprOf(Stmt stmt) {
        if (stmt instanceof JInvokeStmt invokeStmt) {
            return invokeStmt.getInvokeExpr().orElse(null);
        }
        if (stmt instanceof AbstractDefinitionStmt defStmt) {
            Value rhs = defStmt.getRightOp();
            if (rhs instanceof AbstractInvokeExpr invokeExpr) {
                return invokeExpr;
            }
        }
        return null;
    }

    /**
     * Reads the source line of a statement from the line number table of the analyzed bytecode,
     * or {@link DomainCalls.CallSite#UNKNOWN_LINE} if it carries no position information.
     */
    private int lineNumberOf(Stmt stmt) {
        try {
            int line = stmt.getPositionInfo().getStmtPosition().getFirstLine();
            return line > 0 ? line : DomainCalls.CallSite.UNKNOWN_LINE;
        } catch (RuntimeException e) {
            return DomainCalls.CallSite.UNKNOWN_LINE;
        }
    }

    /**
     * Loads the body of a method, swallowing resolution errors.
     * <p>
     * The three ways this can come up empty are told apart, because they mean different things: a
     * method without a body is normal (abstract or interface methods) and not reported, a method
     * missing from the classpath is expected for references outside it and reported as info, and
     * a body that fails to translate is a real gap and reported as a warning.
     */
    private Optional<Body> bodyOf(AnalysisContext ctx, MethodSignature signature) {
        try {
            Optional<? extends SootMethod> methodOpt = ctx.view().getMethod(signature);
            if (methodOpt.isEmpty()) {
                ctx.report(Diagnostic.methodNotOnClasspath(signature.toString()));
                return Optional.empty();
            }
            SootMethod method = methodOpt.get();
            if (!method.hasBody()) {
                // abstract or interface method: nothing to analyze, nothing to report
                return Optional.empty();
            }
            return Optional.of(method.getBody());
        } catch (RuntimeException e) {
            ctx.report(Diagnostic.bodyNotLoadable(signature.toString(),
                e.getClass().getSimpleName()
                    + (e.getMessage() == null ? "" : ": " + e.getMessage())));
            return Optional.empty();
        }
    }

    /**
     * Whether a method exists only in the bytecode and has no counterpart in the mirror by design.
     * <p>
     * Constructors and static initializers are not modelled as {@link MethodMirror} at all,
     * and synthetic methods (lambda bodies, enum {@code $values}, {@code access$}
     * bridges) are an implementation detail of the compiler. Failing to map them is expected, not
     * a gap worth reporting: a lambda body's calls are attributed to the method that defines it.
     */
    private boolean isCompilerGenerated(MethodSignature signature) {
        String name = signature.getName();
        return name.startsWith("<")
            || name.startsWith("$")
            || name.startsWith("lambda$")
            || name.startsWith("access$");
    }

    private boolean isSyntheticLambdaOf(MethodSignature caller, MethodSignature target) {
        return target.getDeclClassType().equals(caller.getDeclClassType())
            && target.getName().startsWith("lambda$");
    }

    // ---------------------------------------------------------------------
    // Downward inheritance: concrete implementations of a domain type
    // ---------------------------------------------------------------------

    /**
     * Returns the instantiable (non-abstract, non-interface) classes that realize
     * the given type. For a concrete class this is the class itself; for an
     * interface all implementers, for an abstract class all concrete subclasses
     * (each transitively).
     */
    private Set<SootClass> concreteImplementations(AnalysisContext ctx, ClassType classType) {
        return ctx.concreteImplementations.computeIfAbsent(classType,
            type -> resolveConcreteImplementations(ctx, type));
    }

    private Set<SootClass> resolveConcreteImplementations(
        AnalysisContext ctx, ClassType classType) {

        if (!ctx.typeHierarchy().contains(classType)) {
            return Collections.emptySet();
        }

        Set<SootClass> result = new LinkedHashSet<>();

        ctx.view().getClass(classType).ifPresent(cls -> {
            if (!cls.isAbstract() && !cls.isInterface()) {
                result.add(cls);
            }
        });

        Collection<ClassType> candidates =
            ctx.typeHierarchy().isInterface(classType)
                ? ctx.typeHierarchy().implementersOf(classType).collect(Collectors.toList())
                : ctx.typeHierarchy().subclassesOf(classType).collect(Collectors.toList());

        for (ClassType sub : candidates) {
            ctx.view().getClass(sub).ifPresent(subCls -> {
                if (!subCls.isAbstract() && !subCls.isInterface()) {
                    result.add(subCls);
                }
            });
        }

        return result;
    }

    // ---------------------------------------------------------------------
    // Upward inheritance: own + inherited methods of a concrete class
    // ---------------------------------------------------------------------

    /**
     * Collects own and inherited methods of a concrete class. Deduplicates by
     * sub-signature so that on overriding the most specific (own) variant wins.
     * superClassesOf returns the full superclass chain (transitively), so own
     * methods are added first to take precedence over inherited ones.
     * <p>
     * Implemented interfaces are collected as well, and last: a {@code default} method has a
     * body and is inherited through the interface rather than through the superclass chain, so
     * without them the calls made by a default method that nobody overrides would be missing
     * from the result entirely. Being last is what makes an implementation of the method win
     * over the default.
     */
    private Collection<MethodSignature> collectMethodsWithInherited(
        AnalysisContext ctx, SootClass concrete) {

        return ctx.methodsWithInherited.computeIfAbsent(concrete.getType(),
            type -> resolveMethodsWithInherited(ctx, concrete));
    }

    private Collection<MethodSignature> resolveMethodsWithInherited(
        AnalysisContext ctx, SootClass concrete) {

        Map<String, MethodSignature> collected = new LinkedHashMap<>();

        // own methods first so they win on overriding
        collectInto(concrete, collected);

        // all superclasses (transitively, up to Object)
        ctx.typeHierarchy().superClassesOf(concrete.getType()).forEach(superType ->
            ctx.view().getClass(superType).ifPresent(superCls ->
                collectInto(superCls, collected)));

        // and finally the interfaces, for their default methods
        allInterfacesOf(ctx, concrete).forEach(interfaceType ->
            ctx.view().getClass(interfaceType).ifPresent(
                interfaceCls -> collectInto(interfaceCls, collected)));

        return collected.values();
    }

    private void collectInto(SootClass declaring, Map<String, MethodSignature> collected) {
        declaring.getMethods().forEach(method ->
            collected.putIfAbsent(method.getSignature().getSubSignature().toString(),
                method.getSignature()));
    }

    /**
     * All interfaces a class realizes: its own, those of its superclasses, and the interfaces
     * those extend, transitively. Walked over the classes rather than asked of the hierarchy,
     * because both directions - up the superclass chain and up the interface chain - contribute.
     */
    private Set<ClassType> allInterfacesOf(AnalysisContext ctx, SootClass concrete) {
        Set<ClassType> interfaces = new LinkedHashSet<>();
        Deque<ClassType> worklist = new ArrayDeque<>();

        worklist.add(concrete.getType());
        ctx.typeHierarchy().superClassesOf(concrete.getType()).forEach(worklist::add);

        while (!worklist.isEmpty()) {
            ClassType current = worklist.poll();
            ctx.view().getClass(current).ifPresent(cls ->
                cls.getInterfaces().forEach(interfaceType -> {
                    if (interfaces.add(interfaceType)) {
                        worklist.add(interfaceType);
                    }
                }));
        }
        return interfaces;
    }

    // ---------------------------------------------------------------------
    // Mapping Soot signature -> DomainMirror
    // ---------------------------------------------------------------------

    /**
     * Maps a method to the CONCRETE owner type (not the declaring class of the
     * signature). Finds the method by name + parameters, independent of the
     * declaring class, so inherited methods are attributed to the concrete owner.
     */
    private Optional<DomainMethod> mapToOwner(
        DomainMirror domainMirror, MethodSignature methodSignature, String ownerTypeName) {

        return domainMirror.getDomainTypeMirror(ownerTypeName)
            .flatMap(domainTypeMirror -> domainTypeMirror.getMethods()
                .stream()
                .filter(m -> areSemanticallyEqualIgnoringDeclaringClass(methodSignature, m))
                .map(m -> new DomainMethod(domainTypeMirror.getTypeName(), m))
                .findFirst());
    }

    // ---------------------------------------------------------------------
    // Signature comparisons
    // ---------------------------------------------------------------------

    /**
     * Compares a Soot signature and a mirror method by name and parameters,
     * ignoring the declaring class - so inherited methods (whose Soot signature
     * points to a base class) still match the concrete owner's mirror method.
     */
    private static boolean areSemanticallyEqualIgnoringDeclaringClass(
        MethodSignature sootSignature, MethodMirror dlcMirror) {
        if (sootSignature == null || dlcMirror == null) {
            return false;
        }
        if (!sootSignature.getName().equals(dlcMirror.getName())) {
            return false;
        }
        return parametersMatch(sootSignature, dlcMirror);
    }

    private static boolean parametersMatch(MethodSignature sootSignature, MethodMirror dlcMirror) {
        List<Type> sootParams = sootSignature.getParameterTypes();
        List<ParamMirror> dlcParams = dlcMirror.getParameters();
        if (sootParams.size() != dlcParams.size()) {
            return false;
        }
        for (int i = 0; i < sootParams.size(); i++) {
            String sootParamType = sootParams.get(i).toString();
            String dlcParamType = dlcParams.get(i).getType().getTypeName();
            if (!sootParamType.equals(dlcParamType)) {
                return false;
            }
        }
        return true;
    }
}
