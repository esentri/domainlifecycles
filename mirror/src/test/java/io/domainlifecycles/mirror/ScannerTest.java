package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.DomainTypesScanner;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import lombok.extern.slf4j.Slf4j;
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
