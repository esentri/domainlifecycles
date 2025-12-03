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
 *  Unless required by applicable law or written to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.mirrordeserialization.serialize.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.domainlifecycles.mirror.model.AggregateRootReferenceModel;
import io.domainlifecycles.mirror.model.EntityReferenceModel;
import io.domainlifecycles.mirror.model.FieldModel;
import io.domainlifecycles.mirror.model.ValueReferenceModel;


/**
 * Jackson mixin interface for proper serialization of {@link FieldModel} and all its subtypes.
 *
 * @author Mario Herb
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FieldModel.class),
    @JsonSubTypes.Type(value = AggregateRootReferenceModel.class),
    @JsonSubTypes.Type(value = ValueReferenceModel.class),
    @JsonSubTypes.Type(value = EntityReferenceModel.class),
})
public interface FieldMirrorMixinJackson2 {

    @JsonCreator
    FieldMirrorMixinJackson2 init(
        @JsonProperty("name") String name,
        @JsonProperty("type") io.domainlifecycles.mirror.api.AssertedContainableTypeMirror type,
        @JsonProperty("accessLevel") io.domainlifecycles.mirror.api.AccessLevel accessLevel,
        @JsonProperty("declaredByTypeName") String declaredByTypeName,
        @JsonProperty("modifiable") boolean modifiable,
        @JsonProperty("publicReadable") boolean publicReadable,
        @JsonProperty("publicWriteable") boolean publicWriteable,
        @JsonProperty("static") boolean isStatic,
        @JsonProperty("hidden") boolean hidden
    );

    @JsonIgnore
    boolean isIdentityField();
}