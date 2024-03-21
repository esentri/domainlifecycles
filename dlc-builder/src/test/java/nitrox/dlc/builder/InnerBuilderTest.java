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

package nitrox.dlc.builder;

import nitrox.dlc.builder.innerclass.InnerClassDomainObjectBuilder;
import nitrox.dlc.mirror.api.Domain;
import nitrox.dlc.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.records.RecordTest;
import tests.shared.persistence.domain.records.RecordTestId;
import tests.shared.persistence.domain.records.RecordVo;

import java.util.ArrayList;

public class InnerBuilderTest {


    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "nitrox"));
    }

    @Test
    public void testBuild(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        var built = innerBuilder.build();
        Assertions.assertThat(built).isNotNull();
    }

    @Test
    public void testAddToCollection(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        innerBuilder.addValueToCollection(RecordVo
            .builder()
            .setValue1("v1_3")
            .setValue2(25L)
            .build(), "myVoList");
        RecordTest built = (RecordTest) innerBuilder.build();
        Assertions.assertThat(built.getMyVoList()).hasSize(3);
    }

    @Test
    public void testSetFieldValue(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        innerBuilder.setFieldValue("aaa", "myValue");
        RecordTest built = (RecordTest) innerBuilder.build();
        Assertions.assertThat(built.getMyValue()).isEqualTo("aaa");
    }

    @Test
    public void testNewCollectionForField(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        var newCollection = innerBuilder.newCollectionInstanceForField("myVoList");
        Assertions.assertThat(newCollection).isEmpty();
        Assertions.assertThat(newCollection.getClass()).isEqualTo(ArrayList.class);
    }

    @Test
    public void testGetFieldValue(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        var value = innerBuilder.getFieldValue("myValue");
        Assertions.assertThat(value).isEqualTo("Vlue");
    }

    @Test
    public void testCanInstantiateTrue(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        Assertions.assertThat(innerBuilder.canInstantiateField("myValue")).isTrue();
    }

    @Test
    public void testCanInstantiateFalse(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        Assertions.assertThat(innerBuilder.canInstantiateField("myValue1")).isFalse();
    }

    @Test
    public void testGetBuilderInstance(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        Assertions.assertThat(innerBuilder.getBuilderInstance()).isEqualTo(recordTestBuilder);
    }

    @Test
    public void testGetInstanceType(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        Assertions.assertThat(innerBuilder.instanceType()).isEqualTo(RecordTest.class);
    }

    @Test
    public void testGetPrimaryIdentity(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        Assertions.assertThat((RecordTestId)innerBuilder.getPrimaryIdentity()).isEqualTo(new RecordTestId(5L));
    }

    @Test
    public void testGetPrimaryIdentityField(){
        var recordTestBuilder = preInitializedLombokBuilder();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        Assertions.assertThat(innerBuilder.getPrimaryIdentityFieldName()).isEqualTo("id");
    }

    private RecordTest.RecordTestBuilder preInitializedLombokBuilder(){
        return RecordTest
            .builder()
            .setId(new RecordTestId(5L))
            .setMyValue("Vlue")
            .setMyVo(
                RecordVo
                    .builder()
                    .setValue1("v1")
                    .setValue2(22L)
                    .build()
            )
            .setMyVoList(TestDataGenerator.newArrayListOf(
                RecordVo
                    .builder()
                    .setValue1("v1_1")
                    .setValue2(23L)
                    .build(),
                RecordVo
                    .builder()
                    .setValue1("v1_2")
                    .setValue2(24L)
                    .build()
            ));
    }
}
