[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Persistence](persistence_de.md)

---

# Persistence

Das Persistence-Modul von DLC ermöglicht ein vereinfachtes Mapping für in der Datenbank persistierten DomainObjects.
Zu den Funktionen gehören unter anderem:
-   Vereinfachte Aggregate Queries (DLC Fetcher)
-   Vereinfachte Aggregate CRUD Unterstützung (DLC Repositories)
-   Object relationales Auto Mapping
-   Persistenz Action Event Hooks
-   Vollumfänglicher ValueObject support bezüglich Persistenz
-   Unterstützt Java  `final`  Keywords und Java-Optionals innerhalb persistierter Strukturen

## Implementierung
**Hinweis:** Damit eine reibungslose Implementierung des Repositories möglich ist,
muss das Projekt zuerst einmal kompiliert werden, sodass die entsprechenden JOOQ-Records erstellt werden.

### Repository anlegen
Wie auch schon bei den anderen Domain-Types, lässt sich ein Repository definieren,
indem man von der entsprechenden Klasse erbt. Ein beispielhaftes Repository mit den grundlegenden 
Operationen für die eingangs verwendete Customer-Klasse, könnte so aussehen:
```Java
@Component
public class CustomerRepository extends JooqAggregateRepository<Customer, CustomerId> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CustomerRepository.class);
    private final JooqDomainPersistenceProvider jooqDomainPersistenceProvider;
    
    public CustomerRepository(DSLContext dslContext,
                                SpringPersistenceEventPublisher persistenceEventPublisher,
                                JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            Customer.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
        this.jooqDomainPersistenceProvider = jooqDomainPersistenceProvider;
    }
}
```

Bereits durch diese grundlegende Definition sind simple CRUD-Operationen für Aggregates möglich, wobei DLC die Aggregate Struktur auf potentiell mehrere
auf Datenbank Ebene abgebildete Tabellen unterstützt.

### Eigene Datenbank-Operationen definieren
Komplexere Operationen lassen sich hervorragend durch das Zusammenspiel zwischen DLC und JOOQ definieren.
Hierbei findet oft der `Fetcher` Anwendung, welcher vereinfachtes Laden der Aggregates aus der Datenbank ermöglicht.
Beispielhafte Implementierungen können dann so aussehen:

#### Find All
```Java
@Component
public class CustomerRepository extends JooqAggregateRepository<Customer, CustomerId> {
    public List<Customer> findAllCustomers() {
        return dslContext.select()
            .from(CUSTOMER)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(CUSTOMER)).resultValue().get()).collect(Collectors.toList());
    }
}
```
`CUSTOMER` ist hierbei die JOOQ Repräsentation der Customer-Tabelle und entsprechende JOOQ-Records der Datenbank.
Der Fechter übernimmt per `fetchDeep` das Laden weiterer jOOQ-Records aus den Tabellen aus welchen sich das Aggregate zusammensetzt.
Zudem übernimmt der das Mapping in die gewünschte Aggregate-Struktur in der Java-Ebene.

#### Find Paginated
```Java
@Component
public class CustomerRepository extends JooqAggregateRepository<Customer, CustomerId> {
    public List<Customer> findCustomersPaginated(int offset, int pageSize) {
        return dslContext.select()
            .from(Customer)
            .orderBy(CUSTOMER.ID)
            .offset(offset)
            .limit(pageSize)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(CUSTOMER)).resultValue().get()).collect(Collectors.toList());
    }
}
```

#### Find Paginated und gefiltert
```Java
@Component
public class CustomerRepository extends JooqAggregateRepository<Customer, CustomerId> {
    public List<Customer> findCustomersPaginatedAndCustomerFirstNameEqualTo(String firstName, int offset, int pageSize) {
        return dslContext.select()
            .from(CUSTOMER)
            .where(ORDER.FIRST_NAME.eq(firstName))
            .orderBy(CUSTOMER.ID)
            .offset(offset)
            .limit(pageSize)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(CUSTOMER)).resultValue().get()).collect(Collectors.toList());
    }
}
```

---

|             **OpenAPI-Extension**              |            **Domain-Events**             |
|:----------------------------------------------:|:----------------------------------------:|
| [<< Vorherige Seite](open_api_extension_de.md) |  [Nächste Seite >>](domain_events_de.md) |

---

**DE** / [EN](../../english/features/persistence_en.md)
