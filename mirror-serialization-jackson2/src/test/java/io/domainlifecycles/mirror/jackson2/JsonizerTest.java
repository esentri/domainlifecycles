package io.domainlifecycles.mirror.jackson2;

import io.domainlifecycles.mirror.api.DomainMirrorFactory;
import io.domainlifecycles.mirror.serialize.jackson2.JacksonDomainSerializer;
import io.domainlifecycles.mirror.model.EntityModel;
import io.domainlifecycles.mirror.model.FieldModel;
import io.domainlifecycles.mirror.model.MethodModel;
import io.domainlifecycles.mirror.model.ProvidedDomain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;


@Slf4j
public class JsonizerTest {

    @Test
    public void testJsonizeWithoutTypeMetaJackson3() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        testJsonize(factory);
    }

    @Test
    public void testJsonizeWithTypeMetaJackson3() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory( "tests");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        testJsonize(factory);
    }

    private void testJsonize(DomainMirrorFactory factory) {
        var serializer = new JacksonDomainSerializer(true);
        var dm = factory.initializeDomainMirror();
        var result = serializer.serialize(dm);
        log.info("Result:"+result);
        var init = serializer.deserialize(result);
        Assertions.assertThat(init.getAllBoundedContextMirrors()).isEqualTo(dm.getAllBoundedContextMirrors());
        Assertions.assertThat(init.getAllDomainTypeMirrors().size()).isEqualTo(dm.getAllDomainTypeMirrors().size());
        Assertions.assertThat(init.getAllDomainTypeMirrors()).isEqualTo(dm.getAllDomainTypeMirrors());

        Assertions.assertThat(init).isEqualTo(dm);
        Assertions.assertThat(init.getAllDomainTypeMirrors()
                .stream()
                .map(t -> (ProvidedDomain) t)
                .allMatch(ProvidedDomain::domainMirrorSet)).isTrue();
        Assertions.assertThat(init.getAllDomainTypeMirrors()
                .stream()
                .flatMap(dt-> dt.getAllFields().stream())
                .allMatch(t-> ((FieldModel)t).domainMirrorSet())).isTrue();
        Assertions.assertThat(init.getAllDomainTypeMirrors()
            .stream()
            .flatMap(dt-> dt.getMethods().stream())
            .allMatch(t-> ((MethodModel)t).domainMirrorSet())).isTrue();
        Assertions.assertThat(init.getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt instanceof EntityModel)
            .map(dt ->(EntityModel) dt)
            .map(dt-> (FieldModel)dt.getIdentityField().orElse(null))
            .filter(Objects::nonNull)
            .allMatch(FieldModel::domainMirrorSet)).isTrue();
        var result2 = serializer.serialize(init);
        Assertions.assertThat(result).isEqualTo(result2);
    }

}
