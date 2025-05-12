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

import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.converter.TypeConverter;
import io.domainlifecycles.persistence.mapping.converter.TypeConverterProvider;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * Specifies the package location for the default type converters.
     * This constant defines the base package used to scan and load
     * type converter implementations provided as defaults.
     */
    public static final String DEFAULT_CONVERTERS_PACKAGE = "io.domainlifecycles.persistence.mapping.converter.def";

    private static final Logger log = LoggerFactory.getLogger(DefaultTypeConverterProvider.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TypeConverter<?, ?>> provideConverters() {
        try (ScanResult scanResult =
                 new ClassGraph()
                     .acceptPackages(DEFAULT_CONVERTERS_PACKAGE)
                     .scan()) {
            return (List<TypeConverter<?, ?>>) scanResult.getAllClasses()
                .stream()
                .map(ClassInfo::loadClass)
                .filter(TypeConverter.class::isAssignableFrom)
                .map(tcc -> {
                    try {
                        return tcc.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException |
                             InvocationTargetException | NoSuchMethodException e) {
                        throw DLCPersistenceException.fail("Couldn't provide default converters!");
                    }
                })
                .collect(Collectors.toList());
        } catch (Throwable t) {
            String msg = String.format("Scanning package '%s' failed!", DEFAULT_CONVERTERS_PACKAGE);
            log.error(msg);
            throw DLCPersistenceException.fail(msg, t);
        }
    }
}
