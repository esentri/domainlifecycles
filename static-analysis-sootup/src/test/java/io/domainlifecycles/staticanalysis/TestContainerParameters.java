package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Methods whose parameters are containers or arrays are matched to their mirror, as callers and as called methods:
 * the mirror names the element type of a container and the component type of an array, the bytecode the container
 * and the array type.
 */
public class TestContainerParameters {

    private static final String SERVICE = "test.containerparams.PricingService";
    private static final String ITEM = "test.containerparams.Item";

    private static DomainMirror domainMirror;
    private static DomainCalls calls;

    @BeforeAll
    static void analyze() {
        // not via Domain.initialize: other tests share the globally initialized test domain
        domainMirror = new ReflectiveDomainMirrorFactory("test.containerparams").initializeDomainMirror();
        calls = new SootupStaticAnalyzer().analyze(domainMirror, DomainClasspath.ofMirroredTypes(domainMirror));
    }

    @Test
    public void methodsTakingContainersOrArraysAreCalled() {
        assertThat(calledBy("priceAll", "Item"))
            .contains(
                SERVICE + ".total(java.util.List)",
                SERVICE + ".total(java.util.Set)",
                SERVICE + ".first(java.util.Optional)",
                SERVICE + ".sum(" + ITEM + "[])",
                SERVICE + ".count(int[][])",
                SERVICE + ".checksum(byte[])",
                SERVICE + ".weigh(" + ITEM + "[])");
    }

    @Test
    public void methodsTakingContainersCall() {
        assertThat(calledBy("total", "List")).containsExactly(ITEM + ".price()");
        assertThat(calledBy("total", "Set")).containsExactly(ITEM + ".weight()");
        assertThat(calledBy("first", "Optional")).containsExactly(ITEM + ".price()");
    }

    @Test
    public void methodsTakingArraysCall() {
        assertThat(calledBy("sum", "Item")).containsExactly(ITEM + ".price()");
        assertThat(calledBy("weigh", "Item")).containsExactly(ITEM + ".weight()");
    }

    @Test
    public void containerAndArrayParametersAreNamedLikeTheBytecodeNamesThem() {
        assertThat(parameterTypes("total")).containsExactlyInAnyOrder("java.util.List", "java.util.Set");
        assertThat(parameterTypes("first")).containsExactly("java.util.Optional");
        assertThat(parameterTypes("sum")).containsExactly(ITEM + "[]");
        assertThat(parameterTypes("count")).containsExactly("int[][]");
        assertThat(parameterTypes("checksum")).containsExactly("byte[]");
    }

    /**
     * The methods called by the service method of the given name whose first parameter type contains the given text.
     */
    private static List<String> calledBy(String methodName, String firstParameterType) {
        DomainTypeMirror service = domainMirror.getDomainTypeMirror(SERVICE).orElseThrow();
        MethodMirror method = service.getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .filter(m -> SootupStaticAnalyzer.bytecodeTypeName(m.getParameters().get(0).getType()).contains(firstParameterType))
            .findFirst()
            .orElseThrow();
        return calls.callsFor(new DomainMethod(SERVICE, method)).methods().stream()
            .map(called -> called.typeName() + "." + called.mirror().getName() + "("
                + String.join(",", called.mirror().getParameters().stream()
                    .map(parameter -> SootupStaticAnalyzer.bytecodeTypeName(parameter.getType())).toList())
                + ")")
            .toList();
    }

    private static List<String> parameterTypes(String methodName) {
        return domainMirror.getDomainTypeMirror(SERVICE).orElseThrow().getMethods().stream()
            .filter(method -> method.getName().equals(methodName))
            .map(method -> SootupStaticAnalyzer.bytecodeTypeName(method.getParameters().get(0).getType()))
            .toList();
    }
}
