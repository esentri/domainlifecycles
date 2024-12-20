[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [JSON-Mapping](json_mapping_en.md)

---

# JSON-Mapping
Offers JSON mapping based on [Jackson](https://github.com/FasterXML/jackson), with DDD specific customization 
and configuration options.

---

## Implementation
Ein an DDD angepasstes, besseres JSON-Mapping wird bereits sichergestellt durch die 
vorgenommene Konfiguration unter [Projekt erstellen](../guides/configuration_en.md#JSON-Mapping).

Im weiteren Verlauf kann das Default-Mapping angepasst werden durch das überschreiben einer oder mehrerer Methoden des
`JacksonMappingCustomizer`, wie folgt:
```
public class CustomerMappingCustomizer extends JacksonMappingCustomizer<Customer>{

    public CustomerMappingCustomizer() {
        super(Customer.class);
    }

    @Override
    public void afterObjectRead(PersistableMappingContext mappingContext, ObjectCodec codec) {
        DomainObjectBuilder<?> b = mappingContext.domainObjectBuilder;
        
        // alter some of the mapping configurations
    }
}
```

und die Konfiguration anschließend aktivieren:

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
Für entsprechende Unit-Tests kann sowohl die Serialisierung als auch Deserialisierung getestete werden.
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
        objectMapper.registerModule(new JavaTimeModule());
        
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
            new DlcJacksonModule(
                new InnerClassDomainObjectBuilderProvider(),
                entityIdentityProvider
            )
        );
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new ParameterNamesModule());
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
| [<< Previous](./dommainobject_builders.md) | [Next >>](open_api_extension_en.md) |

---

**EN** / [DE](../../german/features/json_mapping_de.md)