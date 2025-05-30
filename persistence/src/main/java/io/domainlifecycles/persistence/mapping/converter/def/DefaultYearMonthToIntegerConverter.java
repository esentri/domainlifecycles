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

package io.domainlifecycles.persistence.mapping.converter.def;

import io.domainlifecycles.persistence.mapping.converter.TypeConverter;

import java.time.YearMonth;

/**
 * Converts a YearMonth to a Integer.
 *
 * @author Mario Herb
 */
public class DefaultYearMonthToIntegerConverter extends TypeConverter<YearMonth, Integer> {

    /**
     * Default converter that transforms a {@link YearMonth} into an {@link Integer}.
     * This converter encodes the year and month into a single integer, where the year is multiplied
     * by 100 and the month is added to it. If the year is negative, the month is also negated.
     */
    public DefaultYearMonthToIntegerConverter() {
        super(YearMonth.class, Integer.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer convert(YearMonth from) {
        if (from == null) {
            return null;
        }
        int year = (from.getYear() * 100);
        int month = from.getMonthValue();
        if (year < 0) {
            month = month * (-1);
        }
        return year + month;
    }

}
