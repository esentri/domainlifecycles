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

package nitrox.dlc.swagger.v3.resource;

import java.util.ListResourceBundle;

/**
 * Default resource bundle (german) for bean validation descriptions in Open API docs.
 *
 * @author Mario Herb
 */
public class ResourceBundle_de_DE extends ListResourceBundle {

    /**
     * Resource bundle programmatic declaration.
     *
     * @return array of defined contents
     */
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
            { "beanValidationNotEmpty", "Der Wert darf nicht leer sein!" },
            { "beanValidationNotBlank", "Der Wert darf nicht leer sein und darf nicht ausschließlich aus Leerzeichen bestehen!" },
            { "beanValidationEmail", "Der Wert muss einer validen Email-Adresse entsprechen!" },
            { "beanValidationPattern", "Der Wert muss dem angegebenen regulären Ausdruck entsprechen!" },
            { "beanValidationSizeMin", "Die Länge muss größer oder gleich %s sein!" },
            { "beanValidationSizeMax", "Die Länge muss kleiner oder gleich %s sein!" },
            { "beanValidationDecimalMin", "Der Wert muss größer sein als %s!" },
            { "beanValidationDecimalMinInclusive", "Der Wert muss größer oder gleich sein als %s!" },
            { "beanValidationDecimalMax", "Der Wert muss kleiner sein als %s!" },
            { "beanValidationDecimalMaxInclusive", "Der Wert muss kleiner oder gleich sein als %s!" },
            { "beanValidationNegative", "Der Wert muss negativ sein!" },
            { "beanValidationPositive", "Der Wert muss positiv sein!" },
            { "beanValidationNegativeOrZero", "Der Wert muss negativ oder gleich Null sein!" },
            { "beanValidationPositiveOrZero", "Der Wert muss positiv oder gleich Null sein!" },
            { "beanValidationDigitsInteger", "Der Wert darf höchstens aus %s Ziffern bestehen!" },
            { "beanValidationDigitsIntegerFraction", "Der Wert darf höchstens aus %s Ziffern vor und höchstens aus %s Ziffern nach dem Dezimalpunkt bestehen!" },
            { "beanValidationPast", "Der Wert muss in der Vergangenheit liegen!" },
            { "beanValidationFuture", "Der Wert muss in der Zukunft liegen!" },
            { "beanValidationPastOrPresent", "Der Wert muss in der Vergangenheit liegen oder dem aktuellen Zeitpunkt entsprechen!" },
            { "beanValidationFutureOrPresent", "Der Wert muss in der Zukunft liegen oder dem aktuellen Zeitpunkt entsprechen!" },
        };
    }
}
