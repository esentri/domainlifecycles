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

package io.domainlifecycles.validation;

import io.domainlifecycles.validation.jakarta.JakartaValidations;
import io.domainlifecycles.validation.javax.JavaxValidations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Method;

/**
 * Generic static Bean Validation support, supporting Bean Validation 2.0 (Javax)
 * as well as Bean Validations 3.0 (Jakarta).
 * <p>
 * The static constructor initializes the validator factory, that is available in the class path.
 *
 * @author Mario Herb
 */
public class BeanValidations {

    private static ValidatorFactory javaxFactory = null;
    private static jakarta.validation.ValidatorFactory jakartaFactory = null;
    private static final Logger log = LoggerFactory.getLogger(BeanValidations.class);

    static {
        try {
            jakartaFactory = jakarta.validation.Validation.buildDefaultValidatorFactory();
        } catch (Throwable t) {
            log.warn("Jakarta Bean Validation disabled. ValidatorFactory could not be created!");
        }
        try {
            javaxFactory = Validation.buildDefaultValidatorFactory();
        } catch (Throwable t) {
            log.warn("Javax Bean Validation disabled. ValidatorFactory could not be created!");
        }
    }

    /**
     * Generic object validation using bean validation
     *
     * @param thisObject {@link Object} to be validated
     */
    public static void validate(Object thisObject) {
        if (jakartaFactory != null) {
            JakartaValidations.validate(jakartaFactory, thisObject);
        }
        if (javaxFactory != null) {
            JavaxValidations.validate(javaxFactory, thisObject);
        }
    }

    /**
     * Generic method parameter validation implementation using bean validations.
     *
     * @param thisObject validated
     * @param method     validated
     * @param arguments  parameter argument to be validated
     */
    public static void validateMethodParameters(Object thisObject, Method method, Object[] arguments) {
        if (jakartaFactory != null) {
            JakartaValidations.validateMethodParameters(jakartaFactory, thisObject, method, arguments);
        }
        if (javaxFactory != null) {
            JavaxValidations.validateMethodParameters(javaxFactory, thisObject, method, arguments);
        }
    }

    /**
     * Generic return value validation implementation using bean validations.
     *
     * @param thisObject  {@link Object}
     * @param method      validated
     * @param returnValue {@link Object} to be validated
     */
    public static void validateMethodReturnValue(Object thisObject, Method method, Object returnValue) {
        if (jakartaFactory != null) {
            JakartaValidations.validateMethodReturnValue(jakartaFactory, thisObject, method, returnValue);
        }
        if (javaxFactory != null) {
            JavaxValidations.validateMethodReturnValue(javaxFactory, thisObject, method, returnValue);
        }
    }

}
