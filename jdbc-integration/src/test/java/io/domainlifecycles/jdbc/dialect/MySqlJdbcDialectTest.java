package io.domainlifecycles.jdbc.dialect;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MySqlJdbcDialectTest {

    private final MySqlJdbcDialect dialect = new MySqlJdbcDialect();

    @Test
    void reportsItsName() {
        assertThat(dialect.name()).isEqualTo("MySQL");
    }

    @Test
    void incrementsSequenceTableAndReadsBackLastInsertId() throws SQLException {
        var connection = mock(Connection.class);
        var statement = mock(Statement.class);
        var resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeUpdate(anyString())).thenReturn(1);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(5L);

        var value = dialect.nextSequenceValue(connection, "TEST_ROOT_SIMPLE_ID_SEQ");

        assertThat(value).isEqualTo(5L);
        verify(statement).executeUpdate(
            "UPDATE `TEST_ROOT_SIMPLE_ID_SEQ` SET `next_val` = LAST_INSERT_ID(`next_val` + 1)");
        verify(statement).executeQuery("SELECT LAST_INSERT_ID()");
    }

    @Test
    void failsWhenSequenceTableHasNoRowToIncrement() throws SQLException {
        var connection = mock(Connection.class);
        var statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeUpdate(anyString())).thenReturn(0);

        assertThatThrownBy(() -> dialect.nextSequenceValue(connection, "EMPTY_SEQ"))
            .isInstanceOf(SQLException.class)
            .hasMessageContaining("has no row to increment");
    }
}
