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

package nitrox.dlc.mirror.reflect;

import nitrox.dlc.domain.types.ReadModel;
import nitrox.dlc.domain.types.QueryClient;
import nitrox.dlc.mirror.api.QueryClientMirror;
import nitrox.dlc.mirror.api.RepositoryMirror;
import nitrox.dlc.mirror.model.QueryClientModel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Builder to create {@link QueryClientMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class QueryClientMirrorBuilder extends DomainTypeMirrorBuilder{
    private final Class<? extends QueryClient<?>> queryClientClass;

    public QueryClientMirrorBuilder(Class<? extends QueryClient<?>> queryClientClass) {
        super(queryClientClass);
        this.queryClientClass = queryClientClass;
    }

    /**
     * Creates a new {@link RepositoryMirror}.
     */
    public QueryClientMirror build(){
        return new QueryClientModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            queryClientInterfaceTypeNames(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes(),
            getProvidedReadModelType(queryClientClass).map(Class::getName).orElse(Object.class.getName())
        );
    }

    @SuppressWarnings("unchecked")
    private static Optional<Class<? extends ReadModel>> getProvidedReadModelType(Class<? extends QueryClient<?>> c)   {
        var resolver = new GenericInterfaceTypeResolver(c);
        var resolved = resolver.resolveFor(QueryClient.class, 0);
        return Optional.ofNullable((Class<? extends ReadModel>) resolved);
    }

    private List<String> queryClientInterfaceTypeNames(){
        return Arrays.stream(queryClientClass.getInterfaces())
            .filter(i -> QueryClient.class.isAssignableFrom(i) && !i.getName().equals(QueryClient.class.getName()))
            .map(Class::getName)
            .toList();
    }
}
