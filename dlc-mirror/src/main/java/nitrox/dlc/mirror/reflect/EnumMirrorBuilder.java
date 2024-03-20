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

import nitrox.dlc.mirror.api.EnumMirror;
import nitrox.dlc.mirror.api.EnumOptionMirror;
import nitrox.dlc.mirror.model.EnumModel;
import nitrox.dlc.mirror.model.EnumOptionModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builder to create {@link EnumMirror}. Uses Java reflection.
 *
 * @author Mario Herb
 */
public class EnumMirrorBuilder extends DomainTypeMirrorBuilder{
    private final Class<?extends Enum<?>> enumClass;

    public EnumMirrorBuilder(Class<? extends Enum<?>> enumClass
    ) {
        super(enumClass);
        this.enumClass = enumClass;
    }

    /**
     * Creates a new {@link EnumMirror}.
     */
    public EnumMirror build() {
        return new EnumModel(
            getTypeName(),
            isAbstract(),
            buildFields(),
            buildMethods(),
            buildEnumOptions(),
            buildInheritanceHierarchy(),
            buildInterfaceTypes()
        );
    }
    private List<EnumOptionMirror> buildEnumOptions(){
        if(enumClass.getEnumConstants() != null) {
            return Arrays.stream(enumClass.getEnumConstants())
                .map(c -> new EnumOptionModel(c.name()))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
