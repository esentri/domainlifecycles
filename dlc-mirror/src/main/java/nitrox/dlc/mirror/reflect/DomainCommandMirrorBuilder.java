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

import nitrox.dlc.domain.types.AggregateCommand;
import nitrox.dlc.domain.types.DomainCommand;
import nitrox.dlc.domain.types.DomainService;
import nitrox.dlc.domain.types.DomainServiceCommand;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.mirror.api.DomainCommandMirror;
import nitrox.dlc.mirror.model.DomainCommandModel;

import java.util.Optional;

/**
 * Builder to create {@link DomainCommandMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class DomainCommandMirrorBuilder extends DomainTypeMirrorBuilder{

    private final Class<? extends DomainCommand> domainCommandClass;

    public DomainCommandMirrorBuilder(Class<? extends DomainCommand> domainCommandClass) {
        super(domainCommandClass);
        this.domainCommandClass = domainCommandClass;
    }

    /**
     * Creates a new {@link DomainCommandMirror}.
     */
    public DomainCommandMirror build(){

        var targetType = getTargetType(domainCommandClass);
        String aggregateIdentityTargetTypeName = null;
        String domainServiceTargetTypeName = null;
        if(targetType.isPresent()){
            if(DomainService.class.isAssignableFrom(targetType.get())){
                domainServiceTargetTypeName = targetType.get().getName();
            }else if(Identity.class.isAssignableFrom(targetType.get())){
                aggregateIdentityTargetTypeName = targetType.get().getName();
            }
        }
        return new DomainCommandModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            Optional.ofNullable(aggregateIdentityTargetTypeName),
            Optional.ofNullable(domainServiceTargetTypeName),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }

    private static Optional<Class<?>> getTargetType(Class<? extends DomainCommand> c)   {
        if(DomainServiceCommand.class.isAssignableFrom(c)){
            var resolver = new GenericInterfaceTypeResolver(c);
            var resolved = resolver.resolveFor(DomainServiceCommand.class, 0);
            return Optional.ofNullable(resolved);
        }else if(AggregateCommand.class.isAssignableFrom(c)){
            var resolver = new GenericInterfaceTypeResolver(c);
            var resolved = resolver.resolveFor(AggregateCommand.class, 0);
            return Optional.ofNullable(resolved);
        }
        return Optional.empty();
    }




}
