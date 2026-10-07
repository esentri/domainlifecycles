package io.domainlifecycles.jooq.imp.provider;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.IgnoredFieldProvider;
import io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror;
import io.domainlifecycles.test.jooq.tables.records.EntityIdEnumListEnumListRecord;
import io.domainlifecycles.test.jooq.tables.records.EntityIdEnumListIdListRecord;
import io.domainlifecycles.test.jooq.tables.records.EntityIdEnumListRecord;
import io.domainlifecycles.test.jooq.tables.records.RootIdEnumListEnumListRecord;
import io.domainlifecycles.test.jooq.tables.records.RootIdEnumListIdListRecord;
import io.domainlifecycles.test.jooq.tables.records.RootIdEnumListRecord;
import io.domainlifecycles.test.jooq.tables.records.RootIdEnumListUuidIdListRecord;
import org.jooq.UpdatableRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.oneToManyIdentityEnum.EntityIdEnumList;
import tests.shared.persistence.domain.oneToManyIdentityEnum.RootIdEnumList;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Fields excluded from auto mapping via {@code withIgnoredDomainObjectFields} (typically because a custom record
 * mapper handles them, e.g. an enum set serialized into a single column of the containing record) must not
 * require a value object record of their own - neither for List&lt;Enum&gt;/List&lt;Identity&gt; fields, nor for
 * list fields nested within an ignored value object field.
 */
class JooqDomainPersistenceProviderIgnoredFieldsTest {

    /**
     * Only the records of the aggregate root and its entity - none of the value object records of the
     * list-valued fields.
     */
    private static final Set<Class<? extends UpdatableRecord<?>>> ENTITY_RECORDS = Set.of(
        RootIdEnumListRecord.class,
        EntityIdEnumListRecord.class
    );

    /**
     * The value object records of the scalar list fields (List&lt;Enum&gt;/List&lt;Identity&gt;), but none of
     * the ones of the value object fields' nested lists.
     */
    private static final Set<Class<? extends UpdatableRecord<?>>> SCALAR_LIST_RECORDS = Set.of(
        RootIdEnumListEnumListRecord.class,
        RootIdEnumListIdListRecord.class,
        RootIdEnumListUuidIdListRecord.class,
        EntityIdEnumListEnumListRecord.class,
        EntityIdEnumListIdListRecord.class
    );

    @BeforeAll
    static void initDomain() {
        //scoped to a single, self-contained domain package: every entity/aggregate root found by the
        //provider must resolve to a record
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared.persistence.domain.oneToManyIdentityEnum"));
    }

    @Test
    void ignoredScalarListAndValueObjectFieldsDoNotRequireRecords() {
        //when
        var provider = new JooqDomainPersistenceProvider(config(
            ENTITY_RECORDS,
            ignoring("enumList", "idList", "uuidIdList", "valueWithListsList", "valueWithLists")));

        //then
        assertThat(provider.persistenceMirror.getEntityRecordMirror(RootIdEnumList.class.getName())
            .valueObjectRecords()).isEmpty();
        assertThat(provider.persistenceMirror.getEntityRecordMirror(EntityIdEnumList.class.getName())
            .valueObjectRecords()).isEmpty();
    }

    @Test
    void ignoringAValueObjectFieldAlsoSkipsTheListFieldsNestedWithinIt() {
        //given
        var recordClasses = new HashSet<>(ENTITY_RECORDS);
        recordClasses.addAll(SCALAR_LIST_RECORDS);

        //when
        var provider = new JooqDomainPersistenceProvider(config(
            recordClasses,
            ignoring("valueWithListsList", "valueWithLists")));

        //then
        assertThat(provider.persistenceMirror.getEntityRecordMirror(RootIdEnumList.class.getName())
            .valueObjectRecords())
            .extracting(ValueObjectRecordMirror::completePath)
            .containsExactlyInAnyOrder("enumList", "idList", "uuidIdList");
        assertThat(provider.persistenceMirror.getEntityRecordMirror(EntityIdEnumList.class.getName())
            .valueObjectRecords())
            .extracting(ValueObjectRecordMirror::completePath)
            .containsExactlyInAnyOrder("enumList", "idList");
    }

    @Test
    void notIgnoredScalarListFieldStillRequiresARecord() {
        //given - enumList is not ignored, but there is no record for it
        var configuration = config(
            ENTITY_RECORDS,
            ignoring("idList", "uuidIdList", "valueWithListsList", "valueWithLists"));

        //when / then
        assertThatThrownBy(() -> new JooqDomainPersistenceProvider(configuration))
            .isInstanceOf(DLCPersistenceException.class)
            .hasMessageContaining("No value object record type found for composition of");
    }

    private static JooqDomainPersistenceConfiguration config(
        Set<Class<? extends UpdatableRecord<?>>> recordClasses,
        IgnoredFieldProvider ignoredFieldProvider
    ) {
        return JooqDomainPersistenceConfiguration.JooqPersistenceConfigurationBuilder.newConfig()
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .withRecordClassProvider(() -> recordClasses)
            .withIgnoredDomainObjectFields(ignoredFieldProvider)
            .make();
    }

    private static IgnoredFieldProvider ignoring(String... fieldNames) {
        var names = Set.of(fieldNames);
        return (FieldMirror f) -> names.contains(f.getName());
    }
}
