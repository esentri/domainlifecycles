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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.jdbc.configuration;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.jdbc.configuration.def.JdbcRecordPropertyAccessor;
import io.domainlifecycles.jdbc.configuration.def.JdbcRecordPropertyProvider;
import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.dialect.JdbcDialect;
import io.domainlifecycles.jdbc.imp.matcher.JdbcRecordPropertyMatcher;
import io.domainlifecycles.jdbc.imp.matcher.JdbcTableToEntityTypeMatcher;
import io.domainlifecycles.jdbc.imp.provider.JdbcRecordMirrorInstanceProvider;
import io.domainlifecycles.jdbc.records.JdbcNewRecordInstanceProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.jdbc.schema.JdbcSchemaMetadata;
import io.domainlifecycles.persistence.cache.NoOpTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import io.domainlifecycles.persistence.configuration.DomainPersistenceConfiguration;
import io.domainlifecycles.persistence.exception.DLCPersistenceException;
import io.domainlifecycles.persistence.mapping.IgnoredFieldProvider;
import io.domainlifecycles.persistence.mapping.IgnoredRecordPropertyProvider;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.mapping.RecordPropertyMatcher;
import io.domainlifecycles.persistence.mapping.converter.TypeConverterProvider;
import io.domainlifecycles.persistence.mapping.converter.def.DefaultTypeConverterProvider;
import io.domainlifecycles.persistence.records.NewRecordInstanceProvider;
import io.domainlifecycles.persistence.records.RecordPropertyAccessor;
import io.domainlifecycles.persistence.records.RecordPropertyProvider;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * Plain JDBC based implementation of a {@link DomainPersistenceConfiguration}.
 * <p>
 * Where {@code JooqDomainPersistenceConfiguration} discovers table/record information from generated
 * {@code UpdatableRecord} classes (via a {@code RecordClassProvider} scanning a configured package), this
 * configuration instead requires a {@link JdbcSchemaMetadata} snapshot, read once via {@link
 * java.sql.DatabaseMetaData} from a JDBC {@link java.sql.Connection}: there is no code generation step, and
 * every table is represented by the same {@link JdbcRecord} class at runtime.
 * <p>
 * By default every {@code List<ValueObject>}/{@code List<Identity>}/{@code List<Enum>} field is mapped to
 * its own child table purely by naming convention (see {@link JdbcTableToEntityTypeMatcher}); {@link
 * #entityValueObjectRecordClassProvider} is the escape hatch for the cases that convention cannot resolve -
 * a table name that doesn't follow it, or a single (non-collection) value object that should still be
 * persisted in its own dedicated table rather than embedded inline into its owner's record.
 *
 * @author Mario Herb
 */
public class JdbcDomainPersistenceConfiguration extends DomainPersistenceConfiguration {

    /**
     * The schema metadata snapshot (tables, columns, primary and foreign keys) this configuration is built
     * from.
     */
    public final JdbcSchemaMetadata schemaMetadata;

    /**
     * Supplies the connection used for all database interaction. Registered centrally here so that the
     * {@link io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider} built from this configuration
     * can hand it on to repositories, persisters and fetchers, instead of every one of them requiring it as a
     * separate constructor parameter.
     */
    public final JdbcConnectionProvider connectionProvider;

    /**
     * The dialect used for sequence access. Registered centrally for the same reason as {@link
     * #connectionProvider}.
     */
    public final JdbcDialect dialect;

    /**
     * Matches a physical table name to an entity type.
     */
    public final JdbcTableToEntityTypeMatcher tableToEntityTypeMatcher;

    /**
     * Provides {@link io.domainlifecycles.persistence.mirror.api.EntityRecordMirror} and
     * {@link io.domainlifecycles.persistence.mirror.api.ValueObjectRecordMirror} instances.
     */
    public final JdbcRecordMirrorInstanceProvider recordMirrorInstanceProvider;

    /**
     * Provides type converters used to transform values between record and domain object.
     */
    public final TypeConverterProvider typeConverterProvider;

    /**
     * Matches record properties to entity fields, value object property paths, or entity references.
     */
    public final RecordPropertyMatcher recordPropertyMatcher;

    /**
     * Provides new, empty {@link JdbcRecord} instances for a given table.
     */
    public final NewRecordInstanceProvider newRecordInstanceProvider;

    /**
     * Provides the properties (columns) of a table.
     */
    public final RecordPropertyProvider recordPropertyProvider;

    /**
     * Provides access to the column values of a {@link JdbcRecord}.
     */
    public final RecordPropertyAccessor<JdbcRecord> recordPropertyAccessor;

    /**
     * Provides domain object fields to be ignored during persistence auto-mapping.
     */
    public final IgnoredFieldProvider ignoredDomainObjectFields;

    /**
     * Provides record properties to be ignored during persistence auto-mapping.
     */
    public final IgnoredRecordPropertyProvider ignoredRecordProperties;

    /**
     * Provides explicit value object table configurations for cases the naming-convention based auto-mapping
     * cannot resolve on its own. May be {@code null}, in which case only naming-convention based auto-mapping
     * is used.
     */
    public final JdbcEntityValueObjectRecordClassProvider entityValueObjectRecordClassProvider;

    /**
     * Supplies the {@code TransactionCache} active for the currently running transaction, if any - see
     * {@link DomainPersistenceConfiguration#transactionCacheEnabled}. Plain JDBC has no transaction listener of its
     * own, so the cache is only used with a provider whose scopes follow the transaction boundaries reliably - see
     * {@link JdbcPersistenceConfigurationBuilder#withTransactionCacheProvider(TransactionCacheProvider)}. Without
     * one, or with the feature disabled, this is a {@link NoOpTransactionCacheProvider}: every write reads the
     * current state of the aggregate.
     */
    public final TransactionCacheProvider<JdbcRecord> transactionCacheProvider;

    private JdbcDomainPersistenceConfiguration(
        DomainObjectBuilderProvider domainObjectBuilderProvider,
        Set<RecordMapper<?, ?, ?>> customRecordMappers,
        boolean transactionCacheEnabled,
        JdbcSchemaMetadata schemaMetadata,
        JdbcConnectionProvider connectionProvider,
        JdbcDialect dialect,
        JdbcTableToEntityTypeMatcher tableToEntityTypeMatcher,
        JdbcRecordMirrorInstanceProvider recordMirrorInstanceProvider,
        TypeConverterProvider typeConverterProvider,
        RecordPropertyMatcher recordPropertyMatcher,
        NewRecordInstanceProvider newRecordInstanceProvider,
        RecordPropertyProvider recordPropertyProvider,
        RecordPropertyAccessor<JdbcRecord> recordPropertyAccessor,
        IgnoredFieldProvider ignoredDomainObjectFields,
        IgnoredRecordPropertyProvider ignoredRecordProperties,
        JdbcEntityValueObjectRecordClassProvider entityValueObjectRecordClassProvider,
        TransactionCacheProvider<JdbcRecord> transactionCacheProvider
    ) {
        super(domainObjectBuilderProvider, customRecordMappers, transactionCacheEnabled);
        this.schemaMetadata = Objects.requireNonNull(schemaMetadata);
        this.connectionProvider = Objects.requireNonNull(connectionProvider);
        this.dialect = Objects.requireNonNull(dialect);
        this.tableToEntityTypeMatcher = Objects.requireNonNull(tableToEntityTypeMatcher);
        this.recordMirrorInstanceProvider = Objects.requireNonNull(recordMirrorInstanceProvider);
        this.typeConverterProvider = typeConverterProvider;
        this.recordPropertyMatcher = Objects.requireNonNull(recordPropertyMatcher);
        this.newRecordInstanceProvider = Objects.requireNonNull(newRecordInstanceProvider);
        this.recordPropertyProvider = Objects.requireNonNull(recordPropertyProvider);
        this.recordPropertyAccessor = Objects.requireNonNull(recordPropertyAccessor);
        this.ignoredDomainObjectFields = ignoredDomainObjectFields;
        this.ignoredRecordProperties = ignoredRecordProperties;
        this.entityValueObjectRecordClassProvider = entityValueObjectRecordClassProvider;
        this.transactionCacheProvider = Objects.requireNonNull(transactionCacheProvider);
    }

    /**
     * Configuration builder.
     */
    public static class JdbcPersistenceConfigurationBuilder {
        private DomainObjectBuilderProvider domainObjectBuilderProvider;
        private Set<RecordMapper<?, ?, ?>> customRecordMappers;
        private JdbcSchemaMetadata schemaMetadata;
        private JdbcConnectionProvider connectionProvider;
        private JdbcDialect dialect;
        private JdbcTableToEntityTypeMatcher tableToEntityTypeMatcher;
        private JdbcRecordMirrorInstanceProvider recordMirrorInstanceProvider;
        private TypeConverterProvider typeConverterProvider;
        private RecordPropertyMatcher recordPropertyMatcher;
        private NewRecordInstanceProvider newRecordInstanceProvider;
        private RecordPropertyProvider recordPropertyProvider;
        private RecordPropertyAccessor<JdbcRecord> recordPropertyAccessor;
        private IgnoredFieldProvider ignoredDomainObjectFields;
        private IgnoredRecordPropertyProvider ignoredRecordProperties;
        private JdbcEntityValueObjectRecordClassProvider entityValueObjectRecordClassProvider;
        private boolean transactionCacheEnabled = false;
        private TransactionCacheProvider<JdbcRecord> transactionCacheProvider;

        /**
         * Creates a new instance of {@code JdbcPersistenceConfigurationBuilder}.
         *
         * @return a new {@code JdbcPersistenceConfigurationBuilder} instance to configure persistence settings.
         */
        public static JdbcPersistenceConfigurationBuilder newConfig() {
            return new JdbcPersistenceConfigurationBuilder();
        }

        /**
         * Sets the schema metadata snapshot this configuration is built from. Mandatory.
         *
         * @param schemaMetadata the schema metadata snapshot
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withSchemaMetadata(JdbcSchemaMetadata schemaMetadata) {
            this.schemaMetadata = schemaMetadata;
            return this;
        }

        /**
         * Sets the {@link JdbcConnectionProvider} supplying the connection used for all database interaction.
         * Mandatory. Registered here once, so that repositories, persisters and fetchers built through the
         * resulting {@link io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider} no longer need
         * it as a separate constructor parameter.
         *
         * @param connectionProvider the connection provider to use
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withConnectionProvider(JdbcConnectionProvider connectionProvider) {
            this.connectionProvider = connectionProvider;
            return this;
        }

        /**
         * Sets the {@link JdbcDialect} used for sequence access. Mandatory. Registered here for the same
         * reason as {@link #withConnectionProvider(JdbcConnectionProvider)}.
         *
         * @param dialect the dialect to use
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withDialect(JdbcDialect dialect) {
            this.dialect = dialect;
            return this;
        }

        /**
         * Sets the {@code JdbcTableToEntityTypeMatcher} used to resolve the table for a given entity type.
         *
         * @param tableToEntityTypeMatcher the matcher to use
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withTableToEntityTypeMatcher(
            JdbcTableToEntityTypeMatcher tableToEntityTypeMatcher) {
            this.tableToEntityTypeMatcher = tableToEntityTypeMatcher;
            return this;
        }

        /**
         * Sets the {@code JdbcRecordMirrorInstanceProvider}, which provides record mirror instances.
         *
         * @param recordMirrorInstanceProvider the provider instance to be used
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withRecordMirrorInstanceProvider(
            JdbcRecordMirrorInstanceProvider recordMirrorInstanceProvider) {
            this.recordMirrorInstanceProvider = recordMirrorInstanceProvider;
            return this;
        }

        /**
         * Sets the {@code TypeConverterProvider}, which provides type converters for mapping between
         * database types and domain object types.
         *
         * @param typeConverterProvider the {@code TypeConverterProvider} instance to be used for type conversion
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withTypeConverterProvider(
            TypeConverterProvider typeConverterProvider) {
            this.typeConverterProvider = typeConverterProvider;
            return this;
        }

        /**
         * Sets the {@code RecordPropertyMatcher}, which is responsible for matching record properties
         * to entity fields in the persistence configuration.
         *
         * @param recordPropertyMatcher the {@code RecordPropertyMatcher} instance to be used for property matching
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withRecordEntityPropertyMatcher(
            RecordPropertyMatcher recordPropertyMatcher) {
            this.recordPropertyMatcher = recordPropertyMatcher;
            return this;
        }

        /**
         * Sets the {@code NewRecordInstanceProvider} responsible for creating new record instances.
         *
         * @param newRecordInstanceProvider the provider to be used for providing new record instances
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withNewRecordInstanceProvider(
            NewRecordInstanceProvider newRecordInstanceProvider) {
            this.newRecordInstanceProvider = newRecordInstanceProvider;
            return this;
        }

        /**
         * Sets the {@code RecordPropertyProvider}, which provides record properties
         * necessary for configuring the persistence layer.
         *
         * @param recordPropertyProvider the provider instance to be used for supplying record property information
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withRecordPropertyProvider(
            RecordPropertyProvider recordPropertyProvider) {
            this.recordPropertyProvider = recordPropertyProvider;
            return this;
        }

        /**
         * Sets the {@code RecordPropertyAccessor}, which provides access to record properties.
         *
         * @param recordPropertyAccessor the instance to be used for accessing record properties
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withRecordPropertyAccessor(
            RecordPropertyAccessor<JdbcRecord> recordPropertyAccessor) {
            this.recordPropertyAccessor = recordPropertyAccessor;
            return this;
        }

        /**
         * Sets the {@code IgnoredFieldProvider}, which defines fields of domain objects
         * to be excluded from persistence auto-mapping.
         *
         * @param ignoredFieldProvider the instance used to specify the fields to ignore during auto-mapping
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withIgnoredDomainObjectFields(
            IgnoredFieldProvider ignoredFieldProvider) {
            this.ignoredDomainObjectFields = ignoredFieldProvider;
            return this;
        }

        /**
         * Configures the {@code IgnoredRecordPropertyProvider}, which defines record properties
         * to be excluded from persistence auto-mapping.
         *
         * @param ignoredRecordPropertyProvider the instance specifying the record properties to ignore
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withIgnoredRecordProperties(
            IgnoredRecordPropertyProvider ignoredRecordPropertyProvider) {
            this.ignoredRecordProperties = ignoredRecordPropertyProvider;
            return this;
        }

        /**
         * Configures the builder with a set of custom record mappers.
         *
         * @param customRecordMappers a set of custom record mappers to be used for mapping database records
         *                            to application-specific objects
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withCustomRecordMappers(
            Set<RecordMapper<?, ?, ?>> customRecordMappers) {
            this.customRecordMappers = customRecordMappers;
            return this;
        }

        /**
         * Sets the provider for building domain objects.
         *
         * @param domainObjectBuilderProvider the provider responsible for creating domain object builders
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withDomainObjectBuilderProvider(
            DomainObjectBuilderProvider domainObjectBuilderProvider) {
            this.domainObjectBuilderProvider = domainObjectBuilderProvider;
            return this;
        }

        /**
         * Enables or disables the transaction cache feature (disabled by default). It only takes effect with a
         * provider set via {@link #withTransactionCacheProvider(TransactionCacheProvider)}; disabled, that provider
         * is ignored.
         *
         * @param transactionCacheEnabled whether the transaction cache feature should be enabled
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withTransactionCacheEnabled(boolean transactionCacheEnabled) {
            this.transactionCacheEnabled = transactionCacheEnabled;
            return this;
        }

        /**
         * Sets the provider of the transaction cache. Plain JDBC has no transaction listener of its own, so the
         * cache is off unless a provider is set whose scopes follow the transaction boundaries reliably:
         * <ul>
         *     <li>with Spring, a {@code SpringTransactionCacheProvider} from {@code persistence-cache-spring-tx} - as
         *     {@code DlcJdbcPersistenceAutoConfiguration} wires it,</li>
         *     <li>with JTA, a {@code JtaTransactionCacheProvider} from {@code persistence-cache-jakarta-jta},</li>
         *     <li>otherwise a {@link ThreadBoundTransactionCacheProvider} whose scope the application opens and
         *     closes around each transaction itself, and clears after a rollback to a savepoint.</li>
         * </ul>
         *
         * @param transactionCacheProvider the transaction cache provider to use
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withTransactionCacheProvider(
            TransactionCacheProvider<JdbcRecord> transactionCacheProvider) {
            this.transactionCacheProvider = transactionCacheProvider;
            return this;
        }

        /**
         * Sets the {@code JdbcEntityValueObjectRecordClassProvider}, which provides explicit value object
         * table configurations for cases the naming-convention based auto-mapping cannot resolve on its own.
         *
         * @param entityValueObjectRecordClassProvider the provider instance to be used
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withEntityValueObjectRecordClassProvider(
            JdbcEntityValueObjectRecordClassProvider entityValueObjectRecordClassProvider) {
            this.entityValueObjectRecordClassProvider = entityValueObjectRecordClassProvider;
            return this;
        }

        /**
         * Configures the builder with the given value object table configurations directly.
         *
         * @param entityValueObjectRecordTypeConfigurations the configurations to apply
         * @return this builder
         */
        public JdbcPersistenceConfigurationBuilder withEntityValueObjectRecordTypeConfiguration(
            JdbcEntityValueObjectRecordTypeConfiguration... entityValueObjectRecordTypeConfigurations) {
            return withEntityValueObjectRecordClassProvider(
                () -> Arrays.asList(entityValueObjectRecordTypeConfigurations));
        }

        /**
         * Builds and returns a configured instance of {@link JdbcDomainPersistenceConfiguration}.
         * If any non-mandatory component is not explicitly set, a default implementation is used.
         *
         * @return a fully constructed instance of {@link JdbcDomainPersistenceConfiguration}.
         * @throws DLCPersistenceException when {@code schemaMetadata} was not configured.
         */
        public JdbcDomainPersistenceConfiguration make() {
            if (this.schemaMetadata == null) {
                throw DLCPersistenceException.fail(
                    "No schema metadata configured. Call 'withSchemaMetadata' with a JdbcSchemaMetadata snapshot " +
                        "read from your database connection.");
            }

            if (this.connectionProvider == null) {
                throw DLCPersistenceException.fail(
                    "No connection provider configured. Call 'withConnectionProvider' with a JdbcConnectionProvider.");
            }

            if (this.dialect == null) {
                throw DLCPersistenceException.fail(
                    "No dialect configured. Call 'withDialect' with a JdbcDialect.");
            }

            if (this.tableToEntityTypeMatcher == null) {
                this.tableToEntityTypeMatcher = new JdbcTableToEntityTypeMatcher();
            }

            if (this.newRecordInstanceProvider == null) {
                this.newRecordInstanceProvider = new JdbcNewRecordInstanceProvider();
            }

            if (this.recordPropertyProvider == null) {
                this.recordPropertyProvider = new JdbcRecordPropertyProvider(schemaMetadata);
            }

            if (this.recordMirrorInstanceProvider == null) {
                this.recordMirrorInstanceProvider = new JdbcRecordMirrorInstanceProvider(schemaMetadata);
            }

            if (this.typeConverterProvider == null) {
                this.typeConverterProvider = new DefaultTypeConverterProvider();
            }

            if (this.recordPropertyMatcher == null) {
                this.recordPropertyMatcher = new JdbcRecordPropertyMatcher();
            }

            if (this.recordPropertyAccessor == null) {
                this.recordPropertyAccessor = new JdbcRecordPropertyAccessor(schemaMetadata);
            }

            if (this.transactionCacheProvider == null || !this.transactionCacheEnabled) {
                this.transactionCacheProvider = new NoOpTransactionCacheProvider<>();
            }

            return new JdbcDomainPersistenceConfiguration(
                this.domainObjectBuilderProvider,
                this.customRecordMappers,
                this.transactionCacheEnabled,
                this.schemaMetadata,
                this.connectionProvider,
                this.dialect,
                this.tableToEntityTypeMatcher,
                this.recordMirrorInstanceProvider,
                this.typeConverterProvider,
                this.recordPropertyMatcher,
                this.newRecordInstanceProvider,
                this.recordPropertyProvider,
                this.recordPropertyAccessor,
                this.ignoredDomainObjectFields,
                this.ignoredRecordProperties,
                this.entityValueObjectRecordClassProvider,
                this.transactionCacheProvider
            );
        }
    }
}
