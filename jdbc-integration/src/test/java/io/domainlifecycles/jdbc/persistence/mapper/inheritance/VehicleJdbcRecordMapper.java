package io.domainlifecycles.jdbc.persistence.mapper.inheritance;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
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
        VehicleId id = new VehicleId((Long) record.get("ID"));
        Integer lengthCm = (Integer) record.get("LENGTH_CM");
        Long concurrencyVersion = (Long) record.get("CONCURRENCY_VERSION");
        String type = (String) record.get("TYPE");
        if (Bike.class.getSimpleName().equals(type)) {
            Long gears = (Long) record.get("GEARS");
            return new InnerClassDomainObjectBuilder<>(Bike.builder()
                .setId(id)
                .setGears(gears.intValue())
                .setLengthCm(lengthCm)
                .setConcurrencyVersion(concurrencyVersion));
        }
        if (Car.class.getSimpleName().equals(type)) {
            return new InnerClassDomainObjectBuilder<>(Car.builder()
                .setId(id)
                .setBrand(Car.Brand.valueOf((String) record.get("BRAND")))
                .setLengthCm(lengthCm)
                .setConcurrencyVersion(concurrencyVersion));
        }
        throw new IllegalStateException("Vehicles are only Cars or Bikes!");
    }

    @Override
    public JdbcRecord from(Vehicle vehicle, Vehicle root) {
        JdbcRecord record = new JdbcRecord("VEHICLE");
        record.set("ID", vehicle.getId().value());
        record.set("CONCURRENCY_VERSION", vehicle.concurrencyVersion());
        record.set("LENGTH_CM", vehicle.getLengthCm());
        if (vehicle instanceof Bike bike) {
            record.set("GEARS", (long) bike.getGears());
            record.set("TYPE", Bike.class.getSimpleName());
        } else if (vehicle instanceof Car car) {
            record.set("BRAND", car.getBrand().name());
            record.set("TYPE", Car.class.getSimpleName());
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
