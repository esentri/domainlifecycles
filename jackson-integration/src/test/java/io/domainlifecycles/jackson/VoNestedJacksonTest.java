/*
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
