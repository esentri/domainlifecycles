[Getting Started](../index.md) / [Features](../features.md) / [JSON-Mapping](json_mapping.md)

---

# JSON-Mapping
Bietet JSON-Mapping basierend auf [Jackson](https://github.com/FasterXML/jackson), mit DDD spezifischen Anpassungen
und Möglichkeiten zur Konfiguration.

---

## Implementierung
Ein an DDD angepasstes, besseres JSON-Mapping wird bereits sichergestellt durch die 
vorgenommene Konfiguration unter [Projekt erstellen](../configuration.md#JSON-Mapping).

Im weiteren Verlauf kann das Default-Mapping angepasst werden durch das Überschreiben einer oder mehrerer Methoden des
`JacksonMappingCustomizer`, wie folgt:
```
public class CustomerMappingCustomizer extends JacksonMappingCustomizer<Customer>{

    public CustomerMappingCustomizer() {
        super(Customer.class);
    }

    @Override
    public void afterObjectRead(PersistableMappingContext mappingContext, ObjectCodec codec) {
        DomainObjectBuilder<?> b = mappingContext.domainObjectBuilder;
     
    }

}
```

In diesem obigen Beispiel wird die Methode ```afterObjectRead``` von DLC aufgerufen, wenn zuvor ein entsprechendes JSON-Objekt 
geparst und dessen Werte bereits in die DomainObjectBuilder-Instanz übergeben wurde. Nun kann der Builder manipuliert werden,
bevor DLC aus diesem das Ziel-Domänenobjekt erzeugt.

Der `JacksonMappingCustomizer` muss über die folgende Konfiguration aktiviert werden per ````module.registerCustomizer````:

```
@Configuration
public class JacksonConfiguration {

    @Bean
    DlcJacksonModule dlcModuleConfiguration(List<? extends JacksonMappingCustomizer<?>> customizers,
                                            DomainObjectBuilderProvider domainObjectBuilderProvider,
                                            EntityIdentityProvider entityIdentityProvider,
                                            IdentityFactory identityFactory,
                                            ClassProvider classProvider) {
        DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider, entityIdentityProvider, identityFactory, classProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
        return module;
    }
}
```

## Unit-Tests
Für entsprechende Unit-Tests kann sowohl die Serialisierung als auch Deserialisierung getestet werden.
Ein Beispiel zum Testen einer erfolgreichen Serialisierung:

```
public class JacksonTest {

    private final ObjectMapper objectMapper;
    
    @BeforeAll
    public static void init() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        Domain.initialize(factory);
    }

    public JacksonTest() {
        this.objectMapper = new ObjectMapper();
      
        var dlcJacksonModule = new DlcJacksonModule(
            new InnerClassDomainObjectBuilderProvider(),
            entityIdentityProvider
        );
        
        var customerMappingCustomizer = new CustomerMappingCustomizer();
        
        dlcJacksonModule.registerCustomizer(customerMappingCustomizer, customerMappingCustomizer.instanceType);
        
        var entityIdentityProvider = new EntityIdentityProvider() {
            @Override
            public Identity<?> provideFor(String entityTypeName) {
                if (entityTypeName.equals(CustomerId.class.getName())) {
                    return new CustomerId(Long.randomLong());
                }
                return null;
            }
        };

        objectMapper.registerModule(
            dlcJacksonModule
        );
        
    }
    
    @Test
    void testJsonSerialization() {
        Customer curstomer = Customer.builder()
            .id(CustomerId.builder().id(1L).build())
            .firstName("Tom")
            .someProperty("Test")
            .build();
            
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(curstomer);
        Customer serializedCustomer = objectMapper.readValue(json, Customer.class);
        
        assertEquals(customer.getId().get(), serializedCustomer.getId().get());
        assertEquals(customer.getFirstName(), serializedCustomer.getFirstName());
        assertEquals(customer.getSomeProperty(), serializedCustomer.getSomeProperty());
    }
}
```


---

|            **Domain-Object Builders**             |            **OpenAPI-Extension**            |
|:-------------------------------------------------:|:-------------------------------------------:|
| [<< Vorherige Seite](./dommainobject_builders.md) | [Nächste Seite >>](./open_api_extension.md) |
