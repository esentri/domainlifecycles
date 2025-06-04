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

package io.domainlifecycles.validation.javax;


import io.domainlifecycles.assertion.DomainAssertionException;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * This class wraps Bean Validation (Javax) {@link jakarta.validation.ConstraintViolation} violations,
 * so that they can be thrown as {@link DomainAssertionException}.
 *
 * @author Mario Herb
 */
@Deprecated
public class JavaxBeanValidationDomainAssertionException extends DomainAssertionException {

    /**
     * The set of violations that caused the exception.
     */
    public final Set<ConstraintViolation<Object>> violations;

    /**
     * Constructor
     *
     * @param message    for exception
     * @param violations causes
     */
    public JavaxBeanValidationDomainAssertionException(String message, Set<ConstraintViolation<Object>> violations) {
        super(message);
        this.violations = violations;
    }
}
