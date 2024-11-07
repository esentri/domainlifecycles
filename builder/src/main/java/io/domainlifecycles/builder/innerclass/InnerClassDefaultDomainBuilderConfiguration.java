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

package io.domainlifecycles.builder.innerclass;

import io.domainlifecycles.builder.DomainBuilderConfiguration;

import java.util.function.Function;

/**
 * The default configuration for inner builders.
 *
 * @author Dominik Galler
 */
public final class InnerClassDefaultDomainBuilderConfiguration implements DomainBuilderConfiguration {

    private final Function<String, String> f = k -> "set" + k.substring(0, 1).toUpperCase() + k.substring(1);

    /**
     * Creates new default inner builder configuration.
     */
    public InnerClassDefaultDomainBuilderConfiguration() {
        // Default Constructor
    }

    /**
     * The default builder method name.
     *
     * @return "builder"
     */
    @Override
    public String builderMethodName() {
        return "builder";
    }

    /**
     * The default build method name.
     *
     * @return "build"
     */
    @Override
    public String buildMethodName() {
        return "build";
    }

    /**
     * The default setter name.
     *
     * @return String
     */
    @Override
    public String setterFromPropertyName(String propertyName) {
        return f.apply(propertyName);
    }
}
