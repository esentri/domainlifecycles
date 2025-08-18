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

package io.domainlifecycles.mirror.reflect;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainMirrorFactory;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.model.DomainModel;
import io.domainlifecycles.mirror.resolver.DefaultEmptyGenericTypeResolver;
import io.domainlifecycles.mirror.resolver.GenericTypeResolver;
import io.domainlifecycles.mirror.validate.CompletenessChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Uses Java reflection to initialize a mirror of the domain by analyzing the given bounded context packages.
 *
 * @author Mario Herb
 */
public class ReflectiveDomainMirrorFactory extends DomainMirrorFactoryUtils implements DomainMirrorFactory {

    private static final Logger log = LoggerFactory.getLogger(ReflectiveDomainMirrorFactory.class);

    /**
     * Initialize the factory with the domainModelPackages to be scanned.
     *
     * @param domainModelPackages the packages containing the domain model classes
     */
    public ReflectiveDomainMirrorFactory(String... domainModelPackages) {
        super(domainModelPackages);
    }

    /**
     * Initializes the domain with the scanned classes.
     *
     * @return DomainMirror - a container for all mirrors that are available in the analyzed bounded contexts.
     */
    @Override
    public DomainMirror initializeDomainMirror() {
        initializeForScanning();
        var domainModelPackagesExtended = Arrays.copyOf(domainModelPackages, domainModelPackages.length+1);
        domainModelPackagesExtended[domainModelPackages.length] = "io.domainlifecycles";

        Map<String, ? extends DomainTypeMirror> builtTypeMirrors =
            classGraphDomainTypesScanner
                .scan(domainModelPackagesExtended)
                .stream()
                .collect(
                    Collectors.toMap(
                        DomainTypeMirror::getTypeName,
                        Function.identity()
                    )
                );

        builtTypeMirrors
            .values()
            .forEach(m -> log.debug("Created Mirror: {}", m));

        var dm = new DomainModel(builtTypeMirrors, boundedContextPackages);
        var c = new CompletenessChecker(dm);
        c.checkForCompleteness();

        return dm;
    }
}
