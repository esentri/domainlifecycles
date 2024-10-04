## Domain Lifecycles JSON Mapping

DLC supports serialization and deserialization of JSON to/from Java based DomainObjects
via a [Jackson](https://github.com/FasterXML/jackson) extension.

The support of concepts of Domain Driven Design (DDD) and Java class structures that correspond to DDD concepts
is not necessarily provided by Jackson. Especially with objects that are immutable, Jackson requires either
default constructors or one have to specify the mapping for deserialization by annotation or in another way.
In addition, certain properties in DomainObjects are mapped
unfavorably in the default behavior of Jackson, as shown below.

### Mapping of Identities and single-valued ValueObjects

Identities are always 'single-valued' with DLC (i.e. they have exactly one property that holds the ID value).
In addition, ValueObjects often have only one property that holds the value. Because of the Java representation of
Identities
and ValueObjects as Java objects (except for enum-based ValueObjects), Jackson would map them as in the following
example:

The following Java class structure

```Java
public class Order extends AggregateRootBase<OrderNumber> {
    
  private final Ordernumber orderNumber;
  private CustomerId customerId;
}

public class OrderNumber implements Identity<Long> {
  private final Long value;
}

public class CustomerId implements ValueObject {
  private final String theIdOfTheCustomer;
}

```

is mapped by Jackson to the following JSON structure, if Jackson default mapping is applied:

 ```JSON
{
    "oderNumber": {
        "value": "1"
    },
    "customerId": {
        "theIdOfTheCustomer": "124-3445-X" 
    }
}
 ```

which is unfavourable for processing the JSON structure in a client application,
due to necessary nested access to the properties,
which may have different names in different 'single-valued' ValueObjects.

Instead of the mapping shown above, DLC provides mechanisms to serialize the Java class from above to
the following leaner and simpler JSON structure:

 ```JSON
{
  "oderNumber": "1",
  "customerId": "124-3445-X"
}
 ```

> **_NOTE:_**  In case of deserialization, DLC maps both JSON representations to the Java object structure shown above.

### Modification of the default mapping behavior

DLC JSON Mapping offers various customization options.

#### MappingCustomizer

Default mapping behaviour of DLC can be customized in various ways via
`api.io.domainlifecycles.jackson.JacksonMappingCustomizer`.
A MappingCustomizer offers various callback methods, which provide multiple entry points for customizations.

The entry-points for callbacks are explained in the following.

##### Callbacks for reading JSON structures

The root class for a MappingCustomizer has the following structure:

```Java
public abstract class JacksonMappingCustomizer<T extends DomainObject> {

    public final Class<? extends T> instanceType;

    public JacksonMappingCustomizer(Class<? extends T> instanceType) {
        this.instanceType = instanceType;
    }

    public MappingAction beforeFieldRead(final DomainObjectMappingContext mappingContext, TreeNode propertyNode, String fieldName, Class<?> expectedType, ObjectCodec codec){
        return MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
    }

    public MappingAction afterFieldRead(final DomainObjectMappingContext mappingContext, final Object readValue, String fieldName, Class<?> expectedType){
        return MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
    }

    public MappingAction beforeFieldWrite(final JsonGenerator jsonGenerator, String fieldName, Object propertyValue){
        return MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
    }

    public MappingAction beforeObjectRead(final DomainObjectMappingContext mappingContext, ObjectCodec codec){
        return MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
    }

    public MappingAction beforeObjectWrite(final JsonGenerator jsonGenerator, T object){
        return MappingAction.CONTINUE_WITH_DEFAULT_ACTION;
    }

    public void afterObjectRead(final DomainObjectMappingContext mappingContext, ObjectCodec codec){
    }
}
```

By overriding the respective methods, individual logic can be integrated into the mapping process:

- `beforeFieldRead`: is executed before the mapping of fields, when reading JSON structures.
- `afterFieldRead`: is executed after the mapping of fields, when reading JSON structures.
- `beforeObjectRead`: is executed before the mapping of DomainObjects, when reading JSON structures.
- `afterObjectRead`: is executed after the mapping of DomainObjects, when reading JSON structures.
- `beforeFieldWrite`: is executed before the mapping of fields, when writing JSON structures.
- `beforeObjectWrite`: is executed before the mapping of DomainObjects, when writing JSON structures.

By returning `MappingAction.CONTINUE_WITH_DEFAULT_ACTION` after the execution of custom logic
the default mapping behaviour for the field or DomainObject is continued.

By returning `MappingAction.SKIP_DEFAULT_ACTION` the default mapping logic for the corresponding
field or DomainObject is skipped.

The most common use case of a MappingCustomizer is `afterObjectRead`.
For the following example we suppose having an AggregateRoot called `Deactivation`:

```Java
public class DeactivationMappingCustomizer extends JacksonMappingCustomizer<Deactivation>{

    public DeactivationMappingCustomizer() {
        super(Deactivation.class);
    }

    @Override
    public void afterObjectRead(PersistableMappingContext mappingContext, ObjectCodec codec) {
        DomainObjectBuilder<?> b = mappingContext.domainObjectBuilder;
        if (mappingContext.domainObjectBuilder.getFieldValue("timestamp") == null) {
            //set the timestamp, when the deactivation is processed. 
            //This must be done in the MappingCustomizer, if `timestamp` is defined as `notNull`
            //and is not passed from `outside` (e.g. via REST endpoint)
            b.setFieldValue(Instant.now(), "timestamp");
        }
    }

}
```

> **_NOTE:_**  All implementations of `JacksonMappingCustomizer` must be passed to the `DlcJacksonModule`
> when [configuring](#activation-jackson-extension) the module.

###### DomainObjectMappingContext

The `DomainObjectMappingContext` provides access to the `DomainObjectBuilder` instance,
which is filled with values during the mapping process before the final `DomainObject` is created from it.
Furthermore, in hierarchical object structures, access can be gained to the parent `DomainObjectMappingContext`,
in order to react to properties of the parent object during the mapping process of child objects.

#### EntityIdentityProvider

When using DLC, Identities must always contain a `non-null` value. If instances of DomainObject are passed
to the application from the outside (e.g. via REST endpoint), then a value for the identity must already be assigned
in the mapping process, if the client does not do this (e.g. via client-side generated UUID). Especially with relational
database systems, the assignment of ID values from e.g. a 'SEQUENCE' is common practice.

By providing a `io.domainlifecycles.jackson.api.EntityIdentityProvider` Jackson can tell how new IDs are generated for
Entities
in the context of the mapping process, so that they can be assigned when the Entity is created from 'outside' and
thus, the always-valid principle is not violated.

```Java
public interface EntityIdentityProvider {
    Identity<?> provideFor(Class<? extends Entity<?>> entityType);
}
```

The method `provideFor()` takes the Entity type as parameter and should return
a new unused Identity suitable for this Entity.

> **_NOTE:_** A suitable `EntityIdentityProvider` implementation must be passed to the `DlcJacksonModule`
> when [configuring](#activation-jackson-extension) the module.

###### JooqEntityIdentityProvider

DLC Persistence provides an `EntityIdentityProvider` implementation based on database `Sequences`.
Prerequisite for the `JooqEntityIdentityProvider` is that `Sequences` are defined for each `Entity`, which
must apply to a predefined naming convention.

Example:
The Entity `DeliveryAddress` contains the Identity `DeliveryAddressId`:

```Java
public class DeliveryAddress extends EntityBase<DeliveryAddressId> {
    ...
}
```

For the correct functioning of the `JooqEntityIdentityProvider` a `Sequence` must be defined,
according to the following schema.

```SQL
CREATE SEQUENCE test_domain.delivery_adress_id_seq  MINVALUE 1000 MAXVALUE 999999999999999999 INCREMENT BY 1 START WITH 1000;
```

The `Sequence` must be named according to the following schema:

`<schema>.<id-type-name>_seq`

this is Camel-Case in Java to lower-case pascal-case in SQL with the suffix `_seq`.

<a name="activation-jackson-extension"></a>

### Activation of the Jackson extension

Common extensions are typically included in Jackson as Jackson modules. DLC offers
the `io.domainlifecycles.jackson.module.DlcJacksonModule` for this purpose.

In Spring or SpringBoot projects, Jackson modules can simply be declared as beans in order to include them:

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
