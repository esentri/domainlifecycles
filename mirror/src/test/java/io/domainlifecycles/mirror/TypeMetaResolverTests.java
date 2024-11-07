package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;
import io.domainlifecycles.mirror.api.WildcardBound;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import org.junit.jupiter.api.Test;
import tests.mirror.BaseEntityWithHidden;
import tests.mirror.SubEntityHiding;
import tests.shared.persistence.domain.inheritanceGenericId.AbstractRoot;
import tests.shared.persistence.domain.inheritanceGenericId.ConcreteRoot;
import tests.shared.persistence.domain.inheritanceGenericId.ConcreteRootId;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeMetaResolverTests {

    private TypeMetaResolver typeMetaResolver = new TypeMetaResolver();

    @Test
    public void testNotGeneric() throws NoSuchFieldException {
        Field fieldToResolve = SubEntityHiding.class.getDeclaredField("hiddenField");
        ResolvedGenericTypeMirror resolved = typeMetaResolver.resolveFieldType(fieldToResolve, SubEntityHiding.class);
        assertThat(resolved.typeName()).isEqualTo(String.class.getName());
        assertThat(resolved.isArray()).isFalse();
        assertThat(resolved.wildcardBound()).isEmpty();
        assertThat(resolved.genericTypes()).isEmpty();
    }

    @Test
    public void testInheritedGeneric() throws NoSuchFieldException {

        Field fieldToResolve = BaseEntityWithHidden.class.getDeclaredField("inheritedGeneric");
        ResolvedGenericTypeMirror resolved = typeMetaResolver.resolveFieldType(fieldToResolve, SubEntityHiding.class);
        assertThat(resolved.typeName()).isEqualTo(String.class.getName());
        assertThat(resolved.isArray()).isFalse();
        assertThat(resolved.wildcardBound()).isEmpty();
        assertThat(resolved.genericTypes()).isEmpty();
    }

    @Test
    public void testInheritedGenericAbstractRoot() throws NoSuchFieldException {
        Field fieldToResolve = AbstractRoot.class.getDeclaredField("myId");
        ResolvedGenericTypeMirror resolved = typeMetaResolver.resolveFieldType(fieldToResolve, ConcreteRoot.class);
        assertThat(resolved.typeName()).isEqualTo(ConcreteRootId.class.getName());
        assertThat(resolved.isArray()).isFalse();
        assertThat(resolved.wildcardBound()).isEmpty();
        assertThat(resolved.genericTypes()).isEmpty();
    }

    @Test
    public void testInheritedGenericMethodNotOverridden() throws NoSuchMethodException {

        Method methodToResolve = BaseEntityWithHidden.class.getDeclaredMethod("showTestNotOverridden", Object.class);
        ResolvedGenericTypeMirror resolved = typeMetaResolver.resolveExecutableReturnType(methodToResolve,
            SubEntityHiding.class);

        assertThat(resolved.typeName()).isEqualTo(String.class.getName());
        assertThat(resolved.isArray()).isFalse();
        assertThat(resolved.wildcardBound()).isEmpty();
        assertThat(resolved.genericTypes()).isEmpty();

        List<ResolvedGenericTypeMirror> methodArgumentsTypeMetaList = typeMetaResolver
            .resolveExecutableParameters(methodToResolve, SubEntityHiding.class);

        assertThat(methodArgumentsTypeMetaList).hasSize(1);
        assertThat(methodArgumentsTypeMetaList.get(0).typeName()).isEqualTo(String.class.getName());
        assertThat(methodArgumentsTypeMetaList.get(0).isArray()).isFalse();
        assertThat(methodArgumentsTypeMetaList.get(0).wildcardBound()).isEmpty();
        assertThat(methodArgumentsTypeMetaList.get(0).genericTypes()).isEmpty();
    }

    @Test
    public void testInheritedGenericMethodOverridden() throws NoSuchMethodException {

        Method methodToResolve = BaseEntityWithHidden.class.getDeclaredMethod("showTestOverridden", Object.class);
        ResolvedGenericTypeMirror resolved = typeMetaResolver.resolveExecutableReturnType(methodToResolve,
            SubEntityHiding.class);

        assertThat(resolved.typeName()).isEqualTo(String.class.getName());
        assertThat(resolved.isArray()).isFalse();
        assertThat(resolved.wildcardBound()).isEmpty();
        assertThat(resolved.genericTypes()).isEmpty();

        List<ResolvedGenericTypeMirror> methodArgumentsTypeMetaList = typeMetaResolver
            .resolveExecutableParameters(methodToResolve, SubEntityHiding.class);

        assertThat(methodArgumentsTypeMetaList).hasSize(1);
        assertThat(methodArgumentsTypeMetaList.get(0).typeName()).isEqualTo(String.class.getName());
        assertThat(methodArgumentsTypeMetaList.get(0).isArray()).isFalse();
        assertThat(methodArgumentsTypeMetaList.get(0).wildcardBound()).isEmpty();
        assertThat(methodArgumentsTypeMetaList.get(0).genericTypes()).isEmpty();
    }

    @Test
    public void testInheritedValidate() throws NoSuchMethodException {
        Method methodToResolve = BaseEntityWithHidden.class.getDeclaredMethod("validate");
        ResolvedGenericTypeMirror resolved = typeMetaResolver.resolveExecutableReturnType(methodToResolve,
            SubEntityHiding.class);

        assertThat(resolved.typeName()).isEqualTo(void.class.getName());
        assertThat(resolved.isArray()).isFalse();
        assertThat(resolved.wildcardBound()).isEmpty();
        assertThat(resolved.genericTypes()).isEmpty();

        List<ResolvedGenericTypeMirror> methodArgumentsTypeMetaList = typeMetaResolver
            .resolveExecutableParameters(methodToResolve, SubEntityHiding.class);

        assertThat(methodArgumentsTypeMetaList).hasSize(0);
    }

    @Test
    public void testInheritedOptionalWildcardUpperBound() throws NoSuchMethodException {

        Method methodToResolve = BaseEntityWithHidden.class.getDeclaredMethod(
            "showTestNotOverriddenOptionalWildcardUpperBound", Optional.class);
        ResolvedGenericTypeMirror resolved = typeMetaResolver.resolveExecutableReturnType(methodToResolve,
            SubEntityHiding.class);

        assertThat(resolved.typeName()).isEqualTo(Optional.class.getName());
        assertThat(resolved.isArray()).isFalse();
        assertThat(resolved.wildcardBound()).isEmpty();
        assertThat(resolved.genericTypes()).hasSize(1);
        assertThat(resolved.genericTypes().get(0).wildcardBound()).isEqualTo(Optional.of(WildcardBound.UPPER));
        assertThat(resolved.genericTypes().get(0).typeName()).isEqualTo(String.class.getName());
        assertThat(resolved.genericTypes().get(0).isArray()).isFalse();

        List<ResolvedGenericTypeMirror> methodArgumentsTypeMetaList = typeMetaResolver
            .resolveExecutableParameters(methodToResolve, SubEntityHiding.class);

        assertThat(methodArgumentsTypeMetaList).hasSize(1);
        assertThat(methodArgumentsTypeMetaList.get(0).typeName()).isEqualTo(Optional.class.getName());
        assertThat(methodArgumentsTypeMetaList.get(0).isArray()).isFalse();
        assertThat(methodArgumentsTypeMetaList.get(0).wildcardBound()).isEmpty();
        assertThat(methodArgumentsTypeMetaList.get(0).genericTypes()).hasSize(1);
        assertThat(methodArgumentsTypeMetaList.get(0).genericTypes().get(0).wildcardBound()).isEqualTo(
            Optional.of(WildcardBound.UPPER));
        assertThat(methodArgumentsTypeMetaList.get(0).genericTypes().get(0).typeName()).isEqualTo(
            String.class.getName());
        assertThat(methodArgumentsTypeMetaList.get(0).genericTypes().get(0).isArray()).isFalse();
    }

    @Test
    public void testArrayField() throws NoSuchFieldException {
        Field fieldToResolve = SubEntityHiding.class.getDeclaredField("intArray");
        ResolvedGenericTypeMirror resolved = typeMetaResolver.resolveFieldType(fieldToResolve, SubEntityHiding.class);
        assertThat(resolved.typeName()).isEqualTo(int.class.getName());
        assertThat(resolved.isArray()).isTrue();
        assertThat(resolved.wildcardBound()).isEmpty();

        assertThat(resolved.genericTypes()).hasSize(1);
        assertThat(resolved.genericTypes().get(0).wildcardBound()).isEmpty();
        assertThat(resolved.genericTypes().get(0).typeName()).isEqualTo(int.class.getName());
        assertThat(resolved.genericTypes().get(0).isArray()).isFalse();
    }

    @Test
    public void testInheritedOptionalArrayReturn() throws NoSuchMethodException {

        Method methodToResolve = BaseEntityWithHidden.class.getDeclaredMethod("deliverArray");
        ResolvedGenericTypeMirror resolved = typeMetaResolver.resolveExecutableReturnType(methodToResolve,
            SubEntityHiding.class);

        assertThat(resolved.typeName()).isEqualTo(Optional.class.getName());
        assertThat(resolved.isArray()).isFalse();
        assertThat(resolved.wildcardBound()).isEmpty();
        assertThat(resolved.genericTypes()).hasSize(1);
        assertThat(resolved.genericTypes().get(0).wildcardBound()).isEmpty();
        assertThat(resolved.genericTypes().get(0).typeName()).isEqualTo(String.class.getName());
        assertThat(resolved.genericTypes().get(0).isArray()).isTrue();


        List<ResolvedGenericTypeMirror> methodArgumentsTypeMetaList = typeMetaResolver
            .resolveExecutableParameters(methodToResolve, SubEntityHiding.class);

        assertThat(methodArgumentsTypeMetaList).hasSize(0);

    }

}
