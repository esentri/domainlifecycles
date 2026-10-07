package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.mirror.overriding.OverridingApi;
import tests.mirror.overriding.OverridingBase;
import tests.mirror.overriding.OverridingController;
import tests.mirror.overriding.other.OtherPackageController;

import static org.assertj.core.api.Assertions.assertThat;

class OverriddenMethodMirrorTest {

    private static DomainMirror domainMirror;

    @BeforeAll
    static void init() {
        domainMirror = new ReflectiveDomainMirrorFactory("tests.mirror.overriding").initializeDomainMirror();
    }

    @Test
    void Should_MarkInterfaceMethodAsOverridden_When_ImplementedByAFinalMethod() {
        assertThat(method(OverridingController.class, "create", OverridingApi.class).isOverridden()).isTrue();
        assertThat(method(OverridingController.class, "create", OverridingController.class).isOverridden()).isFalse();
    }

    @Test
    void Should_MarkMethodAsOverridden_When_OverriddenWithCovariantReturnType() {
        assertThat(method(OverridingController.class, "covariant", OverridingApi.class).isOverridden()).isTrue();
        assertThat(method(OverridingController.class, "covariant", OverridingController.class).isOverridden())
            .isFalse();
    }

    @Test
    void Should_MarkMethodAsOverridden_When_VisibilityIsWidened() {
        assertThat(method(OverridingController.class, "hook", OverridingBase.class).isOverridden()).isTrue();
    }

    @Test
    void Should_MarkMethodAsOverridden_When_OverriddenByAFinalMethod() {
        assertThat(method(OverridingController.class, "describe", OverridingBase.class).isOverridden()).isTrue();
    }

    @Test
    void Should_MarkPackagePrivateMethodAsOverridden_When_OverriddenInTheSamePackage() {
        assertThat(method(OverridingController.class, "packageLocal", OverridingBase.class).isOverridden()).isTrue();
    }

    @Test
    void Should_NotMarkPackagePrivateMethodAsOverridden_When_RedeclaredInAnotherPackage() {
        assertThat(method(OtherPackageController.class, "packageLocal", OverridingBase.class).isOverridden())
            .isFalse();
    }

    @Test
    void Should_NotMarkPrivateMethodAsOverridden_When_RedeclaredInASubclass() {
        assertThat(method(OverridingController.class, "secret", OverridingBase.class).isOverridden()).isFalse();
    }

    @Test
    void Should_NotMarkStaticMethodAsOverridden_When_HiddenInASubclass() {
        assertThat(method(OverridingController.class, "utility", OverridingBase.class).isOverridden()).isFalse();
    }

    @Test
    void Should_MirrorEachMethodOnceAsNotOverridden_When_ImplementedInTheClass() {
        assertThat(domainMirror.getDomainTypeMirror(OverridingController.class.getName()).orElseThrow()
            .getMethods()
            .stream()
            .filter(m -> !m.isOverridden())
            .filter(m -> m.getName().equals("create")))
            .hasSize(1);
    }

    private static MethodMirror method(Class<?> mirroredType, String name, Class<?> declaringType) {
        return domainMirror.getDomainTypeMirror(mirroredType.getName()).orElseThrow()
            .getMethods()
            .stream()
            .filter(m -> m.getName().equals(name))
            .filter(m -> m.getDeclaredByTypeName().equals(declaringType.getName()))
            .findFirst()
            .orElseThrow(() -> new AssertionError(
                "No method " + declaringType.getSimpleName() + "." + name + " in mirror of " + mirroredType));
    }
}
