package io.domainlifecycles.boot3.autoconfig.features.single.jackson;

import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.jackson2.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson2.api.MappingAction;
import io.domainlifecycles.jackson2.databind.context.DomainObjectMappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;
import com.fasterxml.jackson.core.ObjectCodec;

@Configuration
public class JacksonAutoConfigTestConfiguration {

    @Bean
    JacksonMappingCustomizer<AutoMappedSimpleVo> simpleVoMapping() {
        return new JacksonMappingCustomizer<>(AutoMappedSimpleVo.class) {
            @Override
            public MappingAction beforeObjectRead(DomainObjectMappingContext mappingContext, ObjectCodec objectCodec) {
                return super.beforeObjectRead(mappingContext, objectCodec);
            }
        };
    }

    @Bean
    DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
        return new InnerClassDomainObjectBuilderProvider();
    }


}
