package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Calls on types outside the domain are not expanded to the domain types implementing the called method, unless
 * asked for - and DLC's own types are no part of the analysis.
 */
public class TestNonDomainDispatch {

    private static final String SERVICE = "test.objectdispatch.DescribingService";

    private static DomainMirror domainMirror;

    @BeforeAll
    static void initializeMirror() {
        // not via Domain.initialize: other tests share the globally initialized test domain
        domainMirror = new ReflectiveDomainMirrorFactory("test.objectdispatch").initializeDomainMirror();
    }

    @Test
    public void callOnObjectIsNotExpandedByDefault() {
        DomainCalls calls = new SootupStaticAnalyzer().analyze(domainMirror, DomainClasspath.ofMirroredTypes(domainMirror));

        assertThat(calledBy(calls, "describe")).isEmpty();
        assertThat(calledBy(calls, "describeName")).containsExactly("test.objectdispatch.Name.toString");
    }

    @Test
    public void callOnObjectIsExpandedToAllImplementingDomainTypes_When_AskedFor() {
        DomainCalls calls = new SootupStaticAnalyzer(SootupStaticAnalyzer.DEFAULT_CACHE_SIZE, true)
            .analyze(domainMirror, DomainClasspath.ofMirroredTypes(domainMirror));

        assertThat(calledBy(calls, "describe"))
            .contains("test.objectdispatch.Name.toString", "test.objectdispatch.Label.toString");
    }

    @Test
    public void dlcTypesAreNeitherCallersNorTargets() {
        DomainCalls calls = AnalyzedTestDomain.domainCalls();

        assertThat(calls.callers()).extracting(DomainMethod::typeName)
            .noneMatch(typeName -> typeName.startsWith("io.domainlifecycles."));
        assertThat(calls.callers().stream().flatMap(caller -> calls.callsFor(caller).callSites().stream()))
            .extracting(callSite -> callSite.called().typeName())
            .noneMatch(typeName -> typeName.startsWith("io.domainlifecycles."));
    }

    private static List<String> calledBy(DomainCalls calls, String methodName) {
        DomainTypeMirror service = domainMirror.getDomainTypeMirror(SERVICE).orElseThrow();
        DomainMethod caller = new DomainMethod(SERVICE, service.getMethods().stream()
            .filter(method -> method.getName().equals(methodName)).findFirst().orElseThrow());
        return calls.callsFor(caller).methods().stream()
            .map(called -> called.typeName() + "." + called.mirror().getName())
            .toList();
    }
}
