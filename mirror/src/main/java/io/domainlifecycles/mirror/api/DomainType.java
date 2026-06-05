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

}
