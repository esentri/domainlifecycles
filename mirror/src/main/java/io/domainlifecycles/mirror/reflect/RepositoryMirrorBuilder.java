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

import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.model.RepositoryModel;
import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryClient;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.reflect.JavaReflect;
import io.domainlifecycles.reflect.MemberSelect;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Builder to create {@link RepositoryMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class RepositoryMirrorBuilder extends DomainTypeMirrorBuilder{
    private final Class<? extends Repository<?, ?>> repositoryClass;

    public RepositoryMirrorBuilder(Class<? extends Repository<?, ?>> repositoryClass) {
        super(repositoryClass);
        this.repositoryClass = repositoryClass;
    }

    /**
     * Creates a new {@link RepositoryMirror}.
     *
     * @return new instance of RepositoryMirror
     */
    public RepositoryMirror build(){
        return new RepositoryModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            getManagedAggregateType(repositoryClass).map(Class::getName).orElse(Object.class.getName()),
            getReferencedOutboundServiceNames(),
            getReferencedQueryClientNames(),
            repositoryInterfaceTypeNames(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }

    private List<String> getReferencedOutboundServiceNames(){
        return JavaReflect
            .fields(this.repositoryClass, MemberSelect.HIERARCHY)
            .stream()
            .filter(f -> isOutboundService(f.getType()))
            .map(f -> f.getType().getName())
            .toList();
    }

    private List<String> getReferencedQueryClientNames(){
        return JavaReflect
            .fields(this.repositoryClass, MemberSelect.HIERARCHY)
            .stream()
            .filter(f -> isQueryClient(f.getType()))
            .map(f -> f.getType().getName())
            .toList();
    }

    private boolean isOutboundService(Class<?> fieldClass){
        return OutboundService.class.isAssignableFrom(fieldClass);
    }

    private boolean isQueryClient(Class<?> fieldClass){
        return QueryClient.class.isAssignableFrom(fieldClass);
    }

    @SuppressWarnings("unchecked")
    private static Optional<Class<? extends AggregateRoot<?>>> getManagedAggregateType(Class<? extends Repository<?, ?>> c)   {
        var resolver = new GenericInterfaceTypeResolver(c);
        var resolved = resolver.resolveFor(Repository.class, 1);
        return Optional.ofNullable((Class<? extends AggregateRoot<?>>) resolved);
    }

    private List<String> repositoryInterfaceTypeNames(){
        return Arrays.stream(repositoryClass.getInterfaces())
            .filter(i -> Repository.class.isAssignableFrom(i) && !i.getName().equals(Repository.class.getName()))
            .map(Class::getName)
            .toList();
    }
}
