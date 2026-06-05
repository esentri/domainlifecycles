package io.domainlifecycles.jackson3;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jackson3.module.DlcJacksonModule;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitive;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;


public class VoAggregatePrimitiveJacksonTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoAggregatePrimitiveJacksonTest.class);

    private final ObjectMapper objectMapper;

    public VoAggregatePrimitiveJacksonTest() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
        var entityIdentityProvider = new EntityIdentityProvider() {

            @Override
            public Identity<?> provideFor(String entityTypeName) {
                return null;
            }
        };

        this.objectMapper = JsonMapper.builder()
            .addModule(new DlcJacksonModule(
                new InnerClassDomainObjectBuilderProvider(),
                entityIdentityProvider))
            .build();
    }

    @Test
    public void testJacksonMappingEmpty() throws Throwable {
        VoAggregatePrimitive r = TestDataGenerator.buildVoPrimitiveEmpty();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r);
        log.info("JSON = " + json);

        VoAggregatePrimitive read = objectMapper.readValue(json, VoAggregatePrimitive.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringFieldsOfTypes(UUID.class)
            .withStrictTypeChecking()
            .isEqualTo(r);
    }

    @Test
    public void testJacksonMappingComplete() throws Throwable {
        VoAggregatePrimitive r = TestDataGenerator.buildVoPrimitiveComplete();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r);
        log.info("JSON = " + json);

        VoAggregatePrimitive read = objectMapper.readValue(json, VoAggregatePrimitive.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringFieldsOfTypes(UUID.class)
            .withStrictTypeChecking()
            .isEqualTo(r);
    }


}
