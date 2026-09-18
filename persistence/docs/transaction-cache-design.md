# Transaction-Level Cache für DLC Persistence — Design-Plan

Status: **Entwurf / zur Abstimmung** (noch nicht implementiert)
Betroffene Module: `persistence` (Kern), `jooq-integration`, `jdbc-integration`
Autor: Mario Herb (mit Claude Code erarbeitet)
Branch: `feature/transaction_cache`

## 1. Problemstellung

`DomainStructureAwareRepository.update(root)` und `.deleteById(id)` rufen aktuell **immer** zuerst
`findResultById(id)` auf, bevor sie den eigentlichen Schreibvorgang durchführen:

```java
public A update(A root) {
    var rootCurrentDatabaseState = findResultById((I) domainPersistenceProvider.getId(root));
    ...
    processAggregates(root, rootCurrentDatabaseState);
}
```

`findResultById` löst über `AggregateFetcher.fetchDeep(id)` eine vollständige SELECT-Kaskade aus (jede
Entity-Referenz, jedes record-gemappte Value Object, jede Collection wird einzeln nachgeladen). Dieser
"DB-Ist-Zustand" wird ausschließlich dafür gebraucht, in `PersistenceContext` per Feld-für-Feld-Vergleich
die nötigen INSERT/UPDATE/DELETE-Actions zu erkennen (`PersistenceContext.detectChanges()`).

In den meisten Anwendungsfällen wurde das Aggregat in **derselben Transaktion** bereits durch eine Query
geladen (z. B. ein Finder, eine Detailseite, eine Validierung vor dem Update). Der zweite Fetch in
`update()`/`deleteById()` ist dann redundant.

**Ziel:** Diesen redundanten Fetch vermeiden, indem ein bereits in derselben Transaktion geladener Zustand
wiederverwendet wird — konsistent für jOOQ- und JDBC-basierte Persistence, so weit wie möglich in der
gemeinsamen `persistence`-Implementierung, per Default aktiv, aber abschaltbar.

## 2. Analysebefunde aus dem bestehenden Code (Grundlage der Entwurfsentscheidungen)

- `AggregateFetcher.fetchDeep(id)` und `fetchDeep(record)` laufen in `InternalAggregateFetcher` (Basisklasse
  für `JooqAggregateFetcher` **und** `JdbcAggregateFetcher`) beide auf **eine** gemeinsame Methode
  `fetchDeep(BASE_RECORD_TYPE aggregateRecord)` hinaus. Das ist der einzige Punkt, an dem *jede* Query
  (Repository-`findById` genauso wie beliebige Custom-Finder) ein vollständiges Aggregat aufbaut → idealer,
  einheitlicher Hook-Punkt zum Befüllen eines Caches.
- `PersistenceContext` braucht als „alten Zustand“ ein `FetcherResult<A, RECORD>` (Domänenobjekt-Graph +
  `FetcherContext`).
- Nur **Entities** werden von Anwendungscode mutiert; **Value Objects sind unveränderlich** (DDD-Prinzip,
  von DLC durchgängig vorausgesetzt). Der bereits vorhandene `EntityCloner`
  (`type-utils/.../domain/types/clone/EntityCloner.java`) klont exakt das: Entities tief, VO-Instanzen werden
  **by reference** übernommen (nicht dupliziert).
- Der `FetcherContext` (VO-Instanz → Record mit technischer ID) wird nach dem initialen Fetch nur an
  **einer** Stelle noch gebraucht: `BasePersister.deleteRecordMappedValueObject()` — Löschen eines
  record-gemappten Value Objects braucht die ursprünglich gefetchte Record-Instanz, weil die technische ID
  nie in die Domäne zurückgemappt wird.
- Da VO-Instanzen beim Klonen nicht dupliziert werden, bleibt ein **unverändert wiederverwendeter** (nicht
  geklonter) `FetcherContext` neben einem geklonten Root weiterhin korrekt auflösbar (Identity-basierte
  Lookups treffen weiterhin, weil die VO-Referenzen dieselben sind).
- Die tatsächliche INSERT/UPDATE/DELETE-SQL wird **immer frisch** aus dem Domänenobjekt gemappt
  (`BasePersister.getRecordFromDomainObject` → `RecordMapper.from(...)`), nie aus dem ursprünglich gefetchten
  Record. Die Cache-Idee berührt damit weder SQL-Erzeugung noch Optimistic-Locking-Mechanik — nur die Quelle
  des „alten Zustands“ für den Diff ändert sich.
- `ScalarListElement`-Records werden im `FetcherContext` über eine `Deque` **konsumierend** (`poll()`)
  aufgelöst (siehe `SimpleFetcherContext`) — ein `FetcherContext` darf deshalb nicht für zwei unabhängige
  Operationen wiederverwendet werden.
- `DomainPersistenceConfiguration` (Basisklasse) wird von `JooqDomainPersistenceConfiguration` und
  `JdbcDomainPersistenceConfiguration` erweitert, beide mit eigenem Builder nach identischem Muster
  (typisierte Felder pro Modul, Defaults im `.make()`). Genau dieses Muster wird für die neue Konfiguration
  fortgeführt.
- jOOQ-Version im Projekt: **3.19.29** (verifiziert). Bringt ein natives, Spring-unabhängiges
  Transaktions-SPI mit (`org.jooq.TransactionListener`, siehe Abschnitt 5).
- Für plain JDBC existiert noch keine Spring-Autoconfig (nur jOOQ hat
  `DlcJooqPersistenceAutoConfiguration`). Das bleibt außerhalb des Scopes dieses Features.

## 3. Scope-Entscheidung: Was wird gecacht, wann befüllt, wann konsumiert

### 3.1 Cache-Inhalt

Ein Cache-Eintrag ist ein `FetcherResult<A, RECORD>` mit:
- **geklontem** Aggregate-Root-Graph (nur Entities werden über `EntityCloner` geklont),
- demselben (nicht geklonten) `FetcherContext`-Objekt aus dem ursprünglichen Fetch.

Kein Klonen von `UpdatableRecord`/`JdbcRecord` nötig — das spart modul-spezifischen Klon-Code komplett.

### 3.2 Befüllung — nur bei echten Fetches

Ausschließlich in `InternalAggregateFetcher.fetchDeep(BASE_RECORD_TYPE aggregateRecord)`, direkt nachdem
`domainObjectDeepFetched` aufgebaut wurde: falls Feature aktiv, Klon erzeugen und zusammen mit dem
bestehenden `fetcherContext` unter dem Schlüssel `(rootClass, id)` in den aktuellen Transaktions-Cache
einfügen.

### 3.3 Konsum — „Take“-Semantik (get-and-remove), nur bei Schreiboperationen

`DomainStructureAwareRepository.update()`, `.deleteById()` und `.increaseVersion()` fragen **vor** dem
eigenen `findResultById(id)`-Aufruf den Cache per `take(key)` ab (Treffer entfernt den Eintrag sofort aus
dem Cache). Bei Treffer: kein DB-Fetch. Bei Miss: normaler Fetch wie heute (der seinerseits über den Hook in
3.2 den Cache wieder befüllt).

### 3.4 Bewusst NICHT unterstützt (Scope-Grenze für v1)

- **Kein Refill nach INSERT/UPDATE/DELETE.** Nach einem Schreibvorgang einen korrekten `FetcherContext`
  (inkl. technischer VO-IDs) zu rekonstruieren ist fehleranfällig; ein unvollständiger Eintrag würde später
  bei einem VO-Delete eine Exception auslösen (`deleteRecordMappedValueObject` → `.orElseThrow()`). Ein
  Cache-Miss danach löst stattdessen einfach einen normalen SELECT aus — nie falsches Verhalten.
- **Kein Caching von Lesevorgängen selbst** (`findById`/Custom-Queries liefern weiterhin direkt aus der DB;
  der Cache dient nur dazu, den *zweiten* Fetch vor einem Schreibvorgang zu sparen).

Zusammen garantieren diese Regeln: **Ein Cache-Eintrag stammt immer aus einem echten SELECT und wird nie
zweimal für unabhängige Operationen wiederverwendet** — das schließt auch das Risiko der konsumierenden
`ScalarListElement`-Lookups aus (3.2/Analysebefund).

### 3.5 Dokumentierter Trade-off

Wenn zwischen dem gecachten Fetch und dem späteren Update/Delete **innerhalb derselben Transaktion** das
Aggregat per natives SQL oder an DLC vorbei verändert wurde, sieht der Diff den alten Stand. Das ist exakt
das Verhalten, das man von jedem ORM-„First-Level-Cache“ kennt (z. B. Hibernate Persistence Context) und
muss in der öffentlichen Doku klar benannt werden.

## 4. Neue Abstraktionen (Kernmodul `persistence`, Package `io.domainlifecycles.persistence.cache`)

| Typ | Zweck |
|---|---|
| `AggregateCacheKey` | Record `(Class<? extends AggregateRoot<?>> rootType, Identity<?> id)` |
| `TransactionCache<BASE_RECORD_TYPE>` | `Optional<FetcherResult<?, BASE_RECORD_TYPE>> take(key)`, `void put(key, result)`, `void invalidate(key)` |
| `TransactionCacheProvider<BASE_RECORD_TYPE>` | `Optional<TransactionCache<BASE_RECORD_TYPE>> currentTransactionCache()` — liefert leer, wenn kein Scope aktiv ist → fail-safe (nie fail-open) |
| `NoOpTransactionCacheProvider<BASE_RECORD_TYPE>` | Default, wenn Feature deaktiviert |
| `ThreadBoundTransactionCacheProvider<BASE_RECORD_TYPE>` | Die **eine gemeinsame** konkrete Implementierung, von jOOQ- und JDBC-Modul gleichermaßen genutzt (siehe Abschnitt 5 und 6) |
| `AggregateCacheSupport<BASE_RECORD_TYPE>` (intern) | Kapselt Key-Bildung + `EntityCloner`-Aufruf; genutzt von `InternalAggregateFetcher` (populate) und `DomainStructureAwareRepository` (take/invalidate) |

Änderungen an bestehenden Klassen (ausschließlich im gemeinsamen `persistence`-Modul; jOOQ/JDBC-Fetcher/
-Repository brauchen keine eigene Anpassung an dieser Stelle):

- `InternalAggregateFetcher.fetchDeep(BASE_RECORD_TYPE record)` — Cache-Populate-Hook (3.2).
- `DomainStructureAwareRepository.update/deleteById/increaseVersion` — Cache-Take-Hook (3.3).

## 5. `ThreadBoundTransactionCacheProvider` — Aufbau und Härtung

Basis: `ThreadLocal<Map<AggregateCacheKey, FetcherResult<?, BASE_RECORD_TYPE>>>`, als begrenzter LRU-Cache
implementiert (siehe Abschnitt 7).

### 5.1 API als `AutoCloseable`-Scope

```java
public final class ThreadBoundTransactionCacheProvider<R> implements TransactionCacheProvider<R> {
    public TransactionCacheScope open() { ... } // AutoCloseable
    // scope.close() leert die Map und gibt den ThreadLocal-Slot frei
}
```

Genutzt über try-with-resources durch die in Abschnitt 6 beschriebenen Binder — nie direkt durch
Anwendungscode.

### 5.2 Selbstheilung bei `open()` (zentrale Sicherheitsmaßnahme)

Jeder Scope trägt intern eine monoton steigende „Epoch“-Kennung. Ruft `open()` auf einem Thread auf,
während dort noch (fälschlich, z. B. durch einen zuvor nicht abgeschlossenen Fehlerpfad) ein alter Scope
hinterlegt ist, wird dieser **sofort verworfen** (Map geleert, Epoch erhöht, Warn-Log
`"stale transaction cache scope discarded, N entries"`), **bevor** die neue Transaktion überhaupt liest
oder schreibt.

**Konsequenz:** Ein vergessenes `close()` kann höchstens verschwendeten Speicher zwischen zwei
Transaktionen verursachen — **nie** falsch ausgelieferte Daten in einer späteren, unabhängigen Transaktion
auf demselben (wiederverwendeten) Thread. Das ist bewusst so gewählt, weil reine Dokumentations-Disziplin
("bitte close() aufrufen") als alleinige Absicherung nicht ausreicht.

### 5.3 Read-only-Flows

Reine Lese-Abläufe lösen nie `take()` aus; befüllte Einträge bleiben bis zum Scope-Ende stehen und werden
dort komplett geleert — unabhängig davon, ob sie je konsumiert wurden. Das ist unkritisch, solange der
Scope zuverlässig pro Transaktion geöffnet/geschlossen wird (Abschnitt 6) und durch die Größenbegrenzung
(Abschnitt 7) gedeckelt ist.

### 5.4 Wenn gar kein Scope geöffnet wird

`currentTransactionCache()` liefert dauerhaft `Optional.empty()` → das Feature ist ein reines No-op, nie
ein Risiko. Das betrifft z. B. Nutzung ganz ohne die in Abschnitt 6 beschriebenen Binder.

## 6. Automatische, Spring-unabhängige Bindung an Transaktionsgrenzen

Kernidee: **Nicht** eine externe, gemeinsame Abstraktion raten, wann eine Transaktion beginnt/endet,
sondern **den jeweils nativen Mechanismus der Persistenz-Technologie** nutzen. Beide Module steuern
denselben gemeinsamen `ThreadBoundTransactionCacheProvider` (Abschnitt 5) an — nur der Auslöser ist
modulspezifisch. Es gibt dadurch **keine Code-Duplikation** der Cache-Logik selbst.

### 6.1 jOOQ-Modul — natives `org.jooq.TransactionListener`

jOOQ 3.19 bringt ein eingebautes, Spring-unabhängiges SPI mit (verifiziert im Projekt-Dependency-Jar):

```java
public interface TransactionListener {
    void beginStart(TransactionContext ctx);
    void beginEnd(TransactionContext ctx);
    void commitStart(TransactionContext ctx);
    void commitEnd(TransactionContext ctx);
    void rollbackStart(TransactionContext ctx);
    void rollbackEnd(TransactionContext ctx);
}
```

`TransactionCacheJooqBinder implements TransactionListener` wird automatisch auf der `Configuration`
registriert, wenn `transactionCacheEnabled = true`. Ein eigener **Tiefenzähler** (increment bei
`beginStart`, decrement bei `commitEnd`/`rollbackEnd`) sorgt dafür, dass nur die **äußerste** Transaktion
den Scope öffnet/schließt — verschachtelte `dslContext.transaction(...)`-Aufrufe (Savepoints) lösen kein
erneutes Öffnen/Schließen aus (`Transaction`/`TransactionContext` selbst exponieren kein öffentliches
Nesting-Flag, siehe Prüfung unten — der eigene Zähler ist hier robuster als sich auf interne jOOQ-Details zu
verlassen).

**Wichtig:** Dieser Mechanismus feuert auch dann korrekt, wenn eine Spring-Transaktion die eigentliche
Steuerung übernimmt (jOOQs `SpringTransactionProvider` löst dieselben `TransactionListener`-Events aus). Es
ist **kein** Spring-spezifischer Code nötig, damit die Bindung unter Spring automatisch funktioniert.

### 6.2 JDBC-Modul — `Connection`-Decorator (Proxy)

`java.sql` hat kein eingebautes Transaktions-Listener-SPI. Lösung: ein
`TransactionCacheAwareConnectionProvider implements JdbcConnectionProvider`, der einen beliebigen anderen
`JdbcConnectionProvider` dekoriert und die zurückgegebene `Connection` in einen JDK-Dynamic-Proxy hüllt, der
`commit()` und `rollback()` abfängt:

- Erster `getConnection()`-Aufruf nach geschlossenem Scope → `provider.open()`.
- Abgefangenes `commit()`/`rollback()` → nach Delegation an die echte Methode `provider.close()`.

Kein eigener Tiefenzähler nötig: JDBC-Savepoints (`Connection.setSavepoint()`) rufen niemals `commit()`/
`rollback()` auf der Connection auf — ein echter Aufruf dieser Methoden **ist** bereits zuverlässig die
äußerste Transaktionsgrenze. Funktioniert ebenso automatisch unter Spring (`DataSourceTransactionManager`
ruft letztlich `commit()`/`rollback()` auf genau dieser Connection auf) oder JTA.

### 6.3 Spring als Binder — bewusst nicht Teil von v1

Da beide nativen Mechanismen bereits unter Spring korrekt funktionieren, ist ein
`SpringTransactionCacheProvider` (`TransactionSynchronizationManager`) **nicht mehr notwendig** für den
Regelfall. Denkbarer Spezialfall für eine spätere Ergänzung: JTA-Suspend/Resume-Szenarien, bei denen
Spring transaktionale Suspendierung feiner abbildet als reine Connection-/jOOQ-Events. Zurückgestellt auf
„falls in der Praxis ein konkreter Bedarf auftritt“ — kein Bestandteil des aktuellen Plans.

### 6.4 Kein JDBC-Autoconfig-Modul

Für plain JDBC existiert aktuell keine Spring-Boot-Autoconfig (nur `DlcJooqPersistenceAutoConfiguration`
für jOOQ). Der `TransactionCacheAwareConnectionProvider` wird deshalb manuell verdrahtet (Konstruktor-Param
beim Aufbau des `JdbcConnectionProvider`), nicht automatisch per Spring-Boot-Property. Das Anlegen einer
eigenen JDBC-Autoconfig ist explizit **außerhalb des Scopes** dieses Features (separates Thema).

## 7. Cache-Begrenzung (Memory-Schutz bei Massen-Queries)

Ein Report/Batch, der z. B. 50.000 Aggregate in einer Transaktion liest, darf nicht 50.000 Klone im Speicher
halten.

- `ThreadBoundTransactionCacheProvider` implementiert die Map je Scope als begrenzten LRU-Cache über
  `LinkedHashMap(initialCapacity, loadFactor, accessOrder=true)` + überschriebenes `removeEldestEntry(...)`
  — keine zusätzliche Abhängigkeit nötig.
- Neuer Konfigurationsparameter `transactionCacheMaxSize` (Default z. B. **256**), einstellbar über die
  jeweiligen Builder (`.withTransactionCacheMaxSize(int)`).
- **Korrektheits-Eigenschaft:** Eviction ist immer sicher — ein verdrängter Eintrag führt bei späterem
  `take()` einfach zu einem Miss → normaler DB-Fetch, exakt das heutige Verhalten. Die Grenze ist rein eine
  Performance-/Speicher-Stellschraube, nie eine Korrektheitsfrage.

## 8. Konfigurationsoberfläche

Folgt dem bestehenden Muster (`DomainPersistenceConfiguration`-Basisklasse + typisierte Felder in den
Subklassen, wie z. B. bereits bei `recordPropertyAccessor`):

- `DomainPersistenceConfiguration` (Basis): neues `public final boolean transactionCacheEnabled` (Default
  `true`), als Konstruktorparameter durchgereicht.
- `JooqDomainPersistenceConfiguration` / `JdbcDomainPersistenceConfiguration`: je ein typisiertes
  `public final TransactionCacheProvider<UpdatableRecord<?>>` bzw. `<JdbcRecord>`-Feld +
  Builder-Methoden `.withTransactionCacheProvider(...)`, `.withTransactionCacheEnabled(boolean)` (Default
  `true`), `.withTransactionCacheMaxSize(int)` (Default `256`). Default-Provider = neuer
  `ThreadBoundTransactionCacheProvider<>()`, automatisch an den passenden Binder aus Abschnitt 6 gekoppelt.
- Deaktivieren (`transactionCacheEnabled = false`) → `NoOpTransactionCacheProvider` wird verwendet,
  Verhalten ist danach **exakt** wie vor diesem Feature (kein Binder wird registriert, kein zusätzlicher
  Speicher-Overhead).

## 9. Phasenplan

1. **Phase 1 (Kern + Binder, kein Spring-spezifischer Code):**
   - `persistence`-Modul: Cache-Interfaces, `ThreadBoundTransactionCacheProvider` (inkl. Selbstheilung +
     LRU-Begrenzung), Hooks in `InternalAggregateFetcher`/`DomainStructureAwareRepository`, Konfig-Flags in
     `DomainPersistenceConfiguration` + beiden Buildern.
   - `jooq-integration`: `TransactionCacheJooqBinder` (`TransactionListener`), automatische Registrierung.
   - `jdbc-integration`: `TransactionCacheAwareConnectionProvider` (Connection-Proxy).
   - Funktioniert identisch und automatisch für jOOQ und JDBC, unter Spring wie ohne Spring.
2. **Phase 2 (optional, zurückgestellt):** Spring-`TransactionSynchronizationManager`-Binder nur bei
   konkretem Bedarf (z. B. JTA-Suspend/Resume-Edge-Cases). JDBC-Autoconfig-Modul (separates Thema, nicht
   Teil dieses Features).
3. **Phase 3 (Tests):**
   - Unit-Tests: Take/Invalidate-Semantik, Selbstheilung bei verschachteltem/vergessenem `open()`,
     LRU-Verdrängung.
   - Integrationstests (H2, analog bestehender JDBC-Suiten): Nachweis per Query-Zähler/Statement-
     Interception, dass bei „find → update“ **ein** SELECT statt zwei ausgeführt wird.
   - Regressionstests für VO-Collection-Deletes über den Cache-Pfad (der in Abschnitt 3 identifizierte
     fragile Fall).
   - Nested-Transaction-/Savepoint-Tests für den jOOQ-Binder (Tiefenzähler).
   - Test für Feature-Deaktivierung: Verhalten identisch zum Ist-Zustand vor diesem Feature.

## 10. Offene Punkte / bewusst zurückgestellt

- Spring-`TransactionSynchronizationManager`-Binder (Abschnitt 6.3) — nur bei konkretem Bedarf.
- Eigene JDBC-Spring-Autoconfig (Abschnitt 6.4) — separates Thema.
- Größenbasierte (statt anzahlbasierte) Cache-Begrenzung — als mögliche spätere Verfeinerung, für v1 reicht
  eine einfache anzahlbasierte LRU-Grenze.
