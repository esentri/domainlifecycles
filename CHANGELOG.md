# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
## [2.0.23] - 2024-04-02
- TEST

## [2.0.0] - 2024-04-02

### Added

#### dlc-access
- Completely new module, that replaces class and object functionality from dlc-core
- Added several facades to abstract from direct reflective access to classes and objects
    - nitrox.dlc.access.DlcAccess as static access provider for the following interfaces
    - nitrox.dlc.access.classes.ClassProvider
    - nitrox.dlc.access.object.EnumFactory
    - nitrox.dlc.access.object.IdentityFactory
    - nitrox.dlc.access.object.DefaultDomainObjectAccessFactory
    - nitrox.dlc.access.object.DynamicDomainObjectAccessor

#### dlc-builder
- New module, that replaces builder functionality from dlc-core
- Integrated new mirror from dlc-mirror

#### dlc-reflect
- New module, that provides simplified reflection access

#### dlc-types-utils
- New module, that replaces some dlc-core functionalities (especially base types, mirror based companions, entity cloner)

#### dlc-types
- New module, that now contains all basic domain type interfaces and annotations
- All Domain interface types moved package (from nitrox.dlc.domain.api to nitrox.dlc.domain.types)

### Changed

#### dlc-domain-diagrammer
- Little refactorings due to changes in dlc-mirror
- Added rendering for ReadModelProviders and OutboundServices
- Added TransitiveDomainTypeFilter option
- Added more configuration options

#### dlc-jackson
- Integrated new mirror from dlc-mirror (removed old mirror interface from DlcJacksonModule)
- nitrox.dlc.jackson.api.JacksonMappingCustomizer interface changed due to new mirror integration
- Integrated dlc-access

#### dlc-jooq
- Integrated new mirror from dlc-mirror (e.g. removed old mirror interface from nitrox.dlc.jooq.configuration.JooqDomainPersistenceConfiguration)
- nitrox.dlc.jooq.imp.matcher.JooqRecordPropertyMatcher replaces nitrox.dlc.jooq.imp.matcher.JooqRecordEntityPropertyMatcher and nitrox.dlc.jooq.imp.matcher.JooqRecordEntityValueObjectMatcher
- Integrated dlc-access

#### dlc-mirror
- Replaced reflection utils with dlc-reflect
- Integrated new types from dlc-types (breaking api changes)
- Extended some mirrors with additional meta information (DomainTypeMirror, FieldMirror, ...)
- Added own RuntimeException type nitrox.dlc.mirror.exception.MirrorException
- Extended domain type visitor with strict "visitTypesOnlyOnce" mode

#### dlc-persistence
- Replaced dlc-core dependency with dlc-types, dlc-type-utils, dlc-mirror, dlc-access, dlc-builder (breaking api changes, but most of them not relevant for dlc-jooq users)
- Added nitrox.dlc.persistence.mapping.RecordMapper interface to stay independent of nitrox.dlc.persistence.mapping.AbstractRecordMapper
- Added own RuntimeException type nitrox.dlc.persistence.exception.PersistenceException
- Refactored and unified auto record mapping in new implementation nitrox.dlc.persistence.mapping.AutoRecordMapper
- Removed any internal direct Java reflection usage

#### dlc-spring-doc
- Integrated new mirror from dlc-mirror 
- Integrated dlc-access

#### dlc-spring-doc-2
- Integrated new mirror from dlc-mirror
- Integrated dlc-access

#### dlc-spring-web
- Integrated dlc-types

#### dlc-spring-web-6
- Integrated dlc-types

#### dlc-swagger-v3
- Integrated dlc-types
- Integrated dlc-reflect

#### dlc-validation-extender
- Integrated dlc-types

### Removed

#### dlc-core
- removed complete dlc-core module with legacy mirror. Replaced by new modules (dlc-access, dlc-builder, dlc-types, dlc-type-utils, dlc-reflect) and the now fully integrated dlc-mirror

## [1.0.22] - 2023-10-25

### Changed
- last closed source development release



