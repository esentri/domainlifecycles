package io.domainlifecycles.autoconfig.configurations.properties;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.autoconfig.configurations.DlcJooqPersistenceAutoConfiguration;
import org.jooq.SQLDialect;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;

public class JooqPersistenceConfigImportSelector  implements ImportSelector, EnvironmentAware {

    private Environment environment;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attrs = importingClassMetadata
            .getAnnotationAttributes(EnableDlc.class.getName());
        if (attrs != null) {
            String jooqRecordPackage = (String) attrs.get("jooqRecordPackage");
            SQLDialect sqlDialect = (SQLDialect) attrs.get("jooqSqlDialect");
            // Register it in the environment for later use (e.g. by auto-config)
            if (environment instanceof ConfigurableEnvironment) {
                MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
                Map<String, Object> map = new HashMap<>();
                map.put("jooqRecordPackage", jooqRecordPackage);
                map.put("jooqSqlDialect", sqlDialect);
                propertySources.addFirst(new MapPropertySource("enableDlcAnnotation", map));
            }
        }

        // Optionally import additional configuration manually
        return new String[0];
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
