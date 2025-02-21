[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Validation Support](validation_support_de.md)

---

# Validation Support
Implementierung von Geschäftslogik-Regeln und Domänen spezifischen Invarianten kann mittels der DomainAssertion API vereinfacht werden. 
Optional bietet DLC auch erweiterten Support der Java Bean Validation Annotations innerhalb eines DomainObjects, um 
Invarianten oder Pre-/Post/-Conditions auf Methoden zu implementieren. Außerdem steht eine ByteCode Erweiterung zur Verfügung, um die 
Implementierung einer “Always-Valid-Strategy” zu vereinfachen. "Always-Valid" bedeutet, dass alle Domänenobjekte immer nur mit eingehaltenen
Validierungen erzeugt werden können, die ByteCode-Erweiterung fügt entsprechende Validierungsaufrufe beispielsweise in alle entsprechenden 
Konstruktoren hinzu.

---
## Implementierung
In jedem der Domain-Types kann eine Validierung und Umsetzung von Geschäftslogik implementiert werden.

Zum Beispiel so:
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
In diesem Beispiel werden sowohl BeanValidation-Annotations (siehe `@NotNull`) als auch programmatische
`DomainAssertions` verwendet. Die ByteCode-Erweiterung von DLC fügt die Aufrufe von `validate()` und `BeanValidations-validate(this);`
automatisch an den passenden Stellen ein. Der Einsatz von Bean Validations ist dabei Optional. Alternativ lassen sich 
alle Validierungen auch als DomainAssertion abbilden.

Die hier verwendete Byte-Code-Extension funktioniert natürlich nur, sofern diese
auch aktiviert wurde, beispielsweise in der Spring Boot Application Klasse mit `@PostConstruct`-Aufruf:

```Java
@SpringBootApplication
public class ShopApplication {

    static {
        Domain.initialize(new ReflectiveDomainModelFactory("sampleshop"));
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

Andernfalls können die Aufrufe `BeanValidations.validate(this);` und `validate();` 
auch immer explizit vorgenommen werden.

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

|             **Domain-Events**             |            **Spring-Web-Integration**            |
|:-----------------------------------------:|:------------------------------------------------:|
| [<< Vorherige Seite](domain_events_de.md) | [Nächste Seite >>](spring_web_integration_de.md) |

---

**DE** / [EN](../../english/features/validation_support_en.md)
