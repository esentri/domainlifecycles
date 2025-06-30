package io.domainlifecycles.autoconfig.features.single.jackson;

import com.fasterxml.jackson.core.ObjectCodec;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.api.MappingAction;
import io.domainlifecycles.jackson.databind.context.DomainObjectMappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;

@Configuration
public class JacksonAutoConfigTestConfiguration {

    @Bean
    JacksonMappingCustomizer<AutoMappedSimpleVo> simpleVoMapping() {
        return new JacksonMappingCustomizer<>(AutoMappedSimpleVo.class) {
            @Override
            public MappingAction beforeObjectRead(DomainObjectMappingContext mappingContext, ObjectCodec codec) {
                return super.beforeObjectRead(mappingContext, codec);
            }
        };
    }

    @Bean
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }
}
