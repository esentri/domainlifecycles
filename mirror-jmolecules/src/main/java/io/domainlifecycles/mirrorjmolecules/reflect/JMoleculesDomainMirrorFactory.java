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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.mirrorjmolecules.reflect;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainMirrorFactory;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.model.DomainModel;

import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.DefaultEmptyGenericTypeResolver;
import io.domainlifecycles.mirror.validate.CompletenessChecker;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The JMoleculesDomainMirrorFactory is an extended implementation of the ReflectiveDomainMirrorFactory
 * that leverages enhanced domain type scanning capabilities specific to the jMolecules framework.
 * It facilitates the creation of a DomainMirror, a container for all domain type mirrors
 * analyzed within the specified bounded contexts and additional packages relevant to jMolecules domain types.
 *
 * This factory is designed to initialize and validate a domain model structure based on
 * scanned domain type classes. It extends the capabilities of the reflective domain initialization
 * by incorporating additional domain lifecycle and type definitions provided by the jMolecules framework.
 *
 * @author Mario Herb
 */
public class JMoleculesDomainMirrorFactory extends ReflectiveDomainMirrorFactory implements DomainMirrorFactory {

    private static final Logger log = LoggerFactory.getLogger(JMoleculesDomainMirrorFactory.class);

    private ExtendedJMoleculesDomainTypesScanner extendedJMoleculesDomainTypesScanner;

    /**
     * Initialize the factory with the domainModelPackages to be scanned.
     *
     * @param domainModelPackages the packages containing the domain model classes
     */
    public JMoleculesDomainMirrorFactory(String... domainModelPackages) {
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
        var domainModelPackagesExtended = Arrays.copyOf(domainModelPackages, domainModelPackages.length+2);
        domainModelPackagesExtended[domainModelPackages.length] = "io.domainlifecycles";
        domainModelPackagesExtended[domainModelPackages.length+1] = "org.jmolecules.ddd.types";
        Map<String, ? extends DomainTypeMirror> builtTypeMirrors =
            extendedJMoleculesDomainTypesScanner
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
            .forEach(m -> log.debug("Created Mirror:" + m));

        var dm = new DomainModel(builtTypeMirrors, boundedContextPackages);
        var c = new CompletenessChecker(dm);
        c.checkForCompleteness();

        return dm;
    }

    private void initializeForScanning(){
        if(this.genericTypeResolver == null){
            this.genericTypeResolver = new DefaultEmptyGenericTypeResolver();
        }
        if(this.domainTypeDetector == null){
            this.domainTypeDetector = new ExtendedJMoleculesDomainTypeDetector();
        }
        if(this.externalClassLoader == null){
            this.extendedJMoleculesDomainTypesScanner = new ExtendedJMoleculesDomainTypesScanner(genericTypeResolver, domainTypeDetector);
        }else{
            this.extendedJMoleculesDomainTypesScanner = new ExtendedJMoleculesDomainTypesScanner(externalClassLoader, genericTypeResolver, domainTypeDetector);
        }
        if(boundedContextPackages == null){
            this.boundedContextPackages = domainModelPackages;
        }
        validatePackages(boundedContextPackages);
    }
}
