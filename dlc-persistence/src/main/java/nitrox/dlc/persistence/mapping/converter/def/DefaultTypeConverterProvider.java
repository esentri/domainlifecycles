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

import nitrox.dlc.persistence.exception.DLCPersistenceException;
import nitrox.dlc.persistence.mapping.converter.TypeConverter;
import nitrox.dlc.persistence.mapping.converter.TypeConverterProvider;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the default converters.
 *
 * @author Mario Herb
 */
public class DefaultTypeConverterProvider implements TypeConverterProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeConverter<?,?>> provideConverters() {
        var rfDefaultConverters = new Reflections("nitrox.dlc.persistence.mapping.converter.def");
        //noinspection unchecked
        return (List<TypeConverter<?, ?>>) rfDefaultConverters.getSubTypesOf(TypeConverter.class)
            .stream()
            .map(tcc -> {
                try {
                    return tcc.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException |
                         InvocationTargetException | NoSuchMethodException e) {
                    throw DLCPersistenceException.fail("Couldn't provide default converters!");
                }
            })
            .collect(Collectors.toList());
    }
}
