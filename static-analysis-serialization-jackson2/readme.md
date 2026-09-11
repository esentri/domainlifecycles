# DLC Static Analysis Result Serialization (Jackson 2, legacy)

> **_NOTE:_** This module provides legacy support for [Jackson 2](https://github.com/FasterXML/jackson).
> `JacksonDomainCallsSerializer` is marked `@Deprecated`. For new projects, prefer the
> [Jackson 3 based module](../static-analysis-serialization-jackson3/readme.md).

This module provides Jackson 2 based JSON (de)serialization for a `DomainCalls` result of the
[DLC Static Analysis](../static-analysis/readme.md).

Since the analysis needs the compiled domain classes, it typically runs once, at build or test time.
This module lets that result be serialized there and deserialized again elsewhere - for instance by
an external tool that visualizes the domain - without dragging the bytecode analysis framework
(`static-analysis-sootup`) along to the consuming side.

## Dependencies

Gradle setup:

```Groovy
dependencies{
    implementation 'io.domainlifecycles:static-analysis-serialization-jackson2:3.2.0'
}
```

Maven setup:

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>static-analysis-serialization-jackson2</artifactId>
    <version>3.2.0</version>
</dependency>
```

This brings `io.domainlifecycles:static-analysis` along transitively.

## Usage

```Java
DomainCallsSerializer serializer = new JacksonDomainCallsSerializer();

String json = serializer.serialize(domainCalls);

// elsewhere, against a DomainMirror deserialized the same way (see mirror-serialization-jackson2)
// from the very domain the DomainCalls was analyzed against:
DomainCalls deserialized = serializer.deserialize(json, domainMirror);
```

`JacksonDomainCallsSerializer(boolean prettyPrint)` additionally lets you request indented output.

## Streaming

For a domain of a few hundred types the serialized `DomainCalls` can already reach several megabytes.
`serialize`/`deserialize` also come in stream based variants that write to, respectively read from, an
`OutputStream`/`InputStream` directly, without ever holding the complete JSON in memory as a single
String - useful when the JSON is itself compressed or sent over the network as it is produced or
consumed:

```Java
serializer.serialize(domainCalls, outputStream);

DomainCalls deserialized = serializer.deserialize(inputStream, domainMirror);
```

Neither method closes the given stream; that remains the caller's responsibility.

## How a `DomainMethod` is represented

A `DomainCalls` node (`DomainMethod`) is not serialized by embedding its `MethodMirror`: a mirror is
only meaningful wired into a fully initialized `DomainMirror` - it carries a back-reference used e.g.
by `getPublishedEvents()` and `getListenedEvent()`. Embedding it would therefore either duplicate
large parts of that `DomainMirror`'s own JSON representation, or produce a half-initialized mirror on
the reading side.

Instead, a `DomainMethod` is written as a compact reference - its owner type name, method name and
parameter type names, i.e. the information `DomainMethod#signature()` is built from:

```JSON
{
  "typeName": "yourdomain.order.OrderService",
  "methodName": "placeOrder",
  "parameterTypeNames": ["yourdomain.order.PlaceOrder"]
}
```

`deserialize(...)` resolves each such reference back against the `DomainMirror` passed to it. That
`DomainMirror` therefore has to be the one the serialized `DomainCalls` was analyzed against (or an
equally built one) - an unresolvable type or method reference fails deserialization with a
`DomainCallsSerializationException`.

The derived reverse index (`DomainCalls#callersOf(...)`) is not part of the JSON either; it is
rebuilt from the deserialized calls.
