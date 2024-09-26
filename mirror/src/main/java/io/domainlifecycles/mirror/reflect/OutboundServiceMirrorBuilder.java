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

import io.domainlifecycles.mirror.api.OutboundServiceMirror;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.mirror.model.OutboundServiceModel;

import java.util.Arrays;
import java.util.List;

/**
 * Builder to create {@link OutboundServiceMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class OutboundServiceMirrorBuilder extends DomainTypeMirrorBuilder{
    private final Class<? extends OutboundService> outboundServiceClass;

    public OutboundServiceMirrorBuilder(Class<? extends OutboundService> outboundServiceClass) {
        super(outboundServiceClass);
        this.outboundServiceClass = outboundServiceClass;
    }

    /**
     * Creates a new {@link OutboundServiceMirror}.
     *
     * @return new instance of OutboundServiceMirror
     */
    public OutboundServiceMirror build(){
        return new OutboundServiceModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            outboundServiceInterfaceTypeNames(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }

    private List<String> outboundServiceInterfaceTypeNames(){
        return Arrays.stream(outboundServiceClass.getInterfaces())
            .filter(i -> OutboundService.class.isAssignableFrom(i) && !i.getName().equals(OutboundService.class.getName()))
            .map(Class::getName)
            .toList();
    }
}
