# The DLC Mirror jMolecules Extension

This module extends the [DLC Domain Mirror](../mirror/readme.md) with support for [jMolecules](https://github.com/xmolecules/jmolecules)
DDD annotations and interfaces, allowing projects that use jMolecules to participate fully in DLC's metamodel.

## What it does

The standard DLC Domain Mirror recognizes domain building blocks (Aggregate Roots, Entities, Value Objects, etc.)
through DLC's own type system (`io.domainlifecycles.domain.types.*`). This extension adds recognition of the
equivalent jMolecules constructs so that classes marked with jMolecules annotations or implementing jMolecules
interfaces are treated as first-class DLC domain types.

### Supported jMolecules constructs

**Annotations** (`org.jmolecules.ddd.annotation.*` and `org.jmolecules.event.annotation.*`):

| jMolecules annotation | DLC domain type |
|---|---|
| `@AggregateRoot` | `AGGREGATE_ROOT` |
| `@Entity` | `ENTITY` |
| `@ValueObject` | `VALUE_OBJECT` |
| `@Repository` | `REPOSITORY` |
| `@Service` | `DOMAIN_SERVICE` |
| `@DomainEvent` | `DOMAIN_EVENT` |

**Interfaces** (`org.jmolecules.ddd.types.*` and `org.jmolecules.event.types.*`):

| jMolecules interface | DLC domain type |
|---|---|
| `AggregateRoot<A, ID>` | `AGGREGATE_ROOT` |
| `Entity<A, ID>` | `ENTITY` |
| `ValueObject` | `VALUE_OBJECT` |
| `Repository<A, ID>` | `REPOSITORY` |
| `Identifier` | `IDENTITY` |
| `DomainEvent` | `DOMAIN_EVENT` |

### Mixed-model support

Projects can freely mix DLC native types and jMolecules types in the same bounded context. For example, an
`ApplicationService` may implement DLC's `io.domainlifecycles.domain.types.ApplicationService` while an
`AggregateRoot` in the same package implements `org.jmolecules.ddd.types.AggregateRoot`. Both will be correctly
recognized and included in the domain mirror.

## Key classes

- **`ExtendedJMoleculesDomainMirrorFactory`** — drop-in replacement for `ReflectiveDomainMirrorFactory`; scans for both
  DLC and jMolecules domain types.
- **`ExtendedJMoleculesDomainTypeDetector`** — extends the default detector with jMolecules interface and annotation checks.
- **`ExtendedJMoleculesDomainTypesScanner`** — extends the classpath scanner to also pick up classes identified via
  jMolecules annotations and interfaces.

## Domain Mirror initialization

Initialize the mirror with `JMoleculesDomainMirrorFactory` instead of the standard `ReflectiveDomainMirrorFactory`:

```Java
public class ShopApplication {

    static {
        Domain.initialize(new ExtendedJMoleculesDomainMirrorFactory("sampleshop"));
    }

    public static void main(String[] args) {
        ...
    }
}
```

ATTENTION: If generics and deeper nested inheritance structures are used, the default initialization of the mirror as
described above does sometimes not provide all necessary type information (because of Java's type erasure). DLC provides
a way to work around that problem by setting a special type resolver
(`io.domainlifecycles.mirror.resolver.TypeMetaResolver`), that does deep type resolving.

```Java
public class ShopApplication {

    static {
        Domain.initialize(new ExtendedJMoleculesDomainMirrorFactory(new TypeMetaResolver(), "sampleshop"));
    }

    public static void main(String[] args) {
        ...
    }
}
```

This is especially useful for rendering the most concrete type information
using [DLC Domain Diagrams](../domain-diagrammer/readme.md).


