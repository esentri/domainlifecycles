package io.domainlifecycles.mirror.jackson2;

import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.FactoryMirror;
import io.domainlifecycles.mirror.api.MethodMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.serialize.jackson2.JacksonDomainSerializer;
import org.junit.jupiter.api.Test;
import tests.factory.Order;
import tests.factory.OrderFactory;

import static org.assertj.core.api.Assertions.assertThat;

class FactorySerializationTest {

    @Test
    void Should_KeepFactoriesAndFactoryMethods_When_TheMirrorIsSerializedAndDeserialized() {
        var serializer = new JacksonDomainSerializer(false);
        var domainMirror = new ReflectiveDomainMirrorFactory("tests.factory").initializeDomainMirror();

        var json = serializer.serialize(domainMirror);
        var deserialized = serializer.deserialize(json);

        assertThat(deserialized).isEqualTo(domainMirror);
        assertThat(deserialized.getAllFactoryMirrors())
            .extracting(FactoryMirror::getTypeName)
            .containsExactly(OrderFactory.class.getName());
        assertThat(deserialized.getDomainTypeMirror(OrderFactory.class.getName()).orElseThrow().getDomainType())
            .isEqualTo(DomainType.FACTORY);
        assertThat(deserialized.getDomainTypeMirror(OrderFactory.class.getName()).orElseThrow().getFactoryMethods())
            .extracting(MethodMirror::getName)
            .containsExactly("create");
        assertThat(deserialized.getDomainTypeMirror(Order.class.getName()).orElseThrow().getFactoryMethods())
            .extracting(MethodMirror::getName)
            .containsExactly("copy");
        assertThat(json).doesNotContain("factoryMethods");
        assertThat(serializer.serialize(deserialized)).isEqualTo(json);
    }
}
