# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.5.0] - 2026-09-25
- Extended the [mirror](./mirror) module to optionally also mirror classes in the scanned domain
  model packages that implement no domain marker interface, as `NonDomainTypeMirror`/
  `NonDomainTypeModel`, reflecting their fields and methods like any other type - no marker
  interface is required, classification only depends on a class living in a scanned package and
  not matching any recognized `DomainType`. Controlled via
  `AbstractDomainMirrorFactory#setIncludeNonDomainClasses` (default `true`, can be switched off).
  `ServiceKindMirror` gained `getReferencedNonDomainTypes()`, resolving the non-domain classes
  referenced by a service kind's fields, method parameters or return types
- Fixed two related bugs surfaced by the non-domain class mirroring above, both triggered by a
  Lombok `@SuperBuilder`-generated builder class (or any other self-referential/F-bounded generic,
  e.g. `<C extends X, B extends Builder<C, B>>`) now also being mirrored:
  - `AssertedContainableTypeMirrorBuilder` could produce a mirror whose domain type and type name
    described two different classes - the domain type came from reflection's erased bound (correct),
    while the type name came from a generic type resolver that, lacking a concrete usage context for
    such a self-referential type variable, fell back to `java.lang.Object`. Fixed by preferring the
    reflectively known, more specific type whenever the resolved generic type degrades to `Object`.
  - That mismatch made `MethodModel#getProcessedCommands()`/`getListenedEvent()`/`getPublishedEvents()`
    throw `MirrorException` for a command/event type name that could never resolve, which in turn
    made `DomainCallFlowAnalyzer`'s constructor fail for the *entire* domain model as soon as it
    contained such a builder anywhere - not just for a flow reaching it. These three methods now log
    a warning and skip an unresolvable reference instead of throwing, so a single inconsistent entry
    can no longer break flow analysis for an otherwise well-formed domain model
- The [domain diagrammer](./domain-diagrammer) can now render those non-domain classes as diagram
  nodes, restricted to the ones actually referenced by a service kind (domain service, application
  service, repository, query handler, outbound service or unspecified service kind), via the new
  `showNonDomainClasses` and `showNonDomainClassMethods` settings on `GeneralVisualSettings`
  (both `true` by default, so e.g. a controller's endpoint methods are visible out of the box),
  `showNonDomainClassFields` (`false` by default) and a configurable `nonDomainClassStyle` on
  `StyleSettings`
- `mirror-serialization-jackson2`/`jackson3` gained mixins to (de)serialize `NonDomainTypeModel`
- `NonDomainTypeMirror` gained `getReferencedServiceKinds()`, the inverse of
  `ServiceKindMirror#getReferencedNonDomainTypes()`: it resolves the service kinds a non-domain
  class itself references via a field, method parameter or return type - e.g. a controller or a
  message listener calling into an application service. The domain diagrammer now draws a
  non-domain class whenever either direction applies, with an edge pointing from the non-domain
  class to the service kind it calls
- The Gradle and Maven diagram plugins (`dlc-gradle-plugin`/`dlc-maven-plugin`, via the shared
  `dlc-plugins` `DiagramConfig`) now expose `showNonDomainClasses`, `showNonDomainClassFields`,
  `showNonDomainClassMethods` and `nonDomainClassStyle` as diagram configuration options, matching
  the diagrammer's `GeneralVisualSettings`/`StyleSettings`
- Fixed `ExtendedJMoleculesDomainMirrorFactory` (`mirror-jmolecules`), which the Gradle and Maven
  diagram plugins use internally, to also honor `setIncludeNonDomainClasses` - it previously always
  scanned with non-domain class mirroring disabled, regardless of the (default-on) setting,
  silencing the entire feature for every plugin-based diagram
- Fixed `KrokiDockerAdapter` (`dlc-plugins`, used by the Gradle/Maven `svg` diagram format) to pull
  the `yuzutech/kroki` Docker image first if it is not present locally, instead of only ever trying
  to create a container from it - previously the very first SVG diagram generation on a machine
  failed with a Docker "image not known" error, since `docker create` (unlike `docker run`) does not
  pull automatically
- Added backward flow filtering, the counterpart to the existing (forward) `flowFrom`/
  `includeFlowsFrom` restriction:
  - [static-analysis](./static-analysis)'s `FlowAnalyzer` gained `flowTo(DomainMethod)`,
    `flowTo(DomainEventMirror)`, `flowTo(DomainTypeMirror)` and
    `flowTo(String typeName, String methodName)`, answering "what leads into this" instead of
    "what does this lead to" - reversing `CALL`, `IMPLEMENTATION`, `EVENT_LISTEN` and
    `COMMAND_PROCESS` edges, plus, for an `AggregateRootMirror`/`ReadModelMirror`, structurally
    resolving the repository/query handler managing/providing it. There is deliberately no
    `flowTo(DomainCommandMirror)`, since nothing in the analyzed data leads into a command - a
    command can still appear as a reached leaf via reversed `COMMAND_PROCESS`
  - The [domain diagrammer](./domain-diagrammer)'s `DiagramTrimSettings` gained
    `includeFlowsTo`, the backward counterpart of `includeFlowsFrom`, using the same entry syntax
    (except a domain command cannot be a target - `IllegalArgumentException` if attempted). Both
    settings can be combined; their reached classes are united. The direction diagram edges are
    drawn in is unaffected either way, since it always follows the structural mirror data, not the
    flow search direction
  - The Gradle and Maven diagram plugins (via the shared `dlc-plugins` `DiagramConfig`) now expose
    `includeFlowsTo` as a diagram configuration option, mirroring `includeFlowsFrom` exactly,
    including triggering the static analysis whenever either is configured
- Added persistence support for `List<Identity>` and `List<Enum>` fields (1-n relations) on aggregate roots, entities and value objects - insert, load, update and delete, including duplicate values and arbitrary identity value types (e.g. `UUID`). Implemented via a new internal `ScalarListElement` carrier reusing the existing value-object list machinery, so persistence of aggregates without such fields is unaffected.
- Added the [Transaction Cache](./persistence/readme.md#transaction-cache) feature: a per-transaction read cache of previously fetched Aggregate roots, avoiding a redundant `SELECT` when `update()`/`deleteById()` need the currently persisted state to detect changes and the same Aggregate was already loaded earlier in the same transaction. Enabled by default, configurable via `withTransactionCacheEnabled(...)`/`withTransactionCacheProvider(...)`/`withTransactionCacheMaxSize(...)` on the persistence configuration builder.
- Added new [`persistence-spring-tx`](./persistence-spring-tx/readme.md) module with `SpringTransactionCacheBinder`, binding the Transaction Cache to Spring-managed (`@Transactional`) transactions - jOOQ's own `TransactionListener` is bypassed by a transaction Spring itself began and commits/rolls back, which silently left the cache inactive on the primary Spring Boot integration path. Wired into `DlcJooqPersistenceAutoConfiguration` (Spring Boot 3 and 4) automatically via a new `SpringTransactionCacheAwareConnectionProvider` in `jooq-integration`.
- Fixed `TransactionCacheJooqBinder` registration being tied to `JooqAggregateRepository`'s constructor: any access path building on `JooqAggregateFetcher` directly (or a future repository not going through that constructor) silently never got the Transaction Cache wired in. Now registered centrally, once, by `JooqDomainPersistenceProvider` itself via a new `JooqDomainPersistenceProvider(JooqDomainPersistenceConfiguration, DSLContext)` constructor overload.
- Added `DlcJdbcPersistenceAutoConfiguration` (Spring Boot 3 and 4 autoconfig modules), the plain-JDBC (`jdbc-integration`) counterpart of `DlcJooqPersistenceAutoConfiguration` - reads the database schema once at startup instead of relying on generated record classes, resolves a `JdbcDialect` from the same SQL dialect property/attribute jOOQ uses, and wires the Transaction Cache to Spring's own transaction management by default via the same `persistence-spring-tx` binder, with nothing to configure by hand. Deliberately ordered after `DlcJooqPersistenceAutoConfiguration`, so that if both integrations are ever on the classpath at once, jOOQ deterministically wins the shared persistence-provider slot rather than leaving that to an accident of autoconfiguration sorting.
- Fixed `EntityCloner.cloneEntityProperties()` sharing a mutable basic-typed collection/array field with the original entity instead of deep-copying it - could silently corrupt the Transaction Cache's cloned snapshot (or any other code cloning an entity) if the original's field was later mutated in place.
- Fixed `EntityCloner` throwing `IllegalArgumentException` when resolving a cyclic `Optional<Entity>` back-reference, so a self- or mutually-referencing entity graph with such a field could not be cloned at all.
- Fixed `JooqValueObjectIdProvider.isUuidCompatible` over-broadly treating any String- or `byte[]`-typed technical id column as UUID-compatible: a `VARCHAR`/`BINARY` primary key meant to hold a sequence-derived or externally-supplied natural key could silently get a random UUID written into it instead. The check now also considers the column's declared length (mirroring the existing read-side heuristic); a column that is neither UUID- nor long-compatible for its length now throws instead of silently corrupting the key.
- Fixed `BaseValueObjectIdProvider.resolveContainerTechId()` silently misattributing a new value object's technical id to the wrong (grand-)container when an insertion-ordering bug left an intermediate, record-mapped ancestor without a record yet - now raised loudly as an insertion-ordering error instead.
- Fixed a bounds bug in `jooq-integration`'s `NamingUtil.snakeCaseToCamelCase()` that threw `StringIndexOutOfBoundsException` for a column name ending in an underscore, and silently left a stray underscore in the mapped property name for a column name with two consecutive underscores.
- Fixed `DlcJooqPersistenceAutoConfiguration` and `DlcJacksonAutoConfiguration` (`dlc-spring-boot-autoconfig`) silently never running their own `@Bean` methods under Spring Boot 4.1+: their `@AutoConfiguration`/`@AutoConfigureBefore` ordering referenced `DataSourceAutoConfiguration`/`JooqAutoConfiguration`/`JacksonAutoConfiguration` by their pre-Spring-Boot-4 package, which Spring Boot 4 moved into dedicated modules - a stale name silently drops the ordering guarantee instead of failing, so Spring Boot's own, unconfigured jOOQ wiring silently took over. Also fixed a property-key typo in `DlcEnvironmentPostProcessor` (`dlc.feaures...` instead of `dlc.features.persistence.sql-dialect`) that this exposed, since it had until then been masked by the same ordering bug.
- Added `Byte<->Short` and `Boolean<->Short` default `TypeConverter`s, needed for SQL Server's more aggressive mapping of zero-scale `NUMERIC`/`DECIMAL` columns to `Short`/`Byte`.
- Fixed `javadoc` generation failing for `dlc-spring-boot-autoconfig`, `dlc-spring-boot3-autoconfig` and `jdbc-integration`: a class-level `{@link #member}` referenced a method declared on a nested class (or, in `jdbc-integration`'s case, a default method inherited from a generic interface with an unresolvable type-variable signature), which javadoc cannot resolve from the enclosing class. Publishing the javadoc jar for these three modules previously failed silently as part of a larger multi-module build unless run in isolation.
- Fixed two copy-pasted typos in the missing-SQL-dialect error message thrown by `DlcJooqPersistenceAutoConfiguration` (`dlc-spring-boot-autoconfig`/`dlc-spring-boot3-autoconfig`), each module carrying a different wrong property name (`dlc.persistence.sql-dialect` / `dlc.features-persistence.sql-dialect` instead of `dlc.features.persistence.sql-dialect`), misleading a developer debugging exactly the misconfiguration this message is meant to explain.
- Fixed `jdbc-integration`'s `NamingUtil.camelCaseToSnakeCase()` silently diverging from `jooq-integration`'s: a digit run adjacent to a letter (e.g. in the `Identity` class `OrderIdBv3`) was split into its own segment (`order_id_bv_3`) instead of staying glued to the preceding word segment (`order_id_bv3`) as `jooq-integration` already does - both integrations derive a database sequence name from an `Identity` class's simple name this way, and the real sequence in the shared test migration schema (`order_id_bv3_seq`) is glued together, so `jdbc-integration`'s divergence was an unnoticed latent bug (never exercised by its own test suite) that would fail sequence-based id generation for any digit-suffixed `Identity` class name in a real project.
- Added `dlc-spring-boot3-autoconfig` test coverage for `DlcJdbcPersistenceAutoConfiguration` and for disabling either persistence backend by property (`dlc.features.persistence.jooq.enabled=false`/`dlc.features.persistence.jdbc.enabled=false`) - this Spring Boot 3 variant had none, unlike its Spring Boot 4 counterpart.
- Added further Transaction Cache test coverage: a dedicated unit test for both `SpringTransactionCacheAwareConnectionProvider` implementations (jOOQ/JDBC) - previously untested despite being the central glue of the Spring-native wiring path; a `jdbc-integration` counterpart of `jooq-integration`'s `SimpleAggregateRootRepository_TransactionCache_ITest`, exercising the cache-miss/cache-hit invalidation rule and the SELECT-avoidance behavior through `jdbc-integration`'s own, independent repository/fetcher code path; a rollback-path test for `SpringTransactionCacheBinder` (only commit was covered before); and a genuine multi-threaded concurrency test for `ThreadBoundTransactionCacheProvider`, proving its `ThreadLocal`-based isolation under real concurrent access rather than only sequential single-thread calls.

- Non-domain classes generated by jOOQ are no longer mirrored by default: the
  [mirror](./mirror/readme.md#leaving-generated-code-out) leaves out every class whose superclass or
  interfaces (direct or inherited) lie within `org.jooq` - jOOQ's table, record, schema and catalog
  classes, which declare hundreds to thousands of methods each. In a real world project they made up
  95 % of the mirrored non-domain classes' size, and - since calls from and to classes that are not
  mirrored are not recorded - 87 % of the static analysis' call sites. Configurable via the new
  `NonDomainClassFilter`, `AbstractDomainMirrorFactory#setNonDomainExcludedSupertypePackages`
  (default `org.jooq`, an empty list switches it off) and `#setNonDomainExcludedPackages` (whole
  packages, default none), and via the new `nonDomainExcludedSupertypePackages`/
  `nonDomainExcludedPackages` options of all [Gradle and Maven plugin](./dlc-plugins/readme.md#leaving-generated-code-out)
  goals/tasks building a domain mirror (diagram, mirror serialization, Diagram-Viewer upload). In the
  plugins an empty list of excluded supertype packages keeps the default, since Maven injects an empty
  list for an unconfigured list parameter

- Fixed building and deserializing `DomainCalls` (static analysis result) scaling quadratically with the
  number of call sites per calling method: `DomainCalls.Builder` deduplicated call sites with a linear
  `List.contains` per added call site - each a deep `MethodMirror` comparison - and is hash based now;
  `JacksonDomainCallsSerializer` (Jackson 2 and 3) resolved every call site anew against the domain mirror
  (scanning all methods of its type and creating a new `DomainMethod` each time) and now resolves each
  distinct method once, sharing its `DomainMethod`. In a real world project (8.2 million call sites, callers
  with more than 12,000 call sites) deserialization took 283 s and 2.4 GB heap; a prototype of the fix took
  15 s and 0.9 GB. The static analysis in the build plugins benefits as well, since it uses the same builder
- The Diagram-Viewer upload of the [Gradle and Maven plugins](./dlc-plugins/readme.md) has a configurable
  request timeout: `uploadRequestTimeoutMinutes` (default 5, as fixed before), for very large domain models
- Fixed the [domain diagrammer](./domain-diagrammer) spending most of the generation of large diagrams in
  deciding which non-domain classes to show: `DiagramSettingsFilter` resolved the non-domain classes
  referenced by every service kind anew for each non-domain class it checked (quadratic). It now resolves
  them once per filter. In a real world model (4,771 types) generating a flow restricted diagram went from
  about 7 s to 0.2 s, the whole model from 11 s to 3.5 s
- Raised the default size of the static analysis class cache (`SootupStaticAnalyzer.DEFAULT_CACHE_SIZE`, and with
  it the `staticAnalysisCacheSize` default of the [Gradle and Maven plugins](./dlc-plugins/readme.md)) from 500 to
  5000. The cache only holds classes actually parsed, so smaller projects are unaffected; in a real world project
  (4,771 mirrored types) the analysis got about 13 % faster without a measurable increase of the heap needed
- The [mirror](./mirror) module can now derive Bounded Context boundaries directly from the code
  instead of requiring `AbstractDomainMirrorFactory#setBoundedContextPackages(String...)` to be called
  manually: a Bounded Context's root package can be marked with the new
  `@io.domainlifecycles.domain.types.BoundedContext` package annotation (with an optional
  human-readable name, exposed via the new `BoundedContextMirror#getName()`). An explicit
  `setBoundedContextPackages(...)` call still takes precedence over derived Bounded Contexts, which in
  turn take precedence over the previous default fallback (the whole scanned domain model as one
  Bounded Context). Nested/overlapping Bounded Context packages - derived or explicitly configured -
  are now rejected with a `MirrorException`. [mirror-jmolecules](./mirror-jmolecules) additionally
  recognizes jMolecules' own, structurally equivalent `@org.jmolecules.ddd.annotation.BoundedContext`
  package annotation

- Fixed flow traversals of the [static analysis](./static-analysis) (`DomainCallFlowAnalyzer`, used for the flow
  filters of the domain diagrammer) allocating gigabytes for large flows: checking each reached node against its path
  rebuilt the key of every step on the path, including a method's full signature. `Step#hasNodeKey(String)` now
  compares without building the key. In a real world project a flow diagram of 700 classes allocated 1.8 instead of
  6.9 GB and rendered in 1.3 instead of 2.1 s

## [3.4.0] - 2026-09-11
- Improved DLC persistence initialization performance
- Fixed auto record mapping of array typed fields (e.g. `byte[]`): the mirror reports the component type for arrays, which made the mapper look up a converter (`[B` -> `java.lang.Byte`) that could never be served. Added `AssertedContainableTypeMirror#getBinaryTypeName()` and used it for type resolution in `AutoRecordMapper` and `AutoMapperNestedValueObjectAccessor`.
- Added [static analysis](./static-analysis) module, answering which domain methods are called from
  which other domain methods, based on the DomainMirror and a compiled classpath. It carries the
  `StaticAnalyzer` abstraction, the result model and the flow analysis, and depends on nothing but
  the mirror
- Added [static analysis sootup](./static-analysis-sootup) module holding the SootUp based
  `StaticAnalyzer` implementation. Kept as a module of its own so that reading an analysis result -
  for instance to filter a diagram - does not drag a bytecode analysis framework onto the classpath
- The analyzer reads the invoke instructions directly out of the bodies of the mirrored methods
  instead of building a global call graph, so only the inspected bodies are translated and the
  analysis stays confined to the domain
- Resolves lambdas (also nested ones), method references (bound, unbound and static), private
  helper methods, overloads, generic methods with compiler generated bridge methods and inherited
  methods; recursive and mutually recursive calls terminate
- Calls are resolved once per concrete owner: a `this` dispatch inside an inherited body is
  attributed to the overriding subtype, while `super.x()` stays attributed to the base class
- Default methods of mirrored interfaces are analyzed for every implementation not overriding them
- `DomainCalls` holds the immutable result and indexes both directions (`callsFor` and
  `callersOf`). Its nodes combine the concrete owner type with the mirrored method, its edges carry
  the type whose body contained the call and the source line
- `Diagnostic` reports what could not be analyzed - most importantly a mirrored type missing from
  the classpath - so that an empty result can be told apart from an unanalyzable one; `isComplete()`
  summarizes whether the result can be trusted
- `DomainClasspath` derives the classpath to analyze from the mirror, from given types or from a
  classpath string
- Added `FlowAnalyzer`, answering which domain types and methods are reachable from a given method,
  domain event or domain command, by joining the analyzed calls with the event and command
  information of the mirror
- A flow follows four kinds of edges: method calls, dispatch into implementations and overrides,
  published domain events together with the methods listening to them, and the methods processing a
  domain command. Several listeners of one event branch the flow into independent continuations
- Flows are traversed breadth first and stay bounded: every node is expanded at most once, cycles -
  including those closing over a published event - are reported and not followed, and a configurable
  maximum depth reports truncation
- `FlowConfig` allows limiting the depth, switching off event or implementation edges and filtering
  methods (e.g. excluding accessors); a flow renders as an indented tree
- The [domain diagrammer](./domain-diagrammer) optionally takes such an analysis result and can
  reduce a diagram to the classes taking part in one flow, via the new `includeFlowsFrom` trim
  setting - starting at a domain command, a domain event, a single method or a whole type. The
  restriction is combined with the existing trim settings and can only narrow what they allow
- The Gradle and Maven diagram plugins now support flow-based diagram filtering too: the new
  `includeFlowsFrom`, `flowMaxDepth`, `flowFollowEvents`, `flowFollowImplementations` and
  `flowExcludeAccessors` diagram options mirror the diagrammer's flow settings. Whenever
  `includeFlowsFrom` is configured, the plugin runs the SootUp based static analysis on the
  project's compiled classes as part of diagram generation
- Added [static analysis serialization Jackson 3](./static-analysis-serialization-jackson3) and
  [Jackson 2](./static-analysis-serialization-jackson2) modules, JSON (de)serializing a `DomainCalls`
  analysis result so it can be produced once (e.g. in a build step) and consumed elsewhere - for
  instance by an external diagram viewer tool - without re-running the static analysis. A
  `DomainMethod` is written as a compact type/method/parameter-types reference rather than an
  embedded mirror, and resolved back against a `DomainMirror` given at deserialization time
- The Gradle and Maven `domainModelUpload` task/goal now optionally (`runStaticAnalysis`, default
  `true`) also run a static analysis of the compiled domain classes and upload its result
  (`DomainCalls`) alongside the domain model, so a Diagram Viewer can offer flow based diagram
  filtering. `DomainModelUploader` now builds the domain model itself instead of receiving an
  already-serialized JSON string, so the very same `DomainMirror` is reused for both the mirror and
  the static analysis rather than building it twice
- The domain model upload request is now gzip-compressed (`Content-Encoding: gzip`) before being
  sent, since the combined domain model and static analysis JSON can reach the tens of megabytes for
  larger domains; a 10 second connect timeout and a 5 minute overall request timeout were added so an
  unreachable or slow Diagram Viewer fails the build instead of hanging it indefinitely
- `DomainSerializer` (`mirror-serialization-jackson3`/`jackson2`) and `DomainCallsSerializer`
  (`static-analysis-serialization-jackson3`/`jackson2`) gained stream based `serialize`/`deserialize`
  overloads (`OutputStream`/`InputStream`), so a `DomainMirror`/`DomainCalls` can be written to or read
  from a stream directly, without ever holding the complete serialized JSON in memory as a single
  String - a building block towards streaming the domain model upload itself. Both also had Jackson's
  default of closing the given stream once done turned off, to actually honor that contract
- `DomainModelUploader` gained `uploadDomainModelStreaming`, an opt-in alternative to
  `uploadDomainModel` (wired up via the new `streamUpload` option on the Gradle/Maven
  `domainModelUpload` task/goal, default `false`) that streams the gzip-compressed request body
  directly into the HTTP request as it is produced - via a background thread and a bounded
  producer/consumer queue - instead of assembling it completely in memory first. For very large
  domains this trades a flatter memory footprint for the added complexity of a background writer.
  `DomainModelUploaderImpl` also now reuses a single `HttpClient` (previously one was created per
  upload call), avoiding lingering non-daemon client threads
- `SootupStaticAnalyzer` now builds its `JavaView` on a bounded, LRU-evicting class cache instead of
  SootUp's unbounded default, capping memory usage regardless of how many distinct classes (domain,
  JDK or library) end up being touched while resolving method bodies. The cache size defaults to
  `SootupStaticAnalyzer.DEFAULT_CACHE_SIZE` (500) and can be configured via the new
  `SootupStaticAnalyzer(int cacheSize)` constructor; evicted classes are simply re-parsed on demand,
  so a smaller cache only trades CPU for a lower memory ceiling and does not affect analysis results
- This cache size is now also configurable through the plugin layer: `DomainCallsAnalyzerImpl`,
  `DiagramGeneratorImpl` and `DomainModelUploaderImpl` (dlc-plugins) gained a constructor overload
  taking it, and the Gradle `diagram`/`domainModelUpload` task configurations and the Maven
  `createDiagram`/`domainModelUpload` goals gained a `staticAnalysisCacheSize` option (default `500`)
  wherever they can trigger the static analysis
- The static analysis run by the Gradle/Maven plugins no longer scans the whole project classpath:
  `StaticAnalyzer#analyze`, `DomainCallsAnalyzer#analyze` and `SootupStaticAnalyzer` gained an
  `analyzedPackages` parameter restricting which classes are considered (a package itself or any of
  its sub-packages), defaulting to no restriction when empty. `SootupStaticAnalyzer` enforces this by
  wrapping each classpath entry in a `PackageScopedAnalysisInputLocation` that filters SootUp's bulk
  class enumeration (used to build the type hierarchy) to those packages, while leaving by-name type
  lookups (needed to resolve framework/library base types a domain class extends) unrestricted - this
  is what previously forced every class reachable from the classpath, JDK and third-party libraries
  included, to be resolved just to compute the type hierarchy. The Gradle `diagram`/`domainModelUpload`
  task configurations and the Maven `createDiagram`/`domainModelUpload` goals gained a matching
  `staticAnalysisPackages` option, defaulting to the already-configured `domainModelPackages` when
  unset. A concrete implementation of a mirrored domain interface living outside the analyzed packages
  (e.g. in a separate infrastructure package) is not found, the same as if it were simply missing from
  the classpath, so `staticAnalysisPackages` should be widened to cover such packages when needed

## [3.3.0] - 2026-07-24
- Extending persistence support for UUID based identities
- Fixed DLC plugins to remove the need for externally provided JMolecules lib, if not used in the project

## [3.2.0] - 2026-06-16
- Added support for JMolecules DDD types for DLC mirror, now able to render Domain diagrams using JMolecules marker interfaces or annotations
- Upgraded several libraries, minor version upgrades
- Supporting Gruelbox Transaction Outbox 7.0.707
- Extended Maven and Gradle plugins to support multi-module projects

## [3.1.0] - 2026-03-24
- Added Domain diagram relation stereotypes
- Extended Diagrammer and plugins to hide relation labels or stereotypes
- Gradle Plugin serialize Mirror fixed
- Generally upgraded to Gradle 9.4.0 
- Some smaller readme fixes

## [3.0.0] - 2026-02-27
- Upgraded all Spring Boot and Spring dependencies to SpringBoot version 4.0.x and compatible versions
- Refactored Spring Boot AutoConfiguration to use Spring Boot 4.0.x features
- Added Spring Boot 3 AutoConfiguration for legacy support
- Added integration for Spring Event Bus supported DomainEvents (and Spring Modulith events)
- Added support for Jackson 3.x (mirror serialization, domain event serialization, general DLC Jackson 3 integration)
- Provided fallback support for Jackson 2.x (mirror serialization, domain event serialization, general DLC Jackson 2 integration)
- Refactored Spring Web integration to be independent of Jackson serialization
- Added @DomainEventListener annotation for DomainEvents. @ListensTo annotation is deprecated.
- Fixed jOOQ test class generation
- AutoConfig can load mirror from `META-INF/dlc/mirror.json` without reflection at runtime.
- DLC Build plugins provide support for deserializing mirror to ``META-INF/dlc/mirror.json``

## [2.6.0] - 2025-12-02
- sample-project is not part of the main project anymore, but is now a separate project
- Domain Diagramming documentation extended
- Diagrammer default configuration: 'ApplicationService' is default stereotype, instead of 'Driver'
- KrokiContainer default port changed to 8501 to avoid conflicts with other Kroki containers running on the same machine 
- integrated gradle plugin into sample-project to demonstrate usage of DLC plugin diagram generation 

## [2.5.0] - 2025-10-31
- Upgraded all Spring Boot and Spring dependencies to SpringBoot version 3.5 and compatible versions
- Extended default implementation of Entities and ValueObjects companion classes with 
  pure reflective fallbacks for 'equals()', 'hashCode()', 'toString()'. That also enables 
  the corresponding base classes to use 'equals()', 'hashCode()', 'toString()' without having 
  the mirror initialized.
- Added feature for external comments being rendered in Domain Diagramms.
- Fixed bugs in Domain Diagrams due to missing classes not being rendered in certain inheritance cases.
- Removed deprecated Spring Boot 2 support
- Removed deprecated javax BeanValidation support
- OpenAPI integration now provides OpenAPI 3.1 support
- Bugfix in ValidationDomainClassExtender (was conflicting with other byte code extensions - Jacoco)
- Tested support for Spring Boot 3.5.5
- Added DLC SpringBoot starter and AutoConfiguration for Spring Boot 3.5.5
- Extended Open API configuration Options
- OpenAPI Nullabillity support for Open API 3.0 and 3.1

## [2.4.1] - 2025-06-06
- Added deprecated markers at some classes
- Refactored and extended documentation
- Fixed config setting in Gradle plugin for DomainModel Upload

## [2.4.0] - 2025-05-30
- Refactored and extended Domain diagrammer options for showing inheritance structures
- Adjusted plugin diagram options for showing inheritance structures

## [2.3.0] - 2025-05-27
- Refactored and extended Domain diagrammer with new diagram settings (e.g. options for connection based filtering)
- Added [Maven plugin](./dlc-maven-plugin) providing DLC plugin functions via Maven
- Added [Gradle plugin](./dlc-gradle-plugin) providing DLC plugin functions via Gradle
- Added [DLC plugins](./dlc-plugins) containing the general plugin logic
  (exporting DomainModels, draw Domain diagrams, sending updated DomainModel to DLC Diagram Viewer application, 
  that enables rendering and documenting DomainModels)
- Changed Mirror added DomainMirror interface as the primary mirror containing the complete domain model
- Added completeness check for Domain Mirror ensuring all references to model elements within the domain model can be resolved, 
  when a DomainMirror instance is created either by reflection or by deserialization from a JSON export
- Extended domain diagrammer for filtering options (abstract types and packages) and fixed several exisiting filtering options 

## [2.2.1] - 2025-02-28
- Replaced some critical stream operations in "io.domainlifecycles.persistence.repositoryDomainStructureAwareRepository"

## [2.2.0] - 2025-02-26
- Added shutdown hooks for Gruelbox Domain Event channels, to avoid irritating exceptions for example in tests
- Gruelbox based Domain Event handling now provides distinct outbox entries for each Domain Event Consumer, which
  allows now consumer specific retry behaviour, that can be managed using natural Gruelbox features 
- DLC spring-tx-outbox deprecated for future removal

## [2.1.0] - 2025-02-21
- Introduce version catalog for dependency management
- Replace nu.studer.jooq generator with jOOQ native generator
- Upgrade Flyway Plugin to version 10 and modify usage accordingly
- Replace version in readme.md with version defined in version.properties with `gradle build`
- Upgrade Gruelbox (Domain Events outbox feature) dependency to version 6.0.553
- Renamed "io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory" to "io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory"
- Renamed "io.domainlifecycles.mirror.api.InitializedDomain" to "io.domainlifecycles.mirror.model.DomainModel"
- An instance of "io.domainlifecycles.mirror.resolver.GenericTypeResolver" can only be applied via "io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory" 
- Fixed serialization bugs with "io.domainlifecycles.mirror.model.DomainModel"
- Removed static "io.domainlifecycles.mirror.api.Domain" dependency from Domain Diagrammer

## [2.0.4] - 2025-02-06
- Bytebuddy (ValidationDomainClassExtender) must not try to extend static methods

## [2.0.3] - 2025-01-29
- Bugfix for proper handling of ValueObject duplicates in lists

## [2.0.2] - 2025-01-27

- Fixed deserialization error for the mirror
- Extended diagrammer with background config option 

## [2.0.1] - 2024-11-19

- Fixed misleading error messages in specialized automapping constellations

## [2.0.0] - 2024-11-15

### Added

#### domain-events-core

- First draft of a new module supporting Domain Events, in a way where domain event operations
  and the technical message processing are separated

#### domain-events-activemq-classic-5

- Domain Event support for Active MQ Classic 5

#### domain-events-gruelbox

- Domain Event support for Gruelbox Transactional Outbox

#### domain-events-jakarta-jms

- Domain Event support for Jakarta JMS

#### domain-events-mq

- Domain Event support for Java Messaging (abstract)
- used by domain-events-jakarta-jms and domain-events-activemq-classic-5

#### domain-events-spring-tx

- Spring transaction support for Domain Event handling

#### domain-events-spring-tx-outbox

- Spring based DLC specific outbox implementation (experimental)

#### service-registry

- First draft of a DLC specific service registry, that allows to accessing services in dynamic way at runtime 

#### access

- Completely new module, that replaces class and object functionality from dlc-core
- Added several facades to abstract from direct reflective access to classes and objects
    - io.domainlifecycles.access.DlcAccess as static access provider for the following interfaces
    - io.domainlifecycles.access.classes.ClassProvider
    - io.domainlifecycles.access.object.EnumFactory
    - io.domainlifecycles.access.object.IdentityFactory
    - io.domainlifecycles.access.object.DefaultDomainObjectAccessFactory
    - io.domainlifecycles.access.object.DynamicDomainObjectAccessor

#### builder

- New module, that replaces builder functionality from dlc-core
- Integrated new mirror from dlc-mirror

#### reflect

- New module, that provides simplified reflection access

#### types-utils

- New module, that replaces some dlc-core functionalities (especially base types, mirror based companions, entity
  cloner)

#### types

- New module, that now contains all basic domain type interfaces and annotations
- All Domain interface types moved package (from io.domainlifecycles.domain.api to io.domainlifecycles.domain.types)

### Changed

#### domain-diagrammer

- Little refactorings due to changes in dlc-mirror
- Added rendering for QueryHandlers and OutboundServices
- Added TransitiveDomainTypeFilter option
- Added more configuration options

#### jackson-integration

- Integrated new mirror from dlc-mirror (removed old mirror interface from DlcJacksonModule)
- io.domainlifecycles.jackson3.api.JacksonMappingCustomizer interface changed due to new mirror integration
- Integrated access module

#### jooq-integration

- Integrated new mirror from mirror module (e.g. removed old mirror interface from
  io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration)
- io.domainlifecycles.jooq.imp.matcher.JooqRecordPropertyMatcher replaces
  io.domainlifecycles.jooq.imp.matcher.JooqRecordEntityPropertyMatcher and
  io.domainlifecycles.jooq.imp.matcher.JooqRecordEntityValueObjectMatcher
- Integrated access module

#### mirror

- Replaced reflection utils with reflect module
- Integrated new types from types module (breaking api changes)
- Extended some mirrors with additional meta information (DomainTypeMirror, FieldMirror, ...)
- Added own RuntimeException type io.domainlifecycles.mirror.exception.MirrorException
- Extended domain type visitor with strict "visitTypesOnlyOnce" mode

#### persistence

- Replaced dlc-core dependency with types, type-utils, mirror, access and builder modules (breaking api changes, but
  most of them not relevant for jooq-integration users)
- Added io.domainlifecycles.persistence.mapping.RecordMapper interface to stay independent of
  io.domainlifecycles.persistence.mapping.AbstractRecordMapper
- Added own RuntimeException type io.domainlifecycles.persistence.exception.PersistenceException
- Refactored and unified auto record mapping in new implementation
  io.domainlifecycles.persistence.mapping.AutoRecordMapper
- Removed any internal direct Java reflection usage

#### spring-doc-integration

- Integrated new mirror from mirror module
- Integrated access module

#### spring-doc-2-integration

- Integrated new mirror from mirror module
- Integrated access module

#### spring-web-integration

- Integrated types module

#### spring-web-6-integration

- Integrated types module

#### swagger-v3-integration

- Integrated types module
- Integrated reflect module

#### validation-extender

- Integrated types module

### Removed

#### dlc-core

- removed complete dlc-core module with legacy mirror. Replaced by new modules (access, builder, types, type-utils,
  reflect) and the now fully integrated mirror module

## [1.0.22] - 2023-10-25

### Changed

- last closed source development release



