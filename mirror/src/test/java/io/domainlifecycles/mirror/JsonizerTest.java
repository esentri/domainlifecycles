package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainModelFactory;
import io.domainlifecycles.mirror.serialize.api.JacksonDomainSerializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class JsonizerTest {

    @BeforeAll
    public static void init() {
        ReflectiveDomainModelFactory factory = new ReflectiveDomainModelFactory("tests");
        Domain.initialize(factory);
    }

    @Test
    public void testJsonize() {
        var serializer = new JacksonDomainSerializer(true);
        var result = serializer.serialize(Domain.getDomainModel());
        System.out.println("Serialized: "+result);
        var init = serializer.deserialize(result);
        assertThat(init.boundedContextMirrors()).isEqualTo(Domain.getDomainModel().boundedContextMirrors());
        assertThat(init.allTypeMirrors().size()).isEqualTo(Domain.getDomainModel().allTypeMirrors().size());
        for (String key : init.allTypeMirrors().keySet()) {
            //System.out.println(key);
            assertThat(Domain.getDomainModel().allTypeMirrors().containsKey(key)).isTrue();
            assertThat(init.allTypeMirrors().get(key)).isEqualTo(
                Domain.getDomainModel().allTypeMirrors().get(key));
        }
        assertThat(init).isEqualTo(Domain.getDomainModel());
        var result2 = serializer.serialize(init);
        assertThat(result).isEqualTo(result2);
    }
}
