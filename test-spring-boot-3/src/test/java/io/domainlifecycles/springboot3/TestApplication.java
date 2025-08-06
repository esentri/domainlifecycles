package io.domainlifecycles.springboot3;

import io.domainlifecycles.autoconfig.annotation.EnableDlc;
import io.domainlifecycles.builder.DomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jooq.configuration.JooqDomainPersistenceConfiguration;
import io.domainlifecycles.jooq.imp.JooqEntityIdentityProvider;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.persistence.mapping.RecordMapper;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import io.domainlifecycles.persistence.records.EntityValueObjectRecordTypeConfiguration;
import io.domainlifecycles.springboot3.config.PersistenceConfig;
import io.domainlifecycles.springdoc2.openapi.DlcOpenApiCustomizer;
import io.domainlifecycles.test.springboot3.tables.records.AktionsCodeBv3Record;
import io.domainlifecycles.validation.extend.ValidationDomainClassExtender;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import tests.shared.complete.onlinehandel.bestellung.AktionsCodeBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.converter.ConverterStringVo;
import tests.shared.jackson.TypeTestValueObject;
import tests.shared.openapi.TestId;
import tests.shared.persistence.domain.optional.OptionalAggregate;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoAggregateRoot;
import tests.shared.validation.javax.ValidatedAggregateRoot;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
@EnableDlc(
    dlcDomainBasePackages =
        "io.domainlifecycles.springboot3," +
            "tests.shared.openapi," +
            "tests.shared.jackson," +
            "tests.shared.converter," +
            "tests.shared.complete.onlinehandel.bestellung," +
            "tests.shared.persistence.domain.optional," +
            "tests.shared.validation.javax," +
            "tests.shared.persistence.domain.simpleUuid," +
            "tests.shared.persistence.domain.simple," +
            "tests.shared.persistence.domain.valueobjectAutoMapping",
    jooqRecordPackage = "io.domainlifecycles.test.springboot3.tables.records"
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
