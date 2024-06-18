/*
 *
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

package io.domainlifecycles.jooq.persistence.mapper.inheritanceextended;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilder;
import io.domainlifecycles.persistence.mapping.AbstractRecordMapper;
import io.domainlifecycles.test.Tables;
import io.domainlifecycles.test.tables.records.VehicleExtendedRecord;
import tests.shared.persistence.domain.inheritanceExtended.BikeWithComponents;
import tests.shared.persistence.domain.inheritanceExtended.CarWithEngine;
import tests.shared.persistence.domain.inheritanceExtended.VehicleExtended;
import tests.shared.persistence.domain.inheritanceExtended.VehicleExtendedId;

public class VehicleExtendedRecordMapper extends AbstractRecordMapper<VehicleExtendedRecord, VehicleExtended, VehicleExtended> {

    @Override
    public DomainObjectBuilder<VehicleExtended> recordToDomainObjectBuilder(VehicleExtendedRecord record) {
        if (record == null) {
            return null;
        }
        VehicleExtendedRecord vehicleRecord = record.into(Tables.VEHICLE_EXTENDED);
        if (BikeWithComponents.class.getSimpleName().equals(vehicleRecord.getType())) {
            return new InnerClassDomainObjectBuilder(BikeWithComponents.builder()
                .setId(new VehicleExtendedId(vehicleRecord.getId()))
                .setGears(vehicleRecord.getGears().intValue())
                .setLengthCm(vehicleRecord.getLengthCm())
                .setConcurrencyVersion(vehicleRecord.getConcurrencyVersion()));
        }
        if (CarWithEngine.class.getSimpleName().equals(vehicleRecord.getType())) {
            return new InnerClassDomainObjectBuilder(CarWithEngine.builder()
                .setId(new VehicleExtendedId(vehicleRecord.getId()))
                .setBrand(CarWithEngine.Brand.valueOf(vehicleRecord.getBrand()))
                .setLengthCm(vehicleRecord.getLengthCm())
                .setConcurrencyVersion(vehicleRecord.getConcurrencyVersion()));
        }
        throw new IllegalStateException("VehiclesExtended are only CarWithEngine or BikeWithComponents!");

    }

    @Override
    public VehicleExtendedRecord from(VehicleExtended p, VehicleExtended root) {
        if (p instanceof BikeWithComponents) {
            BikeWithComponents bike = (BikeWithComponents) p;
            VehicleExtendedRecord record = new VehicleExtendedRecord();
            record.setId(bike.getId().value());
            record.setConcurrencyVersion(bike.concurrencyVersion());
            record.setLengthCm(bike.getLengthCm());
            record.setGears(Long.valueOf(bike.getGears()));
            record.setType(BikeWithComponents.class.getSimpleName());
            return record;
        }
        if (p instanceof CarWithEngine) {
            CarWithEngine car = (CarWithEngine) p;
            VehicleExtendedRecord record = new VehicleExtendedRecord();
            record.setId(car.getId().value());
            record.setConcurrencyVersion(car.concurrencyVersion());
            record.setLengthCm(car.getLengthCm());
            record.setBrand(car.getBrand().name());
            record.setType(CarWithEngine.class.getSimpleName());
            return record;
        }

        throw new IllegalStateException("VehiclesExtended are only CarWithEngine or BikeWithComponents!");
    }

    @Override
    public Class<VehicleExtended> domainObjectType() {
        return VehicleExtended.class;
    }

    @Override
    public Class<VehicleExtendedRecord> recordType() {
        return VehicleExtendedRecord.class;
    }
}
