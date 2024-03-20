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
import tests.shared.persistence.domain.valueobjectsPrimitive.VoAggregatePrimitive;

import java.util.UUID;



public class VoAggregatePrimitiveJacksonTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(VoAggregatePrimitiveJacksonTest.class);

    private final ObjectMapper objectMapper;

    public VoAggregatePrimitiveJacksonTest() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
        var entityIdentityProvider = new EntityIdentityProvider() {

            @Override
            public Identity<?> provideFor(String entityTypeName) {
                return null;
            }
        };
        objectMapper.registerModule(
            new DlcJacksonModule(
                new InnerClassDomainObjectBuilderProvider(),
                entityIdentityProvider
            )
        );
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new ParameterNamesModule());
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
