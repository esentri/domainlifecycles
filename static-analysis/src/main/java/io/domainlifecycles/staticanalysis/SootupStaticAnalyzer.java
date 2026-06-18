package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import sootup.core.inputlocation.AnalysisInputLocation;
import sootup.core.jimple.basic.Immediate;
import sootup.core.jimple.basic.Value;
import sootup.core.jimple.common.constant.MethodHandle;
import sootup.core.jimple.common.expr.AbstractInvokeExpr;
import sootup.core.jimple.common.expr.JDynamicInvokeExpr;
import sootup.core.jimple.common.stmt.AbstractDefinitionStmt;
import sootup.core.jimple.common.stmt.JInvokeStmt;
import sootup.core.jimple.common.stmt.Stmt;
import sootup.core.model.Body;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.signatures.MethodSubSignature;
import sootup.core.typehierarchy.TypeHierarchy;
import sootup.core.types.ClassType;
import sootup.core.types.Type;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.views.JavaView;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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

public class SootupStaticAnalyzer implements StaticAnalyzer {

    /** A call target resolved to a concrete domain class. */
    private record ResolvedTarget(String concreteOwnerTypeName, MethodSignature signature) {}

    public DomainCalls analyze(DomainMirror domainMirror, String classpath) {
        JavaView view = buildView(classpath);
        JavaIdentifierFactory idf = view.getIdentifierFactory();
        TypeHierarchy typeHierarchy = view.getTypeHierarchy();

        // Set of fully qualified names of all domain types. Used to keep the
        // analysis confined to the mirrored classes: only calls whose target
        // resolves to a domain type are kept, so we never descend into JDK or
        // third-party dependency code.
        Set<String> domainTypeNames = new HashSet<>();
        domainMirror.getAllDomainTypeMirrors()
            .forEach(dtm -> domainTypeNames.add(dtm.getTypeName()));

        // Signature -> set of concrete owner classes.
        // Multimap, because an inherited, non-overridden method shares the same
        // MethodSignature (pointing to the superclass) across multiple concrete classes.
        Map<MethodSignature, Set<String>> entryPointOwners = new LinkedHashMap<>();

        domainMirror.getAllDomainTypeMirrors().forEach(domainTypeMirror -> {
            ClassType domainClassType = idf.getClassType(domainTypeMirror.getTypeName());

            for (SootClass concrete : concreteImplementations(view, typeHierarchy, domainClassType)) {
                String ownerName = concrete.getType().getFullyQualifiedName();
                collectMethodsWithInherited(view, typeHierarchy, concrete).forEach(sig ->
                    entryPointOwners
                        .computeIfAbsent(sig, k -> new LinkedHashSet<>())
                        .add(ownerName));
            }
        });

        Map<DomainCalls.MethodCall, DomainCalls.CalledMethods> domainCalls = new HashMap<>();

        for (MethodSignature method : entryPointOwners.keySet()) {
            // Read the invoke expressions directly from this method's body
            // (and its lambda bodies). No global call graph is built, so only
            // the bodies of the mirrored classes are loaded and translated.
            Set<ResolvedTarget> targets =
                resolveDomainCallsFromMethod(view, idf, typeHierarchy, method, domainTypeNames);
            if (targets.isEmpty()) continue;

            // Map each resolved target to its concrete owner class (handles
            // inherited methods, whose signature points to a base class).
            List<DomainCalls.MethodCall> targetMethods = targets.stream()
                .map(rt -> mapToOwner(domainMirror, rt.signature(), rt.concreteOwnerTypeName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
            if (targetMethods.isEmpty()) continue;

            // Each concrete owner of this signature gets its own entry, always
            // mapped to the concrete calling class (not the declaring class).
            for (String ownerTypeName : entryPointOwners.get(method)) {
                mapToOwner(domainMirror, method, ownerTypeName).ifPresent(caller ->
                    domainCalls.put(caller, new DomainCalls.CalledMethods(targetMethods)));
            }
        }

        return new DomainCalls(domainCalls);
    }

    // ---------------------------------------------------------------------
    // View / classpath
    // ---------------------------------------------------------------------

    /**
     * Splits the externally provided classpath at the platform separator and
     * registers each path as its own input location. Classes are loaded lazily,
     * so providing the full classpath is cheap as long as we do not force-load
     * everything: only the bodies we actually inspect are translated to Jimple.
     */
    private JavaView buildView(String classpath) {
        List<AnalysisInputLocation> inputLocations =
            Arrays.stream(classpath.split(File.pathSeparator))
                .filter(p -> !p.isBlank())
                .map(p -> (AnalysisInputLocation) new JavaClassPathAnalysisInputLocation(p))
                .collect(Collectors.toCollection(ArrayList::new));
        return new JavaView(inputLocations);
    }

    // ---------------------------------------------------------------------
    // Call resolution without a global call graph
    // ---------------------------------------------------------------------

    /**
     * Resolves the calls made from a single method by reading the invoke
     * expressions out of its body. Calls residing inside a lambda end up in a
     * synthetic method (lambda$...) of the same class; those are traversed and
     * their calls attributed to the original method. Lambdas and method
     * references are emitted as invokedynamic and handled separately.
     */
    private Set<ResolvedTarget> resolveDomainCallsFromMethod(
        JavaView view, JavaIdentifierFactory idf, TypeHierarchy typeHierarchy,
        MethodSignature caller, Set<String> domainTypeNames) {

        Set<ResolvedTarget> result = new LinkedHashSet<>();
        Deque<MethodSignature> worklist = new ArrayDeque<>();
        Set<MethodSignature> visited = new HashSet<>();

        worklist.add(caller);

        while (!worklist.isEmpty()) {
            MethodSignature current = worklist.poll();
            if (!visited.add(current)) continue;

            Optional<Body> bodyOpt = bodyOf(view, current);
            if (bodyOpt.isEmpty()) continue;

            for (Stmt stmt : bodyOpt.get().getStmts()) {
                AbstractInvokeExpr invokeExpr = invokeExprOf(stmt);
                if (invokeExpr == null) continue;

                if (invokeExpr instanceof JDynamicInvokeExpr dyn) {
                    handleDynamicInvoke(view, idf, typeHierarchy, dyn,
                        domainTypeNames, worklist, result);
                    continue;
                }

                MethodSignature target = invokeExpr.getMethodSignature();

                // direct synthetic lambda invoke of the same class
                if (isSyntheticLambdaOf(caller, target)) {
                    worklist.add(target);
                    continue;
                }

                result.addAll(resolveTargetsAgainstDomain(
                    view, typeHierarchy, target, domainTypeNames));
            }
        }

        // remove self-reference (caller calling itself by the same signature)
        result.removeIf(rt -> rt.signature().equals(caller));
        return result;
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
        JavaView view, JavaIdentifierFactory idf, TypeHierarchy typeHierarchy,
        JDynamicInvokeExpr dyn, Set<String> domainTypeNames,
        Deque<MethodSignature> worklist, Set<ResolvedTarget> result) {

        // static type of the bound receiver (first dynamic arg), if any
        Optional<ClassType> receiverType = boundReceiverType(dyn);

        for (Immediate bootstrapArg : dyn.getBootstrapArgs()) {
            if (!(bootstrapArg instanceof MethodHandle methodHandle)) continue;
            MethodSignature handleMethod = referencedMethodSignature(methodHandle);
            if (handleMethod == null) continue; // field handle (record accessor etc.)

            if (handleMethod.getName().startsWith("lambda$")) {
                // synthetic lambda body - its body lives in the (same) class, so the
                // handle signature is directly loadable: descend into it, not a target.
                worklist.add(handleMethod);
                continue;
            }

            // real method reference. Prefer the receiver's static type (e.g.
            // MyRepository) as the call-site type, rebuilding the signature on that
            // type with the handle's sub-signature, so the reported target matches
            // the normal invoke path.
            MethodSignature reportSignature = handleMethod;
            if (receiverType.isPresent()
                && domainTypeNames.contains(receiverType.get().getFullyQualifiedName())) {
                reportSignature = idf.getMethodSignature(
                    receiverType.get(), handleMethod.getSubSignature());
            }

            // resolve against the domain for REPORTING - on the receiver type this
            // yields e.g. MyRepository.someOperation as a direct hit, which is the
            // call-site type we want in the result.
            Set<ResolvedTarget> resolved = resolveTargetsAgainstDomain(
                view, typeHierarchy, reportSignature, domainTypeNames);
            result.addAll(resolved);

            // DESCENT is separate from reporting: descend into the concrete
            // implementation bodies. The reporting owner may be an interface (direct
            // hit), whose method has no body - so we explicitly resolve the concrete
            // implementations of the reporting type and descend into their loadable
            // method with the same sub-signature.
            for (MethodSignature descendInto :
                concreteDescentTargets(view, typeHierarchy, reportSignature)) {
                worklist.add(descendInto);
            }
            // also descend into the raw handle method, in case its body genuinely
            // lives on the declaring type (e.g. a static method or a default method)
            worklist.add(handleMethod);
        }
    }

    /**
     * Returns loadable method signatures to descend into for a call whose reporting
     * signature is given. If the reporting type is concrete, that method is returned.
     * If it is an interface or abstract type, the same method (by sub-signature) on
     * every concrete implementation is returned, each built on the concrete class so
     * its body can actually be loaded. This is used for the descent only - reporting
     * keeps the call-site (interface) type.
     */
    private Set<MethodSignature> concreteDescentTargets(
        JavaView view, TypeHierarchy typeHierarchy, MethodSignature reportSignature) {

        Set<MethodSignature> descent = new LinkedHashSet<>();
        String subSignature = reportSignature.getSubSignature().toString();

        for (SootClass impl :
            concreteImplementations(view, typeHierarchy, reportSignature.getDeclClassType())) {
            collectMethodsWithInherited(view, typeHierarchy, impl).stream()
                .filter(sig -> sig.getSubSignature().toString().equals(subSignature))
                .findFirst()
                .ifPresent(descent::add);
        }
        return descent;
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
        JavaView view, TypeHierarchy typeHierarchy,
        MethodSignature target, Set<String> domainTypeNames) {

        String declName = target.getDeclClassType().getFullyQualifiedName();

        // direct hit: declaring type is itself a domain type
        if (domainTypeNames.contains(declName)) {
            return Collections.singleton(new ResolvedTarget(declName, target));
        }

        // otherwise expand to concrete implementations that are domain types
        Set<ResolvedTarget> resolved = new LinkedHashSet<>();
        String subSignature = target.getSubSignature().toString();

        for (SootClass impl :
            concreteImplementations(view, typeHierarchy, target.getDeclClassType())) {

            String implName = impl.getType().getFullyQualifiedName();
            if (!domainTypeNames.contains(implName)) {
                continue;
            }
            // find the method with the same sub-signature on the implementation
            // (own or inherited) and attribute it to the concrete implementation
            collectMethodsWithInherited(view, typeHierarchy, impl).stream()
                .filter(sig -> sig.getSubSignature().toString().equals(subSignature))
                .findFirst()
                .ifPresent(sig -> resolved.add(new ResolvedTarget(implName, sig)));
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
     * Loads the body of a method, swallowing resolution errors (e.g. abstract
     * methods, methods without a body, or bytecode that fails to translate).
     */
    private Optional<Body> bodyOf(JavaView view, MethodSignature signature) {
        try {
            Optional<? extends SootMethod> methodOpt = view.getMethod(signature);
            if (methodOpt.isEmpty()) return Optional.empty();
            SootMethod method = methodOpt.get();
            if (!method.hasBody()) return Optional.empty();
            return Optional.of(method.getBody());
        } catch (RuntimeException e) {
            return Optional.empty();
        }
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
    private Set<SootClass> concreteImplementations(
        JavaView view, TypeHierarchy typeHierarchy, ClassType classType) {

        if (!typeHierarchy.contains(classType)) {
            return Collections.emptySet();
        }

        Set<SootClass> result = new LinkedHashSet<>();

        view.getClass(classType).ifPresent(cls -> {
            if (!cls.isAbstract() && !cls.isInterface()) {
                result.add(cls);
            }
        });

        Collection<ClassType> candidates =
            typeHierarchy.isInterface(classType)
                ? typeHierarchy.implementersOf(classType).collect(Collectors.toList())
                : typeHierarchy.subclassesOf(classType).collect(Collectors.toList());

        for (ClassType sub : candidates) {
            view.getClass(sub).ifPresent(subCls -> {
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
     */
    private Collection<MethodSignature> collectMethodsWithInherited(
        JavaView view, TypeHierarchy typeHierarchy, SootClass concrete) {

        Map<String, MethodSignature> collected = new LinkedHashMap<>();

        // own methods first so they win on overriding
        concrete.getMethods().forEach(m ->
            collected.putIfAbsent(m.getSignature().getSubSignature().toString(),
                m.getSignature()));

        // all superclasses (transitively, up to Object)
        typeHierarchy.superClassesOf(concrete.getType()).forEach(superType ->
            view.getClass(superType).ifPresent(superCls ->
                superCls.getMethods().forEach(m ->
                    collected.putIfAbsent(m.getSignature().getSubSignature().toString(),
                        m.getSignature())))
        );

        return collected.values();
    }

    // ---------------------------------------------------------------------
    // Mapping Soot signature -> DomainMirror
    // ---------------------------------------------------------------------

    /**
     * Maps a method to the CONCRETE owner type (not the declaring class of the
     * signature). Finds the method by name + parameters, independent of the
     * declaring class, so inherited methods are attributed to the concrete owner.
     */
    private Optional<DomainCalls.MethodCall> mapToOwner(
        DomainMirror domainMirror, MethodSignature methodSignature, String ownerTypeName) {

        return domainMirror.getDomainTypeMirror(ownerTypeName)
            .flatMap(domainTypeMirror -> domainTypeMirror.getMethods()
                .stream()
                .filter(m -> areSemanticallyEqualIgnoringDeclaringClass(methodSignature, m))
                .map(m -> new DomainCalls.MethodCall(domainTypeMirror.getTypeName(), m))
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
