package io.domainlifecycles.springboot.events;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    public TestService testService(){
        return new TestService();
    }
}
