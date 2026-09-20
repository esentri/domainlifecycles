package io.domainlifecycles.jdbc.persistence.mapper.inheritance;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.jdbc.persistence.PhysicalNames;
import tests.shared.persistence.domain.inheritance.Bike;
import tests.shared.persistence.domain.inheritance.Car;
import tests.shared.persistence.domain.inheritance.Vehicle;
import tests.shared.persistence.domain.inheritance.VehicleId;

/**
 * VEHICLE is a single-table-inheritance table shared by {@link Bike} and {@link Car}, dispatched by a "type"
 * discriminator column holding the concrete class' simple name - something auto-mapping (which expects one
 * record shape per domain type) cannot express.
 */
public class VehicleJdbcRecordMapper extends AbstractRecordMapper<JdbcRecord, Vehicle, Vehicle> {

    @Override
    public DomainObjectBuilder<Vehicle> recordToDomainObjectBuilder(JdbcRecord record) {
        if (record == null) {
            return null;
        }
        VehicleId id = new VehicleId((Long) record.get(PhysicalNames.name("ID")));
        Integer lengthCm = (Integer) record.get(PhysicalNames.name("LENGTH_CM"));
        Long concurrencyVersion = (Long) record.get(PhysicalNames.name("CONCURRENCY_VERSION"));
        String type = (String) record.get(PhysicalNames.name("TYPE"));
        if (Bike.class.getSimpleName().equals(type)) {
            Long gears = (Long) record.get(PhysicalNames.name("GEARS"));
            return new InnerClassDomainObjectBuilder<>(Bike.builder()
                .setId(id)
                .setGears(gears.intValue())
                .setLengthCm(lengthCm)
                .setConcurrencyVersion(concurrencyVersion));
        }
        if (Car.class.getSimpleName().equals(type)) {
            return new InnerClassDomainObjectBuilder<>(Car.builder()
                .setId(id)
                .setBrand(Car.Brand.valueOf((String) record.get(PhysicalNames.name("BRAND"))))
                .setLengthCm(lengthCm)
                .setConcurrencyVersion(concurrencyVersion));
        }
        throw new IllegalStateException("Vehicles are only Cars or Bikes!");
    }

    @Override
    public JdbcRecord from(Vehicle vehicle, Vehicle root) {
        JdbcRecord record = new JdbcRecord(PhysicalNames.name("VEHICLE"));
        record.set(PhysicalNames.name("ID"), vehicle.getId().value());
        record.set(PhysicalNames.name("CONCURRENCY_VERSION"), vehicle.concurrencyVersion());
        record.set(PhysicalNames.name("LENGTH_CM"), vehicle.getLengthCm());
        if (vehicle instanceof Bike bike) {
            record.set(PhysicalNames.name("GEARS"), (long) bike.getGears());
            record.set(PhysicalNames.name("TYPE"), Bike.class.getSimpleName());
        } else if (vehicle instanceof Car car) {
            record.set(PhysicalNames.name("BRAND"), car.getBrand().name());
            record.set(PhysicalNames.name("TYPE"), Car.class.getSimpleName());
        } else {
            throw new IllegalStateException("Vehicles are only Cars or Bikes!");
        }
        return record;
    }

    @Override
    public Class<Vehicle> domainObjectType() {
        return Vehicle.class;
    }

    @Override
    public Class<JdbcRecord> recordType() {
        return JdbcRecord.class;
    }
}
