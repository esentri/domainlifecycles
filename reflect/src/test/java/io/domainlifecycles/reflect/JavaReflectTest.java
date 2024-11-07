package io.domainlifecycles.reflect;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaReflectTest {


    @Test
    public void testReflectMethodHierarchyInterface() {
        var methods = JavaReflect.methods(ReflectAggregateRepository.class, MemberSelect.HIERARCHY);
        var findByIdMethods = methods.stream().filter(m -> m.getName().equals("findById")).toList();
        assertThat(findByIdMethods).hasSize(1);
    }
}
