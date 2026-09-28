# The DLC Domain Mirror

The DLC Domain Mirror contains a metamodel of the tactical design structures within bounded contexts. This metamodel is
typically instantiated at application startup using Java reflection. It mirrors the current implementation state of the
implemented DDD building blocks. This metamodel information is used by several DLC modules at runtime to derive "DDD specific"
behaviour. For example automapping Aggregates into the underlying database tables, or routing DomainEvents to processing
DomainService or Aggregate instances, ... .

The Domain Mirror provides several options to query the implementation's DDD meta model
via a central static interface `io.domainlifecycles.mirror.api.Domain`.

## Domain Mirror initialization

The Domain Mirror must be initialized before all other DLC module configurations are done, as most of the modules
depend on the mirror.

To guarantee the mirror initialization is done before everything else, it could be done in a static way in the
application's main class:

```Java
public class ShopApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory("sampleshop"));
    }

    public static void main(String[] args) {
        ...
    }
}
```

ATTENTION: If generics and deeper nested inheritance structures are used, the default initialization of the mirror as
described above
does sometimes not provide all necessary type information (because of Java's type erasure). DLC provides a way to work
around that problem by setting a special type resolver (`io.domainlifecycles.mirror.resolver.TypeMetaResolver`), 
that does deep type resolving.

```Java
public class ShopApplication {

    static {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
    }

    public static void main(String[] args) {
        ...
    }
}
```

This is especially useful for rendering the most concrete type information
using [DLC Domain Diagrams](../domain-diagrammer/readme.md).

## Bounded Contexts

The Domain Mirror splits the mirrored domain model into Bounded Contexts
(`io.domainlifecycles.mirror.api.BoundedContextMirror`), each one rooted at exactly one Java package -
every domain type whose type name starts with that package name belongs to it.

Bounded Context boundaries can be derived automatically from the code, by annotating a Bounded
Context's root package (in its `package-info.java`) with `@io.domainlifecycles.domain.types.BoundedContext`:

```Java
@BoundedContext("Order Management")
package sampleshop.orders;

import io.domainlifecycles.domain.types.BoundedContext;
```

The annotation's optional name is exposed via `BoundedContextMirror.getName()`, in addition to the
package name.

The effective Bounded Contexts are resolved with the following precedence:

1. Bounded Context packages configured explicitly via `AbstractDomainMirrorFactory#setBoundedContextPackages(String...)`
   always take precedence, regardless of any `@BoundedContext` annotation.
2. Otherwise, Bounded Contexts derived from `@BoundedContext`-annotated packages are used, if any are found.
3. Otherwise, the whole scanned domain model falls back to a single Bounded Context, as before.

Bounded Context packages - whether derived or explicitly configured - must not be nested within one
another; an overlap is rejected with a `MirrorException` when the mirror is initialized.

## Mirroring non-domain classes

By default, the mirror does not only pick up classes implementing one of the DLC marker interfaces
(`Entity`, `ValueObject`, `AggregateRoot`, `DomainService`, `Repository`, `ApplicationService`,
`DomainCommand`, `DomainEvent`, `ReadModel`, `QueryHandler`, `OutboundService`, `ServiceKind`, ...).
Every other class within the scanned domain model packages (e.g. a mapper, helper or utility class)
is mirrored too, as a `io.domainlifecycles.mirror.api.NonDomainTypeMirror` tagged with
`DomainType.NON_DOMAIN` - reflecting its fields and methods just like any other mirrored type. No
marker interface is needed for this: a class only has to live in one of the packages passed to the
`ReflectiveDomainMirrorFactory` and not match any recognized `DomainType`.

Any `ServiceKindMirror` (domain service, application service, repository, query handler, outbound
service or an unspecified service kind) exposes the non-domain classes it actually uses via
`getReferencedNonDomainTypes()`, resolved from its fields, method parameters and return types.
The inverse also holds: any `NonDomainTypeMirror` exposes the service kinds it itself references
via `getReferencedServiceKinds()` - useful for classes that call *into* a service kind rather than
being called by it, e.g. a controller or a message listener that holds an application service.

This behaviour is enabled by default and can be switched off, e.g. to keep the mirror initialization
faster on very large codebases, or to preserve the previous, marker-interface-only behaviour:

```Java
public class ShopApplication {

    static {
        var factory = new ReflectiveDomainMirrorFactory("sampleshop");
        factory.setIncludeNonDomainClasses(false);
        Domain.initialize(factory);
    }

    public static void main(String[] args) {
        ...
    }
}
```

### Leaving generated code out

Generated code can make up the vast majority of a domain model's non-domain classes - most notably the
table and record classes jOOQ generates, which declare hundreds to thousands of methods each. In a real
world project these accounted for 95 % of the mirrored non-domain classes' size, bloating the mirror
(and the static analysis built on top of it) without adding anything a domain diagram shows. Such
classes are therefore left out by default.

Generated classes are recognized by their supertypes: a class is left out if one of its superclasses or
interfaces - direct or inherited - lies within one of the *excluded supertype packages*, by default
`org.jooq` (catching jOOQ's tables, records, schemas and catalogs). Generation annotations like
`@Generated` cannot be used for this, since they are only retained in the source code. For generated
classes without a common supertype (like jOOQ's `Keys` or `Tables`), whole packages can be excluded:

```Java
var factory = new ReflectiveDomainMirrorFactory("sampleshop");
// default: List.of("org.jooq"); an empty list switches the supertype based exclusion off
factory.setNonDomainExcludedSupertypePackages(List.of("org.jooq", "com.example.codegen.base"));
// default: none
factory.setNonDomainExcludedPackages(List.of("sampleshop.persistence.generated"));
```

Package names match the package itself and all its sub-packages. See `NonDomainClassFilter`.

The [DLC Domain Diagrammer](../domain-diagrammer/readme.md#showing-non-domain-classes) builds on
this to optionally draw the non-domain classes referenced by a service in a diagram.



## Event listeners of Spring and Spring Modulith

A method listens to a domain event (`MethodMirror#getListenedEvent()`) if it is annotated with
`@DomainEventListener` - or with one of the event listener annotations of Spring or Spring Modulith:

- `@org.springframework.context.event.EventListener`
- `@org.springframework.transaction.event.TransactionalEventListener`
- `@org.springframework.modulith.events.ApplicationModuleListener`
- an own annotation composed of one of them (meta-annotated with it)

The listened event is the method's `DomainEvent` parameter or, if it has none, the single `DomainEvent` the annotation
names by its `classes` or `value` attribute. The annotations are recognized by their name, so the mirror does not
depend on Spring.

```Java
public class OrderNotificationService implements ApplicationService {

    @ApplicationModuleListener
    public void on(OrderPlaced event) {
        ...
    }
}
```
