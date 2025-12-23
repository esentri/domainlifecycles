package io.domainlifecycles.springboot4.persistence.bestellung;

import io.domainlifecycles.jooq.imp.JooqAggregateFetcher;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.fetcher.RecordProvider;
import io.domainlifecycles.persistence.repository.PersistenceEventPublisher;
import io.domainlifecycles.test.springboot3.Sequences;
import io.domainlifecycles.test.springboot3.tables.records.AktionsCodeBv3Record;
import io.domainlifecycles.test.springboot3.tables.records.BestellKommentarBv3Record;
import io.domainlifecycles.test.springboot3.tables.records.BestellPositionBv3Record;
import io.domainlifecycles.test.springboot3.tables.records.BestellStatusBv3Record;
import io.domainlifecycles.test.springboot3.tables.records.BestellungBv3Record;
import io.domainlifecycles.test.springboot3.tables.records.LieferadresseBv3Record;
import org.jooq.DSLContext;
import tests.shared.complete.onlinehandel.bestellung.AktionsCodeBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellKommentarBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellKommentarIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusCodeEnumBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungIdBv3;
import tests.shared.complete.onlinehandel.bestellung.LieferadresseBv3;
import tests.shared.complete.onlinehandel.bestellung.LieferadresseIdBv3;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.domainlifecycles.test.springboot3.Tables.AKTIONS_CODE_BV3;
import static io.domainlifecycles.test.springboot3.Tables.BESTELLUNG_BV3;
import static io.domainlifecycles.test.springboot3.Tables.BESTELL_KOMMENTAR_BV3;
import static io.domainlifecycles.test.springboot3.Tables.BESTELL_POSITION_BV3;
import static io.domainlifecycles.test.springboot3.Tables.LIEFERADRESSE_BV3;
import static io.domainlifecycles.test.springboot3.tables.BestellStatusBv3.BESTELL_STATUS_BV3;


public class BestellungBv3Repository extends JooqAggregateRepository<BestellungBv3, BestellungIdBv3> {

    private final JooqDomainPersistenceProvider jooqDomainPersistenceProvider;


    public BestellungBv3Repository(DSLContext dslContext,
                                   PersistenceEventPublisher persistenceEventPublisher,
                                   JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            BestellungBv3.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
        this.jooqDomainPersistenceProvider = jooqDomainPersistenceProvider;
    }

    public Optional<BestellungBv3> findBestellungById(BestellungIdBv3 id) {
        return getFetcher().fetchDeep(id).resultValue();
    }

    public List<BestellungBv3> findAllBestellungen() {
        List<BestellungBv3> result = dslContext.select()
            .from(BESTELLUNG_BV3)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(BESTELLUNG_BV3)).resultValue().get()).collect(Collectors.toList());
        return result;
    }

    public List<BestellungBv3> findBestellungenPaged(int offset, int pageSize) {
        List<BestellungBv3> result = dslContext.select()
            .from(BESTELLUNG_BV3)
            .orderBy(BESTELLUNG_BV3.ID)
            .offset(offset)
            .limit(pageSize)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(BESTELLUNG_BV3)).resultValue().get()).collect(Collectors.toList());
        return result;
    }

    public List<BestellungBv3> findByStatusCode(BestellStatusCodeEnumBv3 statusCode) {
        List<BestellungBv3> result = dslContext.select()
            .from(BESTELLUNG_BV3)
            .join(BESTELL_STATUS_BV3)
            .on(BESTELL_STATUS_BV3.STATUS_CODE.equal(statusCode.name()))
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(BESTELLUNG_BV3)).resultValue().get()).collect(Collectors.toList());
        return result;
    }

    //Achtung aus Sicht fachlich/inhaltlich korrekter Domänenlogik macht diese Methode keinen Sinn
    //Es geht lediglich um die Subquery Demonstration
    public Optional<BestellungBv3> findWithSubquery(BestellungIdBv3 id) {
        var fetcher = new JooqAggregateFetcher<BestellungBv3, BestellungIdBv3>(BestellungBv3.class, dslContext,
            jooqDomainPersistenceProvider);

        //Wir registrieren einen RecordProvider um nur noch Bestellpositionen mit ArtikelId 1 zu fetchen
        // Per FK Auto Fetch würden normalerweise alle Positionen zu einer Bestellung gefetcht
        fetcher.withRecordProvider(
            new RecordProvider<BestellPositionBv3Record, BestellungBv3Record>() {
                @Override
                public Collection<BestellPositionBv3Record> provideCollection(BestellungBv3Record parentRecord) {
                    return dslContext.select()
                        .from(BESTELL_POSITION_BV3)
                        .where(BESTELL_POSITION_BV3.BESTELLUNG_ID.equal(parentRecord.getId())
                            .and(BESTELL_POSITION_BV3.ARTIKEL_ID.equal(1l)))
                        .fetch()
                        .into(BESTELL_POSITION_BV3);
                }
            },
            BestellungBv3.class,
            BestellPositionBv3.class,
            List.of("bestellPositionen"));
        return fetcher.fetchDeep(id).resultValue();
    }

    /**
     * This method ist optimized in the way, that only one select statement is issued to the database for fetching
     * all records
     * instead of the default and fetching several subselects.
     *
     * @param offset
     * @param pageSize
     * @return
     */
    public Stream<BestellungBv3> findBestellungenOptimized(int offset, int pageSize) {
        var fetcher = new JooqAggregateFetcher<BestellungBv3, BestellungIdBv3>(BestellungBv3.class, dslContext,
            jooqDomainPersistenceProvider);

        io.domainlifecycles.test.springboot3.tables.BestellungBv3 b = BESTELLUNG_BV3.as("b");

        var joinedRecords = dslContext.select()
            .from(
                dslContext.select()
                    .from(BESTELLUNG_BV3)
                    .orderBy(BESTELLUNG_BV3.ID)
                    .offset(offset)
                    .limit(pageSize)
                    .asTable("b")
            )
            .join(LIEFERADRESSE_BV3)
            .on(b.LIEFERADRESSE_ID.eq(LIEFERADRESSE_BV3.ID))
            .join(BESTELL_STATUS_BV3)
            .on(b.ID.eq(BESTELL_STATUS_BV3.BESTELLUNG_ID))
            .leftJoin(BESTELL_POSITION_BV3)
            .on(b.ID.eq(BESTELL_POSITION_BV3.BESTELLUNG_ID))
            .leftJoin(BESTELL_KOMMENTAR_BV3)
            .on(b.ID.eq(BESTELL_KOMMENTAR_BV3.BESTELLUNG_ID))
            .leftJoin(AKTIONS_CODE_BV3)
            .on(AKTIONS_CODE_BV3.CONTAINER_ID.eq(b.ID));

        var records = dslContext.fetch(joinedRecords);

        var lieferadresseRecords = records.into(LIEFERADRESSE_BV3).stream().filter(r -> r.getId() != null).collect(
            Collectors.toSet());
        var bestellungenRecords = records.into(b).stream().filter(r -> r.getId() != null).collect(Collectors.toSet());
        var bestellPositionenRecords = records.into(BESTELL_POSITION_BV3).stream().filter(
            r -> r.getId() != null).collect(Collectors.toSet());
        var bestellKommentareRecords = records.into(BESTELL_KOMMENTAR_BV3).stream().filter(
            r -> r.getId() != null).collect(Collectors.toSet());
        var aktionsCodesRecords = records.into(AKTIONS_CODE_BV3).stream().filter(r -> r.getId() != null).collect(
            Collectors.toSet());
        var statusRecords = records.into(BESTELL_STATUS_BV3).stream().filter(r -> r.getId() != null).collect(
            Collectors.toSet());

        fetcher.withRecordProvider(
                new RecordProvider<BestellPositionBv3Record, BestellungBv3Record>() {
                    @Override
                    public Collection<BestellPositionBv3Record> provideCollection(BestellungBv3Record parentRecord) {
                        return bestellPositionenRecords
                            .stream()
                            .filter(p -> p.getBestellungId().equals(parentRecord.getId()))
                            .collect(Collectors.toList());
                    }
                },
                BestellungBv3.class,
                BestellPositionBv3.class,
                List.of("bestellPositionen"))
            .withRecordProvider(new RecordProvider<LieferadresseBv3Record, BestellungBv3Record>() {
                                    @Override
                                    public LieferadresseBv3Record provide(BestellungBv3Record parentRecord) {
                                        return lieferadresseRecords
                                            .stream()
                                            .filter(l -> l.getId().equals(parentRecord.getLieferadresseId()))
                                            .findFirst().orElse(null);
                                    }
                                },
                BestellungBv3.class,
                LieferadresseBv3.class,
                List.of("lieferadresse"))
            .withRecordProvider(new RecordProvider<BestellKommentarBv3Record, BestellungBv3Record>() {
                                    @Override
                                    public Collection<BestellKommentarBv3Record> provideCollection(BestellungBv3Record parentRecord) {
                                        return bestellKommentareRecords.stream()
                                            .filter(k -> k.getBestellungId().equals(parentRecord.getId()))
                                            .collect(Collectors.toList());
                                    }
                                },
                BestellungBv3.class,
                BestellKommentarBv3.class,
                List.of("bestellKommentare"))
            .withRecordProvider(new RecordProvider<BestellStatusBv3Record, BestellungBv3Record>() {
                                    @Override
                                    public BestellStatusBv3Record provide(BestellungBv3Record parentRecord) {
                                        return statusRecords
                                            .stream()
                                            .filter(s -> s.getBestellungId().equals(parentRecord.getId()))
                                            .findFirst().orElse(null);
                                    }
                                },
                BestellungBv3.class,
                BestellStatusBv3.class,
                List.of("bestellStatus"))
            .withRecordProvider(new RecordProvider<AktionsCodeBv3Record, BestellungBv3Record>() {
                                    @Override
                                    public List<AktionsCodeBv3Record> provideCollection(BestellungBv3Record parentRecord) {
                                        return aktionsCodesRecords
                                            .stream()
                                            .filter(ac -> ac.getContainerId().equals(parentRecord.getId()))
                                            .collect(Collectors.toList());
                                    }
                                },
                BestellungBv3.class,
                AktionsCodeBv3.class,
                List.of("aktionsCodes"))
        ;
        var bestellungen = bestellungenRecords.stream().map(br -> fetcher.fetchDeep(br).resultValue().get());

        return bestellungen;
    }

    public BestellungIdBv3 newBestellungId() {
        return new BestellungIdBv3(dslContext.nextval(Sequences.BESTELLUNG_ID_BV3_SEQ));
    }

    public BestellPositionIdBv3 newBestellPositionId() {
        return new BestellPositionIdBv3(dslContext.nextval(Sequences.BESTELL_POSITION_ID_BV3_SEQ));
    }

    public BestellKommentarIdBv3 newBestellKommentarId() {
        return new BestellKommentarIdBv3(dslContext.nextval(Sequences.BESTELL_KOMMENTAR_ID_BV3_SEQ));
    }

    public BestellStatusIdBv3 newBestellStatusId() {
        return new BestellStatusIdBv3(dslContext.nextval(Sequences.BESTELL_STATUS_ID_BV3_SEQ));
    }

    public LieferadresseIdBv3 newLieferadresseId() {
        return new LieferadresseIdBv3(dslContext.nextval(Sequences.LIEFERADRESSE_ID_BV3_SEQ));
    }


}
