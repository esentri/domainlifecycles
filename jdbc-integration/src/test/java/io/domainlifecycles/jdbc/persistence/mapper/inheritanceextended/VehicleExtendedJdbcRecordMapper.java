package io.domainlifecycles.jdbc.persistence.mapper.inheritanceextended;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.jdbc.persistence.PhysicalNames;
import tests.shared.persistence.domain.inheritanceExtended.BikeWithComponents;
import tests.shared.persistence.domain.inheritanceExtended.CarWithEngine;
import tests.shared.persistence.domain.inheritanceExtended.VehicleExtended;
import tests.shared.persistence.domain.inheritanceExtended.VehicleExtendedId;

/**
 * Same single-table-inheritance situation as {@code VehicleJdbcRecordMapper}, one level further: {@code
 * engine}/{@code bikeComponents} are entity/collection references populated by the fetcher, not by this
 * record mapper.
 */
public class VehicleExtendedJdbcRecordMapper extends AbstractRecordMapper<JdbcRecord, VehicleExtended,
    VehicleExtended> {

    @Override
    public DomainObjectBuilder<VehicleExtended> recordToDomainObjectBuilder(JdbcRecord record) {
        if (record == null) {
            return null;
        }
        VehicleExtendedId id = new VehicleExtendedId((Long) record.get(PhysicalNames.name("ID")));
        Integer lengthCm = (Integer) record.get(PhysicalNames.name("LENGTH_CM"));
        Long concurrencyVersion = (Long) record.get(PhysicalNames.name("CONCURRENCY_VERSION"));
        String type = (String) record.get(PhysicalNames.name("TYPE"));
        if (BikeWithComponents.class.getSimpleName().equals(type)) {
            Long gears = (Long) record.get(PhysicalNames.name("GEARS"));
            return new InnerClassDomainObjectBuilder<>(BikeWithComponents.builder()
                .setId(id)
                .setGears(gears.intValue())
                .setLengthCm(lengthCm)
                .setConcurrencyVersion(concurrencyVersion));
        }
        if (CarWithEngine.class.getSimpleName().equals(type)) {
            return new InnerClassDomainObjectBuilder<>(CarWithEngine.builder()
                .setId(id)
                .setBrand(CarWithEngine.Brand.valueOf((String) record.get(PhysicalNames.name("BRAND"))))
                .setLengthCm(lengthCm)
                .setConcurrencyVersion(concurrencyVersion));
        }
        throw new IllegalStateException("VehiclesExtended are only CarWithEngine or BikeWithComponents!");
    }

    @Override
    public JdbcRecord from(VehicleExtended vehicle, VehicleExtended root) {
        JdbcRecord record = new JdbcRecord(PhysicalNames.name("VEHICLE_EXTENDED"));
        record.set(PhysicalNames.name("ID"), vehicle.getId().value());
        record.set(PhysicalNames.name("CONCURRENCY_VERSION"), vehicle.concurrencyVersion());
        record.set(PhysicalNames.name("LENGTH_CM"), vehicle.getLengthCm());
        if (vehicle instanceof BikeWithComponents bike) {
            record.set(PhysicalNames.name("GEARS"), (long) bike.getGears());
            record.set(PhysicalNames.name("TYPE"), BikeWithComponents.class.getSimpleName());
        } else if (vehicle instanceof CarWithEngine car) {
            record.set(PhysicalNames.name("BRAND"), car.getBrand().name());
            record.set(PhysicalNames.name("TYPE"), CarWithEngine.class.getSimpleName());
        } else {
            throw new IllegalStateException("VehiclesExtended are only CarWithEngine or BikeWithComponents!");
        }
        return record;
    }

    @Override
    public Class<VehicleExtended> domainObjectType() {
        return VehicleExtended.class;
    }

    @Override
    public Class<JdbcRecord> recordType() {
        return JdbcRecord.class;
    }
}
