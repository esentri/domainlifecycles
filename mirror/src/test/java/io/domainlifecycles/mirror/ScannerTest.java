package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.AggregateRootMirror;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.api.EnumMirror;
import io.domainlifecycles.mirror.api.IdentityMirror;
import io.domainlifecycles.mirror.api.ValueObjectMirror;
import io.domainlifecycles.mirror.reflect.ClassGraphDomainTypesScanner;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainModelFactory;
import io.domainlifecycles.mirror.resolver.DefaultEmptyGenericTypeResolver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ScannerTest {

    @Test
    public void testScan() {
        var scanner = new ClassGraphDomainTypesScanner(new DefaultEmptyGenericTypeResolver());
        var scanned = scanner.scan("tests");

        log.info("Identity Class info: " + scanned.stream().filter(dt -> dt instanceof IdentityMirror).count());
        log.info("Enum info: " + scanned.stream().filter(dt -> dt instanceof EnumMirror).count());
        log.info("ValueObject Class info: " + scanned.stream().filter(dt -> dt instanceof ValueObjectMirror).count());
        log.info("Entities Class info: " + scanned.stream().filter(dt -> dt instanceof EntityMirror).count());
        log.info("Aggregate Root Class info: " + scanned.stream().filter(dt -> dt instanceof AggregateRootMirror).count());

    }

    @Test
    public void testReflectiveFactory() {
        var factory = new ReflectiveDomainModelFactory("tests");
        Domain.initialize(factory);
    }

}
