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

package io.domainlifecycles.mirror;

import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.DomainTypesScanner;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.Test;

@Slf4j
public class ScannerTest {

    @Test
    public void testScan() {
        var scanner = new DomainTypesScanner();
        scanner.scan("tests");

        log.info("Identity Class info: " + scanner.getScannedIdentities().stream().count());
        log.info("Enum info: " + scanner.getScannedEnums().stream().count());
        log.info("ValueObject Class info: " + scanner.getScannedValueObjects().stream().count());
        log.info("Entities Class info: " + scanner.getScannedEntities().stream().count());
        log.info("Aggregate Root Class info: " + scanner.getScannedAggregateRoots().stream().count());

    }

    @Test
    public void testReflectiveFactory() {
        var factory = new ReflectiveDomainMirrorFactory("tests");
        Domain.initialize(factory);
    }

}
