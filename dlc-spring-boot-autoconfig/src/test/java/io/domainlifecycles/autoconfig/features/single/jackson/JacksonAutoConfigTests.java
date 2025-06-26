package io.domainlifecycles.autoconfig.features.single.jackson;

import com.fasterxml.jackson.core.ObjectCodec;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.api.MappingAction;
import io.domainlifecycles.jackson.databind.context.DomainObjectMappingContext;
import io.domainlifecycles.jackson.module.DlcJacksonModule;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
public class JacksonAutoConfigTests {

    @Autowired
    private DlcJacksonModule dlcJacksonModule;

    @Test
    public void testJacksonMappingSimpleVOOnly() {
        assertThat(dlcJacksonModule.getDependencies().iterator().hasNext()).isTrue();
    }

    @org.springframework.boot.test.context.TestConfiguration
    public static class TestConfiguration {
        @Bean
        JacksonMappingCustomizer<AutoMappedSimpleVo> simpleVoMapping() {
            return new JacksonMappingCustomizer<>(AutoMappedSimpleVo.class) {
                @Override
                public MappingAction beforeObjectRead(DomainObjectMappingContext mappingContext, ObjectCodec codec) {
                    return super.beforeObjectRead(mappingContext, codec);
                }
            };
        }
    }
}


