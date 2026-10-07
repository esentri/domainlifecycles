package io.domainlifecycles.jdbc.dialect;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostgresJdbcDialectTest {

    private final PostgresJdbcDialect dialect = new PostgresJdbcDialect();

    @Test
    void reportsItsName() {
        assertThat(dialect.name()).isEqualTo("PostgreSQL");
    }

    @Test
    void selectsNextvalForSequenceName() throws SQLException {
        var connection = mock(Connection.class);
        var statement = mock(Statement.class);
        var resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(3L);

        var value = dialect.nextSequenceValue(connection, "TEST_ROOT_SIMPLE_ID_SEQ");

        assertThat(value).isEqualTo(3L);
        verify(statement).executeQuery("SELECT nextval('TEST_ROOT_SIMPLE_ID_SEQ')");
    }
}
