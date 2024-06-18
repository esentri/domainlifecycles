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

package io.domainlifecycles.springboot2.persistence.bestellung;

import io.domainlifecycles.jooq.imp.JooqAggregateFetcher;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.test.springboot2.Sequences;
import io.domainlifecycles.test.springboot2.Tables;
import io.domainlifecycles.test.springboot2.tables.records.AktionsCodeRecord;
import io.domainlifecycles.test.springboot2.tables.records.BestellKommentarRecord;
import io.domainlifecycles.test.springboot2.tables.records.BestellPositionRecord;
import io.domainlifecycles.test.springboot2.tables.records.BestellStatusRecord;
import io.domainlifecycles.test.springboot2.tables.records.BestellungRecord;
import io.domainlifecycles.test.springboot2.tables.records.LieferadresseRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import tests.shared.persistence.domain.bestellung.bv2.AktionsCode;
import tests.shared.persistence.domain.bestellung.bv2.BestellKommentar;
import tests.shared.persistence.domain.bestellung.bv2.BestellKommentarId;
import tests.shared.persistence.domain.bestellung.bv2.BestellPosition;
import tests.shared.persistence.domain.bestellung.bv2.BestellPositionId;
import tests.shared.persistence.domain.bestellung.bv2.BestellStatus;
import tests.shared.persistence.domain.bestellung.bv2.BestellStatusCodeEnum;
import tests.shared.persistence.domain.bestellung.bv2.BestellStatusId;
import tests.shared.persistence.domain.bestellung.bv2.Bestellung;
import tests.shared.persistence.domain.bestellung.bv2.BestellungId;
import tests.shared.persistence.domain.bestellung.bv2.Lieferadresse;
import tests.shared.persistence.domain.bestellung.bv2.LieferadresseId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BestellungRepository extends JooqAggregateRepository<Bestellung, BestellungId> {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BestellungRepository.class);
    private final JooqDomainPersistenceProvider jooqDomainPersistenceProvider;


    public BestellungRepository(DSLContext dslContext,
                                PersistenceEventPublisher persistenceEventPublisher,
                                JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            Bestellung.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
        this.jooqDomainPersistenceProvider = jooqDomainPersistenceProvider;
    }

    public Optional<Bestellung> findBestellungById(BestellungId id) {
        return getFetcher().fetchDeep(id).resultValue();
    }

    public List<Bestellung> findAllBestellungen() {
        List<Bestellung> result = dslContext.select()
            .from(Tables.BESTELLUNG)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(Tables.BESTELLUNG)).resultValue().get()).collect(Collectors.toList());
        return result;
    }

    public List<Bestellung> findBestellungenPaged(int offset, int pageSize) {
        List<Bestellung> result = dslContext.select()
            .from(Tables.BESTELLUNG)
            .orderBy(Tables.BESTELLUNG.ID)
            .offset(offset)
            .limit(pageSize)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(Tables.BESTELLUNG)).resultValue().get()).collect(Collectors.toList());
        return result;
    }

    public List<Bestellung> findByStatusCode(BestellStatusCodeEnum statusCode) {
        List<Bestellung> result = dslContext.select()
            .from(Tables.BESTELLUNG)
            .join(Tables.BESTELL_STATUS)
            .on(Tables.BESTELL_STATUS.STATUS_CODE.equal(statusCode.name()))
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(Tables.BESTELLUNG)).resultValue().get()).collect(Collectors.toList());
        return result;
    }

    //Achtung aus Sicht fachlich/inhaltlich korrekter Domänenlogik macht diese Methode keinen Sinn
    //Es geht lediglich um die Subquery Demonstration
    public Optional<Bestellung> findWithSubquery(BestellungId id) {
        var fetcher = new JooqAggregateFetcher<Bestellung, BestellungId>(Bestellung.class, dslContext, jooqDomainPersistenceProvider);

        //Wir registrieren einen RecordProvider um nur noch Bestellpositionen mit ArtikelId 1 zu fetchen
        // Per FK Auto Fetch würden normalerweise alle Positionen zu einer Bestellung gefetcht
        fetcher.withRecordProvider(
            new RecordProvider<BestellPositionRecord, BestellungRecord>() {
                @Override
                public Collection<BestellPositionRecord> provideCollection(BestellungRecord parentRecord) {
                    return dslContext.select()
                        .from(Tables.BESTELL_POSITION)
                        .where(Tables.BESTELL_POSITION.BESTELLUNG_ID.equal(parentRecord.getId())
                            .and(Tables.BESTELL_POSITION.ARTIKEL_ID.equal(1l)))
                        .fetch()
                        .into(Tables.BESTELL_POSITION);
                }
            },
            Bestellung.class,
            BestellPosition.class,
            List.of("bestellPositionen"));
        return fetcher.fetchDeep(id).resultValue();
    }

    /**
     * This method ist optimized in the way, that only one select statement is issued to the database for fetching all records
     * instead of the default and fetching several subselects.
     * @param offset
     * @param pageSize
     * @return
     */
    public Stream<Bestellung> findBestellungenOptimized(int offset, int pageSize) {
        var fetcher = new JooqAggregateFetcher<Bestellung, BestellungId>(Bestellung.class, dslContext, jooqDomainPersistenceProvider);

        io.domainlifecycles.test.springboot2.tables.Bestellung b = Tables.BESTELLUNG.as("b");

        var joinedRecords = dslContext.select()
                .from(
                    dslContext.select()
                    .from(Tables.BESTELLUNG)
                    .orderBy(Tables.BESTELLUNG.ID)
                    .offset(offset)
                    .limit(pageSize)
                        .asTable("b")
                )
                .join(Tables.LIEFERADRESSE)
                .on(b.LIEFERADRESSE_ID.eq(Tables.LIEFERADRESSE.ID))
                .join(Tables.BESTELL_STATUS)
                .on(b.ID.eq(Tables.BESTELL_STATUS.BESTELLUNG_ID))
                .leftJoin(Tables.BESTELL_POSITION)
                .on(b.ID.eq(Tables.BESTELL_POSITION.BESTELLUNG_ID))
                .leftJoin(Tables.BESTELL_KOMMENTAR)
                .on(b.ID.eq(Tables.BESTELL_KOMMENTAR.BESTELLUNG_ID))
                .leftJoin(Tables.AKTIONS_CODE)
                .on(Tables.AKTIONS_CODE.CONTAINER_ID.eq(b.ID));

        var records = dslContext.fetch(joinedRecords);

        var lieferadresseRecords = records.into(Tables.LIEFERADRESSE).stream().filter(r -> r.getId()!=null).collect(Collectors.toSet());
        var bestellungenRecords = records.into(b).stream().filter(r -> r.getId()!=null).collect(Collectors.toSet());
        var bestellPositionenRecords = records.into(Tables.BESTELL_POSITION).stream().filter(r -> r.getId()!=null).collect(Collectors.toSet());
        var bestellKommentareRecords = records.into(Tables.BESTELL_KOMMENTAR).stream().filter(r -> r.getId()!=null).collect(Collectors.toSet());
        var aktionsCodesRecords = records.into(Tables.AKTIONS_CODE).stream().filter(r -> r.getId()!=null).collect(Collectors.toSet());
        var statusRecords = records.into(Tables.BESTELL_STATUS).stream().filter(r -> r.getId()!=null).collect(Collectors.toSet());

        fetcher.withRecordProvider(
            new RecordProvider<BestellPositionRecord, BestellungRecord>() {
                @Override
                public Collection<BestellPositionRecord> provideCollection(BestellungRecord parentRecord) {
                    return bestellPositionenRecords
                        .stream()
                        .filter(p -> p.getBestellungId().equals(parentRecord.getId()))
                        .collect(Collectors.toList());
                }
            },
            Bestellung.class,
            BestellPosition.class,
            List.of("bestellPositionen"))
        .withRecordProvider(new RecordProvider<LieferadresseRecord, BestellungRecord>() {
                                      @Override
                                      public LieferadresseRecord provide(BestellungRecord parentRecord) {
                                          return lieferadresseRecords
                                              .stream()
                                              .filter(l -> l.getId().equals(parentRecord.getLieferadresseId()))
                                              .findFirst().orElse(null);
                                      }
                                  },
            Bestellung.class,
            Lieferadresse.class,
            List.of("lieferadresse"))
        .withRecordProvider(new RecordProvider<BestellKommentarRecord, BestellungRecord>() {
                                      @Override
                                      public Collection<BestellKommentarRecord> provideCollection(BestellungRecord parentRecord) {
                                          return bestellKommentareRecords.stream()
                                              .filter(k -> k.getBestellungId().equals(parentRecord.getId()))
                                              .collect(Collectors.toList());
                                      }
                                  },
            Bestellung.class,
            BestellKommentar.class,
            List.of("bestellKommentare"))
        .withRecordProvider(new RecordProvider<BestellStatusRecord, BestellungRecord>() {
                                          @Override
                                          public BestellStatusRecord provide(BestellungRecord parentRecord) {
                                              return statusRecords
                                                  .stream()
                                                  .filter(s -> s.getBestellungId().equals(parentRecord.getId()))
                                                  .findFirst().orElse(null);
                                          }
                                      },
            Bestellung.class,
            BestellStatus.class,
                List.of("bestellStatus"))
        .withRecordProvider(new RecordProvider<AktionsCodeRecord, BestellungRecord>() {
                                @Override
                                public List<AktionsCodeRecord> provideCollection(BestellungRecord parentRecord) {
                                    return aktionsCodesRecords
                                        .stream()
                                        .filter(ac -> ac.getContainerId().equals(parentRecord.getId()))
                                        .collect(Collectors.toList());
                                }
                            },
            Bestellung.class,
            AktionsCode.class,
            List.of("aktionsCodes"))
        ;
        var bestellungen = bestellungenRecords.stream().map(br->fetcher.fetchDeep(br).resultValue().get());

        return bestellungen;
    }

    public BestellungId newBestellungId() {
        return new BestellungId(dslContext.nextval(Sequences.BESTELLUNG_ID_SEQ));
    }

    public BestellPositionId newBestellPositionId() {
        return new BestellPositionId(dslContext.nextval(Sequences.BESTELL_POSITION_ID_SEQ));
    }

    public BestellKommentarId newBestellKommentarId() {
        return new BestellKommentarId(dslContext.nextval(Sequences.BESTELL_KOMMENTAR_ID_SEQ));
    }

    public BestellStatusId newBestellStatusId() {
        return new BestellStatusId(dslContext.nextval(Sequences.BESTELL_STATUS_ID_SEQ));
    }

    public LieferadresseId newLieferadresseId() {
        return new LieferadresseId(dslContext.nextval(Sequences.LIEFERADRESSE_ID_SEQ));
    }


}
