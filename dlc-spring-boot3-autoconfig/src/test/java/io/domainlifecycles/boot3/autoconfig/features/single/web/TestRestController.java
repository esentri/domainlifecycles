package io.domainlifecycles.boot3.autoconfig.features.single.web;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Profile("test-dlc-rest")
public class TestRestController {

    @GetMapping("/test/{id}")
    public ResponseEntity<String> getId(@PathVariable("id") String id) {
        return ResponseEntity.ok(id);
    }
}
