package io.domainlifecycles.mirror;

import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.serialize.api.JacksonDomainSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class JsonizerTest {

    @BeforeAll
    public static void init() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        Domain.initialize(factory);
    }

    @Test
    public void testJsonize() {
        var serializer = new JacksonDomainSerializer(true);
        var result = serializer.serialize(Domain.getInitializedDomain());
        System.out.println("Serialized: "+result);
        var init = serializer.deserialize(result);
        assertThat(init.boundedContextMirrors()).isEqualTo(Domain.getInitializedDomain().boundedContextMirrors());
        assertThat(init.allTypeMirrors().size()).isEqualTo(Domain.getInitializedDomain().allTypeMirrors().size());
        for (String key : init.allTypeMirrors().keySet()) {
            //System.out.println(key);
            assertThat(Domain.getInitializedDomain().allTypeMirrors().containsKey(key)).isTrue();
            assertThat(init.allTypeMirrors().get(key)).isEqualTo(
                Domain.getInitializedDomain().allTypeMirrors().get(key));
        }
        assertThat(init).isEqualTo(Domain.getInitializedDomain());
        var result2 = serializer.serialize(init);
        assertThat(result).isEqualTo(result2);
    }
}
