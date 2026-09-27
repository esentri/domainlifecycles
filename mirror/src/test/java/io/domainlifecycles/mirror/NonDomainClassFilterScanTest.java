package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;
import io.domainlifecycles.mirror.reflect.NonDomainClassFilter;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers leaving generated code out of the mirrored non-domain classes ({@link NonDomainClassFilter}).
 */
public class NonDomainClassFilterScanTest {

    private static final String PACKAGE = "tests.mirror.nondomainfilter";

    @Test
    public void jooqGeneratedClassesAreExcludedByDefault() {

        List<String> nonDomainTypeNames = nonDomainTypeNames(factory -> { });

        assertThat(nonDomainTypeNames).containsExactlyInAnyOrder(
            PACKAGE + ".PlainHelper",
            PACKAGE + ".CustomGeneratedClass",
            PACKAGE + ".CustomGeneratedViaInterface",
            PACKAGE + ".IntermediateBase",
            PACKAGE + ".generated.Keys"
        );
    }

    @Test
    public void jooqGeneratedClassesAreMirrored_When_SupertypeExclusionIsSwitchedOff() {

        List<String> nonDomainTypeNames = nonDomainTypeNames(factory -> factory.setNonDomainExcludedSupertypePackages(List.of()));

        assertThat(nonDomainTypeNames).contains(PACKAGE + ".generated.GeneratedTable");
    }

    @Test
    public void classesAreExcludedByConfiguredSupertypePackages_DirectOrInherited() {

        List<String> nonDomainTypeNames = nonDomainTypeNames(factory ->
            factory.setNonDomainExcludedSupertypePackages(List.of("org.jooq", "tests.mirror.nondomainbase")));

        assertThat(nonDomainTypeNames).containsExactlyInAnyOrder(
            PACKAGE + ".PlainHelper",
            PACKAGE + ".generated.Keys"
        );
    }

    @Test
    public void classesAreExcludedByConfiguredPackages() {

        List<String> nonDomainTypeNames = nonDomainTypeNames(factory ->
            factory.setNonDomainExcludedPackages(List.of(PACKAGE + ".generated")));

        assertThat(nonDomainTypeNames)
            .doesNotContain(PACKAGE + ".generated.Keys", PACKAGE + ".generated.GeneratedTable")
            .contains(PACKAGE + ".PlainHelper");
    }

    @Test
    public void nullRestoresTheDefaults() {

        List<String> nonDomainTypeNames = nonDomainTypeNames(factory -> {
            factory.setNonDomainExcludedSupertypePackages(null);
            factory.setNonDomainExcludedPackages(null);
        });

        assertThat(nonDomainTypeNames)
            .doesNotContain(PACKAGE + ".generated.GeneratedTable")
            .contains(PACKAGE + ".generated.Keys");
    }

    @Test
    public void filterMatchesPackagesAndSubPackagesOnly() {

        NonDomainClassFilter filter = new NonDomainClassFilter(List.of(), List.of("a.b"));

        assertThat(filter.isExcludedByName("a.b.C")).isTrue();
        assertThat(filter.isExcludedByName("a.b.c.D")).isTrue();
        assertThat(filter.isExcludedByName("a.bc.D")).isFalse();
        assertThat(NonDomainClassFilter.DEFAULT.excludedSupertypePackages()).containsExactly("org.jooq");
        assertThat(NonDomainClassFilter.NONE.isExcludedBySupertype(tests.mirror.nondomainfilter.generated.GeneratedTable.class)).isFalse();
        assertThat(NonDomainClassFilter.DEFAULT.isExcludedBySupertype(tests.mirror.nondomainfilter.generated.GeneratedTable.class)).isTrue();
    }

    private static List<String> nonDomainTypeNames(Consumer<ReflectiveDomainMirrorFactory> configuration) {
        var factory = new ReflectiveDomainMirrorFactory(PACKAGE);
        configuration.accept(factory);
        DomainMirror domainMirror = factory.initializeDomainMirror();
        return domainMirror.getAllDomainTypeMirrors()
            .stream()
            .filter(dtm -> DomainType.NON_DOMAIN.equals(dtm.getDomainType()))
            .map(DomainTypeMirror::getTypeName)
            .filter(name -> name.startsWith(PACKAGE))
            .toList();
    }
}
