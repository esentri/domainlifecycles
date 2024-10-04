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

package io.domainlifecycles.jooq.configuration.def;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.converter.def.DefaultTypeConverterProvider;
import io.domainlifecycles.persistence.records.RecordClassProvider;
import io.domainlifecycles.reflect.JavaReflect;
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

    public JooqRecordClassProvider(String recordPackage) {
        if (!JavaReflect.isValidPackage(recordPackage)) {
            throw new IllegalStateException(String.format("The package '%s' is not a valid package!", recordPackage));
        }
        this.recordPackage = recordPackage;
    }

    /**
     * {@inheritDoc}
     *
     * @return
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
