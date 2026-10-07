package io.domainlifecycles.jdbc.persistence.tests.inheritanceextended;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.inheritanceExtended.BikeWithComponents;
import tests.shared.persistence.domain.inheritanceExtended.CarWithEngine;
import tests.shared.persistence.domain.inheritanceExtended.VehicleExtended;
import tests.shared.persistence.domain.inheritanceExtended.VehicleExtendedId;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Same single-table-inheritance discriminator situation as {@code VehicleRepository} - see {@code
 * VehicleExtendedJdbcRecordMapper}. {@code findAll}/{@code findAllCars}/{@code findAllBikes} have no equivalent
 * on {@link JdbcAggregateRepository}, so - mirroring jooq-integration's own {@code VehicleExtendedRepository} -
 * they are implemented here directly against the schema metadata and connection, then delegate each row to the
 * inherited fetcher.
 */
public class VehicleExtendedRepository extends JdbcAggregateRepository<VehicleExtended, VehicleExtendedId> {

    private final JdbcConnectionProvider connectionProvider;
    private final JdbcSchemaMetadata schemaMetadata;

    public VehicleExtendedRepository(JdbcDomainPersistenceProvider domainPersistenceProvider,
                                      PersistenceEventPublisher persistenceEventPublisher) {
        super(VehicleExtended.class, domainPersistenceProvider, persistenceEventPublisher);
        this.connectionProvider = domainPersistenceProvider.connectionProvider;
        this.schemaMetadata = domainPersistenceProvider.schemaMetadata;
    }

    public Stream<VehicleExtended> findAll() {
        var table = schemaMetadata.table("VEHICLE_EXTENDED");
        return selectAll(table, null).stream()
            .map(r -> getFetcher().fetchDeep(r).resultValue().get());
    }

    public Stream<CarWithEngine> findAllCars() {
        var table = schemaMetadata.table("VEHICLE_EXTENDED");
        return selectAll(table, CarWithEngine.class.getSimpleName()).stream()
            .map(r -> (CarWithEngine) getFetcher().fetchDeep(r).resultValue().get());
    }

    public Stream<BikeWithComponents> findAllBikes() {
        var table = schemaMetadata.table("VEHICLE_EXTENDED");
        return selectAll(table, BikeWithComponents.class.getSimpleName()).stream()
            .map(r -> (BikeWithComponents) getFetcher().fetchDeep(r).resultValue().get());
    }

    private List<JdbcRecord> selectAll(TableMetadata table, String typeDiscriminator) {
        var sql = "SELECT * FROM " + table.qualifiedName()
            + (typeDiscriminator == null ? "" : " WHERE TYPE = ?");
        try (PreparedStatement statement = connectionProvider.getConnection().prepareStatement(sql)) {
            if (typeDiscriminator != null) {
                statement.setObject(1, typeDiscriminator);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                List<JdbcRecord> rows = new ArrayList<>();
                while (resultSet.next()) {
                    JdbcRecord record = new JdbcRecord(table.name());
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
