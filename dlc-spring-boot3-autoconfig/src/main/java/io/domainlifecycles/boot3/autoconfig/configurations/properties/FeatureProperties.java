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
package io.domainlifecycles.boot3.autoconfig.configurations.properties;

import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.ServiceKind;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;

/**
 * Configuration properties for defining DLC features and their behavior. The properties
 * are prefixed with "dlc.features" in the configuration files.
 *
 * @author Mario Herb
 */
@ConfigurationProperties(prefix = "dlc.features")
@Deprecated
public class FeatureProperties {

    private final Feature builder = new Feature();
    private final Mirror mirror = new Mirror();
    private final Feature jackson2 = new Feature();
    private final Persistence persistence = new Persistence();
    private final Events events = new Events();
    private final Feature openapi = new Feature();
    private final Feature springweb = new Feature();
    private final ServiceKinds serviceKinds = new ServiceKinds();

    /**
     * Retrieves the builder feature configuration.
     *
     * @return the {@code Feature} instance representing the builder configuration.
     */
    public Feature getBuilder() {
        return builder;
    }

    /**
     * Retrieves the Mirror feature configuration.
     *
     * @return the {@code Mirror} instance representing the mirror feature configuration.
     */
    public Mirror getMirror() {
        return mirror;
    }

    /**
     * Retrieves the Jackson2 feature configuration.
     *
     * @return the {@code Feature} instance representing the Jackson2 feature configuration.
     */
    public Feature getJackson2() {
        return jackson2;
    }

    /**
     * Retrieves the Persistence feature configuration.
     *
     * @return the {@code Persistence} instance representing the persistence-related
     *         feature configuration, including settings such as jOOQ record packages
     *         and SQL dialect.
     */
    public Persistence getPersistence() {
        return persistence;
    }

    /**
     * Retrieves the configuration for event-related features, which includes
     * details about the in-memory and Spring bus event mechanisms.
     *
     * @return an instance of {@code Events} containing configurations for
     *         event mechanisms such as in-memory and Spring bus.
     */
    public Events getEvents() {
        return events;
    }

    /**
     * Retrieves the OpenAPI feature configuration.
     *
     * @return the {@code Feature} instance representing the OpenAPI feature configuration.
     */
    public Feature getOpenapi() {
        return openapi;
    }

    /**
     * Retrieves the Spring Web feature configuration.
     *
     * @return the {@code Feature} instance representing the Spring Web feature configuration.
     */
    public Feature getSpringweb() {
        return springweb;
    }

    /**
     * Retrieves the service kinds configuration, which includes details such as
     * whether the service kind feature is enabled and the associated package names.
     *
     * @return an instance of {@code ServiceKinds} containing the configuration
     *         for the service kinds feature, including its enabled state and
     *         associated package information.
     */
    public ServiceKinds getServiceKinds() {
        return serviceKinds;
    }

    /**
     * Represents a configurable feature with an enabled/disabled state.
     * This class is utilized to manage the activation status of a specific feature.
     */
    public static class Feature {

        private boolean enabled = true;

        /**
         * Checks if the current feature is enabled.
         *
         * @return true if the feature is enabled, false otherwise
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets the enabled state of the feature.
         *
         * @param enabled a boolean value indicating whether the feature should be enabled
         *                (true) or disabled (false)
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * Represents the configuration for the Mirror feature.
     * The Mirror feature includes settings for enabling or disabling
     * the feature and defining the base packages relevant to its operation.
     */
    public static class Mirror {

        private boolean enabled = true;
        private String[] basePackages;

        /**
         * Indicates whether the DLC Domain Mirror feature is enabled.
         * The mirror is the base for most DLC features.
         *
         * @return true if the Mirror feature is enabled, false otherwise
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets the status of the Mirror feature, enabling or disabling it based on the
         * specified boolean value. For most use cases, this should be set to {@code true}
         * to enable the feature, unless there's a specific reason to disable it.
         *
         * @param enabled a boolean value indicating whether the Mirror feature should be enabled.
         *                Passing {@code true} enables the feature, while {@code false} disables it.
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Retrieves the base packages associated with the Mirror feature configuration.
         * These base packages are used to define the scope for scanning or processing
         * within the Mirror functionality.
         *
         * @return an array of strings representing the base package names
         *         configured for the Mirror feature, or {@code null} if no
         *         base packages have been defined.
         */
        public String[] getBasePackages() {
            return basePackages;
        }

        /**
         * Sets the base packages associated with the Domain Mirror feature configuration.
         * These base packages are used to define the scope for scanning and processing
         * DLC types marking DDD concepts within the Mirror functionality.
         *
         * @param basePackages an array of strings representing the base package names
         *                     to be configured for the Mirror feature. Passing {@code null}
         *                     clears the existing configuration.
         */
        public void setBasePackages(String[] basePackages) {
            this.basePackages = basePackages;
        }
    }

    /**
     * Represents the configuration settings related to persistence features.
     * This includes options to enable or disable persistence, specify the package
     * for jOOQ-generated records, and define the SQL dialect to be used.
     */
    public static class Persistence {

        private boolean enabled = true;
        private String jooqRecordPackage;
        private String sqlDialect;

        /**
         * Determines whether the DLC persistence feature is enabled.
         *
         * @return {@code true} if persistence is enabled; {@code false} otherwise.
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets the enabled status for the persistence feature.
         *
         * @param enabled a boolean value indicating whether persistence is enabled.
         *                Pass {@code true} to enable persistence, or {@code false} to disable it.
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Retrieves the package name specified for jOOQ-generated records.
         * The package serves as the designated location for jOOQ record classes,
         * which are typically generated during build-time or runtime for database interaction.
         *
         * @return the package name specified for jOOQ-generated records, or {@code null}
         *         if no package has been set.
         */
        public String getJooqRecordPackage() {
            return jooqRecordPackage;
        }

        /**
         * Sets the package name for jOOQ-generated record classes. The specified package is
         * used as the location for placing jOOQ record classes generated during build-time.
         * DLC persistence uses them for runtime for database interaction.
         *
         * @param jooqRecordPackage the package name where jOOQ-generated records will be located;
         *                          must be a valid package name or {@code null} if no package is to be set.
         */
        public void setJooqRecordPackage(String jooqRecordPackage) {
            this.jooqRecordPackage = jooqRecordPackage;
        }

        /**
         * Retrieves the SQL dialect specified for persistence operations.
         * The SQL dialect defines the database-specific SQL syntax and behavior
         * to be used when interacting with the database.
         *
         * @return the name of the SQL dialect as a string, or {@code null} if no dialect has been set.
         */
        public String getSqlDialect() {
            return sqlDialect;
        }

        /**
         * Sets the SQL dialect to be used for persistence operations.
         * The SQL dialect defines the database-specific SQL syntax and behavior
         * that should be applied when interacting with the database.
         * The following dialects are supported among others:
         * MARIADB, MYSQL, POSTGRES, SQLITE, H2, HSQLDB, ORACLE, SQLSERVER
         *
         * @param sqlDialect the name of the SQL dialect as a string; should be a valid
         *                   dialect supported by the underlying persistence framework.
         *                   Pass {@code null} to clear any previously set value.
         */
        public void setSqlDialect(String sqlDialect) {
            this.sqlDialect = sqlDialect;
        }
    }

    /**
     * Represents the configuration for event-related features in the system.
     * This includes settings for in-memory event handling and the Spring bus event mechanism.
     */
    public static class Events {

        private final Inmemory inmemory = new Inmemory();
        private final Springbus springbus = new Springbus();

        /**
         * Retrieves the Inmemory configuration instance.
         * This instance contains settings related to in-memory event handling,
         * such as whether the in-memory feature is enabled.
         *
         * @return the {@code Inmemory} configuration object.
         */
        public Inmemory getInmemory() {
            return inmemory;
        }

        /**
         * Retrieves the configuration instance for the Spring bus event mechanism.
         * The returned {@code Springbus} object contains settings related to event handling,
         * such as whether the Spring bus is enabled, the aggregate supoprt for domain events,
         * and proxy configurations for service kinds.
         *
         * @return the {@code Springbus} configuration object.
         */
        public Springbus getSpringbus() {
            return springbus;
        }
    }

    /**
     * Represents the configuration for service kinds, including its enabled state
     * and associated package information. This class provides methods to access
     * and update these configurations.
     */
    public static class ServiceKinds {
        private boolean enabled = true;
        private String[] packages;

        /**
         * Checks whether autowiring for service kind domain implementations
         * (DomainServices, ApplicationServices, Repositories and OutboundServices) is enabled.
         *
         * @return {@code true} if the service kind is enabled; {@code false} otherwise.
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets enabled status for autowiring of service kind domain implementations
         * (DomainServices, ApplicationServices, Repositories and OutboundServices).
         *
         * @param enabled a {@code boolean} value indicating whether the service kind
         *                should be enabled ({@code true}) or disabled ({@code false}).
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Retrieves the list of package names on which automatic bean autowiring on
         * {@link ServiceKind} initialization should occur.
         *
         * @return an array of package names as {@code String[]} that are associated with the service kind
         * autowiring feature, or {@code null} if no packages are defined.
         */
        public String[] getPackages() {
            return packages;
        }

        /**
         * Sets the list of package names on which automatic bean autowiring on
         *          * @link ServiceKind} initialization should occur.
         *
         * @param packages an array of package names as {@code String[]} that are associated with the service kind
         *                 autowiring feature, or {@code null} if no packages are defined.
         */
        public void setPackages(String[] packages) {
            this.packages = packages;
        }
    }

    /**
     * Represents the configuration settings for the Springbus feature within the application.
     * This class allows control over various behaviors related to the Springbus feature,
     * such as enabling or disabling it, aggregate domain events support, and enabling service kinds proxy.
     */
    public static class Springbus{
        private boolean enabled = true;
        private boolean aggregateDomainEvents = true;
        private boolean serviceKindsProxy = true;

        /**
         * Checks if the Springbus DomainEvents feature is enabled.
         *
         * @return true if the Springbus feature is enabled, false otherwise.
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets the enabled status for the Springbus DomainEvents feature.
         *
         * @param enabled a boolean indicating whether the Springbus feature should be enabled (true) or disabled (false)
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Checks if Aggregate DomainEvents support is enabled for the Springbus feature.
         *
         * @return true if Aggregate DomainEvents are enabled, false otherwise.
         */
        public boolean isAggregateDomainEvents() {
            return aggregateDomainEvents;
        }

        /**
         * Sets the enabled status for Aggregate DomainEvents support.
         *
         * @param aggregateDomainEvents a boolean indicating whether Aggregate DomainEvents
         *                              should be enabled (true) or disabled (false)
         */
        public void setAggregateDomainEvents(boolean aggregateDomainEvents) {
            this.aggregateDomainEvents = aggregateDomainEvents;
        }

        /**
         * Checks if the service kinds proxy is enabled for the Springbus DomainEvents feature.
         *
         * @return true if the service kinds proxy is enabled, false otherwise.
         */
        public boolean isServiceKindsProxy() {
            return serviceKindsProxy;
        }

        /**
         * Sets the status for enabling or disabling the service kinds proxy in the Springbus
         * DomainEvents feature. By enabling this feature, methods marked with {@link DomainEventListener}
         * behave like being marked with Spring {@link Async}, {@link org.springframework.transaction.event.TransactionalEventListener}
         * and also starting a new transaction like {@link org.springframework.transaction.annotation.Transactional}
         * with REQUIRES_NEW.
         *
         * @param serviceKindsProxy a boolean indicating whether the service kinds proxy feature
         *                          should be enabled (true) or disabled (false)
         */
        public void setServiceKindsProxy(boolean serviceKindsProxy) {
            this.serviceKindsProxy = serviceKindsProxy;
        }
    }

    /**
     * Represents the configuration for an in-memory feature.
     *
     * This class provides an option to enable or disable the in-memory functionality.
     * It is typically used as part of a larger feature configuration system.
     */
    public static class Inmemory {

        private boolean enabled = false;

        /**
         * Checks whether the in-memory DomainEvents feature is enabled.
         *
         * @return true if the in-memory DomainEvents feature is enabled; false otherwise
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Enables or disables the in-memory DomainEvents feature based on the provided parameter.
         *
         * @param enabled a boolean value indicating whether the in-memory DomainEvents feature
         *                should be enabled (true) or disabled (false)
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
