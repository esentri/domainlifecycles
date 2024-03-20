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
 *  Copyright 2019-2023 the original author or authors.
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

package nitrox.dlc.builder;

import nitrox.dlc.domain.types.internal.DomainObject;

/**
 * Common abstract class that is used for providers of DomainObjectBuilders.
 * A provider is used to obtain a DomainObjectBuilder for a given domain object class {@link DomainObject}.
 *
 * @author Dominik Galler
 * @author Mario Herb
 */
public abstract class DomainObjectBuilderProvider {

    private final DomainBuilderConfiguration domainBuilderConfiguration;

    /**
     * Creates a new DomainObjectBuilderProvider.
     *
     * @param domainBuilderConfiguration for builder conventions
     */
    public DomainObjectBuilderProvider(DomainBuilderConfiguration domainBuilderConfiguration) {

        this.domainBuilderConfiguration = domainBuilderConfiguration;
    }

    /**
     * Provides a DomainObjectBuilder for a given class of {@link DomainObject} (domain object)
     *
     * @param domainObjectTypeName The full qualified type name of a type to provide a DomainObjectBuilder for
     * @param <T>   The DomainType to be built
     * @return DomainObjectBuilder
     */
    public abstract <T extends DomainObject> DomainObjectBuilder<T> provide(String domainObjectTypeName);

    /**
     * To get the {@link DomainBuilderConfiguration} for this provider.
     * @return the configuration instance
     */
    public DomainBuilderConfiguration getBuilderConfiguration() {
        return this.domainBuilderConfiguration;
    }


}
