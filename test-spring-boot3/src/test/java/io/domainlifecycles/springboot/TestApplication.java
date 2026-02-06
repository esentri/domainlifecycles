package io.domainlifecycles.springboot;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.validation.extend.ValidationDomainClassExtender;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
@EnableDlc(
    dlcDomainBasePackages =
        "io.domainlifecycles.springboot," +
            "tests.shared.openapi," +
            "tests.shared.jackson," +
            "tests.shared.converter," +
            "tests.shared.complete.onlinehandel.bestellung," +
            "tests.shared.persistence.domain.optional," +
            "tests.shared.validation.javax," +
            "tests.shared.persistence.domain.simpleUuid," +
            "tests.shared.persistence.domain.simple," +
            "tests.shared.persistence.domain.valueobjectAutoMapping",
    jooqRecordPackage = "io.domainlifecycles.test.springboot3.tables.records",
    jooqSqlDialect = "H2"
)
@Slf4j
public class TestApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(TestApplication.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        ValidationDomainClassExtender.extend("tests");
        log.info("ValidationExtension Done!");
    }
}
