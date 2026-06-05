package io.domainlifecycles.events.serialize.jackson3.test;

import io.domainlifecycles.events.serialize.jackson3.JacksonDomainEventSerializer;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainEventSerializerTest {

    @Test
    public void testSerialize(){
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.events.serialize.jackson3.test"));
        var serialized = new JacksonDomainEventSerializer().serialize(new MyDomainEvent(new MyId(5l), new MyVO("test")));
        assertThat(serialized).isEqualTo("{\"id\":5,\"vo\":\"test\"}");
    }

    @Test
    public void testDeserialize(){
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.events.serialize.jackson3.test"));
        var event = new JacksonDomainEventSerializer().deserialize("{\"id\":5,\"vo\":\"test\"}", MyDomainEvent.class);
        assertThat(event).isEqualTo(new MyDomainEvent(new MyId(5l), new MyVO("test")));
    }
}
