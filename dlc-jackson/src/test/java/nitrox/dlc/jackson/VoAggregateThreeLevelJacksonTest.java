/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package nitrox.dlc.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import nitrox.dlc.domain.types.Identity;
import nitrox.dlc.jackson.module.DlcJacksonModule;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.reflect.ReflectiveDomainMirrorFactory;
import nitrox.dlc.persistence.provider.EntityIdentityProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevel;
import tests.shared.persistence.domain.multilevelvo.VoAggregateThreeLevelId;

import java.util.UUID;


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
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
        var entityIdentityProvider = new EntityIdentityProvider() {

            @Override
            public Identity<?> provideFor(String entityTypeName) {
                if(entityTypeName.equals(VoAggregateThreeLevel.class.getName())){
                    return new VoAggregateThreeLevelId(1L);
                }
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
        Assertions.assertThat(read.getIdentifikationsNummer()).isEqualTo(new VoAggregateThreeLevelId(  1L));
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
        Assertions.assertThat(read.getIdentifikationsNummer()).isEqualTo(new VoAggregateThreeLevelId(  1L));
    }
}
