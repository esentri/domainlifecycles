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

package nitrox.dlc.services.api;

import nitrox.dlc.domain.types.ApplicationService;
import nitrox.dlc.domain.types.DomainService;
import nitrox.dlc.domain.types.OutboundService;
import nitrox.dlc.domain.types.QueryClient;
import nitrox.dlc.domain.types.Repository;

/**
 * The ServiceRegistrator interface represents a registrator that is responsible for registering instances of various types of services.
 * It allows registering instances of {@link ApplicationService}, {@link Repository} and {@link DomainService}.
 *
 * @author Mario Herb
 */
public interface ServiceRegistrator {

    /**
     * Registers an instance of {@link ApplicationService}.
     *
     * @param applicationService The instance of {@link ApplicationService} to register.
     */
    void registerApplicationServiceInstance(ApplicationService applicationService);

    /**
     * Registers an instance of {@link DomainService} with the {@link ServiceRegistrator}.
     *
     * @param domainService The {@link DomainService} instance to register.
     */
    void registerDomainServiceInstance(DomainService domainService);

    /**
     * Registers an instance of {@link Repository} with the {@link ServiceRegistrator}.
     *
     * @param repository The {@link Repository} instance to register.
     */
    void registerRepositoryInstance(Repository<?, ?> repository);

    /**
     * Registers an instance of {@link QueryClient} with the {@link ServiceRegistrator}.
     *
     * @param queryClient The {@link QueryClient} instance to register.
     */
    void registerQueryClientInstance(QueryClient<?> queryClient);

    /**
     * Registers an instance of {@link OutboundService} with the {@link ServiceRegistrator}.
     *
     * @param outboundService The {@link OutboundService} instance to register.
     */
    void registerOutboundServiceInstance(OutboundService outboundService);

}
