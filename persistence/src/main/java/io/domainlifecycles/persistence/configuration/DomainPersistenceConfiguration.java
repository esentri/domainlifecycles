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

package io.domainlifecycles.persistence.configuration;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.persistence.mapping.RecordMapper;

import java.util.Objects;
import java.util.Set;

/**
 * DomainPersistenceConfiguration is the base class for all domain persistence configurations.
 *
 * @author Mario Herb
 */
public abstract class DomainPersistenceConfiguration {


    public final DomainObjectBuilderProvider domainObjectBuilderProvider;
    public final Set<RecordMapper<?, ?, ?>> customRecordMappers;

    /**
     * Creates a new DomainPersistenceConfiguration.
     *
     * @param domainObjectBuilderProvider the domain object builder provider to use
     * @param customRecordMappers         custom record mappers to use
     */
    public DomainPersistenceConfiguration(DomainObjectBuilderProvider domainObjectBuilderProvider,
                                          Set<RecordMapper<?, ?, ?>> customRecordMappers) {
        this.domainObjectBuilderProvider = Objects.requireNonNull(domainObjectBuilderProvider);
        this.customRecordMappers = customRecordMappers;
    }
}
