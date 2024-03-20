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

package tests.mirror;


import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.domain.types.base.EntityBase;

import java.util.List;
import java.util.Optional;

public class BaseEntityWithHidden<TEST> extends EntityBase<BaseEntityWithHidden.HiddenId> {

    public record HiddenId(Long value) implements Identity<Long>{}
    private String hiddenField = "hidden";

    private TEST inheritedGeneric;

    private List<TEST> inheritedGenericList;

    public String hidden(){
        return hiddenField;
    }

    public TEST showTestNotOverridden(TEST test){return null;}

    public TEST showTestOverridden(TEST test){return null;}

    public Optional<? extends TEST> showTestNotOverriddenOptionalWildcardUpperBound(Optional<? extends TEST> test){return null;}

    public Optional<TEST[]> deliverArray(){
        return null;
    };

    @Override
    public void validate() {
        super.validate();
    }
}
