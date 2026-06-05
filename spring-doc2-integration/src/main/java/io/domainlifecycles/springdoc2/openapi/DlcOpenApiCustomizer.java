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

package io.domainlifecycles.springdoc2.openapi;

import io.domainlifecycles.swagger.v3.MirrorBasedOpenApiExtension;
import io.domainlifecycles.swagger.v3.OpenAPIOptionalNullabilityExtension;
import io.domainlifecycles.swagger.v3.OpenAPIPrimitivePropertyExtension;
import io.domainlifecycles.swagger.v3.OpenAPIPropertyBeanValidationExtension;
import io.domainlifecycles.swagger.v3.OpenAPITemporalTypeExtension;
import io.swagger.v3.oas.models.OpenAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;

/**
 * The DlcOpenApiCustomizer class customizes OpenAPI specifications by applying various extensions
 * for DLC-specific features. It implements the OpenApiCustomizer interface to modify the OpenAPI
 * definition based on specific configuration properties and optional extensions.
 *
 * The customizer includes several optional extensions that can be enabled or disabled:
 * - Temporal types extension: Adds support for handling temporal types in OpenAPI schemas.
 * - Primitive property extension: Enhances OpenAPI schema representation for primitive properties.
 * - Bean validation extension: Incorporates bean validation metadata into OpenAPI schema definitions.
 * - Mirror-based OpenAPI extension: Provides schema extensions using mirror-based reflection analysis and DLC/DDD specific extensions.
 * - Optional nullability extension: Extends schema definitions to reflect the nullability of optional properties.
 *
 * This class leverages SpringDoc configuration properties to control its behavior and requires the
 * 'springdoc.use-fqn' configuration to be set to 'true'.
 *
 * @author Mario Herb
 */
@Deprecated
public class DlcOpenApiCustomizer implements OpenApiCustomizer {

    private static final Logger log = LoggerFactory.getLogger(DlcOpenApiCustomizer.class);

    private boolean enableTemporalTypesExtension = true;
    private boolean enablePrimitivePropertyExtension = true;
    private boolean enableBeanValidationExtension = true;
    private boolean enableMirrorBasedOpenApiExtension = true;
    private boolean enableOptionalNullabilityExtension = true;

    private final SpringDocConfigProperties springDocConfigProperties;

    private final MirrorBasedOpenApiExtension mirrorBasedOpenApiExtension;

    private final String[] entitiesWithExternallyProvidedIds;

    /**
     * Constructor of the DLC Spring Doc based Open API extension.
     * <p>
     * To work correctly, the spring doc configuration property 'springdoc.use-fqn' must be 'true',
     * as this extension relies on full qualified path names of the extended classes.
     *
     * @param springDocConfigProperties         configuration for the customizer
     * @param entitiesWithExternallyProvidedIds name of entities which IDs are provided from extern
     */
    public DlcOpenApiCustomizer(
        SpringDocConfigProperties springDocConfigProperties,
        String... entitiesWithExternallyProvidedIds) {
        this.springDocConfigProperties = springDocConfigProperties;
        this.entitiesWithExternallyProvidedIds = entitiesWithExternallyProvidedIds;
        if (!this.springDocConfigProperties.isUseFqn()) {
            throw new RuntimeException(
                "DLC Open API extension requires the spring doc config property 'springdoc.use-fqn' to be 'true'!");
        }
        this.mirrorBasedOpenApiExtension = new MirrorBasedOpenApiExtension();
    }

    /**
     * Central entry point for DLC Open API extensions.
     */
    @Override
    public void customise(OpenAPI openApi) {
        log.debug("Customizing Open API informations with DLC specifica!");
        if(enableTemporalTypesExtension){
            OpenAPITemporalTypeExtension.extendOpenAPISchemaForTemporalTypes(openApi);
        }
        if(enablePrimitivePropertyExtension){
            OpenAPIPrimitivePropertyExtension.extendPrimitiveProperties(openApi);
        }
        if(enableMirrorBasedOpenApiExtension){
            mirrorBasedOpenApiExtension.extendOpenAPISchemaForDLCTypes(openApi, entitiesWithExternallyProvidedIds);
        }
        if(enableBeanValidationExtension){
            OpenAPIPropertyBeanValidationExtension.extendWithBeanValidationInformation(openApi);
        }
        if(enableOptionalNullabilityExtension){
            OpenAPIOptionalNullabilityExtension.extendNullabilityForOptionals(openApi);
        }
    }

    /**
     * Indicates whether the temporal types extension feature is enabled.
     *
     * @return true if the temporal types extension is enabled; false otherwise
     */
    public boolean isEnableTemporalTypesExtension() {
        return enableTemporalTypesExtension;
    }

    /**
     * Sets whether the temporal types extension feature is enabled.
     *
     * @param enableTemporalTypesExtension a boolean value indicating whether the temporal types extension
     *                                     should be enabled (true) or disabled (false)
     */
    public void setEnableTemporalTypesExtension(boolean enableTemporalTypesExtension) {
        this.enableTemporalTypesExtension = enableTemporalTypesExtension;
    }

    /**
     * Indicates whether the primitive property extension feature is enabled.
     *
     * @return true if the primitive property extension is enabled; false otherwise
     */
    public boolean isEnablePrimitivePropertyExtension() {
        return enablePrimitivePropertyExtension;
    }

    /**
     * Sets whether the primitive property extension feature is enabled.
     *
     * @param enablePrimitivePropertyExtension a boolean value indicating whether the primitive property extension
     *                                         should be enabled (true) or disabled (false)
     */
    public void setEnablePrimitivePropertyExtension(boolean enablePrimitivePropertyExtension) {
        this.enablePrimitivePropertyExtension = enablePrimitivePropertyExtension;
    }

    /**
     * Indicates whether the bean validation extension feature is enabled.
     *
     * @return true if the bean validation extension is enabled; false otherwise
     */
    public boolean isEnableBeanValidationExtension() {
        return enableBeanValidationExtension;
    }

    /**
     * Sets whether the bean validation extension feature is enabled or disabled.
     *
     * @param enableBeanValidationExtension a boolean value indicating whether the bean validation
     *                                      extension should be enabled (true) or disabled (false)
     */
    public void setEnableBeanValidationExtension(boolean enableBeanValidationExtension) {
        this.enableBeanValidationExtension = enableBeanValidationExtension;
    }

    /**
     * Indicates whether the mirror-based OpenAPI extension feature is enabled.
     *
     * @return true if the mirror-based OpenAPI extension is enabled; false otherwise
     */
    public boolean isEnableMirrorBasedOpenApiExtension() {
        return enableMirrorBasedOpenApiExtension;
    }

    /**
     * Sets whether the mirror-based OpenAPI extension feature is enabled or disabled.
     *
     * @param enableMirrorBasedOpenApiExtension a boolean value indicating whether the mirror-based OpenAPI
     *                                          extension should be enabled (true) or disabled (false)
     */
    public void setEnableMirrorBasedOpenApiExtension(boolean enableMirrorBasedOpenApiExtension) {
        this.enableMirrorBasedOpenApiExtension = enableMirrorBasedOpenApiExtension;
    }

    /**
     * Indicates whether the optional nullability extension feature is enabled.
     *
     * @return true if the optional nullability extension is enabled; false otherwise
     */
    public boolean isEnableOptionalNullabilityExtension() {
        return enableOptionalNullabilityExtension;
    }

    /**
     * Sets whether the optional nullability extension feature is enabled or disabled.
     *
     * @param enableOptionalNullabilityExtension a boolean value indicating whether the optional nullability extension
     *                                           should be enabled (true) or disabled (false)
     */
    public void setEnableOptionalNullabilityExtension(boolean enableOptionalNullabilityExtension) {
        this.enableOptionalNullabilityExtension = enableOptionalNullabilityExtension;
    }

    /**
     * Retrieves the SpringDoc configuration properties associated with this customizer.
     *
     * @return an instance of {@code SpringDocConfigProperties} containing the configuration settings.
     */
    public SpringDocConfigProperties getSpringDocConfigProperties() {
        return springDocConfigProperties;
    }

}
