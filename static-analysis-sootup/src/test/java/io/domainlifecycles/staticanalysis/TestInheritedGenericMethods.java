package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Calls of generic methods a domain type inherits - {@code findById} of a repository extending DLC's
 * {@code Repository<ID, A>} - are recorded although the mirror resolves the type arguments ({@code findById(MyAggregateRoot.Id)})
 * while the bytecode invokes the erased method ({@code findById(Identity)}). The build plugins mirror with resolved
 * generics, as done here.
 */
public class TestInheritedGenericMethods {

    private static final String REPOSITORY = "test.domain.MyRepository";

    private static DomainMirror domainMirror;
    private static DomainCalls calls;

    @BeforeAll
    static void analyze() {
        // not via Domain.initialize: other tests share the globally initialized test domain
        var factory = new ReflectiveDomainMirrorFactory("test.domain");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        domainMirror = factory.initializeDomainMirror();
        calls = new SootupStaticAnalyzer().analyze(domainMirror, DomainClasspath.ofMirroredTypes(domainMirror));
    }

    @Test
    void mirrorResolvesTheTypeArgumentsOfInheritedGenericMethods() {
        MethodMirror findById = repositoryMethod("findById");

        assertThat(findById.getParameters().get(0).getType().getTypeName()).isEqualTo("test.domain.MyAggregateRoot$Id");
    }

    @Test
    void callOfAnInheritedGenericMethodIsRecorded() {
        DomainMethod findById = new DomainMethod(REPOSITORY, repositoryMethod("findById"));

        assertThat(calls.callersOf(findById)).isNotEmpty();
    }

    private static MethodMirror repositoryMethod(String name) {
        DomainTypeMirror repository = domainMirror.getDomainTypeMirror(REPOSITORY).orElseThrow();
        return repository.getMethods().stream().filter(method -> method.getName().equals(name)).findFirst().orElseThrow();
    }
}
