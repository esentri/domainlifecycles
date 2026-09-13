package io.domainlifecycles.jdbc.persistence.tests.inheritance;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.JdbcAggregateRepository;
import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.jdbc.schema.TableMetadata;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import tests.shared.persistence.domain.inheritance.Bike;
import tests.shared.persistence.domain.inheritance.Car;
import tests.shared.persistence.domain.inheritance.Vehicle;
import tests.shared.persistence.domain.inheritance.VehicleId;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Single-table-inheritance discriminator (see {@code VehicleJdbcRecordMapper}). {@code findAll}/{@code
 * findAllCars}/{@code findAllBikes} have no equivalent on {@link JdbcAggregateRepository}, so - mirroring
 * jooq-integration's own {@code VehicleRepository} - they are implemented here directly against the schema
 * metadata and connection, then delegate each row to the inherited fetcher.
 */
public class VehicleRepository extends JdbcAggregateRepository<Vehicle, VehicleId> {

    private final JdbcConnectionProvider connectionProvider;
    private final JdbcSchemaMetadata schemaMetadata;

    public VehicleRepository(JdbcConnectionProvider connectionProvider,
                              JdbcDialect dialect,
                              JdbcSchemaMetadata schemaMetadata,
                              JdbcDomainPersistenceProvider domainPersistenceProvider,
                              PersistenceEventPublisher persistenceEventPublisher) {
        super(Vehicle.class, connectionProvider, dialect, schemaMetadata, domainPersistenceProvider,
            persistenceEventPublisher);
        this.connectionProvider = connectionProvider;
        this.schemaMetadata = schemaMetadata;
    }

    public Stream<Vehicle> findAll() {
        var table = schemaMetadata.table("VEHICLE");
        return selectAll(table, null).stream()
            .map(r -> getFetcher().fetchDeep(r).resultValue().get());
    }

    public Stream<Car> findAllCars() {
        var table = schemaMetadata.table("VEHICLE");
        return selectAll(table, Car.class.getSimpleName()).stream()
            .map(r -> (Car) getFetcher().fetchDeep(r).resultValue().get());
    }

    public Stream<Bike> findAllBikes() {
        var table = schemaMetadata.table("VEHICLE");
        return selectAll(table, Bike.class.getSimpleName()).stream()
            .map(r -> (Bike) getFetcher().fetchDeep(r).resultValue().get());
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
