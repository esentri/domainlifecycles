/*
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.jooq.configuration;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jooq.configuration.def.JooqRecordClassProvider;
import io.domainlifecycles.jooq.configuration.def.JooqRecordMirrorInstanceProvider;
import io.domainlifecycles.jooq.configuration.def.JooqRecordPropertyAccessor;
import io.domainlifecycles.jooq.configuration.def.JooqRecordPropertyProvider;
import io.domainlifecycles.jooq.imp.matcher.JooqRecordPropertyMatcher;
import io.domainlifecycles.jooq.imp.matcher.JooqRecordTypeToEntityTypeMatcher;
import io.domainlifecycles.persistence.configuration.DomainPersistenceConfiguration;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.IgnoredFieldProvider;
import io.domainlifecycles.persistence.mapping.IgnoredRecordPropertyProvider;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mapping.RecordPropertyMatcher;
import io.domainlifecycles.persistence.mapping.converter.TypeConverter;
import io.domainlifecycles.persistence.mapping.converter.TypeConverterProvider;
import io.domainlifecycles.persistence.mapping.converter.def.DefaultTypeConverterProvider;
import io.domainlifecycles.persistence.records.DefaultNewRecordInstanceProvider;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordClassProvider;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordTypeConfiguration;
import io.domainlifecycles.persistence.records.NewRecordInstanceProvider;
import io.domainlifecycles.persistence.records.RecordClassProvider;
import io.domainlifecycles.persistence.records.RecordMirrorInstanceProvider;
import io.domainlifecycles.persistence.records.RecordPropertyAccessor;
import io.domainlifecycles.persistence.records.RecordPropertyProvider;
import io.domainlifecycles.persistence.records.RecordTypeToEntityTypeMatcher;
import org.jooq.UpdatableRecord;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * jOOQ specific implementation of a {@link DomainPersistenceConfiguration}.
 *
 * @author Mario Herb
 */
public class JooqDomainPersistenceConfiguration extends DomainPersistenceConfiguration {

    /**
     * Provides the mechanism for obtaining record classes, enabling type-specific operations
     * within the persistence configuration. This field leverages a {@link RecordClassProvider}
     * to supply a collection of record classes for use in domain persistence operations.
     *
     * The record classes are primarily used in conjunction with database record-to-entity
     * mapping and related domain model transformations. This enables the dynamic discovery of
     * specific record types supported by the persistence configuration.
     *
     * A record class refers to a concrete implementation of {@link UpdatableRecord}.
     *
     * The {@code RecordClassProvider} is typically utilized by other components, such as
     * entity mappers or record-to-entity matchers, to identify and work with the appropriate
     * record types during database access and domain object building.
     */
    public final RecordClassProvider<UpdatableRecord<?>> recordClassProvider;

    /**
     * A matcher used to associate a specific type of database record, represented by
     * {@link UpdatableRecord}, with a corresponding entity type in the domain model.
     * This provides the capability to determine the appropriate record type for a given
     * entity type name, facilitating operations involving database interaction and domain
     * entity mappings.
     *
     * This field is part of the persistence configuration and plays a critical role in
     * resolving the relationships between the record types provided by the database schema
     * and the domain entity types designed within the application.
     */
    public final RecordTypeToEntityTypeMatcher<UpdatableRecord<?>> recordTypeToEntityTypeMatcher;

    /**
     * Provides an instance of {@link RecordMirrorInstanceProvider} for managing record mirrors.
     * This field is used in the context of configuring domain persistence to facilitate the
     * creation and retrieval of entity and value object record mirrors based on record types
     * and their associated metadata. The {@link RecordMirrorInstanceProvider} interface defines
     * methods for managing the mapping and mirror creation processes.
     *
     * In conjunction with other components such as {@link RecordClassProvider} and
     * {@link RecordTypeToEntityTypeMatcher}, this field plays a crucial role in enabling
     * dynamic and type-safe persistence operations for domain entities and value objects.
     */
    public final RecordMirrorInstanceProvider<UpdatableRecord<?>> recordMirrorInstanceProvider;

    /**
     * The {@code typeConverterProvider} field supplies an implementation of {@link TypeConverterProvider},
     * which is responsible for providing a collection of {@link TypeConverter} instances. These
     * converters facilitate type transformations between different representations, often needed
     * for mapping database records to domain objects or vice versa.
     *
     * This field plays a key role in ensuring proper type conversion within the persistence configuration,
     * allowing customization and flexibility in handling type compatibility during data processing.
     */
    public final TypeConverterProvider typeConverterProvider;

    /**
     * Matches record properties to corresponding entity fields, value object property paths,
     * or entity references within the context of domain persistence configuration.
     *
     * This field is utilized to determine the compatibility between database record properties
     * and domain model structures, enabling seamless data mapping between the two.
     */
    public final RecordPropertyMatcher recordPropertyMatcher;

    /**
     * A provider responsible for creating new record instances within the persistence configuration.
     *
     * This field is utilized to dynamically generate record instances needed for database operations
     * or other persistence-related tasks. Its implementation must conform to the contract specified
     * by the {@link NewRecordInstanceProvider} interface, ensuring support for various record types.
     *
     * By leveraging this provider, the configuration enables a flexible and extensible mechanism
     * for handling database record instantiation, which is particularly useful in scenarios where
     * records are dynamically typed or require customized initialization logic.
     */
    public final NewRecordInstanceProvider newRecordInstanceProvider;

    /**
     * Provides an implementation for obtaining record properties in the context of Jooq domain persistence
     * configuration. This field is responsible for supplying a list of property metadata for a given record
     * class, facilitating interaction with database records and enabling dynamic property access.
     *
     * The recordPropertyProvider field plays a critical role in the overall domain persistence architecture,
     * supporting the conversion and management of record properties between database records and domain objects.
     * It relies on the {@link RecordPropertyProvider} interface to define its behavior.
     *
     * It is especially useful when dealing with property reflection or other metadata-driven processes
     * that depend on record-specific property structures.
     */
    public final RecordPropertyProvider recordPropertyProvider;

    /**
     * A field that provides access to record properties for {@link UpdatableRecord}.
     * It enables setting and retrieving the values of specific properties
     * from a given record using the {@link RecordPropertyAccessor} interface.
     *
     * This field is typically used in the configuration and management of
     * domain persistence, where direct manipulation of record properties
     * is required to support dynamic data operations and mappings.
     *
     * The associated {@link RecordPropertyAccessor} implementation ensures
     * that property values can be programmatically managed, facilitating
     * adaptable and customizable persistence logic.
     */
    public final RecordPropertyAccessor<UpdatableRecord<?>> recordPropertyAccessor;

    /**
     * Provides information about domain object fields that should be ignored during persistence auto-mapping.
     * The {@link IgnoredFieldProvider} interface is implemented to define the specific logic for determining
     * which fields are excluded from the persistence layer's automatic mapping operations.
     *
     * This field is utilized by the domain persistence configuration to handle scenarios where certain fields
     * of an entity or value object should be excluded from database interactions, ensuring that only relevant fields
     * are considered for persistence processing.
     */
    public final IgnoredFieldProvider ignoredDomainObjectFields;

    /**
     * A provider for determining which record properties should be ignored during persistence auto-mapping.
     * The ignored properties are excluded from automated persistence mapping processes to allow for
     * customization or to account for properties that should not be persisted.
     *
     * This field integrates with the domain persistence configuration to enhance flexibility
     * in handling database record and domain model associations.
     */
    public final IgnoredRecordPropertyProvider ignoredRecordProperties;

    /**
     * A provider that supplies configurations for value object record classes
     * contained within an entity. This field is used to retrieve the
     * {@link EntityValueObjectRecordTypeConfiguration} instances that represent the
     * value object record classes related to an entity's persistence structure.
     *
     * This is typically utilized in domain persistence configurations to manage the
     * mapping between value object classes and their corresponding record classes in
     * the database representation.
     */
    public final EntityValueObjectRecordClassProvider entityValueObjectRecordClassProvider;


    private JooqDomainPersistenceConfiguration(DomainObjectBuilderProvider domainObjectBuilderProvider,
                                               Set<RecordMapper<?, ?, ?>> customRecordMappers,
                                               RecordClassProvider<UpdatableRecord<?>> recordClassProvider,
                                               RecordTypeToEntityTypeMatcher<UpdatableRecord<?>> recordTypeToEntityTypeMatcher,
                                               RecordMirrorInstanceProvider<UpdatableRecord<?>> recordMirrorInstanceProvider,
                                               TypeConverterProvider typeConverterProvider,
                                               RecordPropertyMatcher recordPropertyMatcher,
                                               NewRecordInstanceProvider newRecordInstanceProvider,
                                               RecordPropertyProvider recordPropertyProvider,
                                               RecordPropertyAccessor<UpdatableRecord<?>> recordPropertyAccessor,
                                               IgnoredFieldProvider ignoredDomainObjectFields,
                                               IgnoredRecordPropertyProvider ignoredRecordProperties,
                                               EntityValueObjectRecordClassProvider entityValueObjectRecordClassProvider
    ) {
        super(domainObjectBuilderProvider, customRecordMappers);
        this.recordClassProvider = Objects.requireNonNull(recordClassProvider);
        this.recordTypeToEntityTypeMatcher = Objects.requireNonNull(recordTypeToEntityTypeMatcher);
        this.recordMirrorInstanceProvider = Objects.requireNonNull(recordMirrorInstanceProvider);
        this.typeConverterProvider = typeConverterProvider;
        this.recordPropertyMatcher = Objects.requireNonNull(recordPropertyMatcher);
        this.newRecordInstanceProvider = Objects.requireNonNull(newRecordInstanceProvider);
        this.recordPropertyProvider = Objects.requireNonNull(recordPropertyProvider);
        this.recordPropertyAccessor = Objects.requireNonNull(recordPropertyAccessor);
        this.ignoredDomainObjectFields = ignoredDomainObjectFields;
        this.ignoredRecordProperties = ignoredRecordProperties;
        this.entityValueObjectRecordClassProvider = entityValueObjectRecordClassProvider;
    }

    /**
     * Configuration builder
     */
    public static class JooqPersistenceConfigurationBuilder {
        private DomainObjectBuilderProvider domainObjectBuilderProvider;
        private Set<RecordMapper<?, ?, ?>> customRecordMappers;
        private RecordClassProvider<UpdatableRecord<?>> recordClassProvider;
        private RecordTypeToEntityTypeMatcher<UpdatableRecord<?>> recordTypeToEntityTypeMatcher;
        private RecordMirrorInstanceProvider<UpdatableRecord<?>> recordMirrorInstanceProvider;
        private TypeConverterProvider typeConverterProvider;
        private RecordPropertyMatcher recordPropertyMatcher;
        private NewRecordInstanceProvider newRecordInstanceProvider;
        private RecordPropertyProvider recordPropertyProvider;
        private RecordPropertyAccessor<UpdatableRecord<?>> recordPropertyAccessor;
        private IgnoredFieldProvider ignoredDomainObjectFields;
        private IgnoredRecordPropertyProvider ignoredRecordProperties;
        private EntityValueObjectRecordClassProvider entityValueObjectRecordClassProvider;
        private String recordPackage;

        /**
         * Creates a new instance of {@code JooqPersistenceConfigurationBuilder}.
         *
         * @return a new {@code JooqPersistenceConfigurationBuilder} instance to configure persistence settings.
         */
        public static JooqPersistenceConfigurationBuilder newConfig() {
            return new JooqPersistenceConfigurationBuilder();
        }

        /**
         * Sets the {@code RecordTypeToEntityTypeMatcher} for matching record types to entity types.
         *
         * @param recordTypeToDomainObjectTypeMatcher the matcher to use for resolving the relationship between
         *                                            record types and entity types
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder} for method chaining
         */
        public JooqPersistenceConfigurationBuilder withRecordTypeToEntityTypeMatcher(RecordTypeToEntityTypeMatcher<UpdatableRecord<?>> recordTypeToDomainObjectTypeMatcher) {
            this.recordTypeToEntityTypeMatcher = recordTypeToDomainObjectTypeMatcher;
            return this;
        }

        /**
         * Sets the {@code RecordMirrorInstanceProvider}, which provides record mirror instances
         * for mapping between records and domain objects.
         *
         * @param recordMirrorInstanceProvider the {@code RecordMirrorInstanceProvider} instance to be used
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder} for method chaining
         */
        public JooqPersistenceConfigurationBuilder withRecordMirrorInstanceProvider(RecordMirrorInstanceProvider<UpdatableRecord<?>> recordMirrorInstanceProvider) {
            this.recordMirrorInstanceProvider = recordMirrorInstanceProvider;
            return this;
        }

        /**
         * Sets the {@code RecordClassProvider} used to supply record classes for persistence configuration.
         * This method enables the configuration of the provider responsible for determining the available JOOQ record classes.
         *
         * @param recordClassProvider the {@code RecordClassProvider} instance that supplies record class information
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder} for method chaining
         */
        public JooqPersistenceConfigurationBuilder withRecordClassProvider(RecordClassProvider<UpdatableRecord<?>> recordClassProvider) {
            this.recordClassProvider = recordClassProvider;
            return this;
        }

        /**
         * Sets the {@code TypeConverterProvider}, which provides type converters for mapping between
         * database types and domain object types.
         *
         * @param typeConverterProvider the {@code TypeConverterProvider} instance to be used for type conversion
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder} to allow method chaining
         */
        public JooqPersistenceConfigurationBuilder withTypeConverterProvider(TypeConverterProvider typeConverterProvider) {
            this.typeConverterProvider = typeConverterProvider;
            return this;
        }

        /**
         * Sets the {@code RecordPropertyMatcher}, which is responsible for matching record properties
         * to entity fields in the persistence configuration.
         *
         * @param recordPropertyMatcher the {@code RecordPropertyMatcher} instance to be used for property matching
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder} to allow method chaining
         */
        public JooqPersistenceConfigurationBuilder withRecordEntityPropertyMatcher(RecordPropertyMatcher recordPropertyMatcher) {
            this.recordPropertyMatcher = recordPropertyMatcher;
            return this;
        }

        /**
         * Sets the {@code NewRecordInstanceProvider} responsible for creating new record instances.
         *
         * @param newRecordInstanceProvider the {@code NewRecordInstanceProvider} to be used for providing new record instances
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder} to allow method chaining
         */
        public JooqPersistenceConfigurationBuilder withNewRecordInstanceProvider(NewRecordInstanceProvider newRecordInstanceProvider) {
            this.newRecordInstanceProvider = newRecordInstanceProvider;
            return this;
        }

        /**
         * Sets the {@code RecordPropertyProvider}, which provides record properties
         * necessary for configuring the persistence layer.
         *
         * @param recordPropertyProvider the {@code RecordPropertyProvider} instance to be used
         *                               for supplying record property information
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder}
         *         to allow method chaining
         */
        public JooqPersistenceConfigurationBuilder withRecordPropertyProvider(RecordPropertyProvider recordPropertyProvider) {
            this.recordPropertyProvider = recordPropertyProvider;
            return this;
        }

        /**
         * Sets the {@code RecordPropertyAccessor}, which provides access to
         * record properties and facilitates the retrieval and modification
         * of property values for {@code UpdatableRecord} instances.
         *
         * @param recordPropertyAccessor the {@code RecordPropertyAccessor} instance
         *                                to be used for accessing record properties
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder}
         *         to allow method chaining
         */
        public JooqPersistenceConfigurationBuilder withRecordPropertyAccessor(RecordPropertyAccessor<UpdatableRecord<
            ?>> recordPropertyAccessor) {
            this.recordPropertyAccessor = recordPropertyAccessor;
            return this;
        }

        /**
         * Sets the {@code IgnoredFieldProvider}, which defines fields of domain objects
         * to be excluded from persistence auto-mapping.
         *
         * @param ignoredFieldProvider the {@code IgnoredFieldProvider} instance used to specify
         *                             the fields to ignore during auto-mapping
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder}
         *         to enable method chaining
         */
        public JooqPersistenceConfigurationBuilder withIgnoredDomainObjectFields(IgnoredFieldProvider ignoredFieldProvider) {
            this.ignoredDomainObjectFields = ignoredFieldProvider;
            return this;
        }

        /**
         * Configures the {@code IgnoredRecordPropertyProvider}, which defines record properties
         * to be excluded from persistence auto-mapping.
         *
         * @param ignoredRecordPropertyProvider the {@code IgnoredRecordPropertyProvider} instance
         *                                      specifying the record properties to ignore
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder}
         *         to enable method chaining
         */
        public JooqPersistenceConfigurationBuilder withIgnoredRecordProperties(IgnoredRecordPropertyProvider ignoredRecordPropertyProvider) {
            this.ignoredRecordProperties = ignoredRecordPropertyProvider;
            return this;
        }

        /**
         * Sets the {@code EntityValueObjectRecordClassProvider}, which provides record class definitions
         * for entity value objects in the persistence configuration.
         *
         * @param entityValueObjectRecordClassProvider the {@code EntityValueObjectRecordClassProvider} instance
         *                                             to be used for supplying record class information related to
         *                                             entity value objects
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder} to allow method chaining
         */
        public JooqPersistenceConfigurationBuilder withEntityValueObjectRecordClassProvider(EntityValueObjectRecordClassProvider entityValueObjectRecordClassProvider) {
            this.entityValueObjectRecordClassProvider = entityValueObjectRecordClassProvider;
            return this;
        }

        /**
         * Configures the JooqPersistenceConfigurationBuilder with the specified EntityValueObjectRecordTypeConfiguration objects.
         *
         * @param entityValueObjectRecordTypeConfigurations the array of EntityValueObjectRecordTypeConfiguration to be applied to the configuration
         * @return the updated instance of JooqPersistenceConfigurationBuilder with the provided configurations applied
         */
        public JooqPersistenceConfigurationBuilder withEntityValueObjectRecordTypeConfiguration(EntityValueObjectRecordTypeConfiguration... entityValueObjectRecordTypeConfigurations) {
            return withEntityValueObjectRecordClassProvider(
                () -> Arrays.asList(entityValueObjectRecordTypeConfigurations));
        }

        /**
         * Sets the package name where the Jooq record classes are located.
         *
         * @param recordPackage the fully qualified name of the package containing the Jooq record classes
         * @return the current instance of {@code JooqPersistenceConfigurationBuilder} for method chaining
         */
        public JooqPersistenceConfigurationBuilder withRecordPackage(String recordPackage) {
            this.recordPackage = recordPackage;
            return this;
        }

        /**
         * Configures the JooqPersistenceConfigurationBuilder with a set of custom record mappers.
         *
         * @param customRecordMappers a set of custom record mappers to be used for mapping database records
         *                            to application-specific objects
         * @return the updated instance of JooqPersistenceConfigurationBuilder
         */
        public JooqPersistenceConfigurationBuilder withCustomRecordMappers(Set<RecordMapper<?, ?, ?>> customRecordMappers) {
            this.customRecordMappers = customRecordMappers;
            return this;
        }

        /**
         * Sets the provider for building domain objects and associates it with the configuration builder.
         *
         * @param domainObjectBuilderProvider the provider responsible for creating domain object builders
         * @return the instance of {@code JooqPersistenceConfigurationBuilder} for method chaining
         */
        public JooqPersistenceConfigurationBuilder withDomainObjectBuilderProvider(DomainObjectBuilderProvider domainObjectBuilderProvider) {
            this.domainObjectBuilderProvider = domainObjectBuilderProvider;
            return this;
        }

        /**
         * Builds and returns a configured instance of {@link JooqDomainPersistenceConfiguration}.
         * This method ensures that all required components for the configuration are properly initialized.
         * If any mandatory component is not explicitly set, default implementations or values are used.
         *
         * The method also performs validation to avoid ambiguous or conflicting configurations,
         * such as ensuring only one between 'recordPackage' and 'recordClassProvider' is set.
         *
         * @return a fully constructed instance of {@link JooqDomainPersistenceConfiguration} with all necessary dependencies.
         * @throws DLCPersistenceException when both 'recordPackage' and 'recordClassProvider' are configured, causing ambiguity.
         */
        public JooqDomainPersistenceConfiguration make() {

            if (this.recordTypeToEntityTypeMatcher == null) {
                this.recordTypeToEntityTypeMatcher = new JooqRecordTypeToEntityTypeMatcher();
            }

            if (this.newRecordInstanceProvider == null) {
                this.newRecordInstanceProvider = new DefaultNewRecordInstanceProvider();
            }

            if (this.recordPropertyProvider == null) {
                recordPropertyProvider = new JooqRecordPropertyProvider(newRecordInstanceProvider);
            }

            if (this.recordMirrorInstanceProvider == null) {
                recordMirrorInstanceProvider = new JooqRecordMirrorInstanceProvider(newRecordInstanceProvider);
            }

            if (this.typeConverterProvider == null) {
                this.typeConverterProvider = new DefaultTypeConverterProvider();
            }

            if (this.recordPropertyMatcher == null) {
                this.recordPropertyMatcher = new JooqRecordPropertyMatcher();
            }

            if (this.recordPropertyAccessor == null) {
                this.recordPropertyAccessor = new JooqRecordPropertyAccessor();
            }

            if (this.recordPackage != null && this.recordClassProvider != null) {
                throw DLCPersistenceException.fail(
                    "Ambiguous persistence configuration. 'recordPackage' and 'recordClassProvider' configured. " +
                        "We don't know which one should be used!");
            }

            if (this.recordPackage != null) {
                this.recordClassProvider = new JooqRecordClassProvider(this.recordPackage);
            }

            JooqDomainPersistenceConfiguration configuration = new JooqDomainPersistenceConfiguration(
                this.domainObjectBuilderProvider,
                this.customRecordMappers,
                this.recordClassProvider,
                this.recordTypeToEntityTypeMatcher,
                this.recordMirrorInstanceProvider,
                this.typeConverterProvider,
                this.recordPropertyMatcher,
                this.newRecordInstanceProvider,
                this.recordPropertyProvider,
                this.recordPropertyAccessor,
                this.ignoredDomainObjectFields,
                this.ignoredRecordProperties,
                this.entityValueObjectRecordClassProvider
            );
            return configuration;
        }
    }

}
