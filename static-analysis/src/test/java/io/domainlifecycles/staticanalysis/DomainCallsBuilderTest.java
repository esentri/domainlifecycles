package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers the deduplication of call sites in {@link DomainCalls.Builder}, which is hash based since it was
 * quadratic per caller before (a linear {@code List.contains} per added call site).
 */
public class DomainCallsBuilderTest {

    private static final String REPOSITORY = "io.domainlifecycles.staticanalysis.fixture.TestRepositoryImpl";

    @BeforeEach
    void initializeMirror() {
        var factory = new ReflectiveDomainMirrorFactory("io.domainlifecycles.staticanalysis.fixture");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
    }

    @Test
    void duplicateCallSitesAreDroppedAcrossAddCalls_And_TheirOrderIsKept() {
        var caller = domainMethod("update");
        var findById = new DomainCalls.CallSite(domainMethod("findById"), REPOSITORY, 10);
        var insert = new DomainCalls.CallSite(domainMethod("insert"), REPOSITORY, 11);
        var findByIdOtherLine = new DomainCalls.CallSite(domainMethod("findById"), REPOSITORY, 12);

        var domainCalls = DomainCalls.builder()
            .add(caller, List.of(findById, insert))
            .add(caller, List.of(insert, findByIdOtherLine, findById))
            .build();

        assertThat(domainCalls.callsFor(caller).callSites()).containsExactly(findById, insert, findByIdOtherLine);
        assertThat(domainCalls.callsFor(caller).methods()).containsExactly(domainMethod("findById"), domainMethod("insert"));
    }

    @Test
    void manyCallSitesPerCallerAreBuiltInLinearTime() {
        var caller = domainMethod("update");
        var called = domainMethod("findById");
        var callSites = new ArrayList<DomainCalls.CallSite>();
        for (int line = 0; line < 50_000; line++) {
            callSites.add(new DomainCalls.CallSite(called, REPOSITORY, line));
        }

        long start = System.nanoTime();
        var domainCalls = DomainCalls.builder().add(caller, callSites).add(caller, callSites).build();
        long millis = (System.nanoTime() - start) / 1_000_000;

        assertThat(domainCalls.callsFor(caller).callSites()).hasSize(50_000);
        // quadratic deduplication needed minutes for this; generous bound against slow CI machines
        assertThat(millis).isLessThan(10_000);
    }

    private static DomainMethod domainMethod(String methodName) {
        var typeMirror = Domain.getDomainMirror().getDomainTypeMirror(REPOSITORY).orElseThrow();
        var method = typeMirror.getMethods().stream()
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .orElseThrow();
        return new DomainMethod(typeMirror.getTypeName(), method);
    }
}
