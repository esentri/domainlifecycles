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

package io.domainlifecycles.mirrorjmolecules.reflect;

import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.reflect.DefaultDomainTypeDetector;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Entity;
import org.jmolecules.ddd.types.Identifier;
import org.jmolecules.ddd.types.Repository;
import org.jmolecules.ddd.types.ValueObject;
import org.jmolecules.event.types.DomainEvent;

import java.lang.reflect.Type;

/**
 * An extended implementation of {@link DefaultDomainTypeDetector} that enhances the detection of domain
 * types based on domain-driven design (DDD) principles. This class adds support for additional checks
 * using the {@link org.jmolecules.ddd.annotation} annotations to classify types into specific domain types.
 *
 * This class overrides the {@link DefaultDomainTypeDetector#detectDomainType(Type)} method to provide
 * supplemental logic for identifying DDD concepts such as Aggregate Roots, Entities, Value Objects,
 * Identifiers, Repositories, Domain Events, and Domain Services. It also leverages annotations for
 * classification, enabling more robust detection where base class or interface assignment may not exist.
 *
 * The classification process includes:
 * - Checking if the type is assignable to common DDD-related classes or interfaces, such as
 *   {@link AggregateRoot}, {@link Entity}, {@link ValueObject}, {@link Identifier}, {@link Repository},
 *   and {@link DomainEvent}.
 * - Checking for the presence of JMolecules DDD annotations like
 *   {@link org.jmolecules.ddd.annotation.AggregateRoot}, {@link org.jmolecules.ddd.annotation.Entity},
 *   {@link org.jmolecules.ddd.annotation.ValueObject}, {@link org.jmolecules.ddd.annotation.Repository},
 *   and {@link org.jmolecules.ddd.annotation.Service}.
 *
 * If the domain type cannot be determined using the extended logic, it falls back to the default
 * detection logic provided by {@link DefaultDomainTypeDetector}.
 *
 * The domain type classification results in one of the {@link DomainType} values, such as
 * {@link DomainType#AGGREGATE_ROOT}, {@link DomainType#ENTITY}, {@link DomainType#VALUE_OBJECT},
 * {@link DomainType#IDENTITY}, {@link DomainType#REPOSITORY}, {@link DomainType#DOMAIN_EVENT},
 * {@link DomainType#DOMAIN_SERVICE}, or the default {@link DomainType#NON_DOMAIN}.
 *
 * @author Mario Herb
 */
public class ExtendedJMoleculesDomainTypeDetector extends DefaultDomainTypeDetector {

    /**
     * Detects the domain type of the given {@code type} based on domain-driven design (DDD) principles.
     * The method extends the base detection functionality by adding additional checks for domain-specific
     * classifications using jmolecules annotations and interfaces.
     *
     * @param type the {@link Type} to be inspected and classified into a specific domain type.
     *             This can be a class, interface, or another Type representation. The method evaluates
     *             whether the type implements or is annotated with domain-driven design concepts such as
     *             {@code AggregateRoot}, {@code Entity}, {@code ValueObject}, {@code Repository}, or
     *             other relevant domain concepts.
     *
     * @return the {@link DomainType} classification of the provided {@code type}, such as
     *         {@code DomainType.AGGREGATE_ROOT}, {@code DomainType.ENTITY}, {@code DomainType.VALUE_OBJECT},
     *         or {@code DomainType.NON_DOMAIN} if no valid domain classification is found.
     */
    @Override
    public DomainType detectDomainType(Type type) {
        var dt = super.detectDomainType(type);
        if(DomainType.NON_DOMAIN.equals(dt)){
            if (type instanceof Class<?> c) {
                if (AggregateRoot.class.isAssignableFrom(c)) {
                    return DomainType.AGGREGATE_ROOT;
                } else if (Entity.class.isAssignableFrom(c)) {
                    return DomainType.ENTITY;
                } else if (ValueObject.class.isAssignableFrom(c)) {
                    return DomainType.VALUE_OBJECT;
                } else if (Identifier.class.isAssignableFrom(c)) {
                    return DomainType.IDENTITY;
                } else if (Repository.class.isAssignableFrom(c)) {
                    return DomainType.REPOSITORY;
                } else if (DomainEvent.class.isAssignableFrom(c)) {
                    return DomainType.DOMAIN_EVENT;
                } else if (c.isAnnotationPresent(org.jmolecules.ddd.annotation.AggregateRoot.class)){
                    return DomainType.AGGREGATE_ROOT;
                } else if (c.isAnnotationPresent(org.jmolecules.ddd.annotation.Entity.class)){
                    return DomainType.ENTITY;
                } else if (c.isAnnotationPresent(org.jmolecules.ddd.annotation.ValueObject.class)){
                    return DomainType.VALUE_OBJECT;
                } else if (c.isAnnotationPresent(org.jmolecules.ddd.annotation.Repository.class)){
                    return DomainType.REPOSITORY;
                } else if (c.isAnnotationPresent(org.jmolecules.ddd.annotation.Service.class)){
                    return DomainType.DOMAIN_SERVICE;
                }
            }
        }
        return dt;
    }

}
