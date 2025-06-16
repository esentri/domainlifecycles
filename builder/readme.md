# DLC Builder Pattern Necessity and Support

DLC internally heavily relies on the Builder Pattern for the creation of DomainObjects. This is due to the fact
that we emphasize an always valid strategy on the one hand and on the other hand ValueObjects are by definition
immutable.
Thus, Domain Objects created from JSON Input or by querying a database must be created by a builder.
For developers, the usage of Builder Pattern is encouraged, since Domain Classes do often have many arguments
which bloats constructor calls unnecessarily.

DLC supports the Builder Pattern as suggested by Joshua Bloch in his book "Effective Java" (3rd Edition, 2018).
The specific implementation of the Builder Pattern as suggested by Joshua Bloch is described in the following.
The simplest way to work with DLC regarding the builder necessity is to use [Lombok Builders](#lombok-builder-support).

```java
public class SampleClass {

    private final int immutableProperty;
    private final int anotherImmutableProperty;
    private int mutableProperty;

    private SampleClass(int immutableProperty, int anotherImmutableProperty, int mutableProperty) {
        this.immutableProperty = immutableProperty;
        this.anotherImmutableProperty = anotherImmutableProperty;
        this.mutableProperty = mutableProperty;
    }

    public static SampleClassBuilder builder() {
        return new SampleClassBuilder();
    }

    public static class SampleClassBuilder {
        private final int immutableProperty;
        private final int anotherImmutableProperty;
        private int mutableProperty;

        SampleClassBuilder() {
        }

        public SampleClassBuilder setImmutableProperty(int immutableProperty) {
            this.immutableProperty = immutableProperty;
            return this;
        }

        // ... other setters are ommitted for previty

        public SampleClass build() {
            return new SampleClass(this.immutableProperty, this.anotherImmutableProperty, this.mutableProperty);
        }
    }
}
```

## DLC Builder Pattern requirements

The Builder Pattern implementation provided above is compliant to the default Builder Pattern support of DLC.

The classes `InnerClassDOmainObjectBuilderProvider`,
`InnerClassDomainObjectBuilder` and `InnerClassDomainObjectBuilderConfiguration` are used by default.

The required implementations are (alongside the implementation example above)

### Static Builder Access in the parent class

The tangible builder is fetched by accessing the type of the parents class `builder` method. Thus, the method
named `builder` is necessary for DLC to fetch that builder.

```java
public class SampleClass
    
    ...
    
    public static SampleClassBuilder builder() {
        return new SampleClassBuilder();
    }
    
    ...
```

### Provide default builder configuration

To provide the default builder configuration, the DLC configuration must be extended by the following
`Bean` in the manner of a classical Spring Configuration Bean.

```java
    @Bean
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider(DomainMirror domainMirror){
        return new InnerClassDomainObjectBuilderProvider(domainMirror);
    }

```

<a name="lombok-builder-support"></a>

## Lombok Builder Support

The Pattern and configuration provided above is also supported by Lombok.

With usage of Lombok, the class `SampleClass` would look like the following.


```java
public class SampleClass {

    private final int requiredProperty;
    private final int anotherRequiredProperty;
    private int optionalProperty;

    @Builder
    public SampleClass(Builder builder) {
        this.requiredProperty = builder.requiredProperty;
        this.anotherRequiredProperty = builder.anotherRequiredProperty;
        this.optionalProperty = builder.optionalProperty;
    }
}
```

## Custom Naming for Inner Class Builder Pattern

If the inner class Builder Pattern as shown above is sufficient, and you only need to use a different naming
schema for the `builder`, `build`and `set`Methods, you can provide a custom configuration.

Custom configurations can be provided by implementing `DomainBuilderConfiguration`. The tangible implementation
can be shown by the default configuration for the inner builder.

```java
public final class InnerClassDefaultDomainBuilderConfiguration implements DomainBuilderConfiguration {

    private final Function<String, String> f = k -> "set" + k.substring(0, 1).toUpperCase() + k.substring(1);

    public InnerClassDefaultDomainBuilderConfiguration() {
        // Default Constructor
    }

    @Override
    public String builderMethodName() {
        return "builder";
    }

    @Override
    public String buildMethodName() {
        return "build";
    }

    @Override
    public String setterFromPropertyName(String propertyName) {
        return f.apply(propertyName);
    }
}
```

Nothing fancy here. Simply provide the desired method names and the desired setter naming schema.

Pass the configuration to the `InnerClassDomainObjectBuilderProvider` constructor.

```java
    @Bean
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider(DomainMirror domainMirror){
        return new InnerClassDomainObjectBuilderProvider(domainMirror, new MyNewInnerDomainBuilderConfiguration());
    }
```

## Custom Builder Pattern

If the default inner class Builder Pattern implementation is not sufficient, a custom implementation can be provided.
Therefore, the following classes must be implemented.

* `AbstractDomainObjectBuilder<T>` with `<T extends DomainObject>` to define how to build the DomainObject.
* `DomainObjectBuilderProvider` to provide the custom builder.
* `DomainBuilderConfiguration` to provide the custom builder configuration.

Further information, on how to implement the classes above, can be found by examining the default
inner builder class implementation located in the package `io.domainlifecycles.builder.innerclass` alongside
with the provided documentation.
