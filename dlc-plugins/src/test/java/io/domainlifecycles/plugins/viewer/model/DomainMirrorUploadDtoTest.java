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
 *  Copyright 2019-2026 the original author or authors.
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

package io.domainlifecycles.plugins.viewer.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DomainMirrorUploadDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void embedsDomainMirrorAndDomainCallsAsRawJson() throws Exception {
        DomainMirrorUploadDto dto = new DomainMirrorUploadDto(
            "{\"typeName\":\"my.domain.Order\"}",
            "{\"callsByCaller\":[]}",
            List.of("my.domain"));

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(dto));

        // embedded as a nested JSON object/value, not as an escaped string
        assertThat(json.get("domainMirror").isTextual()).isFalse();
        assertThat(json.get("domainMirror").get("typeName").asText()).isEqualTo("my.domain.Order");
        assertThat(json.get("domainCalls").isTextual()).isFalse();
        assertThat(json.get("domainCalls").get("callsByCaller").isArray()).isTrue();
        assertThat(json.get("domainModelPackages")).isNotNull();
        assertThat(json.get("domainModelPackages").get(0).asText()).isEqualTo("my.domain");
    }

    @Test
    void omitsDomainCallsFieldEntirelyWhenNull() throws Exception {
        DomainMirrorUploadDto dto = new DomainMirrorUploadDto(
            "{\"typeName\":\"my.domain.Order\"}", null, List.of("my.domain"));

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(dto));

        assertThat(json.has("domainCalls")).isFalse();
        assertThat(json.get("domainMirror")).isNotNull();
    }

    @Test
    void gettersReturnConstructorArguments() {
        DomainMirrorUploadDto dto = new DomainMirrorUploadDto("mirrorJson", "callsJson", List.of("a.b"));

        assertThat(dto.getDomainMirror()).isEqualTo("mirrorJson");
        assertThat(dto.getDomainCalls()).isEqualTo("callsJson");
        assertThat(dto.getDomainModelPackages()).containsExactly("a.b");
    }
}
