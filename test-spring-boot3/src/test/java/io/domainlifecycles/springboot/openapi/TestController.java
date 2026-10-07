package io.domainlifecycles.springboot.openapi;

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
import tests.shared.complete.ecommerce.order.OrderItemIdBv3;
import tests.shared.complete.ecommerce.order.OrderBv3;
import tests.shared.complete.ecommerce.order.OrderIdBv3;
import tests.shared.complete.ecommerce.order.CustomerNumberBv3;
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

    @GetMapping(path = "/testOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderBv3 getTestOrder() {
        return TestDataGenerator.buildOrderBv3();
    }

    @GetMapping(path = "/testDto", produces = MediaType.APPLICATION_JSON_VALUE)
    public TestDTO2 getTestDTO() {
        return new TestDTO2();
    }

    @GetMapping(path = "/testComplexVo/{orderId}/{simpleVo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AutoMappedComplexVo getComplexVO(@NotNull @PathVariable(name = "orderId") OrderIdBv3 orderId, @NotNull @PathVariable(name = "simpleVo") AutoMappedSimpleVo simpleVo, @RequestParam(name = "orderItemId") OrderItemIdBv3 orderItemId, @RequestParam(name = "customerNumbers") List<CustomerNumberBv3> customerNumbers) {
        log.debug("OrderId = {},  simpleVo = {}, orderItemId = {}, customerNumbers = {}", orderId.value(),
            simpleVo.getValue(), orderItemId.value(), customerNumbers);
        return AutoMappedComplexVo.builder()
            .setValueA(
                orderId.value().toString() + " " + orderItemId.value().toString() + " " + customerNumbers.stream().map(
                    k -> k.value()).collect(Collectors.joining(" ")))
            .setValueB(simpleVo)
            .build();
    }

    @PostMapping(path = "/testCommand", produces = MediaType.APPLICATION_JSON_VALUE)
    public void testCommand(TestCommand tc) {
        log.debug("TestCommand: " + tc);
    }


}
