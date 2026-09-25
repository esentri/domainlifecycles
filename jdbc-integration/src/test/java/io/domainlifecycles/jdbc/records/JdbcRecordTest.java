package io.domainlifecycles.jdbc.records;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcRecordTest {

    @Test
    void storesAndReturnsValuesByColumnName() {
        var record = new JdbcRecord("PARENT");

        record.set("ID", 1L);
        record.set("NAME", "Alice");

        assertThat(record.tableName()).isEqualTo("PARENT");
        assertThat(record.get("ID")).isEqualTo(1L);
        assertThat(record.get("NAME")).isEqualTo("Alice");
        assertThat(record.has("NAME")).isTrue();
        assertThat(record.has("MISSING")).isFalse();
        assertThat(record.get("MISSING")).isNull();
    }

    @Test
    void valuesAreReturnedAsImmutableSnapshot() {
        var record = new JdbcRecord("PARENT");
        record.set("ID", 1L);

        var snapshot = record.values();
        record.set("NAME", "Alice");

        assertThat(snapshot).containsOnlyKeys("ID");
    }

    @Test
    void equalityIsStructuralByTableAndValues() {
        var a = new JdbcRecord("PARENT");
        a.set("ID", 1L);
        a.set("NAME", "Alice");

        var b = new JdbcRecord("PARENT");
        b.set("ID", 1L);
        b.set("NAME", "Alice");

        var differentTable = new JdbcRecord("CHILD");
        differentTable.set("ID", 1L);
        differentTable.set("NAME", "Alice");

        var differentValues = new JdbcRecord("PARENT");
        differentValues.set("ID", 2L);
        differentValues.set("NAME", "Alice");

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(differentTable);
        assertThat(a).isNotEqualTo(differentValues);
    }
}
