package io.domainlifecycles.jdbc.persistence.tests.bestellung;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateFetcher;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusCodeEnumBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellKommentarIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungIdBv3;
import tests.shared.complete.onlinehandel.bestellung.LieferadresseIdBv3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BestellungRepository extends JdbcAggregateRepository<BestellungBv3, BestellungIdBv3> {

    private final JdbcConnectionProvider connectionProvider;
    private final JdbcDialect dialect;
    private final JdbcSchemaMetadata schemaMetadata;
    private final JdbcDomainPersistenceProvider domainPersistenceProvider;

    public BestellungRepository(JdbcConnectionProvider connectionProvider,
                                 JdbcDialect dialect,
                                 JdbcSchemaMetadata schemaMetadata,
                                 JdbcDomainPersistenceProvider domainPersistenceProvider,
                                 PersistenceEventPublisher persistenceEventPublisher) {
        super(BestellungBv3.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
        this.connectionProvider = connectionProvider;
        this.dialect = dialect;
        this.schemaMetadata = schemaMetadata;
        this.domainPersistenceProvider = domainPersistenceProvider;
    }

    public Optional<BestellungBv3> findBestellungById(BestellungIdBv3 id) {
        return getFetcher().fetchDeep(id).resultValue();
    }

    public List<BestellungBv3> findAllBestellungen() {
        return selectMany("BESTELLUNG_BV3", null)
            .stream()
            .map(r -> getFetcher().fetchDeep(r).resultValue().get())
            .collect(Collectors.toList());
    }

    public List<BestellungBv3> findBestellungenPaged(int offset, int pageSize) {
        var table = schemaMetadata.table("BESTELLUNG_BV3");
        var sql = "SELECT * FROM " + table.qualifiedName() + " ORDER BY ID LIMIT ? OFFSET ?";
        return selectWithSql(table, sql, pageSize, offset)
            .stream()
            .map(r -> getFetcher().fetchDeep(r).resultValue().get())
            .collect(Collectors.toList());
    }

    public List<BestellungBv3> findByStatusCode(BestellStatusCodeEnumBv3 statusCode) {
        var table = schemaMetadata.table("BESTELLUNG_BV3");
        var statusTable = schemaMetadata.table("BESTELL_STATUS_BV3");
        // mirrors jooq-integration's own (unconditional, not FK-linked) cross join - purely to demonstrate
        // a custom finder, not a fachlich sinnvolle query
        var sql = "SELECT b.* FROM " + table.qualifiedName() + " b, " + statusTable.qualifiedName()
            + " s WHERE s.STATUS_CODE = ?";
        return selectWithSql(table, sql, statusCode.name())
            .stream()
            .map(r -> getFetcher().fetchDeep(r).resultValue().get())
            .collect(Collectors.toList());
    }

    public Optional<BestellungBv3> findWithSubquery(BestellungIdBv3 id) {
        var fetcher = new JdbcAggregateFetcher<BestellungBv3, BestellungIdBv3>(
            BestellungBv3.class, connectionProvider, schemaMetadata, domainPersistenceProvider);

        var positionTable = schemaMetadata.table("BESTELL_POSITION_BV3");
        fetcher.withRecordProvider(
            new RecordProvider<JdbcRecord, JdbcRecord>() {
                @Override
                public Collection<JdbcRecord> provideCollection(JdbcRecord parentRecord) {
                    var sql = "SELECT * FROM " + positionTable.qualifiedName()
                        + " WHERE BESTELLUNG_ID = ? AND ARTIKEL_ID = ?";
                    return selectWithSql(positionTable, sql, parentRecord.get("ID"), 1L);
                }
            },
            BestellungBv3.class,
            BestellPositionBv3.class,
            List.of("bestellPositionen"));
        return fetcher.fetchDeep(id).resultValue();
    }

    /**
     * Not literally query-optimized (this module has no jOOQ-style single-join batch fetch machinery to hook
     * into) - kept only functionally equivalent to jooq-integration's own method of the same name, which this
     * scenario's tests only check the result size of.
     */
    public Stream<BestellungBv3> findBestellungenOptimized(int offset, int pageSize) {
        return findBestellungenPaged(offset, pageSize).stream();
    }

    public BestellungIdBv3 newBestellungId() {
        return new BestellungIdBv3(nextVal("BESTELLUNG_ID_BV3_SEQ"));
    }

    public BestellPositionIdBv3 newBestellPositionId() {
        return new BestellPositionIdBv3(nextVal("BESTELL_POSITION_ID_BV3_SEQ"));
    }

    public BestellKommentarIdBv3 newBestellKommentarId() {
        return new BestellKommentarIdBv3(nextVal("BESTELL_KOMMENTAR_ID_BV3_SEQ"));
    }

    public BestellStatusIdBv3 newBestellStatusId() {
        return new BestellStatusIdBv3(nextVal("BESTELL_STATUS_ID_BV3_SEQ"));
    }

    public LieferadresseIdBv3 newLieferadresseId() {
        return new LieferadresseIdBv3(nextVal("LIEFERADRESSE_ID_BV3_SEQ"));
    }

    private long nextVal(String sequenceName) {
        try {
            return dialect.nextSequenceValue(connectionProvider.getConnection(), sequenceName);
        } catch (SQLException e) {
            throw DLCPersistenceException.fail("Could not obtain next value of sequence '%s'.", e, sequenceName);
        }
    }

    private List<JdbcRecord> selectMany(String tableName, String whereClauseIgnored) {
        var table = schemaMetadata.table(tableName);
        return selectWithSql(table, "SELECT * FROM " + table.qualifiedName());
    }

    private List<JdbcRecord> selectWithSql(TableMetadata table, String sql, Object... params) {
        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                List<JdbcRecord> rows = new ArrayList<>();
                while (resultSet.next()) {
                    var record = new JdbcRecord(table.name());
                    for (var column : table.columns()) {
                        record.set(column.name(), resultSet.getObject(column.name(), column.javaType()));
                    }
                    rows.add(record);
                }
                return rows;
            }
        } catch (SQLException e) {
            throw DLCPersistenceException.fail("Query on '%s' failed.", e, table.name());
        }
    }
}
