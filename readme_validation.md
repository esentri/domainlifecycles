## NitroX Domain Lifecycles validation

Many frameworks are based on the idea that the validation of objects always takes place
at certain phase transitions, e.g. before these objects are stored (that means somehow in the persistence layer).
There is nothing wrong with this in principle, but NitroX DLC focuses on
a strong compliance with the business rules within the domain (in the domain core).

The goal here is, that all DomainObjects follow the business rules associated with the domain or bounded context at all times.
That means those rules should be checked as soon as the corresponding objects are ready to act in the Domain Core,
i.e. as soon as they are created (via Constructor). In the Domain Core it should never come to situations,
where DomainObjects are processed in an "invalid" state.

### Invariants

Invariants are invariant conditions that must be met at any time during program execution.
They have to be checked therefore with the construction of the domain objects and also with any state changes.

An advantage of this procedure is, that one does not have check several times at different places in the business logic
to make sure that invariants are really met.

### Pre- and post-conditions

Besides invariants, it makes sense to check certain state transitions for their correctness.

Pre-conditions are checked before a change of state. Post-conditions after the execution of a 
state change. They ensure the correctness of the previously executed operation.

### Basis - the interface 'Validatable`

The interface `nitrox.dlc.domain.types.Validatable` defines a
method `validate()`. This method can be used in every `DomainObject`
(= any `Entity`, any `ValueObject`) to validate invariants via`DomainAssertion`.

With enabled `always-valid` strategy (see below), 
NitroX DLC will call `validate()` at appropriate times (e.g. after construction of DomainObjects) 
and thus ensures the implemented invariants. Then developers don't have to take care of that.

### DomainAssertions

A possibility to check business rules of any kind (no matter, if they are
invariants, pre or post conditions) in imperative form.
is given in NitroX DLC by `DomainAssertions`. 

The following possibilities are provided by
`nitrox.dlc.domain.types.assertion.DomainAssertions`:

| DomainAssertion      | description                                                                                                                                        | available for                                                                                                                                                                                                                                                                                                                          | 
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| equals               | check for equality                                                                                                                                 | primitive Java types and any kind of Java object or `java.util.Optional`                                                                                                                                                                                                                                                               |
| notEquals            | check for inequality                                                                                                                               | primitive Java types and any kind of Java object or `java.util.Optional`                                                                                                                                                                                                                                                               |
| isFalse              | checking a boolean expression or value for `false`                                                                                                 | `boolean`                                                                                                                                                                                                                                                                                                                              |
| isTrue               | checking a boolean expression or value for `true`                                                                                                  | `boolean`                                                                                                                                                                                                                                                                                                                              |
| isOneOf              | checking a Java object (or optional) whether it corresponds to one of the objects passed in a collection (equals comparison)                       | any kind of Java object or even `java.util.Optional`                                                                                                                                                                                                                                                                                   |
| hasLengthMax         | checking the maximum length of a string                                                                                                            | `java.lang.String` or `java.lang.Optional<String>` respectively                                                                                                                                                                                                                                                                        |
| hasLengthMin         | checking the minimum length of a string                                                                                                            | `java.lang.String` or `java.lang.Optional<String>` respectively                                                                                                                                                                                                                                                                        |
| hasLength            | checking the length of a string (minimum and maximum)                                                                                              | `java.lang.String` or `java.lang.Optional<String>` respectively                                                                                                                                                                                                                                                                        |
| hasSizeMax           | checking the maximum size of a collection or map or array or optional array.                                                                       | `java.util.Collection` or `java.util.Map` or array or optional array (`java.util.Optional<Object[]>`)                                                                                                                                                                                                                                  |
| hasSizeMin           | checking the minimum size of a collection or map or array or optional array.                                                                       | `java.util.Collection` or `java.util.Map` or array or optional array (`java.util.Optional<Object[]>`)                                                                                                                                                                                                                                  |
| hasSize              | checking the size of a collection or map or array or optional array (minimum and maximum).                                                         | `java.util.Collection` or `java.util.Map` or array or optional array (`java.util.Optional<Object[]>`)                                                                                                                                                                                                                                  |
| hasMaxDigits         | checking the maximum number of digits before and after the decimal point for numerical values.                                                     | `java.math.BigDecimal` or double, float, and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                                                                           |
| hasMaxDigitsInteger  | checking the maximum number of digits before the decimal point for numerical values.                                                               | `java.math.BigDecimal`, `java.math.BigInteger` or double, float, long, int, byte, short and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                               |
| hasMaxDigitsFraction | checking the maximum number of digits after the decimal point for numerical values.                                                                | `java.math.BigDecimal` or double, float as well as their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                                                                                 |
| isFuture             | checking a time specification whether it is in the future.                                                                                         | `java.time.LocalDate`, `java.time.LocalTime`, `java.time.LocalDateTime`, `java.util.Date`, `java.util.Calendar`, `java.time.Instant`, `java.time.Year`, `java.time.YearMonth`, `java.time.MonthDay`, `java.time.ZonedDateTime`, `java.time.OffsetTime`, `java.time.OffsetDateTime` or `java.util.Optional` with corresponding content |
| isFutureOrPresent    | checking a time specification whether it is in the future or corresponds to the current time.                                                      | `java.time.LocalDate`, `java.time.LocalTime`, `java.time.LocalDateTime`, `java.util.Date`, `java.util.Calendar`, `java.time.Instant`, `java.time.Year`, `java.time.YearMonth`, `java.time.MonthDay`, `java.time.ZonedDateTime`, `java.time.OffsetTime`, `java.time.OffsetDateTime` or `java.util.Optional` with corresponding content |
| isPast               | checking a time specification whether it is in the past.                                                                                           | `java.time.LocalDate`, `java.time.LocalTime`, `java.time.LocalDateTime`, `java.util.Date`, `java.util.Calendar`, `java.time.Instant`, `java.time.Year`, `java.time.YearMonth`, `java.time.MonthDay`, `java.time.ZonedDateTime`, `java.time.OffsetTime`, `java.time.OffsetDateTime` or `java.util.Optional` with corresponding content |
| isPastOrPresent      | checking a time specification whether it is in the past or corresponds to the current time.                                                        | `java.time.LocalDate`, `java.time.LocalTime`, `java.time.LocalDateTime`, `java.util.Date`, `java.util.Calendar`, `java.time.Instant`, `java.time.Year`, `java.time.YearMonth`, `java.time.MonthDay`, `java.time.ZonedDateTime`, `java.time.OffsetTime`, `java.time.OffsetDateTime` or `java.util.Optional` with corresponding content |
| isBefore             | check of a time specification whether it is before a comparison time specification (both time specifications must have the same type).             | `java.time.LocalDate`, `java.time.LocalTime`, `java.time.LocalDateTime`, `java.util.Date`, `java.util.Calendar`, `java.time.Instant`, `java.time.Year`, `java.time.YearMonth`, `java.time.MonthDay`, `java.time.ZonedDateTime`, `java.time.OffsetTime`, `java.time.OffsetDateTime` or `java.util.Optional` with corresponding content |
| isAfter              | check of a time specification whether it is after a comparison time specification (both time specifications must have the same type).              | `java.time.LocalDate`, `java.time.LocalTime`, `java.time.LocalDateTime`, `java.util.Date`, `java.util.Calendar`, `java.time.Instant`, `java.time.Year`, `java.time.YearMonth`, `java.time.MonthDay`, `java.time.ZonedDateTime`, `java.time.OffsetTime`, `java.time.OffsetDateTime` or `java.util.Optional` with corresponding content |
| isBeforeOrEqualTo    | check of a time specification whether it is before or equal to a comparison time specification (both time specifications must have the same type). | `java.time.LocalDate`, `java.time.LocalTime`, `java.time.LocalDateTime`, `java.util.Date`, `java.util.Calendar`, `java.time.Instant`, `java.time.Year`, `java.time.YearMonth`, `java.time.MonthDay`, `java.time.ZonedDateTime`, `java.time.OffsetTime`, `java.time.OffsetDateTime` or `java.util.Optional` with corresponding content |
| isAfterOrEqualTo     | checking a time specification whether it is after or equal to a comparison time specification (both time specifications must have the same type).  | `java.time.LocalDate`, `java.time.LocalTime`, `java.time.LocalDateTime`, `java.util.Date`, `java.util.Calendar`, `java.time.Instant`, `java.time.Year`, `java.time.YearMonth`, `java.time.MonthDay`, `java.time.ZonedDateTime`, `java.time.OffsetTime`, `java.time.OffsetDateTime` or `java.util.Optional` with corresponding content |
| isNotEmpty           | checking a string if it is neither empty `""` nor null or if an optional of a string is not empty.                                                 | `java.lang.String` or `java.lang.Optional<String>`                                                                                                                                                                                                                                                                               |
| isValidEmail         | checking a string whether it corresponds to a valid email address.                                                                                 | `java.lang.String` or `java.lang.Optional<String>`                                                                                                                                                                                                                                                                                   |
| isNotBlank           | checking a string whether it contains at least one alphanumeric character.                                                                         | `java.lang.String` or `java.lang.Optional<String>`                                                                                                                                                                                                                                                                                  |
| isNotEmptyIterable   | checking a collection or a map or an array or an optional array whether they each contain at least one element.                                    | `java.util.Collection` or `java.util.Map` or array or optional array (`java.util.Optional<Object[]>`)                                                                                                                                                                                                                     |
| isNull               | checking an object whether it is `null`.                                                                                                           | `java.lang.Object`                                                                                                                                                                                                                                                                                                                     |
| isNotNull            | checking an object if it is not `null`.                                                                                                            | `java.lang.Object`                                                                                                                                                                                                                                                                                                                     |
| isInRange            | checking a numeric value whether it is within the defined limits (limit parameters must be of the same type).                                      | `java.math.BigDecimal`, `java.math.BigInteger` or double, float, long, int, byte, short and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                                  |
| regEx                | checking a string whether it corresponds to a regular expression.                                                                                  | `java.lang.String` or `java.lang.Optional<String>`                                                                                                                                                                                                                                                                                   |
| isGreaterThan        | checking a numerical value whether it is greater than a comparison value (comparison value must have the same type).                               | `java.math.BigDecimal`, `java.math.BigInteger` or double, float, long, int, byte, short and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                                 |
| isLessThan           | checking a numerical value whether it is smaller than a comparison value (comparison value must have the same type).                               | `java.math.BigDecimal`, `java.math.BigInteger` or double, float, long, int, byte, short and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                             |
| isGreaterOrEqual     | checking a numerical value whether it is greater than or equal to a comparison value (comparison value must have the same type).                   | `java.math.BigDecimal`, `java.math.BigInteger` or double, float, long, int, byte, short and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                                 |
| isLessOrEqual        | checking a numerical value whether it is less than or equal to a comparison value (comparison value must have the same type).                      | `java.math.BigDecimal`, `java.math.BigInteger` or double, float, long, int, byte, short and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                                 |
| isPositive           | checking a numeric value to see if it is positive.                                                                                                 | `java.math.BigDecimal`, `java.math.BigInteger` or double, float, long, int, byte, short and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                                |
| isNegative           | checking a numeric value to see if it is negative.                                                                                                 | `java.math.BigDecimal`, `java.math.BigInteger` or double, float, long, int, byte, short and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                                 |
| isPositiveOrZero     | checking a number value whether it is positive or `0`.                                                                                             | `java.math.BigDecimal`, `java.math.BigInteger` or double, float, long, int, byte, short and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                                 |
| isNegativeOrZero     | checking a number value whether it is negative or `0`.                                                                                             | `java.math.BigDecimal`, `java.math.BigInteger` or double, float, long, int, byte, short and their wrapper types and `java.util.Optional` with corresponding content                                                                                                                                                                |

ATTENTION: For all checks, where the value to be checked is passed in a
in a `java.lang.Optional`, the respective `DomainAssertion`
logic will only be executed if `Optional.isPresent()` is `true`.

### Java Bean Validation Support

NitroX DLC explicitly supports the features of
of [Jakarta Bean Validation Standards 2.0](https://beanvalidation.org/2.0-jsr380/):

- [Unterstützte Annotations](https://beanvalidation.org/2.0-jsr380/spec/#builtinconstraints)
- [Field & Property Validation](https://beanvalidation.org/2.0-jsr380/spec/#constraintdeclarationvalidationprocess-requirements-objectvalidation)
- [Method & Constructor Level Constraints](https://beanvalidation.org/2.0-jsr380/spec/#constraintdeclarationvalidationprocess-methodlevelconstraints)

The use of bean validation annotations is completely optional.
Alternatively, all business rules can also be expressed imperatively via `DomainAssertions`!

ATTENTION:
- Basic requirement for the use of Java Bean Validations, is that a Bean Validation implementation is provided.
  I.e. an implementation library like e.g. Hibernate Validator must be included at runtime. Otherwise the corresponding constraint
  checks will not be performed and only a log warning will be issued!
- The use of `@Valid` (
  see [GraphValidation](https://beanvalidation.org/2.0-jsr380/spec/#constraintdeclarationvalidationprocess-validationroutine-graphvalidation))
  is not needed, since Nitrox DLC always performs a validation of the complete object graph for all DomainTypes.
  validation of the complete object graph!
- Without using the [NitroX DLC Byte Code Extension for Validation](#always-valid), 
  the developer must take care to start bean validation annotation evaluation (e.g. by calling `BeanValidations.validate(thisObject);`). 
  He also needs to take care that `validate()` is called at appropriate places.

```Java
...
@Override
public void validate() {
        BeanValidations.validate(this); // only needed, if BeansValidations are used without the Nitrox DLC ValidationExtender
        long numberOfCurrenciesUsed = positions
        .stream()
        .map(p -> p.getUnitPrice().getCurrency())
        .distinct()
        .count();
        DomainAssertions.equals(
        numberOfCurrenciesUsed,
        1,
        "All items in an order must have a 'unitPrice' in the same currency."
        );
}
        ...
```

#### Example: Field level Bean Validation Annotations to implement invariants

```Java
public class Order extends AggregateRootBase<OrderNumber> {

  @NotNull
  private final OrderNumber orderNumber;

  private Optional<@Size(max = 255) String> commentCustomer;

  private Optional<LocalDate> desiredDeliveryDate;

  @NotNull
  private CustomerNumber customerNumber;

  @NotNull
  private OrderStatus status;
  private List<ActionCode> actionCodes;

  @NotEmpty
  private List<OrderPosition> positions;

  @NotNull
  private DeliveryAddress deliveryAddress;
    
    ...
}
```

<a name="always-valid"></a>
### Always Valid

This feature uses [ByteBuddy](https://bytebuddy.net/#/) to extend methods and the
constructor of domain classes at runtime.
A developer must substantially less effort to decide, at which
the state of the program a domain object is generated/manipulated 
and if that action requires a validation call.

The bytecode extension extends all DomainObjects that implement Validatable
(AggregateRoot, Entity, ValueObject, DomainCommand, DomainEvent, Identity). 
The following places are extended:

- Any constructor: `validate();` (as well as `BeanValidations.validate(this);`) is inserted at the end of each constructor.
- Any method that does not follow the getter convention (getter =
  method name starts with `get...` (boolean `is...`), without parameters, but with a return value): `validate()` (and `BeanValidations.validate(this);`) is inserted at the end of the affected methods,
  before returning a potential return value
- Any method being annotated with either `nitrox.dlc.core.validation.Query`
  or with `nitrox.dlc.core.validation.ExcludeValidation` will be
  excluded from the ByteCode extension

Example of a class with ByteCode extension enabled. The places where the
ByteCode extension intervenes are marked in the code:

```Java
public class Order extends AggregateRootBase<OrderNumber> {

  @NotNull
  private final OrderNumber orderNumber;
  private Optional<@Size(max = 255) String> commentCustomer;

  private Optional<LocalDate> desiredDeliveryDate;

  @NotNull
  private CustomerNumber customerNumber;

  @NotNull
  private OrderStatus status;
  private List<ActionCode> actionCodes;

  @NotEmpty
  private List<OrderPosition> positions;

  @NotNull
  private DeliveryAddress deliveryAddress;

  @Builder(setterPrefix = "set")
  public Order(OrderNumber orderNumber,
               List<OrderPosition> positions,
               DeliveryAddress deliveryAddress,
               CustomerNumber customerNumber,
               OrderStatus status,
               List<ActionCode> actionCodes,
               String commentCustomer,
               LocalDate desiredDeliveryDate,
               long concurrencyVersion
  ) {
    super(concurrencyVersion);
    this.orderNumber = orderNumber;
    this.commentCustomer = Optional.ofNullable(commentCustomer);
    this.customerNumber = customerNumber;
    this.status = status;
    this.actionsCodes = (actionsCodes == null ? new ArrayList<>() : new ArrayList<>(actionsCodes));
    this.positions = positions;
    this.deliveryAddress = deliveryAddress;
    this.desiredDeliveryDate = Optional.ofNullable(desiredDeliveryDate);
    //BeanValidations.validate(this); --> inserted by byte code extension.
    //validate(); --> inserted by byte code extension.
  }

  @Override
  public void validate() {
    long numberOfCurrenciesUsed = positions
            .stream()
            .map(p -> p.getUnitPrice().getCurrency())
            .distinct()
            .count();
    DomainAssertions.equals(
            numberOfCurrenciesUsed,
            1,
            "All items in an order must have a 'unitPrice' in the same currency."
    );
  }

  public price getTotalPrice() {
    BigDecimal sum = positions
            .stream()
            .map(p -> p.getUnitPrice().getAmount())
            .reduce(BigDecimal::add)
            .get();
    return price
            .builder()
            .setAmount(sum)
            .setCurrency(positions.get(0).getUnitPrice().getCurrency())
            .build();
  }

  public void setCommentOrderer(String commentOrderer) {
    this.commentOrderer = Optional.ofNullable(commentOrderer);
    //BeanValidations.validate(this); --> inserted by byte code extension.
    //validate(); --> inserted by byte code extension.
  }

  public void setDesiredDeliveryDate(LocalDate desiredDeliveryDate) {
    DomainAssertions.equals(status.getStatusCode(), StatusCode.INPUT, "The 'desiredDeliveryDate' can only be changed in the OrderStatus with statusCode 'INPUT' yet!");
    this.desiredDeliveryDate = Optional.ofNullable(desiredDeliveryDate);
    //BeanValidations.validate(this); --> inserted by byte code extension.
    //validate(); --> inserted by byte code extension.
  }

  public OrderNumber getOrderNumber() {
    return orderNumber;
  }

  public Optional<String> getCommentCustomer() {
    return commentCustomer;
  }

  ...

  @Query //--> no byte code extension of this method
  private Optional<OrderPosition> findOrderPosition(OrderPositionId orderPositionId) {
    var orderPositionOptional = positions
            .stream()
            .filter(p -> p.getId().equals(orderPositionId))
            .findFirst();
    return orderPositionOptional;
  }
}
```

ATTENTION:
- Not shown in this example is, that the byte code extension also automatically takes care 
  of Bean Validation annotated return values or annotated method parameters

#### Activation of the byte code extension

The byte code extension is activated by Java call and should be done as soon as 
possible after application start:

- The activation is done via `ValidationDomainClassExtender.extend(...);`
- As parameter a list of packages is to be passed, in which the classes
  which are to be extended.

Example: Activation in a Spring application

```
@SpringBootApplication
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class).run(args);
    }

    @PostConstruct
    public void postConstruct(){
        ValidationDomainClassExtender.extend(List.of("nitrox.demo"));
    }
}
```

### Validations without byte code extension

Alternatively to the use of byte code extensions there is the possibility that
that the developer inserts all validation calls independently, that causes more effort, 
but allows a more fine-grained control.

In this case, the previous example could look like this:

```Java
public class Order extends AggregateRootBase<OrderNumber> {

  @NotNull
  private final OrderNumber orderNumber;
  private Optional<@Size(max = 255) String> commentCustomer;

  private Optional<LocalDate> desiredDeliveryDate;

  @NotNull
  private CustomerNumber customerNumber;

  @NotNull
  private OrderStatus status;
  private List<ActionCode> actionCodes;

  @NotEmpty
  private List<OrderPosition> positions;

  @NotNull
  private DeliveryAddress deliveryAddress;

  @Builder(setterPrefix = "set")
  public Order(OrderNumber orderNumber,
               List<OrderPosition> positions,
               DeliveryAddress deliveryAddress,
               CustomerNumber customerNumber,
               OrderStatus status,
               List<ActionCode> actionCodes,
               String commentCustomer,
               LocalDate desiredDeliveryDate,
               long concurrencyVersion
  ) {
    super(concurrencyVersion);
    this.orderNumber = orderNumber;
    this.commentCustomer = Optional.ofNullable(commentCustomer);
    this.customerNumber = customerNumber;
    this.status = status;
    this.actionsCodes = (actionsCodes == null ? new ArrayList<>() : new ArrayList<>(actionsCodes));
    this.positions = positions;
    this.deliveryAddress = deliveryAddress;
    this.desiredDeliveryDate = Optional.ofNullable(desiredDeliveryDate);
    BeanValidations.validate(this); //--> manually inserted by developer
    validate(); //--> manually inserted by developer
  }

  @Override
  public void validate() {
    long numberOfCurrenciesUsed = positions
            .stream()
            .map(p -> p.getUnitPrice().getCurrency())
            .distinct()
            .count();
    DomainAssertions.equals(
            numberOfCurrenciesUsed,
            1,
            "All items in an order must have a 'unitPrice' in the same currency."
    );
  }

  public price getTotalPrice() {
    BigDecimal sum = positions
            .stream()
            .map(p -> p.getUnitPrice().getAmount())
            .reduce(BigDecimal::add)
            .get();
    return price
            .builder()
            .setAmount(sum)
            .setCurrency(positions.get(0).getUnitPrice().getCurrency())
            .build();
  }

  public void setCommentOrderer(String commentOrderer) {
    this.commentOrderer = Optional.ofNullable(commentOrderer);
    BeanValidations.validate(this); //--> manually inserted by developer
  }

  public void setDesiredDeliveryDate(LocalDate desiredDeliveryDate) {
    DomainAssertions.equals(status.getStatusCode(), StatusCode.INPUT, "The 'desiredDeliveryDate' can only be changed in the OrderStatus with statusCode 'INPUT' yet!");
    this.desiredDeliveryDate = Optional.ofNullable(desiredDeliveryDate);
    //no call of validations needs to added, as the desiredDeliveryDate is not involved in
    //validation rules implemented in validate() or any Bean Validation annotations
  }

  public OrderNumber getOrderNumber() {
    return orderNumber;
  }

  public Optional<String> getCommentCustomer() {
    return commentCustomer;
  }

  ...

  // @Query  annotation not needed without activated byte code extension 
  private Optional<OrderPosition> findOrderPosition(OrderPositionId orderPositionId) {
    var orderPositionOptional = positions
            .stream()
            .filter(p -> p.getId().equals(orderPositionId))
            .findFirst();
    return orderPositionOptional;
  }
}
```




