package io.domainlifecycles.staticanalysis;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainCommandMirror;
import io.domainlifecycles.mirror.api.DomainEventMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link Step#hasNodeKey(String)} compares without building the key, and must decide exactly like
 * {@code nodeKey().equals(key)} - checked for every step of the fixture domain against every key, and against keys
 * differing only slightly (a longer type name, a missing closing parenthesis, another step kind's prefix, ...).
 */
class StepNodeKeyTest {

    @BeforeEach
    void initializeMirror() {
        var factory = new ReflectiveDomainMirrorFactory("io.domainlifecycles.staticanalysis.fixture");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
    }

    @Test
    void hasNodeKeyDecidesLikeComparingTheBuiltKey() {
        List<Step> steps = allSteps();
        Set<String> keys = new LinkedHashSet<>();
        for (Step step : steps) {
            String key = step.nodeKey();
            keys.add(key);
            keys.add(key + "x");
            keys.add(key.substring(0, key.length() - 1));
            keys.add(key.replaceFirst("^[MECT]:", "X:"));
            keys.add(key.replaceFirst("^M:", "T:"));
            keys.add(key.replaceFirst("^T:", "M:"));
            keys.add(key.replace(", ", ","));
            keys.add(key.replace("#", "."));
        }
        keys.add("");
        keys.add("M:");

        int methodSteps = 0;
        for (Step step : steps) {
            if (step instanceof Step.MethodStep) {
                methodSteps++;
            }
            for (String key : keys) {
                assertThat(step.hasNodeKey(key))
                    .as("%s against %s", step.nodeKey(), key)
                    .isEqualTo(step.nodeKey().equals(key));
            }
        }
        assertThat(methodSteps).isGreaterThan(10);
    }

    private static List<Step> allSteps() {
        var mirror = Domain.getDomainMirror();
        List<Step> steps = new ArrayList<>();
        mirror.getAllDomainTypeMirrors().forEach(type -> {
            steps.add(new Step.TypeStep(Optional.empty(), StepKind.START, 0, false, type));
            type.getMethods().forEach(method ->
                steps.add(new Step.MethodStep(Optional.empty(), StepKind.START, 0, false,
                    new DomainMethod(type.getTypeName(), method))));
            if (type instanceof DomainEventMirror event) {
                steps.add(new Step.EventStep(Optional.empty(), StepKind.START, 0, false, event));
            }
            if (type instanceof DomainCommandMirror command) {
                steps.add(new Step.CommandStep(Optional.empty(), StepKind.START, 0, false, command));
            }
        });
        return steps;
    }
}
