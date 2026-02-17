package io.domainlifecycles.boot3.autoconfig.configurations;

import io.domainlifecycles.boot3.autoconfig.configurations.properties.FeatureProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(FeatureProperties.class)
@Deprecated
public class DlcPropertiesAutoConfiguration {
}
