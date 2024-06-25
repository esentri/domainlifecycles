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

package nitrox.dlc.mirror.serialize.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nitrox.dlc.mirror.model.AggregateRootReferenceModel;
import nitrox.dlc.mirror.model.EntityReferenceModel;
import nitrox.dlc.mirror.model.FieldModel;
import nitrox.dlc.mirror.model.ValueReferenceModel;

/**
 * Jackson mixin interface for proper serialization of {@link FieldModel} and all its subtypes.
 *
 * @author Mario Herb
 */
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, property="@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FieldModel.class),
    @JsonSubTypes.Type(value = AggregateRootReferenceModel.class),
    @JsonSubTypes.Type(value = ValueReferenceModel.class),
    @JsonSubTypes.Type(value = EntityReferenceModel.class),
})
public interface FieldMirrorMixin {}