package io.domainlifecycles.jooq.persistence.mapper.inheritance;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.VehicleRecord;
import tests.shared.persistence.domain.inheritance.Bike;
import tests.shared.persistence.domain.inheritance.Car;
import tests.shared.persistence.domain.inheritance.Vehicle;
import tests.shared.persistence.domain.inheritance.VehicleId;

public class VehicleRecordMapper extends AbstractRecordMapper<VehicleRecord, Vehicle, Vehicle> {

    @Override
    public DomainObjectBuilder<Vehicle> recordToDomainObjectBuilder(VehicleRecord record) {
        if (record == null) {
            return null;
        }
        VehicleRecord vehicleRecord = record.into(Tables.VEHICLE);
        if (Bike.class.getSimpleName().equals(vehicleRecord.getType())) {
            return new InnerClassDomainObjectBuilder(Bike.builder()
                .setId(new VehicleId(vehicleRecord.getId()))
                .setGears(vehicleRecord.getGears().intValue())
                .setLengthCm(vehicleRecord.getLengthCm())
                .setConcurrencyVersion(vehicleRecord.getConcurrencyVersion()));
        }
        if (Car.class.getSimpleName().equals(vehicleRecord.getType())) {
            return new InnerClassDomainObjectBuilder(Car.builder()
                .setId(new VehicleId(vehicleRecord.getId()))
                .setBrand(Car.Brand.valueOf(vehicleRecord.getBrand()))
                .setLengthCm(vehicleRecord.getLengthCm())
                .setConcurrencyVersion(vehicleRecord.getConcurrencyVersion()));
        }
        throw new IllegalStateException("Vehicles are only Cars or Bikes!");

    }

    @Override
    public VehicleRecord from(Vehicle p, Vehicle root) {
        if (p instanceof Bike) {
            Bike bike = (Bike) p;
            VehicleRecord record = new VehicleRecord();
            record.setId(bike.getId().value());
            record.setConcurrencyVersion(bike.concurrencyVersion());
            record.setLengthCm(bike.getLengthCm());
            record.setGears(Long.valueOf(bike.getGears()));
            record.setType(Bike.class.getSimpleName());
            return record;
        }
        if (p instanceof Car) {
            Car car = (Car) p;
            VehicleRecord record = new VehicleRecord();
            record.setId(car.getId().value());
            record.setConcurrencyVersion(car.concurrencyVersion());
            record.setLengthCm(car.getLengthCm());
            record.setBrand(car.getBrand().name());
            record.setType(Car.class.getSimpleName());
            return record;
        }

        throw new IllegalStateException("Vehicles are only Cars or Bikes!");
    }

    @Override
    public Class<Vehicle> domainObjectType() {
        return Vehicle.class;
    }

    @Override
    public Class<VehicleRecord> recordType() {
        return VehicleRecord.class;
    }
}
