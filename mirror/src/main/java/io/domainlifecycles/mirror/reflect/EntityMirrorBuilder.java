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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.internal.ConcurrencySafe;
import io.domainlifecycles.mirror.model.EntityModel;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Builder to create {@link EntityMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class EntityMirrorBuilder extends DomainTypeMirrorBuilder{
    private final Class<? extends Entity<?>> entityClass;

    public EntityMirrorBuilder(Class<? extends Entity<?>> entityClass
    ) {
        super(entityClass);
        this.entityClass = entityClass;
    }

    /**
     * Creates a new {@link EntityMirror}.
     *
     * @return new instance of EntityMirror
     */
    public EntityMirror build() {
        return new EntityModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            identityField(),
            concurrencyVersionField(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }
    protected Optional<FieldMirror> identityField(){
        var identityType = getIdentityType(entityClass);
        if(identityType.isPresent()) {
            var idPropertyFieldCandidates = JavaReflect.fields(entityClass, MemberSelect.HIERARCHY)
                .stream()
                .filter(
                    f -> identityType.get().isAssignableFrom(f.getType()) || Identity.class.getName().equals(f.getType().getName())
                )
                .toList();
            Optional<Field> idProperty;
            if(idPropertyFieldCandidates.size()>1){
                idProperty = idPropertyFieldCandidates
                    .stream()
                    .filter(f -> f.isAnnotationPresent(Entity.Id.class))
                    .findFirst();
            }else{
                idProperty = idPropertyFieldCandidates
                    .stream()
                    .findFirst();
            }
            if(idProperty.isPresent()){
                return Optional.of(new FieldMirrorBuilder(idProperty.get(), entityClass, isHidden(idProperty.get())).build());
            }
        }
        return Optional.empty();
    }

    protected Optional<FieldMirror> concurrencyVersionField(){
        var concurrencyFieldCandidates = JavaReflect.fields(entityClass, MemberSelect.HIERARCHY)
            .stream()
            .filter(
                f -> f.isAnnotationPresent(ConcurrencySafe.ConcurrencyVersion.class)
            )
            .toList();
        Field concurrencyField;
        if(concurrencyFieldCandidates.size()==1){
            concurrencyField = concurrencyFieldCandidates.get(0);
            return Optional.of(new FieldMirrorBuilder(concurrencyField, entityClass, isHidden(concurrencyField) ).build());
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private static Optional<Class<? extends Identity<?>>> getIdentityType(Class<? extends Entity<? extends Identity<?>>> c) {
        var resolver = new GenericInterfaceTypeResolver(c);
        var resolved = resolver.resolveFor(Entity.class, 0);
        return Optional.ofNullable((Class<? extends Identity<?>>) resolved);
    }

}
