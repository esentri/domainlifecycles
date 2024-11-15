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
    public AutoMappedComplexVo getComplexVO(@NotNull @PathVariable(name = "bestellungId") BestellungId bestellungId,
                                            @NotNull @PathVariable(name = "simpleVo") AutoMappedSimpleVo simpleVo,
                                            @RequestParam(name = "bestellPositionId") BestellPositionId bestellPositionId, @RequestParam(name = "kundennummern") List<Kundennummer> kundennummern) {
        log.debug("BestellungId = {},  simpleVo = {}, bestellPositionId = {}, kundennummern = {}", bestellungId.value(),
            simpleVo, bestellPositionId.value(), kundennummern);
        return AutoMappedComplexVo.builder()
            .setValueA(
                bestellungId.value().toString() + " " + bestellPositionId.value().toString() + " " + kundennummern.stream().map(
                    k -> k.value()).collect(Collectors.joining(" ")))
            .setValueB(simpleVo)
            .build();
    }

    @PostMapping(path = "/testCommand", produces = MediaType.APPLICATION_JSON_VALUE)
    public void testCommand(TestCommand tc) {
        log.debug("TestCommand: " + tc);
    }


}
