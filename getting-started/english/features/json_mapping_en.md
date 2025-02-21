[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [JSON-Mapping](json_mapping_en.md)

---

# JSON-Mapping
Offers JSON mapping based on [Jackson](https://github.com/FasterXML/jackson), with DDD specific customization 
and configuration options. 

---

## Implementierung
A better JSON mapping adapted to DDD is already ensured by the configuration made under configuration made under [Create project](../guides/configuration_en.md#JSON-Mapping).
In a typical DDD implementation of building blocks, immutable structures are heavily used, or, for example, typed IDs are employed, 
which can cause problems or require additional effort when working with common JSON-mapping frameworks.

The default mapping can then be adapted by overwriting one or more methods of the
`JacksonMappingCustomizer`, as follows:
```Java
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

In this example above, the ```afterObjectRead``` method of DLC is called if a corresponding JSON object has been parsed 
and its values have already been transferred to the DomainObjectBuilder instance. The builder can now be manipulated,
before DLC creates the target domain object from it.

The `JacksonMappingCustomizer` must be activated via the following configuration using ````module.registerCustomizer````:

```Java
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
Both serialization and deserialization can be tested for corresponding unit tests.
An example of how to test successful serialization:

```Java
public class JacksonTest {

    private final ObjectMapper objectMapper;
    
    @BeforeAll
    public static void init() {
        var factory = new ReflectiveDomainModelFactory("tests");
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

|          **Domain-Object Builders**          |        **OpenAPI-Extension**        |
|:--------------------------------------------:|:-----------------------------------:|
| [<< Previous](./domainobject_builders_en.md) | [Next >>](open_api_extension_en.md) |

---

**EN** / [DE](../../german/features/json_mapping_de.md)
