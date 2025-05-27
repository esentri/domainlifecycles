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

package io.domainlifecycles.persistence.configuration;

import io.domainlifecycles.builder.DomainObjectBuilder;
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

    /**
     * A provider of {@link DomainObjectBuilder} instances for building domain objects.
     * This field is used to obtain {@link DomainObjectBuilder} implementations based on
     * domain object types. It is core to the domain persistence configuration and enables
     * the dynamic creation of domain objects.
     */
    public final DomainObjectBuilderProvider domainObjectBuilderProvider;

    /**
     * A set of custom record mappers used for mapping between database records and domain objects.
     * This field allows the customization of how specific types of records are transformed
     * into domain objects and vice versa. Each mapper in the set implements the {@link RecordMapper}
     * interface and is responsible for handling a specific record type and domain object type.
     *
     * By providing custom record mappers, it becomes possible to support unique mapping requirements
     * or unusual database and domain model structures that cannot be managed with default mapping mechanisms.
     *
     * Typically used in domain persistence configurations to perform database operations, leveraging custom
     * transformation logic where necessary.
     */
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
