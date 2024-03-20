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

package nitrox.dlc.mirror.model;

import nitrox.dlc.mirror.api.ResolvedGenericTypeMirror;
import nitrox.dlc.mirror.api.WildcardBound;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a resolved generic type model.
 *
 * @author Mario Herb
 */
public record ResolvedGenericTypeModel(String typeName, boolean isArray, List<ResolvedGenericTypeMirror> genericTypes, Optional<WildcardBound> wildcardBound) implements ResolvedGenericTypeMirror {

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolvedTypeDescription(boolean shortTypeNames){
        var desc = new StringBuilder();
        wildcardBound.ifPresent(bound -> desc.append(bound.equals(WildcardBound.UPPER) ? "? extends " : "? super "));
        desc.append(typeName(typeName, shortTypeNames));
        if(genericTypes != null && !genericTypes.isEmpty()){
            desc.append("<");
            desc.append(genericTypes.stream().map(gt -> gt.resolvedTypeDescription(shortTypeNames)).collect(Collectors.joining(", ")));
            desc.append(">");
        }
        return desc.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolvedTypeDescriptionFirstLevel(boolean shortTypeNames){
        var desc = new StringBuilder();
        wildcardBound.ifPresent(bound -> desc.append(bound.equals(WildcardBound.UPPER) ? "? extends " : "? super "));
        desc.append(typeName(typeName, shortTypeNames));
        if(genericTypes != null && !genericTypes.isEmpty()){
            desc.append("<");
            desc.append(genericTypes.stream().map(gt -> {
                var s = "";
                if(gt.wildcardBound().isPresent()){
                    s = gt.wildcardBound().get().equals(WildcardBound.UPPER) ? "? extends " : "? super ";
                }
                s += typeName(gt.typeName(), shortTypeNames);
                return s;
            }).collect(Collectors.joining(", ")));
            desc.append(">");
        }
        return desc.toString();
    }

    private String typeName(String typeName, boolean shortTypeNames){
        Objects.requireNonNull(typeName, "A type name is obligatory!");
        return shortTypeNames ? typeName.substring(typeName.lastIndexOf(".")+1) : typeName;
    }

}
