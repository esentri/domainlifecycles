package io.domainlifecycles.mirror.jackson3;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.serialize.DeserializingDomainMirrorFactory;
import org.junit.jupiter.api.Test;
import io.domainlifecycles.mirror.serialize.jackson3.JacksonDomainSerializer;


public class DeserializingDomainMirrorFactoryTest {

    @Test
    void testDeserializeMirrorFromFile() {
        Domain.initialize(new DeserializingDomainMirrorFactory(new JacksonDomainSerializer(true)));
    }
}
