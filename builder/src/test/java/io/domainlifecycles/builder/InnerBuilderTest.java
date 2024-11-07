package io.domainlifecycles.builder;

import io.domainlifecycles.builder.exception.DLCBuilderException;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import java.util.HashSet;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.shared.TestDataGenerator;
import tests.shared.persistence.domain.records.RecordTest;
import tests.shared.persistence.domain.records.RecordTestId;
import tests.shared.persistence.domain.records.RecordVo;

import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InnerBuilderTest {

    @BeforeAll
    static void beforeAll() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests", "io.domainlifecycles"));
    }

    @Test
    public void testBuild() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        var built = innerBuilder.build();
        assertThat(built).isNotNull();
    }

    @Test
    public void testAddToCollectionOk() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        innerBuilder.addValueToCollection(RecordVo
            .builder()
            .setValue1("v1_3")
            .setValue2(25L)
            .build(), "myVoList");
        RecordTest built = (RecordTest) innerBuilder.build();
        assertThat(built.getMyVoList()).hasSize(3);
    }

    @Test
    public void testAddToCollectionOkNull() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        recordTestBuilder.setMyVoList(null);
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);

        innerBuilder.addValueToCollection(RecordVo
            .builder()
            .setValue1("v1_3")
            .setValue2(25L)
            .build(), "myVoList");
        RecordTest built = (RecordTest) innerBuilder.build();
        assertThat(built.getMyVoList()).hasSize(1);
    }

    @Test
    public void testAddToCollectionFailNoCollection() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);

        assertThatThrownBy(() ->
            innerBuilder.addValueToCollection(RecordVo
                .builder()
                .setValue1("v1_3")
                .setValue2(25L)
                .build(), "myValue"))
            .isInstanceOf(DLCBuilderException.class);
    }

    @Test
    public void testSetFieldValue() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        innerBuilder.setFieldValue("aaa", "myValue");
        RecordTest built = (RecordTest) innerBuilder.build();
        assertThat(built.getMyValue()).isEqualTo("aaa");
    }

    @Test
    public void testNewCollectionForFieldOkList() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        var newCollection = innerBuilder.newCollectionInstanceForField("myVoList");
        assertThat(newCollection).isEmpty();
        assertThat(newCollection.getClass()).isEqualTo(ArrayList.class);
    }

    @Test
    public void testNewCollectionForFieldOkSet() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        var newCollection = innerBuilder.newCollectionInstanceForField("myVoSet");
        assertThat(newCollection).isEmpty();
        assertThat(newCollection.getClass()).isEqualTo(HashSet.class);
    }

    @Test
    public void testNewCollectionForFieldFailNoContainer() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);

        assertThatThrownBy(() -> innerBuilder.newCollectionInstanceForField("myValue"))
            .isInstanceOf(DLCBuilderException.class);
    }

    @Test
    public void testGetFieldValue() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        var value = innerBuilder.getFieldValue("myValue");
        assertThat(value).isEqualTo("Vlue");
    }

    @Test
    public void testCanInstantiateTrue() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        assertThat(innerBuilder.canInstantiateField("myValue")).isTrue();
    }

    @Test
    public void testCanInstantiateFalse() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        assertThat(innerBuilder.canInstantiateField("myValue1")).isFalse();
    }

    @Test
    public void testGetBuilderInstance() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        assertThat(innerBuilder.getBuilderInstance()).isEqualTo(recordTestBuilder);
    }

    @Test
    public void testGetInstanceType() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        assertThat(innerBuilder.instanceType()).isEqualTo(RecordTest.class);
    }

    @Test
    public void testGetPrimaryIdentity() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        assertThat((RecordTestId) innerBuilder.getPrimaryIdentity()).isEqualTo(new RecordTestId(5L));
    }

    @Test
    public void testGetPrimaryIdentityFieldNameOk() {
        var recordTestBuilder = preInitializedLombokBuilderAggregateRoot();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);
        assertThat(innerBuilder.getPrimaryIdentityFieldName()).isEqualTo("id");
    }

    @Test
    public void testGetPrimaryIdentityFieldNameFailValueObject() {
        var recordTestBuilder = preInitializedLombokBuilderRecordValueObject();
        var innerBuilder = new InnerClassDomainObjectBuilder<>(recordTestBuilder);

        assertThatThrownBy(innerBuilder::getPrimaryIdentityFieldName)
            .isInstanceOf(DLCBuilderException.class)
            .hasMessageContaining("not an Entity and has no primary Identity");
    }

    private RecordTest.RecordTestBuilder preInitializedLombokBuilderAggregateRoot() {
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

    private RecordVo.RecordVoBuilder preInitializedLombokBuilderRecordValueObject() {
        return RecordVo
            .builder()
            .setValue1("1")
            .setValue2(1L);
    }
}
