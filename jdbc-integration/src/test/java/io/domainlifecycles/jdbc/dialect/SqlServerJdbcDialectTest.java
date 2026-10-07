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

class SqlServerJdbcDialectTest {

    private final SqlServerJdbcDialect dialect = new SqlServerJdbcDialect();

    @Test
    void reportsItsName() {
        assertThat(dialect.name()).isEqualTo("SQL Server");
    }

    @Test
    void selectsNextValueForSequenceName() throws SQLException {
        var connection = mock(Connection.class);
        var statement = mock(Statement.class);
        var resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(7L);

        var value = dialect.nextSequenceValue(connection, "TEST_ROOT_SIMPLE_ID_SEQ");

        assertThat(value).isEqualTo(7L);
        verify(statement).executeQuery("SELECT NEXT VALUE FOR TEST_ROOT_SIMPLE_ID_SEQ");
    }
}
