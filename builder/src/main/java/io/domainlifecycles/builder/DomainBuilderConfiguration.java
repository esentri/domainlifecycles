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

package io.domainlifecycles.builder;

import io.domainlifecycles.builder.innerclass.InnerClassDefaultDomainBuilderConfiguration;

/**
 * Common interface to provide DomainBuilderConfiguration. This configuration
 * can be used to customize builder behaviour.
 * <p>
 * see {@link InnerClassDefaultDomainBuilderConfiguration} for
 * reference implementation
 *
 * @author Dominik Galler
 */
public interface DomainBuilderConfiguration {

    /**
     * Method name, that returns the builder class instance.
     *
     * @return the method name, that returns the builder class instance
     */
    String builderMethodName();

    /**
     * Method name, that is used to build the instance, the builder is designed for.
     *
     * @return the method name, that builds the object instance (not the builder)
     */
    String buildMethodName();

    /**
     * Method to obtain the setter name by a given property name.
     * <p>
     * For example if the property name is 'acme' and setters are prefixed with set,
     * then this method should return 'setAcme'
     * </p>
     *
     * @param propertyName the property name to obtain the setter name for
     * @return the setter name for the given property.
     */
    String setterFromPropertyName(String propertyName);

}
