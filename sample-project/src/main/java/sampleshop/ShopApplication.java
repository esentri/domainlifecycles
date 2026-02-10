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

//                      ?
//                    ?_ ? ?
//       __        -  (o)o)  -  __
//   ___/ /__ __ _ ooO'(_)--Ooo/ /  ___  ___
//  / _  / -_)  ' \/ _ \  (_-</ _ \/ _ \/ _ \
//  \_,_/\__/_/_/_/\___/ /___/_//_/\___/ .__/
//                                    /_/

package sampleshop;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.validation.extend.ValidationDomainClassExtender;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Locale;

/**
 * DLC demo application.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
@SpringBootApplication
@EnableDlc
public class ShopApplication {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(ShopApplication.class).run(args);
    }

    /**
     * Enable DLC byte code extension for the domain model of the "sampleshop"
     */
    @PostConstruct
    public void postConstruct() {
        ValidationDomainClassExtender.extend("sampleshop");
    }
}
