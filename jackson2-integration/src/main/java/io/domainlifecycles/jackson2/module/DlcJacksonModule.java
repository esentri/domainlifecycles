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
 *  Copyright 2019-2025 the original author or authors.
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

package io.domainlifecycles.jackson2.module;

import io.domainlifecycles.builder.DomainObjectBuilder;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.internal.DomainObject;
import io.domainlifecycles.jackson2.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson2.databind.DlcDeserializerModifier;
import io.domainlifecycles.jackson2.databind.DlcSerializerModifier;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * DLC Jackson extension
 *
 * @author Leon Völlinger
 * @author Mario Herb
 * @see SimpleModule
 */
@Deprecated
public class DlcJacksonModule extends SimpleModule {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String MODULE_NAME = "dlc-module";

    /**
     * Manages the container for registered customizers used within the DlcJacksonModule.
     * This container holds mappings between domain object types and their respective
     * {@link JacksonMappingCustomizer} instances. It enables flexible customization
     * of Jackson serialization and deserialization based on domain-specific requirements.
     */
    private final CustomizerContainer customizerContainer = new CustomizerContainer();

    /**
     * A provider of {@link DomainObjectBuilder} instances used for constructing domain objects.
     * This field ensures that the module has access to domain object builder functionality
     * required for mapping and customization processes.
     *
     * Utilized by the {@code DlcJacksonModule} for tasks such as registering customizations
     * and configuring domain object builders for specific domain types.
     */
    private final DomainObjectBuilderProvider domainObjectBuilderProvider;

    /**
     * Provides access to entity identity information within the module.
     * This component is used to facilitate the retrieval of identity details
     * for entity types during operations such as serialization and deserialization.
     */
    private final EntityIdentityProvider entityIdentityProvider;


    /**
     * Constructor for DlcJacksonModule.
     *
     * @param domainObjectBuilderProvider the provider for DomainObjectBuilders
     * @param entityIdentityProvider the provider for entity identity information
     */
    public DlcJacksonModule(DomainObjectBuilderProvider domainObjectBuilderProvider,
                            EntityIdentityProvider entityIdentityProvider) {
        this.domainObjectBuilderProvider = domainObjectBuilderProvider;
        this.entityIdentityProvider = entityIdentityProvider;
    }

    /**
     * Constructor for DlcJacksonModule.
     *
     * @param domainObjectBuilderProvider the provider for DomainObjectBuilders
     */
    public DlcJacksonModule(DomainObjectBuilderProvider domainObjectBuilderProvider) {
        this.domainObjectBuilderProvider = domainObjectBuilderProvider;
        this.entityIdentityProvider = null;
    }

    /**
     * Setup module.
     */
    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.addBeanDeserializerModifier(
            new DlcDeserializerModifier(customizerContainer, domainObjectBuilderProvider, entityIdentityProvider));
        context.addBeanSerializerModifier(new DlcSerializerModifier(customizerContainer));
    }

    /**
     * @return module name
     */
    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    /**
     * @return module version
     */
    @Override
    public Version version() {
        return new Version(3, 0, 0, "", "io.domainlifecycles", "jackson-integration");
    }

    /**
     * Register {@link JacksonMappingCustomizer} instances
     *
     * @param customizer       registered
     * @param domainObjectType for which customizer is registered
     */
    public void registerCustomizer(JacksonMappingCustomizer<?> customizer,
                                   Class<? extends DomainObject> domainObjectType) {
        customizerContainer.addCustomizer(customizer, domainObjectType);
    }

    /**
     * Container for registered customizers.
     */
    public static class CustomizerContainer {
        private final Map<Class<? extends DomainObject>, JacksonMappingCustomizer<?>> customizers = new HashMap<>();

        /**
         * Finds a JacksonMappingCustomizer for the given beanClass.
         *
         * @param beanClass the class for which to find the customizer
         * @return the JacksonMappingCustomizer for the specified beanClass, or null if not found
         */
        public JacksonMappingCustomizer<?> findCustomizer(Class<?> beanClass) {
            return customizers.get(beanClass);
        }

        private void addCustomizer(JacksonMappingCustomizer<?> customizer,
                                   Class<? extends DomainObject> domainObjectType) {
            customizers.put(domainObjectType, customizer);
        }

    }

}
