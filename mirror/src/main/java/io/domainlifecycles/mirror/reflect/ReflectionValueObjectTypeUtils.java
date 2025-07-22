package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class ReflectionValueObjectTypeUtils {

    public static List<Field> valueObjectFields(Class<? extends ValueObject> valueObjectClass) {
        return JavaReflect.fields(valueObjectClass, MemberSelect.HIERARCHY)
            .stream()
            .filter(field ->
                !Modifier.isStatic(field.getModifiers())
                && !field.isSynthetic())
            .toList();
    }


}
