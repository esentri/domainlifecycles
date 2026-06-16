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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.ApplicationService;
import io.domainlifecycles.domain.types.DomainCommand;
import io.domainlifecycles.domain.types.DomainEvent;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Entity;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.OutboundService;
import io.domainlifecycles.domain.types.QueryHandler;
import io.domainlifecycles.domain.types.ReadModel;
import io.domainlifecycles.domain.types.Repository;
import io.domainlifecycles.domain.types.ServiceKind;
import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.mirror.api.DomainType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

/**
 * Implementation of the {@link DomainTypeDetector} interface that detects the domain type classification
 * of a given type based on domain-driven design (DDD) principles. This class provides logic to map
 * various types to known domain categories, such as aggregate roots, entities, value objects, and
 * other DDD-specific classifications.
 *
 * The detection relies on checking if the provided type implements or extends specific base classes
 * or interfaces associated with DDD concepts. If the type does not belong to any known classifications,
 * it defaults to {@link DomainType#NON_DOMAIN}.
 *
 * @author Mario Herb
 */
public class DefaultDomainTypeDetector implements DomainTypeDetector {
    private static final Logger log = LoggerFactory.getLogger(DefaultDomainTypeDetector.class);

    /**
     * Detects the domain type of the given {@code type} based on domain-driven design (DDD) principles.
     * The method determines the classification of the provided {@code type} by checking if it implements
     * or extends specific DDD-related interfaces or classes. If the {@code type} does not belong to any
     * recognized DDD classifications, it will return {@link DomainType#NON_DOMAIN}.
     *
     * @param type the {@link Type} to be inspected and classified into a specific domain type.
     *             This can be a class, interface, or other Type representation.
     * @return the {@link DomainType} classification of the provided {@code type}, such as
     *         {@link DomainType#AGGREGATE_ROOT}, {@link DomainType#ENTITY}, or {@link DomainType#NON_DOMAIN}
     *         if the provided {@code type} does not match any known classification.
     */
    @Override
    public DomainType detectDomainType(Type type) {
        if (type instanceof Class<?> c) {
            if (AggregateRoot.class.isAssignableFrom(c)) {
                return DomainType.AGGREGATE_ROOT;
            } else if (Entity.class.isAssignableFrom(c)) {
                return DomainType.ENTITY;
            } else if (ValueObject.class.isAssignableFrom(c)) {
                return DomainType.VALUE_OBJECT;
            } else if (Enum.class.isAssignableFrom(c)) {
                return DomainType.ENUM;
            } else if (Identity.class.isAssignableFrom(c)) {
                return DomainType.IDENTITY;
            } else if (DomainService.class.isAssignableFrom(c)) {
                return DomainType.DOMAIN_SERVICE;
            } else if (Repository.class.isAssignableFrom(c)) {
                return DomainType.REPOSITORY;
            } else if (DomainEvent.class.isAssignableFrom(c)) {
                return DomainType.DOMAIN_EVENT;
            } else if (DomainCommand.class.isAssignableFrom(c)) {
                return DomainType.DOMAIN_COMMAND;
            } else if (ReadModel.class.isAssignableFrom(c)) {
                return DomainType.READ_MODEL;
            } else if (ApplicationService.class.isAssignableFrom(c)) {
                return DomainType.APPLICATION_SERVICE;
            } else if (QueryHandler.class.isAssignableFrom(c)) {
                return DomainType.QUERY_HANDLER;
            } else if (OutboundService.class.isAssignableFrom(c)) {
                return DomainType.OUTBOUND_SERVICE;
            } else if (ServiceKind.class.isAssignableFrom(c)) {
                return DomainType.SERVICE_KIND;
            }
        }
        return DomainType.NON_DOMAIN;
    }
}
