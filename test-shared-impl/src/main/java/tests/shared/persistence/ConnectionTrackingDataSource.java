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

package tests.shared.persistence;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Wraps a {@link DataSource}, counting the connections obtained from it and not closed yet, and recording the SQL
 * of every statement prepared on them - to verify that persistence code hands back every connection it obtains, so
 * that a connection pool can never run dry.
 *
 * @author Mario Herb
 */
public final class ConnectionTrackingDataSource implements DataSource {

    private final DataSource target;

    private final AtomicInteger openConnections = new AtomicInteger();

    private final List<String> preparedStatements = new CopyOnWriteArrayList<>();

    public ConnectionTrackingDataSource(DataSource target) {
        this.target = target;
    }

    /**
     * @return the number of connections obtained and not closed yet
     */
    public int openConnections() {
        return openConnections.get();
    }

    /**
     * @return the SQL of the statements prepared since the last {@link #clearPreparedStatements()}
     */
    public List<String> preparedStatements() {
        return List.copyOf(preparedStatements);
    }

    public void clearPreparedStatements() {
        preparedStatements.clear();
    }

    /**
     * @param table the unqualified table name
     * @return the number of SELECTs prepared since the last {@link #clearPreparedStatements()} reading the table
     */
    public long selectsFrom(String table) {
        var fromTable = Pattern.compile(
            "\\bFROM\\s+(\"?\\w+\"?\\.)?\"?" + table.toUpperCase(Locale.ROOT) + "\"?(\\s|$)");
        return preparedStatements.stream()
            .map(sql -> sql.toUpperCase(Locale.ROOT).trim())
            .filter(sql -> sql.startsWith("SELECT"))
            .filter(sql -> fromTable.matcher(sql).find())
            .count();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return tracked(target.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return tracked(target.getConnection(username, password));
    }

    private Connection tracked(Connection connection) {
        openConnections.incrementAndGet();
        var closed = new AtomicBoolean();
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class<?>[]{Connection.class},
            (proxy, method, args) -> {
                if ("close".equals(method.getName()) && closed.compareAndSet(false, true)) {
                    openConnections.decrementAndGet();
                } else if (method.getName().startsWith("prepare") && args != null && args[0] instanceof String sql) {
                    preparedStatements.add(sql);
                }
                return invoke(connection, method, args);
            });
    }

    private static Object invoke(Object target, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return target.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        target.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        target.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return target.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return target.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return target.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return target.isWrapperFor(iface);
    }
}
