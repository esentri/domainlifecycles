# The Domain Mirror

The DLC Domain Mirror is a DDD specific implementation of the ideas of Gilad Bracha and David Ungar, who described
[Design Principles for Meta-level Facilities of Object-Oriented Programming Languages](https://bracha.org/mirrors.pdf)

The DLC Domain Mirror contains a metamodel of the tactical design structures within bounded contexts. This metamodel is
typically
instantiated at application startup using Java reflection. It mirrors the current implementation state of the
implemented
DDD building blocks. This metamodel information is used by several DLC modules at runtime to derive "DDD specific"
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
around that problem by
setting a special type resolver (`io.domainlifecycles.mirror.resolver.TypeMetaResolver`), that does deep type resolving.

```Java
public class ShopApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory(new TypeMetaResolver(), "sampleshop"));
    }

    public static void main(String[] args) {
        ...
    }
}
```

This is especially useful for rendering the most concrete type information
using [DLC Domain Diagrams](./readme_diagrammer.md).


