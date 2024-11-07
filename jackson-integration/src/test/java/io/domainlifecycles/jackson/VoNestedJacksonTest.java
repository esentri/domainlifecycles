package io.domainlifecycles.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.valueobjectsNested.NestedEnumSingleValued;
import tests.shared.persistence.domain.valueobjectsNested.NestedId;
import tests.shared.persistence.domain.valueobjectsNested.NestedSimpleVo;
import tests.shared.persistence.domain.valueobjectsNested.VoAggregateNested;

public class VoNestedJacksonTest {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoNestedJacksonTest.class);


    private final ObjectMapper objectMapper;

    public VoNestedJacksonTest() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
        var entityIdentityProvider = new EntityIdentityProvider() {

            @Override
            public Identity<?> provideFor(String entityTypeName) {
                return null;
            }
        };
        objectMapper.registerModule(new DlcJacksonModule(
                new InnerClassDomainObjectBuilderProvider(),
                entityIdentityProvider
            )
        );
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new ParameterNamesModule());
    }

    @Test
    public void testNestedEnumSingleValued() throws Throwable {
        VoAggregateNested r = TestDataGenerator.buildVoNestedComplete();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r.getNestedEnumSingleValued());
        log.info("JSON = " + json);

        NestedEnumSingleValued read = objectMapper.readValue(json, NestedEnumSingleValued.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .withStrictTypeChecking()
            .isEqualTo(r.getNestedEnumSingleValued());
    }

    @Test
    public void testNestedSimpleVoPrimitive() throws Throwable {
        VoAggregateNested r = TestDataGenerator.buildVoNestedComplete();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r.getNestedSimpleVo());
        log.info("JSON = " + json);

        NestedSimpleVo read = objectMapper.readValue(json, NestedSimpleVo.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .withStrictTypeChecking()
            .isEqualTo(r.getNestedSimpleVo());
    }

    @Test
    public void testNestedIdentity() throws Throwable {
        VoAggregateNested r = TestDataGenerator.buildVoNestedComplete();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r.getNestedId());
        log.info("JSON = " + json);

        NestedId read = objectMapper.readValue(json, NestedId.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .withStrictTypeChecking()
            .isEqualTo(r.getNestedId());
    }

    @Test
    public void testJacksonMappingEmpty() throws Throwable {
        VoAggregateNested r = TestDataGenerator.buildVoNestedEmpty();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r);
        log.info("JSON = " + json);

        VoAggregateNested read = objectMapper.readValue(json, VoAggregateNested.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .withStrictTypeChecking()
            .isEqualTo(r);
    }

    @Test
    public void testJacksonMappingComplete() throws Throwable {
        VoAggregateNested r = TestDataGenerator.buildVoNestedComplete();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r);
        log.info("JSON = " + json);
        Assertions.assertThat(json).contains("\"nestedEnumSingleValuedList\" : [ \"B\" ]");
        Assertions.assertThat(json).contains("\"nestedSimpleVoList\" : [ 90 ]");
        Assertions.assertThat(json).contains("\"nestedIdList\" : [ 550 ]");

        VoAggregateNested read = objectMapper.readValue(json, VoAggregateNested.class);
        log.info("Read = " + read);


        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .withStrictTypeChecking()
            .isEqualTo(r);
    }

}
