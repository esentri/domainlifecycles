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

package io.domainlifecycles.mirror.api;

import io.domainlifecycles.mirror.model.BoundedContextModel;
import io.domainlifecycles.mirror.model.DomainTypeModel;
import io.domainlifecycles.mirror.model.EntityModel;
import io.domainlifecycles.mirror.model.FieldModel;
import io.domainlifecycles.mirror.model.MethodModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This record is a container for all mirrors in a mirrored Domain.
 *
 * @param allTypeMirrors        all the DomainTypeMirrors
 * @param boundedContextMirrors all the BoundedContextMirrors
 * @author Mario Herb
 */
public record DomainModel(Map<String, ? extends DomainTypeMirror> allTypeMirrors,
                          List<BoundedContextMirror> boundedContextMirrors) {

    public DomainModel(Map<String, ? extends DomainTypeMirror> allTypeMirrors, List<BoundedContextMirror> boundedContextMirrors) {
        this.allTypeMirrors = new HashMap<>(allTypeMirrors);
        this.boundedContextMirrors = new ArrayList<>(boundedContextMirrors);
        allTypeMirrors
            .values()
            .stream()
            .map(m -> (DomainTypeModel)m)
            .forEach(m -> {
                m.setDomainModel(this);
                if(m instanceof EntityModel e) {
                    e.getIdentityField()
                        .map(f -> (FieldModel) f)
                        .ifPresent(f -> f.setDomainModel(this));
                }
                m.getAllFields()
                    .stream()
                    .map(f -> (FieldModel) f)
                    .forEach(f -> f.setDomainModel(this));
                m.getMethods()
                    .stream()
                    .map(meth -> (MethodModel) meth)
                    .forEach(meth -> meth.setDomainModel(this));

            });
        boundedContextMirrors.forEach(m -> {
            var bc = (BoundedContextModel)m;
            bc.setDomainModel(this);
        });
    }

    public <T extends DomainTypeMirror> Optional<T> getDomainTypeMirror(String typeName) {
        return  Optional.ofNullable((T)allTypeMirrors.get(typeName));
    }
}

