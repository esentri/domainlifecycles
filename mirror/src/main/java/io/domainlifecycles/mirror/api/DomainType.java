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

import java.lang.reflect.Type;

/**
 * Every mirrored class is either one of the here defined DomainTypes or it is {@code NON_DOMAIN}.
 *
 * @author Mario Herb
 */
public enum DomainType {
    /**
     * Represents the domain type "root entity of an Aggregate" in the domain-driven design (DDD) context.
     */
    AGGREGATE_ROOT,
    /**
     * Represents the domain type "entity" in a domain-driven design (DDD) context.
     */
    ENTITY,
    /**
     * Represents the domain type "value object" in a domain-driven design (DDD) context.
     */
    VALUE_OBJECT,
    /**
     * Represents the domain type constant or variable that typically represents value
     * used to uniquely identify an entity.
     */
    IDENTITY,
    /**
     * Represents the domain type used to clearly define a predefined set of constants
     * that represent specific options or states within a domain-driven design (DDD) context.
     */
    ENUM,
    /**
     * Represents the domain type "domain event" within a domain-driven design (DDD) context.
     */
    DOMAIN_EVENT,
    /**
     * Represents the domain type "domain command" within a domain-driven design (DDD) context.
     */
    DOMAIN_COMMAND,
    /**
     * Represents the domain type "domain service" within a domain-driven design (DDD) context.
     */
    DOMAIN_SERVICE,
    /**
     * Represents the domain type "repository" within a domain-driven design (DDD) context.
     */
    REPOSITORY,
    /**
     * Represents the domain type "read model" within a domain-driven design (DDD) context.
     */
    READ_MODEL,
    /**
     * Represents the domain type "application service" within a domain-driven design (DDD) context.
     */
    APPLICATION_SERVICE,
    /**
     * Represents the domain type "unspecified service kind" within a domain-driven design (DDD) context.
     */
    SERVICE_KIND,
    /**
     * Represents the domain type "query handler" within a domain-driven design (DDD) context.
     */
    QUERY_HANDLER,
    /**
     * Represents the domain type "outbound service" within a domain-driven design (DDD) context.
     */
    OUTBOUND_SERVICE,
    /**
     * Represents the domain type for non domain objects used within a domain-driven design (DDD) context.
     */
    NON_DOMAIN;

    /**
     * Derives the DomainType form a given Java {@link Type}.
     *
     * @param type the type to return the corresponding DomainType for
     * @return the reflected DomainType for the given type
     */
    public static DomainType of(Type type) {
        if (type instanceof Class<?>) {
            Class<?> c = (Class<?>) type;
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
