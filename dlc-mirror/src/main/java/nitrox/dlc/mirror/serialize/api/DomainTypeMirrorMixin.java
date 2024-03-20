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
import nitrox.dlc.mirror.model.AggregateRootModel;
import nitrox.dlc.mirror.model.ApplicationServiceModel;
import nitrox.dlc.mirror.model.DomainCommandModel;
import nitrox.dlc.mirror.model.DomainEventModel;
import nitrox.dlc.mirror.model.DomainServiceModel;
import nitrox.dlc.mirror.model.EntityModel;
import nitrox.dlc.mirror.model.EnumModel;
import nitrox.dlc.mirror.model.IdentityModel;
import nitrox.dlc.mirror.model.OutboundServiceModel;
import nitrox.dlc.mirror.model.ReadModelProviderModel;
import nitrox.dlc.mirror.model.RepositoryModel;
import nitrox.dlc.mirror.model.ValueObjectModel;

/**
 * Generic Jackson mixin interface for proper serialization of domain type models.
 *
 * @author Mario Herb
 */
@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, property="@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EntityModel.class),
    @JsonSubTypes.Type(value = IdentityModel.class),
    @JsonSubTypes.Type(value = DomainServiceModel.class),
    @JsonSubTypes.Type(value = RepositoryModel.class),
    @JsonSubTypes.Type(value = DomainEventModel.class),
    @JsonSubTypes.Type(value = ValueObjectModel.class),
    @JsonSubTypes.Type(value = AggregateRootModel.class),
    @JsonSubTypes.Type(value = EnumModel.class),
    @JsonSubTypes.Type(value = DomainCommandModel.class),
    @JsonSubTypes.Type(value = ApplicationServiceModel.class),
    @JsonSubTypes.Type(value = ReadModelProviderModel.class),
    @JsonSubTypes.Type(value = OutboundServiceModel.class),
})
public interface DomainTypeMirrorMixin {}
