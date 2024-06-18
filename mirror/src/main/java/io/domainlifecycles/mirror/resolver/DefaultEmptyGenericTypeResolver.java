/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.mirror.resolver;

import io.domainlifecycles.mirror.api.ResolvedGenericTypeMirror;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * A class that implements the GenericTypeResolver interface.
 * The Default resolver does not do any resolving, because it's not needed for most mirror use cases.
 * <br>
 * If generic type resolving is needed it could be plugged in by using {@see TypeMetaResolver}.
 *
 * @author Mario Herb
 */
public class DefaultEmptyGenericTypeResolver implements GenericTypeResolver {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResolvedGenericTypeMirror resolveFieldType(Field f, Class<?> contextClass) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResolvedGenericTypeMirror> resolveExecutableParameters(Method m, Class<?> contextClass) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResolvedGenericTypeMirror resolveExecutableReturnType(Method m, Class<?> contextClass) {
        return null;
    }

    /*private Class<?> mostConcreteType(){
        if(isInherited()){
            var superClass = topLevelClass;
            Map<String, Type> genericTypeReplacements = new HashMap<>();
            do{
                genericTypeReplacements.putAll(genericTypeReplacements(superClass.getGenericSuperclass()));
                superClass = superClass.getSuperclass();
            }while(!superClass.getName().equals(field.getDeclaringClass().getName()));
            Type actualType = field.getGenericType();
            Type mappedType;
            boolean last = false;
            do{
                mappedType = genericTypeReplacements.get(actualType.getTypeName());
                if(mappedType == null){
                    last = true;
                }else{
                    actualType = mappedType;
                    if(actualType instanceof Class<?>){
                        last = true;
                    }
                }
            }while(!last);
            if(actualType instanceof Class<?>){
                return ((Class<?>) actualType);
            }else if(actualType instanceof TypeVariable){

                var bounds = ((TypeVariable<?>)actualType).getBounds();
                if(bounds.length>0){
                    if(bounds[0] instanceof Class<?>){
                        return (Class<?>) bounds[0];
                    }else{
                        return Object.class;
                    }
                }else{
                    return Object.class;
                }
            }else if(actualType instanceof ParameterizedType paramType){
                if(paramType.getRawType() instanceof Class<?>){
                    return (Class<?>) paramType.getRawType();
                }
            }
            return Object.class;
        }else{
        return field.getType();
        }
    }

    private Map<String, Type> genericTypeReplacements(Type genericSuperClass){
        if(genericSuperClass instanceof ParameterizedType parameterizedSuperclass){
            Map<String, Type> genericTypeReplacements = new HashMap<>();
            for(var i=0;i<parameterizedSuperclass.getActualTypeArguments().length;i++){
                genericTypeReplacements.put(
                    ((Class<?>)parameterizedSuperclass.getRawType()).getTypeParameters()[i].getName(),
                    parameterizedSuperclass.getActualTypeArguments()[i]
                );
            }
            return genericTypeReplacements;
        }else{
            return Collections.emptyMap();
        }
    }

    private boolean isInherited(){
        return !field.getDeclaringClass().getName().equals(topLevelClass.getName());
    }*/
}
