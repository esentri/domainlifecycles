[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Validation Support](validation_support_en.md)

---

# Validation Support
The implementation of business logic rules and domain-specific invariants can be simplified using the DomainAssertion API. 
Optionally, DLC also offers extended support of Java Bean Validation Annotations within a DomainObject to implement 
invariants or pre/post/conditions on methods.  Also a ByteCode extension is avaliable to simplify the implementation of 
an “Always-Valid-Strategy”. "Always-Valid" means that all domain objects can only be created with validations adhered to at all times. 
The ByteCode extension, for example, adds corresponding validation calls to all relevant constructors.

---

## Implementation
Validation and implementation of business logic can be implemented in each of the domain types.

For example like this:

```Java
public class Customer extends AggregateRootBase<CustomerId> {

    @NotNull
    private final CustomerId id;
    private final LocalDate birthDate;
    
    public Customer(final CustomerId id,
                    final long concurrencyVersion,
                    final LocalDate birthDate) {
        super(concurrencyVersion);
        this.id = id;
        //BeanValidations.validate(this); --> inserted by byte code extension.
        //validate(); --> inserted by byte code extension.
    }
    
    @Override
    public void validate() {
        DomainAssertions.isPast(
            numberOfCurrenciesUsed,
            "BirthDate has to be in the past!"
        );
        
        DomainAssertions.isBefore(
            numberOfCurrenciesUsed,
            LocalDate.now().minusYears(18),
            "Customer has to be at least 18 years old!"
        );
    }
    
    public void setBirthDate(final LocalDate birthDate) {
        this.birthDate = birthDate;
        //BeanValidations.validate(this); --> inserted by byte code extension.
        //validate(); --> inserted by byte code extension.
    }
    
    @Query //--> no byte code extension of this method
    private void doSomething() {
        // some logic
    }
}
```
In this example, both BeanValidation annotations (see `@NotNull`) and programmatic
`DomainAssertions` are used. The ByteCode extension of DLC automatically inserts the calls of `validate()` and 
`BeanValidations-validate(this);` automatically in the appropriate places. The use of bean validations is optional.
Alternatively all validations can also be mapped as DomainAssertion.

Of course, the byte code extension used here only works if it has been activated, for example in the 
Spring Boot Application class with `@PostConstruct` call:

```Java
@SpringBootApplication
public class ShopApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory("sampleshop"));
    }
    
    public static void main(String[] args) {
        new SpringApplicationBuilder(ShopApplication.class).run(args);
    }

    /**
     * Enable DLC byte code extension for the domain model of the "sampleshop"
     */
    @PostConstruct
    public void postConstruct() {
        ValidationDomainClassExtender.extend("sampleshop");
    }
}
```

Alternatively, the calls `BeanValidations.validate(this);` and `validate();`
can always be made explicitly.

## Unit-Tests
```Java
class CustomerTest {
    
    @Test
    void testSuccessfulInit() {
        assertThatNoException().isThrownBy(() -> 
            Customer.builder()
                .id(CustomerId.builder().id(1L).build())
                .birthDate(LocalDate.of("1990-01-01"))
                .build());
    }
    
    @Test
    void testAssertionFail() {
        assertThatThrownBy(() -> 
            Customer.builder()
                .id(CustomerId.builder().id(1L).build())
                .birthDate(LocalDate.of("2016-01-01"))
                .build())
            .isInstanceOf(DomainAssertionException.class)
            .withMessageContaining("Customer has to be at least 18 years old!");
    }
}
```
---

|          **Domain-Events**           |        **Spring-Web-Integration**         |
|:------------------------------------:|:-----------------------------------------:|
|  [<< Previous](domain_events_en.md)  |  [Next >>](spring_web_integration_en.md)  |

---

**EN** / [DE](../../german/features/validation_support_de.md)
