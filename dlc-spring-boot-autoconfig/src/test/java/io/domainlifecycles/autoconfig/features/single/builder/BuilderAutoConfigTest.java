package io.domainlifecycles.autoconfig.features.single.builder;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test"})
public class BuilderAutoConfigTest {

    @Autowired
    DomainObjectBuilderProvider domainObjectBuilderProvider;

    @Test
    void testBuilderProviderPresent() {
        assertThat(domainObjectBuilderProvider).isNotNull();
    }
}
