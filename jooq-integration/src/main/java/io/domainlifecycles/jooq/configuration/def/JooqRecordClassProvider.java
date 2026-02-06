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

package io.domainlifecycles.jooq.configuration.def;

import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.converter.def.DefaultTypeConverterProvider;
import io.domainlifecycles.persistence.records.RecordClassProvider;
import io.domainlifecycles.reflect.JavaReflect;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.jooq.UpdatableRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * jOOQ specific implementation of a {@link RecordClassProvider}.
 *
 * @author Mario Herb
 */
public class JooqRecordClassProvider implements RecordClassProvider<UpdatableRecord<?>> {

    private final String recordPackage;

    private static final Logger log = LoggerFactory.getLogger(DefaultTypeConverterProvider.class);

    /**
     * Constructs a {@code JooqRecordClassProvider} instance with the specified package name.
     *
     * The provided package name is validated to ensure it is a valid Java package.
     * If the package name is invalid, an {@code IllegalStateException} is thrown.
     *
     * @param recordPackage the name of the package where jOOQ record classes are located
     * @throws IllegalStateException if the provided {@code recordPackage} is not a valid Java package
     */
    public JooqRecordClassProvider(String recordPackage) {
        if (!JavaReflect.isValidPackage(recordPackage)) {
            throw new IllegalStateException(String.format("The package '%s' is not a valid package!", recordPackage));
        }
        this.recordPackage = recordPackage;
    }

    /**
     * Provides a set of classes implementing the {@code UpdatableRecord} interface from the specified package.
     *
     * The method scans the package defined in the {@code recordPackage} field for classes that implement
     * {@code UpdatableRecord}, loads those classes, and collects them into a set. If the package scan fails,
     * a {@code DLCPersistenceException} is thrown with an appropriate error message.
     *
     * @return a set of classes that implement {@code UpdatableRecord<?>}.
     * @throws DLCPersistenceException if scanning the specified package for record classes fails.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<Class<? extends UpdatableRecord<?>>> provideRecordClasses() {
        try (ScanResult scanResult =
                 new ClassGraph()
                     .acceptPackages(recordPackage)
                     .scan()) {
            return scanResult.getClassesImplementing(UpdatableRecord.class)
                .stream()
                .map(tcc -> (Class<? extends UpdatableRecord<?>>) tcc.loadClass())
                .collect(Collectors.toSet());
        } catch (Throwable t) {
            String msg = String.format("Scanning package '%s' failed!", recordPackage);
            log.error(msg);
            throw DLCPersistenceException.fail(msg, t);
        }
    }
}
