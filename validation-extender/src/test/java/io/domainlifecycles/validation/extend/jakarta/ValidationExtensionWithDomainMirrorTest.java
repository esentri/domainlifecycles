package io.domainlifecycles.validation.extend.jakarta;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainModelFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import io.domainlifecycles.validation.extend.ValidationDomainClassExtender;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThatNoException;

@Slf4j
class ValidationExtensionWithDomainMirrorTest {

    @Test
    void noExceptionThrown() {

        final String[] packages = {"tests", "io.domainlifecycles.validation.extend"};

        Locale.setDefault(Locale.ENGLISH);

        Domain.initialize(new ReflectiveDomainModelFactory(new TypeMetaResolver(), packages));

        assertThatNoException()
            .isThrownBy(() -> ValidationDomainClassExtender.extend(packages));
    }
}
