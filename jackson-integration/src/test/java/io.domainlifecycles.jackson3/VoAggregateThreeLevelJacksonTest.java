package io.domainlifecycles.jackson3;

import tools.jackson.databind.ObjectMapper;
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
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevel;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevelId;

import java.util.UUID;
import tools.jackson.databind.json.JsonMapper;


/**
 * Several edge case aspects are tested here A) We have a three entity to VO 1-1
 * Relationship where on vo (ThreeLevelVO) contains 3 nested layers of VOs B)
 * The ThreeLevelVO itself contains a primitive value (long) but is optional
 * itself, so the Database can contain null C) if the complete ThreeLevelVo is
 * null all corresponding database columns have to be null D) We have a Primary
 * Key whose property on Builder Level has a name that is not "id"
 */

public class VoAggregateThreeLevelJacksonTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoAggregateThreeLevelJacksonTest.class);

    private final ObjectMapper objectMapper;

    public VoAggregateThreeLevelJacksonTest() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
        var entityIdentityProvider = new EntityIdentityProvider() {

            @Override
            public Identity<?> provideFor(String entityTypeName) {
                if (entityTypeName.equals(VoAggregateThreeLevel.class.getName())) {
                    return new VoAggregateThreeLevelId(1L);
                }
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
    public void testJacksonMappingMin() throws Throwable {
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMin();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r);
        log.info("JSON = " + json);

        VoAggregateThreeLevel read = objectMapper.readValue(json, VoAggregateThreeLevel.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringFieldsOfTypes(UUID.class)
            .withStrictTypeChecking()
            .isEqualTo(r);
    }

    @Test
    public void testJacksonMappingMiddle() throws Throwable {
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMiddle();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r);
        log.info("JSON = " + json);

        VoAggregateThreeLevel read = objectMapper.readValue(json, VoAggregateThreeLevel.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringFieldsOfTypes(UUID.class)
            .withStrictTypeChecking()
            .isEqualTo(r);
    }

    @Test
    public void testJacksonMappingMax() throws Throwable {
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMax();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(r);
        log.info("JSON = " + json);

        VoAggregateThreeLevel read = objectMapper.readValue(json, VoAggregateThreeLevel.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringFieldsOfTypes(UUID.class)
            .withStrictTypeChecking()
            .isEqualTo(r);
    }

    @Test
    public void testJacksonMappingProvideIdMin() throws Throwable {

        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMin();
        String json = "{\n" +
            "}";
        log.info("JSON = " + json);

        VoAggregateThreeLevel read = objectMapper.readValue(json, VoAggregateThreeLevel.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringFieldsOfTypes(UUID.class)
            .ignoringFields("identifikationsNummer")
            .withStrictTypeChecking()
            .isEqualTo(r);
        Assertions.assertThat(read.getIdentifikationsNummer()).isEqualTo(new VoAggregateThreeLevelId(1L));
    }

    @Test
    public void testJacksonMappingProvideIdMiddle() throws Throwable {
        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMiddle();

        String json = """
            {
              "info" : "TestMiddle",
              "threeLevelVo" : {
                "ownValue" : 5,
                "levelTwoA" : {
                  "levelThreeA" : {
                    "text" : "2_A_3_A_test",
                    "another" : "2_A_3_A_another"
                  }
                }
              }
            }""";
        log.info("JSON = " + json);

        VoAggregateThreeLevel read = objectMapper.readValue(json, VoAggregateThreeLevel.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringFieldsOfTypes(UUID.class)
            .ignoringFields("identifikationsNummer")
            .withStrictTypeChecking()
            .isEqualTo(r);
        Assertions.assertThat(read.getIdentifikationsNummer()).isEqualTo(new VoAggregateThreeLevelId(1L));
    }

    @Test
    public void testJacksonMappingProvideIdMax() throws Throwable {

        VoAggregateThreeLevel r = TestDataGenerator.buildVoAggregateThreeLevelMax();
        String json = """
            {
              "info" : "TestMax",
              "threeLevelVo" : {
                "ownValue" : 5,
                "levelTwoA" : {
                  "levelThreeA" : {
                    "text" : "2_A_3_A_test",
                    "another" : "2_A_3_A_another"
                  },
                  "levelThreeB" : {
                    "text" : "2_A_3_B_test",
                    "another" : "2_A_3_B_another"
                  }
                },
                "levelTwoB" : {
                  "levelThreeA" : {
                    "text" : "2_B_3_A_test",
                    "another" : "2_B_3_A_another"
                  },
                  "levelThreeB" : {
                    "text" : "2_B_3_B_test",
                    "another" : "2_B_3_B_another"
                  }
                }
              },
              "myComplexVo" : {
                "valueA" : "myComplex_ValueA",
                "valueB" : "myComplex_ValueB"
              }
            }""";
        log.info("JSON = " + json);

        VoAggregateThreeLevel read = objectMapper.readValue(json, VoAggregateThreeLevel.class);
        log.info("Read = " + read);
        Assertions.assertThat(read).usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringFieldsOfTypes(UUID.class)
            .ignoringFields("identifikationsNummer")
            .withStrictTypeChecking()
            .isEqualTo(r);
        Assertions.assertThat(read.getIdentifikationsNummer()).isEqualTo(new VoAggregateThreeLevelId(1L));
    }
}
