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

package io.domainlifecycles.validation.javax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * Static access to execute validation checks on annotated objects (Javax Bean Validation annotations).
 *
 * @author Mario Herb
 */
public class JavaxValidations {

    private static final Logger log = LoggerFactory.getLogger(JavaxValidations.class);

    /**
     * Generic object validation implementation using bean validations.
     *
     * @param javaxFactory to create a validator instance
     * @param thisObject   {@link Object} to be validated
     */
    public static void validate(ValidatorFactory javaxFactory, Object thisObject) {
        Objects.requireNonNull(javaxFactory);
        Set<ConstraintViolation<Object>> violations = null;
        try {
            Validator validator = javaxFactory.getValidator();
            violations = validator.validate(thisObject);
        } catch (Throwable t) {
            log.error("Something failed on executing bean validations!", t);
        }
        throwForViolationsJavax(violations);
    }

    /**
     * Generic return value validation using bean validation annotations.
     *
     * @param javaxFactory to create a validator instance
     * @param thisObject   {@link Object} validated
     * @param method       validated
     * @param returnValue  {@link Object} to be validated
     */
    public static void validateMethodReturnValue(ValidatorFactory javaxFactory, Object thisObject, Method method,
                                                 Object returnValue) {
        Objects.requireNonNull(javaxFactory);
        Set<ConstraintViolation<Object>> violations = null;
        try {
            ExecutableValidator executableValidator = javaxFactory.getValidator().forExecutables();
            violations = executableValidator.validateReturnValue(thisObject, method, returnValue);
        } catch (Throwable t) {
            log.error("Something failed on executing bean validations!", t);
        }
        throwForViolationsJavax(violations);

    }

    /**
     * Generic method parameter validation using bean validation annotations.
     *
     * @param javaxFactory to create a validator instance
     * @param thisObject   validated
     * @param method       validated
     * @param arguments    parameter argument to be validated
     */
    public static void validateMethodParameters(ValidatorFactory javaxFactory, Object thisObject, Method method,
                                                Object[] arguments) {
        Objects.requireNonNull(javaxFactory);
        Set<ConstraintViolation<Object>> violations = null;
        try {
            ExecutableValidator executableValidator = javaxFactory.getValidator().forExecutables();
            violations = executableValidator.validateParameters(thisObject, method, arguments);
        } catch (Throwable t) {
            log.error("Something failed on executing bean validations!", t);
        }
        throwForViolationsJavax(violations);
    }

    private static void throwForViolationsJavax(Set<ConstraintViolation<Object>> violations) {
        if (violations != null) {
            StringBuilder messageBuilder = new StringBuilder();
            for (ConstraintViolation<Object> violation : violations) {
                messageBuilder.append("'");
                messageBuilder.append(violation.getPropertyPath().toString());
                messageBuilder.append("' ");
                messageBuilder.append(violation.getMessage());
                messageBuilder.append("\n");
            }
            if (messageBuilder.length() > 0) {
                throw new JavaxBeanValidationDomainAssertionException(messageBuilder.toString(), violations);
            }
        }
    }
}
