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

package io.domainlifecycles.mirror.model;

import io.domainlifecycles.mirror.api.AssertionMirror;

import java.util.Objects;

/**
 * Represents a model for assertions implementing the {@code AssertionMirror} interface.
 * This class encapsulates details about an assertion, including its type, associated parameters,
 * and a descriptive message.
 *
 * AssertionModel is designed to provide an immutable representation of an assertion
 * by maintaining its properties in final fields.
 *
 * @author Mario Herb
 */
public class AssertionModel implements AssertionMirror {

    private final AssertionType assertionType;
    private final String param1;
    private final String param2;
    private final String message;

    /**
     * Constructs an instance of {@code AssertionModel} which represents a specific assertion
     * with its defined type and parameters.
     *
     * @param assertionType the type of the assertion; must not be null
     * @param param1 the first parameter associated with the assertion
     * @param param2 the second parameter associated with the assertion
     * @param message the message or description related to the assertion
     */
    public AssertionModel(AssertionType assertionType,
                          String param1,
                          String param2,
                          String message) {
        this.assertionType = Objects.requireNonNull(assertionType);
        this.param1 = param1;
        this.param2 = param2;
        this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssertionType getAssertionType() {
        return assertionType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParam1() {
        return param1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParam2() {
        return param2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AssertionModel{" +
            "assertionType=" + assertionType +
            ", param1='" + param1 + '\'' +
            ", param2='" + param2 + '\'' +
            ", message='" + message + '\'' +
            '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssertionModel that = (AssertionModel) o;
        return assertionType == that.assertionType && Objects.equals(param1, that.param1) && Objects.equals(param2,
            that.param2) && Objects.equals(message, that.message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(assertionType, param1, param2, message);
    }
}
