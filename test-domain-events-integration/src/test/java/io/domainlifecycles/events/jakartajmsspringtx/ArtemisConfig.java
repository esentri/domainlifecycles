package io.domainlifecycles.events.jakartajmsspringtx;

import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArtemisConfig implements ArtemisConfigurationCustomizer {
    @Override
    public void customize(org.apache.activemq.artemis.core.config.Configuration configuration) {
        try {
            configuration.addAcceptorConfiguration("remote", "tcp://0.0.0.0:64444");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
