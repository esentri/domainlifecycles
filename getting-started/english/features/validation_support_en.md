[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Validation Support](validation_support_en.md)

---

# Validation Support
Implementation of business logic rules and domain-specific invariants using the DomainAssertion API. 
Optionally, DLC also offers extended support of Java Bean Validation Annotations within a DomainObject to implement 
invariants or pre/post/conditions on methods and also ByteCode extension to simplify the implementation of 
an “Always-Valid-Strategy”.

---

## Implementation
In jedem der Domain-Types kann eine Validierung und Umsetzung von Geschäftslogik implementiert werden.

Zum Beispiel so:
```
public class Customer extends AggregateRootBase<CustomerId> {
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
            numberOfCurrenciesUsed
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

Die hier verwendete Byte-Code-Extension funktioniert natürlich nur, sofern diese
auch wie in der [Konfiguration](../guides/configuration_en.md) gezeigt aktiviert wurde.

Andernfalls können die Aufrufe `BeanValidations.validate(this);` und `validate();` 
auch immer explizit vorgenommen werden.

## Unit-Tests
```
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
        ).isInstanceOf(DomainAssertionException.class)
        .withMessageContaining("Customer has to be at least 18 years old!");
    }
}
```
---

|            **Domain-Events**             |           **Spring-Web-Integration**            |
|:----------------------------------------:|:-----------------------------------------------:|
| [<< Previous](domain_events_en.md) | [Next >>](spring_web_integration_en.md) |

---

**EN** / [DE](../../german/features/validation_support_de.md)
