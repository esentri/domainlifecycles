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

package nitrox.dlc.persistence.mapping.converter.def;

import nitrox.dlc.persistence.mapping.converter.TypeConverter;

import java.math.BigDecimal;

/**
 *  Converts a Double to a BigDecimal.
 *
 * @author Mario Herb
 */
public class DefaultDoubleToBigDecimalConverter extends TypeConverter<Double, BigDecimal> {


    public DefaultDoubleToBigDecimalConverter() {
        super(Double.class, BigDecimal.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal convert(Double from) {
        if (from == null) {
            return null;
        }
        return BigDecimal.valueOf(from);
    }
}
