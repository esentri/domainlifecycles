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

package io.domainlifecycles.springdoc2.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.domainlifecycles.swagger.v3.MirrorBasedOpenApiExtension;
import io.domainlifecycles.swagger.v3.OpenAPIPrimitivePropertyExtension;
import io.domainlifecycles.swagger.v3.OpenAPIPropertyBeanValidationExtension;
import io.domainlifecycles.swagger.v3.OpenAPITemporalTypeExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;

/**
 * Open API customizations and extensions for DLC.
 *
 * Provides additional temporal type support, which reflects the Jackson default behaviour.
 * The extension adds Open API schemata for several additional temporal types {@link OpenAPITemporalTypeExtension}.
 * The temporal type extension works as well as for domain object types as for all other classes.
 *
 * By this extension primitive typed properties of Open API schemata are marekd as required {@link OpenAPIPrimitivePropertyExtension}.
 *
 * If a bean validation implementation is provided in the classpath of the target application, then
 * this customization extends all classes which have bean validation annotations on their properties (also non dlc domain object classes, like DTOs)
 * with a corresponding additional open api description, if they are used in controller interfaces which additionally provide Open API documentation {@link OpenAPIPropertyBeanValidationExtension}.
 *
 * Additionally, for all special DLC domain object types (entities, valueObject, identities) some Open API extensions are performed, to make the
 * Open API description of controller interfaces match the default mapping behaviour of the DLC Jackson extension {@link MirrorBasedOpenApiExtension}.
 *
 * A note for the error management of this extension: Any exception or error which happens when trying to
 * modify or extend the Open API behaviour of any class, results in an error log message without stopping the extension or
 * modification of other classes. Also all errors are caught and only reported to the log.
 * So any problem, that arises in an unpredicted way, should never affect the regular application execution, as Open API
 * is primarily used only for additional API documentation purposes.
 *
 * @author Mario Herb
 */
public class DlcOpenApiCustomizer implements OpenApiCustomizer {

    private static final Logger log = LoggerFactory.getLogger(DlcOpenApiCustomizer.class);

    private final SpringDocConfigProperties springDocConfigProperties;

    private final MirrorBasedOpenApiExtension mirrorBasedOpenApiExtension;

    private final String[] entitiesWithExternallyProvidedIds;

    /**
     * Constructor of the DLC Spring Doc based Open API extension.
     *
     * To work correctly, the spring doc configuration property 'springdoc.use-fqn' must be 'true',
     * as this extension relies on full qualified path names of the extended classes.
     *
     * @param springDocConfigProperties configuration for the customizer
     * @param entitiesWithExternallyProvidedIds name of entities which IDs are provided from extern
     */
    public DlcOpenApiCustomizer(
        SpringDocConfigProperties springDocConfigProperties,
                                String... entitiesWithExternallyProvidedIds) {
        this.springDocConfigProperties = springDocConfigProperties;
        this.entitiesWithExternallyProvidedIds = entitiesWithExternallyProvidedIds;
        if(!this.springDocConfigProperties.isUseFqn()){
            throw new RuntimeException("DLC Open API extension requires the spring doc config property 'springdoc.use-fqn' to be 'true'!");
        }
        this.mirrorBasedOpenApiExtension = new MirrorBasedOpenApiExtension();
    }

    /**
     * Central entry point for DLC Open API extensions.
     */
    @Override
    public void customise(OpenAPI openApi) {
        log.debug("Customizing Open API informations with DLC specifica!");
        OpenAPITemporalTypeExtension.extendOpenAPISchemaForTemporalTypes(openApi);
        OpenAPIPrimitivePropertyExtension.extendPrimitiveProperties(openApi);
        OpenAPIPropertyBeanValidationExtension.extendWithBeanValidationInformation(openApi);
        mirrorBasedOpenApiExtension.extendOpenAPISchemaForDLCTypes(openApi, entitiesWithExternallyProvidedIds);

    }

}
