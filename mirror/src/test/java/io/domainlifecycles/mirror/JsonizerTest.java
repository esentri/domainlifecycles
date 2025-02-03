package io.domainlifecycles.mirror;

import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.EntityMirror;
import io.domainlifecycles.mirror.model.DomainTypeModel;
import io.domainlifecycles.mirror.model.EntityModel;
import io.domainlifecycles.mirror.model.FieldModel;
import io.domainlifecycles.mirror.model.MethodModel;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainModelFactory;
import io.domainlifecycles.mirror.serialize.api.JacksonDomainSerializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

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
        var init = serializer.deserialize(result);
        assertThat(init.boundedContextMirrors()).isEqualTo(Domain.getDomainModel().boundedContextMirrors());
        assertThat(init.allTypeMirrors().size()).isEqualTo(Domain.getDomainModel().allTypeMirrors().size());
        for (String key : init.allTypeMirrors().keySet()) {
            assertThat(Domain.getDomainModel().allTypeMirrors().containsKey(key)).isTrue();
            assertThat(init.allTypeMirrors().get(key)).isEqualTo(
                Domain.getDomainModel().allTypeMirrors().get(key));
        }
        assertThat(init).isEqualTo(Domain.getDomainModel());
        assertThat(init.allTypeMirrors()
                .values()
                .stream()
                .map(dt -> (DomainTypeModel)dt)
                .map(DomainTypeModel::innerDomainModelReference)
                .allMatch(t -> t.equals(init))).isTrue();
        assertThat(init.allTypeMirrors()
                .values()
                .stream()
                .flatMap(dt-> dt.getAllFields().stream())
                .allMatch(t-> ((FieldModel)t).innerDomainModelReference().equals(init))).isTrue();
        assertThat(init.allTypeMirrors()
            .values()
            .stream()
            .flatMap(dt-> dt.getMethods().stream())
            .allMatch(t-> ((MethodModel)t).innerDomainModelReference().equals(init))).isTrue();
        assertThat(init.allTypeMirrors()
            .values()
            .stream()
            .filter(dt -> dt instanceof EntityModel)
            .map(dt ->(EntityModel) dt)
            .map(dt-> (FieldModel)dt.getIdentityField().orElse(null))
            .filter(Objects::nonNull)
            .allMatch(t-> t.innerDomainModelReference().equals(init))).isTrue();
        var result2 = serializer.serialize(init);
        assertThat(result).isEqualTo(result2);
    }


}
