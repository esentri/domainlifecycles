/*
 *
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

package nitrox.dlc.jackson.module;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import nitrox.dlc.access.classes.ClassProvider;
import nitrox.dlc.access.object.IdentityFactory;
import nitrox.dlc.builder.DomainObjectBuilderProvider;
import nitrox.dlc.domain.types.internal.DomainObject;
import nitrox.dlc.jackson.api.JacksonMappingCustomizer;
import nitrox.dlc.jackson.databind.DlcDeserializerModifier;
import nitrox.dlc.jackson.databind.DlcSerializerModifier;
import nitrox.dlc.persistence.provider.EntityIdentityProvider;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * NitroX DLC Jackson extension
 * @see SimpleModule
 *
 * @author Mario Herb
 */
public class DlcJacksonModule extends SimpleModule {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String MODULE_NAME = "dlc-module";

    private final CustomizerContainer customizerContainer = new CustomizerContainer();
    private final DomainObjectBuilderProvider domainObjectBuilderProvider;

    private final EntityIdentityProvider entityIdentityProvider;

    public DlcJacksonModule(DomainObjectBuilderProvider domainObjectBuilderProvider,
                            EntityIdentityProvider entityIdentityProvider) {
        this.domainObjectBuilderProvider = domainObjectBuilderProvider;
        this.entityIdentityProvider = entityIdentityProvider;
    }

    /**
     * Setup module.
     */
    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.addBeanDeserializerModifier(new DlcDeserializerModifier(customizerContainer, domainObjectBuilderProvider, entityIdentityProvider));
        context.addBeanSerializerModifier(new DlcSerializerModifier(customizerContainer));
    }

    /**
     *
     * @return module name
     */
    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    /**
     *
     * @return module version
     */
    @Override
    public Version version() {
        return new Version(1, 0, 21, "", "nitrox", "dlc");
    }

    /**
     * Register {@link JacksonMappingCustomizer} instances
     *
     * @param customizer registered
     * @param domainObjectType for which customizer is registered
     */
    public void registerCustomizer(JacksonMappingCustomizer<?> customizer, Class<? extends DomainObject> domainObjectType) {
        customizerContainer.addCustomizer(customizer, domainObjectType);
    }

    /**
     * Container for registered customizers.
     */
    public static class CustomizerContainer {
        private final Map<Class<? extends DomainObject>, JacksonMappingCustomizer<?>> customizers = new HashMap<>();

        public JacksonMappingCustomizer<?> findCustomizer(Class<?> beanClass) {
            return customizers.get(beanClass);
        }

        private void addCustomizer(JacksonMappingCustomizer<?> customizer, Class<? extends DomainObject> domainObjectType) {
            customizers.put(domainObjectType, customizer);
        }

    }

}
