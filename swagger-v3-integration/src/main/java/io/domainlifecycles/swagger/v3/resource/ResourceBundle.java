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

package io.domainlifecycles.swagger.v3.resource;

import java.util.ListResourceBundle;

/**
 * Default resource bundle (english) for bean validation descriptions in Open API docs.
 *
 * @author Mario Herb
 */
public class ResourceBundle extends ListResourceBundle {

    /**
     * Resource bundle programmatic declaration.
     *
     * @return array of defined contents
     */
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
            {"beanValidationNotEmpty", "The value must not be empty!"},
            {"beanValidationNotBlank", "The value must not be blank!"},
            {"beanValidationEmail", "The value must be a valid email address!"},
            {"beanValidationPattern", "The value must match the specified pattern!"},
            {"beanValidationSizeMin", "The length must be greater than or equal to %s!"},
            {"beanValidationSizeMax", "The length must be lower than or equal to %s!"},
            {"beanValidationDecimalMin", "The value must be greater than %s!"},
            {"beanValidationDecimalMinInclusive", "The value must be greater than or equal to %s!"},
            {"beanValidationDecimalMax", "The value must be lower than %s!"},
            {"beanValidationDecimalMaxInclusive", "The value must be lower than or equal to %s!"},
            {"beanValidationNegative", "The value must be negative!"},
            {"beanValidationPositive", "The value must be positive!"},
            {"beanValidationNegativeOrZero", "The value must be negative or zero!"},
            {"beanValidationPositiveOrZero", "The value must be positive or zero!"},
            {"beanValidationDigitsInteger", "The value must consist of a maximum of %s digits!"},
            {"beanValidationDigitsIntegerFraction", "The value must consist of a maximum of %s digits before the " +
                "decimal point and of a maximum of %s decimal places!"},
            {"beanValidationPast", "The value must be in the past!"},
            {"beanValidationFuture", "The value must be in the future!"},
            {"beanValidationPastOrPresent", "The value must be in the past or correspond to the current time!"},
            {"beanValidationFutureOrPresent", "The value must be in the future or correspond to the current time!"},
        };
    }
}
