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
import static org.mockito.Mockito.when;

class OracleJdbcDialectTest {

    private final OracleJdbcDialect dialect = new OracleJdbcDialect();

    @Test
    void reportsItsName() {
        assertThat(dialect.name()).isEqualTo("Oracle");
    }

    @Test
    void selectsNextvalFromDualForSequenceName() throws SQLException {
        var connection = mock(Connection.class);
        var statement = mock(Statement.class);
        var resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(42L);

        var value = dialect.nextSequenceValue(connection, "TEST_ROOT_SIMPLE_ID_SEQ");

        assertThat(value).isEqualTo(42L);
        org.mockito.Mockito.verify(statement).executeQuery("SELECT TEST_ROOT_SIMPLE_ID_SEQ.NEXTVAL FROM DUAL");
    }

    @Test
    void failsWhenQueryReturnsNoRow() throws SQLException {
        var connection = mock(Connection.class);
        var statement = mock(Statement.class);
        var resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThatThrownBy(() -> dialect.nextSequenceValue(connection, "MISSING_SEQ"))
            .isInstanceOf(SQLException.class);
    }
}
