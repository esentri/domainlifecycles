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

    public final RecordClassProvider<UpdatableRecord<?>> recordClassProvider;
    public final RecordTypeToEntityTypeMatcher<UpdatableRecord<?>> recordTypeToEntityTypeMatcher;

    public final RecordMirrorInstanceProvider<UpdatableRecord<?>> recordMirrorInstanceProvider;
    public final TypeConverterProvider typeConverterProvider;

    public final RecordPropertyMatcher recordPropertyMatcher;
    public final NewRecordInstanceProvider newRecordInstanceProvider;
    public final RecordPropertyProvider recordPropertyProvider;
    public final RecordPropertyAccessor<UpdatableRecord<?>> recordPropertyAccessor;

    public final IgnoredFieldProvider ignoredDomainObjectFields;
    public final IgnoredRecordPropertyProvider ignoredRecordProperties;

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

        public static JooqPersistenceConfigurationBuilder newConfig() {
            return new JooqPersistenceConfigurationBuilder();
        }

        public JooqPersistenceConfigurationBuilder withRecordTypeToEntityTypeMatcher(RecordTypeToEntityTypeMatcher<UpdatableRecord<?>> recordTypeToDomainObjectTypeMatcher) {
            this.recordTypeToEntityTypeMatcher = recordTypeToDomainObjectTypeMatcher;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withRecordMirrorInstanceProvider(RecordMirrorInstanceProvider<UpdatableRecord<?>> recordMirrorInstanceProvider) {
            this.recordMirrorInstanceProvider = recordMirrorInstanceProvider;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withRecordClassProvider(RecordClassProvider<UpdatableRecord<?>> recordClassProvider) {
            this.recordClassProvider = recordClassProvider;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withTypeConverterProvider(TypeConverterProvider typeConverterProvider) {
            this.typeConverterProvider = typeConverterProvider;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withRecordEntityPropertyMatcher(RecordPropertyMatcher recordPropertyMatcher) {
            this.recordPropertyMatcher = recordPropertyMatcher;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withNewRecordInstanceProvider(NewRecordInstanceProvider newRecordInstanceProvider) {
            this.newRecordInstanceProvider = newRecordInstanceProvider;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withRecordPropertyProvider(RecordPropertyProvider recordPropertyProvider) {
            this.recordPropertyProvider = recordPropertyProvider;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withRecordPropertyAccessor(RecordPropertyAccessor<UpdatableRecord<
            ?>> recordPropertyAccessor) {
            this.recordPropertyAccessor = recordPropertyAccessor;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withIgnoredDomainObjectFields(IgnoredFieldProvider ignoredFieldProvider) {
            this.ignoredDomainObjectFields = ignoredFieldProvider;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withIgnoredRecordProperties(IgnoredRecordPropertyProvider ignoredRecordPropertyProvider) {
            this.ignoredRecordProperties = ignoredRecordPropertyProvider;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withEntityValueObjectRecordClassProvider(EntityValueObjectRecordClassProvider entityValueObjectRecordClassProvider) {
            this.entityValueObjectRecordClassProvider = entityValueObjectRecordClassProvider;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withEntityValueObjectRecordTypeConfiguration(EntityValueObjectRecordTypeConfiguration<UpdatableRecord<?>>... entityValueObjectRecordTypeConfigurations) {
            return withEntityValueObjectRecordClassProvider(
                () -> Arrays.asList(entityValueObjectRecordTypeConfigurations));
        }

        public JooqPersistenceConfigurationBuilder withRecordPackage(String recordPackage) {
            this.recordPackage = recordPackage;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withCustomRecordMappers(Set<RecordMapper<?, ?, ?>> customRecordMappers) {
            this.customRecordMappers = customRecordMappers;
            return this;
        }

        public JooqPersistenceConfigurationBuilder withDomainObjectBuilderProvider(DomainObjectBuilderProvider domainObjectBuilderProvider) {
            this.domainObjectBuilderProvider = domainObjectBuilderProvider;
            return this;
        }

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
