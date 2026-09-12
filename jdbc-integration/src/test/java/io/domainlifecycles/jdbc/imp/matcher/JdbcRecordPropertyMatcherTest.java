package io.domainlifecycles.jdbc.imp.matcher;

import io.domainlifecycles.mirror.api.EntityReferenceMirror;
import io.domainlifecycles.mirror.api.FieldMirror;
import io.domainlifecycles.persistence.records.RecordProperty;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JdbcRecordPropertyMatcherTest {

    private final JdbcRecordPropertyMatcher matcher = new JdbcRecordPropertyMatcher();

    @Test
    void matchesPropertyIgnoringCaseAndUnderscores() {
        var recordProperty = new RecordProperty("concurrencyVersion", "PARENT", Long.class, false, false);
        var fieldMirror = mock(FieldMirror.class);
        when(fieldMirror.getName()).thenReturn("concurrencyVersion");

        assertThat(matcher.matchProperty(recordProperty, fieldMirror)).isTrue();
    }

    @Test
    void doesNotMatchDifferentFieldName() {
        var recordProperty = new RecordProperty("name", "PARENT", String.class, false, false);
        var fieldMirror = mock(FieldMirror.class);
        when(fieldMirror.getName()).thenReturn("description");

        assertThat(matcher.matchProperty(recordProperty, fieldMirror)).isFalse();
    }

    @Test
    void matchesValueObjectPathByConcatenatedFieldNames() {
        var recordProperty = new RecordProperty("addressStreet", "PARENT", String.class, false, false);
        var addressField = mock(FieldMirror.class);
        when(addressField.getName()).thenReturn("address");
        var streetField = mock(FieldMirror.class);
        when(streetField.getName()).thenReturn("street");

        assertThat(matcher.matchValueObjectPath(recordProperty, List.of(addressField, streetField))).isTrue();
    }

    @Test
    void matchesForwardReferenceByStrippingIdSuffix() {
        var recordProperty = new RecordProperty("parentId", "CHILD", Long.class, false, true);
        var entityReferenceMirror = mock(EntityReferenceMirror.class);
        when(entityReferenceMirror.getName()).thenReturn("parent");

        assertThat(matcher.matchForwardReference(recordProperty, entityReferenceMirror)).isTrue();
    }
}
