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

The [DLC Domain Diagrammer](../domain-diagrammer/readme.md#showing-non-domain-classes) builds on
this to optionally draw the non-domain classes referenced by a service in a diagram.


