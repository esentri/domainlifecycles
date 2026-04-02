package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.api.ParamMirror;
import sootup.callgraph.CallGraph;
import sootup.callgraph.CallGraphAlgorithm;
import sootup.callgraph.ClassHierarchyAnalysisAlgorithm;
import sootup.core.model.SootClass;
import sootup.core.model.SootMethod;
import sootup.core.signatures.MethodSignature;
import sootup.core.types.Type;
import sootup.java.bytecode.frontend.inputlocation.JavaClassPathAnalysisInputLocation;
import sootup.java.core.JavaIdentifierFactory;
import sootup.java.core.views.JavaView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SootupStaticAnalyzer implements StaticAnalyzer{

    public DomainCalls analyze(DomainMirror domainMirror, String classpath) {
        JavaView view = new JavaView(
            List.of(new JavaClassPathAnalysisInputLocation(classpath))
        );

        JavaIdentifierFactory idf = view.getIdentifierFactory();
        List<MethodSignature> entryPoints = new ArrayList<>();
        domainMirror.getAllDomainTypeMirrors().forEach(domainTypeMirror -> {
            var classType = idf.getClassType(domainTypeMirror.getTypeName());
            Optional<? extends SootClass> clsOpt = view.getClass(classType);
            if (!clsOpt.isEmpty()) {
                SootClass cls = clsOpt.get();
                entryPoints.addAll(cls.getMethods()
                    .stream()
                    .map(SootMethod::getSignature)
                    .toList());
            }
        });

        CallGraphAlgorithm cha = new ClassHierarchyAnalysisAlgorithm(view);
        CallGraph cg = cha.initialize(entryPoints);

        Map<MethodMirror, DomainCalls.CalledMethods> domainCalls = new HashMap<>();

        for (MethodSignature method : entryPoints) {
            var caller = map(domainMirror, method);
            if(caller.isEmpty()) continue;

            Set<MethodSignature> targets = cg.callTargetsFrom(method);
            if (targets.isEmpty()) continue;

            List<MethodMirror> targetMethods = targets.stream()
                .map(m -> map(domainMirror, m))
                .filter(Optional::isPresent)
                .map(Optional::get).toList();

            if(targetMethods.isEmpty()) continue;
            domainCalls.put(caller.get(), new DomainCalls.CalledMethods(targetMethods));
        }

        return new DomainCalls(domainCalls);
    }


    private Optional<MethodMirror> map(DomainMirror domainMirror, MethodSignature methodSignature){
        var dtm = domainMirror.getDomainTypeMirror(methodSignature.getDeclClassType().getFullyQualifiedName());
        if(dtm.isPresent()){
            return dtm.get().getMethods()
                .stream()
                .filter(
                    m -> areSemanticallyEqual(methodSignature, m)
                )
                .findFirst();
        }
        return Optional.empty();
    }

    public static boolean areSemanticallyEqual(MethodSignature sootSignature, MethodMirror dlcMirror) {
        if (sootSignature == null || dlcMirror == null) {
            return false;
        }

        // 1. Compare Method Name
        if (!sootSignature.getName().equals(dlcMirror.getName())) {
            return false;
        }

        // 2. Compare Declaring Class (Fully Qualified Name)
        String sootClassName = sootSignature.getDeclClassType().getFullyQualifiedName();
        String dlcClassName = dlcMirror.getDeclaredByTypeName();

        if (!sootClassName.equals(dlcClassName)) {
            return false;
        }

        // 3. Compare Parameter Count
        List<Type> sootParams = sootSignature.getParameterTypes();
        List<ParamMirror> dlcParams = dlcMirror.getParameters();

        if (sootParams.size() != dlcParams.size()) {
            return false;
        }

        // 4. Compare Parameter Types (Order Matters)
        for (int i = 0; i < sootParams.size(); i++) {
            String sootParamType = sootParams.get(i).toString();
            String dlcParamType = dlcParams.get(i).getType().getTypeName();

            if (!sootParamType.equals(dlcParamType)) {
                return false;
            }
        }

        return true;
    }


    //TODO test semantically equal
}
