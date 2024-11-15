package io.domainlifecycles.jooq.persistence.tests.sharedKernel;

import io.domainlifecycles.jooq.persistence.BasePersistence_ITest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.shared.persistence.domain.shared.another.AnotherConfiguration;
import tests.shared.persistence.domain.shared.another.AnotherConfigurationId;
import tests.shared.persistence.domain.shared.another.TangibleConfigurationTableEntry;
import tests.shared.persistence.domain.shared.another.TangibleConfigurationTableEntryId;
import tests.shared.persistence.domain.shared.one.Configuration;
import tests.shared.persistence.domain.shared.one.ConfigurationId;
import tests.shared.persistence.domain.shared.one.GlobalConfigurationTableEntry;
import tests.shared.persistence.domain.shared.one.GlobalConfigurationTableEntryId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/*
Test-Szenario for shared kernel in DDD

┌───────────────────────────────────────────────┐┌─────────────────────┐┌───────────────────────────────────────────────┐
│                    Context                    ││    Shared Kernel    ││                Another Context
   │
│                                               ││                     ││
   │
│                                               ││                     ││
   │
│      ┌─────────────────────────────────┐      ││                     ││      ┌─────────────────────────────────┐
   │
│      │          Configuration          │      ││                     ││      │      AnotherConfiguration       │
   │
│      └─────────────────────────────────┘      ││                     ││      └─────────────────────────────────┘
   │
│                       ▲                       ││                     ││                       ▲
   │
│                       │                       ││                     ││                       │
   │
│                       │                       ││                     ││                       │
   │
│                       │                       ││                     ││                       │
   │
│                       │                       ││                     ││                       │
   │
│                       │                       ││                     ││                       │
   │
│                       │                       ││                     ││                       │
   │
│      ┌─────────────────────────────────┐      ││   ┌───────────┐     ││      ┌─────────────────────────────────┐
   │
│      │  GlobalConfigurationTableEntry  │◀─────┼┼───│   Entry   │─────┼┼─────▶│ TangibleConfigurationTableEntry │
   │
│      └─────────────────────────────────┘      ││   └───────────┘     ││      └─────────────────────────────────┘
   │
│                                               ││                     ││
   │
│                                               ││                     ││
   │
│                                               ││                     ││
   │
│                                               ││                     ││
   │
└───────────────────────────────────────────────┘└─────────────────────┘└───────────────────────────────────────────────┘
*/
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfigurationRepository_ITest extends BasePersistence_ITest {

    private ConfigurationRepository configurationRepository;

    private AnotherConfigurationRepository anotherConfigurationRepository;

    @BeforeAll
    public void init() {
        configurationRepository = new ConfigurationRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
        anotherConfigurationRepository = new AnotherConfigurationRepository(
            persistenceConfiguration.dslContext,
            persistenceEventTestHelper.testEventPublisher,
            persistenceConfiguration.domainPersistenceProvider
        );
    }

    @Test
    public void testInsert() {
        // Create the configuration (first context which uses shared kernel)
        Configuration configuration = Configuration
            .builder()
            .setId(new ConfigurationId(1L))
            .setConcurrencyVersion(0)
            .setClassA(GlobalConfigurationTableEntry.builder()
                .setId(new GlobalConfigurationTableEntryId(1L))
                .setConfigurationId(new ConfigurationId(1L))
                .setX(73)
                .setY(42)
                .build())
            .build();

        Configuration configurationCopy = persistenceEventTestHelper.kryo.copy(configuration);

        // Insert
        Configuration inserted = configurationRepository.insert(configurationCopy);


        // Create the antoher configuration (second context which uses shared kernel)
        AnotherConfiguration anotherConfiguration = AnotherConfiguration
            .builder()
            .setId(new AnotherConfigurationId(1L))
            .setConcurrencyVersion(0)
            .setClassA(TangibleConfigurationTableEntry.builder()
                .setId(new TangibleConfigurationTableEntryId(1L))
                .setAnotherConfigurationId(new AnotherConfigurationId(1L))
                .setX(73)
                .setY(42)
                .build())
            .build();

        AnotherConfiguration anotherConfigurationCopy = persistenceEventTestHelper.kryo.copy(anotherConfiguration);

        // Insert
        AnotherConfiguration insertedAnother = anotherConfigurationRepository.insert(anotherConfigurationCopy);


        // Fist context
        Optional<Configuration> found = configurationRepository.findResultById(new ConfigurationId(1L)).resultValue();

        Assertions.assertThat(found).isPresent();
        Assertions.assertThat(found.get()).isEqualTo(inserted);

        // Second context
        Optional<AnotherConfiguration> foundAnother = anotherConfigurationRepository
            .findResultById(new AnotherConfigurationId(1L)).resultValue();

        Assertions.assertThat(foundAnother).isPresent();
        Assertions.assertThat(foundAnother.get()).isEqualTo(insertedAnother);
    }

}
