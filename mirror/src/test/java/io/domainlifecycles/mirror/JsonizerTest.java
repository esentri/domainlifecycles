/*
 *
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

package io.domainlifecycles.mirror;

import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.serialize.api.JacksonDomainSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class JsonizerTest {

    @BeforeAll
    public static void init(){
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        Domain.initialize(factory);
    }

    @Test
    public void testJsonize() {
        var serializer = new JacksonDomainSerializer(true);
        var result = serializer.serialize(Domain.getInitializedDomain());
        //System.out.println("Serialized: "+result);
        var init = serializer.deserialize(result);
        assertThat(init.boundedContextMirrors()).isEqualTo(Domain.getInitializedDomain().boundedContextMirrors());
        assertThat(init.allTypeMirrors().size()).isEqualTo(Domain.getInitializedDomain().allTypeMirrors().size());
        for(String key : init.allTypeMirrors().keySet()){
            //System.out.println(key);
            assertThat(Domain.getInitializedDomain().allTypeMirrors().containsKey(key)).isTrue();
            assertThat(init.allTypeMirrors().get(key)).isEqualTo(Domain.getInitializedDomain().allTypeMirrors().get(key));
        }
        assertThat(init).isEqualTo(Domain.getInitializedDomain());
        var result2 = serializer.serialize(init);
        assertThat(result).isEqualTo(result2);
    }
}
