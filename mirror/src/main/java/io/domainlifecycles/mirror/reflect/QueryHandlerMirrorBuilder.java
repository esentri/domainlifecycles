/*
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

import io.domainlifecycles.domain.types.QueryHandler;
import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.mirror.api.QueryHandlerMirror;
import io.domainlifecycles.mirror.api.RepositoryMirror;
import io.domainlifecycles.mirror.model.QueryHandlerModel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Builder to create {@link QueryHandlerMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class QueryHandlerMirrorBuilder extends ServiceKindMirrorBuilder {
    private final Class<? extends QueryHandler<?>> queryHandlerClass;

    public QueryHandlerMirrorBuilder(Class<? extends QueryHandler<?>> queryHandlerClass) {
        super(queryHandlerClass);
        this.queryHandlerClass = queryHandlerClass;
    }

    /**
     * Creates a new {@link RepositoryMirror}.
     *
     * @return new instance of RepositoryMirror
     */
    public QueryHandlerMirror build() {
        return new QueryHandlerModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            queryHandlerInterfaceTypeNames(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes(),
            getProvidedReadModelType(queryHandlerClass).map(Class::getName).orElse(Object.class.getName())
        );
    }

    private static Optional<Class<? extends ReadModel>> getProvidedReadModelType(Class<? extends QueryHandler<?>> c) {
        var resolver = new GenericInterfaceTypeResolver(c);
        var resolved = resolver.resolveFor(QueryHandler.class, 0);
        return Optional.ofNullable((Class<? extends ReadModel>) resolved);
    }

    private List<String> queryHandlerInterfaceTypeNames() {
        return Arrays.stream(queryHandlerClass.getInterfaces())
            .filter(i -> QueryHandler.class.isAssignableFrom(i) && !i.getName().equals(QueryHandler.class.getName()))
            .map(Class::getName)
            .toList();
    }
}
