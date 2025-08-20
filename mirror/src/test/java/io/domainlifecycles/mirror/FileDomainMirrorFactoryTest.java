package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.FileDomainMirrorFactory;
import org.junit.jupiter.api.Test;

public class FileDomainMirrorFactoryTest {

    @Test
    void testDeserializeMirrorFromFile() {
        Domain.initialize(new FileDomainMirrorFactory());
    }
}
