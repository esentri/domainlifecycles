package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;

import java.lang.reflect.Field;
import java.util.Optional;

public class ReflectionEntityTypeUtils {

    public static Optional<Field> identityField(Class<? extends Entity<? extends Identity<?>>> entityClass) {
        var resolver = new GenericInterfaceTypeResolver(entityClass);
        var identityType = getIdentityType(resolver);

        if (identityType.isPresent()) {
            var idPropertyFieldCandidates = JavaReflect.fields(entityClass, MemberSelect.HIERARCHY)
                .stream()
                .filter(
                    f -> identityType.get().isAssignableFrom(f.getType()) || Identity.class.getName().equals(
                        f.getType().getName())
                )
                .toList();
            Optional<Field> idProperty;
            if (idPropertyFieldCandidates.size() > 1) {
                idProperty = idPropertyFieldCandidates
                    .stream()
                    .filter(f -> f.isAnnotationPresent(Entity.Id.class))
                    .findFirst();
            } else {
                idProperty = idPropertyFieldCandidates
                    .stream()
                    .findFirst();
            }
            return idProperty;
        }
        return Optional.empty();
    }

    private static Optional<Class<? extends Identity<?>>> getIdentityType(
        GenericInterfaceTypeResolver  resolver
    ) {
        var resolved = resolver.resolveFor(Entity.class, 0);
        return Optional.ofNullable((Class<? extends Identity<?>>) resolved);
    }
}
