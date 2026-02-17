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

package io.domainlifecycles.events.springmodulith;

import io.domainlifecycles.events.spring.listeners.AggregateDomainEventAdapter;
import io.domainlifecycles.events.spring.listeners.ServiceKindListenerPostProcessor;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import testdomain.modulith.ADomainService;
import testdomain.modulith.ARepository;

@SpringBootApplication
public class TestApplicationSpringEventsModulith {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestApplicationSpringEventsModulith.class).run(args);
    }

    @Bean
    DomainMirror initializedDomain(){
        Domain.unInitialize();
        Domain.initialize(new ReflectiveDomainMirrorFactory("testdomain.modulith"));
        return Domain.getDomainMirror();
    }
    @Bean
    public ADomainService aDomainService(){
        return new ADomainService();
    }

    @Bean
    public ARepository aRepository(){
        return new ARepository();
    }


    @Bean
    public ServiceKindListenerPostProcessor serviceKindListenerPostProcessor(){
        return new ServiceKindListenerPostProcessor();
    }

    @Bean
    public AggregateDomainEventAdapter aggregateDomainEventAdapter(DomainMirror domainMirror){
        return new AggregateDomainEventAdapter(domainMirror);
    }
}
