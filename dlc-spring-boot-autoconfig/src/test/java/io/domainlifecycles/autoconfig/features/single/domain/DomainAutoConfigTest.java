package io.domainlifecycles.autoconfig.features.single.domain;

import io.domainlifecycles.mirror.api.Domain;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test"})
public class DomainAutoConfigTest {

    @Test
    void testDomainPresentAndInitialized() {
        assertThat(Domain.isInitialized()).isTrue();
        assertThat(Domain.getDomainMirror()).isNotNull();
    }
}
