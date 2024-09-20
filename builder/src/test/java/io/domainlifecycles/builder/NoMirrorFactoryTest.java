package io.domainlifecycles.builder;

import io.domainlifecycles.builder.exception.DLCBuilderException;
import io.domainlifecycles.builder.helper.TestValueObject;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NoMirrorFactoryTest {

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("some.wrong.package"));
    }

    @Test
    public void testNoDomainMirrorFail(){
        var testBuilder = preInitializedLombokBuilder();
        assertThatThrownBy(() -> new InnerClassDomainObjectBuilder<>(testBuilder))
            .isInstanceOf(DLCBuilderException.class)
            .hasMessageContaining("DomainTypeMirror for 'io.domainlifecycles.builder.helper.TestValueObject' not found!");
    }

    private TestValueObject.TestValueObjectBuilder preInitializedLombokBuilder(){
        return TestValueObject.builder()
            .first("1")
            .second(1L);
    }
}
