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

package io.domainlifecycles.springboot2.openapi;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tests.shared.TestDataGenerator;
import tests.shared.openapi.javax.TestDTO;
import tests.shared.persistence.domain.bestellung.bv2.BestellPositionId;
import tests.shared.persistence.domain.bestellung.bv2.Bestellung;
import tests.shared.persistence.domain.bestellung.bv2.BestellungId;
import tests.shared.persistence.domain.bestellung.bv2.Kundennummer;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedComplexVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequiredArgsConstructor
@Slf4j
@Tag(name = "test", description = "Test API")
@RequestMapping("/api")
public class TestController {

    @GetMapping(path = "/testBestellung", produces = MediaType.APPLICATION_JSON_VALUE)
    public Bestellung getTestBestellung() {
        return TestDataGenerator.buildBestellung();
    }

    @GetMapping(path = "/testDto", produces = MediaType.APPLICATION_JSON_VALUE)
    public TestDTO getTestDTO() {
        return new TestDTO();
    }

    @GetMapping(path = "/testComplexVo/{bestellungId}/{simpleVo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AutoMappedComplexVo getComplexVO(@NotNull @PathVariable(name = "bestellungId") BestellungId bestellungId, @NotNull @PathVariable(name = "simpleVo") AutoMappedSimpleVo simpleVo, @RequestParam(name = "bestellPositionId") BestellPositionId bestellPositionId, @RequestParam(name = "kundennummern") List<Kundennummer> kundennummern) {
        log.debug("BestellungId = {},  simpleVo = {}, bestellPositionId = {}, kundennummern = {}", bestellungId.value(), simpleVo, bestellPositionId.value(), kundennummern );
        return AutoMappedComplexVo.builder()
            .setValueA(bestellungId.value().toString() + " " + bestellPositionId.value().toString() + " " + kundennummern.stream().map(k -> k.value()).collect(Collectors.joining(" ")))
            .setValueB(simpleVo)
            .build();
    }

    @PostMapping(path = "/testCommand", produces = MediaType.APPLICATION_JSON_VALUE)
    public void testCommand(TestCommand tc) {
        log.debug("TestCommand: " + tc);
    }


}
