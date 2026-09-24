package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.NonDomainTypeMirror;
import io.domainlifecycles.mirror.api.ServiceKindMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class NonDomainTypeScanTest {

    @Test
    public void nonDomainClassesAreMirroredByDefaultAndReferencedByService() {
        var factory = new ReflectiveDomainMirrorFactory("tests.mirror.nondomain");
        var domainMirror = factory.initializeDomainMirror();

        List<NonDomainTypeMirror> nonDomainMirrors = domainMirror.getAllDomainTypeMirrors()
            .stream()
            .filter(dtm -> DomainType.NON_DOMAIN.equals(dtm.getDomainType()))
            .filter(dtm -> dtm.getTypeName().startsWith("tests.mirror.nondomain"))
            .map(dtm -> (NonDomainTypeMirror) dtm)
            .collect(Collectors.toList());

        assertThat(nonDomainMirrors)
            .extracting(NonDomainTypeMirror::getTypeName)
            .containsExactlyInAnyOrder(
                "tests.mirror.nondomain.NonDomainHelper",
                "tests.mirror.nondomain.UnreferencedHelper"
            );

        var service = domainMirror.getAllDomainServiceMirrors()
            .stream()
            .filter(s -> s.getTypeName().equals("tests.mirror.nondomain.NonDomainAwareService"))
            .findFirst()
            .orElseThrow();

        assertThat(service.getReferencedNonDomainTypes())
            .extracting(NonDomainTypeMirror::getTypeName)
            .containsExactly("tests.mirror.nondomain.NonDomainHelper");
    }

    @Test
    public void nonDomainClassesAreExcludedWhenDisabled() {
        var factory = new ReflectiveDomainMirrorFactory("tests.mirror.nondomain");
        factory.setIncludeNonDomainClasses(false);
        var domainMirror = factory.initializeDomainMirror();

        var nonDomainInScope = domainMirror.getAllDomainTypeMirrors()
            .stream()
            .filter(dtm -> DomainType.NON_DOMAIN.equals(dtm.getDomainType()))
            .filter(dtm -> dtm.getTypeName().startsWith("tests.mirror.nondomain"))
            .toList();

        assertThat(nonDomainInScope).isEmpty();

        ServiceKindMirror service = domainMirror.getAllDomainServiceMirrors()
            .stream()
            .filter(s -> s.getTypeName().equals("tests.mirror.nondomain.NonDomainAwareService"))
            .findFirst()
            .orElseThrow();

        assertThat(service.getReferencedNonDomainTypes()).isEmpty();
    }
}
