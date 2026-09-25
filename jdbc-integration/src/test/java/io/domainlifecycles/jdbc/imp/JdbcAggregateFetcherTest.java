package io.domainlifecycles.jdbc.imp;

import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.configuration.JdbcDomainPersistenceConfiguration;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.connection.SingleJdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.H2JdbcDialect;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.mirror.JdbcValueObjectRecordMirrorImpl;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.shared.persistence.domain.oneToMany.TestEntityOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToMany;
import tests.shared.persistence.domain.oneToMany.TestRootOneToManyId;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcAggregateFetcherTest {

    private Connection connection;
    private JdbcConnectionProvider connectionProvider;
    private JdbcSchemaMetadata schemaMetadata;
    private JdbcDomainPersistenceProvider domainPersistenceProvider;

    @BeforeAll
    static void initDomain() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests.shared.persistence.domain.simple",
            "tests.shared.persistence.domain.oneToMany"));
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:h2:mem:jdbc_aggregate_fetcher_test_" + UUID.randomUUID());
        connectionProvider = new SingleJdbcConnectionProvider(connection);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE TEST_ROOT_SIMPLE (
                    ID BIGINT PRIMARY KEY,
                    NAME VARCHAR(255),
                    CONCURRENCY_VERSION BIGINT NOT NULL
                )
                """);
            stmt.execute("""
                CREATE TABLE TEST_ROOT_ONE_TO_MANY (
                    ID BIGINT PRIMARY KEY,
                    CONCURRENCY_VERSION BIGINT NOT NULL,
                    NAME VARCHAR(255)
                )
                """);
            stmt.execute("""
                CREATE TABLE TEST_ENTITY_ONE_TO_MANY (
                    ID BIGINT PRIMARY KEY,
                    CONCURRENCY_VERSION BIGINT NOT NULL,
                    TEST_ROOT_ID BIGINT NOT NULL,
                    NAME VARCHAR(255),
                    CONSTRAINT FK_TEOTM_ROOT FOREIGN KEY (TEST_ROOT_ID) REFERENCES TEST_ROOT_ONE_TO_MANY(ID)
                )
                """);
            stmt.execute("CREATE TABLE WIDGET (ID BIGINT PRIMARY KEY, NAME VARCHAR(255))");
            stmt.execute("""
                CREATE TABLE WIDGET_ITEM (
                    ID BIGINT PRIMARY KEY,
                    WIDGET_ID BIGINT NOT NULL,
                    LABEL VARCHAR(255),
                    CONSTRAINT FK_WIDGET_ITEM_WIDGET FOREIGN KEY (WIDGET_ID) REFERENCES WIDGET(ID)
                )
                """);

            stmt.execute("INSERT INTO TEST_ROOT_SIMPLE VALUES (1, 'Root', 0)");

            stmt.execute("INSERT INTO TEST_ROOT_ONE_TO_MANY VALUES (1, 0, 'Root')");
            stmt.execute("INSERT INTO TEST_ENTITY_ONE_TO_MANY VALUES (10, 0, 1, 'Child A')");
            stmt.execute("INSERT INTO TEST_ENTITY_ONE_TO_MANY VALUES (11, 0, 1, 'Child B')");

            stmt.execute("INSERT INTO WIDGET VALUES (1, 'W1')");
            stmt.execute("INSERT INTO WIDGET_ITEM VALUES (100, 1, 'Item A')");
            stmt.execute("INSERT INTO WIDGET_ITEM VALUES (101, 1, 'Item B')");
        }
        schemaMetadata = JdbcSchemaMetadata.read(connection);
        var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withSchemaMetadata(schemaMetadata)
            .withConnectionProvider(connectionProvider)
            .withDialect(new H2JdbcDialect())
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .make();
        domainPersistenceProvider = new JdbcDomainPersistenceProvider(configuration);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void fetchesRootOnlyAggregateById() {
        var fetcher = new JdbcAggregateFetcher<>(
            TestRootSimple.class, domainPersistenceProvider);

        var result = fetcher.fetchDeep(new TestRootSimpleId(1L));

        assertThat(result.resultValue()).isPresent();
        var root = result.resultValue().get();
        assertThat(root.getName()).isEqualTo("Root");
        assertThat(root.concurrencyVersion()).isEqualTo(0L);
    }

    @Test
    void fetchesUnknownIdAsEmpty() {
        var fetcher = new JdbcAggregateFetcher<>(
            TestRootSimple.class, domainPersistenceProvider);

        var result = fetcher.fetchDeep(new TestRootSimpleId(999L));

        assertThat(result.resultValue()).isEmpty();
    }

    @Test
    void fetchesAggregateRootWithOneToManyChildren() {
        var fetcher = new JdbcAggregateFetcher<>(
            TestRootOneToMany.class, domainPersistenceProvider);

        var result = fetcher.fetchDeep(new TestRootOneToManyId(1L));

        assertThat(result.resultValue()).isPresent();
        var root = result.resultValue().get();
        assertThat(root.getName()).isEqualTo("Root");
        assertThat(root.getTestEntityOneToManyList())
            .extracting(TestEntityOneToMany::getName)
            .containsExactlyInAnyOrder("Child A", "Child B");
    }

    @Test
    void fetchesChildRecordsByForeignKeyForAValueObjectRecordMirror() {
        var fetcher = new JdbcAggregateFetcher<>(
            TestRootSimple.class, domainPersistenceProvider);
        var parentRecord = new JdbcRecord("WIDGET");
        parentRecord.set("ID", 1L);
        parentRecord.set("NAME", "W1");
        var vorm = new JdbcValueObjectRecordMirrorImpl(
            "some.Entity", "some.Vo", "WIDGET_ITEM", List.of("items"), null, List.of("dummy"));

        var children = fetcher.getChildValueObjectRecordCollectionByParentRecord(parentRecord, vorm);

        assertThat(children).hasSize(2);
        assertThat(children).extracting(r -> r.get("LABEL")).containsExactlyInAnyOrder("Item A", "Item B");
    }

    @Test
    void resolvesForeignKeysThroughAQuotingDialect() throws SQLException {
        // the foreign-key resolution behind fetchDeep(...) goes through JdbcRecordMapper.selectByColumn(...),
        // which now asks the dialect to build the SELECT - this proves a custom dialect overriding only
        // quoteIdentifier() still lets the fetcher resolve a real 1:n relation end-to-end
        var quotingDialect = new JdbcDialect() {
            private final H2JdbcDialect delegate = new H2JdbcDialect();

            @Override
            public String name() {
                return "QUOTING-H2";
            }

            @Override
            public long nextSequenceValue(Connection connection, String sequenceName) throws SQLException {
                return delegate.nextSequenceValue(connection, sequenceName);
            }

            @Override
            public String quoteIdentifier(String identifier) {
                return "\"" + identifier + "\"";
            }
        };
        var configuration = JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
            .withSchemaMetadata(schemaMetadata)
            .withConnectionProvider(connectionProvider)
            .withDialect(quotingDialect)
            .withDomainObjectBuilderProvider(new InnerClassDomainObjectBuilderProvider())
            .make();
        var quotingProvider = new JdbcDomainPersistenceProvider(configuration);
        var fetcher = new JdbcAggregateFetcher<>(TestRootOneToMany.class, quotingProvider);

        var result = fetcher.fetchDeep(new TestRootOneToManyId(1L));

        assertThat(result.resultValue()).isPresent();
        assertThat(result.resultValue().get().getTestEntityOneToManyList())
            .extracting(TestEntityOneToMany::getName)
            .containsExactlyInAnyOrder("Child A", "Child B");
    }
}
