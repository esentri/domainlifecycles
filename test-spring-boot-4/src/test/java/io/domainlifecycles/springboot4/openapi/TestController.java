package io.domainlifecycles.springboot4.openapi;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
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
import tests.shared.complete.onlinehandel.bestellung.BestellPositionIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungIdBv3;
import tests.shared.complete.onlinehandel.bestellung.KundennummerBv3;
import tests.shared.openapi.jakarta.TestDTO2;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedComplexVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequiredArgsConstructor
@Slf4j
@Tag(name = "test", description = "Test API")
@RequestMapping("/api")
public class TestController {

    @GetMapping(path = "/testBestellung", produces = MediaType.APPLICATION_JSON_VALUE)
    public BestellungBv3 getTestBestellung() {
        return TestDataGenerator.buildBestellungBv3();
    }

    @GetMapping(path = "/testDto", produces = MediaType.APPLICATION_JSON_VALUE)
    public TestDTO2 getTestDTO() {
        return new TestDTO2();
    }

    @GetMapping(path = "/testComplexVo/{bestellungId}/{simpleVo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AutoMappedComplexVo getComplexVO(@NotNull @PathVariable(name = "bestellungId") BestellungIdBv3 bestellungId, @NotNull @PathVariable(name = "simpleVo") AutoMappedSimpleVo simpleVo, @RequestParam(name = "bestellPositionId") BestellPositionIdBv3 bestellPositionId, @RequestParam(name = "kundennummern") List<KundennummerBv3> kundennummern) {
        log.debug("BestellungId = {},  simpleVo = {}, bestellPositionId = {}, kundennummern = {}", bestellungId.value(),
            simpleVo.getValue(), bestellPositionId.value(), kundennummern);
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
