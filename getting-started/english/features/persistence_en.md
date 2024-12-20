[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Persistence](persistence_en.md)

---

# Persistence
The persistence module of DLC makes it easy to combine the DDD approach with 
a relational database.
Some of the functions it offers are:
- Type-safe Queries based on jOOQ
- Support and abstraction of many database engines using JOOQ
- Simplified aggregate queries (DLC Fetcher)
- Simplified aggregate CRUD support (DLC repositories)
- Object relational auto mapping
- Persistence Action Event hooks
- Full ValueObject support regarding persistence
- Supports Java `final` keywords and Java optionals within persisted structures

## Implementation
**Hinweis:** Damit eine reibungslose Implementation des Repositories möglich ist,
muss das Projekt zuerst einmal kompiliert werden, sodass die entsprechenden JOOQ-Records/-Tabellen erstellt werden.

### Repository anlegen
Wie auch schon bei den anderen Domain-Types, lässt sich ein Repository definieren,
indem man von der entsprechenden Klasse erbt. Ein beispielhaftes Repository mit den grundlegenden 
Operationen für die eingangs verwendete Customer-Klasse, könnte so aussehen:
```
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

Bereits durch diese grundlegende Definition sind simple CRUD-Operationen möglich.

### Eigene Datenbank-Operationen definieren
Komplexere Operationen lassen sich hervorragend durch das Zusammenspiel zwischen DLC und JOOQ definieren.
Hierbei findet oft der `Fetcher` Anwendung, welcher vereinfachtes Laden der Aggregates aus der Datenbank ermöglicht.
Beispielhafte Implementationen können dann so aussehen:

#### Find All
```
public List<Customer> findAllCustomers() {
        List<Customers> result = dslContext.select()
        .from(CUSTOMER)
        .fetch().stream()
        .map(r -> getFetcher().fetchDeep(r.into(CUSTOMER)).resultValue().get()).collect(Collectors.toList());
        return result;
    }
```
`CUSTOMER` ist hierbei die JOOQ Repräsentation der Customer-Tabelle und entsprechende JOOQ-Records der Datenbank.

#### Find Paginated
```
public List<Customer> findCustomersPaginated(int offset, int pageSize) {
    List<Customer> result = dslContext.select()
        .from(Customer)
        .orderBy(CUSTOMER.ID)
        .offset(offset)
        .limit(pageSize)
        .fetch().stream()
        .map(r -> getFetcher().fetchDeep(r.into(CUSTOMER)).resultValue().get()).collect(Collectors.toList());
    return result;
}
```

#### Find Paginated und gefiltert
```
public List<Customer> findCustomersPaginatedAndCustomerFirstNameEqualTo(String firstName, int offset, int pageSize) {
    List<Customer> result = dslContext.select()
        .from(CUSTOMER)
        .where(ORDER.FIRST_NAME.eq(firstName)) 
        .orderBy(CUSTOMER.ID)
        .offset(offset)
        .limit(pageSize)
        .fetch().stream()
        .map(r -> getFetcher().fetchDeep(r.into(CUSTOMER)).resultValue().get()).collect(Collectors.toList());
    return result;
}
```

## Tests

```
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testFindAll() {
        
        
    }

}
```


---

|          **OpenAPI-Extension**           |           **Domain-Events**            |
|:----------------------------------------:|:--------------------------------------:|
| [<< Previous](open_api_extension_en.md)  | [Next >>](domain_events_en.md) |

---

**EN** / [DE](../../german/features/persistence_de.md)
