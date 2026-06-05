package io.domainlifecycles.autoconfig.features.single.jackson;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jackson3.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson3.api.MappingAction;
import io.domainlifecycles.jackson3.databind.context.DomainObjectMappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;
import tools.jackson.core.ObjectReadContext;

@Configuration
public class JacksonAutoConfigTestConfiguration {

    @Bean
    JacksonMappingCustomizer<AutoMappedSimpleVo> simpleVoMapping() {
        return new JacksonMappingCustomizer<>(AutoMappedSimpleVo.class) {
            @Override
            public MappingAction beforeObjectRead(DomainObjectMappingContext mappingContext, ObjectReadContext objectReadContext) {
                return super.beforeObjectRead(mappingContext, objectReadContext);
            }
        };
    }

    @Bean
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }


}
