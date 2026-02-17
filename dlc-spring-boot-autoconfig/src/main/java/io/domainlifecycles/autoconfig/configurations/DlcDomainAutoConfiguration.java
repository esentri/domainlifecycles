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

package io.domainlifecycles.autoconfig.configurations;

import io.domainlifecycles.autoconfig.exception.DLCAutoConfigException;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.serialize.DeserializingDomainMirrorFactory;
import io.domainlifecycles.mirror.serialize.jackson3.JacksonDomainSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * Core domain configuration for enabling standard domain-related features in the DLC framework.
 * This serves as the foundation for all subsequent dependencies in DLC.
 *
 * @author Mario Herb
 * @author Leon Völlinger
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "dlc.features.mirror", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DlcDomainAutoConfiguration implements EnvironmentAware {

    private Environment environment;

    /**
     * Creates and initializes the domain mirror for the DLC framework.
     * The domain mirror is built by scanning the specified base packages for domain objects
     * and creating reflective metadata about the domain structure.
     * <p>
     * This method ensures that the domain is only initialized once and uses either
     * the configuration properties or the annotation value to determine which packages to scan.
     * </p>
     *
     * @return the initialized DomainMirror instance
     * @throws DLCAutoConfigException if the base packages property is missing or invalid
     */
    @Bean
    public DomainMirror initializedDomain() {
        if (!Domain.isInitialized()) {
            String[] basePackages = environment.getProperty("dlc.features.mirror.base-packages", String[].class);
            if (basePackages == null || basePackages.length == 0) {
                var mirrorSerialized = readMirrorFromMetaInfDlc();
                if( mirrorSerialized != null){
                    var serializer = new JacksonDomainSerializer(false);
                    Domain.initialize(new DeserializingDomainMirrorFactory(serializer));
                    return Domain.getDomainMirror();
                }
                throw DLCAutoConfigException.fail(
                        "Property 'basePackages' is missing. Make sure you specified a property called " +
                            "'dlc.features.mirror.base-packages' or add a 'dlcMirrorBasePackages' value on the @EnableDLC annotation.");

            }
            Domain.initialize(new ReflectiveDomainMirrorFactory(basePackages));
        }

        return Domain.getDomainMirror();
    }

    private String readMirrorFromMetaInfDlc() {
        try {
            var resolver = new PathMatchingResourcePatternResolver(getClass().getClassLoader());

            Resource[] resources = resolver.getResources("classpath*:META-INF/dlc/*");

            Resource res = Arrays.stream(resources)
                .filter(Objects::nonNull)
                .filter(Resource::exists)
                .findFirst()
                .orElse(null);

            if (res == null) return null;

            try (var in = res.getInputStream()) {
                String content = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
                return content;
            }
        } catch (Exception e) {
            throw DLCAutoConfigException.fail(
                "Failed reading mirror from 'META-INF/dlc'!");
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
